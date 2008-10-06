package plugin.lsttokens.ability;

import pcgen.cdom.enumeration.ObjectKey;
import pcgen.core.Ability;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractToken;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.util.Logging;

/**
 * Deal with STACK token
 */
public class StackToken extends AbstractToken implements
		CDOMPrimaryToken<Ability>
{

	@Override
	public String getTokenName()
	{
		return "STACK";
	}

	public boolean parse(LoadContext context, Ability ability, String value)
	{
		if (isEmpty(value))
		{
			return false;
		}
		Boolean set;
		char firstChar = value.charAt(0);
		if (firstChar == 'y' || firstChar == 'Y')
		{
			if (value.length() > 1 && !value.equalsIgnoreCase("YES"))
			{
				Logging.errorPrint("You should use 'YES' as the "
						+ getTokenName() + ": " + value);
				return false;
			}
			set = Boolean.TRUE;
		}
		else
		{
			if (firstChar != 'N' && firstChar != 'n')
			{
				Logging.errorPrint("You should use 'YES' or 'NO' as the "
						+ getTokenName() + ": " + value);
				return false;
			}
			if (value.length() > 1 && !value.equalsIgnoreCase("NO"))
			{
				Logging.errorPrint("You should use 'YES' or 'NO' as the "
						+ getTokenName() + ": " + value);
				return false;
			}
			set = Boolean.FALSE;
		}
		context.getObjectContext().put(ability, ObjectKey.STACKS, set);
		return true;
	}

	public String[] unparse(LoadContext context, Ability ability)
	{
		Boolean stacks = context.getObjectContext().getObject(ability,
				ObjectKey.STACKS);
		if (stacks == null)
		{
			return null;
		}
		return new String[] { stacks.booleanValue() ? "YES" : "NO" };
	}

	public Class<Ability> getTokenClass()
	{
		return Ability.class;
	}
}
