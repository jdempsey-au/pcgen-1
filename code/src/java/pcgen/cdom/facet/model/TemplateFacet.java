/*
 * Copyright (c) Thomas Parker, 2012.
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
package pcgen.cdom.facet.model;

import pcgen.cdom.facet.AbstractSourcedListFacet;
import pcgen.cdom.facet.DataFacetChangeEvent;
import pcgen.cdom.facet.DataFacetChangeListener;
import pcgen.core.PCTemplate;

/**
 * TemplateFacet is a Facet that tracks all PCTemplates that have been granted
 * to a Player Character.
 */
public class TemplateFacet extends AbstractSourcedListFacet<PCTemplate>
		implements DataFacetChangeListener<PCTemplate>
{
	/**
	 * Adds the active PCTemplate to this facet.
	 * 
	 * Triggered when one of the Facets to which TemplateFacet listens fires a
	 * DataFacetChangeEvent to indicate PCTemplate was added to a Player
	 * Character.
	 * 
	 * @param dfce
	 *            The DataFacetChangeEvent containing the information about the
	 *            change
	 * 
	 * @see pcgen.cdom.facet.DataFacetChangeListener#dataAdded(pcgen.cdom.facet.DataFacetChangeEvent)
	 */
	@Override
	public void dataAdded(DataFacetChangeEvent<PCTemplate> dfce)
	{
		add(dfce.getCharID(), dfce.getCDOMObject(), dfce.getSource());
	}

	/**
	 * Removes the no-longer active PCTemplate from this facet.
	 * 
	 * Triggered when one of the Facets to which TemplateFacet listens fires a
	 * DataFacetChangeEvent to indicate PCTemplate was removed from a Player
	 * Character.
	 * 
	 * @param dfce
	 *            The DataFacetChangeEvent containing the information about the
	 *            change
	 * 
	 * @see pcgen.cdom.facet.DataFacetChangeListener#dataRemoved(pcgen.cdom.facet.DataFacetChangeEvent)
	 */
	@Override
	public void dataRemoved(DataFacetChangeEvent<PCTemplate> dfce)
	{
		remove(dfce.getCharID(), dfce.getCDOMObject(), dfce.getSource());
	}

}
