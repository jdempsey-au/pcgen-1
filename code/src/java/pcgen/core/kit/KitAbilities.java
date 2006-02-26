/*
 * KitAbilities.java
 * Copyright 2005 (C) Andrew Wilson <nuance@sourceforge.net>
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
 * Created on 10 September 2005
 *
 * $Id: KitAbilities.java,v 1.10 2006/02/07 15:40:53 karianna Exp $
 */
package pcgen.core.kit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import pcgen.core.Ability;
import pcgen.core.AbilityInfo;
import pcgen.core.AbilityStore;
import pcgen.core.AbilityUtilities;
import pcgen.core.Categorisable;
import pcgen.core.Globals;
import pcgen.core.Kit;
import pcgen.core.PlayerCharacter;

/**
 * <code>KitAbiltiies</code>.
 *
 * @author   Andrew Wilson <nuance@sourceforge.net>
 * @version  $Revision: 1.10 $
 */
public final class KitAbilities extends BaseKit implements Serializable, Cloneable
{
	// Only change the UID when the serialized form of the class has also changed
	private static final long  serialVersionUID = 1;

	private AbilityStore abilityStore     = new AbilityStore();
	private boolean            free             = false;
	private String       stringRep;

	// These members store the state of an instance of this class.  They are
	// not cloned.
	private transient List theAbilities = new ArrayList();
	private transient List abilitiesToAdd = null;

	/**
	 * Constructor that takes a | separated list of Abilities, with Interspersed
	 * CATEGORY=FOO entries.
	 *
	 * @param  abString         the string containing the Abilities and
	 *                          Categories
	 * @param  defaultCategory  the default Category
	 * @param  lockCategory     Whether the initial category a subsequent
	 *                          CATEGORY= tag will be acted on or if it is an
	 *                          error
	 */
	public KitAbilities(
		final String abString,
		String       defaultCategory,
		boolean      lockCategory)
	{
		abilityStore.addAbilityInfo(abString, defaultCategory, "|", lockCategory, false);

		final StringBuffer info = new StringBuffer();

		if ((choiceCount != 1) || (abilityStore.size() != 1))
		{
			info.append(choiceCount).append(" of ");
		}

		boolean firstDone = false;

		for (Iterator it = this.getIterator(); it.hasNext();)
		{
			if (firstDone)
			{
				info.append("; ");
			}
			else
			{
				firstDone = true;
			}

			info.append(((Categorisable) it.next()).getKeyName());
		}

		if (free)
		{
			info.append(" (free)");
		}

		stringRep = info.toString();
	}

	/**
	 * Get an Iterator over the AbilityInfo Objects stored in this KitAbilities
	 * object
	 *
	 * @return  the AbilityInfo Iterator
	 */
	public Iterator getIterator()
	{
		return abilityStore.getKeyIterator("ALL");
	}

	/**
	 * Set whether the kit is free.
	 *
	 * @param  argFree  true if the kit is free
	 */
	public void setFree(final boolean argFree)
	{
		free = argFree;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return  the string representation of the object
	 *
	 * @see     Object#toString()
	 */
	public String toString()
	{
		return stringRep;
	}

	public boolean testApply(Kit aKit, PlayerCharacter aPC, List warnings)
	{
		theAbilities = new ArrayList();
		abilitiesToAdd = null;

		if (theAbilities == null)
		{
			return false;
		}

		final HashMap nameMap    = new HashMap();
		final HashMap catMap     = new HashMap();
		boolean useNameMap = true;

		for (Iterator kAbInnerIt = getIterator(); kAbInnerIt.hasNext();)
		{
			final AbilityInfo Info = (AbilityInfo) kAbInnerIt.next();

			if (!Info.qualifies(aPC)) { continue;}

			if (Info.getAbility() != null)
			{
				AbilityInfo abI = (AbilityInfo) nameMap.put(Info.toString(), Info);
				catMap.put(Info.getCategory() + " " + Info.toString(), Info);

				if (abI != null) { useNameMap = false; }
			}
			else
			{
				warnings.add("ABILITY: Non-existant Ability \"" + Info.getKeyName() + "\"");
			}
		}

		int numberOfChoices = getChoiceCount();

		// Can't choose more entries than there are...
		if (numberOfChoices > nameMap.size())
		{
			numberOfChoices = nameMap.size();
		}

		/*
		 * this section needs to be rewritten once we determine how
		 * the new Ability Pools are going to work
		 */

		boolean tooManyAbilities = false;
		int     abilitiesChosen  = 0;
		// Don't allow choosing of more than allotted number of feats
		if (!free && (numberOfChoices > ((int) aPC.getFeats() - abilitiesChosen)))
		{
			numberOfChoices  = (int) aPC.getFeats() - abilitiesChosen;
			tooManyAbilities = true;
		}

		if (numberOfChoices == 0)
		{
			return false;
		}

		List choices = useNameMap ?	new ArrayList(nameMap.keySet()) : new ArrayList(catMap.keySet());
		List xs;

		if (numberOfChoices == nameMap.size())
		{
			xs = choices;
		}
		else
		{
			// Force user to make enough selections
			while (true)
			{
				xs = Globals.getChoiceFromList(
						"Choose abilities",
						choices,
						new ArrayList(),
						numberOfChoices);

				if (xs.size() != 0)
				{
					break;
				}
			}
		}

		// Add to list of things to add to the character
		for (Iterator e = xs.iterator(); e.hasNext();)
		{
			if (abilitiesToAdd == null)
			{
				abilitiesToAdd = new ArrayList();
			}
			final String  choice = (String) e.next();
			AbilityInfo Info = useNameMap ?
				(AbilityInfo) nameMap.get(choice):
				(AbilityInfo) catMap.get(choice);

			final Ability anAbility = Info.getAbility();

			if (anAbility != null)
			{
				abilitiesToAdd.add(Info);
				++abilitiesChosen;
//				AbilityUtilities.modFeat(aPC, null, anAbility.getKeyName(), true, false);
				if (free == true)
				{
					// Need to pay for it first
					aPC.setFeats(aPC.getFeats() + 1);
				}
				AbilityUtilities.modFeat(aPC, null, Info.toString(), true, false);

			}
			else
			{
				warnings.add("ABILITY: Non-existant Ability \"" + choice + "\"");
			}
		}

		if (tooManyAbilities)
		{
			warnings.add("ABILITY: Some Abilities were not granted -- not enough remaining feats");
			return false;
		}

		return true;
	}

	public void apply(PlayerCharacter aPC)
	{
		for (Iterator i = abilitiesToAdd.iterator(); i.hasNext(); )
		{
			AbilityInfo info = (AbilityInfo)i.next();
			// Ability ability = info.getAbility();
			AbilityUtilities.modFeat(aPC, null, info.toString(), true, false);

			if (free == true)
			{
				aPC.setFeats(aPC.getFeats() + 1);
			}
		}
	}

	public Object clone()
	{
		KitAbilities aClone = null;
		aClone = (KitAbilities)super.clone();
		aClone.abilityStore = abilityStore;
		aClone.free = free;
		aClone.stringRep = stringRep;
		return aClone;
	}

	public String getObjectName()
	{
		return "Abilities";
	}
}
