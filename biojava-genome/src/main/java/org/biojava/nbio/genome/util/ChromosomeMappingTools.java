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
package org.biojava.nbio.genome.util;

import com.google.common.collect.Range;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.template.SequenceView;
import org.biojava.nbio.genome.parsers.genename.ChromPos;
import org.biojava.nbio.genome.parsers.genename.GeneChromosomePosition;
import org.biojava.nbio.genome.parsers.twobit.TwoBitFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that can map chromosomal positions to mRNA (coding sequence)
 * positions.
 *
 * @author Andreas Prlic
 */

public class ChromosomeMappingTools {

	private static final Logger logger = LoggerFactory.getLogger(ChromosomeMappingTools.class);

	private static final String newline = System.getProperty("line.separator");

	public static final String CHROMOSOME = "CHROMOSOME";
	public static final String CDS = "CDS";

	private static int base = 1;

	public static void setCoordinateSystem(int baseInt) {
		base = baseInt;
	}

	/**
	 * Pretty print the details of a GeneChromosomePosition to a String
	 *
	 * @param chromosomePosition
	 * @return
	 */
	public static String formatExonStructure(GeneChromosomePosition chromosomePosition) {
		if (chromosomePosition.getOrientation() == '+') {
			return formatExonStructureForward(chromosomePosition);
		}
		return formatExonStructureReverse(chromosomePosition);
	}

	private static String formatExonStructureForward(GeneChromosomePosition chromPos) {

		StringWriter s = new StringWriter();

		List<Integer> exonStarts = chromPos.getExonStarts();
		List<Integer> exonEnds = chromPos.getExonEnds();

		int cdsStart = chromPos.getCdsStart();
		int cdsEnd = chromPos.getCdsEnd();

		boolean inCoding = false;
		int codingLength = 0;

		for (int i = 0; i < exonStarts.size(); i++) {

			int start = exonStarts.get(i);
			int end = exonEnds.get(i);

			if (start <= cdsStart + 1 && end >= cdsStart + 1) {

				inCoding = true;
				codingLength += (end - cdsStart);
				s.append("     UTR         : ").append(format(start)).append(" - ").append(format(cdsStart));
				s.append(newline);
				s.append(" ->  Exon        : ").append(format(cdsStart + 1)).append(" - ").append(format(end))
						.append(" | ").append(Integer.toString(end - cdsStart)).append(" | ")
						.append(Integer.toString(codingLength)).append(" | ")
						.append(Integer.toString(codingLength % 3));
				s.append(newline);

			} else if (start <= cdsEnd && end >= cdsEnd) {
				// logger.debug(" <-- CDS end at: " + cdsEnd );
				inCoding = false;
				codingLength += (cdsEnd - start);

				s.append(" <-  Exon        : ").append(format(start + 1)).append(" - ").append(format(cdsEnd))
						.append(" | ").append(Integer.toString(cdsEnd - start)).append(" | ")
						.append(Integer.toString(codingLength)).append(" | ")
						.append(Integer.toString(codingLength % 3));
				s.append(newline);
				s.append(new StringBuilder().append("     UTR         : ").append(cdsEnd + 1).append(" - ")
						.append(format(end)).toString());
				s.append(newline);

			} else if (inCoding) {
				// full exon is coding
				codingLength += (end - start);

				s.append("     Exon        : ").append(format(start + 1)).append(" - ").append(format(end))
						.append(" | ").append(Integer.toString(end - start)).append(" | ")
						.append(Integer.toString(codingLength)).append(" | ")
						.append(Integer.toString(codingLength % 3));
				s.append(newline);
			}
		}
		s.append("Coding Length: ");
		s.append(Integer.toString((codingLength - 3)));
		s.append(newline);
		return s.toString();
	}

