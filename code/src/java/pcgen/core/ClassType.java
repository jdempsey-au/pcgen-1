/*
 * Campaign.java
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
 */
package pcgen.core;

import pcgen.core.utils.MessageType;
import pcgen.core.utils.ShowMessageDelegate;

/**
 * <code>Campaign</code>.
 *
 * @author Felipe Diniz <fdiniz@users.sourceforge.net>
 * @version $Revision$
 */
public final class ClassType extends PObject
{
//	private boolean isPrestige = false;
	private String crFormula = "";
	private boolean xpPenalty = true;
	private boolean isMonster = false;

	/**
	 * Set the CR Formula
	 * @param crFormula
	 */
	public void setCRFormula(final String crFormula)
	{
		this.crFormula = crFormula;
	}

	/**
	 * Get the CR formula
	 * @return CR Formula
	 */
	public String getCRFormula()
	{
		return crFormula;
	}

	/**
	 * Set the monster
	 * @param monster
	 */
	public void setMonster(final boolean monster)
	{
		isMonster = monster;
	}

	/**
	 * is monster
	 * @return TRUE if it is a monster
	 */
	public boolean isMonster()
	{
		return isMonster;
	}

	/**
	 * Set the XP penalty
	 * @param xpPenalty
	 */
	public void setXPPenalty(final boolean xpPenalty)
	{
		this.xpPenalty = xpPenalty;
	}

	/**
	 * Get the XP penalty
	 * @return true if there is a penalty
	 */
	public boolean getXPPenalty()
	{
		return xpPenalty;
	}

	public Object clone()
	{
		ClassType newClassType = null;

		try
		{
			newClassType = (ClassType) super.clone();
			newClassType.isMonster = isMonster;

//			newClassType.isPrestige = isPrestige;
			newClassType.crFormula = new String(crFormula);
			newClassType.xpPenalty = xpPenalty;
		}
		catch (CloneNotSupportedException exc)
		{
			ShowMessageDelegate.showMessageDialog(exc.getMessage(), Constants.s_APPNAME, MessageType.ERROR);
		}

		return newClassType;
	}
}
