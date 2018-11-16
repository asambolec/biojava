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
import org.biojava.nbio.structure.domain.LocalProteinDomainParser;
import org.biojava.nbio.structure.domain.pdp.Domain;
import org.biojava.nbio.structure.domain.pdp.Segment;
import org.biojava.nbio.structure.io.FileParsingParameters;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoDomainsplit {

	private static final Logger logger = LoggerFactory.getLogger(DemoDomainsplit.class);

	public static void main(String[] args) {

		DemoDomainsplit split = new DemoDomainsplit();

		// String pdbId = "3gly";
		String pdbId = "4hhb";

		split.basicLoad(pdbId);

	}

	public void basicLoad(String pdbId) {

		try {

			// This utility class can automatically download missing PDB files.
			AtomCache cache = new AtomCache();

			//
			// configure the parameters of file parsing (optional)

			FileParsingParameters params = new FileParsingParameters();

			// should the ATOM and SEQRES residues be aligned when creating the internal
			// data model?
			params.setAlignSeqRes(true);
			// should secondary structure get parsed from the file
			params.setParseSecStruc(false);

			// and set the params in the cache.
			cache.setFileParsingParams(params);

			// end of optional part

			Structure struc = cache.getStructure(pdbId);

			logger.info("structure loaded: " + struc);

			List<Domain> domains = LocalProteinDomainParser.suggestDomains(struc);

			logger.info("RESULTS: =====");
			domains.stream().map(dom -> {
				logger.info(new StringBuilder().append("DOMAIN:").append(dom.getSize()).append(" ")
						.append(dom.getScore()).toString());
				return dom.getSegments();
			}).flatMap(List::stream).forEach(s -> logger.info("   Segment: " + s));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}
}
