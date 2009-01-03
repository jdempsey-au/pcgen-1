/**
 * pcgen.core.term.PCCountAbilitiesNatureNormalTermEvaluator.java
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
 * Created 09-Aug-2008 16:02:49
 *
 * Current Ver: $Revision:$
 * Last Editor: $Author:$
 * Last Edited: $Date:$
 *
 */

package pcgen.core.term;

import java.util.List;

import pcgen.core.PlayerCharacter;
import pcgen.core.AbilityCategory;
import pcgen.core.Ability;

public class PCCountAbilitiesNatureNormalTermEvaluator 
		extends BasePCCountAbilitiesNatureTermEvaluator implements TermEvaluator
{
	public PCCountAbilitiesNatureNormalTermEvaluator(
			String originalText,
			AbilityCategory abCat,
			boolean visible,
			boolean hidden)
	{
		this.originalText = originalText;		
		this.abCat   = abCat;
		this.visible = visible;
		this.hidden  = hidden;
	}

	List<Ability> getAbilities(PlayerCharacter pc)
	{
		return pc.getRealAbilitiesListAnyCat(abCat);
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
