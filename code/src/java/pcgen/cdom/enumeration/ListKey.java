/*
 * Copyright 2005 (C) Tom Parker <thpr@users.sourceforge.net>
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
 * Created on June 18, 2005.
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 */
package pcgen.cdom.enumeration;

import java.net.URI;

import pcgen.base.formula.Formula;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.TransitionChoice;
import pcgen.cdom.content.ChangeProf;
import pcgen.cdom.content.KnownSpellIdentifier;
import pcgen.cdom.content.LevelCommandFactory;
import pcgen.cdom.helper.Aspect;
import pcgen.cdom.helper.AttackCycle;
import pcgen.cdom.helper.FollowerLimit;
import pcgen.cdom.helper.PointCost;
import pcgen.cdom.helper.Qualifier;
import pcgen.cdom.helper.Quality;
import pcgen.cdom.helper.StatLock;
import pcgen.cdom.list.ClassSkillList;
import pcgen.cdom.modifier.ChangeArmorType;
import pcgen.cdom.reference.CDOMSingleRef;
import pcgen.core.Ability;
import pcgen.core.DamageReduction;
import pcgen.core.Deity;
import pcgen.core.Description;
import pcgen.core.Domain;
import pcgen.core.Equipment;
import pcgen.core.EquipmentModifier;
import pcgen.core.FollowerOption;
import pcgen.core.Kit;
import pcgen.core.Language;
import pcgen.core.Movement;
import pcgen.core.PCClass;
import pcgen.core.PCStat;
import pcgen.core.PCTemplate;
import pcgen.core.QualifiedObject;
import pcgen.core.SpecialAbility;
import pcgen.core.SpecialProperty;
import pcgen.core.SpellProhibitor;
import pcgen.core.Vision;
import pcgen.core.WeaponProf;
import pcgen.core.bonus.BonusObj;
import pcgen.persistence.lst.CampaignSourceEntry;

/**
 * @author Tom Parker <thpr@users.sourceforge.net>
 * 
 * This is a Typesafe enumeration of legal List Characteristics of an object. It
 * is designed to act as an index to a specific Object items within a
 * CDOMObject.
 * 
 * ListKeys are designed to store items in a CDOMObject in a type-safe
 * fashion. Note that it is possible to use the ListKey to cast the object to
 * the type of object stored by the ListKey. (This assists with Generics)
 * 
 * @param <T>
 *            The class of object stored by this ListKey.
 */
public final class ListKey<T> {

