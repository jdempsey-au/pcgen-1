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

import pcgen.cdom.enumeration.ObjectKey;
import pcgen.core.PCClass;
import pcgen.core.PCStat;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.util.Logging;

/**
 * Class deals with SPELLSTAT Token
 */
public class SpellstatToken implements CDOMPrimaryToken<PCClass>
{

	private static final Class<PCStat> PCSTAT_CLASS = PCStat.class;

	public String getTokenName()
	{
		return "SPELLSTAT";
	}

	public boolean parse(LoadContext context, PCClass pcc, String value)
	{
		if ("SPELL".equalsIgnoreCase(value))
		{
			context.getObjectContext().put(pcc, ObjectKey.USE_SPELL_SPELL_STAT,
					Boolean.TRUE);
			return true;
		}
		context.getObjectContext().put(pcc, ObjectKey.USE_SPELL_SPELL_STAT,
				Boolean.FALSE);
		if ("OTHER".equalsIgnoreCase(value))
		{
			context.getObjectContext().put(pcc,
					ObjectKey.CASTER_WITHOUT_SPELL_STAT, Boolean.TRUE);
			return true;
		}
		context.getObjectContext().put(pcc,
				ObjectKey.CASTER_WITHOUT_SPELL_STAT, Boolean.FALSE);
		PCStat pcs = context.ref.getAbbreviatedObject(PCSTAT_CLASS, value);
		if (pcs == null)
		{
			Logging.errorPrint("Invalid Stat Abbreviation in " + getTokenName()
					+ ": " + value);
			return false;
		}
		context.getObjectContext().put(pcc, ObjectKey.SPELL_STAT, pcs);
		return true;
	}

	public String[] unparse(LoadContext context, PCClass pcc)
	{
		PCStat pcs = context.getObjectContext().getObject(pcc,
				ObjectKey.SPELL_STAT);
		Boolean useStat = context.getObjectContext().getObject(pcc,
				ObjectKey.USE_SPELL_SPELL_STAT);
		Boolean otherCaster = context.getObjectContext().getObject(pcc,
				ObjectKey.CASTER_WITHOUT_SPELL_STAT);
		if (useStat == null)
		{
			if (pcs != null)
			{
				context
						.addWriteMessage(getTokenName()
								+ " expected USE_SPELL_SPELL_STAT to exist if SPELL_STAT was defined");
			}
			if (otherCaster != null)
			{
				context
						.addWriteMessage(getTokenName()
								+ " expected USE_SPELL_SPELL_STAT to exist if CASTER_WITHOUT_SPELL_STAT was defined");
			}
			return null;
		}
		if (useStat.booleanValue())
		{
			if (pcs != null)
			{
				context
						.addWriteMessage(getTokenName()
								+ " did not expect SPELL_STAT to exist since USE_SPELL_SPELL_STAT was true");
				return null;
			}
			if (otherCaster != null)
			{
				context
						.addWriteMessage(getTokenName()
								+ " did not expect CASTER_WITHOUT_SPELL_STAT to exist since USE_SPELL_SPELL_STAT was true");
				return null;
			}
			return new String[] { "SPELL" };
		}
		if (otherCaster == null)
		{
			context
					.addWriteMessage(getTokenName()
							+ " expected CASTER_WITHOUT_SPELL_STAT to exist if USE_SPELL_SPELL_STAT was false");
			return null;
		}
		else if (otherCaster.booleanValue())
		{
			if (pcs != null)
			{
				context
						.addWriteMessage(getTokenName()
								+ " did not expect SPELL_STAT to exist since CASTER_WITHOUT_SPELL_STAT was true");
				return null;
			}
			return new String[] { "OTHER" };
		}
		else if (pcs == null)
		{
			context
					.addWriteMessage(getTokenName()
							+ " expected SPELL_STAT to exist since USE_SPELL_SPELL_STAT and CASTER_WITHOUT_SPELL_STAT were false");
			return null;
		}
		else
		{
			return new String[] { pcs.getLSTformat() };
		}
	}

	public Class<PCClass> getTokenClass()
	{
		return PCClass.class;
	}
}
