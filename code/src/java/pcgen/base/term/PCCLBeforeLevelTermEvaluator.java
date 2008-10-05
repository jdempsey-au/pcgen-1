/**
 * pcgen.base.term.PCQualifiedCLBeforeLevelTermEvaluator.java
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
 * Created 04-Aug-2008 17:02:58
 *
 * Current Ver: $Revision:$
 * Last Editor: $Author:$
 * Last Edited: $Date:$
 *
 */

package pcgen.base.term;

import pcgen.core.PlayerCharacter;
import pcgen.core.PCClass;

public class PCCLBeforeLevelTermEvaluator
		extends BasePCTermEvaluator implements TermEvaluator
{

	private final String source;
	private final int level;
	
	public PCCLBeforeLevelTermEvaluator(
			String originalText, String source, int level)
	{
		this.originalText = originalText;
		this.source       = source;
		this.level        = level;
	}

	public Float resolve(PlayerCharacter pc) {

		final PCClass aClass = pc.getClassKeyed(source);

		if (aClass != null)
		{
			if (level > 0)
			{
				return (float) pc.getLevelBefore(aClass.getKeyName(), level);
			}

			return (float) aClass.getLevel();
		}
		
		return 0f;
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
