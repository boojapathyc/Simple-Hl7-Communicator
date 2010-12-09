package hl7.communicator.view;

import hl7.communicator.server.MLLPResponderService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ca.uhn.hl7v2.model.Message;

public class RecieverTabbedViewHandler {
	RecieverTabbedView view;
	MLLPResponderService mllpService;

	public RecieverTabbedViewHandler(RecieverTabbedView view) {
		this.view = view;
		addStartHandler();
		addStopHandler();
		addMsgSelectionListener();
	}

	private void addMsgSelectionListener() {
		view.getMessageTable().getSelectionModel()
				.addListSelectionListener(new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent e) {
						ListSelectionModel listSelectionModel = (ListSelectionModel)e.getSource();
						Message message = (Message) view.getMessageTable().getValueAt(listSelectionModel.getAnchorSelectionIndex(),3);
						if(message != null) {
						view.getMsgDisplayPanel().setMessage(message);
						}
					}
				});
	}

	private void addStartHandler() {
		view.getStartButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mllpService != null && mllpService.isRunning())
					mllpService.stop();
				
				mllpService = new MLLPResponderService(view.getPortNumber(),
						view.getMessageTable());
				if(!mllpService.isRunning()) 
					view.getStatusText().setText("Port is bound to another process. Server cannot be started in this port");
				else view.getStatusText().setText("Server is started");
			}

		});
	}

	private void addStopHandler() {
		view.getStopButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (mllpService != null && mllpService.isRunning())
					mllpService.stop();
				view.getStatusText().setText("Server is stopped");
			}

		});
	}

}
