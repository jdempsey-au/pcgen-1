/**
 * pcgen.base.term.PCSkillTotalTermEvaluator.java
 * Copyright � 2008 Andrew Wilson <nuance@users.sourceforge.net>.
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
 * Created 09-Aug-2008 13:29:52
 *
 * Current Ver: $Revision:$
 * Last Editor: $Author:$
 * Last Edited: $Date:$
 *
 */

package pcgen.base.term;

import pcgen.core.PlayerCharacter;
import pcgen.core.Skill;
import pcgen.core.analysis.SkillModifier;
import pcgen.core.analysis.SkillRankControl;

public class PCSkillTotalTermEvaluator
		extends BasePCTermEvaluator implements TermEvaluator
{
	private final String total;

	public PCSkillTotalTermEvaluator(String originalText, String total)
	{
		this.originalText = originalText;
		this.total        = total;
	}

	public Float resolve(PlayerCharacter pc)
	{
		final Skill aSkill = pc.getSkillKeyed(total);

		Float total = SkillRankControl.getTotalRank(pc, aSkill);
		total += SkillModifier.modifier(aSkill, pc);

		return (aSkill == null) ? 0f : total;
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
