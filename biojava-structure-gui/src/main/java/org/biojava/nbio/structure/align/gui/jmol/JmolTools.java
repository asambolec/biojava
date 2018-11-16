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
package org.biojava.nbio.structure.align.gui.jmol;

import org.biojava.nbio.structure.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JmolTools {

	private static final Logger logger = LoggerFactory.getLogger(JmolTools.class);
	private static Pattern inscodePatter;
	static {
		inscodePatter = Pattern.compile("([0-9]+)([a-zA-Z]*)?");
	}

	/**
	 * get jmol style info: jmol style: [MET]508:A.CA/1 #3918 insertion code:
	 * [ASP]1^A:A.CA/1 #2
	 * 
	 * @param a .. the Atom
	 *
	 * @return a String representation in Jmol style of the PDB information of this
	 *         atom
	 */
	public static final String getPdbInfo(Atom a) {
		return getPdbInfo(a, true);
	}

	public static void main(String[] args) {

		Chain c = new ChainImpl();
		c.setId("X");

		Group g = new AminoAcidImpl();
		g.setResidueNumber(ResidueNumber.fromString("1A"));
		try {
			g.setPDBName("ALA");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		Atom a = new AtomImpl();
		a.setName("CA");
		g.addAtom(a);
		c.addGroup(g);

		logger.info(getPdbInfo(a));
	}

	// TODO: move this to AtomInfo class

	public static final String getPdbInfo(Atom a, boolean printResName) {
		String aa3 = "";

		String chain1 = "";
		String res1 = "";

		if (a != null) {
			Group g1 = a.getGroup();
			if (g1 != null) {
				aa3 = g1.getPDBName();
				res1 = g1.getResidueNumber().toString();
				Chain ch1 = g1.getChain();
				if (ch1 != null) {
					chain1 = ch1.getId();
				}
			}
		}

		StringBuilder buf = new StringBuilder();
		if (printResName) {
			if (!"".equals(aa3)) {
				buf.append("[");
				buf.append(aa3);
				buf.append("]");
			}
		}
		if (!"".equals(res1)) {

			// let's check if there is an insertion code...
			Matcher matcher = inscodePatter.matcher(res1);

			boolean found = matcher.find();
			if (!found) {
				logger.error("JmolTools: could not parse the residue number string " + res1);
				buf.append(res1);
			} else {
				String residueNumber = matcher.group(1);
				String insCode = matcher.group(2);
				buf.append(residueNumber);
				if (insCode != null && !("".equals(insCode))) {
					buf.append("^");
					buf.append(insCode);
				}
			}

		}

		if (!"".equals(chain1)) {
			buf.append(":");
			buf.append(chain1);
		}
		return buf.toString();
	}
}
