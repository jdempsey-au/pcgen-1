/*
 * TurnPane.java
 *
 * Created on February 6, 2004, 2:42 PM
 */

package plugin.charactersheet.gui;

import pcgen.core.PlayerCharacter;
import pcgen.io.exporttoken.StatToken;
import pcgen.io.exporttoken.VarToken;
import pcgen.util.Delta;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author  ddjone3
 */
public class TurnPane extends javax.swing.JPanel {
	private PlayerCharacter pc;
	private String turnType;
	private ArrayList<JCheckBox> checkList = new ArrayList<JCheckBox>();
	private Properties pcProperties;
	private boolean updateProperties = false;

	private JPanel checkPanel;
	private JPanel titlePanel;
	private JLabel title;
	private JPanel col1Header;
	private JPanel col1row1;
	private JPanel col1row2;
	private JPanel col1row3;
	private JPanel col1row4;
	private JPanel col1row5;
	private JPanel col1row6;
	private JPanel col1row7;
	private JPanel col1row8;
	private JPanel col1row9;
	private JPanel col2Header;
	private JPanel col2row1;
	private JPanel col2row2;
	private JPanel col2row3;
	private JPanel col2row4;
	private JPanel col2row5;
	private JPanel col2row6;
	private JPanel col2row7;
	private JPanel col2row8;
	private JPanel col2row9;
	private JLabel turn1;
	private JLabel turn2;
	private JLabel turn3;
	private JLabel turn4;
	private JLabel turn5;
	private JLabel turn6;
	private JLabel turn7;
	private JLabel turn8;
	private JLabel turn9;
	private JPanel turnCheckPanel;
	private JPanel turnCheckLabelPanel;
	private JLabel turnCheck;
	private JPanel turnLevelPanel;
	private JPanel turnLevelLabelPanel;
	private JLabel turnLevel;
	private JPanel turnDamagePanel;
	private JPanel turnDamageLabelPanel;
	private JLabel turnDamage;
	private JPanel turnDayLabelPanel;
	private JLabel turnDay;
	private JTextArea turnTextArea;

	/** Creates new form TurnPane */
	public TurnPane() {
		initComponents();
		setLocalColor();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents() {//GEN-BEGIN:initComponents
		GridBagConstraints gridBagConstraints;

		titlePanel = new JPanel();
		title = new JLabel();
		turnCheckLabelPanel = new JPanel();
		turnCheckPanel = new JPanel();
		turnCheck = new JLabel();

		setLayout(new java.awt.GridBagLayout());

		titlePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));

		title.setFont(new java.awt.Font("Dialog", 1, 14));
		title.setText("TURN/REBUKE UNDEAD");
		titlePanel.add(title);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(titlePanel, gridBagConstraints);

		//Column 1
		col1Header = new JPanel();
		col1row1 = new JPanel();
		col1row2 = new JPanel();
		col1row3 = new JPanel();
		col1row4 = new JPanel();
		col1row5 = new JPanel();
		col1row6 = new JPanel();
		col1row7 = new JPanel();
		col1row8 = new JPanel();
		col1row9 = new JPanel();

		col1Header.setLayout(new javax.swing.BoxLayout(col1Header, javax.swing.BoxLayout.Y_AXIS));
		col1Header.add(new JLabel("Turning Check"));
		col1Header.add(new JLabel("Result"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col1Header, gridBagConstraints);

		col1row1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		JLabel col1row1Label = new JLabel();
		col1row1Label.setFont(new java.awt.Font("Dialog", 0, 10));
		col1row1Label.setText("Up to 0");
		col1row1.add(col1row1Label);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col1row1, gridBagConstraints);

