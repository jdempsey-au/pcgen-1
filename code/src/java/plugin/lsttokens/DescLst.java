/*
 * Copyright 2008 (C) Thomas Parker <thpr@users.sourceforge.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package plugin.lsttokens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import pcgen.base.lang.StringUtil;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.Constants;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.Description;
import pcgen.core.prereq.Prerequisite;
import pcgen.io.EntityEncoder;
import pcgen.rules.context.Changes;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractToken;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.util.Logging;

/**
 * Handles DESC token processing
 * 
 * @author djones4
 */
public class DescLst extends AbstractToken implements
		CDOMPrimaryToken<CDOMObject>
{
	/**
	 * @see pcgen.persistence.lst.LstToken#getTokenName()
	 */
	@Override
	public String getTokenName()
	{
		return "DESC"; //$NON-NLS-1$
	}

	public boolean parse(LoadContext context, CDOMObject obj, String value)
	{
		if (isEmpty(value) || hasIllegalSeparator('|', value))
		{
			return false;
		}

		if (Constants.LST_DOT_CLEAR.equals(value))
		{
			context.obj.removeList(obj, ListKey.DESCRIPTION);
			return true;
		}
		if (value.startsWith(Constants.LST_DOT_CLEAR_DOT))
		{
			context.getObjectContext().removePatternFromList(obj,
				ListKey.DESCRIPTION, value.substring(7));
			return true;
		}

		Description d = parseDescription(value);
		if (d == null)
		{
			return false;
		}
		context.obj.addToList(obj, ListKey.DESCRIPTION, d);
		return true;
	}

	/**
	 * Parses the DESC tag into a Description object.
	 * 
	 * @param aDesc
	 *            The LST tag
	 * @return A <tt>Description</tt> object
	 */
	public Description parseDescription(final String aDesc)
	{
		StringTokenizer tok = new StringTokenizer(aDesc, Constants.PIPE);

		String descString = tok.nextToken();

		if (descString.startsWith("PRE") || descString.startsWith("!PRE"))
		{
			Logging.log(Logging.LST_ERROR, getTokenName() + " encountered only a PRExxx: "
				+ aDesc);
			return null;
		}
		String ds = EntityEncoder.decode(descString);
		if (!StringUtil.hasBalancedParens(ds))
		{
			Logging.log(Logging.LST_ERROR, getTokenName()
					+ " encountered imbalanced Parenthesis: " + aDesc);
			return null;
		}
		Description desc = new Description(ds);

		if (!tok.hasMoreTokens())
		{
			return desc;
		}

		String token = tok.nextToken();
		while (true)
		{
			if (Constants.LST_DOT_CLEAR.equals(token))
			{
				Logging.log(Logging.LST_ERROR, getTokenName()
					+ " tag confused by '.CLEAR' as a " + "middle token: "
					+ aDesc);
				return null;
			}
			else if (token.startsWith("PRE") || token.startsWith("!PRE"))
			{
				break;
			}
			else
			{
				desc.addVariable(token);
			}

			if (!tok.hasMoreTokens())
			{
				// No prereqs, so we're done
				return desc;
			}
			token = tok.nextToken();
		}

		while (true)
		{
			Prerequisite prereq = getPrerequisite(token);
			if (prereq == null)
			{
				Logging.log(Logging.LST_ERROR, "   (Did you put Abilities after the "
					+ "PRExxx tags in " + getTokenName() + ":?)");
				return null;
			}
			desc.addPrerequisite(prereq);
			if (!tok.hasMoreTokens())
			{
				break;
			}
			token = tok.nextToken();
		}
		return desc;
	}

	public String[] unparse(LoadContext context, CDOMObject obj)
	{
		Changes<Description> changes =
				context.obj.getListChanges(obj, ListKey.DESCRIPTION);
		if (changes == null || changes.isEmpty())
		{
			return null;
		}
		List<String> list = new ArrayList<String>();
		Collection<Description> removedItems = changes.getRemoved();
		if (changes.includesGlobalClear())
		{
			if (removedItems != null && !removedItems.isEmpty())
			{
				context.addWriteMessage("Non-sensical relationship in "
					+ getTokenName()
					+ ": global .CLEAR and local .CLEAR. performed");
				return null;
			}
			list.add(Constants.LST_DOT_CLEAR);
		}
		if (removedItems != null && !removedItems.isEmpty())
		{
			for (Description d : removedItems)
			{
				list.add(Constants.LST_DOT_CLEAR_DOT + d);
			}
		}
		/*
		 * TODO .CLEAR. is not properly round-robin capable
		 */
		Collection<Description> addedItems = changes.getAdded();
		if (addedItems != null)
		{
			for (Description d : addedItems)
			{
				list.add(d.getPCCText());
			}
		}
		if (list.isEmpty())
		{
			return null;
		}
		return list.toArray(new String[list.size()]);
	}

	public Class<CDOMObject> getTokenClass()
	{
		return CDOMObject.class;
	}
}
