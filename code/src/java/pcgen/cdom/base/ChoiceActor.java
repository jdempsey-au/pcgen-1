/*
 * Copyright 2008 (C) Tom Parker <thpr@users.sourceforge.net>
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
package pcgen.cdom.base;

import java.util.List;

import pcgen.core.PlayerCharacter;

/**
 * A ChoiceActor is an object that can limit and apply choices of a particular
 * type of object to a PlayerCharacter. This is typically an object that will
 * act after a selection has been made by a user through a TransitionChoice
 * object.
 * 
 * @param <T>
 *            The type of object that this ChoiceActor can apply to a
 *            PlayerCharacter
 */
public interface ChoiceActor<T>
{
	/**
	 * Applies the given choice to the given PlayerCharacter.
	 * 
	 * @param owner
	 *            The owning object for this choice.
	 * @param choice
	 *            The choice being applied to the given PlayerCharacter
	 * @param pc
	 *            The PlayerCharacter to which the given choice should be
	 *            applied.
	 */
	public void applyChoice(CDOMObject owner, T choice, PlayerCharacter pc);

	/**
	 * Returns true if the given choice should be allowed for the
	 * PlayerCharacter under the provided stacking conditions.
	 * 
	 * @param choice
	 *            The choice being tested to see if it should be allowed for the
	 *            given PlayerCharacter
	 * @param pc
	 *            The PlayerCharacter to be used in determining if the given
	 *            choice is allowed.
	 * @param allowStack
	 *            True if the given choice should be allowed to stack (meaning
	 *            the PC can have more than one isntance of the choice); false
	 *            otherwise
	 * @return true if the given choice should be allowed for the
	 *         PlayerCharacter under the provided stacking conditions.
	 */
	public boolean allow(T choice, PlayerCharacter pc, boolean allowStack);
	
	/**
	 * Returns a list of the items *for this ChoiceActor* that have been
	 * previously selected. Note that this does not identify whether a PC
	 * has previously taken an item through another means (that is resolved
	 * by the allow method) This returns what has previously been selected
	 * and what should be placed in the 'selected' section of a chooser that
	 * is presented to the user.
	 * 
	 * @param owner
	 *            The owning object for this choice.
	 * @param pc
	 *            The PlayerCharacter for which the currently selected items
	 *            are being returned.
	 */
	public List<T> getCurrentlySelected(CDOMObject owner, PlayerCharacter pc);
}
