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

import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AmbiguityDNACompoundSet;
import org.biojava.nbio.core.sequence.compound.AmbiguityRNACompoundSet;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.io.DNASequenceCreator;
import org.biojava.nbio.core.sequence.io.FastaReader;
import org.biojava.nbio.core.sequence.io.GenericFastaHeaderParser;
import org.biojava.nbio.core.sequence.template.CompoundSet;
import org.biojava.nbio.core.sequence.template.Sequence;
import org.biojava.nbio.core.sequence.transcription.Frame;
import org.biojava.nbio.core.sequence.transcription.TranscriptionEngine;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by andreas on 8/10/15.
 */
public class DemoSixFrameTranslation {

	private static final Logger logger = LoggerFactory.getLogger(DemoSixFrameTranslation.class);

	public static void main(String[] args) {
		String dnaFastaS = new StringBuilder()
				.append(">gb:GQ903697|Organism:Arenavirus H0030026 H0030026|Segment:S|Host:Rat\n")
				.append("CGCACAGAGGATCCTAGGCGTTACTGACTTGCGCTAATAACAGATACTGTTTCATATTTAGATAAAGACC\n")
				.append("CAGCCAACTGATTGGTCAGCATGGGACAACTTGTGTCCCTCTTCAGTGAAATTCCATCAATCATACACGA\n")
				.append("AGCTCTCAATGTTGCTCTCGTAGCTGTTAGCATCATTGCAATATTGAAAGGGGTTGTGAATGTTTGGAAG\n")
				.append("AGTGGAGTTTTGCAGCTTTTGGCCTTCTTGCTCCTGGCGGGAAGATCCTGCTCAGTCATAATTGGTCATC\n")
				.append("ATCTCGAACTGCAGCATGTGATCTTCAATGGGTCATCAATCACACCCTTTTTACCAGTTACATGTAAGAT\n")
				.append("CAATGATACCTACTTCCTACTAAGAGGCCCCTATGAAGCTGATTGGGCAGTTGAATTGAGTGTAACTGAA\n")
				.append("ACCACAGTCTTGGTTGATCTTGAAGGTGGCAGCTCAATGAAGCTGAAAGCCGGAAACATCTCAGGTTGTC\n")
				.append("TTGGAGACAACCCCCATCTGAGATCAGTGGTCTTCACATTGAATTGGTTGCTAACAGGATTAGATCATGT\n")
				.append("TATTGATTCTGACCCGAAAATTCTCTGTGATCTTAAAGACAGTGGGCACTTTCGTCTCCAGATGAACTTA\n")
				.append("ACAGAAAAGCACTATTGTGACAAGTTTCACATCAAAATGGGCAAGGTCTTTGGCGTATTCAAAGATCCGT\n")
				.append("GCATGGCTGGTGGTAAAATGTTTGCCATACTAAAAAATACCTCTTGGTCGAACCAGTGCCAAGGAAACCA\n")
				.append("TGTCAGCACCATTCATCTTGTCCTTCAGAGTAATTTCAAACAGGTCCTCAGTAGCAGGAAACTGTTGAAC\n")
				.append("TTTTTCAGCTGGTCATTGTCTGATGCCACAGGGGCTGATATGCCTGGTGGTTTTTGTCTGGAAAAATGGA\n")
				.append("TGTTGATTTCAAGTGAACTGAAATGCTTTGGAAACACAGCTGTGGCAAAGTGCAACTTAAATCATGACTC\n")
				.append("AGAGTTCTGTGACATGCTTAGGCTTTTTGATTTCAACAAAAAGGCAATAGTCACTCTTCAGAACAAAACA\n")
				.append("AAGCATCGGCTGGACACAGTAATTACTGCTATCAATTCATTGATCTCTGATAATATTCTTATGAAGAACA\n")
				.append("GGATTAAAGAATTGATAGATGTTCCTTACTGTAATTACACCAAATTTTGGTATGTCAATCACACAGGTCT\n")
				.append("AAATCTGCACACCCTTCCAAGATGTTGGCTTGTTAAAAATGGTAGCTACTTGAATGTGTCTGACTTCAGG\n")
				.append("AATGAGTGGATATTGGAGAGTGATCATCTTGTTTCGGAGATCCTTTCAAAGGAGTATGAGGAAAGGCAAA\n")
				.append("ATCGTACACCACTCTCACTGGTTGACATCTGTTTCTGGAGTACATTGTTTTACACAGCATCAATTTTCCT\n")
				.append("ACACCTCTTGAGAATTCCAACCCACAGACACATTGTTGGTGAGGGCTGCCCGAAGCCTCATAGGCTAAAC\n")
				.append("AGGCACTCAATATGTGCTTGTGGCCTTTTCAAACAAGAAGGCAGACCCTTGAGATGGGTAAGAAAGGTGT\n")
				.append("GAACAATGGTTGCTTGGTGGCCTCCATTGCTGCACCCCCCTAGGGGGGTGCAGCAATGGAGGTTCTCGYT\n")
				.append("GAGCCTAGAGAACAACTGTTGAATCGGGTTCTCTAAAGAGAACATCGATTGGTAGTACCCTTTTTGGTTT\n")
				.append("TTCATTGGTCACTGACCCTGAAAGCACAGCACTGAACATCAAACAGTCCAAAAGTGCACAGTGTGCATTT\n")
				.append("GTTGTGGCTGGTGCTGATCCTTTCTTCTTACTTTTAATGACTATTCCCTTATGTCTGTCACACAGATGTT\n")
				.append("CAAATCTCTTCCAAACAAGATCTTCAAAGAGCCGTGACTGTTCTGCGGTCAGTTTGACATCAACAATCTT\n")
				.append("CAAATCCTGTCTTCCATGCATATCAAAGAGCCTCCTAATATCATCAGCACCTTGCGCAGTGAAAACCATG\n")
				.append("GATTTAGGCAGACTCCTTATTATGCTTGTGATGAGGCCAGGTCGTGCATGTTCAACATCCTTCAGCAATA\n")
				.append("TCCCATGACAATATTTACTTTGGTCCTTAAAAGATTTTATGTCATTGGGTTTTCTGTAGCAGTGGATGAA\n")
				.append("TTTTTGTGATTCAGGCTGGTAAATTGCAAACTCAACAGGGTCATGTGGCGGGCCTTCAATGTCAATCCAT\n")
				.append("GTTGTGTCACTGACCATCAACGACTCTACACTTCTCTTCACCTGAGCCTCCACCTCAGGCTTGAGCGTGG\n")
				.append("ACAAGAGTGGGGCACCACCGTTCCGGATGGGGACTGGTGTTTTGCTTGGTAAACTCTCAAATTCCACAAC\n")
				.append("TGTATTGTCCCATGCTCTCCCTTTGATCTGTGATCTTGATGAAATGTAAGGCCAGCCCTCACCAGAGAGA\n")
				.append("CACACCTTATAAAGTATGTTTTCATAAGGATTCCTCTGTCCTGGTATGGCACTGATGAACATGTTTTCCC\n")
				.append("TCTTTTTGATCTCCAAGAGGGTTTTTATAATGGTTGTGAATGTGGACTCCTCAATCTTTATTGTTTCCAG\n")
				.append("CATGTTGCCACCATCAATCAGGCAAGCACCGGCTTTCACAGCAGCTGATAAACTAAGGTTGTAGCCTGAT\n")
				.append("ATGTTAATTTGAGAATCCTCCTGAGTGATTACCTTTAGAGAAGGATGCTTCTCCATCAAAGCATCTAAGT\n")
				.append("CACTTAAATTAGGGTATTTTGCTGTGTATAGCAACCCCAGATCTGTGAGGGCCTGAACCACATCATTTAG\n")
				.append("AGTTTCCCCTCCCTGTTCAGTCATACAGGAAATTGTGAGTGCTGGCATCGATCCAAATTGGTTGATCATA\n")
				.append("AGTGATGAGTCTTTAACGTCCCAGACTTTGACCACCCCTCCAGTTCTAGCCAACCCAGGTCTCTGAATAC\n")
				.append("CAACAAGTTGCAGAATTTCGGACCTCCTGGTGAGCTGTGTTGTAGAGAGGTTCCCTAGATACTGGCCACC\n")
				.append("TGTGGCTGTCAACCTCTCTGTTCTTTGAACTTTTTGCCTTAATTTGTCCAAGTCACTGGAGAGTTCCATT\n")
				.append("AGCTCTTCCTTTGACAATGATCCTATCTTAAGGAACATGTTCTTTTGGGTTGACTTCATGACCATCAATG\n")
				.append("AGTCAACTTCCTTATTCAAGTCCCTCAAACTAACAAGATCACTGTCATCTCTTTTAGACCTCCTCATCAT\n")
				.append("GCGTTGCACACTTGCAACCTTTGAAAAATCTAAGCCGGACAGAAGAGCCCTCGCGTCAGTTAGGACATCT\n")
				.append("GCCTTAACAGCAGTTGTCCAGTTCGAGAGTCCTCTCCTGAGAGACTGTGTCCATCTGAATGATGGGATTG\n")
				.append("GTTGTTCGCTCATAGTGATGAAATTGCGCAGAGTTATCCAAAAGCCTAGGATCCTCTGTGCG").toString();

		try {

			// parse the raw sequence from the string
			InputStream stream = new ByteArrayInputStream(dnaFastaS.getBytes());

			// define the Ambiguity Compound Sets
			AmbiguityDNACompoundSet ambiguityDNACompoundSet = AmbiguityDNACompoundSet.getDNACompoundSet();
			CompoundSet<NucleotideCompound> nucleotideCompoundSet = AmbiguityRNACompoundSet.getRNACompoundSet();

			FastaReader<DNASequence, NucleotideCompound> proxy = new FastaReader<>(stream,
					new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
					new DNASequenceCreator(ambiguityDNACompoundSet));

			// has only one entry in this example, but could be easily extended to parse a
			// FASTA file with multiple sequences
			LinkedHashMap<String, DNASequence> dnaSequences = proxy.process();

			// Initialize the Transcription Engine
			TranscriptionEngine engine = new TranscriptionEngine.Builder().dnaCompounds(ambiguityDNACompoundSet)
					.rnaCompounds(nucleotideCompoundSet).build();

			Frame[] sixFrames = Frame.getAllFrames();

			for (DNASequence dna : dnaSequences.values()) {

				Map<Frame, Sequence<AminoAcidCompound>> results = engine.multipleFrameTranslation(dna, sixFrames);

				for (Frame frame : sixFrames) {
					logger.info(new StringBuilder().append("Translated Frame:").append(frame).append(" : ")
							.append(results.get(frame)).toString());
					// System.out.println(dna.getRNASequence(frame).getProteinSequence(engine));

					ProteinSequence ps = new ProteinSequence(results.get(frame).getSequenceAsString());
					logger.info(String.valueOf(ps));
					try {

					} catch (Exception e) {
						logger.error(new StringBuilder().append(e.getMessage())
								.append(" when trying to translate frame ").append(frame).toString(), e);
					}
				}

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

}
