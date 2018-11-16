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
 * Created on Apr 6, 2010
 * Author: Andreas Prlic
 *
 */

package org.biojava.nbio.structure.align.gui;

import org.biojava.nbio.structure.align.util.UserConfiguration;
import org.biojava.nbio.structure.align.webstart.WebStartMain;
import org.biojava.nbio.structure.gui.util.PDBUploadPanel;
import org.biojava.nbio.structure.io.LocalPDBDirectory.FetchBehavior;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.UIManager;

public class ConfigPDBInstallPanel extends JPanel {

	private static final Logger logger = LoggerFactory.getLogger(ConfigPDBInstallPanel.class);

	/**
	 *
	 */
	private static final long serialVersionUID = -1055193854675583808L;

	private static ConfigPDBInstallPanel instance = new ConfigPDBInstallPanel();

	static JDialog dialog;

	JCheckBox fromFtp;

	JComboBox fileType;

	JTextField pdbDir;

	private ConfigPDBInstallPanel() {

		UserConfiguration config = WebStartMain.getWebStartConfig();

		fileType = PDBUploadPanel.getFileFormatSelect();

		Box vBox = Box.createVerticalBox();
		Box hBox = Box.createHorizontalBox();

		pdbDir = new JTextField(20);

		String conf = System.getProperty(UserConfiguration.PDB_DIR);
		if (conf != null) {
			pdbDir.setText(conf);
		}

		JLabel l01 = new JLabel("Directory containing PDBs");
		// panel.add(l01);
		// panel.add(f);

		hBox.add(l01);
		hBox.add(pdbDir);
		vBox.add(hBox);

		if (config != null) {
			pdbDir.setText(config.getPdbFilePath());
		}

		Action action = new ChooseDirAction(pdbDir, config);

		JButton chooser = new JButton(action);
		hBox.add(chooser);

		Box hBox3 = Box.createHorizontalBox();
		JLabel label2 = new JLabel("Fetch missing PDBs from ftp site:");
		fromFtp = new JCheckBox();
		fromFtp.setMnemonic(KeyEvent.VK_F);
		fromFtp.setSelected(true);
		if (config != null) {
			fromFtp.setSelected(config.getFetchBehavior() != FetchBehavior.LOCAL_ONLY);
		}

		JLabel ftype = new JLabel("File format:");

		hBox3.add(Box.createGlue());
		hBox3.add(ftype);
		hBox3.add(fileType);
		hBox3.add(Box.createGlue());
		hBox3.add(label2);
		hBox3.add(fromFtp);

		vBox.add(hBox3);
		this.add(vBox);

	}

	public static void showDialog() {
		if (dialog != null) {

			dialog.setVisible(true);
			return;
		}

		dialog = new JDialog();
		dialog.setSize(new Dimension(600, 300));
		Box vBox = Box.createVerticalBox();

		vBox.add(instance);

		UIManager.LookAndFeelInfo[] lookAndFeels = UIManager.getInstalledLookAndFeels();
		JPanel panel = new JPanel();

		for (UIManager.LookAndFeelInfo lookAndFeel : lookAndFeels) {
			JButton button = new JButton(lookAndFeel.getName());
			button.addActionListener(new MyAction(dialog));
			panel.add(button);
		}

		JTabbedPane jTab = new JTabbedPane();
		jTab.addTab("Select User Interface", null, panel, "Select the Look and Feel of the application.");

		vBox.add(jTab);
		vBox.add(Box.createGlue());

		JButton apply = new JButton("Apply");
		apply.addActionListener((ActionEvent event) -> {
			instance.applyValues();
			dialog.dispose();
		});

		JButton close = new JButton("Cancel");

		close.addActionListener((ActionEvent event) -> dialog.dispose());

		Box hBoxb = Box.createHorizontalBox();
		hBoxb.add(Box.createGlue());
		hBoxb.add(close, BorderLayout.EAST);
		hBoxb.add(apply, BorderLayout.EAST);
		vBox.add(hBoxb);

		dialog.getContentPane().add(vBox);
		dialog.setVisible(true);

	}

	protected void applyValues() {
		UserConfiguration config = WebStartMain.getWebStartConfig();

		String dir = pdbDir.getText();
		config.setPdbFilePath(dir);
		boolean fromFtpF = fromFtp.isSelected();
		if (fromFtpF) {
			config.setFetchBehavior(FetchBehavior.FETCH_REMEDIATED);
		} else {
			config.setFetchBehavior(FetchBehavior.LOCAL_ONLY);
		}
		String fileFormat = (String) fileType.getSelectedItem();
		config.setFileFormat(fileFormat);

	}

	public JCheckBox getFromFtp() {
		return fromFtp;
	}

	public void setFromFtp(JCheckBox fromFtp) {
		logger.info(String.valueOf(fromFtp));
		this.fromFtp = fromFtp;
	}

	public JTextField getPDBDirField() {
		return pdbDir;
	}

	public void setPDBDirField(JTextField dir) {
		pdbDir = dir;
	}

}

class MyAction implements ActionListener {
	private static final Logger logger = LoggerFactory.getLogger(MyAction.class);
	JDialog dialog;

	public MyAction(JDialog dialog) {
		this.dialog = dialog;
	}

	@Override
	@SuppressWarnings("unused")
	public void actionPerformed(ActionEvent ae) {
		Object EventSource = ae.getSource();
		String lookAndFeelClassName = null;
		UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
		for (UIManager.LookAndFeelInfo look : looks) {
			if (ae.getActionCommand().equals(look.getName())) {
				lookAndFeelClassName = look.getClassName();
				break;
			}
		}
		try {
			UIManager.setLookAndFeel(lookAndFeelClassName);

			SwingUtilities.updateComponentTreeUI(dialog);

			SwingUtilities.updateComponentTreeUI(AlignmentGui.getInstanceNoVisibilityChange());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			JOptionPane.showMessageDialog(dialog, "Can't change look and feel:" + lookAndFeelClassName, "Invalid PLAF",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