	private static String formatExonStructureReverse(GeneChromosomePosition chromPos) {
		StringWriter s = new StringWriter();

		List<Integer> exonStarts = chromPos.getExonStarts();
		List<Integer> exonEnds = chromPos.getExonEnds();

		int cdsStart = chromPos.getCdsStart();
		int cdsEnd = chromPos.getCdsEnd();

		// logger.debug("CDS START:" +format(cdsStart) + " - " + format(cdsEnd));

		boolean inCoding = false;
		int codingLength = 0;

		if (cdsEnd < cdsStart) {
			int tmp = cdsEnd;
			cdsEnd = cdsStart;
			cdsStart = tmp;
		}

		// map reverse
		for (int i = exonStarts.size() - 1; i >= 0; i--) {

			int end = exonStarts.get(i);
			int start = exonEnds.get(i);

			if (end < start) {
				int tmp = end;
				end = start;
				start = tmp;
			}

			if (start <= cdsEnd && end >= cdsEnd) {
				inCoding = true;

				int tmpstart = start;
				if (start < cdsStart) {
					tmpstart = cdsStart;
				}
				codingLength += (cdsEnd - tmpstart);

				s.append(new StringBuilder().append("     UTR         :").append(format(cdsEnd + 1)).append(" | ")
						.append(format(end)).toString());
				s.append(newline);
				if (tmpstart == start) {
					s.append(" ->  ");
				} else {
					s.append(" <-> ");
				}
				s.append("Exon        :").append(format(tmpstart + 1)).append(" - ").append(format(cdsEnd))
						.append(" | ").append(Integer.toString(cdsEnd - tmpstart)).append(" | ")
						.append(Integer.toString(codingLength)).append(" | ")
						.append(Integer.toString(codingLength % 3));
				s.append(newline);
				// single exon with UTR on both ends
				if (tmpstart != start) {
					s.append(new StringBuilder().append("     UTR         :").append(format(cdsStart)).append(" - ")
							.append(format(start + 1)).toString());
				}
				s.append(newline);

			} else if (start <= cdsStart && end >= cdsStart) {
				inCoding = false;
				codingLength += (end - cdsStart);

				s.append(new StringBuilder().append(" <-  Exon        : ").append(format(cdsStart + 1)).append(" - ")
						.append(format(end)).append(" | ").append(end - cdsStart).append(" | ").append(codingLength)
						.append(" | ").append(codingLength % 3).toString());
				s.append(newline);
				s.append(new StringBuilder().append("     UTR         : ").append(format(start + 1)).append(" - ")
						.append(format(cdsStart)).toString());
				s.append(newline);

			} else if (inCoding) {
				// full exon is coding
				codingLength += (end - start);

				s.append(new StringBuilder().append("     Exon        : ").append(format(start + 1)).append(" - ")
						.append(format(end)).append(" | ").append(end - start).append(" | ").append(codingLength)
						.append(" | ").append(codingLength % 3).toString());
				s.append(newline);
			} else {
				// e.g. see UBQLN3
				s.append(" no translation! UTR: ").append(format(start)).append(" - ").append(format(end));
				s.append(newline);
			}
		}

		s.append("CDS length: ").append(Integer.toString(codingLength - 3));
		s.append(newline);

		return s.toString();
	}

	/**
	 * Get the length of the CDS in nucleotides.
	 *
	 * @param chromPos
	 * @return length of the CDS in nucleotides.
	 */
	public static int getCDSLength(GeneChromosomePosition chromPos) {

		List<Integer> exonStarts = chromPos.getExonStarts();
		List<Integer> exonEnds = chromPos.getExonEnds();

		int cdsStart = chromPos.getCdsStart();
		int cdsEnd = chromPos.getCdsEnd();

		int codingLength;
		if (chromPos.getOrientation().equals('+')) {
			codingLength = ChromosomeMappingTools.getCDSLengthForward(exonStarts, exonEnds, cdsStart, cdsEnd);
		} else {
			codingLength = ChromosomeMappingTools.getCDSLengthReverse(exonStarts, exonEnds, cdsStart, cdsEnd);
		}
		return codingLength;
	}

	/**
	 * Maps the position of a CDS nucleotide back to the genome
	 *
	 * @param cdsNucleotidePosition
	 * @return a ChromPos object
	 */
	public static ChromPos getChromosomePosForCDScoordinate(int cdsNucleotidePosition,
			GeneChromosomePosition chromPos) {

		logger.debug(" ? Checking chromosome position for CDS position " + cdsNucleotidePosition);

		List<Integer> exonStarts = chromPos.getExonStarts();
		List<Integer> exonEnds = chromPos.getExonEnds();

		logger.debug(" Exons:" + exonStarts.size());

		int cdsStart = chromPos.getCdsStart();
		int cdsEnd = chromPos.getCdsEnd();

		ChromPos chromosomePos = null;

		if (chromPos.getOrientation().equals('+')) {
			chromosomePos = ChromosomeMappingTools.getChromPosForward(cdsNucleotidePosition, exonStarts, exonEnds,
					cdsStart, cdsEnd);
		} else {
			chromosomePos = ChromosomeMappingTools.getChromPosReverse(cdsNucleotidePosition, exonStarts, exonEnds,
					cdsStart, cdsEnd);
		}

		logger.debug(new StringBuilder().append("=> CDS pos ").append(cdsNucleotidePosition).append(" for ")
				.append(chromPos.getGeneName()).append(" is on chromosome at  ").append(chromosomePos).toString());
		return chromosomePos;

	}

	/**
	 * Returns a nicely formatted representation of the position
	 *
	 * @param chromosomePosition
	 * @return
	 */
	private static String format(int chromosomePosition) {
		return String.format("%,d", chromosomePosition);
	}

