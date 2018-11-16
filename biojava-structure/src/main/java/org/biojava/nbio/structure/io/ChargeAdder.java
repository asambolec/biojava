/*
 *                  BioJava development code
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
 * Created on Jan. 22, 2016
 *
 */
package org.biojava.nbio.structure.io;

import java.util.List;

import org.biojava.nbio.structure.Atom;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.io.mmcif.ChemCompGroupFactory;
import org.biojava.nbio.structure.io.mmcif.model.ChemComp;
import org.biojava.nbio.structure.io.mmcif.model.ChemCompAtom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class to add appropriate charge information to a structure.
 * 
 * @author Anthony Bradley
 *
 */
public class ChargeAdder {

	private static final Logger logger = LoggerFactory.getLogger(ChargeAdder.class);

	/**
	 * Function to add the charges to a given structure.
	 */
	public static void addCharges(Structure structure) {
		// Loop through the models
		for (int i = 0; i < structure.nrModels(); i++) {
			// Now do the same for alt locs
			structure.getChains(i).forEach(c -> c.getAtomGroups().forEach(g -> {
				ChemComp thisChemComp = ChemCompGroupFactory.getChemComp(g.getPDBName());
				List<ChemCompAtom> chemAtoms = thisChemComp.getAtoms();
				chemAtoms.forEach(chemCompAtom -> {
					Atom atom = g.getAtom(chemCompAtom.getAtom_id());
					String stringCharge = chemCompAtom.getCharge();
					short shortCharge = 0;
					if (stringCharge != null) {
						if (!"?".equals(stringCharge)) {
							try {
								shortCharge = Short.parseShort(stringCharge);
							} catch (NumberFormatException e) {
								logger.error(e.getMessage(), e);
								logger.warn(new StringBuilder().append("Number format exception. Parsing '")
										.append(stringCharge).append("' to short").toString());
							}
						} else {
							logger.warn(
									new StringBuilder().append("? charge on atom ").append(chemCompAtom.getAtom_id())
											.append(" in group ").append(thisChemComp.getId()).toString());
						}
					} else {
						logger.warn(new StringBuilder().append("Null charge on atom ").append(chemCompAtom.getAtom_id())
								.append(" in group ").append(thisChemComp.getId()).toString());
					}
					if (atom != null) {
						atom.setCharge(shortCharge);
					}
					for (Group altLoc : g.getAltLocs()) {
						Atom altAtom = altLoc.getAtom(chemCompAtom.getAtom_id());
						if (altAtom != null) {
							altAtom.setCharge(shortCharge);
						}
					}
				});
			}));
		}
	}
}
