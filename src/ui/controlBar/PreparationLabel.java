package ui.controlBar;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import ui.Language;

public class PreparationLabel extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel title;
	private JCheckBox fastMode;
	private JButton deployButton;

	private int troops;
	private String country;

	private List<PreparationLabelListener> listeners;

	public PreparationLabel(int troops, String country) {
		if (country == null)
			country = "";
		this.troops = troops;
		this.country = country;

		listeners = new ArrayList<>();

		this.setMaximumSize(new Dimension(9999999, 500));

		title = new JLabel("", SwingConstants.CENTER);
		title.setFont(new Font("Dialog", Font.PLAIN, 12));
		updateLabel();

		fastMode = new JCheckBox(Language.get("preparation_fast"));
		fastMode.setFont(new Font("Dialog", Font.PLAIN, 12));
		fastMode.setOpaque(false);
		fastMode.addChangeListener(e -> {
			for (PreparationLabelListener dl : listeners)
				dl.preparationActionPerformed(PreparationLabelListener.TYPE_FAST_MODE_CHANGED);
		});

		deployButton = new JButton(Language.get("preparation_confirm"));
		deployButton.addActionListener(a -> {
			for (PreparationLabelListener dl : listeners)
				dl.preparationActionPerformed(PreparationLabelListener.TYPE_DEPLOY);
		});		

		GroupLayout gl = new GroupLayout(this);
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(title).addComponent(fastMode).addGap(0, 50, 9999)
				.addComponent(deployButton));

		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup().addComponent(title)
						.addComponent(fastMode, GroupLayout.Alignment.CENTER)
						.addComponent(deployButton, GroupLayout.Alignment.TRAILING)));

		this.setLayout(gl);
	}

	private void updateLabel() {
		title.setText("<html><div style='text-align: center;'>" + Language.get("preparation_title") + "<br>"
				+ Language.get("preparation_text_" + (troops == 1 || troops == -1 ? "s" : "m"), troops, country)
				+ "</div></html>");
		
	}
	
	public boolean getFastMode() {
		return fastMode.isSelected();
	}
	
	public void setTroops(int troops) {
		this.troops = troops;
		updateLabel();
	}
	
	public void setCountry(String country) {
		if (country == null)
			country = "";
    
		this.country = country;
		updateLabel();
	}

	public void addPreparationLabelListener(PreparationLabelListener listener) {
		listeners.add(listener);
	}

	public void removePreparationLabelListener(PreparationLabelListener listener) {
		listeners.remove(listener);
	}

	public interface PreparationLabelListener {

		public static final int TYPE_DEPLOY = 0;
		public static final int TYPE_FAST_MODE_CHANGED = 2;

		public void preparationActionPerformed(int type);

	}
}
