/*
 * Created on Sep 2, 2005
 *
 */
package plugin.lsttokens;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import pcgen.base.formula.Formula;
import pcgen.cdom.base.Constants;
import pcgen.cdom.enumeration.FormulaKey;
import pcgen.cdom.enumeration.IntegerKey;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.StringKey;
import pcgen.cdom.inst.EquipmentHead;
import pcgen.core.Equipment;
import pcgen.core.Globals;
import pcgen.core.PCTemplate;
import pcgen.core.PObject;
import pcgen.core.Race;
import pcgen.core.SettingsHandler;
import pcgen.core.SizeAdjustment;
import pcgen.core.WeaponProf;
import pcgen.persistence.lst.GlobalLstToken;
import pcgen.rules.context.AbstractReferenceContext;
import pcgen.util.Logging;

/**
 * @author djones4
 *
 */
public class NaturalattacksLst implements GlobalLstToken
{
	/*
	 * Note: Don't need to wait for Template's LevelToken before this can be converted
	 * as there is no level support in templates for this token
	 */

	/**
	 * @see pcgen.persistence.lst.LstToken#getTokenName()
	 */
	public String getTokenName()
	{
		return "NATURALATTACKS"; //$NON-NLS-1$
	}

	/**
	 * @see pcgen.persistence.lst.GlobalLstToken#parse(pcgen.core.PObject, java.lang.String, int)
	 */
	public boolean parse(PObject obj, String value, int anInt)
	{
		// first entry is primary, others are secondary
		// lets try the format:
		// NATURALATTACKS:primary weapon name,num attacks,damage|secondary1 weapon
		// name,num attacks,damage|secondary2.....
		// damage will be of the form XdY+Z or XdY-Z
		List<Equipment> naturalWeapons = parseNaturalAttacks(obj, value);
		for (Equipment weapon : naturalWeapons)
		{
			obj.addNaturalWeapon(weapon, anInt);
		}
		return true;
	}

	/**
	 * NATURAL WEAPONS CODE <p/>first natural weapon is primary, the rest are
	 * secondary; NATURALATTACKS:primary weapon name,weapon type,num
	 * attacks,damage|secondary1 weapon name,weapon type,num
	 * attacks,damage|secondary2 format is exactly as it would be in an equipment
	 * lst file Type is of the format Weapon.Natural.Melee.Bludgeoning number of
	 * attacks is the number of attacks with that weapon at BAB (for primary), or
	 * BAB - 5 (for secondary)
	 * @param obj
	 * @param aString
	 * @return List
	 */
	private static List<Equipment> parseNaturalAttacks(PObject obj,
		String aString)
	{
		// Currently, this isn't going to work with monk attacks
		// - their unarmed stuff won't be affected.
		String aSize = "M";

		if (obj instanceof PCTemplate || obj instanceof Race)
		{
			/*
			 * TODO This is actually broken - This is accidentally(?) order
			 * dependent, meaning if SIZE appears after NATURALATTACKS this will
			 * have bad problems vs. if it appears before. This order dependence
			 * should be removed, but that really requires the Formula for Size
			 * be projected into both Race and Equipment
			 */
			Formula f = obj.get(FormulaKey.SIZE);
			if (f != null)
			{
				aSize = f.toString();
			}
		}

		if (aSize == null)
		{
			aSize = "M";
		}

		int count = 1;
		boolean onlyOne = false;

		final StringTokenizer attackTok = new StringTokenizer(aString, "|");

		// Make a preliminary guess at whether this is an "only" attack
		if (attackTok.countTokens() == 1)
		{
			onlyOne = true;
		}

		// This is wrong as we need to replace old natural weapons
		// with "better" ones
		List<Equipment> naturalWeapons = new ArrayList<Equipment>();

		while (attackTok.hasMoreTokens())
		{
			StringTokenizer aTok =
					new StringTokenizer(attackTok.nextToken(), ",");
			Equipment anEquip = createNaturalWeapon(aTok, aSize);

			if (anEquip != null)
			{
				if (count == 1)
				{
					anEquip.setModifiedName("Natural/Primary");
				}
				else
				{
					anEquip.setModifiedName("Natural/Secondary");
				}

				if (onlyOne && anEquip.isOnlyNaturalWeapon())
				{
					anEquip.setOnlyNaturalWeapon(true);
				}
				else
				{
					anEquip.setOnlyNaturalWeapon(false);
				}

				anEquip.setOutputIndex(0);
				anEquip.setOutputSubindex(count);
				naturalWeapons.add(anEquip);
			}

			count++;
		}
		return naturalWeapons;
	}

