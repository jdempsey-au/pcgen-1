/**
 * pcgen.core.term.PCMaxLevelTermEvaluator.java
 * Copyright (c) 2010 Thomas Parker <thpr@users.sourceforge.net>.
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
 */

package pcgen.core.term;

import pcgen.core.PCClass;
import pcgen.core.PlayerCharacter;

public class PCMaxLevelTermEvaluator extends BasePCTermEvaluator implements
		TermEvaluator
{
	private final String classKey;

	public PCMaxLevelTermEvaluator(String originalText, String sourceInfo)
	{
		this.originalText = originalText;
		this.classKey = sourceInfo;
	}

	@Override
	public Float resolve(PlayerCharacter pc)
	{
		if (classKey.length() == 0)
		{
			return 0.0f;
		}
		PCClass aClass = pc.getClassKeyed(classKey);
		int level =
				pc.getSpellSupport(aClass).getMaxSpellLevelForClassLevel(
					pc.getLevel(aClass));

		return Integer.valueOf(level).floatValue();
	}

	public boolean isSourceDependant()
	{
		return true;
	}

	public boolean isStatic()
	{
		return false;
	}
}
