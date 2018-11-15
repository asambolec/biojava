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

import java.io.IOException;
import java.util.List;

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.secstruc.SecStrucElement;
import org.biojava.nbio.structure.secstruc.SecStrucCalc;
import org.biojava.nbio.structure.secstruc.SecStrucTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstration on how to use the Secondary Structure Prediction (DSSP)
 * implementation in BioJava and obtain different SS representations and
 * outputs.
 *
 * @author Aleix Lafita
 *
 */
public class DemoSecStrucCalc {

	private static final Logger logger = LoggerFactory.getLogger(DemoSecStrucCalc.class);

	public static void main(String[] args) throws IOException, StructureException {

		String pdbID = "5pti";

		AtomCache cache = new AtomCache();

		// Load structure without any SS assignment
		Structure s = cache.getStructure(pdbID);

		// Predict and assign the SS of the Structure
		SecStrucCalc ssp = new SecStrucCalc();
		ssp.calculate(s, true);

		// Print the DSSP output
		logger.info("******DSSP output: ");
		logger.info(ssp.printDSSP());

		// Print the FASTA sequence of SS
		logger.info("\n******FASTA output: ");
		logger.info(ssp.printFASTA());

		// Print the Helix Summary
		logger.info("\n******Helix Summary: ");
		logger.info(ssp.printHelixSummary());

		// Obtain and print the SS elements of the Structure
		List<SecStrucElement> sse = SecStrucTools.getSecStrucElements(s);
		logger.info("\n******SecStrucElements: ");
		sse.forEach(e -> logger.info(String.valueOf(e)));

	}
}
