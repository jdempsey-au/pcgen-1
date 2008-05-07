/*
 * Page2Panel.java
 *
 * Created on February 12, 2004, 5:01 PM
 */

package plugin.charactersheet.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import pcgen.cdom.base.Constants;
import pcgen.core.Ability;
import pcgen.core.Equipment;
import pcgen.core.Language;
import pcgen.core.PlayerCharacter;
import pcgen.core.WeaponProf;
import pcgen.io.exporttoken.DomainToken;
import pcgen.io.exporttoken.EqToken;
import plugin.charactersheet.CharacterSheetUtils;

/**
 *
 * @author  ddjone3
 */
public class Page2Panel extends javax.swing.JPanel
{
	private PlayerCharacter pc;
	private javax.swing.JPanel col1 = new javax.swing.JPanel();
	private javax.swing.JPanel col2 = new javax.swing.JPanel();
	private EquipmentPane equipmentPane = new EquipmentPane();
	private ShortListPane specialPane = new ShortListPane();
	private ListPane weaponProfPane = new ListPane();
	private ShortListPane languagePane = new ShortListPane();
	private ShortListPane templatePane = new ShortListPane();
	private ShortListPane moneyPane = new ShortListPane();
	private ShortListPane magicPane = new ShortListPane();
	private WeightPane weightPane = new WeightPane();
	private DualListPane domainPane = new DualListPane();
	private DualListPane featPane = new DualListPane();
	private javax.swing.JPanel gluePane1 = new javax.swing.JPanel();
	private javax.swing.JPanel gluePane2 = new javax.swing.JPanel();
	private int serial = 0;

	/** Creates new form Page2Panel */
	public Page2Panel()
	{
		initComponents();
		setLocalColor();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents()
	{//GEN-BEGIN:initComponents
		setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

		col1.setLayout(new java.awt.GridBagLayout());
		add(col1);

		col2.setLayout(new java.awt.GridBagLayout());
		add(col2);

		//Column 1
		CharacterSheetUtils.addGbComponentCell(col1, equipmentPane, 0, 0, 1, 1,
			new Insets(0, 0, 0, 0), GridBagConstraints.BOTH,
			GridBagConstraints.CENTER);

		gluePane1.setBackground(CharacterPanel.white);
		gluePane1.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE,
			Integer.MAX_VALUE));
		gluePane1.setPreferredSize(new java.awt.Dimension(20, 20));
		CharacterSheetUtils.addGbComponentCell(col1, gluePane1, 0, 1, 9, 1,
			new Insets(5, 0, 0, 0), GridBagConstraints.NONE,
			GridBagConstraints.WEST);

		//Column 2
		CharacterSheetUtils.addGbComponentCell(col2, weightPane, 0, 0, 1, 1,
			new Insets(0, 0, 0, 0), GridBagConstraints.BOTH,
			GridBagConstraints.CENTER);
		CharacterSheetUtils.addGbComponentCell(col2, moneyPane, 0, 1, 1, 1,
			new Insets(5, 0, 0, 0), GridBagConstraints.BOTH,
			GridBagConstraints.CENTER);
		CharacterSheetUtils.addGbComponentCell(col2, magicPane, 0, 2, 1, 1,
			new Insets(5, 0, 0, 0), GridBagConstraints.BOTH,
			GridBagConstraints.CENTER);
		CharacterSheetUtils.addGbComponentCell(col2, specialPane, 0, 3, 1, 1,
			new Insets(5, 0, 0, 0), GridBagConstraints.BOTH,
			GridBagConstraints.CENTER);
		CharacterSheetUtils.addGbComponentCell(col2, featPane, 0, 4, 1, 1,
			new Insets(5, 0, 0, 0), GridBagConstraints.BOTH,
			GridBagConstraints.CENTER);
		CharacterSheetUtils.addGbComponentCell(col2, domainPane, 0, 5, 1, 1,
			new Insets(5, 0, 0, 0), GridBagConstraints.BOTH,
			GridBagConstraints.CENTER);
		CharacterSheetUtils.addGbComponentCell(col2, weaponProfPane, 0, 6, 1,
			1, new Insets(5, 0, 0, 0), GridBagConstraints.BOTH,
			GridBagConstraints.CENTER);
		CharacterSheetUtils.addGbComponentCell(col2, languagePane, 0, 7, 1, 1,
			new Insets(5, 0, 0, 0), GridBagConstraints.BOTH,
			GridBagConstraints.CENTER);
		CharacterSheetUtils.addGbComponentCell(col2, templatePane, 0, 8, 1, 1,
			new Insets(5, 0, 0, 0), GridBagConstraints.BOTH,
			GridBagConstraints.CENTER);

