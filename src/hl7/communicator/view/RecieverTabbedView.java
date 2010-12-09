package hl7.communicator.view;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;

public class RecieverTabbedView {
	private JFormattedTextField portNumber;
	private NumberFormatter formatter;
	private JButton start;
	private JButton stop;
	private JTable table;
	private JLabel statusText;
	private TreePanel msgDisplayPanel;

	public JPanel getDisplayableView() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		
		createMessageTable();
		
		createConnectPanel(panel, cons);
		
		createSplitPane(panel, cons);
		
		return panel;
	}

	private void createSplitPane(JPanel panel, GridBagConstraints cons) {
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(120);
		splitPane.add(new JScrollPane(table));
		msgDisplayPanel = new TreePanel();
		msgDisplayPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		splitPane.add(new JScrollPane(msgDisplayPanel));
		
		statusText = new JLabel("");
		cons.gridx = 0;
		cons.gridy = 1;
		panel.add(statusText,cons);
		
		cons.fill = GridBagConstraints.BOTH;
		cons.gridx = 0;
		cons.gridy = 2;
		cons.weightx = 1.0;
		cons.weighty = 1.0;
		panel.add(splitPane, cons);
	}

	private void createConnectPanel(JPanel panel, GridBagConstraints cons) {
		JPanel connectPanel = new JPanel();
		createNumericTextBox();
		start = new JButton("Start");
		stop = new JButton("Stop");
		
		connectPanel.add(portNumber);
		connectPanel.add(start);
		connectPanel.add(stop);
		
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.gridx = 0;
		cons.gridy = 0;
		panel.add(connectPanel, cons);
	}

	private void createNumericTextBox() {
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		numberFormat.setMaximumFractionDigits(0);
		numberFormat.setGroupingUsed(false);
		formatter = new NumberFormatter(numberFormat);
		formatter.setAllowsInvalid(false);
		formatter.setCommitsOnValidEdit(true);
		portNumber = new JFormattedTextField(formatter);
		portNumber.setColumns(5);
		portNumber.setText("12345");
	}

	public int getPortNumber() {
			return Integer.parseInt(portNumber.getText());
	}

	public JButton getStartButton() {
		return start;
	}
	
	public JButton getStopButton() {
		return stop;
	}
	
	public JTable getMessageTable() {
		return table;
	}

	private void createMessageTable() {
		String[] columnNames = { "Recieved Time", "Message Type", "Message","" };

		Object[][] data = new Object[200][4];

		table = new JTable(200, 3);
		table.setModel(new DefaultTableModel(data, columnNames));
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		table.setShowHorizontalLines(true);
		table.setShowVerticalLines(true);
		table.getColumnModel().getColumn(3).setMaxWidth(1);
	}

	public TreePanel getMsgDisplayPanel() {
		return msgDisplayPanel;
	}

	public JLabel getStatusText() {
		return statusText;
	}

}
