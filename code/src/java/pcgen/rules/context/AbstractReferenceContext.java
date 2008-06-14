/*
 * Copyright 2007 (C) Tom Parker <thpr@users.sourceforge.net>
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package pcgen.rules.context;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pcgen.base.util.OneToOneMap;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.CategorizedCDOMObject;
import pcgen.cdom.base.Category;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.StringKey;
import pcgen.cdom.list.ClassSkillList;
import pcgen.cdom.list.ClassSpellList;
import pcgen.cdom.list.DomainSpellList;
import pcgen.cdom.reference.CDOMDirectSingleRef;
import pcgen.cdom.reference.CDOMGroupRef;
import pcgen.cdom.reference.CDOMSingleRef;
import pcgen.cdom.reference.ReferenceManufacturer;
import pcgen.core.Domain;
import pcgen.core.PCClass;
import pcgen.core.SubClass;

public abstract class AbstractReferenceContext
{

	private static final Class<DomainSpellList> DOMAINSPELLLIST_CLASS = DomainSpellList.class;
	private static final Class<ClassSkillList> CLASSSKILLLIST_CLASS = ClassSkillList.class;
	private static final Class<ClassSpellList> CLASSSPELLLIST_CLASS = ClassSpellList.class;

	private Map<Class<?>, OneToOneMap<CDOMObject, String>> abbMap = new HashMap<Class<?>, OneToOneMap<CDOMObject, String>>();

	private HashMap<CDOMObject, CDOMSingleRef<?>> directRefCache = new HashMap<CDOMObject, CDOMSingleRef<?>>();

	public abstract <T extends CDOMObject> ReferenceManufacturer<T, ? extends CDOMSingleRef<T>> getManufacturer(
			Class<T> cl);

	public abstract <T extends CDOMObject & CategorizedCDOMObject<T>> ReferenceManufacturer<T, ? extends CDOMSingleRef<T>> getManufacturer(
			Class<T> cl, Category<T> cat);

	public abstract Collection<? extends ReferenceManufacturer<? extends CDOMObject, ?>> getAllManufacturers();

	public boolean validate()
	{
		boolean returnGood = true;
		for (ReferenceManufacturer<?, ?> ref : getAllManufacturers())
		{
			returnGood &= ref.validate();
		}
		return returnGood;
	}

	public <T extends CDOMObject> CDOMGroupRef<T> getCDOMAllReference(Class<T> c)
	{
		return getManufacturer(c).getAllReference();
	}

	public <T extends CDOMObject & CategorizedCDOMObject<T>> CDOMGroupRef<T> getCDOMAllReference(Class<T> c, Category<T> cat)
	{
		return getManufacturer(c, cat).getAllReference();
	}

	public <T extends CDOMObject> CDOMGroupRef<T> getCDOMTypeReference(
			Class<T> c, String... val)
	{
		return getManufacturer(c).getTypeReference(val);
	}

	public <T extends CDOMObject & CategorizedCDOMObject<T>> CDOMGroupRef<T> getCDOMTypeReference(
			Class<T> c, Category<T> cat, String... val)
	{
		return getManufacturer(c, cat).getTypeReference(val);
	}

	public <T extends CDOMObject> T constructCDOMObject(Class<T> c, String val)
	{
		T obj;
		if (CategorizedCDOMObject.class.isAssignableFrom(c))
		{
			Class cl = c;
			obj = (T) getManufacturer(cl, null).constructCDOMObject(val);
		}
		else
		{
			obj = getManufacturer(c).constructCDOMObject(val);
		}
		obj.put(ObjectKey.SOURCE_URI, sourceURI);
		return obj;
	}

	public <T extends CDOMObject> void constructIfNecessary(Class<T> cl,
			String value)
	{
		getManufacturer(cl).constructIfNecessary(value);
	}

	public <T extends CDOMObject> CDOMSingleRef<T> getCDOMReference(Class<T> c,
			String val)
	{
		/*
		 * Keeping this generic (not inlined as the other methods in this class)
		 * is required by bugs in Sun's Java 5 compiler.
		 */
		ReferenceManufacturer manufacturer = getManufacturer(c);
		return manufacturer.getReference(val);
	}

	public <T extends CDOMObject & CategorizedCDOMObject<T>> CDOMSingleRef<T> getCDOMReference(
			Class<T> c, Category<T> cat, String val)
	{
		/*
		 * Keeping this generic (not inlined as the other methods in this class)
		 * is required by bugs in Sun's Java 5 compiler.
		 */
		ReferenceManufacturer manufacturer = getManufacturer(c, cat);
		return manufacturer.getReference(val);
	}

	public <T extends CDOMObject> void reassociateKey(String key, T obj)
	{
		 if (CategorizedCDOMObject.class.isAssignableFrom(obj.getClass()))
		{
			Class cl = obj.getClass();
			reassociateCategorizedKey(key, obj, cl);
		}
		else
		{
			getManufacturer((Class<T>) obj.getClass()).reassociateKey(key, obj);
		}
	}

	private <T extends CDOMObject & CategorizedCDOMObject<T>> void reassociateCategorizedKey(
			String key, CDOMObject orig, Class<T> cl)
	{
		T obj = (T) orig;
		getManufacturer(cl, obj.getCDOMCategory()).reassociateKey(key, obj);
	}

	public <T extends CDOMObject> T silentlyGetConstructedCDOMObject(
			Class<T> c, String val)
	{
		return getManufacturer(c).silentlyGetConstructedCDOMObject(val);
	}

	// public <T extends CDOMObject & CategorizedCDOMObject<T>> CDOMSingleRef<T>
	// getCDOMReference(
	// Class<T> c, Category<T> cat, String val)
	// {
	// return categorized.getCDOMReference(c, cat, val);
	// }
	//
	// public <T extends CDOMObject & CategorizedCDOMObject<T>> void
	// reassociateCategory(
	// Category<T> cat, T obj)
	// {
	// categorized.reassociateCategory(cat, obj);
	// }

	// public <T extends CDOMObject> T cloneConstructedCDOMObject(T orig,
	// String newKey)
	// {
	// Class cl = (Class) orig.getClass();
	// if (CategorizedCDOMObject.class.isAssignableFrom(cl))
	// {
	// return (T) cloneCategorized(cl, ((CategorizedCDOMObject) orig)
	// .getCDOMCategory(), orig, newKey);
	// }
	// else
	// {
	// return (T) simple.cloneConstructedCDOMObject(cl, orig, newKey);
	// }
	// }

	public <T extends CDOMObject> void importObject(T orig)
	{
		if (CategorizedCDOMObject.class.isAssignableFrom(orig.getClass()))
		{
			Class cl = orig.getClass();
			importCategorized(orig, cl);
		}
		else
		{
			getManufacturer((Class<T>) orig.getClass()).registerWithKey(orig,
					orig.getKeyName());
		}
	}

	private <T extends CDOMObject & CategorizedCDOMObject<T>> void importCategorized(
			CDOMObject orig, Class<T> cl)
	{
		T obj = (T) orig;
		getManufacturer(cl, obj.getCDOMCategory()).registerWithKey(obj,
				obj.getKeyName());
	}

	public <T extends CDOMObject> boolean forget(T obj)
	{
		return getManufacturer((Class<T>) obj.getClass()).forgetObject(obj);
	}

	// public <T extends CDOMObject & CategorizedCDOMObject<T>> T
	// cloneCategorized(
	// Class<T> cl, Category<T> cat, Object o, String newKey)
	// {
	// return categorized.cloneConstructedCDOMObject(cl, cat, (T) o, newKey);
	// }

	// public <T extends CDOMObject & CategorizedCDOMObject<T>>
	// ReferenceManufacturer<T, CDOMCategorizedSingleRef<T>>
	// getReferenceManufacturer(
	// Class<T> c, Category<T> cat)
	// {
	// return categorized.getManufacturer(c, cat);
	// }

	public <T extends CDOMObject> Collection<T> getConstructedCDOMObjects(
			Class<T> c)
	{
		// if (CategorizedCDOMObject.class.isAssignableFrom(c))
		// {
		// return categorized.getAllConstructedCDOMObjects((Class) c);
		// }
		// else
		// {
		return getManufacturer(c).getAllConstructedCDOMObjects();
		// }
	}

	// public <T extends CDOMObject & CategorizedCDOMObject<T>> Collection<T>
	// getConstructedCDOMObjects(
	// Class<T> c, Category<T> cat)
	// {
	// return categorized.getConstructedCDOMObjects(c, cat);
	// }

	public Set<CDOMObject> getAllConstructedObjects()
	{
		Set<CDOMObject> set = new HashSet<CDOMObject>();
		for (ReferenceManufacturer<? extends CDOMObject, ?> ref : getAllManufacturers())
		{
			set.addAll(ref.getAllConstructedCDOMObjects());
		}
		// Collection otherSet = categorized.getAllConstructedCDOMObjects();
		// set.addAll(otherSet);
		return set;
	}

	public <T extends CDOMObject> boolean containsConstructedCDOMObject(
			Class<T> c, String s)
	{
		return getManufacturer(c).containsConstructedCDOMObject(s);
	}

	public void buildDerivedObjects()
	{
		Collection<Domain> domains = getConstructedCDOMObjects(Domain.class);
		for (Domain d : domains)
		{
			constructCDOMObject(DOMAINSPELLLIST_CLASS, d.getKeyName());
		}
		Collection<PCClass> classes = getConstructedCDOMObjects(PCClass.class);
		for (PCClass pcc : classes)
		{
			String key = pcc.getKeyName();
			constructCDOMObject(CLASSSKILLLIST_CLASS, key);
			// TODO Need to limit which are built to only spellcasters...
			constructCDOMObject(CLASSSPELLLIST_CLASS, key);
			// simple.constructCDOMObject(SPELLPROGRESSION_CLASS, key);
//			Collection<CDOMSubClass> subclasses = categorized
//					.getConstructedCDOMObjects(SUBCLASS_CLASS, SubClassCategory
//							.getConstant(key));
//			for (CDOMSubClass subcl : subclasses)
			List<SubClass> subc = pcc.getSubClassList();
			if (subc != null)
			{
				for (SubClass subcl : subc)
				{
					String subKey = subcl.getKeyName();
					constructCDOMObject(CLASSSKILLLIST_CLASS, subKey);
					// TODO Need to limit which are built to only
					// spellcasters...
					constructCDOMObject(CLASSSPELLLIST_CLASS, subKey);
					//constructCDOMObject(SPELLPROGRESSION_CLASS, subKey);
				}
			}
		}
	}

	public <T extends CDOMObject> CDOMSingleRef<T> getCDOMDirectReference(T obj)
	{
		CDOMSingleRef<?> ref = directRefCache.get(obj);
		if (ref == null)
		{
			ref = new CDOMDirectSingleRef<T>(obj);
		}
		return (CDOMSingleRef<T>) ref;
	}

	public void registerAbbreviation(CDOMObject obj, String value)
	{
		OneToOneMap<CDOMObject, String> map = abbMap.get(obj.getClass());
		if (map == null)
		{
			map = new OneToOneMap<CDOMObject, String>();
			abbMap.put(obj.getClass(), map);
		}
		map.put(obj, value);
		obj.put(StringKey.ABB, value);
	}

	public String getAbbreviation(CDOMObject obj)
	{
		OneToOneMap<CDOMObject, String> map = abbMap.get(obj.getClass());
		return map == null ? null : map.get(obj);
	}

	public <T> T getAbbreviatedObject(Class<T> cl, String value)
	{
		OneToOneMap<T, String> map = (OneToOneMap<T, String>) abbMap.get(cl);
		return map == null ? null : map.getKeyFor(value);
	}

	private URI sourceURI;

	private URI extractURI;

	public URI getExtractURI()
	{
		return extractURI;
	}

	public void setExtractURI(URI extractURI)
	{
		this.extractURI = extractURI;
	}

	public URI getSourceURI()
	{
		return sourceURI;
	}

	public void setSourceURI(URI sourceURI)
	{
		this.sourceURI = sourceURI;
	}

	public void resolveReferences()
	{
		for (ReferenceManufacturer<?, ?> rs : getAllManufacturers())
		{
			rs.fillReferences();
			rs.resolveReferences();
		}
	}

	public void buildDeferredObjects()
	{
		for (ReferenceManufacturer<?, ?> rs : getAllManufacturers())
		{
			rs.buildDeferredObjects();
		}
	}

	// public <T extends CDOMObject> CDOMAddressedSingleRef<T>
	// getAddressedReference(
	// CDOMObject obj, Class<T> name, String addressName)
	// {
	// CDOMAddressedSingleRef<T> addr = addressed.get(obj, name);
	// if (addr == null)
	// {
	// addr = new CDOMAddressedSingleRef<T>(obj, name, addressName);
	// addressed.put(obj, name, addr);
	// }
	// return addr;
	// }

}
