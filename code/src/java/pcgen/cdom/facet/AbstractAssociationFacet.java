/*
 * Copyright (c) Thomas Parker, 2012.
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
package pcgen.cdom.facet;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import pcgen.base.util.ListSet;
import pcgen.cdom.enumeration.CharID;

/**
 * An AbstractAssociationFacet is a DataFacet that contains information about
 * associations for Objects that are contained in a PlayerCharacter.
 * 
 * This is used when each source may only have one association (such as
 * associating hit points to a class level), although the target can be a class
 * that can store multiple pieces of information in some implementations.
 * 
 * If the source object (e.g. the class level) is re-added with a second
 * association, this will overwrite the original association.
 * 
 * null is NOT a valid source.
 * 
 * null is NOT a valid base object or association.
 * 
 * @author Thomas Parker (thpr [at] yahoo.com)
 */
public abstract class AbstractAssociationFacet<S, A> extends AbstractStorageFacet
{

	/**
	 * Gets the association for the Player Character (identified by the given
	 * CharID) and the given source object.
	 * 
	 * @param id
	 *            The CharID identifying the Player Character for which the
	 *            association get is being performed.
	 * @param obj
	 *            The source object for which the association get is being
	 *            performed.
	 * @return The association for the Player Character (identified by the given
	 *         CharID) and the given source object
	 */
	public A get(CharID id, S obj)
	{
		if (obj == null)
		{
			throw new IllegalArgumentException(
				"Object for getting association may not be null");
		}
		Map<S, A> map = getCachedMap(id);
		if (map != null)
		{
			return map.get(obj);
		}
		return null;
	}

	/**
	 * Set the given association for the given object in this
	 * AbstractAssociationFacet for the Player Character represented by the
	 * given CharID
	 * 
	 * @param id
	 *            The CharID representing the Player Character for which the
	 *            given association should be made
	 * @param obj
	 *            The object for which the association will be set
	 * @param association
	 *            The association for the given object
	 */
	public void set(CharID id, S obj, A association)
	{
		if (obj == null)
		{
			throw new IllegalArgumentException("Object to add may not be null");
		}
		if (association == null)
		{
			throw new IllegalArgumentException("Association may not be null");
		}
		getConstructingCachedMap(id).put(obj, association);
	}

	/**
	 * Removes the association for the given source object in this
	 * AbstractAssociationFacet for the Player Character represented by the
	 * given CharID.
	 * 
	 * @param id
	 *            The CharID representing the Player Character from which the
	 *            given item association should be removed
	 * @param obj
	 *            The object for which the association should be removed
	 */
	public void remove(CharID id, S obj)
	{
		Map<S, A> map = getCachedMap(id);
		if (map != null)
		{
			map.remove(obj);
		}
	}

	/**
	 * Removes all objects (and all associations for those objects) from the
	 * list of objects stored in this AbstractAssociationFacet for the Player
	 * Character represented by the given CharID
	 * 
	 * This method is value-semantic in that ownership of the returned Map is
	 * transferred to the class calling this method. Since this is a remove all
	 * function, modification of the returned Map will not modify this
	 * AbstractAssociationFacet and modification of this
	 * AbstractAssociationFacet will not modify the returned Map. Modifications
	 * to the returned List will also not modify any future or previous objects
	 * returned by this (or other) methods on AbstractAssociationFacet. If you
	 * wish to modify the information stored in this AbstractAssociationFacet,
	 * you must use the add*() and remove*() methods of
	 * AbstractAssociationFacet.
	 * 
	 * @param id
	 *            The CharID representing the Player Character from which all
	 *            items should be removed
	 * @return A non-null Map of objects to their associations that were removed
	 *         from this AbstractAssociationFacet for the Player Character
	 *         represented by the given CharID
	 */
	public Map<S, A> removeAll(CharID id)
	{
		Map<S, A> componentMap = getCachedMap(id);
		if (componentMap == null)
		{
			return Collections.emptyMap();
		}
		removeCache(id, getClass());
		return componentMap;
	}

