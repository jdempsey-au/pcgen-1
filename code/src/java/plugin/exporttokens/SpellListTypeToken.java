/*
 * SpellListTypeToken.java
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
 * $Id: SpellListTypeToken.java,v 1.4 2005/10/16 13:13:16 jdempsey Exp $
 *
 */
package plugin.exporttokens;

import pcgen.core.PCClass;
import pcgen.core.PObject;
import pcgen.core.PlayerCharacter;
import pcgen.io.ExportHandler;
import pcgen.io.exporttoken.SpellListToken;

/**
 * <code>SpellListTypeToken</code> outputs the type (i.e. Arcane or 
 * Divine) of the specified spell class.
 *
 * Last Editor: $Author: jdempsey $
 * Last Edited: $Date: 2005/10/16 13:13:16 $
 *
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision: 1.4 $
 */

public class SpellListTypeToken extends SpellListToken
{

	/** token name */
	public static final String TOKENNAME = "SPELLLISTTYPE";

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

		SpellListTokenParams params = new SpellListTokenParams(tokenSource,
			SpellListToken.SPELLTAG_TYPE);
		
		final PObject aObject = pc.getSpellClassAtIndex(params.getClassNum());

		if (aObject != null)
		{
			PCClass aClass = null;

			if (aObject instanceof PCClass)
			{
				aClass = (PCClass) aObject;
			}
			
			if (aClass != null)
			{
				retValue.append(aClass.getSpellType());
			}
		}

		return retValue.toString();
	}

}
