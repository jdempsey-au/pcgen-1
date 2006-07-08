/*
 * PrerequisiteMultWriter.java
 *
 * Copyright 2004 (C) Frugal <frugal@purplewombat.co.uk>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.       See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on 18-Dec-2003
 *
 * Current Ver: $Revision$
 *
 * Last Editor: $Author$
 *
 * Last Edited: $Date$
 *
 */
package pcgen.persistence.lst.output.prereq;

import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteOperator;
import pcgen.persistence.PersistenceLayerException;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class PrerequisiteMultWriter extends AbstractPrerequisiteWriter implements PrerequisiteWriterInterface
{
	private boolean allSkillTot = false;

	/* (non-Javadoc)
	 * @see pcgen.persistence.lst.output.prereq.PrerequisiteWriterInterface#kindHandled()
	 */
	public String kindHandled()
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see pcgen.persistence.lst.output.prereq.PrerequisiteWriterInterface#operatorsHandled()
	 */
	public PrerequisiteOperator[] operatorsHandled()
	{
		return new PrerequisiteOperator[] {
				PrerequisiteOperator.GTEQ,
				PrerequisiteOperator.LT,
				PrerequisiteOperator.EQ,
				PrerequisiteOperator.NEQ
		} ;
	}

	/* (non-Javadoc)
	 * @see pcgen.persistence.lst.output.prereq.PrerequisiteWriterInterface#write(java.io.Writer, pcgen.core.prereq.Prerequisite)
	 */
	public void write(Writer writer, Prerequisite prereq) throws PersistenceLayerException
	{
		checkValidOperator(prereq, operatorsHandled());
		try
		{
			Prerequisite subreq;
			//
			// Check to see if this is a special case for PREMULT
			//
			if (prereq.getPrerequisites().size() != 0)
			{
				subreq = prereq.getPrerequisites().get(0);
				final PrerequisiteWriterInterface test = PrerequisiteWriterFactory.getInstance().getWriter(subreq.getKind());
				if ((test != null) && (test instanceof AbstractPrerequisiteWriter) && ((AbstractPrerequisiteWriter) test).specialCase(writer, prereq))
				{
					return;
				}
			}


			if (isSpecialCase(prereq))
			{
				handleSpecialCase(writer, prereq);
				return;
			}



			if (prereq.getOperator().equals(PrerequisiteOperator.LT))
			{
				writer.write('!');
			}

			writer.write("PREMULT:");
			writer.write(prereq.getOperand());
			writer.write(',');
			int i = 0;
			for (Iterator iter = prereq.getPrerequisites().iterator(); iter.hasNext(); i++)
			{
				subreq = (Prerequisite) iter.next();
				if (i > 0)
				{
					writer.write(',');
				}
				writer.write('[');

				PrerequisiteWriterFactory factory = PrerequisiteWriterFactory.getInstance();
				PrerequisiteWriterInterface w = factory.getWriter(subreq.getKind());
				if (w != null)
				{
					w.write(writer, subreq);
				}
				else
				{
					writer.write("unrecognized kind:" + subreq.getKind());
				}
				writer.write(']');
			}
		}
		catch (IOException e)
		{
			throw new PersistenceLayerException(e.getMessage());
		}
	}

	/**
	 * @param writer
	 * @param prereq
	 * @throws IOException
	 */
	private void handleSpecialCase(Writer writer, Prerequisite prereq) throws IOException
	{
		if (allSkillTot)
		{
			if (prereq.getOperator().equals(PrerequisiteOperator.LT))
			{
				writer.write('!');
			}
			writer.write("PRESKILLTOT:");

			int i = 0;
			for (Iterator iter = prereq.getPrerequisites().iterator(); iter.hasNext(); i++)
			{
				Prerequisite subreq = (Prerequisite) iter.next();
				if (i > 0)
				{
					writer.write(',');
				}
				writer.write(subreq.getKey());
			}
			writer.write('=');
			writer.write(prereq.getOperand());
		}
	}

	/**
	 * @param prereq
	 * @return TRUE if special case, else FALSE
	 */
	private boolean isSpecialCase(Prerequisite prereq)
	{
		// Special case of all subreqs being SKILL with total-values=true
		allSkillTot = true;
		for (Iterator iter = prereq.getPrerequisites().iterator(); iter.hasNext() && allSkillTot; )
		{
			Prerequisite element = (Prerequisite) iter.next();
			if (!"skill".equalsIgnoreCase(element.getKind()) || !element.isTotalValues())
			{
				allSkillTot = false;
			}
		}
		if (allSkillTot)
		{
			return allSkillTot;
		}


		return false;
	}

}
