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
 * Created on Jan 27, 2010
 * Author: Andreas Prlic
 *
 */

package demo;

import org.biojava.nbio.structure.*;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.io.PDBFileReader;
import org.biojava.nbio.core.util.InputStreamProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example for how to load protein structures (from PDB files).
 *
 * @author Andreas Prlic
 *
 */
public class DemoLoadStructure {

	private static final Logger logger = LoggerFactory.getLogger(DemoLoadStructure.class);

	public static void main(String[] args) {

		DemoLoadStructure demo = new DemoLoadStructure();

		demo.loadStructureIO();

		// demo.basicLoad();

		// demo.loadStructureFromCache();
	}

	public void loadStructureIO() {
		try {
			Structure s1 = StructureIO.getStructure("1gav");
			logger.info(s1.getPDBCode() + " asym unit has nr atoms:");
			logger.info(String.valueOf(StructureTools.getNrAtoms(s1)));

			Chain chain1 = s1.getChainByIndex(0);

			logger.info("First chain: " + chain1);

			logger.info(new StringBuilder().append("Chain ").append(chain1.getName())
					.append(" has the following sequence mismatches:").toString());
			chain1.getSeqMisMatches().forEach(mm -> logger.info(String.valueOf(mm)));

			Structure s2 = StructureIO.getBiologicalAssembly("1gav");
			logger.info(s2.getPDBCode() + " biological assembly has nr atoms:");
			logger.info(String.valueOf(StructureTools.getNrAtoms(s2)));

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	public void basicLoad() {
		try {

			PDBFileReader reader = new PDBFileReader();

			// the path to the local PDB installation
			reader.setPath("/tmp");

			// configure the parameters of file parsing

			FileParsingParameters params = new FileParsingParameters();

			// should the ATOM and SEQRES residues be aligned when creating the internal
			// data model?
			params.setAlignSeqRes(true);

			// should secondary structure get parsed from the file
			params.setParseSecStruc(false);

			reader.setFileParsingParameters(params);

			Structure structure = reader.getStructureById("4hhb");

			logger.info(String.valueOf(structure));

			Chain c = structure.getPolyChainByPDB("C");

			logger.info(String.valueOf(c));

			logger.info(String.valueOf(c.getEntityInfo()));

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	public void loadStructureFromCache() {
		String pdbId = "4hhb";
		String chainName = "4hhb.A";
		String entityName = "4hhb:0";

		// we can set a flag if the file should be cached in memory
		// this will enhance IO massively if the same files have to be accessed over and
		// over again.
		// since this is a soft cache, no danger of memory leak
		// this is actually not necessary to provide, since the default is "true" if the
		// AtomCache is being used.
		System.setProperty(InputStreamProvider.CACHE_PROPERTY, "true");

		AtomCache cache = new AtomCache();

		try {
			logger.info("======================");
			Structure s = cache.getStructure(pdbId);

			logger.info("Full Structure:" + s);

			Atom[] ca = cache.getAtoms(chainName);
			logger.info(new StringBuilder().append("got ").append(ca.length).append(" CA atoms").toString());

			Structure firstEntity = cache.getStructure(entityName);
			logger.info("First entity: " + firstEntity);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

}
