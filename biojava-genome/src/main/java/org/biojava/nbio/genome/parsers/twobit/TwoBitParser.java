/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 */
package org.biojava.nbio.genome.parsers.twobit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;

/**
 * downloaded from http://storage.bioinf.fbb.msu.ru/~roman/TwoBitParser.java
 *
 * Class is a parser of UCSC Genome Browser file format .2bit used to store
 * nucleotide sequence information. This class extends InputStream and can be
 * used as it after choosing one of names of containing sequences. This parser
 * can be used to do some work like UCSC tool named twoBitToFa. For it just run
 * this class with input file path as single parameter and set stdout stream
 * into output file. If you have any problems or ideas don't hesitate to contact
 * me through email: rsutormin[at]gmail.com.
 * 
 * @author Roman Sutormin
 */
public class TwoBitParser extends InputStream {

	private static final Logger logger = LoggerFactory.getLogger(TwoBitParser.class);

	//
	private static final char[] bit_chars = { 'T', 'C', 'A', 'G' };

	public int DEFAULT_BUFFER_SIZE = 10000;

	//
	private RandomAccessFile raf;

	private File f;

	private boolean reverse = false;

	private String[] seqNames;

	private HashMap<String, Long> seq2pos = new HashMap<>();

	private String curSeqName;

	private long[][] curNnBlocks;

	private long[][] curMaskBlocks;

	private long curSeqPos;

	private long curDnaSize;

	private int curNnBlockNum;

	private int curMaskBlockNum;

	private int[] curBits;

	private byte[] buffer;

	private long bufferSize;

	private long bufferPos;

	private long startFilePos;

	private long filePos;

	public TwoBitParser(File f) throws Exception {
		this.f = f;
		raf = new RandomAccessFile(f, "r");
		long sign = readFourBytes();
		if (sign == 0x1A412743) {
			logger.debug("2bit: Normal number architecture");
		} else if (sign == 0x4327411A) {
			reverse = true;
			logger.debug("2bit: Reverse number architecture");
		} else {
			throw new Exception("Wrong start signature in 2BIT format");
		}
		readFourBytes();
		int seq_qnt = (int) readFourBytes();
		readFourBytes();
		seqNames = new String[seq_qnt];
		for (int i = 0; i < seq_qnt; i++) {
			int name_len = raf.read();
			char[] chars = new char[name_len];
			for (int j = 0; j < name_len; j++) {
				chars[j] = (char) raf.read();
			}
			seqNames[i] = new String(chars);
			long pos = readFourBytes();
			seq2pos.put(seqNames[i], pos);
			logger.debug("2bit: Sequence name=[{}], pos={}", seqNames[i], pos);
		}
	}

	private long readFourBytes() throws Exception {
		long ret = 0;
		if (!reverse) {
			ret = raf.read();
			ret += raf.read() * 0x100;
			ret += raf.read() * 0x10000;
			ret += raf.read() * 0x1000000;
		} else {
			ret = raf.read() * 0x1000000;
			ret += raf.read() * 0x10000;
			ret += raf.read() * 0x100;
			ret += raf.read();
		}
		return ret;
	}

	public String[] getSequenceNames() {
		String[] ret = new String[seqNames.length];
		System.arraycopy(seqNames, 0, ret, 0, seqNames.length);
		return ret;
	}

	/**
	 * Method open nucleotide stream for sequence with given name.
	 * 
	 * @param seq_name name of sequence (one of returned by getSequenceNames()).
	 * @throws Exception
	 */
	public void setCurrentSequence(String seq_name) throws Exception {
		if (curSeqName != null) {
			throw new Exception(
					new StringBuilder().append("Sequence [").append(curSeqName).append("] was not closed").toString());
		}
		if (seq2pos.get(seq_name) == null) {
			throw new Exception(new StringBuilder().append("Sequence [").append(seq_name)
					.append("] was not found in 2bit file").toString());
		}
		curSeqName = seq_name;
		long pos = seq2pos.get(seq_name);
		raf.seek(pos);
		long dna_size = readFourBytes();
		logger.debug("2bit: Sequence name=[{}], dna_size={}", curSeqName, dna_size);
		curDnaSize = dna_size;
		int nn_block_qnt = (int) readFourBytes();
		curNnBlocks = new long[nn_block_qnt][2];
		for (int i = 0; i < nn_block_qnt; i++) {
			curNnBlocks[i][0] = readFourBytes();
		}
		for (int i = 0; i < nn_block_qnt; i++) {
			curNnBlocks[i][1] = readFourBytes();
		}

		for (int i = 0; i < nn_block_qnt; i++) {
			logger.debug("NN-block: [{},{}] ", curNnBlocks[i][0], curNnBlocks[i][1]);
		}

		int mask_block_qnt = (int) readFourBytes();
		curMaskBlocks = new long[mask_block_qnt][2];
		for (int i = 0; i < mask_block_qnt; i++) {
			curMaskBlocks[i][0] = readFourBytes();
		}
		for (int i = 0; i < mask_block_qnt; i++) {
			curMaskBlocks[i][1] = readFourBytes();
		}

		for (int i = 0; i < mask_block_qnt; i++) {
			logger.debug("[{},{}] ", curMaskBlocks[i][0], curMaskBlocks[i][1]);
		}

		readFourBytes();
		startFilePos = raf.getFilePointer();
		reset();
	}

