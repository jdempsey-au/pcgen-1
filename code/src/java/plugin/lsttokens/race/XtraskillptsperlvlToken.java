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
package plugin.lsttokens.race;

import pcgen.cdom.enumeration.IntegerKey;
import pcgen.core.Race;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.CDOMPrimaryParserToken;
import pcgen.rules.persistence.token.ErrorParsingWrapper;
import pcgen.rules.persistence.token.ParseResult;

/**
 * Class deals with XTRASKILLPTSPERLVL Token
 */
public class XtraskillptsperlvlToken extends ErrorParsingWrapper<Race> implements CDOMPrimaryParserToken<Race>
{

	/**
	 * Get token name
	 * 
	 * @return token name
	 */
	public String getTokenName()
	{
		return "XTRASKILLPTSPERLVL";
	}

	public ParseResult parseToken(LoadContext context, Race race, String value)
	{
		try
		{
			Integer sp = Integer.valueOf(value);
			if (sp.intValue() <= 0)
			{
				return new ParseResult.Fail(getTokenName() + " must be an integer > 0");
			}

			context.getObjectContext().put(race,
					IntegerKey.SKILL_POINTS_PER_LEVEL, sp);
			return ParseResult.SUCCESS;
		}
		catch (NumberFormatException nfe)
		{
			return new ParseResult.Fail(getTokenName()
					+ " expected an integer.  Tag must be of the form: "
					+ getTokenName() + ":<int>");
		}
	}

	public String[] unparse(LoadContext context, Race race)
	{
		Integer sp = context.getObjectContext().getInteger(race,
				IntegerKey.SKILL_POINTS_PER_LEVEL);
		if (sp == null)
		{
			return null;
		}
		if (sp.intValue() <= 0)
		{
			context.addWriteMessage(getTokenName() + " must be an integer > 0");
			return null;
		}
		return new String[] { sp.toString() };
	}

	public Class<Race> getTokenClass()
	{
		return Race.class;
	}
}
