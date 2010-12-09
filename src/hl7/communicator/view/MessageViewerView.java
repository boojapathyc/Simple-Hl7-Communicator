package hl7.communicator.view;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class MessageViewerView {
	private JTextArea message;
	private JButton parse;
	private TreePanel msgDisplayPanel;

	public JPanel getDisplayableView() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		
		message = new JTextArea();
		parse = new JButton("Parse");
		
		createSplitPane(panel, cons);
		
		return panel;
	}
	
	private void createSplitPane(JPanel panel, GridBagConstraints cons) {
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(120);
		JPanel messageSegPanel = new JPanel();
		JScrollPane messageScrollPane = new JScrollPane(message);
		messageScrollPane.setPreferredSize(new Dimension(1300,200));
		messageSegPanel.add(messageScrollPane);
		messageSegPanel.add(parse);
		splitPane.add(messageSegPanel);
		msgDisplayPanel = new TreePanel();
		msgDisplayPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		splitPane.add(new JScrollPane(msgDisplayPanel));
		
		cons.fill = GridBagConstraints.BOTH;
		cons.gridx = 0;
		cons.gridy = 0;
		cons.weightx = 1.0;
		cons.weighty = 1.0;
		panel.add(splitPane, cons);
	}

	public JButton getParse() {
		return parse;
	}

	public TreePanel getMsgDisplayPanel() {
		return msgDisplayPanel;
	}

	public JTextArea getMessage() {
		return message;
	}
}
