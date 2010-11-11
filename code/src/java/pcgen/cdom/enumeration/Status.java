/*
 * Copyright 2007 (C) Tom Parker <thpr@users.sourceforge.net>
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
package pcgen.cdom.enumeration;

import pcgen.core.SettingsHandler;

/**
 * Represents the Campaign Statuses available in PCGen.
 * 
 * It is designed to hold Statuses in a type-safe fashion, so that they can be
 * quickly compared and use less memory when identical Statuses exist in two
 * CDOMObjects.
 */
public enum Status
{
	Release {
		@Override
		public String toString()
		{
			return "Release";
		}
		@Override
		public int getColor()
		{
			return SettingsHandler.getSourceStatusReleaseColor();
		}
	},

	Alpha {
		@Override
		public String toString()
		{
			return "Alpha";
		}
		@Override
		public int getColor()
		{
			return SettingsHandler.getSourceStatusAlphaColor();
		}
	},

	Beta {
		@Override
		public String toString()
		{
			return "Beta";
		}
		@Override
		public int getColor()
		{
			return SettingsHandler.getSourceStatusBetaColor();
		}
	};

	public static Status getDefaultValue()
	{
		return Release;
	}

	public abstract int getColor();

}
