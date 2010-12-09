package hl7.communicator;

import hl7.communicator.view.MessageViewerView;
import hl7.communicator.view.MessageViewerViewHandler;
import hl7.communicator.view.RecieverTabbedView;
import hl7.communicator.view.RecieverTabbedViewHandler;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class CommunicatorView {
	
	public void createView() {
		JFrame frame = new JFrame("Simple HL7 Communicator");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    JTabbedPane tabbedPane = new JTabbedPane();
	    tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	    String titles[] = { "Reciever", "Sender"};
	    for (int i = 0; i < 1; i++) {
	      add(tabbedPane, titles[i]);
	    }
	    MessageViewerView messageViewerView = new MessageViewerView();
	    tabbedPane.add("Sender", new JLabel("Under Construction"));
	    tabbedPane.add("Viewer", messageViewerView.getDisplayableView());
	    new MessageViewerViewHandler(messageViewerView);

	    frame.add(tabbedPane, BorderLayout.CENTER);
	    frame.setSize(400, 150);
	    frame.setVisible(true);
	}
	
	void add(JTabbedPane tabbedPane, String label) {
	    RecieverTabbedView recieverTabbedView = new RecieverTabbedView();
		JPanel displayableView = recieverTabbedView.getDisplayableView();

	    new RecieverTabbedViewHandler(recieverTabbedView);
		tabbedPane.addTab(label, displayableView);
	  }

}
