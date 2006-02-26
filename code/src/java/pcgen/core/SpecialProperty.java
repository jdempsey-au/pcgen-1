/*
 * SpecialProperty.java
 * Copyright 2004 (C) Devon Jones
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
 * Created on April 21, 2001, 2:15 PM
 */
package pcgen.core;

import pcgen.core.prereq.Prerequisite;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.lst.prereq.PreParserFactory;
import pcgen.util.Logging;

import java.util.StringTokenizer;

/**
 * <code>SpecialProperty</code>.
 *
 * @author Devon Jones
 * @version $Revision: 1.6 $
 */
public final class SpecialProperty extends TextProperty
{
	public SpecialProperty()
	{
		super();
	}

	public SpecialProperty(final String name)
	{
		super(name);
	}

	public SpecialProperty(final String name, final String propDesc)
	{
		super(name, propDesc);
	}

	//DJ: This will be the same everywhere this gets used....and currently that is spread across the code.
	//It really shouldn't be in the core layer, but it's this, or have the same code in 10 places.....
	//TODO: get this into the persistance layer
	public static SpecialProperty createFromLst(final String input)
	{
		final StringTokenizer tok = new StringTokenizer(input, "|", true);
		final SpecialProperty sp = new SpecialProperty();

		if (!tok.hasMoreTokens())
		{
			return sp;
		}

		String spName = tok.nextToken();

		while (tok.hasMoreTokens())
		{
			final String cString = tok.nextToken();

			// Check to see if it's a PRExxx: tag
			if (cString.startsWith("PRE") && (cString.indexOf(":") > 0))
			{
				try
				{
					final PreParserFactory factory = PreParserFactory.getInstance();
					final Prerequisite prereq = factory.parse(cString);
					sp.addPreReq(prereq);
				}
				catch (PersistenceLayerException ple)
				{
					Logging.errorPrint(ple.getMessage(), ple);
				}
			}
			else
			{
				spName += cString;
			}

			if (".CLEAR".equals(cString))
			{
				spName = "";
			}
		}

		sp.setName(spName);
		return sp;
	}
}
