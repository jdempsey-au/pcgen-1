package pcgen.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pcgen.core.Ability;
import pcgen.core.Deity;
import pcgen.core.Domain;
import pcgen.core.Equipment;
import pcgen.core.EquipmentModifier;
import pcgen.core.Language;
import pcgen.core.PCClass;
import pcgen.core.PCTemplate;
import pcgen.core.PObject;
import pcgen.core.Race;
import pcgen.core.Skill;
import pcgen.core.WeaponProf;
import pcgen.core.spell.Spell;

public class StringPClassUtil {

	private static Map<String, Class<? extends PObject>> classMap;
	private static Map<Class<? extends PObject>, String> stringMap;
	
	static {
		classMap = new HashMap<String, Class<? extends PObject>>();
		stringMap = new HashMap<Class<? extends PObject>, String>();
		
		classMap.put("DEITY", Deity.class);
		classMap.put("DOMAIN", Domain.class);
		classMap.put("EQUIPMENT", Equipment.class);
		classMap.put("EQMOD", EquipmentModifier.class);
		classMap.put("FEAT", Ability.class);
		classMap.put("CLASS", PCClass.class);
		classMap.put("LANGUAGE", Language.class);
		classMap.put("RACE", Race.class);
		classMap.put("SPELL", Spell.class);
		classMap.put("SKILL", Skill.class);
		classMap.put("TEMPLATE", PCTemplate.class);
		classMap.put("WEAPONPROF", WeaponProf.class);
		
		stringMap.put(Deity.class, "DEITY");
		stringMap.put(Domain.class, "DOMAIN");
		stringMap.put(Equipment.class, "EQUIPMENT");
		stringMap.put(EquipmentModifier.class, "EQMOD");
		stringMap.put(Ability.class, "FEAT");
		stringMap.put(PCClass.class, "CLASS");
		stringMap.put(Language.class, "LANGUAGE");
		stringMap.put(Race.class, "RACE");
		stringMap.put(Spell.class, "SPELL");
		stringMap.put(Skill.class, "SKILL");
		stringMap.put(PCTemplate.class, "TEMPLATE");
		stringMap.put(WeaponProf.class, "WEAPONPROF");
	}
	
	public static Class<? extends PObject> getClassFor(String key) {
		return classMap.get(key);
	}
	
	public static Set<String> getValidStrings() {
		return classMap.keySet();
	}

	public static String getStringFor(Class<?> cl) {
		return stringMap.get(cl);
	}

}
