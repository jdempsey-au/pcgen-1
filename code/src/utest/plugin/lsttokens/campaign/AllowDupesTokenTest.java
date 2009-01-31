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
package plugin.lsttokens.campaign;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import pcgen.core.Campaign;
import pcgen.persistence.PersistenceLayerException;
import pcgen.rules.context.ConsolidatedListCommitStrategy;
import pcgen.rules.context.GameReferenceContext;
import pcgen.rules.context.RuntimeLoadContext;
import pcgen.rules.persistence.CDOMLoader;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import plugin.lsttokens.testsupport.AbstractTokenTestCase;
import plugin.lsttokens.testsupport.CDOMTokenLoader;

public class AllowDupesTokenTest extends AbstractTokenTestCase<Campaign>
{

	static CDOMPrimaryToken<Campaign> token = new AllowDupesToken();
	static CDOMTokenLoader<Campaign> loader = new CDOMTokenLoader<Campaign>(
			Campaign.class);

	@Override
	@Before
	public void setUp() throws PersistenceLayerException, URISyntaxException
	{
		super.setUp();
		primaryContext = new RuntimeLoadContext(new GameReferenceContext(),
				new ConsolidatedListCommitStrategy());
		secondaryContext = new RuntimeLoadContext(new GameReferenceContext(),
				new ConsolidatedListCommitStrategy());
		URI testURI = testCampaign.getURI();
		primaryContext.getObjectContext().setSourceURI(testURI);
		primaryContext.getObjectContext().setExtractURI(testURI);
		secondaryContext.getObjectContext().setSourceURI(testURI);
		secondaryContext.getObjectContext().setExtractURI(testURI);
		primaryProf = getPrimary("TestObj");
		secondaryProf = getSecondary("TestObj");
		expectedPrimaryMessageCount = 0;
	}

	@Override
	public CDOMLoader<Campaign> getLoader()
	{
		return loader;
	}

	@Override
	public Class<Campaign> getCDOMClass()
	{
		return Campaign.class;
	}

	@Override
	public CDOMPrimaryToken<Campaign> getToken()
	{
		return token;
	}

	@Test
	public void testInvalidEmpty() throws PersistenceLayerException
	{
		assertFalse(parse(""));
		assertNoSideEffects();
	}

	@Test
	public void testInvalidType() throws PersistenceLayerException
	{
		assertFalse(parse("SKILL"));
		assertNoSideEffects();
	}

	@Test
	public void testRoundRobinSpell() throws PersistenceLayerException
	{
		runRoundRobin("SPELL");
	}

	@Test
	public void testRoundRobinLanguage() throws PersistenceLayerException
	{
		runRoundRobin("LANGUAGE");
	}

	@Test
	public void testRoundRobinBoth() throws PersistenceLayerException
	{
		runRoundRobin("LANGUAGE", "SPELL");
	}
}