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
 * $Id: SpellListBookToken.java,v 1.6 2005/10/18 20:23:58 binkley Exp $
 *
 */
package plugin.exporttokens;

import pcgen.core.Globals;
import pcgen.core.PObject;
import pcgen.core.PlayerCharacter;
import pcgen.core.character.CharacterSpell;
import pcgen.io.ExportHandler;
import pcgen.io.exporttoken.SpellListToken;

import java.util.Iterator;
import java.util.List;

/**
 * <code>SpellListBookToken</code> gives a comma delimited list of spells
 * known for the specified spellcaster class number and level (if any).
 *
 * Last Editor: $Author: binkley $
 * Last Edited: $Date: 2005/10/18 20:23:58 $
 *
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision: 1.6 $
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
	public String getToken(String tokenSource, PlayerCharacter pc, ExportHandler eh)
	{
		StringBuffer retValue = new StringBuffer();
		int y = 0;

		SpellListTokenParams params = new SpellListTokenParams(tokenSource,
			SpellListToken.SPELLTAG_BOOK);

		final PObject aObject = pc.getSpellClassAtIndex(params.getClassNum());

		if (aObject != null)
		{
			CharacterSpell cs;
			String bookName = Globals.getDefaultSpellBook();

			if (params.getBookNum() > 0)
			{
				bookName = (String) pc.getSpellBooks().get(params.getBookNum());
			}

			final List spells = aObject.getSpellSupport().getCharacterSpell(null,
				bookName, params.getLevel());

			for (Iterator se = spells.iterator(); se.hasNext();)
			{
				cs = (CharacterSpell) se.next();

				if (y++ > 0)
				{
					retValue.append(", ");
				}

				retValue.append(cs.getSpell().getOutputName());
			}

			if ((y == 0) && eh != null && eh.getExistsOnly())
			{
				eh.setNoMoreItems(true);
			}
		}

		return retValue.toString();
	}

}
