/*
 * AbilityListTokenTest.java
 * Copyright 2006 (C) James Dempsey <jdempsey@users.sourceforge.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.     See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on Oct 17, 2006
 *
 * $Id: $
 *
 */
package pcgen.io.exporttoken;

import junit.framework.Test;
import junit.framework.TestSuite;
import pcgen.AbstractCharacterTestCase;
import pcgen.core.Ability;
import pcgen.core.AbilityCategory;
import pcgen.core.PlayerCharacter;
import pcgen.core.SettingsHandler;
import pcgen.io.ExportHandler;
import pcgen.util.TestHelper;
import plugin.exporttokens.TextToken;

/**
 * <code>AbilityListTokenTest</code> tests the functioning of the ABILITYLIST 
 * token processing code. 
 *
 * Last Editor: $Author:  $
 * Last Edited: $Date:  $
 *
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision: $
 */
public class AbilityListTokenTest extends AbstractCharacterTestCase
{

	/**
	 * Quick test suite creation - adds all methods beginning with "test"
	 * @return The Test suite
	 */
	public static Test suite()
	{
		return new TestSuite(AbilityListTokenTest.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		PlayerCharacter character = getCharacter();

		Ability ab1 = TestHelper.makeAbility("Perform (Dance)", "FEAT",
			"General.Fighter");
		ab1.setMultiples("NO");
		AbilityCategory aCategory = SettingsHandler.getGame()
			.getAbilityCategory(ab1.getCategory());
		if (aCategory == null)
		{
			aCategory = new AbilityCategory(ab1.getCategory());
			SettingsHandler.getGame().addAbilityCategory(aCategory);
		}
		character.addAbility(aCategory, ab1, null);

		Ability ab2 = TestHelper.makeAbility("Perform (Dance)", "BARDIC",
			"General.Bardic");
		ab2.setMultiples("NO");
		aCategory = SettingsHandler.getGame().getAbilityCategory(
			ab2.getCategory());
		if (aCategory == null)
		{
			aCategory = new AbilityCategory(ab2.getCategory());
			SettingsHandler.getGame().addAbilityCategory(aCategory);
		}
		character.addAbility(aCategory, ab2, null);

		Ability ab3 = TestHelper.makeAbility("Perform (Oratory)", "FEAT",
			"General.Fighter");
		ab3.setMultiples("NO");
		aCategory = SettingsHandler.getGame().getAbilityCategory(
			ab3.getCategory());
		if (aCategory == null)
		{
			aCategory = new AbilityCategory(ab3.getCategory());
			SettingsHandler.getGame().addAbilityCategory(aCategory);
		}
		character.addAbility(aCategory, ab3, null);

		Ability ab4 = TestHelper.makeAbility("Silent Step", "FEAT", "General");
		ab4.setMultiples("NO");
		aCategory = SettingsHandler.getGame().getAbilityCategory(
			ab4.getCategory());
		if (aCategory == null)
		{
			aCategory = new AbilityCategory(ab4.getCategory());
			SettingsHandler.getGame().addAbilityCategory(aCategory);
		}
		character.addAbility(aCategory, ab4, null);
	}

	/**
	 * Test the output for positive numbers with fractions.
	 */
	public void testTypes()
	{
		AbilityListToken tok = new AbilityListToken();
		ExportHandler eh = new ExportHandler(null);
		PlayerCharacter character = getCharacter();

		assertEquals("ABILITYLIST.FEAT",
			"Perform (Dance), Perform (Oratory), Silent Step", tok
				.getToken("ABILITYLIST.FEAT", character, eh));
		assertEquals("ABILITYLIST.FEAT.TYPE=Fighter",
			"Perform (Dance), Perform (Oratory)", tok
				.getToken("ABILITYLIST.FEAT.TYPE=Fighter", character, eh));
		assertEquals("ABILITYLIST.FEAT.!TYPE=Fighter",
			"Silent Step", tok
				.getToken("ABILITYLIST.FEAT.!TYPE=Fighter", character, eh));
	}

	/**
	 * Test the output for negative numbers with fractions.
	 */
	public void testCategory()
	{
		AbilityListToken tok = new AbilityListToken();
		ExportHandler eh = new ExportHandler(null);
		PlayerCharacter character = getCharacter();

		assertEquals("ABILITYLIST.BARDIC",
			"Perform (Dance)", tok
				.getToken("ABILITYLIST.BARDIC", character, eh));
	}

}