		col1row2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		JLabel col1row2Label = new JLabel();
		col1row2Label.setFont(new java.awt.Font("Dialog", 0, 10));
		col1row2Label.setText("1-3");
		col1row2.add(col1row2Label);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col1row2, gridBagConstraints);

		col1row3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		JLabel col1row3Label = new JLabel();
		col1row3Label.setFont(new java.awt.Font("Dialog", 0, 10));
		col1row3Label.setText("4-6");
		col1row3.add(col1row3Label);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col1row3, gridBagConstraints);

		col1row4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		JLabel col1row4Label = new JLabel();
		col1row4Label.setFont(new java.awt.Font("Dialog", 0, 10));
		col1row4Label.setText("7-9");
		col1row4.add(col1row4Label);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col1row4, gridBagConstraints);

		col1row5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		JLabel col1row5Label = new JLabel();
		col1row5Label.setFont(new java.awt.Font("Dialog", 0, 10));
		col1row5Label.setText("10-12");
		col1row5.add(col1row5Label);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col1row5, gridBagConstraints);

		col1row6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		JLabel col1row6Label = new JLabel();
		col1row6Label.setFont(new java.awt.Font("Dialog", 0, 10));
		col1row6Label.setText("13-15");
		col1row6.add(col1row6Label);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col1row6, gridBagConstraints);

		col1row7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		JLabel col1row7Label = new JLabel();
		col1row7Label.setFont(new java.awt.Font("Dialog", 0, 10));
		col1row7Label.setText("16-18");
		col1row7.add(col1row7Label);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col1row7, gridBagConstraints);

		col1row8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		JLabel col1row8Label = new JLabel();
		col1row8Label.setFont(new java.awt.Font("Dialog", 0, 10));
		col1row8Label.setText("19-21");
		col1row8.add(col1row8Label);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col1row8, gridBagConstraints);

		col1row9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		JLabel col1row9Label = new JLabel();
		col1row9Label.setFont(new java.awt.Font("Dialog", 0, 10));
		col1row9Label.setText("22+");
		col1row9.add(col1row9Label);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col1row9, gridBagConstraints);

		//Column 2
		col2Header = new JPanel();
		col2row1 = new JPanel();
		col2row2 = new JPanel();
		col2row3 = new JPanel();
		col2row4 = new JPanel();
		col2row5 = new JPanel();
		col2row6 = new JPanel();
		col2row7 = new JPanel();
		col2row8 = new JPanel();
		col2row9 = new JPanel();
		turn1 = new JLabel();
		turn2 = new JLabel();
		turn3 = new JLabel();
		turn4 = new JLabel();
		turn5 = new JLabel();
		turn6 = new JLabel();
		turn7 = new JLabel();
		turn8 = new JLabel();
		turn9 = new JLabel();

		col2Header.setLayout(new javax.swing.BoxLayout(col2Header, javax.swing.BoxLayout.Y_AXIS));
		col2Header.add(new JLabel("Undead Affected"));
		col2Header.add(new JLabel("(Maximum Hit Dice)"));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col2Header, gridBagConstraints);

		col2row1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		turn1.setFont(new java.awt.Font("Dialog", 0, 10));
		col2row1.add(turn1);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col2row1, gridBagConstraints);

		col2row2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		turn2.setFont(new java.awt.Font("Dialog", 0, 10));
		col2row2.add(turn2);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col2row2, gridBagConstraints);

		col2row3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		turn3.setFont(new java.awt.Font("Dialog", 0, 10));
		col2row3.add(turn3);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col2row3, gridBagConstraints);

		col2row4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		turn4.setFont(new java.awt.Font("Dialog", 0, 10));
		col2row4.add(turn4);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col2row4, gridBagConstraints);

		col2row5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		turn5.setFont(new java.awt.Font("Dialog", 0, 10));
		col2row5.add(turn5);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col2row5, gridBagConstraints);

		col2row6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		turn6.setFont(new java.awt.Font("Dialog", 0, 10));
		col2row6.add(turn6);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 8;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col2row6, gridBagConstraints);

		col2row7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		turn7.setFont(new java.awt.Font("Dialog", 0, 10));
		col2row7.add(turn7);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 9;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col2row7, gridBagConstraints);

		col2row8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		turn8.setFont(new java.awt.Font("Dialog", 0, 10));
		col2row8.add(turn8);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 10;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col2row8, gridBagConstraints);

		col2row9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		turn9.setFont(new java.awt.Font("Dialog", 0, 10));
		col2row9.add(turn9);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 11;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(col2row9, gridBagConstraints);

		//turnCheck
		turnCheckLabelPanel = new JPanel();
		turnCheckPanel = new JPanel();
		turnCheck = new JLabel();

		turnCheckLabelPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 1, 0));
		JLabel turnCheckLabel = new JLabel();
		turnCheckLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		turnCheckLabel.setText("Turn check");
		turnCheckLabelPanel.add(turnCheckLabel);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(turnCheckLabelPanel, gridBagConstraints);

		turnCheckPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		turnCheck.setFont(new java.awt.Font("Dialog", 0, 10));
		turnCheckPanel.add(turnCheck);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(turnCheckPanel, gridBagConstraints);

		//Turn Level
		turnLevelLabelPanel = new JPanel();
		turnLevelPanel = new JPanel();
		turnLevel = new JLabel();

		turnLevelLabelPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 1, 0));
		JLabel turnLevelLabel = new JLabel();
		turnLevelLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		turnLevelLabel.setText("Turn Level");
		turnLevelLabelPanel.add(turnLevelLabel);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(turnLevelLabelPanel, gridBagConstraints);

		turnLevelPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		turnLevel.setFont(new java.awt.Font("Dialog", 0, 10));
		turnLevelPanel.add(turnLevel);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(turnLevelPanel, gridBagConstraints);

		//Turn Damage
		turnDamageLabelPanel = new JPanel();
		turnDamagePanel = new JPanel();
		turnDamage = new JLabel();

		turnDamageLabelPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		JLabel turnDamageLabel = new JLabel();
		turnDamageLabel.setFont(new java.awt.Font("Dialog", 0, 10));
		turnDamageLabel.setText("Turn Damage");
		turnDamageLabelPanel.add(turnDamageLabel);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(turnDamageLabelPanel, gridBagConstraints);

		turnDamagePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
		turnDamage.setFont(new java.awt.Font("Dialog", 0, 10));
		turnDamagePanel.add(turnDamage);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(turnDamagePanel, gridBagConstraints);

		//Turns/day
		turnDayLabelPanel = new JPanel();
		turnDay = new JLabel();

		turnDayLabelPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 1, 0));
		turnDayLabelPanel.add(turnDay);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(turnDayLabelPanel, gridBagConstraints);


		//Checkmark Panel
		checkPanel = new JPanel();

		checkPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 1));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 12;
		gridBagConstraints.gridwidth = 3;
		//gridBagConstraints.gridheight = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(checkPanel, gridBagConstraints);
		
		//Turn Text
		turnTextArea = new JTextArea();

		turnTextArea.setLineWrap(true);
		turnTextArea.setFont(new java.awt.Font("Dialog", 0, 10));
		turnTextArea.setWrapStyleWord(true);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.gridheight = 8;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		add(turnTextArea, gridBagConstraints);
	}

	/**
	 * Set color, calls setLocalColor() and Refresh()
	 */
	public void setColor() {
		setLocalColor();
		refresh();
	}

	/**
	 * Set the local color of the pane
	 */
	public void setLocalColor() {
		setBackground(CharacterPanel.white);
		setBorder(new javax.swing.border.LineBorder(CharacterPanel.border));
		titlePanel.setBackground(CharacterPanel.header);
		titlePanel.setBorder(new javax.swing.border.LineBorder(CharacterPanel.border));

		col1Header.setBackground(CharacterPanel.header);
		col1Header.setBorder(new javax.swing.border.LineBorder(CharacterPanel.border));
		col1row1.setBackground(CharacterPanel.bodyLight);
		col1row2.setBackground(CharacterPanel.bodyMedLight);
		col1row3.setBackground(CharacterPanel.bodyLight);
		col1row4.setBackground(CharacterPanel.bodyMedLight);
		col1row5.setBackground(CharacterPanel.bodyLight);
		col1row6.setBackground(CharacterPanel.bodyMedLight);
		col1row7.setBackground(CharacterPanel.bodyLight);
		col1row8.setBackground(CharacterPanel.bodyMedLight);
		col1row9.setBackground(CharacterPanel.bodyLight);

		col2Header.setBackground(CharacterPanel.header);
		col2Header.setBorder(new javax.swing.border.LineBorder(CharacterPanel.border));
		col2row1.setBackground(CharacterPanel.bodyLight);
		col2row2.setBackground(CharacterPanel.bodyMedLight);
		col2row3.setBackground(CharacterPanel.bodyLight);
		col2row4.setBackground(CharacterPanel.bodyMedLight);
		col2row5.setBackground(CharacterPanel.bodyLight);
		col2row6.setBackground(CharacterPanel.bodyMedLight);
		col2row7.setBackground(CharacterPanel.bodyLight);
		col2row8.setBackground(CharacterPanel.bodyMedLight);
		col2row9.setBackground(CharacterPanel.bodyLight);

		turnCheckPanel.setBackground(CharacterPanel.white);
		turnCheckPanel.setBorder(new javax.swing.border.LineBorder(CharacterPanel.border));
		turnCheckLabelPanel.setBackground(CharacterPanel.header);
		turnCheckLabelPanel.setBorder(new javax.swing.border.LineBorder(CharacterPanel.border));

		turnLevelPanel.setBackground(CharacterPanel.white);
		turnLevelPanel.setBorder(new javax.swing.border.LineBorder(CharacterPanel.border));
		turnLevelLabelPanel.setBackground(CharacterPanel.header);
		turnLevelLabelPanel.setBorder(new javax.swing.border.LineBorder(CharacterPanel.border));

		turnDamageLabelPanel.setBackground(CharacterPanel.header);
		turnDamageLabelPanel.setBorder(new javax.swing.border.LineBorder(CharacterPanel.border));
		turnDamagePanel.setBackground(CharacterPanel.white);
		turnDamagePanel.setBorder(new javax.swing.border.LineBorder(CharacterPanel.border));

		turnDayLabelPanel.setBackground(CharacterPanel.header);
		turnDayLabelPanel.setBorder(new javax.swing.border.LineBorder(CharacterPanel.border));

		checkPanel.setBackground(CharacterPanel.white);
		checkPanel.setBorder(new javax.swing.border.LineBorder(CharacterPanel.border));

		turnTextArea.setBorder(new javax.swing.border.LineBorder(CharacterPanel.border));
	}

	/**
	 * Set the PC
	 * @param pc
	 * @param pcProperties
	 * @param turnType
	 */
	public void setPc(PlayerCharacter pc, Properties pcProperties, String turnType) {
		this.pc = pc;
		this.pcProperties = pcProperties;
		this.turnType = turnType;
		setVisible(false);
	}

	/**
	 * Get the effect type (Turn or Rebuke)
	 * @param aPc
	 * @param aTurnType
	 * @return the effect type (Turn or Rebuke
	 */
	private String getEffectType(PlayerCharacter aPc, String aTurnType) {
		if(aPc.hasRealFeatNamed("Turn " + aTurnType) || aPc.hasFeatVirtual("Turn " + aTurnType)) {
			return "Turn";
		}
		else if(aPc.hasRealFeatNamed("Rebuke " + aTurnType) || aPc.hasFeatVirtual("Rebuke " + aTurnType)){
			return "Rebuke";
		}
		return "";
	}

	/**
	 * Refresh the pane
	 */
	public void refresh() {
		try {
			int level = VarToken.getIntVarToken(pc, "TurnLevel" + turnType, false);
			if(level > 0) {
				setVisible(true);

				int check = VarToken.getIntVarToken(pc, "TurnCheck" + turnType, false);
				int numDay = VarToken.getIntVarToken(pc, "TurnTimes" + turnType, false);
				int dieNumber = 2;
				int dieSize = 6;
				int damage = VarToken.getIntVarToken(pc, "TurnDamagePlus" + turnType, false);
				if(damage > 0) {
					dieNumber = VarToken.getIntVarToken(pc, "TurnDice" + turnType, false);
					dieSize = VarToken.getIntVarToken(pc, "TurnDieSize" + turnType, false);
				}
				else {
					damage = level + Integer.parseInt(StatToken.getModToken(pc, 5));
				}
				StringBuffer checkSb = new StringBuffer();
				checkSb.append("1d20+").append(check);
				turnCheck.setText(checkSb.toString());
				turnLevel.setText(Integer.toString(level));
				StringBuffer damSb = new StringBuffer();
				damSb.append(dieNumber).append("d").append(dieSize).append(Delta.toString(damage));
				turnDamage.setText(damSb.toString());

				StringBuffer daySb = new StringBuffer();
				daySb.append("Turns/day (").append(numDay).append(")");
				turnDay.setText(daySb.toString());

				turn1.setText(Integer.toString(level - 4));
				turn2.setText(Integer.toString(level - 3));
				turn3.setText(Integer.toString(level - 2));
				turn4.setText(Integer.toString(level - 1));
				turn5.setText(Integer.toString(level));
				turn6.setText(Integer.toString(level + 1));
				turn7.setText(Integer.toString(level + 2));
				turn8.setText(Integer.toString(level + 3));
				turn9.setText(Integer.toString(level + 4));
				addCheckBoxes(numDay);
				StringBuffer textSb = new StringBuffer();
				if(getEffectType(pc, turnType).equals("Turn")) {
					this.title.setText("Turn " + turnType);
					textSb.append("Note: You destroy ")
						.append(turnType)
						.append(" creatures if they have up to ")
						.append((level/2))
						.append(" Hit Dice");
					turnTextArea.setText(textSb.toString());
				}
				else {
					this.title.setText("Rebuke " + turnType);
					textSb.append("Note: You command ")
						.append(turnType)
						.append(" creatures if they have up to ")
						.append(level)
						.append(" Hit Dice");
					turnTextArea.setText(textSb.toString());
				}

				updatePane();
			}
			else {
				setVisible(false);
			}
		}
		catch(Exception e) {
			System.out.println("Exception in " + turnType);
			setVisible(false);
		}
	}

	private void addCheckBoxes(int numDay) {
		if (checkList.size() != numDay) {
			checkList.clear();
			checkPanel.removeAll();
			for(int i = 0; i < numDay; i++) {
				if(i % 5 == 0 && i != 0) {
					javax.swing.JLabel bufLabel = new javax.swing.JLabel();
					bufLabel.setFont(new java.awt.Font("Dialog", 0, 10));
					bufLabel.setText(" ");
					checkPanel.add(bufLabel);
				}
				JCheckBox checkBox = new JCheckBox();
				checkBox.setBackground(new java.awt.Color(255, 255, 255));
				checkBox.setBorder(null);
				checkList.add(checkBox);
				checkPanel.add(checkBox);
				checkBox.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						pc.setDirty(true);
						updateProperties();
					}
        });
			}
		}
	}

	/**
	 * Update the properties of this pane
	 */
	public void updateProperties() {
		if(updateProperties) {
			int counter = 0;
			for (JCheckBox checkBox : checkList)
			{
				if(checkBox.isSelected()) {
					counter++;
				}
			}
			pcProperties.put("cs.TurnPane." + turnType, Integer.toString(counter));
		}
	}

	/**
	 * Update the pane
	 */
	public void updatePane() {
		try {
			int counter = Integer.parseInt((String)pcProperties.get("cs.TurnPane." + turnType));
			for (JCheckBox checkBox : checkList)
			{
				if(counter > 0) {
					checkBox.setSelected(true);
					counter--;
				}
				else {
					checkBox.setSelected(false);
				}
			}
			updateProperties = true;
		}
		catch(NumberFormatException e) {
			//Do nothing
		}
	}

	/**
	 * Destroy
	 */
	public void destruct() {
		//Put any code here that is needed to prevent memory leaks when this panel is destroyed
	}
}
