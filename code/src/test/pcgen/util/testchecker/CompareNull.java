/**
 * CompareNull.java
 * Copyright 2005 (c) Andrew Wilson <nuance@sourceforge.net>
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
 * Current Ver: $$
 * Last Editor: $$
 * Last Edited: $$
 */

package pcgen.util.testchecker;

import pcgen.util.TestChecker;

/**
 * Is the value null?
 */
public class CompareNull extends TestChecker
{
    @Override
	public boolean check(Object obj)
	{
		return obj == null;
	}

    @Override
	public StringBuffer scribe(StringBuffer buf)
	{
		return buf.append("null");
	}
}
