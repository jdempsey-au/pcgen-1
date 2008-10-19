/*
 * Copyright (c) 2008 Tom Parker <thpr@users.sourceforge.net>
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
package plugin.lsttokens.skill;

import pcgen.cdom.enumeration.ObjectKey;
import pcgen.core.Skill;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractToken;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.util.Logging;

/**
 * Class deals with EXCLUSIVE Token
 */
public class ExclusiveToken extends AbstractToken implements
		CDOMPrimaryToken<Skill>
{

	@Override
	public String getTokenName()
	{
		return "EXCLUSIVE";
	}

	public boolean parse(LoadContext context, Skill skill, String value)
	{
		if (isEmpty(value))
		{
			return false;
		}
		boolean set;
		char firstChar = value.charAt(0);
		if (firstChar == 'y' || firstChar == 'Y')
		{
			if (value.length() > 1 && !value.equalsIgnoreCase("YES"))
			{
				Logging.errorPrint("You should use 'YES' or 'NO' as the "
						+ getTokenName());
				return false;
			}
			set = true;
		}
		else
		{
			if (firstChar != 'N' && firstChar != 'n'
					&& !value.equalsIgnoreCase("NO"))
			{
				Logging.errorPrint("You should use 'YES' or 'NO' as the "
						+ getTokenName());
				return false;
			}
			set = false;
		}
		context.getObjectContext().put(skill, ObjectKey.EXCLUSIVE, set);
		return true;
	}

	public String[] unparse(LoadContext context, Skill skill)
	{
		Boolean exclusive = context.getObjectContext().getObject(skill,
				ObjectKey.EXCLUSIVE);
		if (exclusive == null)
		{
			return null;
		}
		return new String[] { exclusive.booleanValue() ? "YES" : "NO" };
	}

	public Class<Skill> getTokenClass()
	{
		return Skill.class;
	}
}
