/*
 * ClassData.java
 * Copyright 2006 (C) Aaron Divinsky <boomer70@yahoo.com>
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
 * Current Ver: $Revision$
 * Last Editor: $Author: $
 * Last Edited: $Date$
 */
package pcgen.core.npcgen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pcgen.core.Ability;
import pcgen.core.AbilityCategory;
import pcgen.core.Categorisable;
import pcgen.core.Globals;
import pcgen.core.PCStat;
import pcgen.core.SettingsHandler;
import pcgen.core.Skill;
import pcgen.util.WeightedList;

/**
 * Stores information about how to randomly generate selections for a class.
 * 
 * @author boomer70 <boomer70@yahoo.com>
 * 
 * @since 5.11.1
 */
public class ClassData
{
	private String theClassKey = null;
	
	// TODO Can this be a PCStat?
	private WeightedList<String> theStatWeights = null;
	private WeightedList<SkillChoice> theSkillWeights = null;
	private Map<AbilityCategory, WeightedList<Ability>> theAbilityWeights = null;
	
	/**
	 * Creates an empty <tt>ClassData</tt> object
	 * 
	 * @param aClassKey The key of the class this data is for
	 */
	public ClassData( final String aClassKey )
	{
		theClassKey = aClassKey;
	}
	
	/**
	 * @return The key of the class this data is for
	 */
	public String getClassKey()
	{
		return theClassKey;
	}
	
	/**
	 * @param aStatAbbr The stat abbreviation to add
	 * @param aWeight The weight to associate with it.
	 */
	public void addStat( final String aStatAbbr, final int aWeight )
	{
		if ( theStatWeights == null )
		{
			theStatWeights = new WeightedList<String>();
		}
		theStatWeights.add(aWeight, aStatAbbr);
	}
	
	/**
	 * @return <tt>WeightedList</tt> of stat abbreviations.
	 */
	public WeightedList<String> getStatWeights()
	{
		// Make sure that we have all the stats
		final List<PCStat> statList = SettingsHandler.getGame().getUnmodifiableStatList();
		for ( final PCStat stat : statList )
		{
			if ( theStatWeights == null || theStatWeights.contains(stat.getAbb()) == false )
			{
				theStatWeights.add(1, stat.getAbb());
			}
		}
		return theStatWeights;
	}
	
	/**
	 * @param aKey
	 * @param aWeight
	 */
	public void addSkill( final String aKey, final int aWeight )
	{
		if ( theSkillWeights == null )
		{
			theSkillWeights = new WeightedList<SkillChoice>();
		}
		for ( final SkillChoice sc : theSkillWeights )
		{
			if ( sc.hasSkill(aKey) )
			{
				return;
			}
		}
		theSkillWeights.add(aWeight, new SkillChoice(aKey));
	}
	
	/**
	 * @param aKey
	 */
	public void removeSkill( final String aKey )
	{
		if ( theSkillWeights == null )
		{
			return;
		}
		theSkillWeights.remove(new SkillChoice(aKey));
	}
	
	/**
	 * @return <tt>WeightedList</tt> of Skill keys
	 */
	public WeightedList<SkillChoice> getSkillWeights()
	{
		if ( theSkillWeights == null )
		{
			for ( final Skill skill : Globals.getSkillList() )
			{
				addSkill( skill.getKeyName(), 1 );
			}
		}
		return theSkillWeights;
	}
	
	/**
	 * @param aCategory
	 * @param anAbility
	 * @param aWeight
	 */
	public void addAbility( final AbilityCategory aCategory, final Ability anAbility, final int aWeight )
	{
		if ( theAbilityWeights == null )
		{
			theAbilityWeights = new HashMap<AbilityCategory, WeightedList<Ability>>();
		}
		WeightedList<Ability> abilities = theAbilityWeights.get(aCategory);
		if ( abilities == null )
		{
			abilities = new WeightedList<Ability>();
			theAbilityWeights.put(aCategory, abilities);
		}
		if ( ! abilities.contains(anAbility) )
		{
			abilities.add(aWeight, anAbility);
		}
	}
	
	/**
	 * Removes an Ability from the list of abilities.
	 * 
	 * @param aCategory The AbilityCategory to remove the ability for
	 * @param anAbility The Ability to remove
	 */
	public void removeAbility( final AbilityCategory aCategory, final Ability anAbility )
	{
		if ( theAbilityWeights == null )
		{
			return;
		}
		final WeightedList<Ability> abilities = theAbilityWeights.get(aCategory);
		if ( abilities == null )
		{
			return;
		}
		abilities.remove(anAbility);
	}
	
	/**
	 * Gets the Abilities of the specified category.
	 * 
	 * <p>If there is no data for this category, all Abilities for the category
	 * will be added to the list with the same weight.
	 * 
	 * @param aCategory The category of ability to retrieve
	 * @return A <tt>WeightedList</tt> of Ability objects
	 */
	public WeightedList<Ability> getAbilityWeights( final AbilityCategory aCategory )
	{
		if ( theAbilityWeights == null )
		{
			for ( final Categorisable cat : Globals.getUnmodifiableAbilityList(aCategory.getAbilityCategory()) )
			{
				addAbility( aCategory, (Ability)cat, 1);
			}
		}
		return theAbilityWeights.get(aCategory);
	}
}
