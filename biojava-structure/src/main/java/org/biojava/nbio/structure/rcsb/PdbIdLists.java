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
package org.biojava.nbio.structure.rcsb;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility classes for retrieving lists of PDB IDs.
 *
 * @author Andreas Prlic
 * @since 4.2.0
 */
public class PdbIdLists {

	private static final Logger logger = LoggerFactory.getLogger(PdbIdLists.class);
	public static final String SERVICELOCATION = "http://www.rcsb.org/pdb/rest/search";

	/**
	 * get the list of current PDB IDs
	 *
	 * @return list of current PDB IDs
	 * @throws IOException
	 */
	public static Set<String> getCurrentPDBIds() throws IOException {
		String xml = new StringBuilder().append("<orgPdbQuery>\n").append("    <version>head</version>\n")
				.append("    <queryType>org.pdb.query.simple.HoldingsQuery</queryType>\n")
				.append("    <description>Holdings : All Structures</description>\n")
				.append("    <experimentalMethod>ignore</experimentalMethod>\n")
				.append("    <moleculeType>ignore</moleculeType>\n").append("  </orgPdbQuery>").toString();

		return postQuery(xml);
	}

	/**
	 * Get the PDB IDs of all virus structures in the current PDB
	 *
	 * @return list of all virus structures
	 * @throws IOException
	 */
	public static Set<String> getAllViruses() throws IOException {
		String xml = new StringBuilder().append("<orgPdbQuery>\n").append("        <version>head</version>\n")
				.append("        <queryType>org.pdb.query.simple.EntriesOfEntitiesQuery</queryType>\n")
				.append("        <description>Entries of :Oligomeric state Search : Min Number of oligomeric state=PAU\n")
				.append("        and\n").append("        TaxonomyTree Search for Viruses\n")
				.append("                </description>\n")
				.append("        <parent><![CDATA[<orgPdbCompositeQuery version=\"1.0\">\n")
				.append("        <queryRefinement>\n")
				.append("        <queryRefinementLevel>0</queryRefinementLevel>\n").append("        <orgPdbQuery>\n")
				.append("        <version>head</version>\n")
				.append("        <queryType>org.pdb.query.simple.BiolUnitQuery</queryType>\n")
				.append("        <description>Oligomeric state Search : Min Number of oligomeric state=PAU </description>\n")
				.append("        <oligomeric_statemin>PAU</oligomeric_statemin>\n").append("        </orgPdbQuery>\n")
				.append("        </queryRefinement>\n").append("        <queryRefinement>\n")
				.append("        <queryRefinementLevel>1</queryRefinementLevel>\n")
				.append("        <conjunctionType>and</conjunctionType>\n").append("        <orgPdbQuery>\n")
				.append("        <version>head</version>\n")
				.append("        <queryType>org.pdb.query.simple.TreeEntityQuery</queryType>\n")
				.append("        <description>TaxonomyTree Search for Viruses</description>\n")
				.append("        <t>1</t>\n").append("        <n>10239</n>\n")
				.append("        <nodeDesc>Viruses</nodeDesc>\n").append("        </orgPdbQuery>\n")
				.append("        </queryRefinement>\n").append("        </orgPdbCompositeQuery>]]></parent>\n")
				.append("        </orgPdbQuery>").toString();

		return postQuery(xml);
	}

	/**
	 * get list of all current NMR structures
	 *
	 * @return list of NMR structures
	 * @throws IOException
	 */
	public static Set<String> getNMRStructures() throws IOException {
		String xml = new StringBuilder().append("<orgPdbCompositeQuery version=\"1.0\">\n")
				.append(" <queryRefinement>\n").append("  <queryRefinementLevel>0</queryRefinementLevel>\n")
				.append("  <orgPdbQuery>\n").append("    <version>head</version>\n")
				.append("    <queryType>org.pdb.query.simple.HoldingsQuery</queryType>\n")
				.append("    <description>Holdings : All Structures</description>\n")
				.append("    <experimentalMethod>ignore</experimentalMethod>\n")
				.append("    <moleculeType>ignore</moleculeType>\n").append("  </orgPdbQuery>\n")
				.append(" </queryRefinement>\n").append(" <queryRefinement>\n")
				.append("  <queryRefinementLevel>1</queryRefinementLevel>\n")
				.append("  <conjunctionType>and</conjunctionType>\n").append("  <orgPdbQuery>\n")
				.append("    <version>head</version>\n")
				.append("    <queryType>org.pdb.query.simple.ExpTypeQuery</queryType>\n")
				.append("    <description>Experimental Method is SOLUTION NMR</description>\n")
				.append("    <mvStructure.expMethod.value>SOLUTION NMR</mvStructure.expMethod.value>\n")
				.append("    <mvStructure.expMethod.exclusive>y</mvStructure.expMethod.exclusive>\n")
				.append("  </orgPdbQuery>\n").append(" </queryRefinement>\n").append("</orgPdbCompositeQuery>\n")
				.toString();

		return postQuery(xml);
	}

