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

import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.Region;
import pcgen.core.PCTemplate;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractTypeSafeTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class RegionTokenTest extends AbstractTypeSafeTokenTestCase<PCTemplate>
{

	static RegionToken token = new RegionToken();
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

	@Override
	public Object getConstant(String string)
	{
		return Region.getConstant(string);
	}

	@Override
	public ObjectKey<?> getObjectKey()
	{
		return ObjectKey.REGION;
	}

	@Override
	protected boolean requiresPreconstruction()
	{
		return false;
	}

	@Test
	public void dummyTest()
	{
		// Just to get Eclipse to recognize this as a JUnit 4.0 Test Case
	}

	@Override
	public boolean isClearLegal()
	{
		return false;
	}

	@Test
	public void testRoundRobinYes() throws PersistenceLayerException
	{
		runRoundRobin("YES");
	}

	@Test
	public void testReplacementYes() throws PersistenceLayerException
	{
		String[] unparsed;
		if (requiresPreconstruction())
		{
			getConstant("TestWP1");
		}
		if (isClearLegal())
		{
			assertTrue(parse("YES"));
			unparsed = getToken().unparse(primaryContext, primaryProf);
			assertEquals(1, unparsed.length);
			assertEquals("Expected item to be equal", "TestWP2", unparsed[0]);
		}
		assertTrue(parse("TestWP1"));
		unparsed = getToken().unparse(primaryContext, primaryProf);
		assertEquals(1, unparsed.length);
		assertEquals("Expected item to be equal", "TestWP1", unparsed[0]);
		if (isClearLegal())
		{
			assertTrue(parse("YES"));
			unparsed = getToken().unparse(primaryContext, primaryProf);
			assertEquals(1, unparsed.length);
			assertEquals("Expected item to be equal", "YES", unparsed[0]);
		}
	}

}