	/**
	 * Get the CDS position mapped on the chromosome position
	 *
	 * @param exonStarts
	 * @param exonEnds
	 * @param cdsStart
	 * @param cdsEnd
	 * @return
	 */
	public static ChromPos getChromPosReverse(int cdsPos, List<Integer> exonStarts, List<Integer> exonEnds,
			int cdsStart, int cdsEnd) {

		boolean inCoding = false;
		int codingLength = 0;

		if (cdsEnd < cdsStart) {
			int tmp = cdsEnd;
			cdsEnd = cdsStart;
			cdsStart = tmp;
		}

		int lengthExons = 0;

		// map reverse
		for (int i = exonStarts.size() - 1; i >= 0; i--) {

			logger.debug(new StringBuilder().append("Exon #").append(i + 1).append("/").append(exonStarts.size())
					.toString());
			int end = exonStarts.get(i);
			int start = exonEnds.get(i);

			if (end < start) {
				int tmp = end;
				end = start;
				start = tmp;
			}
			lengthExons += end - start;

			logger.debug(new StringBuilder().append("     is ").append(cdsPos).append(" part of Reverse exon? ")
					.append(format(start + 1)).append(" - ").append(format(end)).append(" | ").append(end - start + 1)
					.toString());
			logger.debug(new StringBuilder().append("     CDS start: ").append(format(cdsStart + 1)).append("-")
					.append(format(cdsEnd)).append(" coding length counter:").append(codingLength).toString());

			if (start + 1 <= cdsEnd && end >= cdsEnd) {

				// FIRST EXON
				inCoding = true;

				int tmpstart = start;
				if (start < cdsStart) {
					tmpstart = cdsStart;
				}

				// here one of the few places where we don't say start+1
				int check = codingLength + cdsEnd - tmpstart;

				logger.debug(new StringBuilder().append("First Exon    | ").append(check).append(" | ")
						.append(format(start + 1)).append(" ").append(format(end)).append(" | ")
						.append(cdsEnd - tmpstart).append(" | ").append(cdsPos).toString());

				if ((check > cdsPos)) {
					int tmp = cdsPos - codingLength;
					logger.debug(new StringBuilder().append(" -> found position in UTR exon:  ").append(format(cdsPos))
							.append(" ").append(format(tmpstart + 1)).append(" tmp:").append(format(tmp)).append(" cs:")
							.append(format(cdsStart + 1)).append(" ce:").append(format(cdsEnd)).append(" cl:")
							.append(codingLength).toString());
					return new ChromPos((cdsEnd - tmp), -1);
				}

				// don't add 1 here
				codingLength += (cdsEnd - tmpstart);

				boolean debug = logger.isDebugEnabled();

				if (debug) {

					StringBuilder b = new StringBuilder();

					b.append(new StringBuilder().append("     UTR         :").append(format(cdsEnd + 1)).append(" - ")
							.append(format(end)).append(newline).toString());
					if (tmpstart == start) {
						b.append(" ->  ");
					} else {
						b.append(" <-> ");
					}
					b.append(new StringBuilder().append("Exon        :").append(format(tmpstart + 1)).append(" - ")
							.append(cdsEnd).append(" | ").append(format(cdsEnd - tmpstart + 1)).append(" - ")
							.append(codingLength).append(" | ").append(codingLength % 3).append(newline).toString());

					// single exon with UTR on both ends
					if (tmpstart != start) {
						b.append(new StringBuilder().append("     UTR         :").append(format(cdsStart)).append(" - ")
								.append(format(start + 1)).append(newline).toString());
					}

					logger.debug(b.toString());
				}
			} else if (start <= cdsStart && end >= cdsStart) {

				// LAST EXON
				inCoding = false;

				if (codingLength + end - cdsStart >= cdsPos) {

					// how many remaining coding nucleotides?
					int tmp = codingLength + end - cdsStart - cdsPos;

					logger.debug(new StringBuilder().append("cdl: ").append(codingLength).append(" tmp:").append(tmp)
							.append(" cdsStart: ").append(format(cdsStart)).toString());

					logger.debug(new StringBuilder().append(" -> XXX found position noncoding exon:  cdsPos:")
							.append(cdsPos).append(" s:").append(format(start + 1)).append(" tmp:").append(format(tmp))
							.append(" cdsStart:").append(cdsStart + 1).append(" codingLength:").append(codingLength)
							.append(" cdsEnd:").append(format(cdsEnd)).toString());

					return new ChromPos((cdsStart + tmp), -1);
				}

				codingLength += (end - cdsStart);

				logger.debug(new StringBuilder().append(" <-  Exon        : ").append(format(cdsStart + 1))
						.append(" - ").append(format(end)).append(" | ").append(format(end - cdsStart + 1))
						.append(" | ").append(codingLength).append(" | ").append(codingLength % 3).toString());
				logger.debug(new StringBuilder().append("     UTR         : ").append(format(start + 1)).append(" - ")
						.append(format(cdsStart)).toString());

			} else if (inCoding) {

				if (codingLength + end - start - 1 >= cdsPos) {

					int tmp = cdsPos - codingLength;

					if (tmp > (end - start)) {
						tmp = (end - start);
						logger.debug("changing tmp to " + tmp);
					}
					logger.debug(new StringBuilder().append("     ").append(cdsPos).append(" ").append(codingLength)
							.append(" | ").append(cdsPos - codingLength).append(" | ").append(end - start).append(" | ")
							.append(tmp).toString());
					logger.debug(new StringBuilder().append("     Exon        : ").append(format(start + 1))
							.append(" - ").append(format(end)).append(" | ").append(format(end - start)).append(" | ")
							.append(codingLength).append(" | ").append(codingLength % 3).toString());
					logger.debug(new StringBuilder().append(" ->  RRR found position coding exon:  ").append(cdsPos)
							.append(" ").append(format(start + 1)).append(" ").append(format(end)).append(" ")
							.append(tmp).append(" ").append(format(cdsStart + 1)).append(" ").append(codingLength)
							.toString());

					return new ChromPos((end - tmp), cdsPos % 3);
				}
				// full exon is coding
				codingLength += (end - start);

				logger.debug(new StringBuilder().append("     Exon        : ").append(format(start + 1)).append(" - ")
						.append(format(end)).append(" | ").append(format(end - start + 1)).append(" | ")
						.append(codingLength).append(" | ").append(codingLength % 3).toString());
			} else {
				// e.g. see UBQLN3
				logger.debug(" no translation!");
			}
			logger.debug(new StringBuilder().append("     coding length: ").append(codingLength).append("(phase:")
					.append(codingLength % 3).append(") CDS POS trying to map:").append(cdsPos).toString());
		}

		logger.debug("length exons: " + lengthExons);
		// could not map, or map over the full length??
		return new ChromPos(-1, -1);

	}

