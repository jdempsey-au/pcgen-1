package plugin.lsttokens.deprecated;

import java.util.StringTokenizer;

import pcgen.core.PObject;
import pcgen.core.Race;
import pcgen.persistence.lst.DeprecatedToken;
import pcgen.persistence.lst.RaceLstToken;

/**
 * Class deals with HITDICE Token
 */
public class HitdiceToken implements RaceLstToken, DeprecatedToken
{

	public String getTokenName()
	{
		return "HITDICE";
	}

	public boolean parse(Race race, String value)
	{
		try
		{
			final StringTokenizer hitdice = new StringTokenizer(value, ",");

			if (hitdice.countTokens() != 2)
			{
				return false;
			}
			race.setHitDice(Integer.parseInt(hitdice.nextToken()));
			race.setHitDiceSize(Integer.parseInt(hitdice.nextToken()));
			return true;
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
	}

	public String getMessage(PObject obj, String value)
	{
		return getTokenName()
			+ " is deprecated, because Default Monster Mode is deprecated";
	}
}
