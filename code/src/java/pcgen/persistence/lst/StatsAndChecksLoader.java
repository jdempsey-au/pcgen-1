/*
 * StatsAndChecksLoader.java
 * Copyright 2003 (C) David Hibbs <sage_sam@users.sourceforge.net>
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
 * Created on October 13, 2003, 11:50 AM
 *
 * Current Ver: $Revision$ <br>
 * Last Editor: $Author$ <br>
 * Last Edited: $Date$
 */
package pcgen.persistence.lst;

import java.net.URI;
import java.util.Map;

import pcgen.rules.context.LoadContext;
import pcgen.util.Logging;

/**
 * This class is a LstFileLoader that processes the statsandchecks.lst file,
 * handing its multiple types of content off to the appropriate loader
 * for Attributes, Bonus Spells, Checks, and Alignments.
 *
 * @author AD9C15
 */
public class StatsAndChecksLoader extends LstLineFileLoader
{
	/**
	 * StatsAndChecksLoader Constructor.
	 *
	 */
	public StatsAndChecksLoader()
	{
		super();
	}

	/**
	 * @see pcgen.persistence.lst.LstLineFileLoader#parseLine(java.net.URL, java.lang.String)
	 */
	@Override
	public void parseLine(LoadContext context, String lstLine, URI sourceURI)
	{
		final int idxColon = lstLine.indexOf(':');
		if (idxColon < 0)
		{
			return;
		}

		final String key = lstLine.substring(0, idxColon);
		Map<String, LstToken> tokenMap =
				TokenStore.inst().getTokenMap(StatsAndChecksLstToken.class);
		StatsAndChecksLstToken token =
				(StatsAndChecksLstToken) tokenMap.get(key);
		if (token != null)
		{
			LstUtils
				.deprecationCheck(token, key, sourceURI, lstLine);
			if (!token.parse(context, lstLine, sourceURI))
			{
				Logging.errorPrint("Error parsing StatsAndChecks object: "
					+ lstLine + '/' + sourceURI.toString());
			}
		}
		else
		{
			Logging.errorPrint("Illegal StatsAndChecks object: " + lstLine
				+ '/' + sourceURI.toString());
		}
	}
}
