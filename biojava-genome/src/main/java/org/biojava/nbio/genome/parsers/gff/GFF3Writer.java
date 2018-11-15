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
package org.biojava.nbio.genome.parsers.gff;

import org.biojava.nbio.genome.GeneFeatureHelper;
import org.biojava.nbio.core.sequence.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

/**
 *
 * @author Scooter Willis <willishf at gmail dot com>
 */
public class GFF3Writer {

	/**
	 * Output gff3 format for a DNA Sequence
	 * 
	 * @param fileName
	 * @param chromosomeSequence
	 * @throws Exception
	 */
	public void write(OutputStream outputStream, LinkedHashMap<String, ChromosomeSequence> chromosomeSequenceList)
			throws Exception {

		outputStream.write("##gff-version 3\n".getBytes());
		for (String key : chromosomeSequenceList.keySet()) {
			ChromosomeSequence chromosomeSequence = chromosomeSequenceList.get(key);
			String gff3line = "";
			// if(source.length() == 0){
			// Collection<GeneSequence> genes =
			// chromosomeSequence.getGeneSequences().values();
			// for(GeneSequence gene : genes){
			// source = gene.getSource();
			// break;
			// }
			// }
			// gff3line = key + "\t" + source + "\t" + "size" + "\t" + "1" + "\t" +
			// chromosomeSequence.getBioEnd() + "\t.\t.\t.\tName=" + key + "\r\n";
			// outputStream.write(gff3line.getBytes());

			for (GeneSequence geneSequence : chromosomeSequence.getGeneSequences().values()) {
				gff3line = new StringBuilder().append(key).append("\t").append(geneSequence.getSource()).append("\t")
						.append("gene").append("\t").append(geneSequence.getBioBegin()).append("\t")
						.append(geneSequence.getBioEnd()).append("\t").toString();
				Double score = geneSequence.getSequenceScore();
				if (score == null) {
					gff3line = gff3line + ".\t";
				} else {
					gff3line = new StringBuilder().append(gff3line).append(score).append("\t").toString();
				}
				gff3line = new StringBuilder().append(gff3line)
						.append(geneSequence.getStrand().getStringRepresentation()).append("\t").toString();
				gff3line = gff3line + ".\t";
				gff3line = new StringBuilder().append(gff3line).append("ID=")
						.append(geneSequence.getAccession().getID()).append(";Name=")
						.append(geneSequence.getAccession().getID()).toString();
				gff3line = gff3line + getGFF3Note(geneSequence.getNotesList());
				gff3line = gff3line + "\n";
				outputStream.write(gff3line.getBytes());

				int transcriptIndex = 0;
				for (TranscriptSequence transcriptSequence : geneSequence.getTranscripts().values()) {
					transcriptIndex++;

					gff3line = new StringBuilder().append(key).append("\t").append(transcriptSequence.getSource())
							.append("\t").append("mRNA").append("\t").append(transcriptSequence.getBioBegin())
							.append("\t").append(transcriptSequence.getBioEnd()).append("\t").toString();
					score = transcriptSequence.getSequenceScore();
					if (score == null) {
						gff3line = gff3line + ".\t";
					} else {
						gff3line = new StringBuilder().append(gff3line).append(score).append("\t").toString();
					}
					gff3line = new StringBuilder().append(gff3line)
							.append(transcriptSequence.getStrand().getStringRepresentation()).append("\t").toString();
					gff3line = gff3line + ".\t";
					String id = new StringBuilder().append(geneSequence.getAccession().getID()).append(".")
							.append(transcriptIndex).toString();
					gff3line = new StringBuilder().append(gff3line).append("ID=").append(id).append(";Parent=")
							.append(geneSequence.getAccession().getID()).append(";Name=").append(id).toString();
					gff3line = gff3line + getGFF3Note(transcriptSequence.getNotesList());

					gff3line = gff3line + "\n";
					outputStream.write(gff3line.getBytes());

					String transcriptParentName = new StringBuilder().append(geneSequence.getAccession().getID())
							.append(".").append(transcriptIndex).toString();
					ArrayList<CDSSequence> cdsSequenceList = new ArrayList<>(
							transcriptSequence.getCDSSequences().values());
					Collections.sort(cdsSequenceList, new SequenceComparator());
					for (CDSSequence cdsSequence : cdsSequenceList) {
						gff3line = new StringBuilder().append(key).append("\t").append(cdsSequence.getSource())
								.append("\t").append("CDS").append("\t").append(cdsSequence.getBioBegin()).append("\t")
								.append(cdsSequence.getBioEnd()).append("\t").toString();
						score = cdsSequence.getSequenceScore();
						if (score == null) {
							gff3line = gff3line + ".\t";
						} else {
							gff3line = new StringBuilder().append(gff3line).append(score).append("\t").toString();
						}
						gff3line = new StringBuilder().append(gff3line)
								.append(cdsSequence.getStrand().getStringRepresentation()).append("\t").toString();
						gff3line = new StringBuilder().append(gff3line).append(cdsSequence.getPhase()).append("\t")
								.toString();
						gff3line = new StringBuilder().append(gff3line).append("ID=")
								.append(cdsSequence.getAccession().getID()).append(";Parent=")
								.append(transcriptParentName).toString();
						gff3line = gff3line + getGFF3Note(cdsSequence.getNotesList());

						gff3line = gff3line + "\n";
						outputStream.write(gff3line.getBytes());
					}

				}
			}

		}

	}

