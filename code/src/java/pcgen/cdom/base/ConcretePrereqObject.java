/*
 * PrereqObject.java
 * Copyright 2006 Aaron Divinsky <boomer70@yahoo.com>
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
 * Current Version: $Revision: 5686 $
 * Last Editor: $Author: $
 * Last Edited: $Date: 2008-03-23 18:14:22 -0400 (Sun, 23 Mar 2008) $
 *
 */
package pcgen.cdom.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pcgen.base.util.ListSet;
import pcgen.core.PlayerCharacter;
import pcgen.core.prereq.PrereqHandler;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteUtilities;

/**
 * A ConcretePrereqObject is an object that contains a list of Prerequisites.
 * This list of Prerequisites is designed to serve as a list of conditions that
 * must be met before the PrereqObject can be "used"
 * 
 * ConcretePrereqObject is intended to provide a quick foundation class that
 * implements PrereqObject.
 */
public class ConcretePrereqObject implements Cloneable, PrereqObject
{
	/** The list of prerequisites */
	private Set<Prerequisite> thePrereqs = null;

	/**
	 * Tests if the specified PlayerCharacter passes all the prerequisites.
	 * 
	 * @param aPC
	 *            The <tt>PlayerCharacter</tt> to test.
	 * 
	 * @return <tt>true</tt> if the PC passes all the prerequisites.
	 */
	public boolean qualifies(final PlayerCharacter aPC)
	{
		if (thePrereqs == null)
		{
			return true;
		}

		return PrereqHandler.passesAll(thePrereqs, aPC, null);
	}

	/**
	 * Returns a List of the Prerequisite objects contained in the PrereqObject.
	 * If the PrereqObject contains no Prerequisites, the return value may be
	 * null or an empty list, it is implementation-specific.
	 * 
	 * @return A List of Prerequesite objects contained in the PrereqObject.
	 */
	public List<Prerequisite> getPrerequisiteList()
	{
		if (thePrereqs == null)
		{
			return Collections.emptyList();
		}
		/*
		 * TODO This is an ugly facade, but required for easy compatibility with
		 * 5.14 - to be changed once 5.14 code is gone and this can be changed
		 * to return Collection or Set (or perhaps ListSet?)
		 */
		return Collections.unmodifiableList(new ArrayList<Prerequisite>(
				thePrereqs));
	}

	/**
	 * Add a <tt>Prerequesite</tt> to the prereq list with a level qualifier.
	 * 
	 * <p>
	 * If the Prerequisite kind is &quot;clear&quot; all the prerequisites will
	 * be cleared from the list.
	 * 
	 * @param preReq
	 *            The <tt>Prerequisite</tt> to add.
	 * @param levelQualifier
	 *            A level qualifier.
	 * 
	 * @see pcgen.core.prereq.Prerequisite#setLevelQualifier(int)
	 */
	public final void addPreReq(final Prerequisite preReq,
			final int levelQualifier)
	{
		if (preReq == null)
		{
			return;
		}
		if (Prerequisite.CLEAR_KIND.equals(preReq.getKind())) //$NON-NLS-1$
		{
			thePrereqs = null;
		}
		else
		{
			if (thePrereqs == null)
			{
				thePrereqs = new ListSet<Prerequisite>();
			}
			if (levelQualifier > 0)
			{
				preReq.setLevelQualifier(levelQualifier);
			}
			thePrereqs.add(preReq);
		}
	}