	/**
	 * Method resets current position to the begining of sequence stream.
	 */
	@Override
	public synchronized void reset() throws IOException {
		curSeqPos = 0;
		curNnBlockNum = (curNnBlocks.length > 0) ? 0 : -1;
		curMaskBlockNum = (curMaskBlocks.length > 0) ? 0 : -1;
		curBits = new int[4];
		filePos = startFilePos;
		bufferSize = 0;
		bufferPos = -1;
	}

	/**
	 * @return number (starting from 0) of next readable nucleotide in sequence
	 *         stream.
	 */
	public long getCurrentSequencePosition() {
		if (curSeqName == null) {
			throw new RuntimeException("Sequence is not set");
		}
		return curSeqPos;
	}

	public void setCurrentSequencePosition(long pos) throws IOException {
		if (curSeqName == null) {
			throw new RuntimeException("Sequence is not set");
		}
		if (pos > curDnaSize) {
			throw new RuntimeException(new StringBuilder().append("Postion is too high (more than ").append(curDnaSize)
					.append(")").toString());
		}
		if (curSeqPos > pos) {
			reset();
		}
		skip(pos - curSeqPos);
	}

	private void loadBits() throws IOException {
		if ((buffer == null) || (bufferPos < 0) || (filePos < bufferPos) || (filePos >= bufferPos + bufferSize)) {
			if ((buffer == null) || (buffer.length != DEFAULT_BUFFER_SIZE)) {
				buffer = new byte[DEFAULT_BUFFER_SIZE];
			}
			bufferPos = filePos;
			bufferSize = raf.read(buffer);
		}
		int cur_byte = buffer[(int) (filePos - bufferPos)] & 0xff;
		for (int i = 0; i < 4; i++) {
			curBits[3 - i] = cur_byte % 4;
			cur_byte /= 4;
		}
	}

	/**
	 * Method reads 1 nucleotide from sequence stream. You should set current
	 * sequence before use it.
	 */
	@Override
	public int read() throws IOException {
		if (curSeqName == null) {
			throw new IOException("Sequence is not set");
		}
		if (curSeqPos == curDnaSize) {
			logger.debug("End of sequence (file position:{})", raf.getFilePointer());
			return -1;
		}
		int bit_num = (int) curSeqPos % 4;
		if (bit_num == 0) {
			loadBits();
		} else if (bit_num == 3) {
			filePos++;
		}
		char ret = 'N';
		if ((curNnBlockNum >= 0) && (curNnBlocks[curNnBlockNum][0] <= curSeqPos)) {
			if (curBits[bit_num] != 0) {
				throw new IOException(new StringBuilder().append("Wrong data in NN-block (").append(curBits[bit_num])
						.append(") ").append("at position ").append(curSeqPos).toString());
			}
			if (curNnBlocks[curNnBlockNum][0] + curNnBlocks[curNnBlockNum][1] == curSeqPos + 1) {
				curNnBlockNum++;
				if (curNnBlockNum >= curNnBlocks.length) {
					curNnBlockNum = -1;
				}
			}
			ret = 'N';
		} else {
			ret = bit_chars[curBits[bit_num]];
		}
		if ((curMaskBlockNum >= 0) && (curMaskBlocks[curMaskBlockNum][0] <= curSeqPos)) {
			ret = Character.toLowerCase(ret);
			if (curMaskBlocks[curMaskBlockNum][0] + curMaskBlocks[curMaskBlockNum][1] == curSeqPos + 1) {
				curMaskBlockNum++;
				if (curMaskBlockNum >= curMaskBlocks.length) {
					curMaskBlockNum = -1;
				}
			}
		}
		curSeqPos++;
		return ret;
	}

