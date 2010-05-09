/*
 * Copyright (c) Thomas Parker, 2010.
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
package pcgen.cdom.facet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import pcgen.base.util.WrappedMapSet;
import pcgen.cdom.base.Category;
import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.enumeration.Nature;
import pcgen.core.Ability;

/**
 * @author Thomas Parker (thpr [at] yahoo.com)
 * 
 * A GrantedAbilityFacet is a DataFacet that contains information about Ability
 * objects that are contained in a PlayerCharacter
 */
public class GrantedAbilityFacet extends AbstractDataFacet<Ability>
{
	/**
	 * Add the given Ability to the list of Abilities defined by the given
	 * Category and Nature, which is stored in this GrantedAbilityFacet for the
	 * Player Character represented by the given CharID
	 * 
	 * @param id
	 *            The CharID representing the Player Character for which the
	 *            given Ability should be added
	 * @param cat
	 *            The Ability Category identifying the list to which the given
	 *            Ability should be added
	 * @param nat
	 *            The Ability Nature identifying the list to which the given
	 *            Ability should be added
	 * @param obj
	 *            The Ability to be added to the list of Abilities defined by
	 *            the given Category and Nature, which is stored in this
	 *            GrantedAbilityFacet for the Player Character represented by
	 *            the given CharID
	 * @param source
	 *            The source for the given object
	 */
	public void add(CharID id, Category<Ability> cat, Nature nat, Ability obj,
			Object source)
	{
		boolean isNew = ensureCachedSet(id, cat, nat, obj);
		if (getCachedSet(id, cat, nat, obj).add(source) || isNew)
		{
			fireDataFacetChangeEvent(id, obj, DataFacetChangeEvent.DATA_ADDED);
		}
	}

	/**
	 * Adds all of the Abilities in the given Collection to the list of
	 * Abilities defined by the given Category and Nature, which is stored in
	 * this GrantedAbilityFacet for the Player Character represented by the
	 * given CharID
	 * 
	 * @param id
	 *            The CharID representing the Player Character for which the
	 *            given Abilities should be added
	 * @param cat
	 *            The Ability Category identifying the list to which the given
	 *            Abilities should be added
	 * @param nature
	 *            The Ability Nature identifying the list to which the given
	 *            Abilities should be added
	 * @param abilities
	 *            The Collection of Abilities to be added to the list of
	 *            Abilities defined by the given Category and Nature, which is
	 *            stored in this GrantedAbilityFacet for the Player Character
	 *            represented by the given CharID
	 * @throws NullPointerException
	 *             if the given Collection is null
	 */
	public void addAll(CharID id, Category<Ability> cat, Nature nature,
			Collection<Ability> abilities, Object source)
	{
		for (Ability a : abilities)
		{
			add(id, cat, nature, a, source);
		}
	}

	/**
	 * Removes the given Ability from the list of Abilities defined by the given
	 * Category and Nature, which is stored in this GrantedAbilityFacet for the
	 * Player Character represented by the given CharID
	 * 
	 * @param id
	 *            The CharID representing the Player Character from which the
	 *            given Ability should be removed
	 * @param cat
	 *            The Ability Category identifying the list from which the given
	 *            Ability should be removed
	 * @param nat
	 *            The Ability Nature identifying the list from which the given
	 *            Ability should be removed
	 * @param obj
	 *            The Ability to be removed from the list of Abilities defined
	 *            by the given Category and Nature, which is stored in this
	 *            GrantedAbilityFacet for the Player Character represented by
	 *            the given CharID
	 */
	public boolean remove(CharID id, Category<Ability> cat, Nature nat,
			Ability obj)
	{
		Map<Ability, Set<Object>> cached = getCachedMap(id, cat, nat);
		boolean removed = (cached != null) && (cached.remove(obj) != null);
		if (removed)
		{
			fireDataFacetChangeEvent(id, obj, DataFacetChangeEvent.DATA_REMOVED);
		}
		return removed;
	}

