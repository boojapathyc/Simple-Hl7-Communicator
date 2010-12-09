package hl7.communicator.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.GenericParser;

public class MessageViewerViewHandler {
	MessageViewerView view;

	public MessageViewerViewHandler(MessageViewerView msgViewer) {
		this.view = msgViewer;
		addParseListener();
	}

	private void addParseListener() {
		view.getParse().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String text = view.getMessage().getText();
					Message parse = new GenericParser().parse(text.replace("\n", "\r"));
					System.out.println("parsing done successfully " +  parse);
					view.getMsgDisplayPanel().setMessage(parse);
					view.getMsgDisplayPanel().setVisible(true);
				} catch (EncodingNotSupportedException e1) {
					e1.printStackTrace();
				} catch (HL7Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
	}

}
