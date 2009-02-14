/**
 * pcgen.core.term.PCStatBaseTermEvaluator.java
 * Copyright (c) 2008 Andrew Wilson <nuance@users.sourceforge.net>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created 22-Sep-2008 01:08:08
 *
 * Current Ver: $Revision:$
 * Last Editor: $Author:$
 * Last Edited: $Date:$
 *
 */

package pcgen.core.term;

import pcgen.core.PlayerCharacter;

public class PCStatBaseTermEvaluator 
		extends BasePCTermEvaluator implements TermEvaluator
{
	private final String statAbbrev;
	
	public PCStatBaseTermEvaluator (String originalText, String statAbbrev)
	{
		this.originalText = originalText;
		this.statAbbrev   = statAbbrev;
	}

	@Override
	public Float resolve(PlayerCharacter pc)
	{
		return (float) pc.getStatList().getBaseStatFor(statAbbrev);
	}

	public boolean isSourceDependant()
	{
		return false;
	}

	public boolean isStatic()
	{
		return false;
	}
}
