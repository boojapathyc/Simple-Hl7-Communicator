package hl7.communicator.server;

import java.io.IOException;
import java.util.Date;

import javax.swing.JTable;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Application;
import ca.uhn.hl7v2.app.ApplicationException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.segment.EVN;

public class IncomingMessageHandler implements Application {

	private JTable messageTable;
	private int lastValueStoredPosition = 0;

	public IncomingMessageHandler(JTable messageTable) {
		this.messageTable = messageTable;
	}

	@Override
	public boolean canProcess(Message msg) {
		return true;
	}

	@Override
	public Message processMessage(Message msg) throws ApplicationException,
			HL7Exception {
		try {
			messageTable.setValueAt(new Date(), lastValueStoredPosition, 0);
			messageTable.setValueAt(getEventType(msg), lastValueStoredPosition, 1);
			messageTable.setValueAt(msg.encode(), lastValueStoredPosition, 2);
			messageTable.setValueAt(msg, lastValueStoredPosition, 3);
			lastValueStoredPosition++;
			return msg.generateACK();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String getEventType(Message msg) throws HL7Exception {
		String eventType = ((EVN)msg.get("EVN")).getEventTypeCode().encode();
		return eventType;
	}

}
