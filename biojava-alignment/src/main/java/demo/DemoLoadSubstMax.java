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

import org.biojava.nbio.core.alignment.matrices.SubstitutionMatrixHelper;
import org.biojava.nbio.core.alignment.template.SubstitutionMatrix;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoLoadSubstMax {

	private static final Logger logger = LoggerFactory.getLogger(DemoLoadSubstMax.class);

	public static void main(String[] args) {
		// that's the PAM250 matrix (named a bit unclear in AAindex...)
		String max2 = "DAYM780301";
		SubstitutionMatrix<AminoAcidCompound> substMax2 = SubstitutionMatrixHelper.getMatrixFromAAINDEX(max2);
		logger.info("PAM250 matrix: " + substMax2);

		// and here BLOSUM62...
		String max3 = "HENS920102";
		SubstitutionMatrix<AminoAcidCompound> substMax3 = SubstitutionMatrixHelper.getMatrixFromAAINDEX(max3);
		logger.info("%s matrix: %s", max3, substMax3);
		System.out.println();

		// This one I developed a while ago to be optimised for the alignment of
		// distantly related sequences
		String matrixName4 = "PRLA000101";
		SubstitutionMatrix<AminoAcidCompound> substMax4 = SubstitutionMatrixHelper.getMatrixFromAAINDEX(matrixName4);
		logger.info("%s matrix: %s", matrixName4, substMax4);
		System.out.println();
	}
}
