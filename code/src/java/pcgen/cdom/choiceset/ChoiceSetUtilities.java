/*
 * Copyright (c) 2008 Tom Parker <thpr@users.sourceforge.net>
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */
package pcgen.cdom.choiceset;

import java.util.Collection;
import java.util.Comparator;

import pcgen.cdom.base.PrimitiveChoiceSet;

/**
 * ChoiceSetUtilities is a set of utility methods for use with objects that
 * implement pcgen.cdom.base.PrimitiveChoiceSet
 * 
 * @see pcgen.cdom.base.PrimitiveChoiceSet
 */
public final class ChoiceSetUtilities
{

	private ChoiceSetUtilities()
	{
		// Cannot construct utility class
	}

	/**
	 * A Comparator to consistently sort PrimitiveChoiceSet objects. This is
	 * done using the ChoiceSetUtilities.compareChoiceSets method.
	 */
	public static final Comparator<PrimitiveChoiceSet<?>> WRITEABLE_SORTER = new Comparator<PrimitiveChoiceSet<?>>()
	{

		public int compare(PrimitiveChoiceSet<?> arg0,
				PrimitiveChoiceSet<?> arg1)
		{
			return compareChoiceSets(arg0, arg1);
		}
	};

	/**
	 * Compares two PrimitiveChoiceSet objects to establish which should "sort"
	 * first.
	 * 
	 * @param arg0
	 *            The first PrimitiveChoiceSet object to be used in the
	 *            comparison
	 * @param arg1
	 *            The second PrimitiveChoiceSet object to be used in the
	 *            comparison
	 * @return 0 if the PrimitiveChoiceSet objects are equal (at least in name,
	 *         may not be consistent-with-equals), less than zero if the first
	 *         given PrimitiveChoiceSet should be sorted before the second, and
	 *         greater than zero if the first given PrimitiveChoiceSet should be
	 *         sorted after the second.
	 */
	public static int compareChoiceSets(PrimitiveChoiceSet<?> arg0,
			PrimitiveChoiceSet<?> arg1)
	{
		String base = arg0.getLSTformat(false);
		if (base == null)
		{
			if (arg1.getLSTformat(false) == null)
			{
				return 0;
			}
			else
			{
				return -1;
			}
		}
		else
		{
			if (arg1.getLSTformat(false) == null)
			{
				return 1;
			}
			else
			{
				return base.compareTo(arg1.getLSTformat(false));
			}
		}
	}

	/**
	 * Concatenates the LST format of the given Collection of PrimitiveChoiceSet
	 * objects into a String using the separator as the delimiter.
	 * 
	 * The LST format for each CDOMReference is determined by calling the
	 * getLSTformat() method on the PrimitiveChoiceSet.
	 * 
	 * The items will be joined in the order determined by the ordering of the
	 * given Collection.
	 * 
	 * Ownership of the Collection provided to this method is not transferred
	 * and this constructor will not modify the given Collection.
	 * 
	 * @param c
	 *            An Collection of PrimitiveChoiceSet objects
	 * @param separator
	 *            The separating string
	 * @param useAny
	 *            use "ANY" for the global "ALL" reference when creating the LST
	 *            format
	 * @return A 'separator' separated String containing the LST format of the
	 *         given Collection of PrimitiveChoiceSet objects
	 */
	public static String joinLstFormat(
			Collection<? extends PrimitiveChoiceSet<?>> c, String separator,
			boolean useAny)
	{
		if (c == null)
		{
			return "";
		}

		final StringBuilder result = new StringBuilder(c.size() * 10);

		boolean needjoin = false;

		for (PrimitiveChoiceSet<?> obj : c)
		{
			if (needjoin)
			{
				result.append(separator);
			}
			needjoin = true;
			result.append(obj.getLSTformat(useAny));
		}

		return result.toString();
	}

}
