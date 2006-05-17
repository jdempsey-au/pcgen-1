package pcgen.gui.tabs.resources;

import pcgen.core.Globals;
import pcgen.core.PCClass;
import pcgen.core.PlayerCharacter;
import pcgen.core.Race;
import pcgen.core.SettingsHandler;
import pcgen.core.character.CompanionMod;
import pcgen.core.character.Follower;
import pcgen.core.utils.CoreUtility;
import pcgen.gui.TableColumnManagerModel;
import pcgen.gui.utils.AbstractTreeTableModel;
import pcgen.gui.utils.PObjectNode;
import pcgen.gui.utils.TreeTableModel;
import pcgen.util.Logging;

import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *  The TreeTableModel has a single <code>root</code> node
 *  This root node has a null <code>parent</code>.
 *  All other nodes have a parent which points to a non-null node.
 *  Parent nodes contain a list of  <code>children</code>, which
 *  are all the nodes that point to it as their parent.
 *  <code>nodes</code> which have 0 children are leafs (the end of
 *  that linked list).  nodes which have at least 1 child are not leafs
 *  Leafs are like files and non-leafs are like directories.
 *  The leafs contain an Object that we want to know about (Equipment)
 **/
public final class SelectedFollowerModel extends AbstractTreeTableModel implements TableColumnManagerModel
{
	// column positions for Famliar tables
	// if you change these, you also have to change
	// the case statement in the FollowerModel declaration
	private static final int COL_NAME = 0;
	private static final int COL_TYPE = 1;
	private static final int COL_FILE = 2;

	private List displayList = null;

	// there are two roots. One for available equipment
	// and one for selected equipment profiles
	private PObjectNode selRoot;

	// list of columns names
	private String[] selNameList = { "Type/Name", "Type/Race", "File Name" };

	private final int[] selDefaultWidth = { 200, 100, 100 };

	private PlayerCharacter pc;

	// view modes for Item tables
	//private static final int ITEM_VIEW_TYPE = 0;
	//private static final int ITEM_VIEW_NAME = 1;
	// view modes for Vehicle tables
	//private static final int VEHICLE_VIEW_TYPE = 0;
	//private static final int VEHICLE_VIEW_NAME = 1;

	/**
	 * Creates a FollowerModel
	 * @param pc
	 **/
	public SelectedFollowerModel(PlayerCharacter pc)
	{
		super(null);

		setCharacter(pc);

		resetModel();

		int i = 1;
		displayList = new ArrayList();
		displayList.add(new Boolean(true));
		displayList.add(new Boolean(getColumnViewOption(selNameList[i++], true)));
		displayList.add(new Boolean(getColumnViewOption(selNameList[i++], true)));
	}

	/**
	 * Returns boolean if can edit a cell.
	 * @param node
	 * @param column
	 * @return true if cell editable
	 **/
	public boolean isCellEditable(Object node, int column)
	{
		return (column == COL_NAME);
	}

	/**
	 * Returns Class for the column
	 * @param column
	 * @return Class
	 **/
	public Class getColumnClass(int column)
	{
		return (column == COL_NAME) ? TreeTableModel.class : String.class;
	}

	/* The JTreeTableNode interface. */

	/**
	 * Returns int number of columns
	 * @return column count
	 **/
	public int getColumnCount()
	{
		return selNameList.length;
	}

	/**
	 * Returns String name of a column
	 * @param column
	 * @return column name
	 **/
	public String getColumnName(int column)
	{
		return selNameList[column];
	}

	/**
	 * return the root node
	 * @return root
	 **/
	public Object getRoot()
	{
		return (PObjectNode) super.getRoot();
	}

	/**
	 * Set the pc
	 * @param pc
	 */
	public void setCharacter(PlayerCharacter pc) {
		this.pc = pc;
	}

	/**
	 * return the value of a column
	 * @param node
	 * @param column
	 * @return value
	 **/
	public Object getValueAt(Object node, int column)
	{
		final PObjectNode fn = (PObjectNode) node;
		Race race = null;
		Follower fObj = null;
		String sRet = "";

		if (fn == null)
		{
			Logging.errorPrint("No active node when doing getValueAt in InfoRace");

			return null;
		}

		if (fn.getItem() instanceof Race)
		{
			race = (Race) fn.getItem();
		}
		else if (fn.getItem() instanceof Follower)
		{
			fObj = (Follower) fn.getItem();
		}

		column = adjustColumnConst(column);

		switch (column)
		{
			case COL_NAME:

				if (race != null)
				{
					return race.getKeyName();
				}
				return fn.toString();

			case COL_TYPE:

				if (fObj != null)
				{
					sRet = fObj.getType();
				}

				break;

			case COL_FILE:

				if (fObj != null)
				{
					sRet = fObj.getFileName();
				}

				break;

			default:
				Logging.errorPrint("In InfoResources.FollowerModel.getValueAt the column " + column
					+ " is not handled.");

				break;
		}

		return sRet;
	}

