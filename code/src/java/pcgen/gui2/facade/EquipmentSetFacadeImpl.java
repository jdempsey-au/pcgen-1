/**
 * EquipmentSetFacadeImpl.java
 * Copyright James Dempsey, 2010
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
 * Created on 15/01/2011 3:17:17 PM
 *
 * $Id$
 */
package pcgen.gui2.facade;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pcgen.cdom.base.Constants;
import pcgen.cdom.enumeration.StringKey;
import pcgen.core.BodyStructure;
import pcgen.core.Equipment;
import pcgen.core.Globals;
import pcgen.core.PlayerCharacter;
import pcgen.core.SystemCollections;
import pcgen.core.character.EquipSet;
import pcgen.core.character.EquipSlot;
import pcgen.core.display.CharacterDisplay;
import pcgen.core.facade.BodyStructureFacade;
import pcgen.core.facade.DataSetFacade;
import pcgen.core.facade.DefaultReferenceFacade;
import pcgen.core.facade.EquipmentFacade;
import pcgen.core.facade.EquipmentListFacade;
import pcgen.core.facade.EquipmentListFacade.EquipmentListEvent;
import pcgen.core.facade.EquipmentListFacade.EquipmentListListener;
import pcgen.core.facade.EquipmentSetFacade;
import pcgen.core.facade.EquipmentSetFacade.EquipNode.NodeType;
import pcgen.core.facade.ReferenceFacade;
import pcgen.core.facade.UIDelegate;
import pcgen.core.facade.event.ListEvent;
import pcgen.core.facade.event.ListListener;
import pcgen.core.facade.util.DefaultListFacade;
import pcgen.core.facade.util.ListFacade;
import pcgen.system.LanguageBundle;
import pcgen.util.Logging;

/**
 * The Class <code>EquipmentSetFacadeImpl</code> is an implementation of 
 * the EquipmentSetFacade interface for the new user interface. It handles 
 * the interaction with the UI and the character with respect a grouping 
 * of the character's gear. This covers what is carried, what is not and 
 * where each item is located. As a result it also manages what items are 
 * deemed active.
 *
 * <br/>
 * Last Editor: $Author$
 * Last Edited: $Date$
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision$
 */