	/**
	 * Get the CDS position mapped onto the chromosome position
	 *
	 * @param exonStarts
	 * @param exonEnds
	 * @param cdsStart
	 * @param cdsEnd
	 * @return
	 */
	public static ChromPos getChromPosForward(int cdsPos, List<Integer> exonStarts, List<Integer> exonEnds,
			int cdsStart, int cdsEnd) {
		boolean inCoding = false;
		int codingLength = 0;

		@SuppressWarnings("unused")
		int lengthExons = 0;
		// map forward
		for (int i = 0; i < exonStarts.size(); i++) {

			// start can include UTR
			int start = exonStarts.get(i);
			int end = exonEnds.get(i);

			lengthExons += end - start;

			if (start <= cdsStart + 1 && end >= cdsStart + 1) {
				// first exon with UTR
				if (codingLength + (end - cdsStart - 1) >= cdsPos) {
					// we are reaching our target position
					int tmp = cdsPos - codingLength;

					logger.debug(new StringBuilder().append(cdsStart).append(" | ").append(codingLength).append(" | ")
							.append(tmp).toString());
					logger.debug(new StringBuilder().append(" -> found position in UTR exon:  #").append(i + 1)
							.append(" cdsPos:").append(cdsPos).append(" return:").append(cdsStart + 1 + tmp)
							.append(" start:").append(format(start + 1)).append(" ").append(format(tmp)).append(" ")
							.append(cdsStart).append(" ").append(codingLength).toString());

					// we start 1 after cdsStart...
					return new ChromPos((cdsStart + 1 + tmp), -1);
				}
				inCoding = true;
				codingLength += (end - cdsStart);

				logger.debug(new StringBuilder().append("     UTR         : ").append(format(start + 1)).append(" - ")
						.append(cdsStart).toString());
				logger.debug(new StringBuilder().append(" ->  Exon        : ").append(format(cdsStart + 1))
						.append(" - ").append(format(end)).append(" | ").append(format(end - cdsStart)).append(" | ")
						.append(codingLength).append(" | ").append(codingLength % 3).toString());

			} else if (start + 1 <= cdsEnd && end >= cdsEnd) {
				// LAST EXON with UTR
				// logger.debug(" <-- CDS end at: " + cdsEnd );
				inCoding = false;
				if (codingLength + (cdsEnd - start - 1) >= cdsPos) {
					int tmp = cdsPos - codingLength;

					logger.debug(new StringBuilder().append(" <-  Exon        : ").append(format(start + 1))
							.append(" - ").append(format(cdsEnd)).append(" | ").append(format(cdsEnd - start))
							.append(" | ").append(codingLength).append(" | ").append(codingLength % 3).toString());
					logger.debug(new StringBuilder().append("     UTR         : ").append(format(cdsEnd + 1))
							.append(" - ").append(format(end)).toString());
					logger.debug(new StringBuilder().append(codingLength).append(" | ").append(tmp).append(" | ")
							.append(format(start + 1)).toString());
					logger.debug(new StringBuilder().append(" -> chromPosForward found position in non coding exon:  ")
							.append(cdsPos).append(" ").append(format(start + 1)).append(" ").append(format(tmp))
							.append(" ").append(format(cdsStart)).append(" ").append(codingLength).toString());

					return new ChromPos((start + 1 + tmp), cdsPos % 3);
				}
				codingLength += (cdsEnd - start - 1);

				logger.debug(new StringBuilder().append(" <-  Exon        : ").append(format(start + 1)).append(" - ")
						.append(format(cdsEnd)).append(" | ").append(format(cdsEnd - start)).append(" | ")
						.append(codingLength).append(" | ").append(codingLength % 3).toString());
				logger.debug(new StringBuilder().append("     UTR         : ").append(format(cdsEnd + 1)).append(" - ")
						.append(format(end)).toString());

			} else if (inCoding) {
				// A standard coding Exon
				// tests for the maximum length of this coding exon
				if (codingLength + (end - start - 1) >= cdsPos) {

					// we are within the range of this exon
					int tmp = cdsPos - codingLength;

					logger.debug(new StringBuilder().append("     Exon        : ").append(format(start + 1))
							.append(" - ").append(format(end)).append(" | ").append(format(end - start)).append(" | ")
							.append(tmp).append(" | ").append(codingLength).toString());
					logger.debug(new StringBuilder().append(" -> found chr position in coding exon #").append(i + 1)
							.append(":  cdsPos:").append(format(cdsPos)).append(" s:").append(format(start)).append("-")
							.append(format(end)).append(" tmp:").append(format(tmp)).append(" cdsStart:")
							.append(format(cdsStart)).append(" codingLength:").append(codingLength).toString());

					return new ChromPos((start + 1 + tmp), cdsPos % 3);
				}
				// full exon is coding
				codingLength += (end - start);

				logger.debug(new StringBuilder().append("     Exon        : ").append(format(start + 1)).append(" - ")
						.append(format(end)).append(" | ").append(format(end - start)).append(" | ")
						.append(codingLength).append(" | ").append(codingLength % 3).toString());
			}
		}
		return new ChromPos(-1, -1);
	}

