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
package org.biojava.nbio.survival.data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://www.javamex.com/tutorials/memory/ascii_charsequence.shtml
 * 
 * @author Scooter Willis <willishf at gmail dot com>
 */
public class CompactCharSequence implements CharSequence, Serializable {

	private static final Logger logger = LoggerFactory.getLogger(CompactCharSequence.class);
	static final long serialVersionUID = 1L;
	private static final String ENCODING = "ISO-8859-1";
	private final int offset;
	private final int end;
	private final byte[] data;
	private final boolean nullstring;

	/**
	 *
	 * @param str
	 */
	public CompactCharSequence(String str) {
		try {
			if (str != null) {
				data = str.getBytes(ENCODING);
				offset = 0;
				end = data.length;
				nullstring = false;
			} else {
				data = new byte[0];
				offset = 0;
				end = 0;
				nullstring = true;
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(
					new StringBuilder().append("Unexpected: ").append(ENCODING).append(" not supported!").toString());
		}
	}

	private CompactCharSequence(byte[] data, int offset, int end) {
		this.data = data;
		this.offset = offset;
		this.end = end;
		nullstring = false;
	}

	@Override
	public char charAt(int index) {
		int ix = index + offset;
		if (ix >= end) {
			throw new StringIndexOutOfBoundsException(new StringBuilder().append("Invalid index ").append(index)
					.append(" length ").append(length()).toString());
		}
		return (char) (data[ix] & 0xff);
	}

	@Override
	public int length() {
		return end - offset;
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		if (start < 0 || end >= (this.end - offset)) {
			throw new IllegalArgumentException(new StringBuilder().append("Illegal range ").append(start).append("-")
					.append(end).append(" for sequence of length ").append(length()).toString());
		}
		return new CompactCharSequence(data, start + offset, end + offset);
	}

	@Override
	public String toString() {
		try {
			if (nullstring) {
				return null;
			} else {
				if (length() == 0) {
					return "";
				} else {
					return new String(data, offset, end - offset, ENCODING);
				}
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(
					new StringBuilder().append("Unexpected: ").append(ENCODING).append(" not supported").toString());
		}
	}
}