	/**
	 * Returns true if this object has any prerequisites of the kind that is
	 * passed in.
	 * 
	 * @param matchType
	 *            The kind of Prerequisite to test for.
	 * 
	 * @return <tt>true</tt> if this object has a prerequisite of the kind
	 *         that is passed in
	 * 
	 * @see pcgen.core.prereq.Prerequisite#getKind()
	 */
	public final boolean hasPreReqTypeOf(final String matchType)
	{
		if (!hasPrerequisites())
		{
			return false;
		}

		for (Prerequisite prereq : getPrerequisiteList())
		{
			if (PrerequisiteUtilities.hasPreReqKindOf(prereq, matchType))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Clones this ConcretePrereqObject. This is not a "deep" clone, in that the
	 * List of Prerequisites is cloned (to allow Prerequisites to be
	 * added/removed without altering the original or the clone), but the
	 * Prerequisites contained within the ConcretePrereqObject are not cloned.
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public ConcretePrereqObject clone() throws CloneNotSupportedException
	{
		final ConcretePrereqObject obj = (ConcretePrereqObject) super.clone();
		if (thePrereqs != null)
		{
			obj.thePrereqs = new ListSet<Prerequisite>();
			obj.thePrereqs.addAll(thePrereqs);
		}
		return obj;
	}

	/**
	 * Adds a Collection of Prerequisite objects to the ConcretePrereqObject.
	 * 
	 * @param prereqs
	 *            A Collection of Prerequisite objects to added to the
	 *            ConcretePrereqObject.
	 */
	public void addAllPrerequisites(final Collection<Prerequisite> prereqs)
	{
		if (prereqs == null || prereqs.isEmpty())
		{
			return;
		}
		if (thePrereqs == null)
		{
			thePrereqs = new ListSet<Prerequisite>(prereqs.size());
		}
		for (final Prerequisite pre : prereqs)
		{
			addPrerequisite(pre);
		}
	}

	/**
	 * Add a Prerequisite to the ConcretePrereqObject.
	 * 
	 * If the Prerequisite kind is CLEAR all the prerequisites will be cleared
	 * from the list.
	 * 
	 * @param prereq
	 *            The Prerequisite to add to the ConcretePrereqObject.
	 */
	public void addPrerequisite(Prerequisite preReq)
	{
		this.addPreReq(preReq, -1);
	}

	/**
	 * Remove All Prerequisites contained in the ConcretePrereqObject.
	 */
	public void clearPrerequisiteList()
	{
		thePrereqs = null;
	}

	/**
	 * Returns the number of Prerequisites contained in the
	 * ConcretePrereqObject.
	 * 
	 * @return the number of Prerequisites contained in the
	 *         ConcretePrereqObject.
	 */
	public int getPrerequisiteCount()
	{
		if (thePrereqs == null)
		{
			return 0;
		}
		return thePrereqs.size();
	}

	/**
	 * Returns true if this ConcretePrereqObject contains any Prerequisites;
	 * false otherwise.
	 * 
	 * @return true if this ConcretePrereqObject contains any Prerequisites;
	 *         false otherwise.
	 */
	public boolean hasPrerequisites()
	{
		return thePrereqs != null;
	}

	/**
	 * Returns true if the given PrereqObject contains Prerequisites that are
	 * equal to the Prerequisites contained within this ConcretePrereqObject. If
	 * the number of Prerequisites does not match, or if the Prerequisites are
	 * not equal, this will return false.
	 * 
	 * @param other
	 *            The PrereqObject for which the Prerequisites will be compared
	 *            to the Prerequisites in this ConcretePrereqObject.
	 * @return true if the given PrereqObject contains Prerequisites that are
	 *         equal to the Prerequisites contained within this
	 *         ConcretePrereqObject; false otherwise
	 */
	public boolean equalsPrereqObject(PrereqObject other)
	{
		if (this == other)
		{
			return true;
		}
		boolean otherHas = other.hasPrerequisites();
		if (!hasPrerequisites())
		{
			return !otherHas;
		}
		if (!otherHas)
		{
			return false;
		}
		List<Prerequisite> otherPRL = other.getPrerequisiteList();
		if (otherPRL.size() != thePrereqs.size())
		{
			return false;
		}
		ArrayList<Prerequisite> removed = new ArrayList<Prerequisite>(
				thePrereqs);
		removed.removeAll(otherPRL);
		return removed.isEmpty();
	}

}
