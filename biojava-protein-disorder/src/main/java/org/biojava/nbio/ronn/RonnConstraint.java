/*
 * @(#)RonnConstraint.java	1.0 June 2010
 *
 * Copyright (c) 2010 Peter Troshin
 *
 * JRONN version: 3.1
 *
 *        BioJava development code
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

package org.biojava.nbio.ronn;

/**
 * A collection of various constrain values used by RONN
 *
 * @author Peter Troshin
 * @version 1.0
 * @since 3.0.2
 */
public final class RonnConstraint {

	public final static float DEFAULT_DISORDER = 0.53f;
	public final static float DEFAULT_ORDER = 0.47f;

	public final static float DEFAULT_RANGE_PROBABILITY_THRESHOLD = 0.50f;

	// A b C D E F G H I j K L M N o P Q R S T u V W x Y
	//
	// 0 0 1 2 3 4 5 6 7 0 8 9 10 11 0 12 13 14 15 16 0 17 18 0 19

	public static final short[] INDEX = new short[] { 0, 0, 1, 2, 3, 4, 5, 6, 7, 0, 8, 9, 10, 11, 0, 12, 13, 14, 15, 16,
			0, 17, 18, 0, 19 };

	public static final short[][] Blosum62 = new short[][] {
			{ 4, 0, -2, -1, -2, 0, -2, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, -2, -3, -2 },
			{ 0, 9, -3, -4, -2, -3, -3, -1, -3, -1, -1, -3, -3, -3, -3, -1, -1, -1, -2, -2 },
			{ -2, -3, 6, 2, -3, -1, -1, -3, -1, -4, -3, 1, -1, 0, -2, 0, 1, -3, -4, -3 },
			{ -1, -4, 2, 5, -3, -2, 0, -3, 1, -3, -2, 0, -1, 2, 0, 0, 0, -3, -3, -2 },
			{ -2, -2, -3, -3, 6, -3, -1, 0, -3, 0, 0, -3, -4, -3, -3, -2, -2, -1, 1, 3 },
			{ 0, -3, -1, -2, -3, 6, -2, -4, -2, -4, -3, -2, -2, -2, -2, 0, 1, 0, -2, -3 },
			{ -2, -3, 1, 0, -1, -2, 8, -3, -1, -3, -2, 1, -2, 0, 0, -1, 0, -2, -2, 2 },
			{ -1, -1, -3, -3, 0, -4, -3, 4, -3, 2, 1, -3, -3, -3, -3, -2, -2, 1, -3, -1 },
			{ -1, -3, -1, 1, -3, -2, -1, -3, 5, -2, -1, 0, -1, 1, 2, 0, 0, -3, -3, -2 },
			{ -1, -1, -4, -3, 0, -4, -3, 2, -2, 4, 2, -3, -3, -2, -2, -2, -2, 3, -2, -1 },
			{ -1, -1, -3, -2, 0, -3, -2, 1, -1, 2, 5, -2, -2, 0, -1, -1, -1, -2, -1, -1 },
			{ -2, -3, 1, 0, -3, 0, -1, -3, 0, -3, -2, 6, -2, 0, 0, 1, 0, -3, -4, -2 },
			{ -1, -3, -1, -1, -4, -2, -2, -3, -1, -3, -2, -1, 7, -1, -2, -1, 1, -2, -4, -3 },
			{ -1, -3, 0, 2, -3, -2, 0, -3, 1, -2, 0, 0, -1, 5, 1, 0, 0, -2, -2, -1 },
			{ -1, -3, -2, 0, -3, -2, 0, -3, 2, -2, -1, 0, -2, 1, 5, -1, -1, -3, -3, -2 },
			{ 1, -1, 0, 0, -2, 0, -1, -2, 0, -2, -1, 1, -1, 0, -1, 4, 1, -2, -3, -2 },
			{ -1, -1, 1, 0, -2, 1, 0, -2, 0, -2, -1, 0, 1, 0, -1, 1, 4, -2, -3, -2 },
			{ 0, -1, -3, -2, -1, -3, -3, 3, -2, 1, 1, -3, -2, -2, -3, -2, -2, 4, -3, -1 },
			{ -3, -2, -4, -3, 1, -2, -2, -3, -3, -2, -1, -4, -4, -2, -3, -3, -3, -3, 11, 2 },
			{ -2, -2, -3, -2, 3, -3, 2, -1, -2, -1, -1, -2, -3, -1, -2, -2, -2, -1, 2, 7 } };
	static final float[] THRESHOLD0 = { 0.09847548204866169f, 0.5537946867723033f, 0.052493213903229766f,
			0.07714031903493762f };
	static final float[] THRESHOLD1 = { 0.09032956077766974f, 0.5167594539472075f, 0.04596823441915963f,
			0.06455503987769765f };
	static final float[] THRESHOLD2 = { 0.09266796710382286f, 0.5127732233896729f, 0.04963484289158484f,
			0.061048745226114226f };
	static final float[] THRESHOLD3 = { 0.10562230953899814f, 0.4488757690530404f, 0.04922765471815812f,
			0.0824807293665649f };
	static final float[] THRESHOLD4 = { 0.1163716006651586f, 0.5315238539228951f, 0.0556565226094971f,
			0.0714892726762588f };
	static final float[] THRESHOLD5 = { 0.09358976618303182f, 0.49296410198137725f, 0.054219917228374236f,
			0.06593535778132877f };
	static final float[] THRESHOLD6 = { 0.10526844980518248f, 0.4842710501752991f, 0.05215080306266067f,
			0.06518758643119664f };
	static final float[] THRESHOLD7 = { 0.08434396215650031f, 0.6007148113473553f, 0.05107636795876212f,
			0.10341362611675203f };
	static final float[] THRESHOLD8 = { 0.1309148603226209f, 0.5122245658772394f, 0.06436561753520677f,
			0.06639259175313134f };
	static final float[] THRESHOLD9 = { 0.18610705811017647f, 0.5279244438321989f, 0.0721466513318003f,
			0.07983168408322228f };
	static final byte MIN_SEQUENCE_LENGTH = 19;
	static final String HELP_MESSAGE = new StringBuilder().append(" \r\n")
			.append("JRONN version 3.1b usage 1 August 2011:\r\n")
			.append("java -jar JRONN_JAR_NAME -i=inputfile <OPTIONS>\r\n").append("\r\n")
			.append("Where -i=input file \r\n")
			.append("	Input file can contain one or more FASTA formatted sequences.\r\n").append("\r\n")
			.append("All OPTIONS are optional\r\n").append("Supported OPTIONS are: \r\n").append("	-o=output file\r\n")
			.append("	-d=disorder value\r\n").append("	-f=V or H \r\n").append("	-s=statistics file\r\n")
			.append("	-n=number of threads to use\r\n").append("OPTION DETAILED DESCRIPTION:\r\n")
			.append("	-o full path to the output file, if not specified \r\n").append("	standard out is used\r\n")
			.append("\r\n").append("	-d the value of disorder, defaults to 0.5\r\n").append("\r\n")
			.append("	-f output format, V for vertical, where the letters \r\n")
			.append("	of the sequence and corresponding disorder values are \r\n")
			.append("	output in two column layout. H for horizontal, where the\r\n")
			.append("	disorder values are provided under the letters of the \r\n")
			.append("	sequence. Letters and values separated by tabulation in\r\n")
			.append("	this case. Defaults to V.\r\n").append("\r\n")
			.append("	-s the file name to write execution statistics to.\r\n").append("\r\n")
			.append("	-n the number of threads to use. Defaults to the number of \r\n")
			.append("	cores available on the computer. n=1 mean sequential \r\n")
			.append("	processing. Valid values are 1 < n < (2 x num_of_cores)\r\n")
			.append("	Default value will give the best performance.\r\n").append("	\r\n").append("EXAMPLES: \r\n")
			.append("\r\n").append("	Predict disorder values for sequences from input file /home/input.fasta\r\n")
			.append("	output the results to the standard out. Use default disorder value\r\n")
			.append("	and utilise all cpus available on the computer.\r\n").append("\r\n")
			.append("	java -jar JRONN.JAR -i=/home/input.fasta\r\n").append("	\r\n")
			.append("	Predict disorder values for sequences from input file /home/input.fasta\r\n")
			.append("	output the results in horizontal layout to the /home/jronn.out, collect \r\n")
			.append("	execution statistics to /home/jronn.stat.txt file and limit the number \r\n")
			.append("	of threads to two. \r\n").append("	\r\n")
			.append("	java -jar JRONN.JAR -i=/home/input.fasta -o=/home/jronn.out -d=0.6 -n=2 -f=H\r\n")
			.append("	 \r\n").append("	The arguments can be provided in any order.\r\n").append("\r\n")
			.append("ABOUT THE PROGRAM: 	\r\n").append("	\r\n")
			.append("	JRONN is a Java implementation of RONN. JRONN is based on RONN and uses the \r\n")
			.append("	same model data, therefore gives the same predictions. Main motivation \r\n")
			.append("	behind JRONN development was providing an implementation of RONN more \r\n")
			.append("	suitable to use by the automated analysis pipelines and web services.  \r\n").append("	\r\n")
			.append("	Original version of RONN is described in Yang,Z.R., Thomson,R., \r\n")
			.append("	McMeil,P. and Esnouf,R.M. (2005) RONN: the bio-basis function neural network\r\n")
			.append("	technique applied to the detection of natively disordered regions in proteins  \r\n")
			.append("	Bioinformatics 21: 3369-3376\r\n").append("	See also http://www.strubi.ox.ac.uk/RONN\r\n")
			.append("	\r\n").append("	Author: Peter Troshin \r\n").append("	email: to.petr AT gmail DOT com\r\n")
			.append("	\r\n").append("	This is a free software which comes with no guarantees.\r\n")
			.append("	JRONN is distributed under Apache Licence version 2. The full version of \r\n")
			.append("	licence	can be obtained from http://www.apache.org/licenses/LICENSE-2.0\r\n").append("	")
			.toString();
	/**
	 * 700 - maximum number of lines (with sequence values) in the single model
	 * file.
	 */
	static final int maxD = 700;

	public static enum Threshold {
		T0(0, RonnConstraint.THRESHOLD0), T1(1, RonnConstraint.THRESHOLD1), T2(2, RonnConstraint.THRESHOLD2),
		T3(3, RonnConstraint.THRESHOLD3), T4(4, RonnConstraint.THRESHOLD4), T5(5, RonnConstraint.THRESHOLD5),
		T6(6, RonnConstraint.THRESHOLD6), T7(7, RonnConstraint.THRESHOLD7), T8(8, RonnConstraint.THRESHOLD8),
		T9(9, RonnConstraint.THRESHOLD9);

		private final int tnum;
		private final float[] values;

		private Threshold(final int tnum, final float[] values) {
			this.tnum = tnum;
			this.values = values;
		}

		public float[] getValues() {
			return values;
		}

		public static float[] getTreshold(final int number) {
			assert (number >= 0) && (number < 10) : number;
			for (final Threshold t : Threshold.values()) {
				if (t.tnum == number) {
					return t.values;
				}
			}
			return null;
		}

	}
}
