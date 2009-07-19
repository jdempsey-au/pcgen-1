/*
 * Copyright 2009 (C) Tom Parker <thpr@users.sourceforge.net>
 * Derived from PCClass.java
 * Copyright 2001 (C) Bryan McRoberts <merton_monk@yahoo.com>
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package pcgen.core.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pcgen.cdom.enumeration.AssociationKey;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.PCClass;
import pcgen.core.PlayerCharacter;
import pcgen.core.SubstitutionClass;
import pcgen.core.prereq.PrereqHandler;
import pcgen.util.chooser.ChooserFactory;
import pcgen.util.chooser.ChooserInterface;

public class SubstitutionClassApplication
{

	public static void checkForSubstitutionClass(PCClass cl, final int aLevel,
			final PlayerCharacter aPC)
	{
		List<SubstitutionClass> substitutionClassList = cl
				.getListFor(ListKey.SUBSTITUTION_CLASS);
		if (substitutionClassList == null || substitutionClassList.isEmpty())
		{
			return;
		}
		List<String> columnNames = new ArrayList<String>(1);
		columnNames.add("Name");

		List<PCClass> choiceList = new ArrayList<PCClass>();
		buildSubstitutionClassChoiceList(cl, choiceList, cl.getLevel(aPC), aPC);
		if (choiceList.size() <= 1)
		{
			return; // This means the there are no classes for which
			// the pc meets the prerequisitions and thus the
			// base class is chosen.
		}

		final ChooserInterface c = ChooserFactory.getChooserInstance();
		c.setTitle("Substitution Levels");
		c.setMessageText("Choose one of the listed substitution levels "
				+ "or the base class(top entry).  "
				+ "Pressing Close will take the standard class level.");
		c.setTotalChoicesAvail(1);
		c.setPoolFlag(false);

		c.setAvailableColumnNames(columnNames);
		c.setAvailableList(choiceList);

		c.setVisible(true);

		List<PCClass> selectedList = c.getSelectedList();
		PCClass selected = null;
		if (!selectedList.isEmpty())
		{
			selected = selectedList.get(0);
		}

		if ((!selectedList.isEmpty()) && selected instanceof SubstitutionClass)
		{
			SubstitutionClass sc = (SubstitutionClass) selected;
			SubstitutionLevelSupport.applyLevelArrayModsToLevel(sc, cl, aLevel,
					aPC);
			aPC.setAssoc(cl.getActiveClassLevel(aLevel),
					AssociationKey.SUBSTITUTIONCLASS_KEY, sc.getKeyName());
			return;
		}
		else
		{
			/*
			 * the original code has the below line.. however, it appears to not
			 * be needed. I say this because if the original
			 * buildSubstitutionClassChoiceList method returned an empty list,
			 * it returned right away without calling this method.
			 */
			aPC.removeAssoc(cl.getActiveClassLevel(aLevel), AssociationKey.SUBSTITUTIONCLASS_KEY);
			return;
		}

	}

	/**
	 * Build a list of Substitution Classes for the user to choose from. The
	 * list passed in will be populated.
	 * 
	 * @param choiceNames
	 *            The list of substitution classes to choose from.
	 * @param level
	 *            The class level to determine the choices for
	 * @param aPC
	 */
	public static void buildSubstitutionClassChoiceList(PCClass cl,
			final List<PCClass> choiceList, final int level,
			final PlayerCharacter aPC)
	{

		for (SubstitutionClass sc : cl.getListFor(ListKey.SUBSTITUTION_CLASS))
		{
			if (!PrereqHandler.passesAll(sc.getPrerequisiteList(), aPC, cl))
			{
				continue;
			}
			if (!sc.hasOriginalClassLevel(level))
			{
				continue;
			}
			if (!SubstitutionLevelSupport.qualifiesForSubstitutionLevel(sc,
					aPC, level))
			{
				continue;
			}

			choiceList.add(sc);
		}
		Collections.sort(choiceList); // sort the SubstitutionClass's
		choiceList.add(0, cl); // THEN add the base class as the first choice
	}

}