	/**
	 * There must be a root node, but we keep it hidden
	 * @param aNode
	 **/
	private void setRoot(PObjectNode aNode)
	{
		super.setRoot(aNode);
	}

	/**
	 * changes the column order sequence and/or number of
	 * columns based on modelType (0=available, 1=selected)
	 * @param column
	 * @return int
	 **/
	private int adjustColumnConst(int column)
	{
		return column;
	}

	/**
	 * This assumes the FollowerModel exists but
	 * needs branches and nodes to be repopulated
	 **/
	public void resetModel()
	{
		List selectedList = buildCurrentCompanionsList();
		buildSelectedResources(selectedList);


		PObjectNode rootAsPObjectNode = (PObjectNode) getRoot();
		if (rootAsPObjectNode.getChildCount() > 0)
		{
			fireTreeNodesChanged(super.getRoot(), new TreePath(super.getRoot()));
		}
	}

	/**
	 * @param selectedList
	 */
	private void buildSelectedResources(List selectedList) {
		// this is the root node
		selRoot = new PObjectNode();
		setRoot(selRoot);

		PObjectNode[] sl = new PObjectNode[selectedList.size()];

		for (int iSel = 0; iSel < selectedList.size(); iSel++)
		{
			String sString = (String) selectedList.get(iSel);
			sl[iSel] = new PObjectNode();
			sl[iSel].setItem(sString);
			sl[iSel].setParent(selRoot);

			for (Iterator fList = pc.getFollowerList().iterator(); fList.hasNext();)
			{
				Follower aF = (Follower) fList.next();

				if (!sString.startsWith(aF.getType()))
				{
					continue;
				}

				PObjectNode aFN = new PObjectNode();
				aFN.setItem(aF);
				aFN.setParent(sl[iSel]);
				sl[iSel].addChild(aFN);
			}
		}

		// new add to the root node
		selRoot.setChildren(sl);
	}


	/**
	 * @return current companions list
	 */
	private List buildCurrentCompanionsList() {
		ArrayList selectedList = new ArrayList();
		selectedList.add("Followers");

		for (Iterator iComp = Globals.getCompanionModList().iterator(); iComp.hasNext();)
		{
			final CompanionMod aComp = (CompanionMod) iComp.next();
			final String compType = CoreUtility.capitalizeFirstLetter(aComp.getType());

			for (Iterator iType = aComp.getVarMap().keySet().iterator(); iType.hasNext();)
			{
				final String varName = (String) iType.next();

				if ((pc.getVariableValue(varName, "").intValue() > 0) && (!selectedList.contains(compType)))
				{
					selectedList.add(compType);
				}
			}
		}

		for (Iterator iClass = pc.getClassList().iterator(); iClass.hasNext();)
		{
			final PCClass aClass = (PCClass) iClass.next();

			if (Globals.getCompanionModList().isEmpty())
			{
				continue;
			}

			for (Iterator iComp = Globals.getCompanionModList().iterator(); iComp.hasNext();)
			{
				final CompanionMod aComp = (CompanionMod) iComp.next();
				final String compType = CoreUtility.capitalizeFirstLetter(aComp.getType());

				if ((aComp.getClassMap().containsKey(aClass.getKeyName())) && (!selectedList.contains(compType)))
				{
					selectedList.add(compType);
				}
			}
		}

		return selectedList;
	}

	public List getMColumnList() {
		List retList = new ArrayList();
		for(int i = 1; i < selNameList.length; i++) {
			retList.add(selNameList[i]);
		}
		return retList;
	}

	public boolean isMColumnDisplayed(int col) {
		return ((Boolean)displayList.get(col)).booleanValue();
	}

	public void setMColumnDisplayed(int col, boolean disp) {
		setColumnViewOption( selNameList[col], disp);
		displayList.set(col, new Boolean(disp));
	}

	public int getMColumnOffset() {
		return 1;
	}

	public int getMColumnDefaultWidth(int col) {
		return SettingsHandler.getPCGenOption("InfoResources.FollowerModel.sizecol." + selNameList[col], selDefaultWidth[col]);
	}

	public void setMColumnDefaultWidth(int col, int width) {
		SettingsHandler.setPCGenOption("InfoResources.FollowerModel.sizecol." + selNameList[col], width);
	}

	private boolean getColumnViewOption(String colName, boolean defaultVal) {
		return SettingsHandler.getPCGenOption("InfoResources.FollowerModel.viewcol." + colName, defaultVal);
	}

	private void setColumnViewOption(String colName, boolean val) {
		SettingsHandler.setPCGenOption("InfoResources.FollowerModel.viewcol." + colName, val);
	}

	public void resetMColumn(int col, TableColumn column) {
		// TODO Auto-generated method stub

	}
}
