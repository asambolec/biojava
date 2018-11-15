/*
 * @(#)ORonn.java 1.0 June 2010
 *
 * Copyright (c) 2010 Peter Troshin
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

import org.biojava.nbio.data.sequence.FastaSequence;
import org.biojava.nbio.data.sequence.SequenceUtil;
import org.biojava.nbio.ronn.ModelLoader.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;

/**
 * Fully re-factored and enhanced version of RONN.
 *
 * This class does the calculation and contains the main for the command line
 * client.
 *
 * @author Peter Troshin
 * @version 1.0
 * @since 3.0.2
 * 
 *        TODO refactor
 */
public final class ORonn implements Callable<ORonn> {

	private static final Logger logger = LoggerFactory.getLogger(ORonn.class);

	private static final DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG,
			Locale.US);

	private static final NumberFormat nformat = NumberFormat.getInstance();
	static {
		ORonn.nformat.setMaximumFractionDigits(2);
	}

	static final byte NUMBER_OF_MODELS = 10;
	private final FastaSequence sequence;
	private final ModelLoader mloader;
	private final PrintWriter out;
	private final ResultLayout layout;
	private final PrintWriter stat;
	private final Timer timer;
	private final float disorder;

	// This gets initialized after calling a call method!
	private float[] cummulativeScore;

	ORonn(final FastaSequence sequence, final ModelLoader mloader, final InputParameters params) throws IOException {
		this.sequence = sequence;
		this.mloader = mloader;
		out = params.getOutputWriter();
		assert out != null;
		layout = params.getFormat();
		stat = params.getStatWriter();
		disorder = params.getDisorder();
		timer = new Timer(TimeUnit.MILLISECONDS);
	}

	// This constructor is for API calls where the caller collects the results
	// directly
	ORonn(final FastaSequence sequence, final ModelLoader mloader) throws IOException {
		this.sequence = sequence;
		this.mloader = mloader;
		out = new PrintWriter(new NullOutputStream());
		layout = ResultLayout.HORIZONTAL;
		stat = new PrintWriter(new NullOutputStream());
		disorder = RonnConstraint.DEFAULT_DISORDER;
		timer = new Timer(TimeUnit.MILLISECONDS);
	}

	void writeResults(final float[] meanScores, final char[] seqs) {

		synchronized (out) {
			out.println(">" + sequence.getId());
			if (layout == ResultLayout.VERTICAL) {
				for (int i = 0; i < meanScores.length; i++) {
					out.printf("%c\t%.2f%n", seqs[i], meanScores[i]);
					// out.printf("%c\t%f%n", seqs[i], meanScores[i]);
				}
			} else {
				final StringBuilder seqLine = new StringBuilder();
				final StringBuilder resultLine = new StringBuilder();
				final String spacer = "\t";
				for (int i = 0; i < meanScores.length; i++) {
					seqLine.append(seqs[i]);
					seqLine.append(spacer);
					resultLine.append(ORonn.nformat.format(meanScores[i]));
					resultLine.append(spacer);
				}
				out.println(seqLine.toString());
				out.println(resultLine.toString());
			}
			out.println();
			out.flush();
		}
	}

	static boolean isValidSequence(final FastaSequence fsequence) {
		assert fsequence != null;
		return fsequence.getLength() > RonnConstraint.MIN_SEQUENCE_LENGTH;
	}

	@Override
	public ORonn call() throws IOException {
		final String seq = sequence.getSequence();
		// Calculate for each model
		for (int m = 0; m < ORonn.NUMBER_OF_MODELS; m++) {
			final Model model = mloader.getModel(m);
			final ORonnModel rmodel = new ORonnModel(seq, model, disorder);
			final float[] scores = rmodel.detect();
			addScore(scores);
		}

		final char[] ch = seq.toCharArray();
		final float[] meanScores = getMeanScores();
		assert meanScores.length == seq.length() : "Scores are not calculated for " + "all residues!";
		writeResults(meanScores, ch);
		stat.println(new StringBuilder().append(timer.getTotalTime()).append("ms prediction completed for ")
				.append(sequence.getId()).toString());
		return this;
	}

	private void addScore(final float[] scores) {
		// For the first time just add all elements
		if (cummulativeScore == null) {
			cummulativeScore = scores;
			return;
		}
		if (cummulativeScore.length != scores.length) {
			throw new IllegalArgumentException(new StringBuilder().append("Expected ").append(cummulativeScore.length)
					.append(" but get ").append(scores.length).toString());
		}
		for (int i = 0; i < scores.length; i++) {
			cummulativeScore[i] += scores[i];
		}
	}

	float[] getMeanScores() {
		final float[] meanScores = new float[cummulativeScore.length];
		for (int i = 0; i < cummulativeScore.length; i++) {
			meanScores[i] = cummulativeScore[i] / ORonn.NUMBER_OF_MODELS;
		}
		return meanScores;
	}

	static void printUsage() {
		logger.error(RonnConstraint.HELP_MESSAGE);
	}

	static boolean isValidSequenceForRonn(final FastaSequence fsequence, final PrintWriter stat) {
		boolean valid = true;
		String message = "";
		if (!ORonn.isValidSequence(fsequence)) {
			message = new StringBuilder().append("IGNORING sequence ").append(fsequence.getId())
					.append(" as its too short. Minimum sequence length for disorder prediction is ")
					.append(RonnConstraint.MIN_SEQUENCE_LENGTH + 1).append(" characters!").toString();
			stat.println(message);
			logger.warn(message);
			valid = false;
		}
		final String sequence = fsequence.getSequence();
		if (!(SequenceUtil.isProteinSequence(sequence) || SequenceUtil.isAmbiguosProtein(sequence))) {
			message = new StringBuilder().append("IGNORING sequence ").append(fsequence.getId())
					.append(" as it is not a protein sequence!").toString();
			stat.println(message);
			logger.warn(message);
			valid = false;
		}
		return valid;
	}

	static void validateSequenceForRonn(final FastaSequence fsequence) {

		String message = "";
		if (!ORonn.isValidSequence(fsequence)) {
			message = new StringBuilder().append("IGNORING sequence ").append(fsequence.getId())
					.append(" as its too short. Minimum sequence length for disorder prediction is ")
					.append(RonnConstraint.MIN_SEQUENCE_LENGTH + 1).append(" characters!").toString();
			throw new IllegalArgumentException(message);
		}
		final String sequence = fsequence.getSequence();

		if (SequenceUtil.isAmbiguosProtein(sequence)) {
			logger.warn("Sequence is ambiguous!");
		}

		if (!(SequenceUtil.isProteinSequence(sequence))) {
			logger.warn("Does not look like a protein sequence!");
		}

		if (SequenceUtil.isProteinSequence(sequence) || SequenceUtil.isAmbiguosProtein(sequence)) {
			return;
		}
		message = new StringBuilder().append("IGNORING sequence ").append(fsequence.getId())
				.append(" as it is not a protein sequence!").toString();
		throw new IllegalArgumentException(message);
	}

	private static InputParameters parseArguments(final String[] args) throws IOException {
		final InputParameters prms = new InputParameters();
		for (String arg : args) {
			final String prm = arg.trim().toLowerCase();
			if (prm.startsWith(InputParameters.inputKey)) {
				prms.setFilePrm(arg, InputParameters.inputKey);
			}
			if (prm.startsWith(InputParameters.outputKey)) {
				prms.setFilePrm(arg, InputParameters.outputKey);
			}
			if (prm.startsWith(InputParameters.disorderKey)) {
				prms.setDisorder(prm);
			}
			if (prm.startsWith(InputParameters.formatKey)) {
				prms.setFormat(prm);
			}
			if (prm.startsWith(InputParameters.statKey)) {
				prms.setFilePrm(arg, InputParameters.statKey);
			}
			if (prm.startsWith(InputParameters.threadKey)) {
				prms.setThreadNum(prm);
			}

		}
		return prms;
	}

	public static void main(final String[] args) throws IOException {

		if ((args.length == 0) || (args.length > 5)) {
			ORonn.printUsage();
			System.exit(1);
		}
		final InputParameters prms = ORonn.parseArguments(args);

		final PrintWriter stat = prms.getStatWriter();
		stat.println(new StringBuilder().append("Using parameters: \n[").append(prms).append("]").toString());

		if (prms.getInput() == null) {
			logger.error("Input is not defined! ");
			ORonn.printUsage();
			System.exit(1);
		}
		stat.println("Calculation started: " + ORonn.DATE_FORMAT.format(new Date()));

		final Timer timer = new Timer();
		// The stream is closed after reading inside readFasta
		final List<FastaSequence> sequences = SequenceUtil.readFasta(new FileInputStream(prms.getInput()));
		stat.println(timer.getStepTime(TimeUnit.MILLISECONDS) + "ms input file loaded");
		stat.println(
				new StringBuilder().append("Input file has ").append(sequences.size()).append(" sequences").toString());

		final ModelLoader mloader = new ModelLoader();
		mloader.loadModels();

		final PrintWriter out = prms.getOutputWriter();
		assert out != null;

		// do serial execution
		if (prms.getThreadNum() == 1) {
			stat.println("Running predictions serially");
			ORonn.predictSerial(sequences, prms, mloader);
		} else {
			// Run predictions in parallel
			stat.print("Running preditions in parallel - ");
			stat.println(
					new StringBuilder().append("Using ").append(prms.getThreadNum()).append(" threads").toString());
			ORonn.predictParallel(sequences, prms, mloader);
		}

		stat.println(new StringBuilder().append("Total calculation time: ").append(timer.getTotalTime()).append("s ")
				.toString());
		stat.println("Calculation completed: " + ORonn.DATE_FORMAT.format(new Date()));
		stat.close();
		out.flush();
		out.close();
	}

	static void predictSerial(final List<FastaSequence> fsequences, final InputParameters prms,
			final ModelLoader mloader) throws IOException {
		for (final FastaSequence sequence : fsequences) {
			if (!ORonn.isValidSequenceForRonn(sequence, prms.getStatWriter())) {
				continue;
			}
			final ORonn ronn = new ORonn(sequence, mloader, prms);
			ronn.call();
		}
	}

	static void predictParallel(final List<FastaSequence> fsequences, final InputParameters prms,
			final ModelLoader mloader) throws IOException {
		final PrintWriter stat = prms.getStatWriter();

		// Do parallel execution
		final ExecutorService executor = new ThreadPoolExecutor(prms.getThreadNum(), prms.getThreadNum(), 0L,
				TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
		try {
			for (final FastaSequence sequence : fsequences) {
				if (!ORonn.isValidSequenceForRonn(sequence, stat)) {
					continue;
				}
				final ORonn ronn = new ORonn(sequence, mloader, prms);
				/*
				 * To get stack traces from tasks one need to obtain a Future from this method
				 * and call its get() method. Otherwise some task may end up with exception but
				 * unnoticed
				 */
				executor.submit(ronn);
			}
			executor.shutdown();
			final int timeOut = (fsequences.size() < 60) ? 60 : fsequences.size();
			stat.println(new StringBuilder().append("All task submitted. Waiting for complition for ")
					.append("maximum of ").append(timeOut).append(" minutes").toString());
			executor.awaitTermination(timeOut, TimeUnit.MINUTES);
		} catch (final InterruptedException e) {
			logger.error(new StringBuilder().append("Execution is terminated! ")
					.append("Terminated by either by the system or the timeout. ")
					.append("Maximum of 1 minute is allowed for one sequence analisys! ")
					.append("If it took longer to complite this analysis ").append("the program is terminated.")
					.toString(), e);
		} finally {
			executor.shutdownNow();
		}
	}

	/**
	 *
	 * @author pvtroshin
	 *
	 *         VERTICAL - where the letters of the sequence and corresponding
	 *         disorder values are output in two column layout.
	 *
	 *         HORIZONTAL where the disorder values are provided under the letters
	 *         of the sequence. Letters and values separated by tabulation in this
	 *         case.
	 *
	 */
	static enum ResultLayout {
		VERTICAL, HORIZONTAL
	}

} // class end
