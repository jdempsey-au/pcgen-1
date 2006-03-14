/*
 * KitSelect.java
 * Copyright 2005 (C) Aaron Divinsky <boomer70@yahoo.com>
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
 * Created on December 13, 2005
 *
 * $Id$
 */
package pcgen.core.kit;

import pcgen.core.Kit;
import pcgen.core.PlayerCharacter;

import java.io.Serializable;
import java.util.List;

/**
 * <code>KitSelect</code>.
 *
 * @author Aaron Divinsky <boomer70@yahoo.com>
 * @version $Revision$
 */
public final class KitSelect extends BaseKit implements Serializable, Cloneable
{
	// Only change the UID when the serialized form of the class has also changed
	private static final long serialVersionUID = 1;

	private String theFormula = "";

	public KitSelect(final String formula)
	{
		theFormula = formula;
	}

	public String getFormla()
	{
		return theFormula;
	}

	public void setFormula(final String aFormula)
	{
		theFormula = aFormula;
	}

	public String toString()
	{
		return theFormula;
	}

	public boolean testApply(Kit aKit, PlayerCharacter aPC, List warnings)
	{
		aKit.setSelectValue(aPC.getVariableValue(theFormula, "").intValue());
		return true;
	}

	public void apply(PlayerCharacter aPC)
	{
		// Nothing to do.
	}

	public Object clone()
	{
		KitSelect aClone = (KitSelect)super.clone();

		aClone.theFormula = theFormula;

		return aClone;
	}

	public String getObjectName()
	{
		return "Select";
	}
}