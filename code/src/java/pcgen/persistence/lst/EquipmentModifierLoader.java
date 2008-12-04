/*
 * EquipmentModifierLoader.java
 * Copyright 2001 (C) Bryan McRoberts <merton_monk@yahoo.com>
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
 * Created on February 22, 2002, 10:29 PM
 *
 * $Id$
 */
package pcgen.persistence.lst;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.StringTokenizer;

import pcgen.base.lang.UnreachableError;
import pcgen.cdom.base.Constants;
import pcgen.core.Campaign;
import pcgen.core.EquipmentModifier;
import pcgen.core.Globals;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.SystemLoader;
import pcgen.rules.context.LoadContext;
import pcgen.util.Logging;

/**
 * 
 * @author David Rice <david-pcgen@jcuz.com>
 * @version $Revision$
 */
public final class EquipmentModifierLoader extends
		LstObjectFileLoader<EquipmentModifier> {
	@Override
	protected EquipmentModifier getObjectKeyed(String aKey)
	{
		return Globals.getContext().ref.silentlyGetConstructedCDOMObject(
				EquipmentModifier.class, aKey);
	}

	@Override
	public EquipmentModifier parseLine(LoadContext context,
			EquipmentModifier eqMod, String inputLine, CampaignSourceEntry source)
			throws PersistenceLayerException {
		boolean isnew = false;
		if (eqMod == null) {
			eqMod = new EquipmentModifier();
			isnew = true;
		}

		final StringTokenizer colToken = new StringTokenizer(inputLine,
				SystemLoader.TAB_DELIM);

		if (colToken.hasMoreTokens())
		{
			eqMod.setName(colToken.nextToken().replace('|', ' '));
			eqMod.setSourceCampaign(source.getCampaign());
			eqMod.setSourceURI(source.getURI());
			if (isnew)
			{
				context.ref.importObject(eqMod);
			}
		}

		Map<String, LstToken> tokenMap = TokenStore.inst().getTokenMap(
				EquipmentModifierLstToken.class);
		while (colToken.hasMoreTokens()) {
			final String token = colToken.nextToken().trim();
			final int colonLoc = token.indexOf(':');
			if (colonLoc == -1)
			{
				Logging.errorPrint("Invalid Token - does not contain a colon: "
						+ token);
				continue;
			}
			else if (colonLoc == 0)
 			{
				Logging.errorPrint("Invalid Token - starts with a colon: "
						+ token);
				continue;
 			}

			String key = token.substring(0, colonLoc);
			String value = (colonLoc == token.length() - 1) ? null : token
					.substring(colonLoc + 1);
			if (context.processToken(eqMod, key, value))
			{
				context.commit();
			}
			else if (tokenMap.containsKey(key))
			{
				EquipmentModifierLstToken tok = (EquipmentModifierLstToken) tokenMap
						.get(key);
				LstUtils.deprecationCheck(tok, eqMod, value);
				if (!tok.parse(eqMod, value))
				{
					Logging.errorPrint("Error parsing EqMod "
							+ eqMod.getDisplayName() + ':' + source.getURI()
							+ ':' + token + "\"");
				}
			}
			else if (!PObjectLoader.parseTag(eqMod, token))
			{
				Logging.replayParsedMessages();
 			}
			Logging.clearParseMessages();
		}

		completeObject(context, source, eqMod);
		return null;
	}

	@Override
	protected void performForget(EquipmentModifier objToForget) {
		throw new java.lang.UnsupportedOperationException(
				"Cannot FORGET an EquipmentModifier");
	}

	/**
	 * This method adds the default available equipment modifiers to the
	 * Globals.
	 * @throws PersistenceLayerException 
	 * 
	 * @throws PersistenceLayerException
	 *             if some bizarre error occurs, likely due to a change in
	 *             EquipmentModifierLoader
	 */
	public void addDefaultEquipmentMods(LoadContext context) throws PersistenceLayerException {
		CampaignSourceEntry source;
		try {
			source = new CampaignSourceEntry(new Campaign(),
					new URI("file:/" + getClass().getName() + ".java"));
		} catch (URISyntaxException e) {
			throw new UnreachableError(e);
		}
		String aLine;
		aLine = "Add Type\tKEY:ADDTYPE\tTYPE:ALL\tCOST:0\tNAMEOPT:NONAME\tSOURCELONG:PCGen Internal\tCHOOSE:EQBUILDER.EQTYPE|COUNT=ALL|TITLE=desired TYPE(s)";
		parseLine(context, null, aLine, source);

		//
		// Add internal equipment modifier for adding weapon/armor types to
		// equipment
		//
		aLine = Constants.s_INTERNAL_EQMOD_WEAPON
				+ "\tTYPE:Weapon\tVISIBLE:NO\tCHOOSE:NOCHOICE\tNAMEOPT:NONAME";
		parseLine(context, null, aLine, source);

		aLine = Constants.s_INTERNAL_EQMOD_ARMOR
				+ "\tTYPE:Armor\tVISIBLE:NO\tCHOOSE:NOCHOICE\tNAMEOPT:NONAME";
		parseLine(context, null, aLine, source);
	}
}
