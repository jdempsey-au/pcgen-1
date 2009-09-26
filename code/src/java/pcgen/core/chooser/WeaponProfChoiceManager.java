/**
 * WeaponProfChoiceManager.java
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
 * Current Version: $Revision: 285 $
 * Last Editor:     $Author: nuance $
 * Last Edited:     $Date: 2006-03-17 15:19:49 +0000 (Fri, 17 Mar 2006) $
 *
 * Copyright 2006 Andrew Wilson <nuance@sourceforge.net>
 */
package pcgen.core.chooser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.reference.CDOMSingleRef;
import pcgen.core.Equipment;
import pcgen.core.Globals;
import pcgen.core.PObject;
import pcgen.core.PlayerCharacter;
import pcgen.core.WeaponProf;
import pcgen.core.analysis.SizeUtilities;
import pcgen.rules.context.ReferenceContext;
import pcgen.util.Logging;

/**
 * This is the chooser that deals with choosing a Weapon Proficiency
 */
public class WeaponProfChoiceManager extends AbstractBasicPObjectChoiceManager<WeaponProf> {

	/**
	 * Make a new Weapon Proficiency chooser.
	 *
	 * @param aPObject
	 * @param choiceString
	 * @param aPC
	 */
	public WeaponProfChoiceManager(
			PObject         aPObject,
			String          choiceString,
			PlayerCharacter aPC)
	{
		super(aPObject, choiceString, aPC);
		setTitle("Weapon Prof Choice");
	}

