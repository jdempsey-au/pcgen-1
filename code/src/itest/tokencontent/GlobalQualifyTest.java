/*
 * Copyright (c) 2012 Tom Parker <thpr@users.sourceforge.net>
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
package tokencontent;

import org.junit.Test;

import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.facet.FacetLibrary;
import pcgen.cdom.facet.analysis.QualifyFacet;
import pcgen.core.Race;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.persistence.token.CDOMToken;
import pcgen.rules.persistence.token.ParseResult;
import plugin.lsttokens.QualifyToken;
import tokencontent.testsupport.AbstractContentTokenTest;

public class GlobalQualifyTest extends AbstractContentTokenTest
{

	private QualifyToken token = new QualifyToken();
	private QualifyFacet foFacet;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		foFacet = FacetLibrary.getFacet(QualifyFacet.class);
		create(Race.class, "Dwarf");
	}

	@Override
	public void processToken(CDOMObject source)
	{
		ParseResult result = token.parseToken(context, source, "RACE|Dwarf");
		if (result != ParseResult.SUCCESS)
		{
			result.printMessages();
			fail("Test Setup Failed");
		}
		finishLoad();
	}

	@Override
	public CDOMToken<?> getToken()
	{
		return token;
	}

	@Override
	protected boolean containsExpected()
	{
		/*
		 * TODO This indicates that FollowerOptionFacet is not really pure
		 * content - it is doing filtering as well
		 */
		Race dwarf =
				context.ref.silentlyGetConstructedCDOMObject(Race.class,
					"Dwarf");
		return foFacet.grantsQualify(id, dwarf);
	}

	@Override
	protected int targetFacetCount()
	{
		return foFacet.getCount(id);
	}

	@Override
	protected int baseCount()
	{
		return 0;
	}

	@Override
	@Test
	public void testFromAlignment() throws PersistenceLayerException
	{
		//Not supported here today
	}

	@Override
	@Test
	public void testFromCampaign() throws PersistenceLayerException
	{
		//Not supported here today
	}

	@Override
	@Test
	public void testFromCompanionMod() throws PersistenceLayerException
	{
		//Not supported here today
	}

	@Override
	@Test
	public void testFromEqMod() throws PersistenceLayerException
	{
		//Not supported here today
	}
}
