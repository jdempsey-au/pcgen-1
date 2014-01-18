/*
 * Copyright (c) Thomas Parker, 2014.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */
package pcgen.cdom.facet.analysis;

import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.enumeration.SkillCost;
import pcgen.cdom.facet.base.AbstractSubScopeFacet;
import pcgen.cdom.facet.event.DataFacetChangeEvent;
import pcgen.cdom.facet.event.DataFacetChangeListener;
import pcgen.cdom.facet.input.MonsterCSkillFacet;
import pcgen.cdom.facet.model.ClassFacet;
import pcgen.core.PCClass;
import pcgen.core.Skill;

/**
 * @author Thomas Parker (thpr [at] yahoo.com)
 */
public class MonCSkillToSkillCostFacet extends
		AbstractSubScopeFacet<PCClass, SkillCost, Skill> implements
		DataFacetChangeListener<PCClass>
{

	private ClassFacet classFacet;

	private MonsterCSkillFacet monsterCSkillFacet;

	public void skillAdded(DataFacetChangeEvent<Skill> dfce)
	{
		CharID id = dfce.getCharID();
		SkillCost cost = SkillCost.CLASS;
		Skill sk = dfce.getCDOMObject();
		Object source = dfce.getSource();
		for (PCClass cl : classFacet.getClassSet(id))
		{
			if (cl.isMonster())
			{
				add(id, cl, cost, sk, source);
			}
		}
	}

	public void skillRemoved(DataFacetChangeEvent<Skill> dfce)
	{
		CharID id = dfce.getCharID();
		SkillCost cost = SkillCost.CLASS;
		Skill sk = dfce.getCDOMObject();
		Object source = dfce.getSource();
		for (PCClass cl : classFacet.getClassSet(id))
		{
			if (cl.isMonster())
			{
				remove(id, cl, cost, sk, source);
			}
		}
	}

	public void dataAdded(DataFacetChangeEvent<PCClass> dfce)
	{
		PCClass cl = dfce.getCDOMObject();
		if (cl.isMonster())
		{
			CharID id = dfce.getCharID();
			SkillCost cost = SkillCost.CLASS;
			for (Skill sk : monsterCSkillFacet.getSet(id))
			{
				add(id, cl, cost, sk, monsterCSkillFacet);
			}
		}
	}

	public void dataRemoved(DataFacetChangeEvent<PCClass> dfce)
	{
		PCClass cl = dfce.getCDOMObject();
		if (cl.isMonster())
		{
			CharID id = dfce.getCharID();
			SkillCost cost = SkillCost.CLASS;
			for (Skill sk : monsterCSkillFacet.getSet(id))
			{
				remove(id, cl, cost, sk, monsterCSkillFacet);
			}
		}
	}

	public void setMonsterCSkillFacet(MonsterCSkillFacet monsterCSkillFacet)
	{
		this.monsterCSkillFacet = monsterCSkillFacet;
	}

	public void setClassFacet(ClassFacet classFacet)
	{
		this.classFacet = classFacet;
	}

	public void init()
	{
		classFacet.addDataFacetChangeListener(this);
		monsterCSkillFacet.addDataFacetChangeListener(new SkillListener());
	}

	private class SkillListener implements DataFacetChangeListener<Skill>
	{

		public void dataAdded(DataFacetChangeEvent<Skill> dfce)
		{
			skillAdded(dfce);
		}

		public void dataRemoved(DataFacetChangeEvent<Skill> dfce)
		{
			skillRemoved(dfce);
		}
	}
}
