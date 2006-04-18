/*
 * PreDamageReduction.java
 * Copyright 2001 (C) Bryan McRoberts <merton_monk@yahoo.com>
 * Copyright 2003 (C) Chris Ward <frugal@purplewombat.co.uk>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.       See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on November 28, 2003
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 *
 */
package plugin.pretokens.test;

import pcgen.core.PlayerCharacter;
import pcgen.core.prereq.AbstractPrerequisiteTest;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteTest;

import java.util.StringTokenizer;
import java.util.List;
import java.util.Iterator;
import pcgen.core.DamageReduction;

/**
 * @author wardc
 *
 */
public class PreDamageReductionTester extends AbstractPrerequisiteTest implements PrerequisiteTest {

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#passes(pcgen.core.PlayerCharacter)
	 */
	public int passes(final Prerequisite prereq, final PlayerCharacter character) {
		int runningTotal = 0;

		// Parse the character's DR into a lookup map
		List drList = character.getDRList();
		final int target = Integer.parseInt(prereq.getOperand());
		for (Iterator i = drList.iterator(); i.hasNext(); )
		{
			DamageReduction dr = (DamageReduction)i.next();
			if (dr.getBypass().equalsIgnoreCase(prereq.getKey()))
			{
				runningTotal = prereq.getOperator().compare(dr.getReductionValue(), target);
				break;
			}
		}

		return countedTotal(prereq, runningTotal);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#kindsHandled()
	 */
	public String kindHandled() {
		return "DR"; //$NON-NLS-1$
	}
}
