/*
 * Copyright (c) 2009 Tom Parker <thpr@users.sourceforge.net>
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
 */
package pcgen.cdom.facet.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;

import pcgen.cdom.base.FormulaFactory;
import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.enumeration.DataSetID;
import pcgen.cdom.enumeration.FormulaKey;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.facet.BonusCheckingFacet;
import pcgen.cdom.facet.FormulaResolvingFacet;
import pcgen.cdom.facet.analysis.LevelFacet;
import pcgen.core.Globals;
import pcgen.core.PCTemplate;
import pcgen.core.Race;
import pcgen.core.SettingsHandler;
import pcgen.core.SizeAdjustment;
import pcgen.rules.context.AbstractReferenceContext;

public class SizeFacetTest extends TestCase
{
	/*
	 * NOTE: This is not literal unit testing - it is leveraging the existing
	 * RaceFacet and TemplateFacet frameworks. This class trusts that
	 * RaceFacetTest and TemplateFacetTest has fully vetted RaceFacet and
	 * TemplateFacet. PLEASE ensure all tests there are working before
	 * investigating tests here.
	 */
	private CharID id;
	private CharID altid;
	private SizeFacet facet;
	private RaceFacet rfacet = new RaceFacet();
	private TemplateFacet tfacet = new TemplateFacet();
	private int fakeLevels = 0;
	private Map<CharID, Double> bonusInfo;
	private static boolean staticDone = false;
	private static SizeAdjustment t, s, m, l, h;

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		DataSetID cid = DataSetID.getID();
		id = CharID.getID(cid);
		altid = CharID.getID(cid);
		facet = getMockFacet();
		facet.setRaceFacet(rfacet);
		facet.setTemplateFacet(tfacet);
		facet.setFormulaResolvingFacet(new FormulaResolvingFacet());
		bonusInfo = new HashMap<CharID, Double>();
		staticSetUp();
	}

	private static synchronized void staticSetUp()
	{
		if (!staticDone)
		{
			SettingsHandler.getGame().clearLoadContext();
			AbstractReferenceContext ref = Globals.getContext().getReferenceContext();
			t = ref.constructCDOMObject(SizeAdjustment.class, "Tiny");
			ref.registerAbbreviation(t, "T");
			s = ref.constructCDOMObject(SizeAdjustment.class, "Small");
			ref.registerAbbreviation(s, "S");
			m = ref.constructCDOMObject(SizeAdjustment.class, "Medium");
			ref.registerAbbreviation(m, "M");
			m.put(ObjectKey.IS_DEFAULT_SIZE, true);
			l = ref.constructCDOMObject(SizeAdjustment.class, "Large");
			ref.registerAbbreviation(l, "L");
			h = ref.constructCDOMObject(SizeAdjustment.class, "Huge");
			ref.registerAbbreviation(h, "H");
			staticDone = true;
		}
	}

	@Test
	public void testReachUnsetDefault()
	{
		assertEquals(2, facet.sizeInt(id));
		assertEquals(2, facet.racialSizeInt(id));
	}

	@Test
	public void testWithNothingInRaceDefaultsTo2()
	{
		rfacet.set(id, new Race());
		facet.update(id);
		assertEquals(2, facet.sizeInt(id));
		assertEquals(2, facet.racialSizeInt(id));
		rfacet.remove(id);
		facet.update(id);
		assertEquals(2, facet.sizeInt(id));
		assertEquals(2, facet.racialSizeInt(id));
	}

	@Test
	public void testAvoidPollution()
	{
		Race r = new Race();
		r.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(3));
		rfacet.set(id, r);
		facet.update(id);
		assertEquals(2, facet.sizeInt(altid));
		assertEquals(2, facet.racialSizeInt(altid));
	}

	@Test
	public void testGetFromRace()
	{
		Race r = new Race();
		r.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(3));
		rfacet.set(id, r);
		facet.update(id);
		assertEquals(3, facet.sizeInt(id));
	}

	@Test
	public void testGetFromTemplateLowerOverridesDefault()
	{
		rfacet.set(id, new Race());
		PCTemplate t1 = new PCTemplate();
		t1.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(1));
		tfacet.add(id, t1, this);
		facet.update(id);
		assertEquals(1, facet.sizeInt(id));
		tfacet.remove(id, t1, this);
		facet.update(id);
		assertEquals(2, facet.sizeInt(id));
	}

	@Test
	public void testGetFromTemplateHigherOverridesDefault()
	{
		rfacet.set(id, new Race());
		PCTemplate t1 = new PCTemplate();
		t1.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(3));
		tfacet.add(id, t1, this);
		facet.update(id);
		assertEquals(3, facet.sizeInt(id));
		tfacet.remove(id, t1, this);
		facet.update(id);
		assertEquals(2, facet.sizeInt(id));
	}

	@Test
	public void testGetFromTemplateLowerOverridesRace()
	{
		Race r = new Race();
		r.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(3));
		rfacet.set(id, r);
		PCTemplate t1 = new PCTemplate();
		t1.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(1));
		tfacet.add(id, t1, this);
		facet.update(id);
		assertEquals(1, facet.sizeInt(id));
		tfacet.remove(id, t1, this);
		facet.update(id);
		assertEquals(3, facet.sizeInt(id));
	}

	@Test
	public void testGetFromTemplateHigherOverridesRace()
	{
		Race r = new Race();
		r.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(3));
		rfacet.set(id, r);
		PCTemplate t1 = new PCTemplate();
		t1.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(4));
		tfacet.add(id, t1, this);
		facet.update(id);
		assertEquals(4, facet.sizeInt(id));
		tfacet.remove(id, t1, this);
		facet.update(id);
		assertEquals(3, facet.sizeInt(id));
	}

	@Test
	public void testGetFromTemplateSecondOverrides()
	{
		Race r = new Race();
		r.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(1));
		rfacet.set(id, r);
		PCTemplate t1 = new PCTemplate();
		t1.setName("PCT");
		t1.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(3));
		tfacet.add(id, t1, this);
		PCTemplate t2 = new PCTemplate();
		t2.setName("Other");
		t2.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(4));
		tfacet.add(id, t2, this);
		facet.update(id);
		assertEquals(4, facet.sizeInt(id));
		tfacet.remove(id, t2, this);
		facet.update(id);
		assertEquals(3, facet.sizeInt(id));
		tfacet.remove(id, t1, this);
		facet.update(id);
		assertEquals(1, facet.sizeInt(id));
	}

	@Test
	public void testGetWithBonus()
	{
		Race r = new Race();
		r.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(1));
		rfacet.set(id, r);
		facet.update(id);
		assertEquals(1, facet.sizeInt(id));
		bonusInfo.put(altid, 2.0);
		// No pollution
		facet.update(id);
		assertEquals(1, facet.sizeInt(id));
		bonusInfo.put(id, 2.0);
		facet.update(id);
		assertEquals(3, facet.sizeInt(id));
		PCTemplate t1 = new PCTemplate();
		t1.setName("PCT");
		t1.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(0));
		tfacet.add(id, t1, this);
		facet.update(id);
		assertEquals(2, facet.sizeInt(id));
		PCTemplate t2 = new PCTemplate();
		t2.setName("Other");
		t2.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(3));
		tfacet.add(id, t2, this);
		facet.update(id);
		assertEquals(4, facet.sizeInt(id));
		tfacet.remove(id, t2, this);
		facet.update(id);
		assertEquals(2, facet.sizeInt(id));
		bonusInfo.clear();
		facet.update(id);
		assertEquals(0, facet.sizeInt(id));
	}

	@Test
	public void testGetWithLevelProgression()
	{
		Race r = new Race();
		r.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(1));
		rfacet.set(id, r);
		facet.update(id);
		assertEquals(1, facet.sizeInt(id));
		fakeLevels = 6;
		facet.update(id);
		assertEquals(1, facet.sizeInt(id));
		r.addToListFor(ListKey.HITDICE_ADVANCEMENT, 2);
		facet.update(id);
		assertEquals(1, facet.sizeInt(id));
		r.addToListFor(ListKey.HITDICE_ADVANCEMENT, Integer.MAX_VALUE);
		facet.update(id);
		assertEquals(2, facet.sizeInt(id));
		r.removeListFor(ListKey.HITDICE_ADVANCEMENT);
		r.addToListFor(ListKey.HITDICE_ADVANCEMENT, 2);
		r.addToListFor(ListKey.HITDICE_ADVANCEMENT, 5);
		r.addToListFor(ListKey.HITDICE_ADVANCEMENT, 6);
		facet.update(id);
		assertEquals(3, facet.sizeInt(id));
		PCTemplate t1 = new PCTemplate();
		t1.setName("PCT");
		t1.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(0));
		tfacet.add(id, t1, this);
		facet.update(id);
		assertEquals(2, facet.sizeInt(id));
		PCTemplate t2 = new PCTemplate();
		t2.setName("Other");
		t2.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(3));
		tfacet.add(id, t2, this);
		facet.update(id);
		assertEquals(4, facet.sizeInt(id));
		tfacet.remove(id, t2, this);
		facet.update(id);
		assertEquals(2, facet.sizeInt(id));
		r.removeListFor(ListKey.HITDICE_ADVANCEMENT);
		facet.update(id);
		assertEquals(0, facet.sizeInt(id));
	}

	@Test
	public void testGetObjectWithBonus()
	{
		assertEquals(m, facet.getSizeAdjustment(id));
		facet.update(id);
		assertEquals(m, facet.getSizeAdjustment(id));
		Race r = new Race();
		r.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(1));
		rfacet.set(id, r);
		facet.update(id);
		assertEquals(s, facet.getSizeAdjustment(id));
		bonusInfo.put(altid, 2.0);
		// No pollution
		facet.update(id);
		assertEquals(s, facet.getSizeAdjustment(id));
		bonusInfo.put(id, 2.0);
		facet.update(id);
		assertEquals(l, facet.getSizeAdjustment(id));
		PCTemplate t1 = new PCTemplate();
		t1.setName("PCT");
		t1.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(0));
		tfacet.add(id, t1, this);
		facet.update(id);
		assertEquals(m, facet.getSizeAdjustment(id));
		PCTemplate t2 = new PCTemplate();
		t2.setName("Other");
		t2.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(3));
		tfacet.add(id, t2, this);
		facet.update(id);
		assertEquals(h, facet.getSizeAdjustment(id));
		tfacet.remove(id, t2, this);
		facet.update(id);
		assertEquals(m, facet.getSizeAdjustment(id));
		bonusInfo.put(id, -2.0);
		t1.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(1));
		facet.update(id);
		assertEquals(t, facet.getSizeAdjustment(id));
		t2.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(4));
		tfacet.add(id, t2, this);
		facet.update(id);
		assertEquals(m, facet.getSizeAdjustment(id));
		tfacet.remove(id, t2, this);
		facet.update(id);
		assertEquals(t, facet.getSizeAdjustment(id));
		bonusInfo.clear();
		facet.update(id);
		assertEquals(s, facet.getSizeAdjustment(id));
	}

	@Test
	public void testGetAbbWithBonus()
	{
		assertEquals("M", facet.getSizeAbb(id));
		facet.update(id);
		assertEquals("M", facet.getSizeAbb(id));
		Race r = new Race();
		r.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(1));
		rfacet.set(id, r);
		facet.update(id);
		assertEquals("S", facet.getSizeAbb(id));
		bonusInfo.put(altid, 2.0);
		// No pollution
		facet.update(id);
		assertEquals("S", facet.getSizeAbb(id));
		bonusInfo.put(id, 2.0);
		facet.update(id);
		assertEquals("L", facet.getSizeAbb(id));
		PCTemplate t1 = new PCTemplate();
		t1.setName("PCT");
		t1.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(0));
		tfacet.add(id, t1, this);
		facet.update(id);
		assertEquals("M", facet.getSizeAbb(id));
		PCTemplate t2 = new PCTemplate();
		t2.setName("Other");
		t2.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(3));
		tfacet.add(id, t2, this);
		facet.update(id);
		assertEquals("H", facet.getSizeAbb(id));
		tfacet.remove(id, t2, this);
		facet.update(id);
		assertEquals("M", facet.getSizeAbb(id));
		bonusInfo.put(id, -2.0);
		t1.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(1));
		facet.update(id);
		assertEquals("T", facet.getSizeAbb(id));
		t2.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(4));
		tfacet.add(id, t2, this);
		facet.update(id);
		assertEquals("M", facet.getSizeAbb(id));
		tfacet.remove(id, t2, this);
		facet.update(id);
		assertEquals("T", facet.getSizeAbb(id));
		bonusInfo.clear();
		facet.update(id);
		assertEquals("S", facet.getSizeAbb(id));
	}

	@Test
	public void testGetObjectWithLevelProgression()
	{
		Race r = new Race();
		r.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(1));
		rfacet.set(id, r);
		facet.update(id);
		assertEquals(s, facet.getSizeAdjustment(id));
		fakeLevels = 6;
		facet.update(id);
		assertEquals(s, facet.getSizeAdjustment(id));
		r.addToListFor(ListKey.HITDICE_ADVANCEMENT, 2);
		facet.update(id);
		assertEquals(s, facet.getSizeAdjustment(id));
		r.addToListFor(ListKey.HITDICE_ADVANCEMENT, Integer.MAX_VALUE);
		facet.update(id);
		assertEquals(m, facet.getSizeAdjustment(id));
		r.removeListFor(ListKey.HITDICE_ADVANCEMENT);
		r.addToListFor(ListKey.HITDICE_ADVANCEMENT, 2);
		r.addToListFor(ListKey.HITDICE_ADVANCEMENT, 5);
		r.addToListFor(ListKey.HITDICE_ADVANCEMENT, 6);
		facet.update(id);
		assertEquals(l, facet.getSizeAdjustment(id));
		PCTemplate t1 = new PCTemplate();
		t1.setName("PCT");
		t1.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(0));
		tfacet.add(id, t1, this);
		facet.update(id);
		assertEquals(m, facet.getSizeAdjustment(id));
		PCTemplate t2 = new PCTemplate();
		t2.setName("Other");
		t2.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(3));
		tfacet.add(id, t2, this);
		facet.update(id);
		assertEquals(h, facet.getSizeAdjustment(id));
		tfacet.remove(id, t2, this);
		facet.update(id);
		assertEquals(m, facet.getSizeAdjustment(id));
		r.removeListFor(ListKey.HITDICE_ADVANCEMENT);
		facet.update(id);
		assertEquals(t, facet.getSizeAdjustment(id));
	}

	@Test
	public void testGetAbbWithLevelProgression()
	{
		Race r = new Race();
		r.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(1));
		rfacet.set(id, r);
		facet.update(id);
		assertEquals("S", facet.getSizeAbb(id));
		fakeLevels = 6;
		facet.update(id);
		assertEquals("S", facet.getSizeAbb(id));
		r.addToListFor(ListKey.HITDICE_ADVANCEMENT, 2);
		facet.update(id);
		assertEquals("S", facet.getSizeAbb(id));
		r.addToListFor(ListKey.HITDICE_ADVANCEMENT, Integer.MAX_VALUE);
		facet.update(id);
		assertEquals("M", facet.getSizeAbb(id));
		r.removeListFor(ListKey.HITDICE_ADVANCEMENT);
		r.addToListFor(ListKey.HITDICE_ADVANCEMENT, 2);
		r.addToListFor(ListKey.HITDICE_ADVANCEMENT, 5);
		r.addToListFor(ListKey.HITDICE_ADVANCEMENT, 6);
		facet.update(id);
		assertEquals("L", facet.getSizeAbb(id));
		PCTemplate t1 = new PCTemplate();
		t1.setName("PCT");
		t1.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(0));
		tfacet.add(id, t1, this);
		facet.update(id);
		assertEquals("M", facet.getSizeAbb(id));
		PCTemplate t2 = new PCTemplate();
		t2.setName("Other");
		t2.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(3));
		tfacet.add(id, t2, this);
		facet.update(id);
		assertEquals("H", facet.getSizeAbb(id));
		tfacet.remove(id, t2, this);
		facet.update(id);
		assertEquals("M", facet.getSizeAbb(id));
		r.removeListFor(ListKey.HITDICE_ADVANCEMENT);
		facet.update(id);
		assertEquals("T", facet.getSizeAbb(id));
	}

	@Test
	public void testGetWithNegativeBonus()
	{
		assertEquals(2, facet.sizeInt(id));
		assertEquals(2, facet.racialSizeInt(id));
		Race r = new Race();
		r.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(3));
		rfacet.set(id, r);
		facet.update(id);
		assertEquals(3, facet.sizeInt(id));
		assertEquals(3, facet.racialSizeInt(id));
		bonusInfo.put(altid, -2.0);
		// No pollution
		facet.update(id);
		assertEquals(3, facet.sizeInt(id));
		assertEquals(3, facet.racialSizeInt(id));
		bonusInfo.put(id, -2.0);
		facet.update(id);
		assertEquals(1, facet.sizeInt(id));
		assertEquals(3, facet.racialSizeInt(id));
		PCTemplate t1 = new PCTemplate();
		t1.setName("PCT");
		t1.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(1));
		tfacet.add(id, t1, this);
		facet.update(id);
		assertEquals(0, facet.sizeInt(id));
		assertEquals(1, facet.racialSizeInt(id));
		PCTemplate t2 = new PCTemplate();
		t2.setName("Other");
		t2.put(FormulaKey.SIZE, FormulaFactory.getFormulaFor(4));
		tfacet.add(id, t2, this);
		facet.update(id);
		assertEquals(2, facet.sizeInt(id));
		assertEquals(4, facet.racialSizeInt(id));
		tfacet.remove(id, t2, this);
		facet.update(id);
		assertEquals(0, facet.sizeInt(id));
		assertEquals(1, facet.racialSizeInt(id));
		bonusInfo.clear();
		facet.update(id);
		assertEquals(1, facet.sizeInt(id));
		assertEquals(1, facet.racialSizeInt(id));
	}
	
	/**
	 * Verify the function of the sizesAdvanced method.
	 */
	public void testSizesAdvanced()
	{
		Race race = new Race();
		race.setName("Test Race");
		
		// Validate that there are no size changes if no advancement is specified
		assertEquals("Size increase where none specified wrong", 0, facet.sizesToAdvance(race, 1));
		assertEquals("Size increase where none specified wrong", 0, facet.sizesToAdvance(race, 2));
		assertEquals("Size increase where none specified wrong", 0, facet.sizesToAdvance(race, 3));
		assertEquals("Size increase where none specified wrong", 0, facet.sizesToAdvance(race, 4));
		assertEquals("Size increase where none specified wrong", 0, facet.sizesToAdvance(race, 5));

		// Validate that size changes occur when needed and no extra happen if advancement is specified
		race.addToListFor(ListKey.HITDICE_ADVANCEMENT, 2);
		race.addToListFor(ListKey.HITDICE_ADVANCEMENT, 4);
		assertEquals("Size increase pre first change wrong", 0, facet.sizesToAdvance(race, 1));
		assertEquals("Size increase pre first change wrong", 0, facet.sizesToAdvance(race, 2));
		assertEquals("Size increase pre last change wrong", 1, facet.sizesToAdvance(race, 3));
		assertEquals("Size increase pre last change wrong", 1, facet.sizesToAdvance(race, 4));
		assertEquals("Size increase post last change wrong", 1, facet.sizesToAdvance(race, 5));
		assertEquals("Size increase post last change wrong", 1, facet.sizesToAdvance(race, 6));
	}

	public SizeFacet getMockFacet() throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException
	{
		SizeFacet f = new SizeFacet();
		Field field = SizeFacet.class.getDeclaredField("bonusCheckingFacet");
		field.setAccessible(true);
		BonusCheckingFacet fakeFacet = new BonusCheckingFacet()
		{

			@Override
			public double getBonus(CharID cid, String bonusType,
					String bonusName)
			{
				if ("SIZEMOD".equals(bonusType) && "NUMBER".equals(bonusName))
				{
					Double d = bonusInfo.get(cid);
					return d == null ? 0 : d;
				}
				return 0;
			}

		};
		field.set(f, fakeFacet);
		field = SizeFacet.class.getDeclaredField("levelFacet");
		field.setAccessible(true);
		LevelFacet fakeLevelFacet = new LevelFacet()
		{

			@Override
			public int getMonsterLevelCount(CharID cid)
			{
				return fakeLevels;
			}

		};
		field.set(f, fakeLevelFacet);
		return f;
	}
}