	/**
	 * Get the length of the coding sequence
	 *
	 * @param exonStarts
	 * @param exonEnds
	 * @param cdsStart
	 * @param cdsEnd
	 * @return
	 */
	public static int getCDSLengthReverse(List<Integer> exonStarts, List<Integer> exonEnds, int cdsStart, int cdsEnd) {

		int codingLength = 0;

		if (cdsEnd < cdsStart) {
			int tmp = cdsEnd;
			cdsEnd = cdsStart;
			cdsStart = tmp;
		}
		cdsStart += base;

		// map reverse
		for (int i = exonStarts.size() - 1; i >= 0; i--) {

			int end = exonStarts.get(i);
			int start = exonEnds.get(i);

			if (end < start) {
				int tmp = end;
				end = start;
				start = tmp;
			}
			start += base;

			if ((start < cdsStart && end < cdsStart) || (start > cdsEnd && end > cdsEnd)) {
				continue;
			}

			if (start < cdsStart) {
				start = cdsStart;
			}

			if (end > cdsEnd) {
				end = cdsEnd;
			}

			codingLength += (end - start + 1);
		}
		return codingLength - 3;
	}

	/**
	 * Get the length of the coding sequence
	 *
	 * @param exonStarts
	 * @param exonEnds
	 * @param cdsStart
	 * @param cdsEnd
	 * @return
	 */
	public static int getCDSLengthForward(List<Integer> exonStarts, List<Integer> exonEnds, int cdsStart, int cdsEnd) {

		int codingLength = 0;

		for (int i = 0; i < exonStarts.size(); i++) {

			int start = exonStarts.get(i) + base;
			int end = exonEnds.get(i);

			if ((start < cdsStart + base && end < cdsStart) || (start > cdsEnd && end > cdsEnd)) {
				continue;
			}

			if (start < cdsStart + base) {
				start = cdsStart + base;
			}

			if (end > cdsEnd) {
				end = cdsEnd;
			}

			codingLength += (end - start + 1);
		}
		return codingLength - 3;
	}

	/**
	 * Extracts the exon boundaries in CDS coordinates. (needs to be divided by 3 to
	 * get AA positions)
	 *
	 * @param chromPos
	 * @return
	 */
	public static List<Range<Integer>> getCDSExonRanges(GeneChromosomePosition chromPos) {
		if (chromPos.getOrientation() == '+') {
			return getCDSExonRangesForward(chromPos, CDS);
		}
		return getCDSExonRangesReverse(chromPos, CDS);
	}

	/**
	 * Extracts the boundaries of the coding regions in chromosomal coordinates
	 *
	 * @param chromPos
	 * @return
	 */
	public static List<Range<Integer>> getChromosomalRangesForCDS(GeneChromosomePosition chromPos) {
		if (chromPos.getOrientation() == '+') {
			return getCDSExonRangesForward(chromPos, CHROMOSOME);
		}
		return getCDSExonRangesReverse(chromPos, CHROMOSOME);
	}

