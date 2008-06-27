/**
 * 
 */
package pcgen.rules.persistence.token;

import pcgen.cdom.enumeration.IntegerKey;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.inst.PCClassLevel;
import pcgen.core.PCClass;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.context.LoadContext;
import pcgen.util.Logging;

public class ClassWrappedToken implements CDOMCompatibilityToken<PCClassLevel>
{

	private static int wrapIndex = Integer.MIN_VALUE;

	private static final Integer ONE = Integer.valueOf(1);

	private final CDOMToken<PCClass> wrappedToken;

	private final int priority = wrapIndex++;

	public Class<PCClassLevel> getTokenClass()
	{
		return PCClassLevel.class;
	}

	public ClassWrappedToken(CDOMToken<PCClass> tok)
	{
		wrappedToken = tok;
	}

	public boolean parse(LoadContext context, PCClassLevel obj, String value)
			throws PersistenceLayerException
	{
		if (ONE.equals(obj.get(IntegerKey.LEVEL)))
		{
			PCClass parent = (PCClass) obj.get(ObjectKey.PARENT);
			return wrappedToken.parse(context, parent, value);
		}
		Logging.log(Logging.LST_ERROR, "Data used token: " + value
				+ " which is a Class token, "
				+ "but it was used in a class level line other than level 1");
		return false;
	}

	public String getTokenName()
	{
		return wrappedToken.getTokenName();
	}

	public int compatibilityLevel()
	{
		return 5;
	}

	public int compatibilityPriority()
	{
		return priority;
	}

	public int compatibilitySubLevel()
	{
		return 14;
	}

}