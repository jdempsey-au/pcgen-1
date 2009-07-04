/**
 * DomainChoiceManager.java
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
 * Current Version: $Revision: 285 $
 * Last Editor:     $Author: nuance $
 * Last Edited:     $Date: 2006-03-17 15:19:49 +0000 (Fri, 17 Mar 2006) $
 *
 * Copyright 2006 Andrew Wilson <nuance@sourceforge.net>
 */
package pcgen.core.chooser;

import java.util.List;

import pcgen.cdom.base.CDOMReference;
import pcgen.core.Deity;
import pcgen.core.Domain;
import pcgen.core.Globals;
import pcgen.core.PObject;
import pcgen.core.PlayerCharacter;
import pcgen.core.prereq.PrereqHandler;
import pcgen.rules.context.ReferenceContext;

/**
 * This is the chooser that deals with choosing a domain.
 */
public class DomainChoiceManager extends AbstractBasicPObjectChoiceManager<Domain> {

	/**
	 * Make a new Armor Type chooser.
	 *
	 * @param aPObject
	 * @param choiceString
	 * @param aPC
	 */
	public DomainChoiceManager(
			PObject         aPObject,
			String          choiceString,
			PlayerCharacter aPC)
	{
		super(aPObject, choiceString, aPC);
		setTitle("Domain Choice");
	}


	/**
	 * Parse the Choice string and build a list of available choices.
	 * @param aPc
	 * @param availableList
	 * @param selectedList
	 */
	@Override
	public void getChoices(
			final PlayerCharacter aPc,
			final List<Domain>            availableList,
			final List<Domain>            selectedList)
	{
		ReferenceContext refContext = Globals.getContext().ref;
		for (String option : getChoiceList())
		{
			if ("ANY".equals(option))
			{
				// returns a list of all loaded Domains.
				for ( Domain domain : refContext.getConstructedCDOMObjects(Domain.class) )
				{
					availableList.add(domain);
				}
				break;
			}
			else if ("QUALIFY".equals(option))
			{
				// returns a list of loaded Domains the PC qualifies for
				// but does not have.
				for ( Domain domain : refContext.getConstructedCDOMObjects(Domain.class) )
				{
					if (PrereqHandler.passesAll(domain.getPrerequisiteList(), aPc, domain))
					{
						boolean found = false;
						for (Domain d : aPc.getDomainSet())
						{
							if (domain.equals(d))
							{
								found = true;
								break;
							}
						}
						if (!found)
						{
							availableList.add(domain);
						}
					}
				}
				break;
			}
			else if ("PC".equals(option))
			{
				// returns a list of all domains a character actually has.
				for (Domain d : aPc.getDomainSet())
				{
					availableList.add(d);
				}
				break;
			}
			else if (option.startsWith("DEITY"))
			{
				// returns a list of Domains granted by specified Diety.
				String deityName = option.substring(6);
				Deity deity = refContext.silentlyGetConstructedCDOMObject(Deity.class, deityName);
				if (deity != null)
				{
					for (CDOMReference<Domain> ref : deity.getSafeListMods(Deity.DOMAINLIST))
					{
						availableList.addAll(ref.getContainedObjects());
					}
				}
				break;
			}
			else
			{
				// returns a list of the specified domains.
				Domain domain = refContext.silentlyGetConstructedCDOMObject(Domain.class, option);
				if (domain != null)
				{
					availableList.add(domain);
				}
			}
		}

		for (String key : aPc.getAssociationList(pobject))
		{
			Domain domain = refContext.silentlyGetConstructedCDOMObject(Domain.class, key);
			if ( domain != null )
			{
				selectedList.add( domain );
			}
		}
		setPreChooserChoices(selectedList.size());
	}
}
