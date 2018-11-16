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

import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.contact.*;
import org.biojava.nbio.structure.io.FileParsingParameters;
import org.biojava.nbio.structure.xtal.CrystalBuilder;
import org.biojava.nbio.structure.xtal.CrystalTransform;
import org.biojava.nbio.structure.xtal.SpaceGroup;
import org.biojava.nbio.structure.StructureIO;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Vector3d;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoCrystalInterfaces {

	private static final Logger logger = LoggerFactory.getLogger(DemoCrystalInterfaces.class);
	private static final double BSATOASA_CUTOFF = 0.95;
	private static final double MIN_ASA_FOR_SURFACE = 5;
	private static final int CONSIDER_COFACTORS = 40; // minimum number of atoms for a cofactor to be considered, if -1
														// all ignored

	private static final double CUTOFF = 5.5;

	private static final int N_SPHERE_POINTS = 3000;

	private static final double MIN_AREA_TO_KEEP = 35;

	private static final int NTHREADS = Runtime.getRuntime().availableProcessors();

	private static final double CLASH_DISTANCE = 1.5;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		String pdbCode = "1smt";

		AtomCache cache = new AtomCache();
		cache.setUseMmCif(true);

		FileParsingParameters params = new FileParsingParameters();
		params.setAlignSeqRes(true);
		cache.setFileParsingParams(params);

		StructureIO.setAtomCache(cache);

		Structure structure = StructureIO.getStructure(pdbCode);

		logger.info(structure.getPDBCode());

		SpaceGroup sg = structure.getCrystallographicInfo().getSpaceGroup();

		if (sg != null) {
			logger.info(new StringBuilder().append(sg.getShortSymbol()).append(" (").append(sg.getId()).append(")")
					.toString());
			logger.info("Symmetry operators: " + sg.getNumOperators());
		}
		logger.info(new StringBuilder().append("Calculating possible interfaces... (using ").append(NTHREADS)
				.append(" CPUs for ASA calculation)").toString());
		long start = System.currentTimeMillis();

		CrystalBuilder cb = new CrystalBuilder(structure);

		StructureInterfaceList interfaces = cb.getUniqueInterfaces(CUTOFF);
		interfaces.calcAsas(N_SPHERE_POINTS, NTHREADS, CONSIDER_COFACTORS);
		interfaces.removeInterfacesBelowArea(MIN_AREA_TO_KEEP);
		List<StructureInterfaceCluster> clusters = interfaces.getClusters();

		// interfaces.initialiseClusters(pdb, CLUSTERING_CUTOFF, MINATOMS_CLUSTERING,
		// "CA");

		long end = System.currentTimeMillis();
		long total = (end - start) / 1000;
		logger.info(new StringBuilder().append("Total time for interface calculation: ").append(total).append("s")
				.toString());

		logger.info("Total number of interfaces found: " + interfaces.size());

		for (int i = 0; i < interfaces.size(); i++) {
			StructureInterface interf = interfaces.get(i + 1);

			String infiniteStr = "";
			if (interf.isInfinite()) {
				infiniteStr = " -- INFINITE interface";
			}
			logger.info(new StringBuilder().append("\n##Interface ").append(i + 1).append(" ")
					.append(interf.getCrystalIds().getFirst()).append("-").append(interf.getCrystalIds().getSecond())
					.append(infiniteStr).toString());
			// warning if more than 10 clashes found at interface
			List<AtomContact> clashing = interf.getContacts().getContactsWithinDistance(CLASH_DISTANCE);
			if (clashing.size() > 10) {
				logger.info(clashing.size() + " CLASHES!!!");
			}

			CrystalTransform transf1 = interf.getTransforms().getFirst();
			CrystalTransform transf2 = interf.getTransforms().getSecond();

			logger.info(new StringBuilder().append("Transf1: ")
					.append(SpaceGroup.getAlgebraicFromMatrix(transf1.getMatTransform())).append(". Transf2: ")
					.append(SpaceGroup.getAlgebraicFromMatrix(transf2.getMatTransform())).toString());

			String screwStr = "";
			if (transf2.getTransformType().isScrew()) {
				Vector3d screwTransl = transf2.getTranslScrewComponent();
				screwStr = new StringBuilder().append(" -- ").append(transf2.getTransformType().getShortName())
						.append(" with translation ")
						.append(String.format("(%5.2f,%5.2f,%5.2f)", screwTransl.x, screwTransl.y, screwTransl.z))
						.toString();

			}

			if (structure.isCrystallographic()) {
				int foldType = sg.getAxisFoldType(transf2.getTransformId());
				AxisAngle4d axisAngle = sg.getRotAxisAngle(transf2.getTransformId());

				logger.info(new StringBuilder().append(" ").append(foldType).append("-fold on axis ")
						.append(String.format("(%5.2f,%5.2f,%5.2f)", axisAngle.x, axisAngle.y, axisAngle.z))
						.append(screwStr).toString());
			}

			logger.info("Number of contacts: " + interf.getContacts().size());
			// System.out.println("Number of contacting atoms (from both molecules):
			// "+interf.getNumAtomsInContact());
			Pair<List<Group>> cores = interf.getCoreResidues(BSATOASA_CUTOFF, MIN_ASA_FOR_SURFACE);
			logger.info(new StringBuilder().append("Number of core residues at ")
					.append(String.format("%4.2f", BSATOASA_CUTOFF)).append(" bsa to asa cutoff: ")
					.append(cores.getFirst().size()).append(" ").append(cores.getSecond().size()).toString());
			logger.info("Interface area: %8.2f\n", interf.getTotalArea());

			if (interf.isIsologous()) {
				logger.info("Isologous");
			} else {
				logger.info("Heterologous");
			}

		}

		logger.info("Interface clusters (one per line): ");
		clusters.forEach(cluster -> {
			logger.info(cluster.getId() + ": ");
			cluster.getMembers().forEach(member -> logger.info(member.getId() + " "));
			System.out.println();
		});

	}

}