	private static List<Range<Integer>> getCDSExonRangesReverse(GeneChromosomePosition chromPos, String responseType) {

		List<Integer> exonStarts = chromPos.getExonStarts();
		List<Integer> exonEnds = chromPos.getExonEnds();

		List<Range<Integer>> data = new ArrayList<>();
		int cdsStart = chromPos.getCdsStart();
		int cdsEnd = chromPos.getCdsEnd();

		boolean inCoding = false;
		int codingLength = 0;

		if (cdsEnd < cdsStart) {
			int tmp = cdsEnd;
			cdsEnd = cdsStart;
			cdsStart = tmp;
		}

		StringBuilder s = null;

		boolean debug = logger.isDebugEnabled();

		if (debug) {
			s = new StringBuilder();
		}

		// int lengthExons = 0;

		// map reverse
		for (int i = exonStarts.size() - 1; i >= 0; i--) {

			int end = exonStarts.get(i);
			int start = exonEnds.get(i);

			if (end < start) {
				int tmp = end;
				end = start;
				start = tmp;
			}
			// lengthExons += end - start;
			// s.append("Reverse exon: " + end + " - " + start + " | " + (end - start));
			// s.append(newline);

			if (start <= cdsEnd && end >= cdsEnd) {
				inCoding = true;

				int tmpstart = start;
				if (start < cdsStart) {
					tmpstart = cdsStart;
				}
				codingLength += (cdsEnd - tmpstart);
				if (debug) {
					s.append("     UTR         :").append(format(cdsEnd + 1)).append(" | ").append(format(end));
					s.append(newline);
					if (tmpstart == start) {
						s.append(" ->  ");
					} else {
						s.append(" <-> ");
					}
					s.append("Exon        :").append(format(tmpstart + 1)).append(" - ").append(format(cdsEnd))
							.append(" | ").append(cdsEnd - tmpstart).append(" | ").append(codingLength).append(" | ")
							.append(codingLength % 3);
					s.append(newline);
					// single exon with UTR on both ends
					if (tmpstart != start) {
						s.append("     UTR         :").append(format(cdsStart)).append(" - ").append(format(start + 1));
					}
					s.append(newline);
				}

				Range<Integer> r;
				if (responseType.equals(CDS)) {
					r = Range.closed(0, codingLength);
				} else {
					r = Range.closed(tmpstart, cdsEnd);
				}

				data.add(r);

			} else if (start <= cdsStart && end >= cdsStart) {
				inCoding = false;

				Range<Integer> r;
				if (responseType.equals(CDS)) {
					r = Range.closed(codingLength, codingLength + (end - cdsStart));
				} else {
					r = Range.closed(cdsStart + 1, end);
				}

				data.add(r);

				codingLength += (end - cdsStart);
				if (debug) {
					s.append(new StringBuilder().append(" <-  Exon        : ").append(format(cdsStart + 1))
							.append(" - ").append(format(end)).append(" | ").append(end - cdsStart).append(" | ")
							.append(codingLength).append(" | ").append(codingLength % 3).toString());
					s.append(newline);
					s.append("     UTR         : ").append(format(start + 1)).append(" - ").append(format(cdsStart));
					s.append(newline);
				}
			} else if (inCoding) {
				// full exon is coding
				Range<Integer> r;
				if (responseType.equals(CDS)) {
					r = Range.closed(codingLength, codingLength + (end - start));
				} else {
					r = Range.closed(start, end);
				}
				data.add(r);

				codingLength += (end - start);
				if (debug) {
					s.append(new StringBuilder().append("     Exon        : ").append(format(start + 1)).append(" - ")
							.append(format(end)).append(" | ").append(end - start).append(" | ").append(codingLength)
							.append(" | ").append(codingLength % 3).toString());
					s.append(newline);
				}
			} else {
				// e.g. see UBQLN3
				if (debug) {
					s.append(new StringBuilder().append(" no translation! UTR: ").append(format(start)).append(" - ")
							.append(format(end)).toString());
					s.append(newline);
				}
			}
		}
		if (debug) {
			s.append("CDS length: ").append(Integer.toString(codingLength - 3));
			s.append(newline);
			logger.debug(s.toString());
		}

		return data;
	}