	private String getGFF3Note(ArrayList<String> notesList) {
		String notes = "";

		if (notesList.size() > 0) {
			notes = ";Note=";
			int noteindex = 1;
			for (String note : notesList) {
				notes = notes + note;
				if (noteindex < notesList.size() - 1) {
					notes = notes + " ";
				}
			}

		}
		return notes;
	}

	public static void main(String[] args) throws Exception {

		// LinkedHashMap<String, ProteinSequence> proteinSequenceList =
		// GeneFeatureHelper.getProteinSequences(chromosomeSequenceList.values());
		// for(String id : proteinSequenceList.keySet()){
		// ProteinSequence sequence = proteinSequenceList.get(id);
		// System.out.println(id + " " + sequence.getSequenceAsString());
		// GeneMarkGTF.write( list, args[1] );
		// System.out.println(listGenes);
		/*
		 * if (false) { FileOutputStream fo = new FileOutputStream(
		 * "/Users/Scooter/scripps/dyadic/analysis/454Scaffolds/genemark_hmm.gff3");//-
		 * 16 LinkedHashMap<String, ChromosomeSequence> dnaSequenceList =
		 * GeneFeatureHelper.loadFastaAddGeneFeaturesFromGeneMarkGTF(new
		 * File("/Users/Scooter/scripps/dyadic/analysis/454Scaffolds/454Scaffolds.fna"),
		 * new
		 * File("/Users/Scooter/scripps/dyadic/analysis/454Scaffolds/genemark_hmm.gtf"))
		 * ; GFF3Writer gff3Writer = new GFF3Writer(); gff3Writer.write(fo,
		 * dnaSequenceList); fo.close(); }
		 * 
		 * if (false) { LinkedHashMap<String, ChromosomeSequence> dnaSequenceList =
		 * GeneFeatureHelper.loadFastaAddGeneFeaturesFromGlimmerGFF3(new File(
		 * "/Users/Scooter/scripps/dyadic/analysis/454Scaffolds/454Scaffolds-16.fna"),
		 * new File("/Users/Scooter/scripps/dyadic/GlimmerHMM/c1_glimmerhmm-16.gff"));
		 * GFF3Writer gff3Writer = new GFF3Writer(); gff3Writer.write(System.out,
		 * dnaSequenceList); }
		 */
		if (false) {
			return;
		}
		FileOutputStream fo = new FileOutputStream("/Users/Scooter/scripps/dyadic/geneid/geneid/c1-geneid.gff3");// -16
		LinkedHashMap<String, ChromosomeSequence> dnaSequenceList = GeneFeatureHelper
				.loadFastaAddGeneFeaturesFromGeneIDGFF2(
						new File("/Users/Scooter/scripps/dyadic/analysis/454Scaffolds/454Scaffolds.fna"),
						new File("/Users/Scooter/scripps/dyadic/geneid/geneid/c1_geneid.gff"));
		GFF3Writer gff3Writer = new GFF3Writer();
		gff3Writer.write(fo, dnaSequenceList);
		// }
		fo.close();
	}
}
