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
 * Created on Mar 18, 2010
 * Author: Andreas Prlic
 *
 */

package org.biojava.nbio.structure.align.gui;

import org.biojava.nbio.structure.align.webstart.BrowserOpener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemInfo {

	private static final Logger logger = LoggerFactory.getLogger(SystemInfo.class);
	/*
	 * This is a default list of system properties that we will use if the Security
	 * Manager doesn't let us extract the "real" list.
	 */
	public static final String defaultProperties = new StringBuilder().append("").append("browser ")
			.append("file.separator ").append("java.class.version ").append("java.vendor ").append("java.vendor.url ")
			.append("java.version ").append("line.separator ").append("os.arch ").append("os.name ")
			.append("os.version ").append("path.separator ").toString();
	public static final String hexPropertyNames = new StringBuilder().append(" file.separator ")
			.append("line.separator ").append("path.separator ").toString();
	public static final String urlPropertyNames = new StringBuilder().append(" browser.vendor.url ")
			.append(" java.class.path ").append("java.home ").append("user.dir ").append("user.home ")
			.append("user.name ").toString();
	Box vBox;
	String msg;
	EtchedBorder border;

	public SystemInfo() {
		border = new EtchedBorder();
		msg = "";
		try {
			Properties props = System.getProperties();
			/*
			 * Unfortunately, enumerating System.getProperties() returns them in an
			 * unsatisfactory order. To make the display esthetically pleasing, we'll
			 * extract the property names (i.e. the keys) into a vector, then sort the
			 * vector, then use the vector as an enumeration. props.size() is not
			 * trustworthy (bug in MRJ?)
			 */
			// border.setLabelText ("System Properties");
			/*
			 * Count the actual size of the System property list.
			 */
			int size = 0;
			Enumeration<?> enumo = props.propertyNames();
			while (enumo.hasMoreElements()) {
				++size;
				enumo.nextElement();
			}
			String[] names = new String[size];
			enumo = props.propertyNames();
			for (int i = 0; enumo.hasMoreElements(); i++) {
				names[i] = (String) enumo.nextElement();
			}
			if (size < 1) {
				msg = "No System Properties";
			} else {
				quickSort(names, 0, names.length - 1);
				for (int i = 0; i < size; i++) {
					addOneSystemProperty(names[i]);
				}
			}
		} catch (SecurityException e) {
			logger.error(e.getMessage(), e);
			// border.setLabelText ("Default Applet Properties");
			StringTokenizer t = new StringTokenizer(defaultProperties, " ");
			while (t.hasMoreElements()) {
				addOneSystemProperty(t.nextToken());
			}
		} catch (Exception e) {
			append("Strange Exception getting system properties: " + e);
		}
	}

	private void append(String txt) {
		msg += txt;
	}

	/**
	 * Stripped-down QuickSort.
	 * 
	 * @param vector     The vector of strings to sort
	 * @param startIndex The first element to sort
	 * @param endIndex   The last element to sort
	 *
	 *                   example use:
	 * 
	 *                   <pre>
	 *                   JavaInfo.quickSort(vector, 0, vector.length - 1);
	 *                   </pre>
	 */
	public static void quickSort(String[] vector, int startIndex, int endIndex) {
		int i = startIndex;
		int j = endIndex;
		String pivot = vector[(i + j) / 2];
		do {
			while (i < endIndex && pivot.compareTo(vector[i]) > 0) {
				++i;
			}
			while (j > startIndex && pivot.compareTo(vector[j]) < 0) {
				--j;
			}
			if (i < j) {
				String temp = vector[i];
				vector[i] = vector[j];
				vector[j] = temp;
			}
			if (i <= j) {
				++i;
				--j;
			}
		} while (i <= j);
		if (startIndex < j) {
			quickSort(vector, startIndex, j);
		}
		if (i < endIndex) {
			quickSort(vector, i, endIndex);
		}
	}

	public void addOneSystemProperty(String name) {
		try {
			String propValue = System.getProperty(name);
			/*
			 * On the Macintosh (under MRJ), a bunch of font names are loaded into the
			 * property list. We toss them to avoid confusion.
			 */
			if (propValue != null && !name.equals(propValue)) {
				append(new StringBuilder().append("<b>").append(name).append("</b>:").toString());
				boolean isReadable = true;
				for (int i = 0; i < propValue.length(); i++) {
					char c = propValue.charAt(i);
					if (isControlCharacter(c)) {
						isReadable = false;
						break;
					}
				}
				if (!isReadable) {
					for (int i = 0; i < propValue.length(); i++) {
						char c = propValue.charAt(i);
						if (Character.isLetterOrDigit(c)) {
							append(new StringBuilder().append(" '").append(c).append("'").toString());
						} else {
							append(" 0x");
							if (c < 0x10) {
								append("0");
							}
							append(Integer.toHexString(c));
						}
					}
				} else if (isURLProperty(name)) {
					StringBuilder fixed = new StringBuilder();
					int start = 0;
					int hit = 0;
					while ((hit = propValue.indexOf('%', start)) >= 0) {
						fixed.append(propValue.substring(start, hit));
						int value = Integer.parseInt(propValue.substring(hit + 1, hit + 3), 16);
						fixed.append(((char) value));
						start = hit + 3;
					}
					append(new StringBuilder().append(" \"").append(fixed).append("\"").toString());
				} else {
					append(" " + propValue);
				}
				append("<br/>");
			} /* If the property name was found */
		} /* Try to fetch the property name */
		catch (SecurityException e) {
			logger.error(e.getMessage(), e);
			append(name + ": Security Exception\n");
		} catch (Exception e) {
			append(new StringBuilder().append(name).append(": ").append(e).append("\n").toString());
		}
	}

	protected boolean isHexProperty(String thisName) {
		int index = hexPropertyNames.indexOf(new StringBuilder().append(" ").append(thisName).append(" ").toString());
		return (index >= 0);
	}

	protected boolean isURLProperty(String thisName) {
		int index = urlPropertyNames.indexOf(new StringBuilder().append(" ").append(thisName).append(" ").toString());
		return (index >= 0);
	}

	/**
	 * Replicate the Java 1.1 isISOControl method (for Java 1.0.2 compatibility)
	 */
	private boolean isControlCharacter(char c) {
		return ((c >= '\u0000' && c <= '\u001f') || (c >= '\u007f' && c <= '\u009f'));
	}

	public String getMessage() {
		return msg;
	}

	public void showDialog() {
		JDialog dialog = new JDialog();

		dialog.setSize(new Dimension(500, 650));

		String msg = getMessage();
		JEditorPane txt = new JEditorPane("text/html", msg);
		txt.setEditable(false);

		JScrollPane scroll = new JScrollPane(txt);
		scroll.setSize(new Dimension(300, 500));
		vBox = Box.createVerticalBox();
		vBox.add(scroll);

		txt.addHyperlinkListener((HyperlinkEvent e) -> {

			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				String href = e.getDescription();
				BrowserOpener.showDocument(href);
			}
			if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
				// change the mouse curor
				vBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
				vBox.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		});

		JButton close = new JButton("Close");

		close.addActionListener((ActionEvent event) -> {
			Object source = event.getSource();

			JButton but = (JButton) source;
			Container parent = but.getParent().getParent().getParent().getParent().getParent().getParent();

			JDialog dia = (JDialog) parent;
			dia.dispose();
		});

		Box hBoxb = Box.createHorizontalBox();
		hBoxb.add(Box.createGlue());
		hBoxb.add(close, BorderLayout.EAST);

		vBox.add(hBoxb);

		dialog.getContentPane().add(vBox);
		dialog.setVisible(true);

	}
}
