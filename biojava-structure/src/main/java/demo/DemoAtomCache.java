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

import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.StructureIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example of how to load PDB files using the AtomCache class.
 *
 * @author Andreas Prlic
 *
 */
public class DemoAtomCache {
	private static final Logger logger = LoggerFactory.getLogger(DemoAtomCache.class);

	public static void main(String[] args) {
		demoAtomCache();
		demoStructureIO();

	}

	@SuppressWarnings("unused")
	private static void demoStructureIO() {

		try {
			Structure s1 = StructureIO.getStructure("4hhb");

			Structure bioAssembly = StructureIO.getBiologicalAssembly("1stp", 1);

			// do something with them...
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	private static void demoAtomCache() {
		AtomCache cache = new AtomCache();

		FileParsingParameters params = cache.getFileParsingParams();

		params.setAlignSeqRes(true);
		params.setHeaderOnly(false);
		params.setParseCAOnly(false);
		params.setParseSecStruc(false);

		String[] pdbIDs = new String[] { "4hhb", "1cdg", "5pti", "1gav", "WRONGID" };

		for (String pdbID : pdbIDs) {

			try {
				Structure s = cache.getStructure(pdbID);
				if (s == null) {
					logger.info("could not find structure " + pdbID);
					continue;
				}
				// do something with the structure
				logger.info(String.valueOf(s));

			} catch (Exception e) {
				// something crazy happened...
				logger.error(new StringBuilder().append("Can't load structure ").append(pdbID).append(" reason: ")
						.append(e.getMessage()).toString(), e);
				// e.printStackTrace();
			}
		}

	}
}
