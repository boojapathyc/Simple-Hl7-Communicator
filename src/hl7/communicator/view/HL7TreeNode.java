package hl7.communicator.view;

import javax.swing.tree.DefaultMutableTreeNode;

public class HL7TreeNode extends DefaultMutableTreeNode {
	NodeType nodeType;
	public HL7TreeNode(Object userObject, NodeType nodeType) {
		super(userObject);
		this.nodeType = nodeType;
	}
	
	public void hideEmptyNodes() {
		if(getChildCount() != 0) {
		for(int i=0; i< children.size(); i++) {
			HL7TreeNode child = (HL7TreeNode) children.get(i);
			child.hideEmptyNodes();
		}
		}
		if(getChildCount() == 0 && (userObject == null || NodeType.COMPOSITE.equals(nodeType))) {
			
			removeFromParent();
		}
	}
}
