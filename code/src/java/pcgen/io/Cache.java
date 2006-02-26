/*
 * Cache.java
 * Copyright 2002 (C) Thomas Behr <ravenlock@gmx.de>
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
 * Created on September 08, 2002, 12:15 AM
 */
package pcgen.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <code>Cache</code><br>
 * Convenience wrapper class for a HashMap containing
 * only List intances filled with String instances.
 *
 * @author Thomas Behr 08-00-02
 * @version $Revision: 1.10 $
 */
final class Cache
{
	private final Map map;

	Cache()
	{
		//should define some default or make the default constructor private making users of the cache to define its initial size
		map = new HashMap();
	}

	Cache(int initialCapacity)
	{
		map = new HashMap(initialCapacity);
	}

	/**
	 * @param key
	 * @return True if the map contains the key
	 */
	public boolean containsKey(String key)
	{
		return map.containsKey(key);
	}

	/**
	 * Get List from map given key
	 * @param key
	 * @return List
	 */
	public List get(String key)
	{
		return (List) map.get(key);
	}

	/**
	 * Put into the map
	 * @param key
	 * @param value
	 */
	public void put(String key, String value)
	{
		if (map.containsKey(key))
		{
			((List) map.get(key)).add(value);
		}
		else
		{
			final List values = new ArrayList();
			values.add(value);
			map.put(key, values);
		}
	}
}
