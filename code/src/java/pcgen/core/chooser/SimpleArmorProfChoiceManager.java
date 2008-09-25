/*
 * SimpleArmorProfChoiceManager.java
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Current Version: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 * Copyright 2005 Andrew Wilson <nuance@sourceforge.net>
 */
package pcgen.core.chooser;

import java.util.List;

import pcgen.core.AssociatedChoice;
import pcgen.core.Equipment;
import pcgen.core.Globals;
import pcgen.core.PObject;
import pcgen.core.PlayerCharacter;

/**
 * Deal with choosing an armor proficiency
 */
public class SimpleArmorProfChoiceManager extends
		AbstractBasicChoiceManager<String>
{
	/**
	 * Creates a new SimpleArmorProfChoiceManager object.
	 * 
	 * @param aPObject
	 * @param theChoices
	 * @param aPC
	 */
	public SimpleArmorProfChoiceManager(PObject aPObject, String theChoices,
			PlayerCharacter aPC)
	{
		super(aPObject, theChoices, aPC);
	}

	/**
	 * Get a list of Armor proficiencies
	 * 
	 * @param aPc
	 * @param availableList
	 * @param selectedList
	 */
	@Override
	public void getChoices(PlayerCharacter aPc, List<String> availableList,
			List<String> selectedList)
	{
		for (AssociatedChoice<String> choice : pobject.getAssociatedList())
		{
			selectedList.add(choice.getDefaultChoice());
		}
		setPreChooserChoices(selectedList.size());
		for (String tempString : getChoiceList())
		{
			if (tempString.equals("ANY") || tempString.startsWith("ALL"))
			{
				tempString = tempString.substring(5);

				for (Equipment eq : Globals.getContext().ref.getConstructedCDOMObjects(Equipment.class))
				{
					if (eq.isArmor())
					{
						String profKey = eq.getArmorProf().getKeyName();
						if (!availableList.contains(profKey))
						{
							availableList.add(profKey);
						}
					}
				}
			}
			else if (tempString.startsWith("TYPE=")
					|| tempString.startsWith("TYPE."))
			{
				tempString = tempString.substring(5);

				for (Equipment eq : Globals.getContext().ref.getConstructedCDOMObjects(Equipment.class))
				{
					if (eq.isArmor() && eq.isType(tempString))
					{
						String profKey = eq.getArmorProf().getKeyName();
						if (!availableList.contains(profKey))
						{
							availableList.add(profKey);
						}
					}
				}
			}
			else
			{
				final Equipment eq = Globals.getContext().ref
						.silentlyGetConstructedCDOMObject(Equipment.class,
								tempString);
				if (eq != null && eq.isArmor())
				{
					String profKey = eq.getArmorProf().getKeyName();
					if (!availableList.contains(profKey))
					{
						availableList.add(profKey);
					}
				}
			}
		}
	}

	/**
	 * Add the selected Armor proficiencies
	 * 
	 * @param aPC
	 * @param selected
	 */
	@Override
	public void applyChoices(PlayerCharacter aPC, List<String> selected)
	{
		pobject.clearAssociated();
		for (String st : selected)
		{
			if (isMultYes() && !isStackYes())
			{
				if (!pobject.containsAssociated(st))
				{
					aPC.addAssociation(pobject, st);
				}
			}
			else
			{
				aPC.addAssociation(pobject, st);
			}
		}
		adjustPool(selected);
	}
}