	public static final ListKey<Aspect> ASPECT = new ListKey<Aspect>();
	/** CLASS_SKILLS - a ListKey */
	public static final ListKey<String> CLASS_SKILLS = new ListKey<String>();
	/** CROSS_CLASS_SKILLS - a ListKey */
	public static final ListKey<String> CROSS_CLASS_SKILLS = new ListKey<String>();
	/** FILE_ABILITY_CATEGORY - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_ABILITY_CATEGORY = new ListKey<CampaignSourceEntry>();
	/** FILE_BIO_SET - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_BIO_SET = new ListKey<CampaignSourceEntry>();
	/** FILE_CLASS - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_CLASS = new ListKey<CampaignSourceEntry>();
	/** FILE_CLASS_SKILL - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_CLASS_SKILL = new ListKey<CampaignSourceEntry>();
	/** FILE_CLASS_SPELL - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_CLASS_SPELL = new ListKey<CampaignSourceEntry>();
	/** FILE_COMPANION_MOD - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_COMPANION_MOD = new ListKey<CampaignSourceEntry>();
	/** FILE_COVER - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_COVER = new ListKey<CampaignSourceEntry>();
	/** FILE_DEITY - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_DEITY = new ListKey<CampaignSourceEntry>();
	/** FILE_DOMAIN - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_DOMAIN = new ListKey<CampaignSourceEntry>();
	/** FILE_EQUIP - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_EQUIP = new ListKey<CampaignSourceEntry>();
	/** FILE_EQUIP_MOD - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_EQUIP_MOD = new ListKey<CampaignSourceEntry>();
	/** FILE_ABILITY - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_ABILITY = new ListKey<CampaignSourceEntry>();
	/** FILE_FEAT - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_FEAT = new ListKey<CampaignSourceEntry>();
	/** FILE_KIT - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_KIT = new ListKey<CampaignSourceEntry>();
	/** FILE_LANGUAGE - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_LANGUAGE = new ListKey<CampaignSourceEntry>();
	/** FILE_LST_EXCLUDE - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_LST_EXCLUDE = new ListKey<CampaignSourceEntry>();
	/** FILE_PCC - a ListKey */
	public static final ListKey<URI> FILE_PCC = new ListKey<URI>();
	/** FILE_RACE - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_RACE = new ListKey<CampaignSourceEntry>();
	/** FILE_REQ_SKILL - a ListKey */
	public static final ListKey<String> FILE_REQ_SKILL = new ListKey<String>();
	/** FILE_SKILL - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_SKILL = new ListKey<CampaignSourceEntry>();
	/** FILE_SPELL - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_SPELL = new ListKey<CampaignSourceEntry>();
	/** FILE_TEMPLATE - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_TEMPLATE = new ListKey<CampaignSourceEntry>();
	/** FILE_WEAPON_PROF - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_WEAPON_PROF = new ListKey<CampaignSourceEntry>();
	/** GAME_MODE - a ListKey */
	public static final ListKey<String> GAME_MODE = new ListKey<String>();
	/** LICENSE - a ListKey */
	public static final ListKey<String> LICENSE = new ListKey<String>();
	/** LICENSE_FILE - a ListKey */
	public static final ListKey<URI> LICENSE_FILE = new ListKey<URI>();
	/** LINE - a ListKey */
	public static final ListKey<String> LINE = new ListKey<String>();
	/** LOGO - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_LOGO = new ListKey<CampaignSourceEntry>();
	/** PANTHEON - a ListKey */
	public static final ListKey<Pantheon> PANTHEON = new ListKey<Pantheon>();
	/** RACE_PANTHEON - a ListKey */
	public static final ListKey<String> RACEPANTHEON = new ListKey<String>();
	/** REMOVE_STRING_LIST - a ListKey */
	public static final ListKey<String> REMOVE_STRING_LIST = new ListKey<String>();
	/** SAVE - a ListKey */
	public static final ListKey<String> SAVE = new ListKey<String>();
	/** SECTION 15 - a ListKey */
	public static final ListKey<String> SECTION_15 = new ListKey<String>();
	/** SELECTED_ARMOR_PROFS - a ListKey */
	public static final ListKey<String> SELECTED_ARMOR_PROF = new ListKey<String>();
	/** SELECTED_WEAPON_PROF_BONUS - a ListKey */
	public static final ListKey<String> SELECTED_WEAPON_PROF_BONUS = new ListKey<String>();
	/** SPECIAL_ABILITY - a ListKey */
	public static final ListKey<SpecialAbility> SPECIAL_ABILITY = new ListKey<SpecialAbility>();
	/** TEMP_BONUS - a ListKey */
	public static final ListKey<BonusObj> TEMP_BONUS = new ListKey<BonusObj>();
	/** UDAM - a ListKey */
	public static final ListKey<String> UDAM = new ListKey<String>();
	/** Key for a list of virtual feats (feats granted regardless of the prereqs) */
	public static final ListKey<Ability> VIRTUAL_FEATS = new ListKey<Ability>();
//	/** Key for a list of weapon proficiencies */
//	public static final ListKey<String> WEAPON_PROF = new ListKey<String>();
	public static final ListKey<CampaignSourceEntry> FILE_ARMOR_PROF = new ListKey<CampaignSourceEntry>();
	public static final ListKey<CampaignSourceEntry> FILE_SHIELD_PROF = new ListKey<CampaignSourceEntry>();
	public static final ListKey<CDOMReference<WeaponProf>> DEITYWEAPON = new ListKey<CDOMReference<WeaponProf>>();
	public static final ListKey<CDOMReference<ClassSkillList>> CLASSES = new ListKey<CDOMReference<ClassSkillList>>();
	public static final ListKey<CDOMReference<ClassSkillList>> PREVENTED_CLASSES = new ListKey<CDOMReference<ClassSkillList>>();
	public static final ListKey<RaceSubType> RACESUBTYPE = new ListKey<RaceSubType>();
	public static final ListKey<RaceSubType> REMOVED_RACESUBTYPE = new ListKey<RaceSubType>();
	public static final ListKey<LevelCommandFactory> ADD_LEVEL = new ListKey<LevelCommandFactory>();
	public static final ListKey<String> RANGE = new ListKey<String>();
	public static final ListKey<String> SAVE_INFO = new ListKey<String>();
	public static final ListKey<String> DURATION = new ListKey<String>();
	public static final ListKey<String> COMPONENTS = new ListKey<String>();
	public static final ListKey<String> CASTTIME = new ListKey<String>();
	public static final ListKey<String> SPELL_RESISTANCE = new ListKey<String>();
	public static final ListKey<String> VARIANTS = new ListKey<String>();
	public static final ListKey<String> SPELL_SCHOOL = new ListKey<String>();
	public static final ListKey<String> SPELL_SUBSCHOOL = new ListKey<String>();
	public static final ListKey<String> SPELL_DESCRIPTOR = new ListKey<String>();
	public static final ListKey<String> PROHIBITED_ITEM = new ListKey<String>();
	public static final ListKey<String> ITEM = new ListKey<String>();
	public static final ListKey<Integer> HITDICE_ADVANCEMENT = new ListKey<Integer>();
	public static final ListKey<String> ITEM_TYPES = new ListKey<String>();
	public static final ListKey<CDOMSingleRef<EquipmentModifier>> REPLACED_KEYS = new ListKey<CDOMSingleRef<EquipmentModifier>>();
	public static final ListKey<SpecialProperty> SPECIAL_PROPERTIES = new ListKey<SpecialProperty>();
	public static final ListKey<ChangeArmorType> ARMORTYPE = new ListKey<ChangeArmorType>();
	public static final ListKey<Formula> SPECIALTYKNOWN = new ListKey<Formula>();
	public static final ListKey<Formula> KNOWN = new ListKey<Formula>();
	public static final ListKey<Formula> CAST = new ListKey<Formula>();
	public static final ListKey<QualifiedObject<CDOMSingleRef<Domain>>> DOMAIN = new ListKey<QualifiedObject<CDOMSingleRef<Domain>>>();
	public static final ListKey<CDOMReference<Deity>> DEITY = new ListKey<CDOMReference<Deity>>();
	public static final ListKey<PointCost> SPELL_POINT_COST = new ListKey<PointCost>();
	public static final ListKey<AttackCycle> ATTACK_CYCLE = new ListKey<AttackCycle>();
	public static final ListKey<KnownSpellIdentifier> KNOWN_SPELLS = new ListKey<KnownSpellIdentifier>();
	public static final ListKey<SpellProhibitor> SPELL_PROHIBITOR = new ListKey<SpellProhibitor>();
	public static final ListKey<Quality> QUALITY = new ListKey<Quality>();
	public static final ListKey<Description> BENEFIT = new ListKey<Description>();
	public static final ListKey<CDOMReference<Language>> AUTO_LANGUAGES = new ListKey<CDOMReference<Language>>();
	public static final ListKey<PCTemplate> LEVEL_TEMPLATES = new ListKey<PCTemplate>();
	public static final ListKey<PCTemplate> REPEATLEVEL_TEMPLATES = new ListKey<PCTemplate>();
	public static final ListKey<PCTemplate> HD_TEMPLATES = new ListKey<PCTemplate>();
	public static final ListKey<CDOMReference<PCTemplate>> TEMPLATE_CHOOSE = new ListKey<CDOMReference<PCTemplate>>();
	public static final ListKey<CDOMReference<PCTemplate>> TEMPLATE_ADDCHOICE = new ListKey<CDOMReference<PCTemplate>>();
	public static final ListKey<CDOMReference<PCTemplate>> TEMPLATE = new ListKey<CDOMReference<PCTemplate>>();
	public static final ListKey<CDOMReference<PCTemplate>> REMOVE_TEMPLATES = new ListKey<CDOMReference<PCTemplate>>();
	public static final ListKey<Vision> VISION_CACHE = new ListKey<Vision>();
	public static final ListKey<TransitionChoice<?>> ADD = new ListKey<TransitionChoice<?>>();
	public static final ListKey<CDOMReference<? extends PCClass>> FAVORED_CLASS = new ListKey<CDOMReference<? extends PCClass>>();
	public static final ListKey<Qualifier> QUALIFY = new ListKey<Qualifier>();
	public static final ListKey<DamageReduction> DAMAGE_REDUCTION = new ListKey<DamageReduction>();
	public static final ListKey<PCStat> UNLOCKED_STATS = new ListKey<PCStat>();
	public static final ListKey<StatLock> STAT_LOCKS = new ListKey<StatLock>();
	public static final ListKey<TransitionChoice<Kit>> KIT_CHOICE = new ListKey<TransitionChoice<Kit>>();
	public static final ListKey<Movement> MOVEMENT = new ListKey<Movement>();
	public static final ListKey<FollowerOption> COMPANIONLIST = new ListKey<FollowerOption>();
	public static final ListKey<FollowerLimit> FOLLOWERS = new ListKey<FollowerLimit>();
	public static final ListKey<Description> DESCRIPTION = new ListKey<Description>();
	public static final ListKey<ChangeProf> CHANGEPROF = new ListKey<ChangeProf>();
	public static final ListKey<Equipment> NATURAL_WEAPON = new ListKey<Equipment>();
	public static final ListKey<SpecialAbility> SAB = new ListKey<SpecialAbility>();

	/** Private constructor to prevent instantiation of this class */
	private ListKey() {
		//Only allow instantation here
	}

	public T cast(Object o)
	{
		return (T) o;
	}
}