	private static List<Range<Integer>> getCDSExonRangesForward(GeneChromosomePosition chromPos, String responseType) {

		List<Range<Integer>> data = new ArrayList<>();
		List<Integer> exonStarts = chromPos.getExonStarts();
		List<Integer> exonEnds = chromPos.getExonEnds();

		int cdsStart = chromPos.getCdsStart();
		int cdsEnd = chromPos.getCdsEnd();

		boolean inCoding = false;
		int codingLength = 0;

		for (int i = 0; i < exonStarts.size(); i++) {

			int start = exonStarts.get(i);
			int end = exonEnds.get(i);

			if (start <= cdsStart && end >= cdsStart) {

				inCoding = true;
				codingLength += (end - cdsStart);

				Range<Integer> r;
				if (responseType.equals(CDS)) {
					r = Range.closed(0, codingLength);
				} else {
					r = Range.closed(cdsStart, end);
				}
				data.add(r);

			} else if (start <= cdsEnd && end >= cdsEnd) {
				// logger.debug(" <-- CDS end at: " + cdsEnd );
				inCoding = false;

				Range<Integer> r;
				if (responseType.equals(CDS)) {
					r = Range.closed(codingLength, codingLength + (cdsEnd - start));
				} else {
					r = Range.closed(start, cdsEnd);
				}
				data.add(r);
				codingLength += (cdsEnd - start);

			} else if (inCoding) {
				// full exon is coding
				Range<Integer> r;
				if (responseType.equals(CDS)) {
					r = Range.closed(codingLength, codingLength + (end - start));
				} else {
					r = Range.closed(start, end);
				}
				data.add(r);
				codingLength += (end - start);
			}
		}
		return data;
	}

	/**
	 * I have a genomic coordinate, where is it on the mRNA
	 *
	 * @param coordinate
	 * @param chromosomePosition
	 * @return
	 */
	public static int getCDSPosForChromosomeCoordinate(int coordinate, GeneChromosomePosition chromosomePosition) {

		if (chromosomePosition.getOrientation() == '+') {
			return getCDSPosForward(coordinate, chromosomePosition.getExonStarts(), chromosomePosition.getExonEnds(),
					chromosomePosition.getCdsStart(), chromosomePosition.getCdsEnd());
		}

		return getCDSPosReverse(coordinate, chromosomePosition.getExonStarts(), chromosomePosition.getExonEnds(),
				chromosomePosition.getCdsStart(), chromosomePosition.getCdsEnd());
	}

	/**
	 * Converts the genetic coordinate to the position of the nucleotide on the mRNA
	 * sequence for a gene living on the forward DNA strand.
	 * 
	 * @param chromPos   The genetic coordinate on a chromosome
	 * @param exonStarts The list holding the genetic coordinates pointing to the
	 *                   start positions of the exons (including UTR regions)
	 * @param exonEnds   The list holding the genetic coordinates pointing to the
	 *                   end positions of the exons (including UTR regions)
	 * @param cdsStart   The start position of a coding region
	 * @param cdsEnd     The end position of a coding region
	 * 
	 * @return the position of the nucleotide base on the mRNA sequence
	 *         corresponding to the input genetic coordinate (base 1)
	 * 
	 * @author Yana Valasatava
	 */
	public static int getCDSPosForward(int chromPos, List<Integer> exonStarts, List<Integer> exonEnds, int cdsStart,
			int cdsEnd) {

		// the genetic coordinate is not in a coding region
		if ((chromPos < (cdsStart + base)) || (chromPos > (cdsEnd + base))) {
			logger.debug(new StringBuilder().append("The ").append(format(chromPos))
					.append(" position is not in a coding region").toString());
			return -1;
		}

		logger.debug("looking for CDS position for " + format(chromPos));

		// map the genetic coordinates of coding region on a stretch of a reverse strand
		List<Range<Integer>> cdsRegions = getCDSRegions(exonStarts, exonEnds, cdsStart, cdsEnd);

		int codingLength = 0;
		int lengthExon = 0;
		for (Range<Integer> range : cdsRegions) {

			int start = range.lowerEndpoint();
			int end = range.upperEndpoint();

			lengthExon = end - start;

			if (start + base <= chromPos && end >= chromPos) {
				return codingLength + (chromPos - start);
			} else {
				codingLength += lengthExon;
			}
		}
		return -1;
	}

	/**
	 * Converts the genetic coordinate to the position of the nucleotide on the mRNA
	 * sequence for a gene living on the reverse DNA strand.
	 * 
	 * @param chromPos   The genetic coordinate on a chromosome
	 * @param exonStarts The list holding the genetic coordinates pointing to the
	 *                   start positions of the exons (including UTR regions)
	 * @param exonEnds   The list holding the genetic coordinates pointing to the
	 *                   end positions of the exons (including UTR regions)
	 * @param cdsStart   The start position of a coding region
	 * @param cdsEnd     The end position of a coding region
	 * 
	 * @return the position of the nucleotide base on the mRNA sequence
	 *         corresponding to the input genetic coordinate (base 1)
	 * 
	 * @author Yana Valasatava
	 */
	public static int getCDSPosReverse(int chromPos, List<Integer> exonStarts, List<Integer> exonEnds, int cdsStart,
			int cdsEnd) {

		// the genetic coordinate is not in a coding region
		if ((chromPos < (cdsStart + base)) || (chromPos > (cdsEnd + base))) {
			logger.debug(new StringBuilder().append("The ").append(format(chromPos))
					.append(" position is not in a coding region").toString());
			return -1;
		}

		logger.debug("looking for CDS position for " + format(chromPos));

		// map the genetic coordinate on a stretch of a reverse strand
		List<Range<Integer>> cdsRegions = getCDSRegions(exonStarts, exonEnds, cdsStart, cdsEnd);

		int codingLength = 0;
		int lengthExon = 0;
		for (int i = cdsRegions.size() - 1; i >= 0; i--) {

			int start = cdsRegions.get(i).lowerEndpoint();
			int end = cdsRegions.get(i).upperEndpoint();

			lengthExon = end - start;
			// +1 offset to be a base 1
			if (start + base <= chromPos && end >= chromPos) {
				return codingLength + (end - chromPos + 1);
			} else {
				codingLength += lengthExon;
			}
		}
		return -1;
	}