	/**
	 * Parse the Choice string and build a list of available choices.
	 *
	 * @param aPc
	 * @param availableList
	 * @param selectedList
	 */
	@Override
	public void getChoices(
			final PlayerCharacter aPc,
			final List<WeaponProf>            availableList,
			final List<WeaponProf>            selectedList)
	{
		ReferenceContext refContext = Globals.getContext().ref;
		for (String aString : getChoiceList())
		{
			if ("LIST".equalsIgnoreCase(aString))
			{
				for ( WeaponProf wp : aPc.getWeaponProfs() )
				{
					if (wp == null)
					{
						continue;
					}

					if (!availableList.contains(wp))
					{
						availableList.add(wp);
					}
				}
			}
			else if ("DEITYWEAPON".equalsIgnoreCase(aString))
			{
				if (aPc.getDeity() != null)
				{
					List<CDOMReference<WeaponProf>> dwp = aPc.getDeity().getSafeListFor(
							ListKey.DEITYWEAPON);
					for (CDOMReference<WeaponProf> ref : dwp)
					{
						availableList.addAll(ref.getContainedObjects());
					}
				}
			}
			else if ("SIZE.".regionMatches(true, 0, aString, 0, 5))
			{
				final String profKey = aString.substring(7);
				final WeaponProf wp = refContext
						.silentlyGetConstructedCDOMObject(WeaponProf.class,
								profKey);
				if ((aPc.sizeInt() >= SizeUtilities.sizeInt(aString.substring(5, 6)))
					&& aPc.hasWeaponProf(wp))
				{
					if (!availableList.contains(wp))
					{
						availableList.add(wp);
					}
				}
			}
			else if ("WSIZE.".regionMatches(true, 0, aString, 0, 6))
			{
				final StringTokenizer bTok = new StringTokenizer(aString, ".");
				bTok.nextToken(); // should be WSize

				final String sString = bTok.nextToken(); // should be Light, 1 handed, 2 handed choices above
				final List<String> typeList = new ArrayList<String>();

				while (bTok.hasMoreTokens()) // any additional constraints
				{
					final String dString = bTok.nextToken().toUpperCase();
					typeList.add(dString);
				}

				for ( final WeaponProf wp : aPc.getWeaponProfs() )
				{
					if (wp == null)
					{
						continue;
					}

					//
					// get an Equipment object based on the named WeaponProf
					//
					final String profKey = wp.getKeyName();
					Equipment eq = refContext
							.silentlyGetConstructedCDOMObject(Equipment.class,
									profKey);

					if (eq == null)
					{
						//
						// Couldn't find equipment with matching name, look for
						// 1st weapon that uses it
						//
						for (Equipment tempEq : refContext.getConstructedCDOMObjects(Equipment.class))
						{
							CDOMSingleRef<WeaponProf> ref = tempEq
									.get(ObjectKey.WEAPON_PROF);
							if (ref != null)
							{
								if (ref.contains(wp))
								{
									eq = tempEq;
									break;
								}
							}
						}
					}

					boolean isValid = false; // assume we match unless...

					if (eq != null)
					{
						if (typeList.size() == 0)
						{
							isValid = true;
						}
						else
						{
							//
							// search all the optional type strings, just one match passes the test
							//
							for (Iterator<String> wpi = typeList.iterator(); wpi.hasNext();)
							{
								final String wpString = wpi.next();

								if (eq.isType(wpString))
								{
									isValid = true; // if it contains even one of the TYPE strings, it passes

									break;
								}
							}
						}
					}

					if (!isValid)
					{
						continue;
					}

					if (!availableList.contains(wp))
					{
						if ("Light".equals(sString) && eq.isWeaponLightForPC(aPc))
						{
							availableList.add(wp);
						}

						if ("1 handed".equals(sString) && eq.isWeaponOneHanded(aPc))
						{
							availableList.add(wp);
						}

						if ("2 handed".equals(sString) && eq.isWeaponTwoHanded(aPc))
						{
							availableList.add(wp);
						}
					}
				}
			}
			else if ("SPELLCASTER.".regionMatches(true, 0, aString, 0, 12))
			{
				// TODO this should not be hardcoded.
				String profKey = aString.substring(12);
				final WeaponProf wp = refContext.silentlyGetConstructedCDOMObject(WeaponProf.class, profKey);
				if (wp == null)
				{
					continue;
				}
				if (aPc.isSpellCaster(1) && !availableList.contains(wp))
				{
					availableList.add(wp);
				}
			}
			else if ("TYPE.".regionMatches(true, 0, aString, 0, 5) ||
				"TYPE=".regionMatches(true, 0, aString, 0, 5))
			{
				String sString = aString.substring(5);
				boolean adding = true;
				Iterator<WeaponProf> setIter = aPc.getWeaponProfs().iterator();

				if (sString.startsWith("Not."))
				{
					sString = sString.substring(4);
					setIter = availableList.iterator();
					adding = false;
				}

				WeaponProf wp;
				Equipment eq;

				while (setIter.hasNext())
				{
					wp = setIter.next();
					if (wp == null)
					{
						continue;
					}

					eq = refContext.silentlyGetConstructedCDOMObject(
							Equipment.class, wp.getKeyName());

					if (eq == null)
					{
						if (!wp.isType("Natural")) //natural weapons are not in the global eq.list
						{
							continue;
						}

						if (adding && !availableList.contains(wp))
						{
							availableList.add(wp);
						}
					}
					else if (eq.typeStringContains(sString))
					{
						// if this item is of the desired type, add it to the list
						if (adding && !availableList.contains(wp))
						{
							availableList.add(wp);
						}

						// or try to remove it and reset the iterator since remove cause fits
						else if (!adding && availableList.contains(wp))
						{
							availableList.remove(wp);
							setIter = availableList.iterator();
						}
					}
					else if (sString.equalsIgnoreCase("LIGHT"))
					{
						// if this item is of the desired type, add it to the list
						if (adding && !availableList.contains(wp) && eq.isWeaponLightForPC(aPc))
						{
							availableList.add(wp);
						}
						// or try to remove it and reset the iterator since remove cause fits
						else if (!adding && availableList.contains(wp) && eq.isWeaponLightForPC(aPc))
						{
							availableList.remove(wp);
							setIter = availableList.iterator();
						}
					}
				}
			}
			else
			{
				String profKey = aString;
				if ("ADD.".regionMatches(true, 0, aString, 0, 4))
				{
					profKey = aString.substring(4);
				}
				final WeaponProf wp = refContext.silentlyGetConstructedCDOMObject(WeaponProf.class, profKey);
				if (wp == null)
				{
					Logging.log(Logging.LST_INFO,
						"Reference to non-existant weapon proficiency "
							+ profKey + " in CHOOSE:WEAPONPROFS tag ignored.");
				}
				if (wp != null && aPc.hasWeaponProf(wp) && !availableList.contains(wp))
				{
					availableList.add(wp);
				}
			}
		}
		
		// Add the proficiencies already linked to the object to the selected list
		for (String choice : aPc.getAssociationList(pobject))
		{
			selectedList.add(refContext.silentlyGetConstructedCDOMObject(WeaponProf.class, choice));
		}
		setPreChooserChoices(selectedList.size());
	}
}
