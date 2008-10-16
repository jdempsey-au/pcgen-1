/*
 * PCTemplate.java
 * Copyright 2001 (C) Bryan McRoberts <merton_monk@yahoo.com>
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
 * Created on April 21, 2001, 2:15 PM
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 *
 */
package pcgen.core;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pcgen.base.lang.StringUtil;
import pcgen.cdom.enumeration.IntegerKey;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.Region;
import pcgen.cdom.enumeration.SubRace;
import pcgen.cdom.enumeration.SubRegion;
import pcgen.core.bonus.BonusObj;
import pcgen.util.enumeration.Visibility;

/**
 * <code>PCTemplate</code>.
 * 
 * @author Mark Hulsman <hulsmanm@purdue.edu>
 * @version $Revision$
 */
public final class PCTemplate extends PObject
{
	/**
	 * Get the total adjustment to Challenge rating of a character at a given
	 * level (Class and Hit Dice). This will include the absolute adjustment
	 * made with CR:, LEVEL:<num>:CR and HD:<num>:CR tags
	 * 
	 * @param level
	 *            The level to calculate the adjustment for
	 * @param hitdice
	 *            The Hit dice to calculate the adjustment for
	 * 
	 * @return a Challenge Rating adjustment
	 */
	public float getCR(final int level, final int hitdice)
	{
		float localCR = getSafe(ObjectKey.CR_MODIFIER).floatValue();
		for (PCTemplate pct : getConditionalTemplates(level, hitdice))
		{
			localCR += pct.getSafe(ObjectKey.CR_MODIFIER).floatValue();
		}
		return localCR;
	}

	/**
	 * Produce a tailored PCC output, used for saving custom templates.
	 * 
	 * @return PCC Text
	 */
	@Override
	public String getPCCText()
	{
		final StringBuffer txt = new StringBuffer(200);
		txt.append(getDisplayName());
		txt.append("\t");
		txt.append(StringUtil.joinToStringBuffer(Globals.getContext().unparse(
				this), "\t"));
		txt.append("\t");

		if ((getChooseLanguageAutos() != null)
				&& (getChooseLanguageAutos().length() > 0))
		{
			txt.append("\tCHOOSE:LANGAUTO:").append(getChooseLanguageAutos());
		}

		txt.append(super.getPCCText(false));

		return txt.toString();
	}

	/**
	 * Get the override that this template applies to subracetype
	 * 
	 * @return The new subracetype
	 */
	public String getSubRace()
	{
		SubRace sr = get(ObjectKey.SUBRACE);
		if (sr == null)
		{
			if (getSafe(ObjectKey.USETEMPLATENAMEFORSUBRACE))
			{
				return this.getDisplayName();
			}
			return "None";
		}
		return sr.toString();
	}

	/**
	 * Get the override that this template applies to Region
	 * 
	 * @return The new Region
	 */
	public String getRegion()
	{
		Region sr = get(ObjectKey.REGION);
		if (sr == null)
		{
			if (getSafe(ObjectKey.USETEMPLATENAMEFORREGION))
			{
				return this.getDisplayName();
			}
			return "None";
		}
		return sr.toString();
	}

	/**
	 * Get the override that this template applies to SubRegion
	 * 
	 * @return The new SubRegion
	 */
	public String getSubRegion()
	{
		SubRegion sr = get(ObjectKey.SUBREGION);
		if (sr == null)
		{
			if (getSafe(ObjectKey.USETEMPLATENAMEFORSUBREGION))
			{
				return this.getDisplayName();
			}
			return "None";
		}
		return sr.toString();
	}

	/**
	 * Query whether this Template is removable. Factors in the visibility of
	 * the Template
	 * 
	 * @return whether this Template is removable
	 */
	public boolean isRemovable()
	{
		boolean result = false;

		if ((getSafe(ObjectKey.VISIBILITY) == Visibility.DEFAULT)
				|| (getSafe(ObjectKey.VISIBILITY) == Visibility.DISPLAY_ONLY))
		{
			result = getSafe(ObjectKey.REMOVABLE);
		}

		return result;
	}