		gluePane2.setBackground(CharacterPanel.white);
		gluePane2.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE,
			Integer.MAX_VALUE));
		gluePane2.setPreferredSize(new java.awt.Dimension(20, 20));
		CharacterSheetUtils.addGbComponentCell(col2, gluePane2, 0, 9, 1, 1,
			new Insets(5, 0, 0, 0), GridBagConstraints.NONE,
			GridBagConstraints.WEST);

		addComponentListener(new ComponentListener()
		{
			public void componentHidden(ComponentEvent e)
			{
				updateBuffer();
			}

			public void componentMoved(ComponentEvent e)
			{
				updateBuffer();
			}

			public void componentResized(ComponentEvent e)
			{
				updateBuffer();
			}

			public void componentShown(ComponentEvent e)
			{
				updateBuffer();
			}
		});
	}//GEN-END:initComponents

	public void setColor()
	{
		setLocalColor();
		domainPane.setColor();
		featPane.setColor();
		equipmentPane.setColor();
		weaponProfPane.setColor();
		specialPane.setColor();
		languagePane.setColor();
		templatePane.setColor();
		moneyPane.setColor();
		magicPane.setColor();
		weightPane.setColor();
	}

	public void setLocalColor()
	{
		setBackground(CharacterPanel.white);
		col1.setBackground(CharacterPanel.white);
		col2.setBackground(CharacterPanel.white);
		gluePane1.setBackground(CharacterPanel.white);
		gluePane2.setBackground(CharacterPanel.white);
	}

	public void setPc(PlayerCharacter pc, Properties pcProperties)
	{
		if (this.pc != pc)
		{
			this.pc = pc;
			serial = 0;
			equipmentPane.setPc(pc);
			weightPane.setPc(pc);
			updateBuffer();
		}
	}

	/**
	 * This method currently does nothing
	 */
	public void clear()
	{
		// Do Nothing
	}

	public void refresh()
	{
		if (serial < pc.getSerial())
		{
			equipmentPane.refresh();
			weightPane.refresh();
			moneyPane.setList("MONEY", getMoneyList(pc));
			magicPane.setList("MAGIC", getMagicList(pc));
			specialPane.setList("SPECIAL ABILITIES", pc
				.getSpecialAbilityTimesList());
			weaponProfPane.setList("WEAPON PROFICIENCIES", new ArrayList<WeaponProf>(pc
				.getWeaponProfs()));
			languagePane.setList("LANGUAGES", new ArrayList<Language>(pc
				.getLanguagesList()));
			templatePane
				.setList("TEMPLATES", pc.getOutputVisibleTemplateList());
			domainPane.setMap("DOMAINS", getDomainMap(pc));
			featPane.setMap("FEATS", getFeatMap(pc));

			serial = pc.getSerial();
		}
		updateBuffer();
	}

	public void updateProperties()
	{
		// This will be populated later
	}

	private void updateBuffer()
	{
		int left = equipmentPane.getHeight();
		int right =
				weightPane.getHeight() + moneyPane.getHeight()
					+ magicPane.getHeight() + specialPane.getHeight()
					+ featPane.getHeight() + domainPane.getHeight()
					+ weaponProfPane.getHeight() + languagePane.getHeight()
					+ templatePane.getHeight() - 5;
		if (domainPane.isVisible())
			right += 5;
		if (featPane.isVisible())
			right += 5;
		if (equipmentPane.isVisible())
			right += 5;
		if (weaponProfPane.isVisible())
			right += 5;
		if (specialPane.isVisible())
			right += 5;
		if (languagePane.isVisible())
			right += 5;
		if (templatePane.isVisible())
			right += 5;
		if (moneyPane.isVisible())
			right += 5;
		if (magicPane.isVisible())
			right += 5;

		if (left > right)
		{
			int height = left - right;
			gluePane2.setPreferredSize(new java.awt.Dimension(20, height));
			gluePane2.setMinimumSize(new java.awt.Dimension(20, height));

			col1.remove(gluePane1);
			CharacterSheetUtils.addGbComponentCell(col2, gluePane2, 0, 9, 1, 1,
				new Insets(0, 0, 0, 0), GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		}
		else if (right > left)
		{
			int height = right - left;
			gluePane1.setPreferredSize(new java.awt.Dimension(20, height));

			col2.remove(gluePane2);
			CharacterSheetUtils.addGbComponentCell(col1, gluePane1, 0, 1, 9, 1,
				new Insets(0, 0, 0, 0), GridBagConstraints.BOTH,
				GridBagConstraints.CENTER);
		}
	}

	//TODO: [DJ] add to DomainToken
	private Map<String, String> getDomainMap(PlayerCharacter aPC)
	{
		int numDomains = aPC.getCharacterDomainList().size();
		Map<String, String> domainMap = new HashMap<String, String>();
		for (int i = 0; i < numDomains; i++)
		{
			domainMap.put(DomainToken.getDomainToken(aPC, i), DomainToken
				.getPowerToken(aPC, i));
		}
		return domainMap;
	}

	//TODO: [DJ] remove when I add FeatToken
	private Map<String, String> getFeatMap(PlayerCharacter aPC)
	{
		List feats = aPC.aggregateVisibleFeatList();
		Map<String, String> featMap = new HashMap<String, String>();
		for (int i = 0; i < feats.size(); i++)
		{
			Ability feat = (Ability) feats.get(i);
			featMap.put(feat.qualifiedName(), feat.getDescription(aPC));
		}
		return featMap;
	}

	//TODO: [DJ] remove when I modify the money token
	private List<String> getMoneyList(PlayerCharacter aPC)
	{
		int merge = Constants.MERGE_LOCATION;
		List<String> returnList = new ArrayList<String>();

		List moneyList = aPC.getEquipmentOfTypeInOutputOrder("Coin", 3, merge);
		for (int i = 0; i < moneyList.size(); i++)
		{
			Equipment eq = (Equipment) moneyList.get(i);
			StringBuffer sb = new StringBuffer();
			returnList.add(sb.append(EqToken.getLongNameToken(eq)).append(':')
				.append(' ').append(EqToken.getQtyToken(eq)).toString());
		}

		moneyList = aPC.getEquipmentOfTypeInOutputOrder("Gem", 3, merge);
		for (int i = 0; i < moneyList.size(); i++)
		{
			Equipment eq = (Equipment) moneyList.get(i);
			StringBuffer sb = new StringBuffer();
			returnList.add(sb.append(EqToken.getQtyToken(eq)).append(' ')
				.append('x').append(' ').append(EqToken.getLongNameToken(eq))
				.append(' ').append('(').append(EqToken.getCostToken(aPC, eq))
				.append(')').toString());
		}

		return returnList;
	}

	//TODO: [DJ] remove when I add MiscToken
	private List<String> getMagicList(PlayerCharacter aPC)
	{
		List<String> returnList = new ArrayList<String>();
		StringTokenizer aTok =
				new StringTokenizer(aPC.getMiscList().get(2), "\r\n", false);

		while (aTok.hasMoreTokens())
		{
			returnList.add(aTok.nextToken());
		}

		return returnList;
	}

	public void destruct()
	{
		equipmentPane.destruct();
		specialPane.destruct();
		weaponProfPane.destruct();
		languagePane.destruct();
		templatePane.destruct();
		moneyPane.destruct();
		magicPane.destruct();
		weightPane.destruct();
		domainPane.destruct();
		featPane.destruct();

		equipmentPane = null;
		specialPane = null;
		weaponProfPane = null;
		languagePane = null;
		templatePane = null;
		moneyPane = null;
		magicPane = null;
		weightPane = null;
		domainPane = null;
		featPane = null;
	}
}
