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
 * Created on Jan 21, 2010
 *
 */
package demo;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureTools;
import org.biojava.nbio.structure.align.StructureAlignment;
import org.biojava.nbio.structure.align.StructureAlignmentFactory;
import org.biojava.nbio.structure.align.ce.CeMain;
import org.biojava.nbio.structure.align.ce.CeParameters;
import org.biojava.nbio.structure.align.model.AFPChain;
import org.biojava.nbio.structure.align.util.AFPChainScorer;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.align.xml.AFPChainXMLConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example of how to run a structure alignment using the CE algorithm.
 *
 * @author Andreas Prlic
 *
 */
public class DemoCE {

	private static final Logger logger = LoggerFactory.getLogger(DemoCE.class);

	public static void main(String[] args) {

		// String name1 = "4hhb.A";
		// String name2 = "4hhb.B";

		String name1 = "1cdg.A";
		String name2 = "1tim.B";

		AtomCache cache = new AtomCache();

		Structure structure1 = null;
		Structure structure2 = null;

		try {

			StructureAlignment algorithm = StructureAlignmentFactory.getAlgorithm(CeMain.algorithmName);

			structure1 = cache.getStructure(name1);
			structure2 = cache.getStructure(name2);

			Atom[] ca1 = StructureTools.getAtomCAArray(structure1);
			Atom[] ca2 = StructureTools.getAtomCAArray(structure2);

			// get default parameters
			CeParameters params = new CeParameters();

			// add more print
			params.setShowAFPRanges(true);

			// set the maximum gap size to unlimited
			params.setMaxGapSize(-1);

			AFPChain afpChain = algorithm.align(ca1, ca2, params);

			afpChain.setName1(name1);
			afpChain.setName2(name2);

			// flexible original results:
			logger.info(afpChain.toFatcat(ca1, ca2));

			logger.info(afpChain.toRotMat());
			// System.out.println(afpChain.toCE(ca1, ca2));

			logger.info(AFPChainXMLConverter.toXML(afpChain, ca1, ca2));

			double tmScore = AFPChainScorer.getTMScore(afpChain, ca1, ca2);
			afpChain.setTMScore(tmScore);

			// System.out.println(AfpChainWriter.toWebSiteDisplay(afpChain, ca1, ca2));

			printScores(afpChain);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return;
		}
	}

	private static void printScores(AFPChain afpChain) {
		logger.info("=====================");
		logger.info("The main scores for the alignment:");

		logger.info(new StringBuilder().append("EQR       :\t").append(afpChain.getNrEQR())
				.append("\t The number of residues on structurally equivalent positions.").toString());
		logger.info(
				new StringBuilder().append("RMSD      :\t").append(String.format("%.2f", afpChain.getTotalRmsdOpt()))
						.append("\t The RMSD of the alignment").toString());
		logger.info(new StringBuilder().append("Z-score   :\t").append(afpChain.getProbability())
				.append("\t The Z-score of the alignment (CE)").toString());
		logger.info(new StringBuilder().append("TM-score  :\t").append(String.format("%.2f", afpChain.getTMScore()))
				.append("\t The TM-score of the alignment.").toString());
		logger.info("");
		logger.info("Other scores:");
		logger.info(new StringBuilder().append("Identity  :\t").append(String.format("%.2f", afpChain.getIdentity()))
				.append("\t The percent of residues that are sequence-identical in the alignment.").toString());
		logger.info(new StringBuilder().append("Similarity:\t").append(String.format("%.2f", afpChain.getSimilarity()))
				.append("\t The percent of residues in the alignment that are sequence-similar.").toString());
		logger.info(new StringBuilder().append("Coverage1 :\t").append(afpChain.getCoverage1())
				.append(" %\t Percent of protein 1 that is covered with the alignment.").toString());
		logger.info(new StringBuilder().append("Coverage2 :\t").append(afpChain.getCoverage2())
				.append(" %\t Percent of protein 2 that is covered with the alignment.").toString());
		int dab = afpChain.getCa1Length() + afpChain.getCa2Length() - 2 * afpChain.getNrEQR();
		logger.info(new StringBuilder().append("Distance  :\t").append(dab).append("\t Distance between folds a,b ")
				.toString());
		double sab = 2 * afpChain.getNrEQR() / (double) (afpChain.getCa1Length() + afpChain.getCa2Length());
		logger.info(new StringBuilder().append("Rel. Sim. :\t").append(String.format("%.2f", sab))
				.append("\t Relative similarity").toString());

	}
}
