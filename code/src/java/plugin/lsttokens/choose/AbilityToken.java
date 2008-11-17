/*
 * AbilityToken.java
 * Copyright 2008 (C) James Dempsey
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
 * Created on 18/10/2008 19:00:51
 *
 * $Id: $
 */
package plugin.lsttokens.choose;

import pcgen.core.AbilityCategory;
import pcgen.core.PObject;
import pcgen.core.SettingsHandler;
import pcgen.persistence.lst.ChooseLstToken;
import pcgen.util.Logging;

/**
 * The Class <code>AbilityToken</code> is responsible for parsing
 * the ability choose token.
 * 
 * Last Editor: $Author: $
 * Last Edited: $Date:  $
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision:  $
 */
public class AbilityToken implements ChooseLstToken
{

	/* (non-Javadoc)
	 * @see pcgen.persistence.lst.LstToken#getTokenName()
	 */
	public String getTokenName()
	{
		return "ABILITY";
	}

	/* (non-Javadoc)
	 * @see pcgen.persistence.lst.ChooseLstToken#parse(pcgen.core.PObject, java.lang.String, java.lang.String)
	 */
	public boolean parse(PObject po, String prefix, String value)
	{
		if (value == null)
		{
			Logging.log(Logging.LST_ERROR, "CHOOSE:" + getTokenName()
					+ " requires arguments");
			return false;
		}
		if (value.indexOf('[') != -1)
		{
			Logging.log(Logging.LST_ERROR, "CHOOSE:" + getTokenName()
				+ " arguments may not contain [] : " + value);
			return false;
		}
		if (value.charAt(0) == '|')
		{
			Logging.log(Logging.LST_ERROR, "CHOOSE:" + getTokenName()
				+ " arguments may not start with | : " + value);
			return false;
		}
		if (value.charAt(value.length() - 1) == '|')
		{
			Logging.log(Logging.LST_ERROR, "CHOOSE:" + getTokenName()
				+ " arguments may not end with | : " + value);
			return false;
		}
		if (value.indexOf("||") != -1)
		{
			Logging.log(Logging.LST_ERROR, "CHOOSE:" + getTokenName()
				+ " arguments uses double separator || : " + value);
			return false;
		}
		int barLoc = value.indexOf('|');
		if (barLoc == -1)
		{
			Logging.log(Logging.LST_ERROR, "CHOOSE:" + getTokenName()
				+ " requires a CATEGORY and arguments : " + value);
			return false;
		}
		String cat = value.substring(0, barLoc);
		AbilityCategory category = SettingsHandler.getGame()
				.silentlyGetAbilityCategory(cat);
		if (category == null)
		{
			Logging.log(Logging.LST_ERROR, "CHOOSE:" + getTokenName()
					+ " found invalid CATEGORY: " + cat + " in value: "
							+ value);
			return false;
		}

		StringBuilder sb = new StringBuilder();
		if (prefix.length() > 0)
		{
			sb.append(prefix).append('|');
		}
		sb.append(getTokenName()).append('|').append(value);
		po.setChoiceString(sb.toString());
		return true;
	}
}
