package pcgen.gui2.facade;

import pcgen.AbstractCharacterTestCase;
import pcgen.cdom.base.Constants;
import pcgen.core.BodyStructure;
import pcgen.core.Equipment;
import pcgen.core.PlayerCharacter;
import pcgen.core.SettingsHandler;
import pcgen.core.SystemCollections;
import pcgen.core.character.EquipSet;
import pcgen.core.character.EquipSlot;
import pcgen.core.facade.EquipmentSetFacade.EquipNode;
import pcgen.core.facade.EquipmentSetFacade.EquipNode.NodeType;
import pcgen.core.facade.util.ListFacade;
import pcgen.gui2.facade.EquipmentSetFacadeImpl.EquipNodeImpl;

/**
 * The Class <code>EquipmentSetFacadeImplTest</code> is a test class for
 * EquipmentSetFacadeImpl. 
 *
 * <br/>
 * Last Editor: $Author$
 * Last Edited: $Date$
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision$
 */
public class EquipmentSetFacadeImplTest extends AbstractCharacterTestCase
{
	private static final String LOC_HANDS = "HANDS";
	private static final String LOC_BOTH_HANDS = "Both Hands";
	private static final String SLOT_WEAPON = "Weapon";
	private static final int NUM_BASE_NODES = 7;

	MockDataSetFacade dataset;
	MockUIDelegate uiDelegate;
	
	/**
	 * Check that EquipmentSetFacadeImpl can be initialised with an empty dataset.  
	 */
	public void testEmptyInit()
	{
		EquipSet es = new EquipSet("0.1", "Unit Test Equip");
		EquipmentSetFacadeImpl esfi =
				new EquipmentSetFacadeImpl(uiDelegate, getCharacter(), es,
					dataset);
		ListFacade<EquipNode> nodeList = esfi.getNodes();
		assertFalse("Expected a non empty node set", nodeList.isEmpty());
		assertEquals("Incorrect name of base node", Constants.EQUIP_LOCATION_EQUIPPED,
			nodeList.getElementAt(0).toString());
		assertEquals("Incorrect nunber of nodes found", NUM_BASE_NODES, nodeList.getSize());
	}
	
	/**
	 * Check that EquipmentSetFacadeImpl can be initialised with a dataset 
	 * containing equipment.  
	 */
	public void testInitWithEquipment()
	{
		PlayerCharacter pc = getCharacter();
		EquipSet es = new EquipSet("0.1", "Unit Test Equip");
		Equipment item = new Equipment();
		item.setName("Satchel");
		Equipment item2 = new Equipment();
		item2.setName("Book");
		Equipment item3 = new Equipment();
		item3.setName("Quarterstaff");
		
		EquipSet satchelEs = addEquipToEquipSet(pc, es, item, 1.0f);
		addEquipToEquipSet(pc, satchelEs, item2, 1.0f);
		addEquipToEquipSet (pc, es, item3, 1.0f, LOC_BOTH_HANDS);

		EquipmentSetFacadeImpl esfi =
				new EquipmentSetFacadeImpl(uiDelegate, pc, es, dataset);
		ListFacade<EquipNode> nodeList = esfi.getNodes();
		assertFalse("Expected a non empty path set", nodeList.isEmpty());
		EquipNodeImpl node = (EquipNodeImpl) nodeList.getElementAt(0);
		assertEquals("Incorrect body struct name", Constants.EQUIP_LOCATION_EQUIPPED, node.toString());
		assertEquals("Incorrect body struct type", NodeType.BODY_SLOT, node.getNodeType());
		assertEquals("Incorrect sort key", "00", node.getSortKey());
		assertEquals("Incorrect parent", null, node.getParent());
		node = (EquipNodeImpl) nodeList.getElementAt(NUM_BASE_NODES);
		assertEquals("Incorrect container name", item.getName(), node.toString());
		assertEquals("Incorrect container type", NodeType.EQUIPMENT, node.getNodeType());
		assertEquals("Incorrect sort key", "00|"+item.getName(), node.getSortKey());
		assertEquals("Incorrect parent", nodeList.getElementAt(0), node.getParent());
		node = (EquipNodeImpl) nodeList.getElementAt(NUM_BASE_NODES+2);
		assertEquals("Incorrect item name", item2.getName(), node.toString());
		assertEquals("Incorrect item type", NodeType.EQUIPMENT, node.getNodeType());
		assertEquals("Incorrect sort key", "00|"+item.getName()+"|"+item2.getName(), node.getSortKey());
		assertEquals("Incorrect parent", nodeList.getElementAt(NUM_BASE_NODES), node.getParent());
		node = (EquipNodeImpl) nodeList.getElementAt(NUM_BASE_NODES+1);
		assertEquals("Incorrect item name", item3.getName(), node.toString());
		assertEquals("Incorrect item type", NodeType.EQUIPMENT, node.getNodeType());
		assertEquals("Incorrect sort key", "01|"+item3.getName(), node.getSortKey());
		assertEquals("Incorrect parent", LOC_HANDS, node.getParent().toString());
		node = (EquipNodeImpl) nodeList.getElementAt(NUM_BASE_NODES+2);
		EquipNode parent = node.getParent();
		assertEquals("Root incorrect", Constants.EQUIP_LOCATION_EQUIPPED, parent.getParent().toString());
		assertEquals("Leaf incorrect", item.getName(), parent.toString());
		assertEquals("Incorrect nuber of paths found", NUM_BASE_NODES+3, nodeList.getSize());
	}
	
