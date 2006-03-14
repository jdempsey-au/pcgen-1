/*
 * Created on Sep 2, 2005
 *
 */
package plugin.lsttokens;

import pcgen.core.PCClass;
import pcgen.core.PObject;
import pcgen.persistence.lst.GlobalLstToken;

/**
 * @author djones4
 *
 */
public class HitdieLst implements GlobalLstToken {

	public String getTokenName() {
		return "HITDIE";
	}

	public boolean parse(PObject obj, String value, int anInt) {
		if(obj instanceof PCClass) {
			((PCClass)obj).putHitDieLock(value, anInt);
			return true;
		}
		return false;
	}
}