	/**
	 * Returns the Set of Abilities in this GrantedAbilityFacet for the Player
	 * Character represented by the given CharID
	 * 
	 * @param id
	 *            The CharID representing the Player Character for which the
	 *            items in this GrantedAbilityFacet should be returned.
	 * @param cat
	 *            The Ability Category identifying the list of Abilities to be
	 *            returned
	 * @param nat
	 *            The Ability Nature identifying the list of Abilities to be
	 *            returned
	 * @return A non-null Set of Abilities in this GrantedAbilityFacet for the
	 *         Player Character represented by the given CharID
	 */
	public Set<Ability> get(CharID id, Category<Ability> cat, Nature nat)
	{
		Map<Ability, Set<Object>> set = getCachedMap(id, cat, nat);
		if (set == null)
		{
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(set.keySet());
	}

	/**
	 * Returns true if this GrantedAbilityFacet contains the given Ability in
	 * the list of items for the Player Character represented by the given
	 * CharID.
	 * 
	 * @param id
	 *            The CharID representing the Player Character used for testing
	 * @param cat
	 *            The Ability Category identifying the list of Abilities to be
	 *            tested to see if it contains the given Ability
	 * @param nat
	 *            The Ability Nature identifying the list of Abilities to be
	 *            tested to see if it contains the given Ability
	 * @param a
	 *            The Ability to test if this GrantedAbilityFacet contains that
	 *            item for the Player Character represented by the given CharID
	 * @return true if this GrantedAbilityFacet contains the given Ability for
	 *         the Player Character represented by the given CharID; false
	 *         otherwise
	 */
	public boolean contains(CharID id, Category<Ability> cat, Nature nat,
			Ability a)
	{
		Map<Ability, Set<Object>> set = getCachedMap(id, cat, nat);
		if (set == null)
		{
			return false;
		}
		if (set.containsKey(a))
		{
			return true;
		}
		/*
		 * TODO Have to support slow method due to cloning issues :(
		 */
		for (Ability ab : set.keySet())
		{
			if (ab.equals(a))
			{
				return true;
			}
		}
		return false;
	}

	private boolean ensureCachedSet(CharID id, Category<Ability> cat,
			Nature nat, Ability obj)
	{
		boolean isNew = false;
		Map<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>> catMap = getCachedMap(id);
		Map<Nature, Map<Ability, Set<Object>>> natureMap = null;
		Map<Ability, Set<Object>> abilityMap = null;
		Set<Object> sourceSet = null;
		if (catMap == null)
		{
			catMap = new HashMap<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>>();
			FacetCache.set(id, getClass(), catMap);
		}
		else
		{
			natureMap = catMap.get(cat);
		}
		if (natureMap == null)
		{
			natureMap = new HashMap<Nature, Map<Ability, Set<Object>>>();
			catMap.put(cat, natureMap);
		}
		else
		{
			abilityMap = natureMap.get(nat);
		}
		if (abilityMap == null)
		{
			abilityMap = new IdentityHashMap<Ability, Set<Object>>();
			natureMap.put(nat, abilityMap);
		}
		else
		{
			sourceSet = abilityMap.get(obj);
		}
		if (sourceSet == null)
		{
			isNew = true;
			sourceSet = new WrappedMapSet<Object>(IdentityHashMap.class);
			abilityMap.put(obj, sourceSet);
		}
		return isNew;
	}

	/**
	 * Returns the type-safe Set for this GrantedAbilityFacet and the given
	 * CharID. May return null if no information has been set in this
	 * GrantedAbilityFacet for the given CharID.
	 * 
	 * Note that this method SHOULD NOT be public. The Set is owned by
	 * GrantedAbilityFacet, and since it can be modified, a reference to that
	 * Set should not be exposed to any object other than GrantedAbilityFacet.
	 * 
	 * @param id
	 *            The CharID for which the Set should be returned
	 * @param cat
	 *            The Ability Category identifying the list of Abilities to be
	 *            returned
	 * @param nat
	 *            The Ability Nature identifying the list of Abilities to be
	 *            returned
	 * @return The Set for the Player Character represented by the given CharID;
	 *         null if no information has been set in this GrantedAbilityFacet
	 *         for the Player Character.
	 */
	private Map<Ability, Set<Object>> getCachedMap(CharID id,
			Category<Ability> cat, Nature nat)
	{
		Map<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>> catMap = getCachedMap(id);
		if (catMap == null)
		{
			return null;
		}
		Map<Nature, Map<Ability, Set<Object>>> natureMap = catMap.get(cat);
		if (natureMap == null)
		{
			return null;
		}
		return natureMap.get(nat);
	}

	private Set<Object> getCachedSet(CharID id, Category<Ability> cat,
			Nature nat, Ability a)
	{
		Map<Ability, Set<Object>> sourceMap = getCachedMap(id, cat, nat);
		if (sourceMap == null)
		{
			return null;
		}
		return sourceMap.get(a);
	}

	/**
	 * Returns the type-safe Map for this GrantedAbilityFacet and the given
	 * CharID. May return null if no information has been set in this
	 * GrantedAbilityFacet for the given CharID.
	 * 
	 * Note that this method SHOULD NOT be public. The Map is owned by
	 * GrantedAbilityFacet, and since it can be modified, a reference to that
	 * Map should not be exposed to any object other than GrantedAbilityFacet.
	 * 
	 * @param id
	 *            The CharID for which the Map should be returned
	 * @return The Map for the Player Character represented by the given CharID;
	 *         null if no information has been set in this GrantedAbilityFacet
	 *         for the Player Character.
	 */
	private Map<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>> getCachedMap(
			CharID id)
	{
		return (Map<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>>) FacetCache
				.get(id, getClass());
	}

	/**
	 * Removes all Abilities from the list of Abilities stored in this
	 * GrantedAbilityFacet for the Player Character represented by the given
	 * CharID
	 * 
	 * @param id
	 *            The CharID representing the Player Character from which all
	 *            Abilities should be removed
	 */
	public void removeAll(CharID id)
	{
		Map<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>> catMap = (Map<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>>) FacetCache
				.remove(id, getClass());
		if (catMap != null)
		{
			for (Map.Entry<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>> catME : catMap
					.entrySet())
			{
				// Category<Ability> cat = catME.getKey();
				Map<Nature, Map<Ability, Set<Object>>> natMap = catME
						.getValue();
				processRemoveNatureMap(id, natMap);
			}
		}
	}

	/**
	 * Removes all of the Ability objects in the given Category from the lists
	 * of Abilities stored in this GrantedAbilityFacet for the Player Character
	 * represented by the given CharID
	 * 
	 * @param id
	 *            The CharID representing the Player Character from which the
	 *            given Abilities should be removed
	 * @param cat
	 *            The Ability Category identifying which Ability objects are to
	 *            be removed from the lists of Abilities stored in this
	 *            GrantedAbilityFacet for the Player Character represented by
	 *            the given CharID
	 * @throws NullPointerException
	 *             if the given Collection is null
	 */
	public void removeAll(CharID id, Category<Ability> cat)
	{
		Map<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>> catMap = getCachedMap(id);
		if (catMap != null)
		{
			Map<Nature, Map<Ability, Set<Object>>> natMap = catMap.remove(cat);
			if (natMap != null)
			{
				processRemoveNatureMap(id, natMap);
			}
		}
	}

	/**
	 * Removes all of the objects of the given Category and Nature from the list
	 * of Abilities stored in this GrantedAbilityFacet for the Player Character
	 * represented by the given CharID
	 * 
	 * @param id
	 *            The CharID representing the Player Character from which the
	 *            given Abilities should be removed
	 * @param cat
	 *            The Ability Category identifying which Ability objects are to
	 *            be removed from the lists of Abilities stored in this
	 *            GrantedAbilityFacet for the Player Character represented by
	 *            the given CharID
	 * @param nature
	 *            The Ability Nature identifying which Ability objects are to be
	 *            removed from the lists of Abilities stored in this
	 *            GrantedAbilityFacet for the Player Character represented by
	 *            the given CharID
	 * @throws NullPointerException
	 *             if the given Collection is null
	 */
	public void removeAll(CharID id, Category<Ability> cat, Nature nature)
	{
		Map<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>> catMap = getCachedMap(id);
		if (catMap != null)
		{
			Map<Nature, Map<Ability, Set<Object>>> natMap = catMap.remove(cat);
			if (natMap != null)
			{
				Map<Ability, Set<Object>> abilitySet = natMap.get(nature);
				if (abilitySet != null)
				{
					processRemoveAbilityMap(id, abilitySet);
				}
			}
		}
	}

	private void processRemoveNatureMap(CharID id,
			Map<Nature, Map<Ability, Set<Object>>> natMap)
	{
		for (Map.Entry<Nature, Map<Ability, Set<Object>>> natME : natMap
				.entrySet())
		{
			// Nature nature = natME.getKey();
			processRemoveAbilityMap(id, natME.getValue());
		}
	}

	/**
	 * Removes all of the Ability objects in the given Nature from the lists of
	 * Abilities stored in this GrantedAbilityFacet for the Player Character
	 * represented by the given CharID
	 * 
	 * @param id
	 *            The CharID representing the Player Character from which the
	 *            given Abilities should be removed
	 * @param nature
	 *            The Ability Nature identifying which Ability objects are to be
	 *            removed from the lists of Abilities stored in this
	 *            GrantedAbilityFacet for the Player Character represented by
	 *            the given CharID
	 * @throws NullPointerException
	 *             if the given Collection is null
	 */
	public void removeAll(CharID id, Nature nature)
	{
		Map<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>> catMap = getCachedMap(id);
		if (catMap != null)
		{
			for (Map.Entry<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>> catME : catMap
					.entrySet())
			{
				// Category<Ability> cat = catME.getKey();
				Map<Ability, Set<Object>> abilitySet = catME.getValue().remove(
						nature);
				if (abilitySet != null)
				{
					processRemoveAbilityMap(id, abilitySet);
				}
			}
		}
	}

	private void processRemoveAbilityMap(CharID id,
			Map<Ability, Set<Object>> abilitySet)
	{
		for (Ability a : abilitySet.keySet())
		{
			fireDataFacetChangeEvent(id, a, DataFacetChangeEvent.DATA_REMOVED);
		}
	}

	/**
	 * Returns the Set of Ability Category objects in this GrantedAbilityFacet
	 * for the Player Character represented by the given CharID
	 * 
	 * @param id
	 *            The CharID representing the Player Character for which the
	 *            items in this GrantedAbilityFacet should be returned.
	 * @return A non-null Set of Ability Category objects in this
	 *         GrantedAbilityFacet for the Player Character represented by the
	 *         given CharID
	 */
	public Set<Category<Ability>> getCategories(CharID id)
	{
		Map<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>> map = getCachedMap(id);
		if (map == null)
		{
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(map.keySet());
	}

	public void copyContents(CharID id, CharID id2)
	{
		Map<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>> catMap = getCachedMap(id);
		if (catMap != null)
		{
			for (Map.Entry<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>> catME : catMap
					.entrySet())
			{
				Category<Ability> cat = catME.getKey();
				for (Map.Entry<Nature, Map<Ability, Set<Object>>> natME : catME
						.getValue().entrySet())
				{
					Nature nature = natME.getKey();
					for (Map.Entry<Ability, Set<Object>> aME : natME.getValue()
							.entrySet())
					{
						Ability ability = aME.getKey();
						for (Object source : aME.getValue())
						{
							add(id2, cat, nature, ability, source);
						}
					}
				}
			}
		}
	}

	public Nature getNature(CharID id, Category<Ability> category,
			Ability ability)
	{
		Nature n = null;
		Map<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>> catMap = getCachedMap(id);
		if (catMap != null)
		{
			Map<Nature, Map<Ability, Set<Object>>> natMap = catMap
					.get(category);
			if (natMap != null)
			{
				for (Map.Entry<Nature, Map<Ability, Set<Object>>> me : natMap
						.entrySet())
				{
					if (me.getValue().containsKey(ability))
					{
						n = Nature.getBestNature(me.getKey(), n);
					}
				}
			}
		}
		return n;
	}

	public Collection<Nature> getNatures(CharID id, Category<Ability> cat)
	{
		Map<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>> catMap = getCachedMap(id);
		if (catMap != null)
		{
			Map<Nature, Map<Ability, Set<Object>>> natMap = catMap.get(cat);
			if (natMap != null)
			{
				return Collections.unmodifiableSet(natMap.keySet());
			}
		}
		return Collections.emptySet();
	}

	public void removeAll(CharID id, Object source)
	{
		Map<Category<Ability>, Map<Nature, Map<Ability, Set<Object>>>> catMap = getCachedMap(id);
		if (catMap != null)
		{
			for (Iterator<Map<Nature, Map<Ability, Set<Object>>>> cIter = catMap
					.values().iterator(); cIter.hasNext();)
			{
				Map<Nature, Map<Ability, Set<Object>>> natureMap = cIter.next();
				for (Iterator<Map<Ability, Set<Object>>> nIter = natureMap
						.values().iterator(); nIter.hasNext();)
				{
					Map<Ability, Set<Object>> abilMap = nIter.next();
					for (Iterator<Map.Entry<Ability, Set<Object>>> aIter = abilMap
							.entrySet().iterator(); aIter.hasNext();)
					{
						Entry<Ability, Set<Object>> aEntry = aIter.next();
						Set<Object> sourceSet = aEntry.getValue();
						if (sourceSet.remove(source))
						{
							if (sourceSet.isEmpty())
							{
								Ability ab = aEntry.getKey();
								aIter.remove();
								fireDataFacetChangeEvent(id, ab,
										DataFacetChangeEvent.DATA_REMOVED);
							}
						}
					}
					if (abilMap.isEmpty())
					{
						nIter.remove();
					}
				}
				if (natureMap.isEmpty())
				{
					cIter.remove();
				}
			}
		}
	}
}