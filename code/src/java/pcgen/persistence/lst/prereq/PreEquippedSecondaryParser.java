/*
 * PreEquippedSecondary.java
 *
 * Copyright 2003 (C) Chris Ward <frugal@purplewombat.co.uk>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.       See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on 18-Dec-2003
 *
 * Current Ver: $Revision: 1.2 $
 *
 * Last Editor: $Author: jujutsunerd $
 *
 * Last Edited: $Date: 2003/12/29 01:39:32 $
 *
 */
package pcgen.persistence.lst.prereq;


/**
 * @author wardc
 *
 */
public class PreEquippedSecondaryParser extends AbstractPrerequisiteListParser implements PrerequisiteParserInterface
{
	public String[] kindsHandled()
	{
		return new String[]{ "EQUIPSECONDARY" };
	}
}
