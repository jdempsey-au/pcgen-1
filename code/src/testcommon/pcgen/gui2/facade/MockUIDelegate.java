/**
 * MockUIDelegate.java
 * Copyright James Dempsey, 2010
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
 * Created on 29/01/2011 3:48:05 PM
 *
 * $Id$
 */
package pcgen.gui2.facade;

import pcgen.core.facade.CharacterFacade;
import pcgen.core.facade.ChooserFacade;
import pcgen.core.facade.EquipmentBuilderFacade;
import pcgen.core.facade.SpellBuilderFacade;
import pcgen.core.facade.UIDelegate;
import pcgen.system.PropertyContext;
import pcgen.util.Logging;

/**
 * The Class <code></code> is ...
 *
 * <br/>
 * Last Editor: $Author$
 * Last Edited: $Date$
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision$
 */
public class MockUIDelegate implements UIDelegate
{

	/* (non-Javadoc)
	 * @see pcgen.core.facade.UIDelegate#maybeShowWarningConfirm(java.lang.String, java.lang.String, java.lang.String, pcgen.system.PropertyContext, java.lang.String)
	 */
    @Override
	public Boolean maybeShowWarningConfirm(String title, String message,
		String checkBoxText, PropertyContext context, String contextProp)
	{
		Logging.log(Logging.WARNING, title + " - " + message);
		return false;
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.UIDelegate#showErrorMessage(java.lang.String, java.lang.String)
	 */
    @Override
	public void showErrorMessage(String title, String message)
	{
		Logging.log(Logging.ERROR, title + " - " + message);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.UIDelegate#showInfoMessage(java.lang.String, java.lang.String)
	 */
    @Override
	public void showInfoMessage(String title, String message)
	{
		Logging.log(Logging.INFO, title + " - " + message);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.UIDelegate#showLevelUpInfo(pcgen.core.facade.CharacterFacade, int)
	 */
    @Override
	public void showLevelUpInfo(CharacterFacade character, int oldLevel)
	{
		// No action
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.UIDelegate#showWarningConfirm(java.lang.String, java.lang.String)
	 */
    @Override
	public boolean showWarningConfirm(String title, String message)
	{
		// No action
		return false;
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.UIDelegate#showWarningPrompt(java.lang.String, java.lang.String)
	 */
    @Override
	public boolean showWarningPrompt(String title, String message)
	{
		// No action
		return false;
	}

    @Override
	public void showWarningMessage(String title, String message)
	{
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean showGeneralChooser(ChooserFacade chooserFacade)
	{
		// Select as many choices as needed from the top of the available list 
		int numChoices =
				chooserFacade.getRemainingSelections().getReference();
		for (int i = 0; i < numChoices
			&& !chooserFacade.getAvailableList().isEmpty(); i++)
		{
			chooserFacade.addSelected(chooserFacade.getAvailableList()
				.getElementAt(0));
		}
		chooserFacade.commit();
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String showInputDialog(String title, String message, String initialValue)
	{
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomEquipResult showCustomEquipDialog(CharacterFacade character, 
		EquipmentBuilderFacade equipBuilder)
	{
		return CustomEquipResult.CANCELLED;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean showCustomSpellDialog(SpellBuilderFacade spellBuilderFacade)
	{
		return false;
	}

}
