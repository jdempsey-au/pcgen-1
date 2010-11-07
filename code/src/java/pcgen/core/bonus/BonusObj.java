/*
 * BonusObj.java
 * Copyright 2002 (C) Greg Bingleman <byngl@hotmail.com>
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
 *
 * Created on December 13, 2002, 9:19 AM
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 *
 */
package pcgen.core.bonus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import pcgen.base.formula.Formula;
import pcgen.cdom.base.ConcretePrereqObject;
import pcgen.cdom.base.Constants;
import pcgen.cdom.base.FormulaFactory;
import pcgen.cdom.base.QualifyingObject;
import pcgen.core.PlayerCharacter;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.utils.CoreUtility;
import pcgen.persistence.lst.output.prereq.PrerequisiteWriter;
import pcgen.rules.context.LoadContext;

/**
 * <code>BonusObj</code>
 *
 * @author  Greg Bingleman <byngl@hotmail.com>
 **/
public abstract class BonusObj extends ConcretePrereqObject implements Serializable, Cloneable, QualifyingObject
{
	private List<Object>    bonusInfo       = new ArrayList<Object>();
	private Map<String, String>     dependMap  = new HashMap<String, String>();
	private Formula bonusFormula = FormulaFactory.ZERO;
	/** The name of the bonus e.g. STAT or COMBAT */
	private String  bonusName            = Constants.EMPTY_STRING;
	/** The type of the bonus e.g. Enhancement or Dodge */
	private String  bonusType            = Constants.EMPTY_STRING;
	private String  varPart              = Constants.EMPTY_STRING;
	private String typeOfBonus           = Bonus.BONUS_UNDEFINED;
	private String  stringRepresentation = null;
	private String tokenSource = null;
	private boolean saveToPCG = true;

	/** An enum for the possible stacking modifiers a bonus can have */
	public enum StackType {
		/** This bonus will follow the normal stacking rules. */
		NORMAL, 
		/** This bonus will always stack regardless of its type. */
		STACK, 
		/** 
		 * This bonus will stack with other bonuses of its own type but not
		 * with bonuses of other types.
		 */
		REPLACE 
	}
	private StackType theStackingFlag = StackType.NORMAL;

	/**
	 * Get Bonus Info
	 * @return Bonus info
	 */
	public String getBonusInfo()
	{
		final StringBuffer sb = new StringBuffer(50);

		if (bonusInfo.size() > 0)
		{
			for (int i = 0; i < bonusInfo.size(); ++i)
			{
				sb.append(i == 0 ? Constants.EMPTY_STRING : Constants.COMMA);
				sb.append(unparseToken(bonusInfo.get(i)));
			}
		}
		else
		{
			sb.append("|ERROR"); //$NON-NLS-1$
		}

		return sb.toString().toUpperCase();
	}

	/**
	 * Return a list of the unparsed (converted back to strings) 
	 * bonus info entries.
	 * @return The unparsed bonus info list
	 */
	public List<String> getUnparsedBonusInfoList()
	{
		List<String> list = new ArrayList<String>();
		for (Object info : bonusInfo)
		{
			list.add(unparseToken(info));
		}
		return list;
	}
			
	/**
	 * get Bonus Info List
	 * @return Bonus Info List
	 */
	public List<?> getBonusInfoList()
	{
		return bonusInfo;
	}

	/**
	 * Get Bonus Name
	 * @return bonus name
	 */
	public String getBonusName()
	{
		return bonusName;
	}

	/**
	 * Get depends on given a key
	 * @param aString
	 * @return true if it depends on
	 */
	public boolean getDependsOn(final String aString)
	{
		return dependMap.containsKey(aString);
	}