	/**
	 * get all PDB IDs of gag-polyproteins
	 *
	 * @return list of PDB IDs
	 * @throws IOException
	 */
	public static Set<String> getGagPolyproteins() throws IOException {
		String xml = new StringBuilder().append("<orgPdbCompositeQuery version=\"1.0\">\n")
				.append(" <queryRefinement>\n").append("  <queryRefinementLevel>0</queryRefinementLevel>\n")
				.append("  <orgPdbQuery>\n").append("    <version>head</version>\n")
				.append("    <queryType>org.pdb.query.simple.HoldingsQuery</queryType>\n")
				.append("    <description>Holdings : All Structures</description>\n")
				.append("    <experimentalMethod>ignore</experimentalMethod>\n")
				.append("    <moleculeType>ignore</moleculeType>\n").append("  </orgPdbQuery>\n")
				.append(" </queryRefinement>\n").append(" <queryRefinement>\n")
				.append("  <queryRefinementLevel>1</queryRefinementLevel>\n")
				.append("  <conjunctionType>and</conjunctionType>\n").append("  <orgPdbQuery>\n")
				.append("    <version>head</version>\n")
				.append("    <queryType>org.pdb.query.simple.MacroMoleculeQuery</queryType>\n")
				.append("    <description>Molecule : Gag-Pol polyprotein [A1Z651, O12158, P03355, P03366, P03367, P03369, P04584, P04585, P04586, P04587, P04588, P05896, P05897, P05959, P05961, P0C6F2, P12497, P12499, P18042, P19505 ... ]</description>\n")
				.append("    <macromoleculeName>A1Z651,O12158,P03355,P03366,P03367,P03369,P04584,P04585,P04586,P04587,P04588,P05896,P05897,P05959,P05961,P0C6F2,P12497,P12499,P18042,P19505,P19560,P20875,P24740,P35963,Q699E2,Q70XD7,Q72547,Q7SMT3,Q7SPG9,Q90VT5</macromoleculeName>\n")
				.append("  </orgPdbQuery>\n").append(" </queryRefinement>\n").append("</orgPdbCompositeQuery>")
				.toString();

		return postQuery(xml);
	}

	/**
	 * get all Transmembrane proteins
	 *
	 * @return list of PDB IDs
	 * @throws IOException
	 */
	public static Set<String> getTransmembraneProteins() throws IOException {
		String xml = new StringBuilder().append("  <orgPdbQuery>\n").append("    <version>head</version>\n")
				.append("    <queryType>org.pdb.query.simple.TreeQuery</queryType>\n")
				.append("    <description>TransmembraneTree Search for root</description>\n").append("    <t>19</t>\n")
				.append("    <n>0</n>\n").append("    <nodeDesc>root</nodeDesc>\n").append("  </orgPdbQuery>")
				.toString();

		return postQuery(xml);
	}

	public static Set<String> getNucleotides() throws IOException {
		String xml = new StringBuilder().append("<orgPdbQuery>\n").append("    <version>head</version>\n")
				.append("    <queryType>org.pdb.query.simple.ChainTypeQuery</queryType>\n")
				.append("    <description>Chain Type: there is not any Protein chain</description>\n")
				.append("    <containsProtein>N</containsProtein>\n").append("    <containsDna>?</containsDna>\n")
				.append("    <containsRna>?</containsRna>\n").append("    <containsHybrid>?</containsHybrid>\n")
				.append("  </orgPdbQuery>").toString();
		return postQuery(xml);
	}

	public static Set<String> getRibosomes() throws IOException {
		String xml = new StringBuilder().append("<orgPdbQuery>\n").append("    <version>head</version>\n")
				.append("    <queryType>org.pdb.query.simple.StructureKeywordsQuery</queryType>\n")
				.append("    <description>StructureKeywordsQuery: struct_keywords.pdbx_keywords.comparator=contains struct_keywords.pdbx_keywords.value=RIBOSOME </description>\n")
				.append("    <struct_keywords.pdbx_keywords.comparator>contains</struct_keywords.pdbx_keywords.comparator>\n")
				.append("    <struct_keywords.pdbx_keywords.value>RIBOSOME</struct_keywords.pdbx_keywords.value>\n")
				.append("  </orgPdbQuery>").toString();

		return postQuery(xml);
	}

	/**
	 * post am XML query (PDB XML query format) to the RESTful RCSB web service
	 *
	 * @param xml
	 * @return a list of PDB ids.
	 */
	public static Set<String> postQuery(String xml) throws IOException {

		// System.out.println(xml);

		URL u = new URL(SERVICELOCATION);

		String encodedXML = URLEncoder.encode(xml, "UTF-8");

		InputStream in = doPOST(u, encodedXML);

		Set<String> pdbIds = new TreeSet<>();

		try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

			String line;
			while ((line = rd.readLine()) != null) {

				pdbIds.add(line);

			}
			rd.close();
		}

		return pdbIds;

	}

	/**
	 * do a POST to a URL and return the response stream for further processing
	 * elsewhere.
	 *
	 *
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static InputStream doPOST(URL url, String data)

			throws IOException {

		// Send data

		URLConnection conn = url.openConnection();

		conn.setDoOutput(true);

		try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {

			wr.write(data);
			wr.flush();
		}

		// Get the response
		return conn.getInputStream();

	}

	public static void main(String[] args) {
		try {
			logger.info("Current PDB status: " + getCurrentPDBIds().size());
			logger.info("Virus structures: " + getAllViruses().size());
			logger.info("NMR structures: " + getNMRStructures().size());
			logger.info("Gag-polyproteins: " + getGagPolyproteins().size());
			logger.info("Transmembrane proteins: " + getTransmembraneProteins().size());
			logger.info("Nucleotide: " + getNucleotides().size());
			logger.info("Ribosomes: " + getRibosomes().size());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
