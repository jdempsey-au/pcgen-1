/*
 * Copyright (c) Thomas Parker, 2009.
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

import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.RaceType;
import pcgen.core.PCTemplate;
import pcgen.core.Race;
import pcgen.core.character.CompanionMod;

public class RaceTypeFacet
{

	private TemplateFacet templateFacet = FacetLibrary
			.getFacet(TemplateFacet.class);
	private RaceFacet raceFacet = FacetLibrary.getFacet(RaceFacet.class);
	private CompanionModFacet companionModFacet = FacetLibrary
			.getFacet(CompanionModFacet.class);

	public RaceType getRaceType(CharID id)
	{
		RaceType raceType = null;
		Race race = raceFacet.get(id);
		if (race != null)
		{
			RaceType rt = race.get(ObjectKey.RACETYPE);
			if (rt != null)
			{
				raceType = rt;
			}
		}
		for (CompanionMod cm : companionModFacet.getSet(id))
		{
			RaceType rt = cm.get(ObjectKey.RACETYPE);
			if (rt != null)
			{
				raceType = rt;
			}
		}
		for (PCTemplate t : templateFacet.getSet(id))
		{
			RaceType rt = t.get(ObjectKey.RACETYPE);
			if (rt != null)
			{
				raceType = rt;
			}
		}
		return raceType;
	}

}
