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
package plugin.lsttokens.template;

import java.util.Collection;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import pcgen.cdom.base.Constants;
import pcgen.cdom.enumeration.IntegerKey;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.PCTemplate;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.context.Changes;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractTokenWithSeparator;
import pcgen.rules.persistence.token.CDOMPrimaryParserToken;
import pcgen.rules.persistence.token.ComplexParseResult;
import pcgen.rules.persistence.token.ParseResult;

/**
 * Class deals with LEVEL Token
 * 
 * Last Editor: $Author$ Last Edited: $Date: 2008-06-11 19:34:55 -0400
 * (Wed, 11 Jun 2008) $
 * 
 * @version $Revision$
 */
public class LevelToken extends AbstractTokenWithSeparator<PCTemplate> implements
		CDOMPrimaryParserToken<PCTemplate>
{
	@Override
	public String getTokenName()
	{
		return "LEVEL";
	}

	@Override
	public ParseResult parseToken(LoadContext context, PCTemplate template, String value)
	{
		if (".CLEAR".equals(value))
		{
			context.getObjectContext().removeList(template,
				ListKey.LEVEL_TEMPLATES);
			return ParseResult.SUCCESS;
		}
		return super.parseToken(context, template, value);
	}

	@Override
	protected char separator()
	{
		return ':';
	}

	@Override
	protected ParseResult parseTokenWithSeparator(LoadContext context,
		PCTemplate template, String value)
	{
		StringTokenizer tok = new StringTokenizer(value, Constants.COLON);

		String levelStr = tok.nextToken();
		int lvl;
		try
		{
			/*
			 * Note this test of integer (even if it doesn't get used outside
			 * this try) is necessary for catching errors.
			 */
			lvl = Integer.parseInt(levelStr);
			if (lvl <= 0)
			{
				ComplexParseResult cpr = new ComplexParseResult();
				cpr.addErrorMessage("Malformed " + getTokenName()
						+ " Token (Level was <= 0): " + lvl);
				cpr.addErrorMessage("  Line was: " + value);
				return cpr;
			}
		}
		catch (NumberFormatException ex)
		{
			return new ParseResult.Fail("Misunderstood Level value: " + levelStr
					+ " in " + getTokenName());
		}

		if (!tok.hasMoreTokens())
		{
			return new ParseResult.Fail("Invalid " + getTokenName()
					+ ": requires 3 colon separated elements (has one): "
					+ value);
		}
		String typeStr = tok.nextToken();
		if (!tok.hasMoreTokens())
		{
			return new ParseResult.Fail("Invalid " + getTokenName()
					+ ": requires 3 colon separated elements (has two): "
					+ value);
		}
		String argument = tok.nextToken();
		PCTemplate derivative = new PCTemplate();
		derivative.put(IntegerKey.LEVEL, lvl);
		context.getObjectContext().addToList(template, ListKey.LEVEL_TEMPLATES,
				derivative);
		try
		{
			if (context.processToken(derivative, typeStr, argument))
			{
				return ParseResult.SUCCESS;
			}
		}
		catch (PersistenceLayerException e)
		{
			return new ParseResult.Fail(e.getMessage());
		}
		return ParseResult.INTERNAL_ERROR;
	}

	public String[] unparse(LoadContext context, PCTemplate pct)
	{
		Changes<PCTemplate> changes = context.getObjectContext()
				.getListChanges(pct, ListKey.LEVEL_TEMPLATES);
		Collection<PCTemplate> added = changes.getAdded();
		if (added == null || added.isEmpty())
		{
			return null;
		}
		Set<String> set = new TreeSet<String>();
		for (PCTemplate pctChild : added)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(pctChild.get(IntegerKey.LEVEL)).append(':');
			Collection<String> unparse = context.unparse(pctChild);
			if (unparse != null)
			{
				int masterLength = sb.length();
				for (String str : unparse)
				{
					sb.setLength(masterLength);
					set.add(sb.append(str).toString());
				}
			}
		}
		if (set.isEmpty())
		{
			return null;
		}
		return set.toArray(new String[set.size()]);
	}

	public Class<PCTemplate> getTokenClass()
	{
		return PCTemplate.class;
	}

}
