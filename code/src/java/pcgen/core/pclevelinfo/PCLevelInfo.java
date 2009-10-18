/*
 * PCLevelInfo.java
 * Copyright 2001 (C) Greg Bingleman <byngl@hotmail.com>
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
 * Created on November 29, 2002, 10:38 PM
 *
 * $Id$
 */
package pcgen.core.pclevelinfo;

import pcgen.core.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <code>PCLevelInfo</code>.
 *
 * Represents the data kept about a level that a PC has added.
 *
 * @author Greg Bingleman <byngl@hotmail.com>
 * @version  $Revision$
 */
public final class PCLevelInfo implements Cloneable
{
	private List<PCLevelInfoStat>            statsPostModified    = null;
	private List<PCLevelInfoStat>            statsPreModified     = null;
	private String          classKeyName         = "";
	private int             level                = 0;
	private int             skillPointsGained    = Integer.MIN_VALUE;
	private int             skillPointsRemaining = 0;
	private PlayerCharacter aPC;
	private List<Ability>            objects              = new ArrayList<Ability>(1);

	/**
	 * Creates a new PCLevelInfo object.
	 *
	 * @param  aPC              The PC this level is part of
	 * @param  argClassKeyName  The KeyName of the class taken at this level
	 */
	public PCLevelInfo(final PlayerCharacter aPC, final String argClassKeyName)
	{
		super();
		this.aPC     = aPC;
		classKeyName = argClassKeyName;
	}

	/**
	 * Set the Class at this level
	 *
	 * @param  argClassKeyName  the KeyName of the class
	 */
	public void setClassKeyName(final String argClassKeyName)
	{
		classKeyName = argClassKeyName;
	}

	/**
	 * @return  the Keyname of the Class at this level
	 */
	public String getClassKeyName()
	{
		return classKeyName;
	}

	/**
	 * Set the level that this represents
	 *
	 * @param  arg  an int representing the level
	 */
	public void setLevel(final int arg)
	{
		level = arg;
	}

	/**
	 * @return  an int representing the level
	 */
	public int getLevel()
	{
		return level;
	}

	/**
	 * Get a list of character stats
	 *
	 * @param   preMod  Whether any increment gaind at this level should be
	 *                  applied
	 *
	 * @return  a list of character stats at this level
	 */
	public List<PCLevelInfoStat> getModifiedStats(final boolean preMod)
	{
		List<PCLevelInfoStat> result = statsPostModified;

		if (preMod)
		{
			result = statsPreModified;
		}

		return result;
	}

	/**
	 * Set the number of kill points gained at this level
	 *
	 * @param  arg  the number of skill points gained
	 */
	public void setSkillPointsGained(final int arg)
	{
		final int bonusPoints = getBonusSkillPool();
		setFixedSkillPointsGained(arg + bonusPoints);
	}

	/**
	 * @return  the number of skill points gained
	 */
	public int getSkillPointsGained()
	{
		// If this information in not saved on PCG, then try to recalc it
		if ((skillPointsGained == Integer.MIN_VALUE) && (classKeyName.length() > 0))
		{
			final PCClass aClass = Globals.getContext().ref.silentlyGetConstructedCDOMObject(PCClass.class, classKeyName);
			skillPointsGained = aClass.recalcSkillPointMod(aPC, level) +
				getBonusSkillPool();
		}

		return skillPointsGained;
	}

	/**
	 * Set the number of skill points gained at this level that have not been
	 * spent yet.
	 *
	 * @param  arg  skill points remaining for this level
	 */
	public void setSkillPointsRemaining(final int arg)
	{
		skillPointsRemaining = arg;
	}

	/**
	 * @return  The number of skill points remaining at this level
	 */
	public int getSkillPointsRemaining()
	{
		return skillPointsRemaining;
	}

	/**
	 * Get the value of a character stat at this level
	 *
	 * @param   statAbb      the Abbreviation of the stat (STR, DEX, etc)
	 * @param   includePost  whether to include any stat increases gained at
	 *                       this level
	 *
	 * @return  the value of the stat at this level.
	 */
	public int getTotalStatMod(final PCStat aStat, final boolean includePost)
	{
		int mod = 0;

		if (statsPreModified != null)
		{
			for ( PCLevelInfoStat stat : statsPreModified )
			{
				if (stat.getStat().equals(aStat))
				{
					mod += stat.getStatMod();
				}
			}
		}

		if (includePost && (statsPostModified != null))
		{
			for ( PCLevelInfoStat stat : statsPostModified )
			{
				if (stat.getStat().equals(aStat))
				{
					mod += stat.getStatMod();
				}
			}
		}

		return mod;
	}

