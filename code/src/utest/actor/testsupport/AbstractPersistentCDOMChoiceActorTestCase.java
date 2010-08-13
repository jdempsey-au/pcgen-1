/*
 * Copyright (c) 2010 Tom Parker <thpr@users.sourceforge.net>
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
package actor.testsupport;

import pcgen.cdom.base.CDOMObject;

public abstract class AbstractPersistentCDOMChoiceActorTestCase<T extends CDOMObject>
		extends AbstractPersistentChoiceActorTestCase<T>
{
	protected T getObject()
	{
		T item = context.ref.constructCDOMObject(getCDOMClass(), "ItemName");
		//In case
		context.ref.registerAbbreviation(item, "ItemName");
		return item;
	}

	public abstract Class<T> getCDOMClass();
}