	/**
	 * Get depends on given a set of keys
	 * @param aList List of bonus keys
	 * @return true if it any of the keys are depended on
	 */
	public boolean getDependsOn(final List<String> aList)
	{
		for (String key : aList)
		{
			if (dependMap.containsKey(key))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Report on the dependencies of the bonus.
	 * @return String the dependancies
	 */
	public String listDependsMap()
	{
		StringBuffer buff = new StringBuffer("[");
		for (String key : dependMap.keySet())
		{
			if (buff.length()> 1)
			{
				buff.append(", ");
			}
			buff.append(key);
		}
		buff.append("]");
		return buff.toString();
	}

	/**
	 * Checks if this bonus is a &quot;Temporary&quot; bonus.
	 * 
	 * <p>Temporary bonuses are applied and removed from the TempBonuses tab
	 * in the application.
	 * 
	 * TODO - This should be set as a flag on the bonus.
	 * 
	 * @return <tt>true</tt> if this is a temporary bonus.
	 */
	public boolean isTempBonus()
	{
		if ( !hasPrerequisites() )
		{
			return false;
		}
		
		// TODO - This should be handled better
		for ( final Prerequisite prereq : getPrerequisiteList() )
		{
			if ( prereq.getKind() == null )
			{
				continue;
			}
			if ( prereq.getKind().equalsIgnoreCase(Prerequisite.APPLY_KIND) )
			{
				return true;
			}
		}
		return false;
	}
	
	/** An enum for the target of a temp bonus */
	public enum TempBonusTarget {
		/** This bonus applies only to if the PC has the owner of this bonus */
		PC,
		/** Any PC can apply this bonus */
		ANYPC
	}
	
	/**
	 * Tests if this bonus' target is the same as the passed in one.
	 * 
	 * @param aTarget A TempBonusTarget to test for.
	 * 
	 * @return <tt>true</tt> if this bonus has that target.
	 * 
	 * <p><b>TODO</b> - This should be set as a flag on bonus creation.
	 */
	public boolean isTempBonusTarget( final TempBonusTarget aTarget )
	{
		if ( !isTempBonus() || !hasPrerequisites() )
		{
			return false;
		}
		
		for ( final Prerequisite prereq : getPrerequisiteList() )
		{
			if ( prereq.getOperand().equalsIgnoreCase(aTarget.toString()) )
			{
				return true;
			}
			for ( final Prerequisite premult : prereq.getPrerequisites() )
			{
				if ( premult.getOperand().equalsIgnoreCase(aTarget.toString()) )
				{
					return true;
				}
			}
		}
		return false;
	}
	
	////////////////////////////////////////////////
	//        Public Accessors and Mutators       //
	////////////////////////////////////////////////

	/**
	 * @return type of Bonus
	 */
	public String getTypeOfBonus()
	{
		return typeOfBonus;
	}

	/**
	 * Get the bonus type
	 * @return the bonus type
	 */
	public String getTypeString()
	{
		return bonusType;
	}

	/**
	 * Set value
	 * @param bValue
	 */
	Formula setValue(final String bValue)
	{
		bonusFormula = FormulaFactory.getFormulaFor(bValue);
		if (!bonusFormula.isStatic())
		{
			buildDependMap(bValue.toUpperCase());
		}
		return bonusFormula;
	}

	/**
	 * Get the bonus formula
	 * @return the formula
	 */
	public Formula getFormula()
	{
		return bonusFormula;
	}

	/**
	 * Get the bonus value
	 * @return the value
	 */
	public String getValue()
	{
		return bonusFormula.toString();
	}

	/**
	 * Get the bonus value as a double
	 * @param string 
	 * @return bonus value as a double
	 */
	public Number resolve(PlayerCharacter pc, String string)
	{
		return bonusFormula.resolve(pc, string);
	}

	/**
	 * is value static
	 * @return true if value is static
	 */
	public boolean isValueStatic()
	{
		return bonusFormula.isStatic();
	}

	/**
	 * Set the variable
	 * @param aString
	 */
	public void setVariable(final String aString)
	{
		varPart = aString.toUpperCase();
	}

	/**
	 * Get the variable
	 * @return the variable
	 */
	public String getVariable()
	{
		return varPart;
	}

	/**
	 * Has bonus type string
	 * @return true if bonus type string exists
	 */
	public boolean hasTypeString()
	{
		return bonusType.length() > 0;
	}

	/**
	 * has variable
	 * @return true if bonus has variable
	 */
	public boolean hasVariable()
	{
		return varPart.length() > 0;
	}

	/**
	 * Retrieve the persistent text for this bonus. This text
	 * when reparsed will recreate the bonus. PCC Text is used
	 * as a name here as this output could also be used by the
	 * LST editors.
	 * @return The text to be saved for example in a character.
	 */
	public String getPCCText()
	{
		if (stringRepresentation == null)
		{
			final StringBuffer sb = new StringBuffer(50);
	
			sb.append(getTypeOfBonus());
			if (varPart != null && varPart.length() > 0)
			{
				sb.append(varPart);
			}
	
			if (bonusInfo.size() > 0)
			{
				for (int i = 0; i < bonusInfo.size(); ++i)
				{
					sb.append(i == 0 ? '|' : ',').append(
						unparseToken(bonusInfo.get(i)));
				}
			}
			else
			{
				sb.append("|ERROR");
			}
	
			sb.append('|').append(bonusFormula.toString());

			if (bonusType.length() != 0)
			{
				sb.append("|TYPE=").append(bonusType);
			}
			
			// And put the prereqs at the end of the string.
			if (hasPrerequisites())
			{
				sb.append(Constants.PIPE);
				sb.append(new PrerequisiteWriter().getPrerequisiteString(
						getPrerequisiteList(), Constants.PIPE));
			}
	
			stringRepresentation = sb.toString();
		}
		return stringRepresentation;
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString()
	{
		return getPCCText();
	}

	protected void setBonusName(final String aName)
	{
		bonusName = aName;
	}

	protected void setTypeOfBonus(final String type)
	{
		typeOfBonus = type;
	}

	protected void addBonusInfo(final Object obj)
	{
		bonusInfo.add(obj);
	}

	protected void replaceBonusInfo(final Object oldObj, final Object newObj)
	{
		for (int i = 0; i < bonusInfo.size(); ++i)
		{
			final Object curObj = bonusInfo.get(i);
			if (curObj == oldObj)
			{
				bonusInfo.set(i, newObj);
				break;
			}
		}
	}

	protected boolean addType(final String typeString)
	{
		if (bonusType.length() == 0)
		{
			bonusType = typeString.toUpperCase();

			return true;
		}

		return false;
	}

	/**
	 * Sets the stacking flag for this bonus.
	 * 
	 * @param aFlag A <tt>StackType</tt> to set.
	 */
	public void setStackingFlag( final StackType aFlag )
	{
		theStackingFlag = aFlag;
	}
	
	/**
	 * Gets the stacking flag for this bonus.
	 * 
	 * @return A <tt>StackType</tt>.
	 */
	public StackType getStackingFlag()
	{
		return theStackingFlag;
	}
	
	protected abstract boolean parseToken(LoadContext context, final String token);

	protected abstract String unparseToken(final Object obj);

	public abstract String getBonusHandled();

	private void buildDependMap(String aString)
	{
		if (aString.indexOf("SKILLINFO(") >= 0)
		{
			dependMap.put("JEPFORMULA", "1");
		}
		if (aString.indexOf("HP") >= 0)
		{
			dependMap.put("CURRENTMAX", "1");
		}

		// First wack out all the () pairs to find variable names
		while (aString.lastIndexOf('(') >= 0)
		{
			final int x = CoreUtility.innerMostStringStart(aString);
			final int y = CoreUtility.innerMostStringEnd(aString);

			if (y < x)
			{
				return;
			}

			final String bString = aString.substring(x + 1, y);
			buildDependMap(bString);
			aString = new StringBuffer().append(aString.substring(0, x))
					.append(aString.substring(y + 1)).toString();
		}

		if (aString.indexOf("(") >= 0 || aString.indexOf(")") >= 0 ||
				aString.indexOf("%") >= 0)
		{
			return;
		}

		// We now have the substring we want to work on
		final StringTokenizer cTok = new StringTokenizer(aString, ".,");

		while (cTok.hasMoreTokens())
		{
			final String controlString = cTok.nextToken();

			// skip flow control tags
			if ("IF".equals(controlString) || "THEN".equals(controlString) || "ELSE"
					.equals(controlString)
				|| "GT".equals(controlString) || "GTEQ".equals(controlString) || "EQ"
					.equals(controlString)
				|| "LTEQ".equals(controlString) || "LT".equals(controlString))
			{
				continue;
			}

			// Now remove math strings: + - / *
			// and comparison strings: > = <
			// remember, a StringTokenizer will tokenize
			// on any of the found delimiters
			final StringTokenizer mTok = new StringTokenizer(controlString, "+-/*>=<\"");

			while (mTok.hasMoreTokens())
			{
				String newString = mTok.nextToken();
				String testString = newString;
				boolean found = false;

				// now Check for MIN or MAX
				while (!found)
				{
					if (newString.indexOf("MAX") >= 0)
					{
						testString = newString.substring(0, newString.indexOf("MAX"));
						newString = newString.substring(newString.indexOf("MAX") + 3);
					}
					else if (newString.indexOf("MIN") >= 0)
					{
						testString = newString.substring(0, newString.indexOf("MIN"));
						newString = newString.substring(newString.indexOf("MIN") + 3);
					}
					else
					{
						found = true;
					}

					// check to see if it's a number
					try
					{
						Float.parseFloat(testString);
					}
					catch (NumberFormatException e)
					{
						// It's a Variable!
						if (testString.length() > 0)
						{
							if (testString.startsWith("MOVE[")) {
								testString = new StringBuffer().append("TYPE.")
										.append(testString.substring(5,
												testString.length() - 1))
										.toString();
							}
							dependMap.put(testString, "1");
						}
					}
				}
			}
		}
	}
	
	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public BonusObj clone() throws CloneNotSupportedException
	{
		final BonusObj bonusObj = (BonusObj) super.clone();

		bonusObj.bonusInfo = new ArrayList<Object>(bonusInfo);

		bonusObj.dependMap = new HashMap<String, String>();
		bonusObj.setValue(bonusFormula.toString());

		// we want to keep the same references to these objects
		// creatorObj
		// targetObj

		// These objects are immutable and do not need explicit cloning
		// bonusName
		// bonusType
		// choiceString
		// varPart
		// isApplied
		// valueIsStatic
		// pcLevel
		// typeOfBonus
		return bonusObj;
	}

	public void setTokenSource(String tokenName)
	{
		tokenSource = tokenName;
	}
	
	public String getTokenSource()
	{
		return tokenSource;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		if (!(obj instanceof BonusObj))
		{
			return false;
		}
		BonusObj other = (BonusObj) obj;
		return equalsPrereqObject(other)
				&& bonusFormula.equals(other.bonusFormula)
				&& bonusName.equals(other.bonusName)
				&& bonusType.equals(other.bonusType)
				&& theStackingFlag.equals(other.theStackingFlag)
				&& bonusInfo.equals(other.bonusInfo);
	}

	@Override
	public int hashCode()
	{
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	public void setSaveToPCG(boolean b)
	{
		saveToPCG = b;
	}
	
	public boolean saveToPCG()
	{
		return saveToPCG;
	}

	/*
	 * This makes an editor a bit more difficult, but since CHOOSE is an early
	 * target of 5.17, this probably isn't a big deal.
	 */
	private String originalString;

	public void putOriginalString(String bonusString)
	{
		originalString = bonusString;
	}
	
	public String getLSTformat()
	{
		return originalString;
	}
	
}
