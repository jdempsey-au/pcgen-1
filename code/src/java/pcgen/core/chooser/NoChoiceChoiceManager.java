package pcgen.core.chooser;

import java.util.ArrayList;
import java.util.List;

import pcgen.cdom.base.Constants;
import pcgen.core.PObject;
import pcgen.core.PlayerCharacter;

public class NoChoiceChoiceManager extends AbstractBasicChoiceManager<String>
{

	public NoChoiceChoiceManager(PObject object, String theChoices,
			PlayerCharacter apc)
	{
		super(object, theChoices, apc);
	}

	@Override
	public void getChoices(PlayerCharacter pc, List<String> availableList,
			List<String> selectedList)
	{
		availableList.add("NOCHOICE");
		selectedList.addAll(pc.getAssociationList(pobject));
		setPreChooserChoices(selectedList.size());
	}

	@Override
	public List<String> doChooser(PlayerCharacter pc,
			List<String> availableList, List<String> selectedList,
			List<String> reservedList)
	{
		int choiceLimit = getNumberOfChoices();
		if (choiceLimit == -1 || choiceLimit > reservedList.size())
		{
			selectedList.add("NOCHOICE");
		}
		return new ArrayList<String>(selectedList);
	}

	@Override
	public void doChooserRemove(PlayerCharacter apc,
			List<String> availableList, List<String> selectedList,
			List<String> reservedList)
	{
		selectedList.remove(0);
		applyChoices(apc, selectedList);
	}

	@Override
	public void applyChoices(PlayerCharacter apc, List<String> selected)
	{
		apc.removeAllAssociations(pobject);
		for (int i = 0; i < selected.size(); i++)
		{
			apc.addAssociation(pobject, Constants.EMPTY_STRING);
		}
		adjustPool(selected);
	}

}
