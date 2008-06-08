/*
 * RaceGeneratorOption.java
 * Copyright 2006 (C) Aaron Divinsky <boomer70@yahoo.com>
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
 * Current Ver: $Revision$
 * Last Editor: $Author: $
 * Last Edited: $Date$
 */
package pcgen.core.npcgen;

import pcgen.base.util.WeightedList;
import pcgen.cdom.base.Constants;
import pcgen.core.Globals;
import pcgen.core.Race;
import pcgen.util.Logging;

/**
 * This class represents a particular race generator option.
 * 
 * @author boomer70 <boomer70@yahoo.com>
 * @since 5.11.1
 */
public class RaceGeneratorOption extends GeneratorOption
{
	private WeightedList<Race> theChoices = null;
	
	/**
	 * @see pcgen.core.npcgen.GeneratorOption#addChoice(int, java.lang.String)
	 */
	@Override
	public void addChoice(final int aWeight, final String aValue)
	{
		if ( theChoices == null )
		{
			theChoices = new WeightedList<Race>();
		}
		
		if ( aValue.equals("*") ) //$NON-NLS-1$
		{
			for ( final Race race : Globals.getContext().ref.getConstructedCDOMObjects(Race.class) )
			{
				if ( ! theChoices.contains(race) )
				{
					theChoices.add(aWeight, race);
				}
			}
			return;
		}
		if ( aValue.startsWith("TYPE") ) //$NON-NLS-1$
		{
			for ( final Race race : Globals.getContext().ref.getConstructedCDOMObjects(Race.class) )
			{
				if (race.isType(aValue.substring(5)) && !race.getDisplayName().equals(Constants.s_NONESELECTED))
				{
					theChoices.add(aWeight, race);
				}
			}
			return;
		}
		final Race race = Globals.getContext().ref.silentlyGetConstructedCDOMObject(Race.class, aValue);
		if (race == null)
		{
			Logging.errorPrintLocalised("NPCGen.Options.RaceNotFound", aValue); //$NON-NLS-1$
		}
		else
		{
			theChoices.add(aWeight, race);
		}
	}

	/**
	 * getList
	 *
	 * @return List
	 */
	@Override
	public WeightedList<Race> getList()
	{
		return theChoices;
	}
}
