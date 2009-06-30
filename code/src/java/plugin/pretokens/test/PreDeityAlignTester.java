/*
 * Created on 02-Dec-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package plugin.pretokens.test;

import pcgen.cdom.enumeration.ObjectKey;
import pcgen.core.GameMode;
import pcgen.core.Globals;
import pcgen.core.PCAlignment;
import pcgen.core.PlayerCharacter;
import pcgen.core.SettingsHandler;
import pcgen.core.prereq.AbstractPrerequisiteTest;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteTest;
import pcgen.util.PropertyFactory;

/**
 * @author wardc
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class PreDeityAlignTester extends AbstractPrerequisiteTest implements
		PrerequisiteTest
{

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#passes(pcgen.core.PlayerCharacter)
	 */
	@Override
	public int passes(final Prerequisite prereq, final PlayerCharacter character)
	{

		//
		// If game mode doesn't support alignment, then pass the prereq
		//
		int runningTotal = 0;

		if (Globals.getGameModeAlignmentText().length() == 0)
		{
			runningTotal = 1;
		}
		else
		{
			final GameMode gm = SettingsHandler.getGame();
			final String[] aligns = gm.getAlignmentListStrings(false);

			PCAlignment deityAlign = null; //$NON-NLS-1$
			if (character.getDeity() != null)
			{
				deityAlign = character.getDeity().get(ObjectKey.ALIGNMENT);
			}
			if (deityAlign != null)
			{
				String desiredAlign = prereq.getOperand();
				try
				{
					final int align = Integer.parseInt(prereq.getOperand());
					desiredAlign = aligns[align];
				}
				catch (NumberFormatException e)
				{
					// If it isn't a number, we expect the exception 
				}

				if (desiredAlign.equalsIgnoreCase(deityAlign.getKeyName()))
				{
					runningTotal = 1;
				}
			}
		}

		return countedTotal(prereq, runningTotal);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#kindsHandled()
	 */
	public String kindHandled()
	{
		return "DEITYALIGN"; //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#toHtmlString(pcgen.core.prereq.Prerequisite)
	 */
	@Override
	public String toHtmlString(final Prerequisite prereq)
	{
		return PropertyFactory
			.getFormattedString(
				"PreDeityAlign.toHtml", prereq.getOperator().toDisplayString(), SettingsHandler.getGame().getAlignmentAtIndex(Integer.parseInt(prereq.getKey())).getKeyName()); //$NON-NLS-1$
	}

}
