/*
 * Copyright (c) 2007 Tom Parker <thpr@users.sourceforge.net>
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
package plugin.lsttokens.template;

import org.junit.Test;

import pcgen.base.formula.DividingFormula;
import pcgen.cdom.content.HitDie;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.Type;
import pcgen.cdom.modifier.HitDieFormula;
import pcgen.cdom.modifier.HitDieLock;
import pcgen.cdom.modifier.HitDieStep;
import pcgen.core.PCClass;
import pcgen.core.PCTemplate;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;
import plugin.lsttokens.testsupport.ConsolidationRule;

public class HitDieTokenTest extends AbstractTokenTestCase<PCTemplate>
{

	static HitdieToken token = new HitdieToken();
	static CDOMTokenLoader<PCTemplate> loader = new CDOMTokenLoader<PCTemplate>(
			PCTemplate.class);

	@Override
	public Class<PCTemplate> getCDOMClass()
	{
		return PCTemplate.class;
	}

	@Override
	public CDOMLoader<PCTemplate> getLoader()
	{
		return loader;
	}

	@Override
	public CDOMPrimaryToken<PCTemplate> getToken()
	{
		return token;
	}

	@Test
	public void testInvalidInputTooManyLimits()
			throws PersistenceLayerException
	{
		assertFalse(parse("15|CLASS=Fighter|CLASS.TYPE=Base"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputNotALimit() throws PersistenceLayerException
	{
		assertFalse(parse("15|PRECLASS:1,Fighter"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputEmptyLimit() throws PersistenceLayerException
	{
		assertFalse(parse("15|CLASS="));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputEmptyTypeLimit()
			throws PersistenceLayerException
	{
		assertFalse(parse("15|CLASS.TYPE="));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputStartDotTypeLimit()
			throws PersistenceLayerException
	{
		assertFalse(parse("15|CLASS.TYPE=.Strange"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputEndDotTypeLimit()
			throws PersistenceLayerException
	{
		assertFalse(parse("15|CLASS.TYPE=Strange."));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputDoubleDotTypeLimit()
			throws PersistenceLayerException
	{
		assertFalse(parse("15|CLASS.TYPE=Prestige..Strange"));
		assertNoSideEffects();
	}

	@Test
	public void testValidInputDivideNegative() throws PersistenceLayerException
	{
		assertFalse(parse("%/-2"));
		assertNoSideEffects();
	}

	@Test
	public void testValidInputDivideZero() throws PersistenceLayerException
	{
		assertFalse(parse("%/0"));
		assertNoSideEffects();
	}

	@Test
	public void testValidInputDivide() throws PersistenceLayerException
	{
		assertTrue(parse("%/4"));
	}

	@Test
	public void testInvalidInputAddNegative() throws PersistenceLayerException
	{
		assertFalse(parse("%+-3"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputAddZero() throws PersistenceLayerException
	{
		assertFalse(parse("%+0"));
		assertNoSideEffects();
	}

	@Test
	public void testValidInputAdd() throws PersistenceLayerException
	{
		assertTrue(parse("%+4"));
	}

	@Test
	public void testInvalidInputMultiplyNegative()
			throws PersistenceLayerException
	{
		assertFalse(parse("%*-3"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputMultiplyZero() throws PersistenceLayerException
	{
		assertFalse(parse("%*0"));
		assertNoSideEffects();
	}

	@Test
	public void testValidInputMultiply() throws PersistenceLayerException
	{
		assertTrue(parse("%*4"));
	}

	@Test
	public void testInvalidInputSubtractNegative()
			throws PersistenceLayerException
	{
		assertFalse(parse("%--3"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputSubtractZero() throws PersistenceLayerException
	{
		assertFalse(parse("%-0"));
		assertNoSideEffects();
	}

	@Test
	public void testValidInputSubtract() throws PersistenceLayerException
	{
		assertTrue(parse("%-4"));
	}

	@Test
	public void testInvalidInputUpNegative() throws PersistenceLayerException
	{
		assertFalse(parse("%up-3"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputUpZero() throws PersistenceLayerException
	{
		assertFalse(parse("%up0"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputUpTooBig() throws PersistenceLayerException
	{
		assertFalse(parse("%up5"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputUpReallyTooBig()
			throws PersistenceLayerException
	{
		assertFalse(parse("%up15"));
		assertNoSideEffects();
	}

	@Test
	public void testValidInputUp() throws PersistenceLayerException
	{
		assertTrue(parse("%up4"));
	}

	@Test
	public void testInvalidInputHUpNegative() throws PersistenceLayerException
	{
		assertFalse(parse("%Hup-3"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputHUpZero() throws PersistenceLayerException
	{
		assertFalse(parse("%Hup0"));
		assertNoSideEffects();
	}

	@Test
	public void testValidInputHUp() throws PersistenceLayerException
	{
		assertTrue(parse("%Hup4"));
	}

	@Test
	public void testInvalidInputDownNegative() throws PersistenceLayerException
	{
		assertFalse(parse("%down-3"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputDownZero() throws PersistenceLayerException
	{
		assertFalse(parse("%down0"));
		assertNoSideEffects();
	}

	@Test
	public void testValidInputDown() throws PersistenceLayerException
	{
		assertTrue(parse("%down4"));
	}

	@Test
	public void testInvalidInputDownTooBig() throws PersistenceLayerException
	{
		assertFalse(parse("%down5"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputDownReallyTooBig()
			throws PersistenceLayerException
	{
		assertFalse(parse("%down15"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputHdownNegative()
			throws PersistenceLayerException
	{
		assertFalse(parse("%Hdown-3"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputHdownZero() throws PersistenceLayerException
	{
		assertFalse(parse("%Hdown0"));
		assertNoSideEffects();
	}

	@Test
	public void testValidInputHdown() throws PersistenceLayerException
	{
		assertTrue(parse("%Hdown4"));
	}

	@Test
	public void testInvalidInputNegative() throws PersistenceLayerException
	{
		assertFalse(parse("-3"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputZero() throws PersistenceLayerException
	{
		assertFalse(parse("0"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputDecimal() throws PersistenceLayerException
	{
		assertFalse(parse("3.5"));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputMisspell() throws PersistenceLayerException
	{
		assertFalse(parse("%upn5"));
		assertNoSideEffects();
	}

	@Test
	public void testRoundRobinInteger() throws PersistenceLayerException
	{
		runRoundRobin("2");
	}

	@Test
	public void testRoundRobinIntegerClass() throws PersistenceLayerException
	{
		primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		runRoundRobin("2|CLASS=Fighter");
	}

	@Test
	public void testRoundRobinIntegerType() throws PersistenceLayerException
	{
		PCClass a = primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		a.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		PCClass c = secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		c.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		runRoundRobin("2|CLASS.TYPE=Base");
	}

	@Test
	public void testRoundRobinAdd() throws PersistenceLayerException
	{
		runRoundRobin("%+2");
	}

	@Test
	public void testRoundRobinAddClass() throws PersistenceLayerException
	{
		primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		runRoundRobin("%+2|CLASS=Fighter");
	}

	@Test
	public void testRoundRobinAddType() throws PersistenceLayerException
	{
		PCClass a = primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		a.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		PCClass c = secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		c.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		runRoundRobin("%+2|CLASS.TYPE=Base");
	}

	@Test
	public void testRoundRobinSubtract() throws PersistenceLayerException
	{
		runRoundRobin("%-2");
	}

	@Test
	public void testRoundRobinSubtractClass() throws PersistenceLayerException
	{
		primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		runRoundRobin("%-2|CLASS=Fighter");
	}

	@Test
	public void testRoundRobinSubtractType() throws PersistenceLayerException
	{
		PCClass a = primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		a.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		PCClass c = secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		c.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		runRoundRobin("%-2|CLASS.TYPE=Base");
	}

	@Test
	public void testRoundRobinMultiply() throws PersistenceLayerException
	{
		runRoundRobin("%*2");
	}

	@Test
	public void testRoundRobinMultiplyClass() throws PersistenceLayerException
	{
		primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		runRoundRobin("%*2|CLASS=Fighter");
	}

	@Test
	public void testRoundRobinMultiplyType() throws PersistenceLayerException
	{
		PCClass a = primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		a.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		PCClass c = secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		c.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		runRoundRobin("%*2|CLASS.TYPE=Base");
	}

	@Test
	public void testRoundRobinDivide() throws PersistenceLayerException
	{
		runRoundRobin("%/2");
	}

	@Test
	public void testRoundRobinDivideClass() throws PersistenceLayerException
	{
		primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		runRoundRobin("%/2|CLASS=Fighter");
	}

	@Test
	public void testRoundRobinDivideType() throws PersistenceLayerException
	{
		PCClass a = primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		a.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		PCClass c = secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		c.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		runRoundRobin("%/2|CLASS.TYPE=Base");
	}

	@Test
	public void testRoundRobinUp() throws PersistenceLayerException
	{
		runRoundRobin("%up2");
	}

	@Test
	public void testRoundRobinUpClass() throws PersistenceLayerException
	{
		primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		runRoundRobin("%up2|CLASS=Fighter");
	}

	@Test
	public void testRoundRobinUpType() throws PersistenceLayerException
	{
		PCClass a = primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		a.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		PCClass c = secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		c.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		runRoundRobin("%up2|CLASS.TYPE=Base");
	}

	@Test
	public void testRoundRobinHup() throws PersistenceLayerException
	{
		runRoundRobin("%Hup2");
	}

	@Test
	public void testRoundRobinHupClass() throws PersistenceLayerException
	{
		primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		runRoundRobin("%Hup2|CLASS=Fighter");
	}

	@Test
	public void testRoundRobinHupType() throws PersistenceLayerException
	{
		PCClass a = primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		a.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		PCClass c = secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		c.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		runRoundRobin("%Hup2|CLASS.TYPE=Base");
	}

	@Test
	public void testRoundRobinDown() throws PersistenceLayerException
	{
		runRoundRobin("%down2");
	}

	@Test
	public void testRoundRobinDownClass() throws PersistenceLayerException
	{
		primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		runRoundRobin("%down2|CLASS=Fighter");
	}

	@Test
	public void testRoundRobinDownType() throws PersistenceLayerException
	{
		PCClass a = primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		a.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		PCClass c = secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		c.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		runRoundRobin("%down2|CLASS.TYPE=Base");
	}

	@Test
	public void testRoundRobinHdown() throws PersistenceLayerException
	{
		runRoundRobin("%Hdown2");
	}

	@Test
	public void testRoundRobinHdownClass() throws PersistenceLayerException
	{
		primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		runRoundRobin("%Hdown2|CLASS=Fighter");
	}

	@Test
	public void testRoundRobinHdownType() throws PersistenceLayerException
	{
		PCClass a = primaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		a.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		PCClass c = secondaryContext.getReferenceContext().constructCDOMObject(PCClass.class, "Fighter");
		c.addToListFor(ListKey.TYPE, Type.getConstant("Base"));
		runRoundRobin("%Hdown2|CLASS.TYPE=Base");
	}

	@Override
	protected String getAlternateLegalValue()
	{
		return "%Hdown2";
	}

	@Override
	protected String getLegalValue()
	{
		return "%*2|CLASS.TYPE=Base";
	}

	@Test
	public void testUnparseNull() throws PersistenceLayerException
	{
		primaryProf.put(ObjectKey.HITDIE, null);
		assertNull(getToken().unparse(primaryContext, primaryProf));
	}

	@Test
	public void testUnparseLegal() throws PersistenceLayerException
	{
		primaryProf.put(ObjectKey.HITDIE, new HitDieLock(new HitDie(1)));
		expectSingle(getToken().unparse(primaryContext, primaryProf), "1");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUnparseGenericsFail() throws PersistenceLayerException
	{
		ObjectKey objectKey = ObjectKey.HITDIE;
		primaryProf.put(objectKey, new Object());
		try
		{
			getToken().unparse(primaryContext, primaryProf);
			fail();
		}
		catch (ClassCastException e)
		{
			//Yep!
		}
	}

	@Override
	protected ConsolidationRule getConsolidationRule()
	{
		return ConsolidationRule.OVERWRITE;
	}

	@Test
	public void testUnparseZeroSteps() throws PersistenceLayerException
	{
		try
		{
			primaryProf.put(ObjectKey.HITDIE,
					new HitDieStep(0, new HitDie(12)));
			assertBadUnparse();
		}
		catch (IllegalArgumentException e)
		{
			// Good here too :)
		}
	}

	@Test
	public void testUnparseNegativeLevel() throws PersistenceLayerException
	{
		try
		{
			primaryProf.put(ObjectKey.HITDIE, new HitDieLock(new HitDie(-1)));
			assertBadUnparse();
		}
		catch (IllegalArgumentException e)
		{
			// Good here too :)
		}
	}

	@Test
	public void testUnparseZeroDivide() throws PersistenceLayerException
	{
		try
		{
			primaryProf.put(ObjectKey.HITDIE, new HitDieFormula(
					new DividingFormula(0)));
			assertBadUnparse();
		}
		catch (IllegalArgumentException e)
		{
			// Good here too :)
		}
	}

	/*
	 * TODO Need to find owner for this responsibility
	 */
	// @Test
	// public void testUnparseNegativeDivide() throws PersistenceLayerException
	// {
	// try
	// {
	// primaryProf.put(ObjectKey.HITDIE, new HitDieFormula(new
	// DividingFormula(-3)));
	// assertBadUnparse();
	// }
	// catch (IllegalArgumentException e)
	// {
	// //Good here too :)
	// }
	// }
	//
	// @Test
	// public void testUnparseZeroMult() throws PersistenceLayerException
	// {
	// try
	// {
	// primaryProf.put(ObjectKey.HITDIE, new HitDieFormula(new
	// MultiplyingFormula(0)));
	// assertBadUnparse();
	// }
	// catch (IllegalArgumentException e)
	// {
	// //Good here too :)
	// }
	// }
	//
	// @Test
	// public void testUnparseNegativeMult() throws PersistenceLayerException
	// {
	// try
	// {
	// primaryProf.put(ObjectKey.HITDIE, new HitDieFormula(new
	// MultiplyingFormula(-3)));
	// assertBadUnparse();
	// }
	// catch (IllegalArgumentException e)
	// {
	// //Good here too :)
	// }
	// }
	//
	// @Test
	// public void testUnparseZeroAdd() throws PersistenceLayerException
	// {
	// try
	// {
	// primaryProf.put(ObjectKey.HITDIE, new HitDieFormula(new
	// AddingFormula(0)));
	// assertBadUnparse();
	// }
	// catch (IllegalArgumentException e)
	// {
	// //Good here too :)
	// }
	// }
	//
	// @Test
	// public void testUnparseNegativeAdd() throws PersistenceLayerException
	// {
	// try
	// {
	// primaryProf.put(ObjectKey.HITDIE, new HitDieFormula(new
	// AddingFormula(-3)));
	// assertBadUnparse();
	// }
	// catch (IllegalArgumentException e)
	// {
	// //Good here too :)
	// }
	// }
	//
	// @Test
	// public void testUnparseZeroSub() throws PersistenceLayerException
	// {
	// try
	// {
	// primaryProf.put(ObjectKey.HITDIE, new HitDieFormula(new
	// SubtractingFormula(0)));
	// assertBadUnparse();
	// }
	// catch (IllegalArgumentException e)
	// {
	// //Good here too :)
	// }
	// }
	//
	// @Test
	// public void testUnparseNegativeSub() throws PersistenceLayerException
	// {
	// try
	// {
	// primaryProf.put(ObjectKey.HITDIE, new HitDieFormula(new
	// SubtractingFormula(-3)));
	// assertBadUnparse();
	// }
	// catch (IllegalArgumentException e)
	// {
	// //Good here too :)
	// }
	// }
	//
	// @Test
	// public void testUnparseBigSteps() throws PersistenceLayerException
	// {
	// try
	// {
	// primaryProf.put(ObjectKey.HITDIE, new HitDieStep(8, new HitDie(12)));
	// assertBadUnparse();
	// }
	// catch (IllegalArgumentException e)
	// {
	// //Good here too :)
	// }
	// }
	//
	// @Test
	// public void testUnparseBigNegativeSteps() throws
	// PersistenceLayerException
	// {
	// try
	// {
	// primaryProf.put(ObjectKey.HITDIE, new HitDieStep(-8, new HitDie(12)));
	// assertBadUnparse();
	// }
	// catch (IllegalArgumentException e)
	// {
	// //Good here too :)
	// }
	// }
	//
	// @Test
	// public void testUnparseBadBase() throws PersistenceLayerException
	// {
	// try
	// {
	// primaryProf.put(ObjectKey.HITDIE, new HitDieStep(1, new HitDie(6)));
	//			assertBadUnparse();
	//		}
	//		catch (IllegalArgumentException e)
	//		{
	//			//Good here too :)
	//		}
	//	}
}