	/**
	 * Make a copy of this Template
	 * 
	 * @return a clone of this Template
	 * 
	 * @throws CloneNotSupportedException
	 */
	@Override
	public PCTemplate clone() throws CloneNotSupportedException
	{
		return (PCTemplate) super.clone();
	}

	/**
	 * Modify the list passed in to include any special abilities granted by
	 * this Template
	 * 
	 * @param aList
	 *            The list to be modified
	 * @param level
	 *            The level to add Special abilities for
	 * @param hitdice
	 *            the hit die (/range) to add Special Abilities for
	 * 
	 * @return the list passed in with any special abilities this template
	 *         grants added to it
	 */
	@Override
	public List<SpecialAbility> addSpecialAbilitiesToList(
			final List<SpecialAbility> aList, PlayerCharacter pc)
	{
		super.addSpecialAbilitiesToList(aList, pc);
		for (PCTemplate pct : getConditionalTemplates(pc.getTotalLevels(), pc
				.totalHitDice()))
		{
			pct.addSpecialAbilitiesToList(aList, pc);
		}
		return aList;
	}

	/**
	 * Get face
	 * 
	 * @return face
	 */
	public Point2D.Double getFace()
	{
		BigDecimal width = get(ObjectKey.FACE_WIDTH);
		BigDecimal height = get(ObjectKey.FACE_HEIGHT);
		if (width == null && height == null)
		{
			return null;
		}
		return new Point2D.Double(width.doubleValue(), height.doubleValue());
	}

	public List<PCTemplate> getConditionalTemplates(int totalLevels,
			int totalHitDice)
	{
		List<PCTemplate> returnList = new ArrayList<PCTemplate>();

		for (PCTemplate rlt : getSafeListFor(ListKey.REPEATLEVEL_TEMPLATES))
		{
			for (PCTemplate lt : rlt.getSafeListFor(ListKey.LEVEL_TEMPLATES))
			{
				if (lt.get(IntegerKey.LEVEL) <= totalLevels)
				{
					returnList.add(lt);
				}
			}
		}

		for (PCTemplate lt : getSafeListFor(ListKey.LEVEL_TEMPLATES))
		{
			if (lt.get(IntegerKey.LEVEL) <= totalLevels)
			{
				returnList.add(lt);
			}
		}

		for (PCTemplate lt : getSafeListFor(ListKey.HD_TEMPLATES))
		{
			if (lt.get(IntegerKey.HD_MAX) <= totalHitDice
					&& lt.get(IntegerKey.HD_MIN) >= totalHitDice)
			{
				returnList.add(lt);
			}
		}
		return returnList;
	}

	@Override
	public List<BonusObj> getRawBonusList(PlayerCharacter pc)
	{
		List<BonusObj> list = new ArrayList<BonusObj>(super.getRawBonusList(pc));
		/*
		 * TODO Does this require a test of getTotalLevels() totalHitDice() on
		 * the PC?
		 * 
		 * for (PCTemplate pct : getConditionalTemplates(pc.getTotalLevels(),
		 * pc.totalHitDice())) { list.addAll(pct.getRawBonusList(pc); }
		 */
		for (PCTemplate rlt : getSafeListFor(ListKey.REPEATLEVEL_TEMPLATES))
		{
			for (PCTemplate lt : rlt.getSafeListFor(ListKey.LEVEL_TEMPLATES))
			{
				list.addAll(lt.getRawBonusList(pc));
			}
		}

		for (PCTemplate lt : getSafeListFor(ListKey.LEVEL_TEMPLATES))
		{
			list.addAll(lt.getRawBonusList(pc));
		}

		for (PCTemplate lt : getSafeListFor(ListKey.HD_TEMPLATES))
		{
			list.addAll(lt.getRawBonusList(pc));
		}
		// end potential TO-DO change
		return list;
	}

	@Override
	public PObject getActiveEquivalent(PlayerCharacter pc)
	{
		return pc.getTemplateKeyed(getKeyName());
	}

}
