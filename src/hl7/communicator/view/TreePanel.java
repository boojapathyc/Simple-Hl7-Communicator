/**
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the
 * specific language governing rights and limitations under the License.
 *
 * The Original Code is "TreePanel.java".  Description:
 * "This is a Swing panel that displays the contents of a Message object in a JTree"
 *
 * The Initial Developer of the Original Code is University Health Network. Copyright (C)
 * 2001.  All Rights Reserved.
 *
 * Contributor(s): ______________________________________.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * GNU General Public License (the  “GPL”), in which case the provisions of the GPL are
 * applicable instead of those above.  If you wish to allow use of your version of this
 * file only under the terms of the GPL and not to allow others to use your version
 * of this file under the MPL, indicate your decision by deleting  the provisions above
 * and replace  them with the notice and other provisions required by the GPL License.
 * If you do not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the GPL.
 *
 */

/*
 * Created on October 17, 2001, 11:44 AM
 */
package hl7.communicator.view;

import javax.swing.JTree;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.*;

/**
 * This is a Swing panel that displays the contents of a Message object in a
 * JTree. The tree currently only expands to the field level (components shown
 * as one node).
 * 
 * @author Bryan Tripp (bryan_tripp@sourceforge.net)
 */
@SuppressWarnings("serial")
public class TreePanel extends javax.swing.JPanel {

	private Message message;

	/**
	 * Updates the panel with a new Message.
	 */
	public void setMessage(Message message) {
		this.message = message;
		HL7TreeNode top = new HL7TreeNode(message.getName(), NodeType.ROOT);
		addChildren(message, top);
		top.hideEmptyNodes();
		JTree tree = new JTree(top);

		// JScrollPane treeView = new JScrollPane(tree);
		this.removeAll();
		this.add(tree);
		this.revalidate();
	}

	/**
	 * Returns the message that is currently displayed in the tree panel.
	 */
	public Message getMessage() {
		return this.message;
	}

	/**
	 * Adds the children of the given group under the given tree node.
	 */
	private void addChildren(Group messParent, HL7TreeNode treeParent) {
		String[] childNames = messParent.getNames();
		int currChild = 0;
		for (int i = 0; i < childNames.length; i++) {
			try {
				Structure[] childReps = messParent.getAll(childNames[i]);
				for (int j = 0; j < childReps.length; j++) {
					HL7TreeNode newNode = null;
					if (childReps[j] instanceof Group) {
						newNode = new HL7TreeNode(childNames[i] + " (rep " + j
								+ ")", NodeType.GROUP);
						addChildren((Group) childReps[j], newNode);
					} else if (childReps[j] instanceof Segment) {
						newNode = new HL7TreeNode(childReps[j].getName(),
								NodeType.SEGMENT);
						addChildren((Segment) childReps[j], newNode);
					}
					treeParent.insert(newNode, currChild++);
				}
			} catch (HL7Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Add fields of a segment to the tree ...
	 */
	private void addChildren(Segment messParent, HL7TreeNode treeParent) {
		int n = messParent.numFields();
		String[] fieldNames = messParent.getNames();
		int currChild = 0;
		for (int i = 1; i <= n; i++) {
			try {
				Type[] reps = messParent.getField(i);
				for (int j = 0; j < reps.length; j++) {
					HL7TreeNode newNode = new HL7TreeNode(
							"Field " + i + " rep " + j + " ("
									+ fieldNames[i - 1] + ") ",
							NodeType.SEG_FIELD);
					addChildren(reps[j], newNode);
					treeParent.insert(newNode, currChild++);
				}
			} catch (HL7Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Adds children to the tree. If the Type is a Varies, the Varies data are
	 * added under a new node called "Varies". If there are extra components,
	 * these are added under a new node called "ExtraComponents"
	 */
	private void addChildren(Type messParent, HL7TreeNode treeParent) {
		if (Varies.class.isAssignableFrom(messParent.getClass())) {
			// DefaultMutableTreeNode variesNode = new
			// DefaultMutableTreeNode("Varies");
			// treeParent.insert(variesNode, treeParent.getChildCount());
			Type data = ((Varies) messParent).getData();
			HL7TreeNode dataNode = new HL7TreeNode(getLabel(data),
					NodeType.VARIES);
			treeParent.insert(dataNode, 0);
			addChildren(data, dataNode);
		} else {
			if (Composite.class.isAssignableFrom(messParent.getClass())) {
				addChildren((Composite) messParent, treeParent);
			} else if (Primitive.class.isAssignableFrom(messParent.getClass())) {
				addChildren((Primitive) messParent, treeParent);
			}

			if (messParent.getExtraComponents().numComponents() > 0) {
				HL7TreeNode newNode = new HL7TreeNode("ExtraComponents",
						NodeType.EXTRA);
				treeParent.insert(newNode, treeParent.getChildCount());
				for (int i = 0; i < messParent.getExtraComponents()
						.numComponents(); i++) {
					HL7TreeNode variesNode = new HL7TreeNode("Varies",
							NodeType.VARIES);
					newNode.insert(variesNode, i);
					addChildren(
							messParent.getExtraComponents().getComponent(i),
							variesNode);
				}
			}
		}
	}

	/**
	 * Adds components of a composite to the tree ...
	 */
	private void addChildren(Composite messParent, HL7TreeNode treeParent) {
		Type[] components = messParent.getComponents();
		for (int i = 0; i < components.length; i++) {
			HL7TreeNode newNode = new HL7TreeNode("Field " + i + " ("
					+ getLabel(components[i]) + ") ", NodeType.COMPOSITE);
			addChildren(components[i], newNode);
			treeParent.insert(newNode, i);
		}
	}

	/**
	 * Adds single text value to tree as a leaf
	 */
	private void addChildren(Primitive messParent, HL7TreeNode treeParent) {
//		if (messParent.getValue() == null) {
//			treeParent.removeFromParent();
//		} else {
			HL7TreeNode newNode = new HL7TreeNode(messParent.getValue(),
					NodeType.PRIMITIVE);
			treeParent.insert(newNode, 0);
//		}
	}

	/**
	 * Returns the unqualified class name as a label for tree nodes.
	 */
	private static String getLabel(Type reps) {
		String name = reps.getClass().getName();
		return name.substring(name.lastIndexOf('.') + 1, name.length());
	}
}
