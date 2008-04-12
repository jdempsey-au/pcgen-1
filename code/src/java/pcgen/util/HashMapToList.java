/*
 * Copyright 2004-2007 (C) Tom Parker <thpr@sourceforge.net>
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
 * Created on Aug 29, 2004
 * Imported into PCGen on June 18, 2005.
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 */
package pcgen.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Thomas Parker
 * 
 * Represents a Map of objects to Lists. List management is done internally to
 * this class (while copies are accessible, the lists are kept private to this
 * class).
 * 
 * This class is both value-semantic and reference-semantic.
 * 
 * In appropriate cases (such as calling the addToListFor method), HashMapToList
 * will maintain a reference to the given Object. HashMapToList will not modify
 * any of the Objects it is passed; however, it reserves the right to return
 * references to Objects it contains to other Objects.
 * 
 * However, HashMapToList also protects its internal structure (the internal
 * structure is not exposed) ... when any method in which HashMapToList returns
 * a Collection, ownership of the Collection itself is transferred to the
 * calling Object, but the contents of the Collection (keys, values, etc.) are
 * references whose ownership should be respected. Also, when any method in
 * which HashMapToList receives a Collection as a parameter, the ownership of
 * the given Collection is not transferred to HashMapToList (in other words, the
 * Collection will not be modified, and no references to the Collection will be
 * maintain by HashMapToList). HashMapToList will obviously retain references to
 * the contents of any Collections it may be passed.
 * 
 * CAUTION: If you are not looking for the value-semantic protection of this
 * class (of preventing accidental modification, then this is a convenience
 * method and is not appropriate for use in Java 1.5 (Typed Collections are
 * probably more appropriate).
 */
public class HashMapToList<K, V> implements MapToList<K, V>
{

	/*
	 * Comment to programmers on the value-semantic behavior of this class: This
	 * class protects the collections contained within this Class (the Map and
	 * the Lists) from modification by anything other than a direct call to this
	 * object. Therefore, any Collection that is returned by this Class is
	 * either (A) a copy [thus value-semantic] or (B) no longer stored within
	 * this HashMapToList.
	 * 
	 * It is therefore recommended that there never be a .putListFor() method.
	 * This is ASKING for semantic behavior problems, as it is too easy to store
	 * the list that is given (which becomes reference semantic). It is not
	 * painful and not unreasonable to call .removeListFor() and
	 * .addAllToListFor(), which is all that a putListFor convenience method
	 * would do. - Thomas Parker 1/15/07
	 */
	
	/**
	 * The actual map containing the map of objects to Lists
	 */
	private final Map<K, List<V>> mapToList = new HashMap<K, List<V>>();

	/**
	 * Creates a new HashMapToList
	 */
	public HashMapToList()
	{
		super();
	}

	/**
	 * Initializes a List for the given key. The null value cannot be used as a
	 * key in a MapToList.
	 *
	 * This method is reference-semantic and this MapToList will maintain a
	 * strong reference to the key object given as an argument to this method.
	 *
	 * @param key
	 *            The key for which a List should be initialized in this
	 *            MapToList.
	 */
	public void initializeListFor(K key)
	{
		if (key == null)
		{
			throw new NullPointerException("Key in MapToList cannot be null");
		}
		if (mapToList.containsKey(key))
		{
			throw new IllegalArgumentException("Cannot re-initialize key: "
				+ key);
		}
		mapToList.put(key, new ArrayList<V>());
	}

	/**
	 * Adds the given value to the List for the given key. The null value cannot
	 * be used as a key in a MapToList. This method will automatically
	 * initialize the list for the given key if there is not already a List for
	 * that key.
	 *
	 * This method is reference-semantic and this MapToList will maintain a
	 * strong reference to both the key object and the value object given as
	 * arguments to this method.
	 *
	 * @param key
	 *            The key indicating which List the given object should be added
	 *            to.
	 * @param value
	 *            The value to be added to the List for the given key.
	 */
	public void addToListFor(K key, V value)
	{
		/*
		 * Note there is no requirement that a Key is added before this method
		 * is called
		 */
		if (!containsListFor(key))
		{
			initializeListFor(key);
		}
		mapToList.get(key).add(value);
	}

	/**
	 * Adds the given value to the List at the given position, for the given
	 * key. The null value cannot be used as a key in a HashMapToList. This
	 * method will automatically initialize the list for the given key if there
	 * is not already a List for that key. Any necessary null values will be
	 * inserted if the given value is larger than the current length of the
	 * list.
	 * 
	 * This method is reference-semantic and this HashMapToList will maintain a
	 * strong reference to both the key object and the value object given as
	 * arguments to this method.
	 * 
	 * @param key
	 *            The key indicating which List the given object should be added
	 *            to.
	 * @param value
	 *            The value to be added to the List for the given key.
	 * @param loc
	 *            The position at which the value will be added to the list.
	 * @return The value previously at the given position, otherwise null.
	 */
	public V addToListAt(K key, V value, int loc)
	{
		/*
		 * Note there is no requirement that a Key is added before this method
		 * is called
		 */
		if (!containsListFor(key))
		{
			initializeListFor(key);
		}
		List<V> list = mapToList.get(key);
		while (list.size() <= loc)
		{
			list.add(null);
		}
		return list.set(loc, value);
	}

	/**
	 * Adds all of the Objects in the given list to the (internal) List for the
	 * given key. The null value cannot be used as a key in a MapToList. This
	 * method will automatically initialize the list for the given key if there
	 * is not already a List for that key.
	 *
	 * This method is reference-semantic and this MapToList will maintain a
	 * strong reference to both the key object and the object in the given list.
	 *
	 * @param key
	 *            The key indicating which List the objects in the given List
	 *            should be added to.
	 * @param list
	 *            A List containing the items to be added to the List for the
	 *            given key.
	 */
	public void addAllToListFor(K key, Collection<V> list)
	{
		if (list == null)
		{
			return;
		}
		/*
		 * Note there is no requirement that a Key is added before this method
		 * is called
		 */
		if (!containsListFor(key))
		{
			initializeListFor(key);
		}
		mapToList.get(key).addAll(list);
	}

	/**
	 * Adds all of the Lists in the given MapToList to this MapToList. The
	 * resulting lists are independent, however, since MapToList is
	 * reference-semantic, the List keys and values in each list are identical.
	 * 
	 * This method is reference-semantic and this MapToList will maintain a
	 * strong reference to all key objects and objects in each list of the given
	 * MapToList.
	 * 
	 * @param mtl
	 *            The MapToList from which all of the Lists should be imported
	 */
	public void addAllLists(MapToList<K, V> mtl)
	{
		for (K key : mtl.getKeySet())
		{
			/*
			 * Note, this grab of the list for the key is safe regardless of
			 * whether mtl's getListFor is reference or value-semantic, as
			 * addAllToListFor is committed to be value-semantic and not keep or
			 * modify the received list.
			 */
			addAllToListFor(key, mtl.getListFor(key));
		}
	}

	/**
	 * Returns true if this MapToList contains a List for the given key. This
	 * method returns false if the given key is not in this MapToList.
	 *
	 * This method is value-semantic in that no changes are made to the object
	 * passed into the method.
	 *
	 * @param key
	 *            The key being tested.
	 * @return true if this MapToList contains a List for the given key; false
	 *         otherwise.
	 */
	public boolean containsListFor(K key)
	{
		return mapToList.containsKey(key);
	}

	/**
	 * Returns true if this MapToList contains a List for the given key and that
	 * list contains the given value. Note, this method returns false if the
	 * given key is not in this MapToList.
	 *
	 * This method is value-semantic in that no changes are made to the objects
	 * passed into the method.
	 *
	 * @param key
	 *            The key for the List being tested.
	 * @param value
	 *            The value to find in the List for the given key.
	 * @return true if this MapToList contains a List for the given key AND that
	 *         list contains the given value; false otherwise.
	 */
	public boolean containsInList(K key, V value)
	{
		return containsListFor(key) && mapToList.get(key).contains(value);
	}

	/**
	 * Returns the number of objects in the List for the given key. This method
	 * will throw a NullPointerException if this MapToList does not contain a
	 * List for the given key.
	 *
	 * This method is value-semantic in that no changes are made to the object
	 * passed into the method.
	 *
	 * @param key
	 *            The key being tested.
	 * @return the number of objects in the List for the given key
	 */
	public int sizeOfListFor(K key)
	{
		/*
		 * FUTURE It is possible for the context of PCGen that this class should
		 * not throw a NullPointerException if the key is not in the MapToList.
		 * It is possible that better behavior might be to return -1. However,
		 * at this time, that should not be implemented (That is VERY detailed
		 * tweaking) until there is more information on how MapToList gets used
		 * around the PCGen universe (this could be considered inconsistent with
		 * getListFor below, since it doesn't throw a NPE...) - thpr 6/19/05
		 */
		/*
		 * On the other hand, PCGen is also built with a lot of uninitialized
		 * items and at risk of NPEs... in order to fix the save problem in CVS
		 * this is currently set to zero if the key doesn't exist - thpr June
		 * 24, 2005
		 */
		List<V> list = mapToList.get(key);
		if (list == null)
		{
			return 0;
		}
		return list.size();
	}

	/**
	 * Returns a copy of the List contained in this MapToList for the given key.
	 * This method returns null if the given key is not in this MapToList.
	 *
	 * This method is value-semantic in that no changes are made to the object
	 * passed into the method and ownership of the returned List is transferred
	 * to the class calling this method.
	 *
	 * @param key
	 *            The key for which a copy of the list should be returned.
	 * @return a copy of the List contained in this MapToList for the given key;
	 *         null if the given key is not a key in this MapToList.
	 */
	public List<V> getListFor(K key)
	{
		List<V> list = mapToList.get(key);
		/*
		 * Need to 'clone' the List. This is done to ensure value-sematic
		 * behavior, in protecting the mapToList (and in this case the contained
		 * Lists) from being modified by any means other than a direct method
		 * call on this HashMapToList.
		 */
		return list == null ? null : new ArrayList<V>(list);
	}

	/**
	 * Removes the given value from the list for the given key. Returns true if
	 * the value was successfully removed from the list for the given key.
	 * Returns false if there is not a list for the given key object or if the
	 * list for the given key object did not contain the given value object.
	 *
	 * @param key
	 *            The key indicating which List the given object should be
	 *            removed from
	 * @param value
	 *            The value to be removed from the List for the given key
	 * @return true if the value was successfully removed from the list for the
	 *         given key; false otherwise
	 */
	public boolean removeFromListFor(K key, V value)
	{
		/*
		 * Note there is no requirement that a Key is added before this method
		 * is called
		 */
		if (!containsListFor(key))
		{
			return false;
		}
		return mapToList.get(key).remove(value);
	}

	/**
	 * Removes the List for the given key. Note there is no requirement that the
	 * list for the given key be empty before this method is called.
	 * 
	 * Ownership of the returned List is transferred to the object calling this
	 * method.
	 * 
	 * @param key
	 *            The key indicating which List the given object should be
	 *            removed from
	 * @return The List which this MapToList previous mapped the given key
	 */
	public List<V> removeListFor(K key)
	{
		/*
		 * No need to 'protect' this list (can directly return it) because the
		 * key and List will no longer be in this HashMapToList.
		 */
		return mapToList.remove(key);
	}

	/**
	 * Returns true if this MapToList contains no Lists.
	 *
	 * NOTE: This method checks whether this MapToList contains any Lists for
	 * any key. It DOES NOT test whether all Lists defined for all keys are
	 * empty. Therefore, it is possible that this MapToList contains one or more
	 * keys, and all of the lists associated with those keys are empty, yet this
	 * method will return false.
	 *
	 * @return true if this MapToList contains no Lists; false otherwise
	 */
	public boolean isEmpty()
	{
		return mapToList.isEmpty();
	}

	/**
	 * Returns the number of lists contained by this MapToList.
	 *
	 * NOTE: This method counts the number of Lists this MapToList contains. It
	 * DOES NOT determine whether all Lists defined for all keys are empty.
	 * Therefore, it is possible that this MapToList contains one or more keys,
	 * and all of the lists associated with those keys are empty, yet this
	 * method will return a non-zero value.
	 *
	 * @return The number of lists contained by this MapToList.
	 */
	public int size()
	{
		return mapToList.size();
	}

	/**
	 * Returns a Set indicating the Keys of this MapToList. Ownership of the Set
	 * is transferred to the calling Object, no association is kept between the
	 * Set and this MapToList. (Thus, removal of a key from the returned Set
	 * will not remove that key from this MapToList)
	 *
	 * NOTE: This method returns all of the keys this MapToList contains. It
	 * DOES NOT determine whether the Lists defined for the keys are empty.
	 * Therefore, it is possible that this MapToList contains one or more keys,
	 * and all of the lists associated with those keys are empty, yet this
	 * method will return a non-zero length Set.
	 *
	 * @return a Set containing the keys in this MapToList
	 */
	public Set<K> getKeySet()
	{
		/*
		 * Need to 'clone' the Set, since Map returns a set that is still
		 * associated with the Map. This is done to ensure value-sematic
		 * behavior, in protecting the mapToList from being modified by any
		 * means other than a direct method call on this HashMapToList.
		 */
		return new HashSet<K>(mapToList.keySet());
	}

	/**
	 * Returns the Object at the given position within the list for the given
	 * key. If a list for the given key is not present, null will be returned.
	 * 
	 * @param key
	 *            The key indicating which List the given object should be
	 *            returned from
	 * @param i
	 *            The location of the Object to be returned within the list
	 *            defined by the given key.
	 * @return The Object at the given position within the list for the given
	 *         key.
	 */
	public V getElementInList(K key, int i)
	{
		List<V> list = mapToList.get(key);
		return list == null ? null : list.get(i);
	}

	/**
	 * Clears this HashMapToList (removes all keys/list combiantions)
	 */
	public void clear()
	{
		mapToList.clear();
	}
}
