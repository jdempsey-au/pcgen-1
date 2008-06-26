/*
 * Copyright 2007, 2008 (C) Tom Parker <thpr@users.sourceforge.net>
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
package pcgen.cdom.formula;

import pcgen.base.formula.Formula;
import pcgen.cdom.enumeration.StringKey;
import pcgen.core.Globals;
import pcgen.core.PlayerCharacter;
import pcgen.core.SizeAdjustment;

/**
 * A FixedSizeFormula is a Formula that returns a deterministic value, used to
 * uniquely identify a SizeAdjustment. The SizeAdjustment for which this Formula
 * will return the value must be defined during construction of the
 * FixedSizeFormula.
 */
public class FixedSizeFormula implements Formula
{

	/**
	 * The underlying SizeAdjustment for which this Formula will return the
	 * identifying value.
	 */
	private final SizeAdjustment size;

	/**
	 * Creates a new FixedSizeFormula for the given SizeAdjustment.
	 * 
	 * @param s
	 *            The SizeAdjustment for which this Formula will return the
	 *            identifying value.
	 * @throws IllegalArgumentException
	 *             if the given SizeAdjustment is null
	 */
	public FixedSizeFormula(SizeAdjustment s)
	{
		if (s == null)
		{
			throw new IllegalArgumentException(
					"Size Adjustment for FixedSizeFormula cannot be null");
		}
		size = s;
	}

	/**
	 * Returns a String representation of this FixedSizeFormula, primarily for
	 * purposes of debugging. It is strongly advised that no dependency on this
	 * method be created, as the return value may be changed without warning.
	 */
	@Override
	public String toString()
	{
		return size.get(StringKey.ABB);
	}

	/**
	 * Returns the consistent-with-equals hashCode for this FixedSizeFormula
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return size.hashCode();
	}

	/**
	 * Returns true if this FixedSizeFormula is equal to the given Object.
	 * Equality is defined as being another FixedSizeFormula object with equal
	 * underlying SizeAdjustment
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o)
	{
		return o instanceof FixedSizeFormula
				&& size.equals(((FixedSizeFormula) o).size);
	}

	/**
	 * Resolves to the identifying value of the SizeAdjustment provided during
	 * construction of the FixedSizeFormula.
	 * 
	 * @return the identifying value of the SizeAdjustment this FixedSizeFormula
	 *         represents.
	 */
	public Integer resolve(PlayerCharacter pc, String source)
	{
		return Globals.sizeInt(size.getAbbreviation());
	}
}
