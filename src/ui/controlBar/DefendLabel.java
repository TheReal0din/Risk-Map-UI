package ui.controlBar;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import ui.Language;

public class DefendLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel title;
	private JSlider troopCount;
	private JButton defendButton;

	private String attackedCountry;

	private List<DefendLabelListener> listeners;

	public DefendLabel(String attackedCountry, int minTroops, int maxTroops) {

		listeners = new ArrayList<>();

		this.attackedCountry = attackedCountry;
		if (attackedCountry == null)
			this.attackedCountry = "";

		this.setMaximumSize(new Dimension(9999999, 500));

		title = new JLabel("", SwingConstants.CENTER);
		title.setFont(new Font("Dialog", Font.PLAIN, 12));
		updateLabel();

		if (maxTroops < minTroops)
			maxTroops = minTroops;

		troopCount = new JSlider(minTroops, maxTroops, minTroops);
		troopCount.setMajorTickSpacing(1);
		troopCount.setPaintLabels(true);
		troopCount.setOpaque(false);
		troopCount.addChangeListener(e -> {
			if (!troopCount.getValueIsAdjusting())
				for (DefendLabelListener dl : listeners)
					dl.defendActionPerformed(DefendLabelListener.TYPE_TROOP_COUNT_CHANGED);
		});

		defendButton = new JButton(Language.get("defend_confirm"));
		defendButton.addActionListener(ae -> {
			for (DefendLabelListener dl : listeners)
				dl.defendActionPerformed(DefendLabelListener.TYPE_DEFEND);
		});

		GroupLayout gl = new GroupLayout(this);
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(title).addComponent(troopCount).addGap(0, 50, 9999)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(defendButton)));

		gl.setHorizontalGroup(gl.createSequentialGroup().addGroup(gl.createParallelGroup().addComponent(title)
				.addComponent(troopCount).addComponent(defendButton, GroupLayout.Alignment.TRAILING)));

		this.setLayout(gl);
	}

	public void setAttackedCountry(String attackedCountry) {
		if (attackedCountry == null)
			attackedCountry = "";
		
		this.attackedCountry = attackedCountry;
		updateLabel();
	}

	public int getTroopCount() {
		return troopCount.getValue();
	}

	public void addDefendLabelListener(DefendLabelListener listener) {
		listeners.add(listener);
	}

	public void removeDefendLabelListener(DefendLabelListener listener) {
		listeners.remove(listener);
	}

	private void updateLabel() {
		title.setText("<html><div style='text-align: center;'>" + Language.get("defend_title") + "<br>"
				+ Language.get("defend_text", attackedCountry) + "</div></html>");
	}

	public interface DefendLabelListener {

		public static final int TYPE_DEFEND = 0;
		public static final int TYPE_TROOP_COUNT_CHANGED = 2;

		public void defendActionPerformed(int type);

	}
}
