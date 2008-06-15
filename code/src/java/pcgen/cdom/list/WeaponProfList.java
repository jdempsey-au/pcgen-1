/*
 * Copyright 2007, 2008 (C) Tom Parker <thpr@users.sourceforge.net>
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
package pcgen.cdom.list;

import pcgen.cdom.base.CDOMListObject;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.reference.CDOMDirectSingleRef;
import pcgen.core.WeaponProf;

public class WeaponProfList extends CDOMListObject<WeaponProf>
{

	public static final CDOMReference<WeaponProfList> STARTING;
	
	static
	{
		WeaponProfList wpl = new WeaponProfList();
		wpl.setName("*Starting");
		STARTING = new CDOMDirectSingleRef<WeaponProfList>(wpl);
	}

	public Class<WeaponProf> getListClass()
	{
		return WeaponProf.class;
	}

	@Override
	public boolean isType(String str)
	{
		return false;
	}

	// No additional Functionality :)

}
