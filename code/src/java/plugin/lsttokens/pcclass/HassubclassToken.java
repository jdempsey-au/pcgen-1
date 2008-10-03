package plugin.lsttokens.pcclass;

import pcgen.core.PCClass;
import pcgen.persistence.lst.PCClassLstToken;

/**
 * Class deals with HASSUBCLASS Token
 */
public class HassubclassToken implements PCClassLstToken
{

	public String getTokenName()
	{
		return "HASSUBCLASS";
	}

	public boolean parse(PCClass pcclass, String value, int level)
	{
		//TODO Need to deprecate this token
		return true;
	}
}
