/*
 * RollmethodToken.java
 * Copyright 2005 (C) Greg Bingleman <byngl@hotmail.com>
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
 * Created on September 2, 2005, 8:39 AM
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 *
 */
package plugin.lsttokens.gamemode;

import pcgen.core.GameMode;
import pcgen.persistence.lst.GameModeLstToken;
import pcgen.persistence.lst.RollMethodLoader;

/**
 * <code>RollmethodToken</code>
 *
 * @author  Greg Bingleman <byngl@hotmail.com>
 */
public class RollmethodToken implements GameModeLstToken
{

	public String getTokenName()
	{
		return "ROLLMETHOD";
	}

	public boolean parse(GameMode gameMode, String value)
	{
		try
		{
			RollMethodLoader methodLoader = new RollMethodLoader();
			methodLoader.parseLine(gameMode, "ROLLMETHOD:" + value);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