	/**
	 * Check that EquipmentSetFacadeImpl can manage addition and removal of equipment.  
	 */
	public void testAddRemove()
	{
		EquipSet es = new EquipSet("0.1", "Unit Test Equip");
		EquipmentSetFacadeImpl esfi =
				new EquipmentSetFacadeImpl(uiDelegate, getCharacter(), es,
					dataset);
		EquipNode root = esfi.getNodes().getElementAt(0);
		Equipment item = new Equipment();
		item.setName("Dart");
		assertEquals("Initial num carried", 0, item.getCarried(), 0.01);
		assertEquals("Initial num equipped", 0, item.getNumberEquipped());
		assertEquals("Initial node list size", NUM_BASE_NODES, esfi.getNodes().getSize());

		Equipment addedEquip = (Equipment) esfi.addEquipment(root, item, 2);
		assertEquals("First add num carried", 2, addedEquip.getCarried(), 0.01);
		assertEquals("First add num equipped", 2, addedEquip.getNumberEquipped());
		assertEquals("Should be no sideeffects to num carried", 0, item.getCarried(), 0.01);
		assertEquals("Should be no sideeffects to equipped", 0, item.getNumberEquipped());
		assertEquals("First add node list size", NUM_BASE_NODES+1, esfi.getNodes().getSize());
		assertEquals("generated equip set id", "0.1.1", ((EquipNodeImpl)esfi.getNodes().getElementAt(NUM_BASE_NODES)).getIdPath());

		Equipment secondEquip = (Equipment) esfi.addEquipment(root, item, 1);
		assertEquals("Second add num carried", 3, secondEquip.getCarried(), 0.01);
		assertEquals("Second add num equipped", 3, secondEquip.getNumberEquipped());
		assertEquals("Should be no sideeffects to num carried", 0, item.getCarried(), 0.01);
		assertEquals("Should be no sideeffects to equipped", 0, item.getNumberEquipped());
		assertTrue("Same equipment item should have been used", addedEquip == secondEquip);
		assertEquals("First add node list size", NUM_BASE_NODES+1, esfi.getNodes().getSize());

		EquipNode target =  esfi.getNodes().getElementAt(NUM_BASE_NODES);
		Equipment removedEquip = (Equipment) esfi.removeEquipment(target, 2);
		assertEquals("First add num carried", 1, removedEquip.getCarried(), 0.01);
		assertEquals("First add num equipped", 1, removedEquip.getNumberEquipped());
		assertTrue("Same equipment item should have been used", addedEquip == removedEquip);
		assertEquals("Should be no sideeffects to num carried", 0, item.getCarried(), 0.01);
		assertEquals("Should be no sideeffects to equipped", 0, item.getNumberEquipped());
		assertEquals("First add node list size", NUM_BASE_NODES+1, esfi.getNodes().getSize());

		esfi.removeEquipment(target, 1);
		assertEquals("First add num carried", 0, addedEquip.getCarried(), 0.01);
		assertEquals("First add num equipped", 0, addedEquip.getNumberEquipped());
		assertEquals("Should be no sideeffects to num carried", 0, item.getCarried(), 0.01);
		assertEquals("Should be no sideeffects to equipped", 0, item.getNumberEquipped());
		assertEquals("First add node list size", NUM_BASE_NODES, esfi.getNodes().getSize());
	}

	/**
	 * Add the equipment item to the equipset.
	 * 
	 * @param pc The character owning the set
	 * @param es The set to add the item to
	 * @param item The item of equipment
	 * @param qty The number to be placed in the location.
	 * @return The new EquipSet object for the item.
	 */
	private EquipSet addEquipToEquipSet(PlayerCharacter pc, EquipSet es,
		Equipment item, float qty)
	{
		return addEquipToEquipSet(pc, es, item, qty, Constants.EQUIP_LOCATION_EQUIPPED);
	}
	/**
	 * Add the equipment item to the equipset.
	 * 
	 * @param pc The character owning the set
	 * @param es The set to add the item to
	 * @param item The item of equipment
	 * @param qty The number to be placed in the location.
	 * @return The new EquipSet object for the item.
	 */
	private EquipSet addEquipToEquipSet(PlayerCharacter pc, EquipSet es,
		Equipment item, float qty, String locName)
	{
		String id = EquipmentSetFacadeImpl.getNewIdPath(pc, es);
		EquipSet newSet = new EquipSet(id, locName, item.getName(), item);
		item.setQty(qty);
		newSet.setQty(1.0f);
		pc.addEquipSet(newSet);
		return newSet;
	}

	/* (non-Javadoc)
	 * @see pcgen.AbstractCharacterTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		dataset = new MockDataSetFacade();
		dataset.addEquipmentLocation(new BodyStructure(Constants.EQUIP_LOCATION_EQUIPPED, true));
		dataset.addEquipmentLocation(new BodyStructure(LOC_HANDS, false));
		if (SystemCollections.getUnmodifiableEquipSlotList().isEmpty())
		{
			EquipSlot equipSlot = new EquipSlot();
			equipSlot.setSlotName(SLOT_WEAPON);
			equipSlot.addContainedType("Weapon");
			equipSlot.setContainNum(1);
			equipSlot.setSlotNumType("HANDS");
			SystemCollections.addToEquipSlotsList(equipSlot, SettingsHandler.getGame().getName());
		}
		uiDelegate = new MockUIDelegate();
	}
}
