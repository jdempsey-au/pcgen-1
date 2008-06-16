/*
 * Copyright 2007 (C) Tom Parker <thpr@users.sourceforge.net>
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
package pcgen.cdom.content;

import pcgen.base.formula.Formula;
import pcgen.cdom.base.ConcretePrereqObject;
import pcgen.cdom.base.FormulaFactory;

/**
 * A ChallengeRating is intended to be a type-safe wrapper for an Formula
 * serving as a challenge rating
 */
public class ChallengeRating extends ConcretePrereqObject
{

	/**
	 * A ChallengeRating for the integer constant ZERO. This is done in order to
	 * minimize memory usage and construction speed in the many cases where a
	 * default ChallengeRating of ZERO is required.
	 */
	public static final ChallengeRating ZERO = new ChallengeRating("0");

	/**
	 * The Formula for this ChallengeRating
	 */
	private final Formula rating;

	/**
	 * Constructs a new ChallengeRating with the given String. The String must
	 * either be a positive integer or "1/x" where x is a positive Integer.
	 * 
	 * @param string
	 *            The formula for this ChallengeRating
	 * @throws IllegalArgumentException
	 *             if the given String is not of the appropriate syntax
	 */
	public ChallengeRating(String string)
	{
		try
		{
			int i = Integer.parseInt(string.startsWith("1/") ? string
					.substring(2) : string);
			if (i < 0)
			{
				throw new IllegalArgumentException(
						"Challenge Rating cannot be negative");
			}
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException(
					"Challenge Rating must be a positive integer i or 1/i");
		}
		rating = FormulaFactory.getFormulaFor(string);
	}

	/**
	 * Returns a Formula indicating the rating of this ChallengeRating
	 * 
	 * @return a Formula indicating the rating of this ChallengeRating
	 */
	public Formula getRating()
	{
		return rating;
	}

	/**
	 * Returns a representation of this ChallengeRating, suitable for storing in
	 * an LST file.
	 */
	public String getLSTformat()
	{
		String str = rating.toString();
		if (str.startsWith("-"))
		{
			str = "1/" + str.substring(1);
		}
		return str;
	}

	/**
	 * Returns the consistent-with-equals hashCode for this ChallengeRating
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return rating.hashCode();
	}

	/**
	 * Returns true if this ChallengeRating is equal to the given Object.
	 * Equality is defined as being another ChallengeRating object with equal
	 * rating and Prerequisites
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (o instanceof ChallengeRating)
		{
			ChallengeRating other = (ChallengeRating) o;
			return rating.equals(other.rating) && equalsPrereqObject(other);
		}
		return false;
	}
}
