/*
 * AbilityToken.java
 * Copyright 2006 (C) Aaron Divinsky <boomer70@yahoo.com>
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
 *
 * Created on March 3, 2006
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 */

package plugin.lsttokens.kit.ability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.Constants;
import pcgen.cdom.reference.ReferenceManufacturer;
import pcgen.cdom.reference.ReferenceUtilities;
import pcgen.core.Ability;
import pcgen.core.AbilityCategory;
import pcgen.core.AbilityUtilities;
import pcgen.core.kit.KitAbilities;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.TokenUtilities;
import pcgen.rules.persistence.token.AbstractTokenWithSeparator;
import pcgen.rules.persistence.token.CDOMSecondaryParserToken;
import pcgen.rules.persistence.token.ParseResult;

/**
 * FEAT Token for KitAbilities
 */
public class FeatToken extends AbstractTokenWithSeparator<KitAbilities> implements
		CDOMSecondaryParserToken<KitAbilities>
{
	private static final Class<Ability> ABILITY_CLASS = Ability.class;

	/**
	 * Gets the name of the tag this class will parse.
	 * 
	 * @return Name of the tag this class handles
	 */
	@Override
	public String getTokenName()
	{
		return "FEAT";
	}

	public Class<KitAbilities> getTokenClass()
	{
		return KitAbilities.class;
	}

	public String getParentToken()
	{
		return "*KITTOKEN";
	}

	@Override
	protected char separator()
	{
		return '|';
	}

	@Override
	protected ParseResult parseTokenWithSeparator(LoadContext context,
		KitAbilities kitAbil, String value)
	{
		StringTokenizer st = new StringTokenizer(value, "|");

		kitAbil.setCategory(AbilityCategory.FEAT);

		ReferenceManufacturer<Ability> rm = context.ref.getManufacturer(
				ABILITY_CLASS, AbilityCategory.FEAT);

		while (st.hasMoreTokens())
		{
			String token = st.nextToken();

			if (token.startsWith("CATEGORY="))
			{
				return new ParseResult.Fail(
					"Attempting to change the Category of a Feat to '" + token
						+ "'");
			}
			CDOMReference<Ability> ref =
					TokenUtilities.getTypeOrPrimitive(rm, token);
			if (ref == null)
			{
				return ParseResult.INTERNAL_ERROR;
			}
			List<String> choices = null;
			if (token.indexOf('(') != -1)
			{
				choices = new ArrayList<String>();
				AbilityUtilities.getUndecoratedName(token, choices);
			}
			kitAbil.addAbility(ref, choices);
		}
		return ParseResult.SUCCESS;
	}

	public String[] unparse(LoadContext context, KitAbilities KitAbilities)
	{
		Collection<CDOMReference<Ability>> ref = KitAbilities.getAbilityKeys();
		if (ref == null || ref.isEmpty())
		{
			return null;
		}
		return new String[]{ReferenceUtilities.joinLstFormat(ref,
			Constants.PIPE)};
	}
}
