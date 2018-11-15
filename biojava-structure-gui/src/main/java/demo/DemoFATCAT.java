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
 * Created on Mar 15, 2010
 * Author: Andreas Prlic
 *
 */

package demo;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureTools;
import org.biojava.nbio.structure.align.StructureAlignment;
import org.biojava.nbio.structure.align.StructureAlignmentFactory;
import org.biojava.nbio.structure.align.fatcat.FatCatRigid;
import org.biojava.nbio.structure.align.fatcat.calc.FatCatParameters;
import org.biojava.nbio.structure.align.gui.StructureAlignmentDisplay;
import org.biojava.nbio.structure.align.model.AFPChain;
import org.biojava.nbio.structure.align.model.AfpChainWriter;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoFATCAT {

	private static final Logger logger = LoggerFactory.getLogger(DemoFATCAT.class);

	public static void main(String[] args) {

		String name1 = "4hhb.A";
		String name2 = "4hhb.B";

		// String name1 = "1cdg.A";
		// String name2 = "1tim.B";

		AtomCache cache = new AtomCache();

		Structure structure1 = null;
		Structure structure2 = null;

		try {

			StructureAlignment algorithm = StructureAlignmentFactory.getAlgorithm(FatCatRigid.algorithmName);

			structure1 = cache.getStructure(name1);
			structure2 = cache.getStructure(name2);

			Atom[] ca1 = StructureTools.getAtomCAArray(structure1);
			Atom[] ca2 = StructureTools.getAtomCAArray(structure2);

			// get default parameters
			FatCatParameters params = new FatCatParameters();

			AFPChain afpChain = algorithm.align(ca1, ca2, params);

			afpChain.setName1(name1);
			afpChain.setName2(name2);

			// show original FATCAT output:
			logger.info(afpChain.toFatcat(ca1, ca2));

			// show a nice summary print
			logger.info(AfpChainWriter.toWebSiteDisplay(afpChain, ca1, ca2));

			// print rotation matrices
			logger.info(afpChain.toRotMat());
			// System.out.println(afpChain.toCE(ca1, ca2));

			// print XML representation
			// System.out.println(AFPChainXMLConverter.toXML(afpChain,ca1,ca2));

			StructureAlignmentDisplay.display(afpChain, ca1, ca2);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return;
		}
	}

}
