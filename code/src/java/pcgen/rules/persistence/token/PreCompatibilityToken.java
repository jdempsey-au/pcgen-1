/*
 * Copyright 2008 (C) Tom Parker <thpr@users.sourceforge.net>
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package pcgen.rules.persistence.token;

import java.io.StringWriter;
import java.util.Set;
import java.util.TreeSet;

import pcgen.cdom.base.ConcretePrereqObject;
import pcgen.core.prereq.Prerequisite;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.lst.output.prereq.PrerequisiteWriterFactory;
import pcgen.persistence.lst.output.prereq.PrerequisiteWriterInterface;
import pcgen.persistence.lst.prereq.PrerequisiteParserInterface;
import pcgen.rules.context.Changes;
import pcgen.rules.context.LoadContext;

public class PreCompatibilityToken implements
		CDOMPrimaryToken<ConcretePrereqObject>,
		CDOMSecondaryToken<ConcretePrereqObject>
{
	private static PrerequisiteWriterFactory factory = PrerequisiteWriterFactory
			.getInstance();

	private final String tokenRoot;
	private final String tokenName;
	private final PrerequisiteParserInterface token;
	private final boolean invert;

	public PreCompatibilityToken(String s,
			PrerequisiteParserInterface prereqToken, boolean inv)
	{
		tokenRoot = s.toUpperCase();
		token = prereqToken;
		invert = inv;
		tokenName = (invert ? "!" : "") + "PRE" + tokenRoot;
	}

	public Class<ConcretePrereqObject> getTokenClass()
	{
		return ConcretePrereqObject.class;
	}

	public boolean parse(LoadContext context, ConcretePrereqObject obj,
			String value) throws PersistenceLayerException
	{
		Prerequisite p = token.parse(tokenRoot, value, invert, false);
		if (p == null)
		{
			return false;
		}
		context.obj.put(obj, p);
		return true;
	}

	public String getTokenName()
	{
		return tokenName;
	}

	public int compatibilityLevel()
	{
		return 5;
	}

	public int compatibilityPriority()
	{
		return 0;
	}

	public int compatibilitySubLevel()
	{
		return 14;
	}

	public String getParentToken()
	{
		return "*KITTOKEN";
	}

	public String[] unparse(LoadContext context, ConcretePrereqObject obj)
	{
		Set<String> set = new TreeSet<String>();
		Changes<Prerequisite> changes = context.obj.getPrerequisiteChanges(obj);
		if (changes == null || changes.isEmpty())
		{
			return null;
		}
		for (Prerequisite p : changes.getAdded())
		{
			String kind = p.getKind();
			if (kind == null || kind.regionMatches(true, 0, tokenRoot, 0, Math.min(tokenRoot
					.length(), kind.length())))
			{
				final StringWriter capture = new StringWriter();
				try
				{
					PrerequisiteWriterInterface writer = factory
							.getWriter(kind);
					writer.write(capture, p);
				}
				catch (PersistenceLayerException e)
				{
					e.printStackTrace();
				}
				String output = capture.toString();
				int colonLoc = output.indexOf(':');
				if (tokenName.equalsIgnoreCase(output.substring(0, colonLoc)))
				{
					set.add(output.substring(colonLoc + 1));
				}
			}
		}
		if (set.isEmpty())
		{
			return null;
		}
		return set.toArray(new String[set.size()]);
	}

}