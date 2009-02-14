/**
 * pcgen.core.term.PCSkillTypeTermEvaluator.java
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
 * Created 07-Aug-2008 20:27:13
 *
 * Current Ver: $Revision:$
 * Last Editor: $Author:$
 * Last Edited: $Date:$
 *
 */

package pcgen.core.term;

import java.util.List;
import java.util.ArrayList;

import pcgen.core.PlayerCharacter;
import pcgen.core.Skill;
import pcgen.cdom.base.CDOMObject;

public class PCSkillTypeTermEvaluator
		extends BasePCTermEvaluator implements TermEvaluator
{

	private final String type;

	public PCSkillTypeTermEvaluator(String originalText, String type)
	{
		this.originalText = originalText;
		this.type         = type;
	}
	
	@Override
	public Float resolve(PlayerCharacter pc) {

		//noinspection CollectionDeclaredAsConcreteClass
		final ArrayList<Skill> skills = new ArrayList<Skill>(pc.getAllSkillList(true)); 
		final List<Skill> skillList = pc.getSkillListInOutputOrder( skills);

		Float typeCount = 0f;

		for ( CDOMObject skill : skillList )
		{
			if (skill.isType(type))
			{
				typeCount++;
			}
		}

		return typeCount;
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
