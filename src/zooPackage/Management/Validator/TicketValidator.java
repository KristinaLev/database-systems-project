package zooPackage.Management.Validator;

import zooPackage.Entities.Tickets.Ticket;
import zooPackage.Entities.Users.Visitor;
import zooPackage.Management.ZooFacade;

public class TicketValidator {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static TicketValidator instance;

	private TicketValidator() {
	}

	public static synchronized TicketValidator getInstance() {
		if (instance == null) {
			instance = new TicketValidator();
		}
		return instance;
	}

	public boolean validateVisitorTickets(String visitorID) {
		Visitor visitor = zooFacade.getVisitorManager().findVisitorByID(visitorID);
		int visitor_id = visitor.getVisitor_id();
		for (Ticket ticket : zooFacade.getTicketManager().getActiveTickets()) {
			if (visitor_id == ticket.getVisitor_ID()) {
				return true;
			}
		}

		zooFacade.getPanelDialogMessage().showInformationDialog("No tickets found with the entered ID.");
		return false;
	}

}
