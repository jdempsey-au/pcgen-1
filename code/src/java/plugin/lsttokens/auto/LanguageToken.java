/*
 * Copyright 2007 (C) Thomas Parker <thpr@users.sourceforge.net>
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
package plugin.lsttokens.auto;

import java.util.Collection;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.ChooseResultActor;
import pcgen.cdom.base.Constants;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.Globals;
import pcgen.core.Language;
import pcgen.core.PlayerCharacter;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.context.Changes;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractNonEmptyToken;
import pcgen.rules.persistence.token.CDOMSecondaryToken;
import pcgen.rules.persistence.token.ParseResult;

public class LanguageToken extends AbstractNonEmptyToken<CDOMObject> implements
		CDOMSecondaryToken<CDOMObject>, ChooseResultActor
{

	private static final Class<Language> LANGUAGE_CLASS = Language.class;

	public String getParentToken()
	{
		return "AUTO";
	}

	@Override
	public String getTokenName()
	{
		return "LANGUAGE";
	}

	private String getFullName()
	{
		return getParentToken() + ":" + getTokenName();
	}

	@Override
	protected ParseResult parseNonEmptyToken(LoadContext context,
			CDOMObject obj, String value)
	{
		String weaponProfs = value;

		ParseResult pr = checkSeparatorsAndNonEmpty('|', weaponProfs);
		if (!pr.passed())
		{
			return pr;
		}

		boolean foundAny = false;
		boolean foundOther = false;

		StringTokenizer tok = new StringTokenizer(weaponProfs, Constants.PIPE);

		while (tok.hasMoreTokens())
		{
			String aProf = tok.nextToken();

			if ("%LIST".equals(aProf))
			{
				foundOther = true;
				context.obj.addToList(obj, ListKey.CHOOSE_ACTOR, this);
			}
			else if (Constants.LST_ALL.equalsIgnoreCase(aProf))
			{
				foundAny = true;
				context.obj.addToList(obj, ListKey.AUTO_LANGUAGE, context.ref
						.getCDOMAllReference(LANGUAGE_CLASS));
			}
			else
			{
				foundOther = true;
				context.obj.addToList(obj, ListKey.AUTO_LANGUAGE, context.ref
						.getCDOMReference(LANGUAGE_CLASS, aProf));
			}
		}

		if (foundAny && foundOther)
		{
			return new ParseResult.Fail("Non-sensical " + getFullName()
					+ ": Contains ANY and a specific reference: " + value);
		}

		return ParseResult.SUCCESS;
	}

	public String[] unparse(LoadContext context, CDOMObject obj)
	{
		Changes<CDOMReference<Language>> changes = context.obj.getListChanges(
				obj, ListKey.AUTO_LANGUAGE);
		Changes<ChooseResultActor> listChanges = context.getObjectContext()
				.getListChanges(obj, ListKey.CHOOSE_ACTOR);
		Collection<CDOMReference<Language>> added = changes.getAdded();
		Set<String> set = new TreeSet<String>();
		Collection<ChooseResultActor> listAdded = listChanges.getAdded();
		boolean foundAny = false;
		boolean foundOther = false;
		if (listAdded != null && !listAdded.isEmpty())
		{
			for (ChooseResultActor cra : listAdded)
			{
				if (cra.getSource().equals(getTokenName()))
				{
					try
					{
						set.add(cra.getLstFormat());
						foundOther = true;
					}
					catch (PersistenceLayerException e)
					{
						context.addWriteMessage("Error writing Prerequisite: "
								+ e);
						return null;
					}
				}
			}
		}
		if (added != null)
		{
			for (CDOMReference<Language> spp : added)
			{
				String ab = spp.getLSTformat();
				boolean isUnconditionalAll = Constants.LST_ALL.equals(ab);
				foundAny |= isUnconditionalAll;
				foundOther |= !isUnconditionalAll;
				set.add(ab);
			}
		}
		if (foundAny && foundOther)
		{
			context.addWriteMessage("Non-sensical " + getFullName()
					+ ": Contains ANY and a specific reference: " + set);
			return null;
		}
		if (set.isEmpty())
		{
			// okay
			return null;
		}
		return set.toArray(new String[set.size()]);
	}

	public Class<CDOMObject> getTokenClass()
	{
		return CDOMObject.class;
	}

	public void apply(PlayerCharacter pc, CDOMObject obj, String o)
	{
		Language l = Globals.getContext().ref.silentlyGetConstructedCDOMObject(
				LANGUAGE_CLASS, o);
		if (l != null)
		{
			pc.addSkillLanguage(l, obj);
		}
	}

	public void remove(PlayerCharacter pc, CDOMObject obj, String o)
	{
		Language l = Globals.getContext().ref.silentlyGetConstructedCDOMObject(
				LANGUAGE_CLASS, o);
		if (l != null)
		{
			pc.removeSkillLanguage(l, obj);
		}
	}

	public String getSource()
	{
		return getTokenName();
	}

	public String getLstFormat()
	{
		return "%LIST";
	}
}
