/*
 * Copyright (c) Thomas Parker, 2007, 2008.
 *  portions derived from CoreUtility.java
 *    Copyright 2002 (C) Bryan McRoberts <merton_monk@yahoo.com>
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 * 
 */
package pcgen.base.lang;

import java.util.Collection;

public final class StringUtil
{

	private StringUtil()
	{
		// Do not instantiate
	}

	/**
	 * Concatenates the Collection of Strings into a String using the separator
	 * as the delimiter.
	 * 
	 * @param strings
	 *            An Collection of strings
	 * @param separator
	 *            The separating string
	 * @return A 'separator' separated String
	 */
	public static String join(final Collection<?> strings,
		final String separator)
	{
		return joinToStringBuffer(strings, separator).toString();
	}

	/**
	 * Concatenates the Collection of Strings into a StringBuffer using the
	 * separator as the delimiter.
	 * 
	 * @param strings
	 *            An Collection of strings
	 * @param separator
	 *            The separating character
	 * @return A 'separator' separated String
	 */
	public static StringBuilder joinToStringBuffer(final Collection<?> strings,
		final String separator)
	{
		if (strings == null)
		{
			return new StringBuilder();
		}

		final StringBuilder result = new StringBuilder(strings.size() * 10);

		boolean needjoin = false;

		for (Object obj : strings)
		{
			if (needjoin)
			{
				result.append(separator);
			}
			needjoin = true;
			result.append(obj.toString());
		}

		return result;
	}

	/**
	 * Concatenates the Array of Strings into a String using the separator as
	 * the delimiter.
	 * 
	 * @param strings
	 *            An Array of strings
	 * @param separator
	 *            The separating string
	 * @return A 'separator' separated String
	 */
	public static String join(String[] strings, String separator)
	{
		if (strings == null)
		{
			return "";
		}

		final StringBuilder result = new StringBuilder(strings.length * 10);

		boolean needjoin = false;

		for (Object obj : strings)
		{
			if (needjoin)
			{
				result.append(separator);
			}
			needjoin = true;
			result.append(obj.toString());
		}

		return result.toString();
	}
}
