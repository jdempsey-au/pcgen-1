/*
 * ClassListToken.java
 * Copyright 2003 (C) Devon Jones <soulcatcher@evilsoft.org>
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
 * Created on December 15, 2003, 12:21 PM
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 *
 */
package plugin.exporttokens;

import pcgen.core.Constants;
import pcgen.core.PCClass;
import pcgen.core.PlayerCharacter;
import pcgen.io.ExportHandler;
import pcgen.io.exporttoken.Token;
import pcgen.core.Globals;

/**
 * Deal with CLASSLIST token
 */
public class ClassListToken extends Token
{
	/** Token name */
	public static final String TOKENNAME = "CLASSLIST";

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
		return getClassListToken(pc);
	}

	/**
	 * Get the class list token value for the PC
	 * @param pc
	 * @return token value
	 */
	public static String getClassListToken(PlayerCharacter pc)
	{
		String retString = "";
		boolean firstLine = true;

		for (int i = 0; i < pc.getClassList().size(); i++)
		{
			PCClass pcClass = (PCClass) pc.getClassList().get(i);

			if (!firstLine)
			{
				retString += " ";
			}

			firstLine = false;

			if (Constants.s_NONE.equals(pcClass.getSubClassKey()) || "".equals(pcClass.getSubClassKey()))
			{
				retString += pcClass.getOutputName();
			}
			else
			{
				retString += Globals.getClassKeyed(pcClass.getSubClassKey()).getDisplayName();
			}

			retString += pcClass.getLevel();
		}

		return retString;
	}
}
