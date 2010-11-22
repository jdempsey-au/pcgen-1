/*
 * Copyright (c) 2007 Tom Parker <thpr@users.sourceforge.net>
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
package pcgen.cdom.reference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pcgen.cdom.base.Constants;
import pcgen.cdom.enumeration.GroupingState;

/**
 * A CDOMAllRef is a CDOMReference which is intended to contain all objects of
 * the Class this CDOMAllRef represents.
 * 
 * @param <T>
 *            The Class of the underlying objects contained by this reference
 */
public final class CDOMAllRef<T> extends CDOMGroupRef<T>
{
	/**
	 * The objects (presumably all of the objects) of the Class this CDOMAllRef
	 * represents
	 */
	private List<T> referencedList = null;

	/**
	 * Constructs a new CDOMAllRef for the given Class to be represented by this
	 * CDOMAllRef.
	 * 
	 * @param objClass
	 *            The Class of the underlying objects contained by this
	 *            reference.
	 */
	public CDOMAllRef(Class<T> objClass)
	{
		super(objClass, Constants.ALLREF_LST + ": " + objClass.getSimpleName());
	}

	/**
	 * Returns a representation of this CDOMAllRef, suitable for storing in an
	 * LST file.
	 * 
	 * Note that this will return the identifier of the "All" reference, not an
	 * expanded list of the contents of this CDOMAllRef.
	 * 
	 * @return A representation of this CDOMAllRef, suitable for storing in an
	 *         LST file.
	 * @see pcgen.cdom.base.CDOMReference#getLSTformat()
	 */
	@Override
	public String getLSTformat(boolean useAny)
	{
		return useAny ? Constants.LST_ANY : Constants.ALLREF_LST;
	}

	/**
	 * Returns true if the given Object is included in the Collection of Objects
	 * to which this CDOMAllRef refers.
	 * 
	 * Note that the behavior of this class is undefined if the CDOMAllRef has
	 * not yet been resolved.
	 * 
	 * @param item
	 *            The object to be tested to see if it is referred to by this
	 *            CDOMAllRef.
	 * @return true if the given Object is included in the Collection of Objects
	 *         to which this CDOMAllRef refers; false otherwise.
	 * @throws IllegalStateException
	 *             if this CDOMAllRef has not been resolved
	 */
	@Override
	public boolean contains(T item)
	{
		if (referencedList == null)
		{
			throw new IllegalStateException(
					"Cannot ask for contains: Reference has not been resolved");
		}
		return referencedList.contains(item);
	}

	/**
	 * Returns true if this CDOMAllRef is equal to the given Object. Equality is
	 * defined as being another CDOMAllRef object with equal Class represented
	 * by the reference. This is NOT a deep .equals, in that the actual contents
	 * of this CDOMReference are not tested.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof CDOMAllRef
				&& getReferenceClass().equals(
						((CDOMAllRef<?>) obj).getReferenceClass());
	}

	/**
	 * Returns the consistent-with-equals hashCode for this CDOMAllRef
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return getReferenceClass().hashCode();
	}

	/**
	 * Adds an object to be included in the Collection of objects to which this
	 * CDOMAllRef refers.
	 * 
	 * @param item
	 *            An object to be included in the Collection of objects to which
	 *            this CDOMAllRef refers.
	 * @throws IllegalArgumentException
	 *             if the given object for addition to this CDOMAllRef is not of
	 *             the class that this CDOMAllRef represents
	 * @throws NullPointerException
	 *             if the given object is null
	 */
	@Override
	public void addResolution(T item)
	{
		if (item.getClass().equals(getReferenceClass()))
		{
			if (referencedList == null)
			{
				referencedList = new ArrayList<T>();
			}
			referencedList.add(item);
		}
		else
		{
			throw new IllegalArgumentException("Cannot resolve a "
					+ getReferenceClass().getSimpleName() + " Reference to a "
					+ item.getClass().getSimpleName());
		}
	}

	/**
	 * Returns the count of the number of objects included in the Collection of
	 * Objects to which this CDOMAllRef refers.
	 * 
	 * Note that the behavior of this class is undefined if the CDOMAllRef has
	 * not yet been resolved.
	 * 
	 * @return The count of the number of objects included in the Collection of
	 *         Objects to which this CDOMAllRef refers.
	 */
	@Override
	public int getObjectCount()
	{
		return referencedList == null ? 0 : referencedList.size();
	}

	/**
	 * Returns a Collection containing the Objects to which this CDOMAllRef
	 * refers.
	 * 
	 * This method is reference-semantic, meaning that ownership of the
	 * Collection returned by this method is transferred to the calling object.
	 * Modification of the returned Collection should not result in modifying
	 * the CDOMAllRef, and modifying the CDOMAllRef after the Collection is
	 * returned should not modify the Collection.
	 * 
	 * Note that the behavior of this class is undefined if the CDOMAllRef has
	 * not yet been resolved. (It may return null or an empty Collection; that
	 * is implementation dependent)
	 * 
	 * @return A Collection containing the Objects to which this CDOMAllRef
	 *         refers.
	 */
	@Override
	public Collection<T> getContainedObjects()
	{
		return Collections.unmodifiableList(referencedList);
	}

	/**
	 * Returns the GroupingState for this CDOMAllRef. The GroupingState
	 * indicates how this CDOMAllRef can be combined with other
	 * PrimitiveChoiceFilters.
	 * 
	 * @return The GroupingState for this CDOMAllRef.
	 */
	public GroupingState getGroupingState()
	{
		return GroupingState.ALLOWS_NONE;
	}
}
