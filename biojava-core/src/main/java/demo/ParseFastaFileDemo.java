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
package demo;

import java.io.File;
import java.io.InputStream;
import java.util.LinkedHashMap;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.io.FastaReader;
import org.biojava.nbio.core.sequence.io.GenericFastaHeaderParser;
import org.biojava.nbio.core.sequence.io.ProteinSequenceCreator;
import org.biojava.nbio.core.util.InputStreamProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by andreas on 6/17/15.
 */
public class ParseFastaFileDemo {

	private static final Logger logger = LoggerFactory.getLogger(ParseFastaFileDemo.class);

	public ParseFastaFileDemo() {

	}

	/**
	 * e.g. download
	 * ftp://ftp.uniprot.org/pub/databases/uniprot/current_release/knowledgebase/complete/uniprot_trembl.fasta.gz
	 * and pass in path to local location of file
	 *
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		int mb = 1024 * 1024;

		// Getting the runtime reference from system
		Runtime runtime = Runtime.getRuntime();

		logger.info("##### Heap utilization statistics [MB] #####");

		// Print used memory
		logger.info("Used Memory:" + (runtime.totalMemory() - runtime.freeMemory()) / mb);

		// Print free memory
		logger.info("Free Memory:" + runtime.freeMemory() / mb);

		// Print total available memory
		logger.info("Total Memory:" + runtime.totalMemory() / mb);

		// Print Maximum available memory
		logger.info("Max Memory:" + runtime.maxMemory() / mb);

		if (args.length < 1) {
			logger.error("First argument needs to be path to fasta file");
			return;
		}

		File f = new File(args[0]);

		if (!f.exists()) {
			logger.error("File does not exist " + args[0]);
			return;
		}

		long timeS = System.currentTimeMillis();

		// automatically uncompress files using InputStreamProvider
		InputStreamProvider isp = new InputStreamProvider();

		InputStream inStream = isp.getInputStream(f);

		FastaReader<ProteinSequence, AminoAcidCompound> fastaReader = new FastaReader<>(inStream,
				new GenericFastaHeaderParser<ProteinSequence, AminoAcidCompound>(),
				new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet()));

		LinkedHashMap<String, ProteinSequence> b;

		int nrSeq = 0;

		while ((b = fastaReader.process(100)) != null) {
			for (String key : b.keySet()) {
				nrSeq++;
				logger.info(new StringBuilder().append(nrSeq).append(" : ").append(key).append(" ").append(b.get(key))
						.toString());
				if (nrSeq % 100000 == 0) {
					logger.info(String.valueOf(nrSeq));
				}
			}

		}
		long timeE = System.currentTimeMillis();
		logger.info(new StringBuilder().append("parsed a total of ").append(nrSeq).append(" TREMBL sequences! in ")
				.append(timeE - timeS).toString());
	}
}