	/**
	 * Returns a non-null copy of the Set of objects in this
	 * AbstractAssociationFacet for the Player Character represented by the
	 * given CharID. This method returns an empty set if no objects are in this
	 * AbstractAssociationFacet for the Player Character identified by the given
	 * CharID.
	 * 
	 * This method is value-semantic in that ownership of the returned List is
	 * transferred to the class calling this method. Modification of the
	 * returned List will not modify this AbstractAssociationFacet and
	 * modification of this AbstractAssociationFacet will not modify the
	 * returned List. Modifications to the returned List will also not modify
	 * any future or previous objects returned by this (or other) methods on
	 * AbstractAssociationFacet. If you wish to modify the information stored in
	 * this AbstractAssociationFacet, you must use the add*() and remove*()
	 * methods of AbstractAssociationFacet.
	 * 
	 * @param id
	 *            The CharID representing the Player Character for which the
	 *            items in this AbstractAssociationFacet should be returned.
	 * @return A non-null copy of the Set of objects in this
	 *         AbstractAssociationFacet for the Player Character represented by
	 *         the given CharID
	 */
	public Set<S> getSet(CharID id)
	{
		Map<S, A> componentMap = getCachedMap(id);
		if (componentMap == null)
		{
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(new ListSet<S>(componentMap.keySet()));
	}

	/**
	 * Returns the count of items (objects -> association entries) in this
	 * AbstractAssociationFacet for the Player Character represented by the
	 * given CharID
	 * 
	 * @param id
	 *            The CharID representing the Player Character for which the
	 *            count of items should be returned
	 * @return The count of items (objects and thus also associations) in this
	 *         AbstractAssociationFacet for the Player Character represented by
	 *         the given CharID
	 */
	public int getCount(CharID id)
	{
		Map<S, A> componentMap = getCachedMap(id);
		if (componentMap == null)
		{
			return 0;
		}
		return componentMap.size();
	}

	/**
	 * Returns true if this AbstractAssociationFacet does not contain any items
	 * for the Player Character represented by the given CharID
	 * 
	 * @param id
	 *            The CharId representing the PlayerCharacter to test if any
	 *            items are contained by this AbstractsSourcedListFacet
	 * @return true if this AbstractAssociationFacet does not contain any items
	 *         for the Player Character represented by the given CharID; false
	 *         otherwise (if it does contain items for the Player Character)
	 */
	public boolean isEmpty(CharID id)
	{
		Map<S, A> componentMap = getCachedMap(id);
		return componentMap == null || componentMap.isEmpty();
	}

	/**
	 * Returns true if this AbstractAssociationFacet contains the given source
	 * in the list of items for the Player Character represented by the given
	 * CharID.
	 * 
	 * @param id
	 *            The CharID representing the Player Character used for testing
	 * @param obj
	 *            The source object to test if this AbstractAssociationFacet
	 *            contains an association for that item for the Player Character
	 *            represented by the given CharID
	 * @return true if this AbstractAssociationFacet contains an association for
	 *         the given source for the Player Character represented by the
	 *         given CharID; false otherwise
	 */
	public boolean contains(CharID id, S obj)
	{
		Map<S, A> componentMap = getCachedMap(id);
		return componentMap != null && componentMap.containsKey(obj);
	}

	/**
	 * Returns the type-safe Map for this AbstractAssociationFacet and the given
	 * CharID. May return null if no information has been set in this
	 * AbstractAssociationFacet for the given CharID.
	 * 
	 * Note that this method SHOULD NOT be public. The Map is owned by
	 * AbstractAssociationFacet, and since it can be modified, a reference to
	 * that object should not be exposed to any object other than
	 * AbstractAssociationFacet.
	 * 
	 * @param id
	 *            The CharID for which the Map should be returned
	 * @return The Map for the Player Character represented by the given CharID;
	 *         null if no information has been set in this
	 *         AbstractAssociationFacet for the Player Character.
	 */
	protected Map<S, A> getCachedMap(CharID id)
	{
		return (Map<S, A>) getCache(id, getClass());
	}

	/**
	 * Returns a type-safe Map for this AbstractAssociationFacet and the given
	 * CharID. Will return a new, empty Map if no information has been set in
	 * this AbstractAssociationFacet for the given CharID. Will not return null.
	 * 
	 * Note that this method SHOULD NOT be public. The Map object is owned by
	 * AbstractAssociationFacet, and since it can be modified, a reference to
	 * that object should not be exposed to any object other than
	 * AbstractAssociationFacet.
	 * 
	 * @param id
	 *            The CharID for which the Map should be returned
	 * @return The Map for the Player Character represented by the given CharID.
	 */
	private Map<S, A> getConstructingCachedMap(CharID id)
	{
		Map<S, A> componentMap = getCachedMap(id);
		if (componentMap == null)
		{
			componentMap = getComponentMap();
			setCache(id, getClass(), componentMap);
		}
		return componentMap;
	}

	/**
	 * Returns a new (empty) Map for this AbstractAssociationFacet. Can be
	 * overridden by classes that extend AbstractAssociationFacet if a Map other
	 * than an IdentityHashMap is desired for storing the information in the
	 * AbstractAssociationFacet.
	 * 
	 * Note that this method SHOULD NOT be public. The Map object is owned by
	 * AbstractAssociationFacet, and since it can be modified, a reference to
	 * that object should not be exposed to any object other than
	 * AbstractAssociationFacet.
	 * 
	 * Note that this method should always be the only method used to construct
	 * a Map for this AbstractAssociationFacet. It is actually preferred to use
	 * getConstructingCacheMap(CharID) in order to implicitly call this method.
	 * 
	 * @return A new (empty) Map for use in this AbstractAssociationFacet.
	 */
	protected Map<S, A> getComponentMap()
	{
		return new IdentityHashMap<S, A>();
	}

	/**
	 * Copies the contents of the AbstractAssociationFacet from one Player
	 * Character to another Player Character, based on the given CharIDs
	 * representing those Player Characters.
	 * 
	 * This is a method in AbstractAssociationFacet in order to avoid exposing
	 * the mutable Map object to other classes. This should not be inlined, as
	 * the Map is internal information to AbstractAssociationFacet and should
	 * not be exposed to other classes.
	 * 
	 * Note also the copy is a one-time event and no references are maintained
	 * between the Player Characters represented by the given CharIDs (meaning
	 * once this copy takes place, any change to the AbstractAssociationFacet of
	 * one Player Character will only impact the Player Character where the
	 * AbstractAssociationFacet was changed).
	 * 
	 * @param source
	 *            The CharID representing the Player Character from which the
	 *            information should be copied
	 * @param destination
	 *            The CharID representing the Player Character to which the
	 *            information should be copied
	 */
	@Override
	public void copyContents(CharID source, CharID destination)
	{
		Map<S, A> sourceMap = getCachedMap(source);
		if (sourceMap != null)
		{
			getConstructingCachedMap(destination).putAll(sourceMap);
		}
	}
}