public class EquipmentSetFacadeImpl implements EquipmentSetFacade,
		EquipmentListListener, ListListener<EquipmentFacade>
{
	private final PlayerCharacter theCharacter;
	private final CharacterDisplay charDisplay;
	private EquipSet eqSet;
	private List<EquipmentTreeListener> listeners = new ArrayList<EquipmentTreeListener>();
	private DefaultReferenceFacade<String> name;
	private EquipmentListFacadeImpl equippedItemsList;
	private DataSetFacade dataSet;
	private Map<String, BodyStructure> bodyStructMap;
	private UIDelegate delegate;
	private double totalWeight = 0;
	private DefaultListFacade<EquipNode> nodeList;
	private Map<EquipSlot, EquipNode> equipSlotNodeMap;
	private Map<String, EquipNodeImpl> naturalWeaponNodes;
	private final EquipmentListFacadeImpl purchasedList;
	
	/**
	 * Create a new Equipment Set Facade implementation for an existing 
	 * equipset.
	 * 
	 * @param delegate The user interface delegate for notifying the user. 
	 * @param pc The character the set belongs to.
	 * @param eqSet The set.
	 * @param dataSet The datasets that the character is using.
	 * @param purchasedList The list of the charcter's purchased equipment.
	 */
	public EquipmentSetFacadeImpl(UIDelegate delegate, PlayerCharacter pc,
		EquipSet eqSet, DataSetFacade dataSet,
		EquipmentListFacadeImpl purchasedList)
	{
		this.delegate = delegate;
		this.theCharacter = pc;
		this.charDisplay = pc.getDisplay();
		this.dataSet = dataSet;
		this.purchasedList = purchasedList;
		initForEquipSet(eqSet);
		
		purchasedList.addEquipmentListListener(this);
		purchasedList.addListListener(this);
	}


	private void initForEquipSet(EquipSet equipSet)
	{
		this.eqSet = equipSet;
		name = new DefaultReferenceFacade<String>(equipSet.getName());
		bodyStructMap = new HashMap<String, BodyStructure>();
		equippedItemsList = new EquipmentListFacadeImpl();
		naturalWeaponNodes = new HashMap<String, EquipmentSetFacadeImpl.EquipNodeImpl>();

		for (BodyStructureFacade bodyStruct : dataSet.getEquipmentLocations())
		{
			bodyStructMap.put(bodyStruct.toString(), (BodyStructure) bodyStruct);
			// Add a 'base' equippath entry for each body structure
		}
		
		buildNodeList();

		List<EquipSet> equipList = new ArrayList<EquipSet>(charDisplay.getEquipSet());
		Collections.sort(equipList);
		createNaturalWeaponSlots();
		updateNaturalWeaponSlots();
		addChildrenToPath(equipSet.getIdPath(), equipList, (EquipNodeImpl) null);
	}

	private void buildNodeList()
	{
		nodeList = new DefaultListFacade<EquipNode>();
		equipSlotNodeMap = new LinkedHashMap<EquipSlot, EquipNode>();
		int index = 0;
		for (BodyStructureFacade bodyStruct : dataSet.getEquipmentLocations())
		{
			String structString = bodyStruct.toString();
			EquipNodeImpl node = new EquipNodeImpl((BodyStructure) bodyStruct, index++);
			nodeList.addElement(node);
			
			// Add locations for this body structure
			for (EquipSlot slot : SystemCollections.getUnmodifiableEquipSlotList())
			{
				String bodyStructureName = slot.getBodyStructureName();
				final String hands = "HANDS";
				if ("Ring".equalsIgnoreCase(slot.getSlotName()) || "Fingers".equalsIgnoreCase(slot.getSlotName()))
				{
					bodyStructureName = hands;
				}
				if (bodyStructureName.equalsIgnoreCase(structString))
				{
					if (slot.canContainType("WEAPON"))
					{
						// Add phantom nodes for the various weapon slots
						if (charDisplay.getHands() > 0)
						{
							addEquipNodeForEquipSlot(
								node,
								createWeaponEquipSlot(slot,
									Constants.EQUIP_LOCATION_PRIMARY), true);
						}
						for (int i = 1; i < charDisplay.getHands(); ++i)
						{
							if (i > 1)
							{
								addEquipNodeForEquipSlot(
									node,
									createWeaponEquipSlot(slot,
										Constants.EQUIP_LOCATION_SECONDARY
											+ " " + i), true);
							}
							else
							{
								addEquipNodeForEquipSlot(
									node,
									createWeaponEquipSlot(slot,
										Constants.EQUIP_LOCATION_SECONDARY),
									true);
							}
						}

						addEquipNodeForEquipSlot(node, createWeaponEquipSlot(
							slot, Constants.EQUIP_LOCATION_DOUBLE), true);
						addEquipNodeForEquipSlot(node, createWeaponEquipSlot(
							slot, Constants.EQUIP_LOCATION_BOTH), true);
						addEquipNodeForEquipSlot(node, createWeaponEquipSlot(
							slot, Constants.EQUIP_LOCATION_UNARMED), true);
					}
					else
					{
						addEquipNodeForEquipSlot(node, slot, false);
					}
				}
			}
		}
	}

	private EquipSlot createWeaponEquipSlot(EquipSlot slot, String slotName)
	{
		EquipSlot wpnSlot = slot.clone();
		wpnSlot.setSlotName(slotName);
		return wpnSlot;
	}

	/**
	 * Create a new EquipNodeImpl for the slot and add it the node list.  
	 * @param bodyStructNode The parent body structure node
	 * @param slot The equipment slot
	 * @param singleOnly Can the slot only ever have a single entry. e.g. weapon slots
	 */
	private void addEquipNodeForEquipSlot(EquipNodeImpl bodyStructNode,
		EquipSlot slot, boolean singleOnly)
	{
		EquipNodeImpl slotNode =
				new EquipNodeImpl(bodyStructNode, slot, singleOnly);
		nodeList.addElement(slotNode);
		equipSlotNodeMap.put(slot, slotNode);
	}

	/**
	 * Recursive method to build up a tree of EquipNodes. It finds from the 
	 * equipment list those children of the item specified by the idPath and
	 * adds them to the paths list. It will then do the same for each child 
	 * that was found. If the parent is null then all direct children will be 
	 * attached to the relevant body structure nodes. 
	 *  
	 * @param idPath The equipset id of the parent record.
	 * @param equipList The list of equipsets to be added.
	 * @param parent The parent node 
	 */
	private void addChildrenToPath(String idPath, List<EquipSet> equipList, EquipNodeImpl parent)
	{
		List<EquipNodeImpl> children = new ArrayList<EquipNodeImpl>();

		// process all EquipNodeImpl Items
		for (int iSet = 0; iSet < equipList.size(); ++iSet)
		{
			EquipSet es = equipList.get(iSet);

			if (es.getParentIdPath().equals(idPath))
			{
				EquipSlot slot = Globals.getEquipSlotByName(es.getName());
				EquipNodeImpl slotNode = (EquipNodeImpl) equipSlotNodeMap.get(slot);
				EquipNodeImpl parentNode = parent;
				if (parentNode == null)
				{
					if (slotNode != null)
					{
						parentNode = (EquipNodeImpl) slotNode.getParent();
					}
					else
					{
						for (EquipNode scanNode : nodeList)
						{
							if (scanNode.getNodeType() == NodeType.BODY_SLOT
								&& scanNode.toString().equals(es.getName()))
							{
								parentNode = (EquipNodeImpl) scanNode;
								break;
							}
							if (scanNode.getNodeType() == NodeType.PHANTOM_SLOT
								&& scanNode.toString().equals(es.getName()))
							{
								parentNode =
										(EquipNodeImpl) scanNode.getParent();
								slotNode = (EquipNodeImpl) scanNode;
								slot = ((EquipNodeImpl) scanNode).getSlot();
								break;
							}
						}
					}
				}
	
				if (parentNode != null)
				{
					EquipNodeImpl node =
							new EquipNodeImpl(parentNode, slot, es.getItem(),
								es.getIdPath());
					nodeList.addElement(node);
					if (slotNode != null
						&& slotNode.getNodeType() == NodeType.PHANTOM_SLOT
						&& getNumFreeSlots(slotNode) <= 0)
					{
						nodeList.removeElement(slotNode);
						for (EquipNode inompatNode : getIncompatibleWeaponSlots(slotNode))
						{
							nodeList.removeElement(inompatNode);
						}
					}
					
					updateTotalQuantity(es.getItem(), es.getItem().getQty()
						.intValue());
					updateTotalWeight(es.getItem(), es.getItem().getQty(),
						parentNode.getBodyStructure());
	
					// add to list for recursive calls
					children.add(node);
				}
				else
				{
					Logging.errorPrint("Could not find parent for "
						+ es.getName() + " for item " + es.getItem()
						+ " at path " + es.getIdPath());
				}

				// and remove from tempSetList so
				// it won't get processed again
				equipList.remove(es);
				--iSet;
			}
		}

		// Now process the children
		for (EquipNodeImpl equipNodeImpl : children)
		{
			addChildrenToPath(equipNodeImpl.getIdPath(), equipList, equipNodeImpl);
		}
	}

	/**
	 * Make this equipment set the active one. 
	 */
	protected void activateEquipSet()
	{
		theCharacter.setCalcEquipSetId(eqSet.getIdPath());
		theCharacter.setCalcEquipmentList();
		updateOutputOrder();
		theCharacter.setUseTempMods(eqSet.getUseTempMods());
		theCharacter.calcActiveBonuses();
		theCharacter.setDirty(true);
	}
	
	/**
	 * Add the weight for the item to the total for the set.
	 * 
	 * @param equip The equipment item being added
	 * @param quantity The number being added (or negative if being removed)
	 * @param root The BodyStructure in which the item is being placed or removed  
	 */
	private void updateTotalWeight(Equipment equip, float quantity, BodyStructureFacade root)
	{
		if (!Constants.EQUIP_LOCATION_NOTCARRIED.equals(root.toString()))
		{
			equip.setNumberCarried(equip.getCarried()+quantity);
			if (!Constants.EQUIP_LOCATION_CARRIED.equals(root.toString()))
			{
				equip.setNumberEquipped((int) (equip.getNumberEquipped()+quantity));
			}
		}

		theCharacter.setCalcEquipmentList();
		if (!Constants.EQUIP_LOCATION_NOTCARRIED.equals(root.toString()))
		{
			totalWeight = charDisplay.totalWeight();
		}
	}

	/**
	 * returns new id_Path with the last id one higher than the current
	 * highest id for EquipSets with the same ParentIdPath.
	 * TODO: This needs to be moved to the core.
	 * @param display The display interface for the character owning the equipset.
	 * @param parentSet The parent of the equipset that is being created, null if it a root set.
	 * @return new id path
	 **/
	static String getNewIdPath(CharacterDisplay display, EquipSet parentSet)
	{
		String pid = "0";
		int newID = 0;

		if (parentSet != null)
		{
			pid = parentSet.getIdPath();
		}

		for (EquipSet es : display.getEquipSet())
		{
			if (es.getParentIdPath().equals(pid) && (es.getId() > newID))
			{
				newID = es.getId();
			}
		}

		++newID;

		return pid + '.' + newID;
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.EquipmentSetFacade#addEquipment(pcgen.core.facade.EquipmentSetFacade.EquipNode, pcgen.core.facade.EquipmentFacade, int)
	 */
	@Override
	public EquipmentFacade addEquipment(EquipNode node, EquipmentFacade equipment,
		int quantity)
	{
		if (!(node instanceof EquipNodeImpl))
		{
			return null;
		}
		if (!(equipment instanceof Equipment))
		{
			return null;
		}

		Equipment item = (Equipment) equipment;
		EquipNodeImpl targetNode = (EquipNodeImpl) node;
		
		// Validate the item can go into the location.
		if (!canEquip(targetNode, equipment))
		{
			delegate.showErrorMessage(Constants.APPLICATION_NAME, LanguageBundle
				.getFormattedString("in_equipCannotEquipToLocation", item
					.toString(), targetNode.toString()));
			return null;
		}

		EquipNodeImpl parent;
		EquipSet parentEs;
		EquipSlot equipSlot;
		String locName;
		switch (targetNode.getNodeType())
		{
			case BODY_SLOT:
				parent = targetNode;
				parentEs = eqSet;
				equipSlot = null;
				locName = parent.toString();
				break;

			case PHANTOM_SLOT:
				parent = (EquipNodeImpl) targetNode.getParent();
				parentEs = eqSet;
				equipSlot = targetNode.getSlot();
				locName = equipSlot.toString();
				break;

			case EQUIPMENT:
				parent = targetNode;
				parentEs = charDisplay.getEquipSetByIdPath(parent.getIdPath());
				equipSlot = targetNode.getSlot();
				locName = parent.toString();
				break;
				
			default:
				// Should never occur
				return null;
		}
		
		// Check for adding more instances to an existing item, but don't merge containers
		if (!item.isContainer())
		{
			for (EquipNode existing : nodeList)
			{
				if (parent.equals(existing.getParent())
					&& existing.getNodeType() == NodeType.EQUIPMENT)
				{
					EquipNodeImpl existingImpl = (EquipNodeImpl) existing;
					if (equipSlot != null && !equipSlot.equals(existingImpl.getSlot()))
					{
						continue;
					}

					Equipment existingItem =
							(Equipment) existing.getEquipment();
					if (existingItem.equals(item))
					{
						int totalQuantity = (int) (existingItem.getQty() + quantity);
						existingItem.setQty(totalQuantity);
						EquipSet es =
								charDisplay
									.getEquipSetByIdPath(((EquipNodeImpl) existing)
										.getIdPath());
						es.setQty(es.getQty() + quantity);
						updateTotalWeight(existingItem, quantity, parent.getBodyStructure());
						fireQuantityChanged(existing);
						updateTotalQuantity(existingItem, quantity);
						updateNaturalWeaponSlots();
						return existingItem;
					}
				}
			}
		}
		
		// Create equip set for the item
		String id = EquipmentSetFacadeImpl.getNewIdPath(charDisplay, parentEs);
		Equipment newItem = item.clone();
		EquipSet newSet = new EquipSet(id, locName, newItem.getName(), newItem);
		newItem.setQty(quantity);
		newSet.setQty((float) quantity);
		theCharacter.addEquipSet(newSet);
		Equipment eqTarget = parentEs.getItem();
		if ((eqTarget != null) && eqTarget.isContainer())
		{
			eqTarget.insertChild(theCharacter, newItem);
			newItem.setParent(eqTarget);
		}
		
		// Create EquipNode for the item
		EquipNodeImpl itemNode = new EquipNodeImpl(parent, equipSlot, newItem, id);
		nodeList.addElement(itemNode);
		if (targetNode.getNodeType() == NodeType.PHANTOM_SLOT
			&& getNumFreeSlots(targetNode) <= 0)
		{
			nodeList.removeElement(targetNode);
			for (EquipNode inompatNode : getIncompatibleWeaponSlots(targetNode))
			{
				nodeList.removeElement(inompatNode);
			}
		}
		
		updateTotalWeight(newItem, quantity, parent.getBodyStructure());
		updateTotalQuantity(newItem, quantity);
		updateNaturalWeaponSlots();
		updateOutputOrder();
		theCharacter.calcActiveBonuses();
		
		return newItem;
	}
	
	/**
	 * Reorder the equipment for output to cater for any changes in the 
	 * equipment list. Note this assumes this equipment set is the active one 
	 * as it updates the character's master equipment list.
	 */
	private void updateOutputOrder()
	{
		List<EquipNode> orderedEquipNodes =
				new ArrayList<EquipmentSetFacade.EquipNode>(
					nodeList.getContents());
		Collections.sort(orderedEquipNodes);
		List<Equipment> processed =
				new ArrayList<Equipment>(orderedEquipNodes.size());

		int outputIndex = 1;
		for (EquipNode equipNode : orderedEquipNodes)
		{
			if (equipNode.getEquipment() != null)
			{
				Equipment equip =
						theCharacter.getEquipmentNamed(equipNode.getEquipment()
							.toString());
				// If an item is split in multiple places, don't overwrite its order
				if (equip != null && !processed.contains(equip))
				{
					equip.setOutputIndex(outputIndex++);
					processed.add(equip);
				}
			}
		}

	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.EquipmentSetFacade#removeEquipment(pcgen.core.facade.EquipmentSetFacade.EquipNode, int)
	 */
	@Override
	public EquipmentFacade removeEquipment(EquipNode node, int quantity)
	{
		if (!(node instanceof EquipNodeImpl) || node.getNodeType()!= NodeType.EQUIPMENT)
		{
			return null;
		}
		
		EquipNodeImpl targetNode = (EquipNodeImpl) node;
		EquipNodeImpl parentNode = (EquipNodeImpl) node.getParent();
		EquipSet eSet = charDisplay.getEquipSetByIdPath(targetNode.getIdPath());
		if (eSet == null)
		{
			Logging.errorPrint("No equipset found for node " + targetNode
				+ " path " + targetNode.getIdPath());
			return null;
		}
		int newQty = (int) (eSet.getQty()-quantity);

		Equipment eqI = eSet.getItem();
		
		if (newQty <= 0)
		{
			// If it was a container, remove all children
			removeChildren(node);

			// remove Equipment (via EquipSet) from the PC
			theCharacter.delEquipSet(eSet);
			nodeList.removeElement(targetNode);
	
			// if it was inside a container, make sure to update
			// the container Equipment Object
			if (parentNode.getNodeType()==NodeType.EQUIPMENT)
			{
				Equipment eqP = eqI.getParent();
	
				if (eqP != null)
				{
					eqP.removeChild(theCharacter, eqI);
				}
			}
			else if (targetNode.getSlot() != null)
			{
				final EquipNodeImpl restoredNode = (EquipNodeImpl) equipSlotNodeMap.get(targetNode.getSlot());
				if (restoredNode != null && !nodeList.containsElement(restoredNode))
				{
					nodeList.addElement(0, restoredNode);
					addCompatWeaponSlots(restoredNode);
				}
			}
		}
		else
		{
			eSet.setQty((float) newQty);
			fireQuantityChanged(targetNode);
		}
		updateTotalWeight(eqI, quantity*-1, targetNode.getBodyStructure());
		updateTotalQuantity(eqI, quantity*-1);
		updateNaturalWeaponSlots();
		theCharacter.calcActiveBonuses();
		
		return eqI;
	}

	/**
	 * Remove all equipment items held within a supplied container
	 * @param parentNode The container node to be emptied
	 */
	private void removeChildren(EquipNode parentNode)
	{
		if (!(parentNode instanceof EquipNodeImpl) || parentNode.getNodeType()!= NodeType.EQUIPMENT)
		{
			return;
		}

		List<EquipNode> equipToBeRemoved = new ArrayList<EquipNode>(nodeList.getSize());
		for (EquipNode node : nodeList)
		{
			// Only select top level equipment, anything in a container will be removed along with the container. 
			if (node.getNodeType() == NodeType.EQUIPMENT
				&& node.getParent() == parentNode)
			{
				equipToBeRemoved.add(node);
			}
		}

		for (EquipNode node : equipToBeRemoved)
		{
			removeEquipment(node, getQuantity(node));
		}
	}

	/**
	 * Calculate a list of weapon slots that are not compatible with the 
	 * supplied slot. These can then be removed from the list to be displayed 
	 * when the slot is filled and added back in when the slot is empty. 
	 * 
	 * @param targetNode The node to be check.
	 * @return The list of incompatible nodes, empty if the target is not a weapon slot.
	 */
	private List<EquipNode> getIncompatibleWeaponSlots(EquipNodeImpl targetNode)
	{
		List<EquipNode> wpnList = new ArrayList<EquipNode>();
		if (targetNode.getNodeType() != NodeType.PHANTOM_SLOT)
		{
			return wpnList;
		}

		String incompatLocNames[] = {};
		final String slotName = targetNode.getSlot().toString();
		if (Constants.EQUIP_LOCATION_PRIMARY.equals(slotName))
		{
			incompatLocNames =
					new String[]{Constants.EQUIP_LOCATION_BOTH, Constants.EQUIP_LOCATION_DOUBLE};
		}
		else if (Constants.EQUIP_LOCATION_SECONDARY.equals(slotName))
		{
			incompatLocNames =
					new String[]{Constants.EQUIP_LOCATION_BOTH, Constants.EQUIP_LOCATION_DOUBLE,
						Constants.EQUIP_LOCATION_SHIELD};
		}
		else if (Constants.EQUIP_LOCATION_SHIELD.equals(slotName))
		{
			incompatLocNames =
					new String[]{Constants.EQUIP_LOCATION_BOTH, Constants.EQUIP_LOCATION_DOUBLE,
						Constants.EQUIP_LOCATION_SECONDARY};
		}
		else if (Constants.EQUIP_LOCATION_BOTH.equals(slotName))
		{
			incompatLocNames =
					new String[]{Constants.EQUIP_LOCATION_PRIMARY, Constants.EQUIP_LOCATION_DOUBLE,
						Constants.EQUIP_LOCATION_SECONDARY, Constants.EQUIP_LOCATION_SHIELD};
		}
		else if (Constants.EQUIP_LOCATION_DOUBLE.equals(slotName))
		{
			incompatLocNames =
					new String[]{Constants.EQUIP_LOCATION_PRIMARY, Constants.EQUIP_LOCATION_BOTH,
						Constants.EQUIP_LOCATION_SECONDARY, Constants.EQUIP_LOCATION_SHIELD};
		}
		//TODO: Extra secondary locations for more than 2 arms

		List<String> namesList = Arrays.asList(incompatLocNames);
		for (EquipSlot slot : equipSlotNodeMap.keySet())
		{
			if (namesList.contains(slot.toString()))
			{
				wpnList.add(equipSlotNodeMap.get(slot));
			}
		}
		return wpnList;
	}

	/**
	 * Add back to the list any weapon slots that are now valid again. Called 
	 * after a weapon is removed from a character and the weapon;s node has 
	 * been added back to the node list.
	 * 
	 * @param restoredNode The weapon equip node being restored.
	 */
	private void addCompatWeaponSlots(final EquipNodeImpl restoredNode)
	{
		List<EquipNode> weaponSlots = getIncompatibleWeaponSlots(restoredNode);
		Set<EquipNode> incompatNodes = new HashSet<EquipNode>();
		for (EquipNode equipNode : nodeList)
		{
			if (equipNode.getNodeType() == NodeType.EQUIPMENT
				&& affectsWeaponSlots(equipNode))
			{
				EquipSlot slot = ((EquipNodeImpl) equipNode).getSlot();
				if (slot !=  null)
				{
					incompatNodes
						.addAll(getIncompatibleWeaponSlots((EquipNodeImpl) equipSlotNodeMap
							.get(slot)));
				}
			}
		}
		weaponSlots.removeAll(incompatNodes);
		for (EquipNode node : weaponSlots)
		{
			nodeList.addElement(0, node);
		}
	}

	private boolean affectsWeaponSlots(EquipNode equipNode)
	{
		List<String> typeList =
				Arrays.asList(equipNode.getEquipment().getTypes());
		return typeList.contains("WEAPON") || typeList.contains("SHIELD");
	}

	/**
	 * Update the total quantity held of an item as recorded in the 
	 * equipmentList. This will add or remove the item from the list as 
	 * required. Items with the same name are aggregated to give a total
	 * amount held.
	 *  
	 * @param item The item to be updated
	 * @param amtChanged The amount by which the quantity has changed.
	 */
	private void updateTotalQuantity(Equipment item, int amtChanged)
	{
		for (EquipmentFacade equip : equippedItemsList)
		{
			if (item.equals(equip))
			{
				int newQty = equippedItemsList.getQuantity(equip) + amtChanged;
				if (newQty > 0)
				{
					equippedItemsList.setQuantity(equip, newQty);
				}
				else
				{
					equippedItemsList.removeElement(equip);
				}
				return;
			}
		}
		
		// Item is new so add it
		equippedItemsList.addElement(item, amtChanged);
	}

	/**
	 * Update the available natural weapon slots that are displayed. Will hide the 
	 * natural weapon slots unless there are unequipped natural weapons.  
	 */
	private void updateNaturalWeaponSlots()
	{
		if (pcHasUnequippedNaturalWeapons())
		{
			// Ensure natural weapon locations are visible
			for (EquipNodeImpl natWpnEquipNode : naturalWeaponNodes.values())
			{
				if (!nodeList.containsElement(natWpnEquipNode))
				{
					nodeList.addElement(natWpnEquipNode);
				}
			}
		}
		else
		{
			// Ensure natural weapon locations are not visible
			for (EquipNodeImpl natWpnEquipNode : naturalWeaponNodes.values())
			{
				if (nodeList.containsElement(natWpnEquipNode))
				{
					nodeList.removeElement(natWpnEquipNode);
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see pcgen.core.facade.EquipmentSetFacade#getName()
	 */
	public String getName()
	{
		return name.getReference();
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.EquipmentSetFacade#isContainer(pcgen.core.facade.EquipmentFacade)
	 */
	@Override
	public boolean isContainer(EquipmentFacade equipment)
	{
		if (!(equipment instanceof Equipment))
		{
			return false;
		}

		Equipment item = (Equipment) equipment;
		return item.isContainer();
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.EquipmentSetFacade#setName(java.lang.String)
	 */
	@Override
	public void setName(String name)
	{
		this.name.setReference(name);
		eqSet.setName(name);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.EquipmentSetFacade#addEquipmentTreeListener(pcgen.core.facade.EquipmentSetFacade.EquipmentTreeListener)
	 */
	@Override
	public void addEquipmentTreeListener(EquipmentTreeListener listener)
	{
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.EquipmentSetFacade#removeEquipmentTreeListener(pcgen.core.facade.EquipmentSetFacade.EquipmentTreeListener)
	 */
	@Override
	public void removeEquipmentTreeListener(EquipmentTreeListener listener)
	{
		listeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.EquipmentSetFacade#getNameRef()
	 */
	@Override
	public ReferenceFacade<String> getNameRef()
	{
		return name;
	}

	@Override
	public EquipmentListFacade getEquippedItems()
	{
		return equippedItemsList;
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.EquipmentSetFacade#canEquip(pcgen.core.facade.EquipmentSetFacade.EquipNode, pcgen.core.facade.EquipmentFacade)
	 */
	@Override
	public boolean canEquip(EquipNode node, EquipmentFacade equipment)
	{
		if (!(equipment instanceof Equipment) || node == null)
		{
			return false;
		}
		Equipment item = (Equipment) equipment;
		
		// Check for a required location (i.e. you can't carry a natural weapon)
		EquipNode requiredLoc = getNaturalWeaponLoc(equipment);
		if (requiredLoc != null)
		{
			return validLocationForNaturalWeapon(node, item, requiredLoc);
		}

		// Is this a container? Then check if the object can fit in
		if (node.getNodeType() == NodeType.EQUIPMENT)
		{
			EquipmentFacade parent = node.getEquipment();
			if (parent instanceof Equipment
					&& ((Equipment) parent).isContainer())
				{
					// Check if it fits
					if (((Equipment) parent).canContain(theCharacter, item) == 1)
					{
						return true;
					}
				}
		}

		if (node.getNodeType() == NodeType.PHANTOM_SLOT)
		{
			// Check first for an already full or taken slot
			if (!getNodes().containsElement(node))
			{
				return false;
			}
			EquipSlot slot = ((EquipNodeImpl) node).getSlot();
			if (slot.canContainType(item.getType()))
			{
				if (item.isWeapon())
				{
					final String slotName = slot.getSlotName();

					if (item.isUnarmed() && slotName.equals(Constants.EQUIP_LOCATION_UNARMED))
					{
						return true;
					}
					if (item.isShield() && slotName.equals(Constants.EQUIP_LOCATION_SHIELD))
					{
						return true;
					}

					// If it is outsized, they can't equip it to a weapon slot
					if (item.isWeaponOutsizedForPC(theCharacter))
					{
						return false;
					}

					if (slotName.startsWith(Constants.EQUIP_LOCATION_BOTH))
					{
						return true;
					}
					if (item.isMelee() && item.isDouble()
						&& slotName.equals(Constants.EQUIP_LOCATION_DOUBLE))
					{
						return true;
					}
					if (item.isWeaponOneHanded(theCharacter))
					{
						if (slotName.equals(Constants.EQUIP_LOCATION_PRIMARY)
							|| slotName.startsWith(Constants.EQUIP_LOCATION_SECONDARY))
						{
							return true;
						}
					}
					
				}
				else
				{
					return true;
				}
			}
		}
		
		// Is this a body structure? Then check if the object be placed there
		if (node.getNodeType() == NodeType.BODY_SLOT)
		{
			BodyStructure root = (BodyStructure) node.getBodyStructure();
			if (root.isHoldsAnyType())
			{
				return !root.isForbidden(item.getTrueTypeList(false));
			}
		}

		// This item can't be equipped in this location 
		return false;
	}

	/**
	 * Check if the node is a valid location for the natural weapon to be equipped to.
	 * This allows primary natural weapons to be equipped to primary or secondary 
	 * slots, but secondary weapons only too the secondary slot.
	 * 
	 * @param node The node to be tested.
	 * @param equipment The natural weapon
	 * @param naturalLoc The natural weapon;s preferred slot.
	 * @return true if the node can take the natural weapon, false otherwise.
	 */
	private boolean validLocationForNaturalWeapon(EquipNode node,
		Equipment equipment, EquipNode naturalLoc)
	{
		if (equipment.isPrimaryNaturalWeapon())
		{
			return naturalWeaponNodes.containsValue(node);
		}
		return node.equals(naturalLoc);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.EquipmentSetFacade#getPreferredLoc(pcgen.core.facade.EquipmentFacade)
	 */
	@Override
	public String getPreferredLoc(EquipmentFacade equipment)
	{
		EquipNode reqNode = getNaturalWeaponLoc(equipment);
		if (reqNode != null)
		{
			return reqNode.toString();
		}
		for (EquipNode node : equipSlotNodeMap.values())
		{
			if (node.getNodeType()==NodeType.PHANTOM_SLOT)
			{
				if (canEquip(node, equipment))
				{
					return node.toString();
				}
			}
		}
		
		return LanguageBundle.getString("in_other"); //$NON-NLS-1$
	}

	/**
	 * Retrieve the preferred location for a natural weapon. Will return null 
	 * for non natural weapon equipment items.
	 * 
	 * @param equipment The equipment item to be checked.
	 * @return The preferred natural equip node, or null if not applicable. 
	 */
	protected EquipNode getNaturalWeaponLoc(EquipmentFacade equipment)
	{
		if (!(equipment instanceof Equipment) || equipment == null)
		{
			return null;
		}
		Equipment item = (Equipment) equipment;
		String locName = theCharacter.getNaturalWeaponLocation(item);
		if (locName != null)
		{
			return naturalWeaponNodes.get(locName);
		}
		
		return null;
	}
	
	protected void createNaturalWeaponSlots()
	{
		for (EquipSlot slot : SystemCollections.getUnmodifiableEquipSlotList())
		{
			if (slot.canContainType("WEAPON")) //$NON-NLS-1$
			{
				for (EquipNode node : nodeList)
				{
					if (node.getNodeType() == NodeType.BODY_SLOT
						&& slot.getBodyStructureName().equalsIgnoreCase(
							node.getBodyStructure().toString()))
					{
						createNaturalWeaponSlot(slot, node,
							Constants.EQUIP_LOCATION_NATURAL_PRIMARY);
						createNaturalWeaponSlot(slot, node,
							Constants.EQUIP_LOCATION_NATURAL_SECONDARY);
						return;
					}
				}
			}
		}

	}

	private void createNaturalWeaponSlot(EquipSlot slot, EquipNode node, String locName)
	{
		EquipSlot natWpnEquipSlot = createWeaponEquipSlot(slot, locName);
		EquipNodeImpl slotNode =
				new EquipNodeImpl((EquipNodeImpl) node,
					natWpnEquipSlot, true);
		naturalWeaponNodes.put(locName, slotNode);
	}
	
	/**
	 * Calculate the number of free instances of the slot there are in the
	 * equipment set.
	 *   
	 * @param slot The slot to be checked, must be a PHANTOM_SLOT.
	 * @return The number of slots free
	 */
	private int getNumFreeSlots(EquipNode slot)
	{
		if (slot.getNodeType() != EquipNode.NodeType.PHANTOM_SLOT)
		{
			return 0;
		}

		EquipNodeImpl node = (EquipNodeImpl) slot;
		int numPossible = getQuantity(node);

		// Scan for items  
		int numUsed = 0;
		for (EquipNode item : nodeList)
		{
			if (item.getNodeType() == NodeType.EQUIPMENT
				&& ((EquipNodeImpl) item).getSlot() == node.getSlot())
			{
				Equipment equip = (Equipment) item.getEquipment();
				numUsed += equip.getSlots(theCharacter);
			}
		}

		return numPossible - numUsed;
	}
	
	/* (non-Javadoc)
	 * @see pcgen.core.facade.EquipmentSetFacade#removeAllEquipment()
	 */
	@Override
	public void removeAllEquipment()
	{
		List<EquipNode> equipToBeRemoved = new ArrayList<EquipNode>(nodeList.getSize());
		for (EquipNode node : nodeList)
		{
			// Only select top level equipment, anything in a container will be removed along with the container. 
			if (node.getNodeType() == NodeType.EQUIPMENT
				&& node.getParent().getNodeType() != NodeType.EQUIPMENT)
			{
				equipToBeRemoved.add(node);
			}
		}

		for (EquipNode node : equipToBeRemoved)
		{
			removeEquipment(node, getQuantity(node));
		}
	}

	@Override
	public String toString()
	{
		return getName();
	}

	/**
	 * Notify any listeners that the quantity of a node has changed.
	 * 
	 * @param node The node that has changed quantity.
	 */
	private void fireQuantityChanged(EquipNode node)
	{
		EquipmentTreeEvent event = null;
		for (EquipmentTreeListener equipmentTreeListener : listeners)
		{
			if (event == null)
			{
				event = new EquipmentTreeEvent(this, node);
			}
			equipmentTreeListener.quantityChanged(event);
		}
	}

	/**
	 * @return The total carried weight for this equipment set.
	 */
	double getTotalWeight()
	{
		return totalWeight;
	}
	
	/**
	 * @return The core EquipSet that this facade represents.
	 */
	EquipSet getEquipSet()
	{
		return eqSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListFacade<EquipNode> getNodes()
	{
		return nodeList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getQuantity(EquipNode equipNode)
	{
		EquipNodeImpl node = (EquipNodeImpl) equipNode;
		switch (node.getNodeType())
		{
			case BODY_SLOT:
				return 1;

			case PHANTOM_SLOT:
				return node.singleOnly ? 1 : node.getSlot().getSlotCount();

			default:
				EquipSet parentEs = charDisplay.getEquipSetByIdPath(node.getIdPath());
				return parentEs == null ? 0 : parentEs.getQty().intValue();
		}
	}

	/* (non-Javadoc)
	 * @see pcgen.core.facade.EquipmentSetFacade#getLocation(pcgen.core.facade.EquipmentSetFacade.EquipNode)
	 */
	@Override
	public String getLocation(EquipNode equipNode)
	{
		EquipNodeImpl node = (EquipNodeImpl) equipNode;
		switch (node.getNodeType())
		{
			case BODY_SLOT:
				return node.toString();

			default:
				if (node.getSlot() != null)
				{
					return node.getSlot().toString();
				}
				return node.getBodyStructure().toString();
		}
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLocation(EquipmentFacade equip)
	{
		for (EquipNode node : nodeList)
		{
			if (equip.equals(node.getEquipment()))
			{
				switch (node.getNodeType())
				{
					case EQUIPMENT:
					case PHANTOM_SLOT:
						return getLocation(node);
					default:
						return node.getParent().toString();
				}
			}
		}
		
		return getPreferredLoc(equip);
	}
	/**
	 * Identify if the character has any natural weapons that have not been 
	 * equipped yet.
	 * @return true if there are unequipped natural attacks, false if not.
	 */
	private boolean pcHasUnequippedNaturalWeapons()
	{
		for (Equipment equipItem : theCharacter.getEquipmentMasterList())
		{
			if (equipItem.isNatural() && !equippedItemsList.containsElement(equipItem))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * The Class <code>EquipNodeImpl</code> represents a node in the equipping 
	 * tree. It may be an item of equipment or a slot that may be filled. 
	 * EquipNodeImpl objects are immutable.
	 */
	public static class EquipNodeImpl implements EquipNode
	{
		private static final NumberFormat FMT = new DecimalFormat("00");
		private NodeType nodeType;
		private BodyStructure bodyStructure;
		private Equipment equipment;
		private EquipNodeImpl parent;
		private String name;
		private EquipSlot slot;
		private String idPath;
		private String order;
		private boolean singleOnly = false;
		
		/**
		 * Create a new EquipNodeImpl instance representing a body structure.
		 *  
		 * @param bodyStructure The part of the body.
		 * @param order The order of the body structure
		 */
		public EquipNodeImpl(BodyStructure bodyStructure, int order)
		{
			this.nodeType = NodeType.BODY_SLOT;
			this.bodyStructure = bodyStructure;
			this.name = bodyStructure.toString();
			this.order = FMT.format(order);
		}
		
		/**
		 * Create a new EquipNodeImpl instance representing an equipment slot. 
		 * These are called phantom slots as they represent a location yet to 
		 * be filled.
		 *  
		 * @param parent The parent body structure node
		 * @param slot The equipment slot
		 * @param singleOnly Can the slot only ever have a single entry. e.g. weapon slots
		 */
		public EquipNodeImpl(EquipNodeImpl parent, EquipSlot slot, boolean singleOnly)
		{
			this.nodeType = NodeType.PHANTOM_SLOT;
			this.bodyStructure = parent.bodyStructure;
			this.slot = slot;
			this.name = slot.getSlotName();
			this.parent = parent;
			this.singleOnly = singleOnly;
		}
		
		/**
		 * Create a new EquipNodeImpl instance representing an item of equipment.
		 *  
		 * @param parent The parent node, may be null for a body structure.
		 * @param slot The equipment slot the item is equipped to.
		 * @param equipment The equipment item, may be null.
		 * @param idPath The id of the path as used by the core.
		 */
		public EquipNodeImpl(EquipNodeImpl parent, EquipSlot slot, Equipment equipment, String idPath)
		{
			this.nodeType = NodeType.EQUIPMENT;
			this.bodyStructure = parent.bodyStructure;
			this.slot = slot;
			this.equipment = equipment;
			this.idPath = idPath;
			this.parent = parent;
			this.name = equipment.getDisplayName();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public BodyStructureFacade getBodyStructure()
		{
			return bodyStructure;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public EquipmentFacade getEquipment()
		{
			return equipment;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public NodeType getNodeType()
		{
			return nodeType;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public EquipNode getParent()
		{
			return parent;
		}

		/**
		 * @return the slot
		 */
		EquipSlot getSlot()
		{
			return slot;
		}

		/**
		 * @return the idPath
		 */
		String getIdPath()
		{
			return idPath;
		}

		/**
		 * @return The key to be used for sorting EquipNodes
		 */
		String getSortKey()
		{
			StringBuilder sortKey = new StringBuilder();
			if (parent != null)
			{
				sortKey.append(parent.getSortKey());
			}
			switch (nodeType)
			{
				case BODY_SLOT:
					sortKey.append(order);
					break;

				case PHANTOM_SLOT:
					sortKey.append("|");
					sortKey.append(slot.getSlotName());
					break;
					
				case EQUIPMENT:
				default:
					sortKey.append("|");
					String objKey = equipment.get(StringKey.SORT_KEY);
					if (objKey == null)
					{
						objKey = equipment.getKeyName();
					}
					sortKey.append(objKey);
					break;
			}
			return sortKey.toString();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString()
		{
			return name;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo(EquipNode o)
		{
			if (o instanceof EquipNodeImpl)
			{
				EquipNodeImpl other = (EquipNodeImpl) o;
				return getSortKey().compareTo(other.getSortKey());
			}
			return toString().compareTo(o.toString());
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void elementAdded(ListEvent<EquipmentFacade> e)
	{
		// We don't care about new equipment being added.
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void elementRemoved(ListEvent<EquipmentFacade> e)
	{
		EquipmentFacade equipmentFacade = e.getElement();
		if (equippedItemsList.containsElement(equipmentFacade))
		{
			Logging.debugPrint("Currently equipped item " + equipmentFacade + " is being removed.");
		}
		List<EquipNodeImpl> affectedList = findEquipmentNodes(equipmentFacade);
		for (EquipNodeImpl equipNode : affectedList)
		{
			EquipSet eSet = charDisplay.getEquipSetByIdPath(equipNode.getIdPath());
			if (eSet != null)
			{
				removeEquipment(equipNode, eSet.getQty().intValue());
			}
		}
	}


	private List<EquipNodeImpl> findEquipmentNodes(EquipmentFacade equipmentFacade)
	{
		List<EquipNodeImpl> affectedList = new ArrayList<EquipNodeImpl>();
		for (EquipNode node : nodeList)
		{
			if (equipmentFacade.equals(node.getEquipment()))
			{
				affectedList.add((EquipNodeImpl) node);
			}
		}
		return affectedList;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void elementsChanged(ListEvent<EquipmentFacade> e)
	{
		// We expect a refresh of the equipment set to follow an equipment changed 
		// event, so we can ignore these.
		Logging.debugPrint("Equip elementsChanged " + e);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void elementModified(ListEvent<EquipmentFacade> e)
	{
		// We don't care about new equipment being modified.
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void quantityChanged(EquipmentListEvent e)
	{
		EquipmentFacade equipmentFacade = e.getEquipment();
		if (equippedItemsList.containsElement(equipmentFacade))
		{
			int quantity = purchasedList.getQuantity(equipmentFacade) -
					equippedItemsList.getQuantity(equipmentFacade);
			if (quantity < 0)
			{
				Logging.debugPrint("Currently equipped item " + equipmentFacade
					+ " is being partially removed " + quantity + " from "
					+ equippedItemsList.getQuantity(equipmentFacade));

				int numStillToRemove = -1*quantity;
				List<EquipNodeImpl> affectedList = findEquipmentNodes(equipmentFacade);
				Collections.sort(affectedList, new EquipLocImportantComparator()); // TODO: Custom sort order
				for (EquipNodeImpl equipNode : affectedList)
				{
					EquipSet eSet = charDisplay.getEquipSetByIdPath(equipNode.getIdPath());
					if (eSet != null)
					{
						int numToRemove = Math.min(eSet.getQty().intValue(),numStillToRemove);
						removeEquipment(equipNode, numToRemove);
						numStillToRemove -= numToRemove;
					}
					
					if (numStillToRemove <= 0)
					{
						return;
					}
				}
				
			}
		}
	}

	/**
	 * The Class <code>EquipLocImportantComparator</code> compares EquipNodes based 
	 * on their 'importance' to the character. The predefined order of the slots is 
	 * not carried, carried, equipped, all others in alpha order.
	 */

	public static class EquipLocImportantComparator implements
			Comparator<EquipNodeImpl>
	{

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compare(EquipNodeImpl node1, EquipNodeImpl node2)
		{
			BodyStructureFacade bodyStruct1 = node1.getBodyStructure();
			BodyStructureFacade bodyStruct2 = node2.getBodyStructure();
			
			if (bodyStruct1 != bodyStruct2)
			{
				String[] locOrder =
						new String[]{Constants.EQUIP_LOCATION_NOTCARRIED,
							Constants.EQUIP_LOCATION_CARRIED,
							Constants.EQUIP_LOCATION_EQUIPPED};
				for (String locName : locOrder)
				{
					if (locName.equals(bodyStruct1.toString()))
					{
						return -1;
					}
					if (locName.equals(bodyStruct2.toString()))
					{
						return 1;
					}
				}
				
				return bodyStruct1.toString().compareTo(bodyStruct2.toString());
			}

			return node1.compareTo(node2);
		}
	
	}
}