	/**
	 * Modify a character stat at this level.  Only works with one of the two
	 * stat lists pre or post depending on the value of isPreMod.  If the list
	 * doesn't exist yet, an empty one is created, then we search for the stat
	 * which has an abbreviation matching statABB.  If we find a match we adjust
	 * it by mod, if we don't find a match set it to mod.  If we cant find a
	 * stat matching statABB, make a new on, set its value to mod and store it
	 * in the list.
	 *
	 * @param  statAbb   The abbreviation of the stat to modifiy
	 * @param  mod       The adjustment to make
	 * @param  isPreMod  Whether the increment should be pre or post the
	 *                   calculation of other benefits gained at this level.
	 */
	public void addModifiedStat(
		final PCStat  stat,
		final int     mod,
		final boolean isPreMod)
	{
		final List<PCLevelInfoStat> statList;

		if (isPreMod)
		{
			if (statsPreModified == null)
			{
				statsPreModified = new ArrayList<PCLevelInfoStat>();
			}

			statList = statsPreModified;
		}
		else
		{
			if (statsPostModified == null)
			{
				statsPostModified = new ArrayList<PCLevelInfoStat>();
			}

			statList = statsPostModified;
		}

		PCLevelInfoStat aStat;

		for (int i = 0; i < statList.size(); ++i)
		{
			aStat = statList.get(i);

			if (stat.equals(aStat.getStat()))
			{
				aStat.modifyStat(mod);

				if (aStat.getStatMod() == 0)
				{
					statList.remove(aStat);
				}

				return;
			}
		}

		statList.add(new PCLevelInfoStat(stat, mod));
	}

	/**
	 * Calculate the number of bonus skill points added by this level.
	 *
	 * @return  the number of bonus skill points added by this level
	 */
	private int getBonusSkillPool()
	{
		int           returnValue = 0;
		final PCClass aClass      = aPC.getClassKeyed(classKeyName);

		final String purchaseName = SettingsHandler.getGame().getPurchaseModeMethodName();
		if (purchaseName != null)
		{
			PointBuyMethod pbm = SettingsHandler.getGame().getPurchaseMethodByName(purchaseName);

			returnValue += (int) aPC.calcBonusFromList(pbm.getBonusListOfType("SKILLPOOL", "NUMBER"), null);
		}


		if (aClass != null)
		{
			// These bonuses apply to the level or higher. We have to add and then remove
			// the previous to get the effective level bonus
			returnValue += (int) aClass.getBonusTo("SKILLPOOL", "NUMBER", level, aPC);
			returnValue -= (int) aClass.getBonusTo("SKILLPOOL", "NUMBER", level - 1, aPC);
		}

		if (level == 1)
		{
			returnValue +=
				(int) aPC.getTotalBonusTo("SKILLPOOL", "CLASS." + classKeyName);
		}

		returnValue += (int) aPC.getTotalBonusTo(
				"SKILLPOOL",
				"CLASS." + classKeyName + ";LEVEL." + Integer.toString(level));

		returnValue += (int) aPC.getTotalBonusTo(
				"SKILLPOOL",
				"LEVEL." + aPC.getCharacterLevel(this));

		return returnValue;
	}

	/**
	 * Associates an object with this character level, used to add Abilities
	 *
	 * @param  ability the Ability to add
	 */
	public void addObject(final Ability ability)
	{
		objects.add(ability);
	}

	/**
	 * Get any Abilites added at this level
	 *
	 * @return  List
	 */
	public List<? extends Ability> getObjects()
	{
		return objects;
	}

	@Override
	public PCLevelInfo clone()
	{
		PCLevelInfo clone = new PCLevelInfo(aPC, classKeyName);
		for (Iterator<? extends Ability> i = objects.iterator(); i.hasNext(); )
		{
			clone.objects.add(i.next());
		}
		if (statsPostModified != null)
		{
			for ( PCLevelInfoStat stat : statsPostModified )
			{
				if (clone.statsPostModified == null)
				{
					clone.statsPostModified = new ArrayList<PCLevelInfoStat>();
				}
				clone.statsPostModified.add(stat);
			}
		}
		if (statsPreModified != null)
		{
			for ( PCLevelInfoStat stat : statsPreModified )
			{
				if (clone.statsPreModified == null)
				{
					clone.statsPreModified = new ArrayList<PCLevelInfoStat>();
				}
				clone.statsPreModified.add(stat);
			}
		}
		return clone;
	}

	public void setFixedSkillPointsGained(int arg)
	{
		skillPointsGained = arg;
	}
}
