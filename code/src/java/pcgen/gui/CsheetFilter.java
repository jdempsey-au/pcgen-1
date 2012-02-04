/*
 * PcgFilter.java
 * Copyright 2001 (C) Bryan McRoberts <merton_monk@yahoo.com>
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
 * Created on May 10, 2001 09:01
 */
package pcgen.gui;

import pcgen.core.SettingsHandler;
import pcgen.util.Logging;
import pcgen.system.LanguageBundle;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 *  This class filters out non-csheet files.
 *
 * @author     Jonas Karlsson <jujutsunerd@users.sourceforge.net>
 * @version $Revision$
 */
public final class CsheetFilter extends FileFilter implements FilenameFilter
{
	private static final String chaTemplates = LanguageBundle.getString("in_chaTemplates");
	private List<String> acceptedList = null;
	private String dirFilter = null;
	private String ignoreExtension = ".fo";
	private int sheetType = 0; // 0 = character sheet, 1 = party sheet

	/**
	 * Constructor
	 */
	public CsheetFilter()
	{
	    // Empty Constructor
	}

	/**
	 * Constructor
	 * @param sheetTypeInt
	 */
	public CsheetFilter(int sheetTypeInt)
	{
		sheetType = sheetTypeInt;
	}

	/**
	 * Get Accepted List
	 * @return accepted List
	 */
	public List<String> getAccepted()
	{
		acceptedList = new ArrayList<String>();
		accept(SettingsHandler.getPcgenOutputSheetDir());

		return acceptedList;
	}

	/**
	 *  Returns a description of this class
	 *
	 * @return    The Description
	 * @since
	 */
	public String getDescription()
	{
		return chaTemplates;
	}

	/**
	 *  sets a directory filter for this instance
	 *
	 * @param arg   the directory name in which valid sheets will be found
	 * @since
	 */
	public void setDirFilter(final String arg)
	{
		dirFilter = arg;
	}

	/**
	 *  sets the extenion of files which are not acceptable
	 *
	 * @param arg   the extenion of files not to be accepted
	 * @since
	 */
	public void setIgnoreExtension(final String arg)
	{
		ignoreExtension = arg;
	}

	/**
	 *  Accept all directories and all csheet files
	 *
	 * @param  f  The file to be checked
	 * @return    Whether the file is accepted
	 */
	public boolean accept(final File f)
	{
		if (f.isDirectory())
		{
			final File[] fileList = f.listFiles();

			for (int i = 0; i < fileList.length; ++i)
			{
				accept(fileList[i]);
			}

			return true;
		}

		return accept(f.getParentFile(), f.getName());
	}

	public boolean accept(final File dir, final String name)
	{
		final String s = name.toLowerCase();
		final File aFile = new File(dir + File.separator + name);

		if (aFile.isDirectory())
		{
			return accept(aFile);
		}

		if (!aFile.getParent().endsWith(File.separator + dirFilter))
		{
			return false;
		}

		// This sure is a lot of code for debugging mode.  XXX
		if (!s.endsWith(ignoreExtension) || (s.endsWith(ignoreExtension) && Logging.isDebugMode()))
		{
			if (((sheetType == 0) && s.startsWith("csheet")) || ((sheetType == 1) && s.startsWith("psheet")))
			{
				if (acceptedList != null)
				{
					try
					{
						final String filename = dir.getAbsolutePath().substring(SettingsHandler.getPcgenOutputSheetDir()
							    .getAbsolutePath().length() + 1) + File.separator + name;
						acceptedList.add(filename);
					}
					catch (Exception exc)
					{
						//TODO: Should we really ignore this?
					}
				}

				return true;
			}
		}

		return false;
	}
}
