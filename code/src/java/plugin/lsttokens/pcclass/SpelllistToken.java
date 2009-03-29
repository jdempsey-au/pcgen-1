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
package plugin.lsttokens.pcclass;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import pcgen.base.formula.Formula;
import pcgen.cdom.base.CDOMListObject;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.ChoiceSet;
import pcgen.cdom.base.Constants;
import pcgen.cdom.base.FormulaFactory;
import pcgen.cdom.base.PrimitiveChoiceSet;
import pcgen.cdom.base.TransitionChoice;
import pcgen.cdom.choiceset.SpellReferenceChoiceSet;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.list.ClassSpellList;
import pcgen.cdom.list.DomainSpellList;
import pcgen.core.PCClass;
import pcgen.core.spell.Spell;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractToken;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.util.Logging;

/**
 * Class deals with SPELLLIST Token
 */
public class SpelllistToken extends AbstractToken implements
		CDOMPrimaryToken<PCClass>
{

	private static Class<ClassSpellList> SPELLLIST_CLASS = ClassSpellList.class;
	private static Class<DomainSpellList> DOMAINSPELLLIST_CLASS = DomainSpellList.class;

	@Override
	public String getTokenName()
	{
		return "SPELLLIST";
	}

	public boolean parse(LoadContext context, PCClass pcc, String value)
	{
		if (isEmpty(value) || hasIllegalSeparator('|', value))
		{
			return false;
		}
		StringTokenizer tok = new StringTokenizer(value, Constants.PIPE);
		Formula count = FormulaFactory.getFormulaFor(tok.nextToken());
		if (!count.isStatic() || count.resolve(null, "").intValue() <= 0)
		{
			Logging.errorPrint("Count in " + getTokenName() + " must be > 0");
			return false;
		}
		if (!tok.hasMoreTokens())
		{
			Logging.addParseMessage(Logging.LST_ERROR, getTokenName()
					+ " must have a | separating "
					+ "count from the list of possible values: " + value);
			return false;
		}
		List<CDOMReference<? extends CDOMListObject<Spell>>> refs = new ArrayList<CDOMReference<? extends CDOMListObject<Spell>>>();
		boolean foundAny = false;
		boolean foundOther = false;

		while (tok.hasMoreTokens())
		{
			String token = tok.nextToken();
			CDOMReference<? extends CDOMListObject<Spell>> ref;
			if (Constants.LST_ALL.equals(token))
			{
				foundAny = true;
				ref = context.ref.getCDOMAllReference(SPELLLIST_CLASS);
			}
			else if (token.startsWith("DOMAIN."))
			{
				foundOther = true;
				ref = context.ref.getCDOMReference(DOMAINSPELLLIST_CLASS, token
						.substring(7));
			}
			else
			{
				foundOther = true;
				ref = context.ref.getCDOMReference(SPELLLIST_CLASS, token);
			}
			refs.add(ref);
		}

		if (foundAny && foundOther)
		{
			Logging.addParseMessage(Logging.LST_ERROR, "Non-sensical "
					+ getTokenName()
					+ ": Contains ANY and a specific reference: " + value);
			return false;
		}

		PrimitiveChoiceSet<CDOMListObject<Spell>> rcs = new SpellReferenceChoiceSet(
				refs);
		ChoiceSet<? extends CDOMListObject<Spell>> cs = new ChoiceSet<CDOMListObject<Spell>>(
				getTokenName(), rcs);
		cs.setTitle("Select class whose list of spells this class will use");
		TransitionChoice<CDOMListObject<Spell>> tc = new TransitionChoice<CDOMListObject<Spell>>(
				cs, count);
		context.getObjectContext().put(pcc, ObjectKey.SPELLLIST_CHOICE, tc);
		tc.setRequired(false);
		return true;
	}

	public String[] unparse(LoadContext context, PCClass pcc)
	{
		TransitionChoice<CDOMListObject<Spell>> grantChanges = context
				.getObjectContext().getObject(pcc, ObjectKey.SPELLLIST_CHOICE);
		if (grantChanges == null)
		{
			// Zero indicates no Token
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(grantChanges.getCount());
		sb.append(Constants.PIPE);
		sb.append(grantChanges.getChoices().getLSTformat().replaceAll(
				Constants.COMMA, Constants.PIPE));
		return new String[] { sb.toString() };
	}

	public Class<PCClass> getTokenClass()
	{
		return PCClass.class;
	}
}
