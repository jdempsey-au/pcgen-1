/*
 * SpellListBookToken.java
 * Copyright 2004 (C) James Dempsey <jdempsey@users.sourceforge.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.     See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on Jul 15, 2004
 *
 * $Id$
 *
 */
package plugin.exporttokens;

import pcgen.core.Globals;
import pcgen.core.PObject;
import pcgen.core.PlayerCharacter;
import pcgen.core.analysis.OutputNameFormatting;
import pcgen.core.character.CharacterSpell;
import pcgen.io.ExportHandler;
import pcgen.io.exporttoken.SpellListToken;

import java.util.List;

/**
 * <code>SpellListBookToken</code> gives a comma delimited list of spells
 * known for the specified spellcaster class number and level (if any).
 *
 * Last Editor: $Author$
 * Last Edited: $Date$
 *
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision$
 */

public class SpellListBookToken extends SpellListToken
{
	/** token name */
	public static final String TOKENNAME = "SPELLLISTBOOK";

	/**
	 * @see pcgen.io.exporttoken.Token#getTokenName()
	 */
	public String getTokenName()
	{
		return TOKENNAME;
	}

	/**
	 * @see pcgen.io.exporttoken.Token#getToken(java.lang.String, pcgen.core.PlayerCharacter, pcgen.io.ExportHandler)
	 */
	public String getToken(String tokenSource, PlayerCharacter pc,
		ExportHandler eh)
	{
		StringBuffer retValue = new StringBuffer();

		SpellListTokenParams params =
				new SpellListTokenParams(tokenSource,
					SpellListToken.SPELLTAG_BOOK);

		final PObject aObject = pc.getSpellClassAtIndex(params.getClassNum());

		if (aObject != null)
		{
			String bookName = Globals.getDefaultSpellBook();

			if (params.getBookNum() > 0)
			{
				bookName = pc.getSpellBooks().get(params.getBookNum());
			}

			final List<CharacterSpell> spells =
					pc.getCharacterSpells(aObject,
						null, bookName, params.getLevel());

			boolean needcomma = false;
			for (CharacterSpell cs : spells)
			{
				if (needcomma)
				{
					retValue.append(", ");
				}
				needcomma = true;

				retValue.append(OutputNameFormatting.getOutputName(cs.getSpell()));
			}

			if (!needcomma && eh != null && eh.getExistsOnly())
			{
				eh.setNoMoreItems(true);
			}
		}

		return retValue.toString();
	}

}
