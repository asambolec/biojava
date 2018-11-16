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
package org.biojava.nbio.structure.align.gui;

import org.biojava.nbio.structure.align.FarmJob;
import org.biojava.nbio.structure.align.events.AlignmentProgressListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a GUI that allows to watch progress as multiple alignments are being
 * processed.
 *
 * @author Andreas Prlic
 *
 */
public class GUIAlignmentProgressListener extends JPanel implements AlignmentProgressListener, ActionListener {

	private static final Logger logger = LoggerFactory.getLogger(GUIAlignmentProgressListener.class);

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	int alignmentsProcessed;

	JProgressBar progressBar;
	private JTextArea taskOutput;
	private JButton stopButton;

	FarmJob farmJob;

	public GUIAlignmentProgressListener() {

		super(new BorderLayout());
		stopButton = new JButton("Stop");
		stopButton.setActionCommand("Stop");
		stopButton.addActionListener(this);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		taskOutput = new JTextArea(5, 20);
		taskOutput.setMargin(new Insets(5, 5, 5, 5));
		taskOutput.setEditable(false);

		JPanel panel = new JPanel();
		panel.add(stopButton);
		panel.add(progressBar);

		add(panel, BorderLayout.PAGE_START);
		add(new JScrollPane(taskOutput), BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

	}

	public FarmJob getFarmJob() {
		return farmJob;
	}

	public void setFarmJob(FarmJob parent) {
		this.farmJob = parent;
	}

	/**
	 * Invoked when the user presses the stop button.
	 */
	@Override
	public void actionPerformed(ActionEvent evt) {

		// System.out.println("stopping!");
		logStatus("terminating");
		logStatus(" Total alignments processed: " + alignmentsProcessed);
		stopButton.setEnabled(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		progressBar.setIndeterminate(true);
		progressBar.setStringPainted(false);
		logger.info("terminating jobs");

		farmJob.terminate();

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		progressBar.setIndeterminate(false);

	}

	@Override
	public void alignmentEnded() {

		alignmentsProcessed++;

		// System.out.println("aligned " + alignmentsProcessed );
		int v = progressBar.getValue();

		progressBar.setValue(v + 1);
		progressBar.setString(String.valueOf(v));
		synchronized (this) {
			notifyAll();
		}

	}

	@Override
	public void alignmentStarted(String name1, String name2) {
		logStatus(new StringBuilder().append("#").append(progressBar.getValue()).append(" starting alignment of ")
				.append(name1).append(" ").append(name2).toString());
	}

	@Override
	public void downloadingStructures(String name) {
		logStatus("Downloading " + name);
	}

	@Override
	public void logStatus(String message) {
		taskOutput.append(message + "\n");
	}

	@Override
	public void requestingAlignmentsFromServer(int nrAlignments) {
		logStatus(new StringBuilder().append("Requesting ").append(nrAlignments).append(" alignments to be calculated")
				.toString());
		progressBar.setMaximum(nrAlignments);
		progressBar.setValue(0);
		synchronized (this) {
			notifyAll();
		}

	}

	@Override
	public void sentResultsToServer(int nrAlignments, String serverMessage) {
		logStatus(new StringBuilder().append("sent alignment results back to server. Server responded: >")
				.append(serverMessage).append("<").toString());
		progressBar.setValue(0);
		synchronized (this) {
			notifyAll();
		}
	}

}
