package hl7.communicator.server;

import javax.swing.JTable;

import ca.uhn.hl7v2.app.Application;
import ca.uhn.hl7v2.app.HL7Service;
import ca.uhn.hl7v2.app.MessageTypeRouter;
import ca.uhn.hl7v2.app.SimpleServer;
import ca.uhn.hl7v2.llp.LowerLayerProtocol;
import ca.uhn.hl7v2.llp.MinLowerLayerProtocol;
import ca.uhn.hl7v2.parser.GenericParser;

public class MLLPResponderService {
	
	private HL7Service hl7Service;

	public MLLPResponderService(int port, JTable messageTable) {
		GenericParser parser = new GenericParser();
		LowerLayerProtocol llp = new MinLowerLayerProtocol();
		hl7Service = new SimpleServer(port, llp,parser);
		Application handler = newApplicationHandler(messageTable);
		hl7Service.registerApplication("*", "*", handler);
		hl7Service.start();
		
	}

	private Application newApplicationHandler(JTable messageTable) {
		MessageTypeRouter messageTypeRouter = new MessageTypeRouter();
		messageTypeRouter.registerApplication("*", "*", new IncomingMessageHandler(messageTable));
		return messageTypeRouter;
	}
	
	public void stop() {
		hl7Service.stop();
	}
	
	public boolean isRunning() {
		return hl7Service.isRunning();
	}

}