	/**
	 * Extracts the exons boundaries in CDS coordinates corresponding to the forward
	 * DNA strand.
	 *
	 * @param origExonStarts The list holding the genetic coordinates pointing to
	 *                       the start positions of the exons (including UTR
	 *                       regions)
	 * @param origExonEnds   The list holding the genetic coordinates pointing to
	 *                       the end positions of the exons (including UTR regions)
	 * @param cdsStart       The start position of a coding region
	 * @param cdsEnd         The end position of a coding region
	 * 
	 * @return the list of genetic positions corresponding to the exons boundaries
	 *         in CDS coordinates
	 */
	public static List<Range<Integer>> getCDSRegions(List<Integer> origExonStarts, List<Integer> origExonEnds,
			int cdsStart, int cdsEnd) {

		// remove exons that are fully landed in UTRs
		List<Integer> exonStarts = new ArrayList<>(origExonStarts);
		List<Integer> exonEnds = new ArrayList<>(origExonEnds);

		int j = 0;
		for (int i = 0; i < origExonStarts.size(); i++) {
			if ((origExonEnds.get(i) < cdsStart) || (origExonStarts.get(i) > cdsEnd)) {
				exonStarts.remove(j);
				exonEnds.remove(j);
			} else {
				j++;
			}
		}

		// remove untranslated regions from exons
		int nExons = exonStarts.size();
		exonStarts.remove(0);
		exonStarts.add(0, cdsStart);
		exonEnds.remove(nExons - 1);
		exonEnds.add(cdsEnd);

		List<Range<Integer>> cdsRegion = new ArrayList<>();
		for (int i = 0; i < nExons; i++) {
			Range<Integer> r = Range.closed(exonStarts.get(i), exonEnds.get(i));
			cdsRegion.add(r);
		}
		return cdsRegion;
	}

	/**
	 * Extracts the DNA sequence transcribed from the input genetic coordinates.
	 *
	 * @param twoBitFacade the facade that provide an access to a 2bit file
	 * @param gcp          The container with chromosomal positions
	 * 
	 * @return the DNA sequence transcribed from the input genetic coordinates
	 */
	public static DNASequence getTranscriptDNASequence(TwoBitFacade twoBitFacade, GeneChromosomePosition gcp)
			throws Exception {
		return getTranscriptDNASequence(twoBitFacade, gcp.getChromosome(), gcp.getExonStarts(), gcp.getExonEnds(),
				gcp.getCdsStart(), gcp.getCdsEnd(), gcp.getOrientation());
	}

	/**
	 * Extracts the DNA sequence transcribed from the input genetic coordinates.
	 *
	 * @param chromosome  the name of the chromosome
	 * @param exonStarts  The list holding the genetic coordinates pointing to the
	 *                    start positions of the exons (including UTR regions)
	 * @param exonEnds    The list holding the genetic coordinates pointing to the
	 *                    end positions of the exons (including UTR regions)
	 * @param cdsStart    The start position of a coding region
	 * @param cdsEnd      The end position of a coding region
	 * @param orientation The orientation of the strand where the gene is living
	 * 
	 * @return the DNA sequence transcribed from the input genetic coordinates
	 */
	public static DNASequence getTranscriptDNASequence(TwoBitFacade twoBitFacade, String chromosome,
			List<Integer> exonStarts, List<Integer> exonEnds, int cdsStart, int cdsEnd, Character orientation)
			throws Exception {

		List<Range<Integer>> cdsRegion = getCDSRegions(exonStarts, exonEnds, cdsStart, cdsEnd);

		String dnaSequence = "";
		for (Range<Integer> range : cdsRegion) {
			String exonSequence = twoBitFacade.getSequence(chromosome, range.lowerEndpoint(), range.upperEndpoint());
			dnaSequence += exonSequence;
		}
		if (orientation.equals('-')) {
			dnaSequence = new StringBuilder(dnaSequence).reverse().toString();
			DNASequence dna = new DNASequence(dnaSequence);
			SequenceView<NucleotideCompound> compliment = dna.getComplement();
			dnaSequence = compliment.getSequenceAsString();
		}
		return new DNASequence(dnaSequence.toUpperCase());
	}
}