	/**
	 * Create the Natural weapon equipment item aTok = primary weapon name,weapon
	 * type,num attacks,damage for Example:
	 * Tentacle,Weapon.Natural.Melee.Slashing,*4,1d6
	 * @param aTok
	 * @param aSize
	 * @return natural weapon
	 */
	private static Equipment createNaturalWeapon(StringTokenizer aTok,
		String aSize)
	{
		final String attackName = aTok.nextToken();

		if (attackName.equalsIgnoreCase(Constants.s_NONE))
		{
			return null;
		}

		Equipment anEquip = new Equipment();
		final String profType = aTok.nextToken();

		anEquip.setName(attackName);
		anEquip.setTypeInfo(profType);
		anEquip.put(ObjectKey.WEIGHT, BigDecimal.ZERO);
		if (aSize.length() > 1) {
			aSize = aSize.toUpperCase().substring(0, 1);
		}
		
		SizeAdjustment sa = SettingsHandler.getGame().getSizeAdjustmentNamed(aSize);
		anEquip.put(ObjectKey.SIZE, sa);
		anEquip.put(ObjectKey.BASESIZE, sa);

		String numAttacks = aTok.nextToken();
		boolean attacksProgress = true;

		if ((numAttacks.length() > 0) && (numAttacks.charAt(0) == '*'))
		{
			numAttacks = numAttacks.substring(1);
			attacksProgress = false;
		}

		int bonusAttacks = 0;

		try
		{
			bonusAttacks = Integer.parseInt(numAttacks) - 1;
		}
		catch (NumberFormatException exc)
		{
			Logging.errorPrint("Non-numeric value for number of attacks: '"
				+ numAttacks + "'");
		}

		if (bonusAttacks > 0)
		{
			anEquip.addBonusList("WEAPON|ATTACKS|" + bonusAttacks);
			anEquip.setOnlyNaturalWeapon(false);
		}
		else
		{
			anEquip.setOnlyNaturalWeapon(true);
		}

		EquipmentHead head = anEquip.getEquipmentHead(1);
		head.put(StringKey.DAMAGE, aTok.nextToken());
		head.put(IntegerKey.CRIT_RANGE, 1);
		head.put(IntegerKey.CRIT_MULT, 2);
		AbstractReferenceContext ref = Globals.getContext().ref;
		ref.constructIfNecessary(WeaponProf.class, attackName);
		anEquip.put(ObjectKey.WEAPON_PROF, ref.getCDOMReference(WeaponProf.class, attackName));

		// sage_sam 02 Dec 2002 for Bug #586332
		// allow hands to be required to equip natural weapons
		int handsRequired = 0;

		if (aTok.hasMoreTokens())
		{
			final String hString = aTok.nextToken();

			try
			{
				handsRequired = Integer.parseInt(hString);
			}
			catch (NumberFormatException exc)
			{
				Logging.errorPrint("Non-numeric value for hands required: '"
					+ hString + "'");
			}
		}

		anEquip.put(IntegerKey.SLOTS, handsRequired);

		//these values need to be locked.
		anEquip.setQty(new Float(1));
		anEquip.setNumberCarried(new Float(1));
		anEquip.put(ObjectKey.ATTACKS_PROGRESS, attacksProgress);

		// Check if the proficiency needs created
		WeaponProf prof = Globals.getContext().ref.silentlyGetConstructedCDOMObject(WeaponProf.class, attackName);

		if (prof == null)
		{
			prof = new WeaponProf();
			prof.setTypeInfo(profType);
			prof.setName(attackName);
			prof.setKeyName(attackName);
			Globals.getContext().ref.importObject(prof);
		}

		anEquip.addAutoArray("WEAPONPROF", attackName); //$NON-NLS-1$
		return anEquip;
	}
}