	/**
	 * Method skips n nucleotides in sequence stream. You should set current
	 * sequence before use it.
	 */
	@Override
	public synchronized long skip(long n) throws IOException {
		if (curSeqName == null) {
			throw new IOException("Sequence is not set");
		}
		if (n < 4) {
			int ret = 0;
			while ((ret < n) && (read() >= 0)) {
				ret++;
			}
			return ret;
		}
		if (n > curDnaSize - curSeqPos) {
			n = curDnaSize - curSeqPos;
		}
		curSeqPos += n;
		filePos = startFilePos + (curSeqPos / 4);
		raf.seek(filePos);
		if ((curSeqPos % 4) != 0) {
			loadBits();
		}
		while ((curNnBlockNum >= 0) && (curNnBlocks[curNnBlockNum][0] + curNnBlocks[curNnBlockNum][1] <= curSeqPos)) {
			curNnBlockNum++;
			if (curNnBlockNum >= curNnBlocks.length) {
				curNnBlockNum = -1;
			}
		}
		while ((curMaskBlockNum >= 0)
				&& (curMaskBlocks[curMaskBlockNum][0] + curMaskBlocks[curMaskBlockNum][1] <= curSeqPos)) {
			curMaskBlockNum++;
			if (curMaskBlockNum >= curMaskBlocks.length) {
				curMaskBlockNum = -1;
			}
		}
		return n;
	}

	/**
	 * Method closes current sequence and it's necessary to invoke it before setting
	 * new current sequence.
	 */
	@Override
	public void close() throws IOException {
		curSeqName = null;
		curNnBlocks = null;
		curMaskBlocks = null;
		curSeqPos = -1;
		curDnaSize = -1;
		curNnBlockNum = -1;
		curMaskBlockNum = -1;
		curBits = null;
		bufferSize = 0;
		bufferPos = -1;
		filePos = -1;
		startFilePos = -1;
	}

	@Override
	public int available() throws IOException {
		if (curSeqName == null) {
			throw new IOException("Sequence is not set");
		}
		return (int) (curDnaSize - curSeqPos);
	}

	/**
	 * Method closes random access file descriptor. You can't use any reading
	 * methods after it.
	 * 
	 * @throws Exception
	 */
	public void closeParser() throws Exception {
		raf.close();
	}

	public File getFile() {
		return f;
	}

	public String loadFragment(long seq_pos, int len) throws IOException {
		if (curSeqName == null) {
			throw new IOException("Sequence is not set");
		}
		setCurrentSequencePosition(seq_pos);
		char[] ret = new char[len];
		int i = 0;
		for (; i < len; i++) {
			int ch = read();
			if (ch < 0) {
				break;
			}
			ret[i] = (char) ch;
		}
		return new String(ret, 0, i);
	}

	public void printFastaSequence() throws IOException {
		if (curSeqName == null) {
			throw new RuntimeException("Sequence is not set");
		}
		printFastaSequence(curDnaSize - curSeqPos);
	}

	public void printFastaSequence(long len) throws IOException {
		if (curSeqName == null) {
			throw new RuntimeException("Sequence is not set");
		}
		logger.info(">{} pos={}, len={}", curSeqName, curSeqPos, len);
		char[] line = new char[60];
		boolean end = false;
		long qnt_all = 0;
		while (!end) {
			int qnt = 0;
			for (; (qnt < line.length) && (qnt_all < len); qnt++, qnt_all++) {
				int ch = read();
				if (ch < 0) {
					end = true;
					break;
				}
				line[qnt] = (char) ch;
			}
			if (qnt > 0) {
				logger.info(new String(line, 0, qnt));
			}
			if (qnt_all >= len) {
				end = true;
			}
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			logger.info("Usage: <program> <input.2bit> [<seq_name> [<start> [<length>]]]");
			logger.info("Resulting fasta data will be written in stdout.");
			return;
		}
		TwoBitParser p = new TwoBitParser(new File(args[0]));
		if (args.length == 1) {
			String[] names = p.getSequenceNames();
			for (String name1 : names) {
				p.setCurrentSequence(name1);
				p.printFastaSequence();
				p.close();
			}
		} else {
			String name = args[1];
			p.setCurrentSequence(name);
			if (args.length > 2) {
				long start = Long.parseLong(args[2]);
				p.skip(start);
			}
			if (args.length > 3) {
				long len = Long.parseLong(args[3]);
				p.printFastaSequence(len);
			} else {
				p.printFastaSequence();
			}
			p.close();
		}
		p.closeParser();
	}
}
