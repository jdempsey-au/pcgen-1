/**
 * StatChoiceManager.java
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

import pcgen.core.PObject;
import pcgen.core.PlayerCharacter;
import pcgen.core.SettingsHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is the chooser that deals with choosing a stat.
 */
public class StatChoiceManager extends AbstractComplexChoiceManager {

	/**
	 * Make a new stat chooser.
	 *
	 * @param aPObject
	 * @param choiceString
	 * @param aPC
	 */
	public StatChoiceManager(
			PObject         aPObject,
			String          choiceString,
			PlayerCharacter aPC)
	{
		super(aPObject, choiceString, aPC);
		title = "Stat choice";
		chooserHandled = "STAT";
		
		if (choices != null && choices.size() > 0 &&
				((String) choices.get(0)).equals(chooserHandled)) {
			choices = choices.subList(1, choices.size());
		}
	}

	/**
	 * Parse the Choice string and build a list of available choices.
	 * 
	 * @param aPc
	 * @param availableList
	 * @param selectedList
	 */
	public void getChoices(
			final PlayerCharacter aPc,
			final List            availableList,
			final List            selectedList)
	{
		final List     excludeList = new ArrayList();
		final Iterator choiceIt    = choices.iterator();

		while (choiceIt.hasNext())
		{
			final String sExclude = (String) choiceIt.next();
			final int iStat = SettingsHandler.getGame().getStatFromAbbrev(sExclude);

			if (iStat >= 0)
			{
				excludeList.add(SettingsHandler.getGame().s_ATTRIBSHORT[iStat]);
			}
		}

		for (int x = 0; x < SettingsHandler.getGame().s_ATTRIBSHORT.length; ++x)
		{
			if (!excludeList.contains(SettingsHandler.getGame().s_ATTRIBSHORT[x]))
			{
				availableList.add(SettingsHandler.getGame().s_ATTRIBSHORT[x]);
			}
		}
		
		pobject.addAssociatedTo(selectedList);
	}

}
