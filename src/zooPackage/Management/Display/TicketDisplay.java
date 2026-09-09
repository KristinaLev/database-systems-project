package zooPackage.Management.Display;

import java.util.Collection;
import java.util.List;
import java.time.format.DateTimeParseException;

import zooPackage.Entities.Tickets.Ticket;
import zooPackage.Entities.Users.Visitor;
import zooPackage.Management.ZooFacade;

public class TicketDisplay {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static TicketDisplay instance;

	private TicketDisplay() {
	}

	public static TicketDisplay getInstance() {
		if (instance == null) {
			instance = new TicketDisplay();
		}
		return instance;
	}

	public void displayActiveTicketsByID() {
		while (true) {
			if (zooFacade.getTicketManager().getActiveTickets().isEmpty()) {
				zooFacade.getPanelDialogMessage().showInformationDialog("No active tickets to display");
				return;
			}

			StringBuilder ticketList = new StringBuilder();
			String idString = zooFacade.getPanelDialogMessage().showInputDialog("Enter your ID:",
					"Find active tickets by visitor ID");

			if (idString == null) {
				return;
			}
			if (idString.isEmpty()) {
				zooFacade.getPanelDialogMessage().showErrorDialog("ID cannot be empty.");
				continue;
			}
			if (!zooFacade.getPersonValidation().isValidIDFormat(idString)) {
				continue;
			}
			try {
				long id = Long.parseLong(idString);
				ticketList = zooFacade.getTicketUtils().ticketStringBuilder(id);
				if (ticketList.isEmpty()) {
					zooFacade.getPanelDialogMessage().showInformationDialog(
							"No active tickets found with the entered visitor ID  :  { " + id + " }");
				} else {
					zooFacade.getPanelDialogMessage().showListDialog(ticketList.toString(),
							"Active Ticket List by Visitor ID");
				}
				return;
			} catch (NumberFormatException e) {
				zooFacade.getPanelDialogMessage().showErrorDialog("Invalid ID format. Please enter a valid number ID.");
			}
		}

	}

	public void displayActiveTicketsByReleaseDate() {
		while (true) {

			if (zooFacade.getTicketManager().getActiveTickets().isEmpty()) {
				zooFacade.getPanelDialogMessage().showInformationDialog("No tickets to display");
				return;
			}

			StringBuilder ticketList = new StringBuilder();
			String releaseDateString = zooFacade.getPanelDialogMessage().showInputDialog("Enter the release date:",
					"Find active ticket by Release Date");

			if (releaseDateString == null) {
				return;
			}
			if (releaseDateString.isEmpty()) {
				zooFacade.getPanelDialogMessage().showErrorDialog("The date field cannot be empty.");
				continue;
			}
			if (!zooFacade.getPersonValidation().validateDateField(releaseDateString)) {
				continue;
			}
			try {
				for (Ticket ticket : zooFacade.getTicketManager().getActiveTickets())
					if (releaseDateString.equals("" + ticket.getReleaseDate()))
						ticketList.append(ticket).append("\n");

				if (ticketList.isEmpty())
					zooFacade.getPanelDialogMessage().showInformationDialog(
							"No tickets found with the entered release date : {" + releaseDateString + "}");
				else
					zooFacade.getPanelDialogMessage().showListDialog(ticketList.toString(),
							"Active Ticket List by Release Date");
				return;
			} catch (DateTimeParseException e) {
				zooFacade.getPanelDialogMessage().showErrorDialog("Invalid Date format.");
			}
		}
	}

	public void displayAllActiveTickets() {
		while (true) {
			StringBuilder ticketList = new StringBuilder();

			if (zooFacade.getTicketManager().getActiveTickets().isEmpty()) {
				zooFacade.getPanelDialogMessage().showInformationDialog("No active tickets to display");
				return;
			}

			for (Ticket ticket : zooFacade.getTicketManager().getActiveTickets()) {
				ticketList.append(ticket).append("\n");
			}

			zooFacade.getPanelDialogMessage().showListDialog(ticketList.toString(), "Active Ticket List");
			break;
		}
	}

	public void displayTicketPurchaseHistoryByID() {
		while (true) {

			if (zooFacade.getTicketManager().getPurchasedTickets().isEmpty()) {
				zooFacade.getPanelDialogMessage().showInformationDialog("No purchased tickets to display");
				return;
			}
			StringBuilder ticketList = new StringBuilder();
			String idString = zooFacade.getPanelDialogMessage().showInputDialog("Enter your ID:",
					"Find purcheased tickets by Visitor ID");

			if (idString == null) {
				return;
			}
			if (idString.isEmpty()) {
				zooFacade.getPanelDialogMessage().showErrorDialog("ID cannot be empty.");
				continue;
			}
			if (!zooFacade.getPersonValidation().isValidIDFormat(idString)) {
				continue;
			}
			try {
				Visitor visitor = zooFacade.getVisitorManager().findVisitorByID(idString);
				List<Ticket> tickets = zooFacade.getTicketManager().getPurchasedTickets().get(visitor.getVisitor_id());
				if (tickets == null || tickets.isEmpty()) {
					zooFacade.getPanelDialogMessage().showInformationDialog(
							"No purchased tickets found with the entered ID  :  { " + visitor.getID() + " }");
				} else {
					for (Ticket ticket : tickets) {
						ticketList.append(ticket).append("\n");
					}
					zooFacade.getPanelDialogMessage().showListDialog(ticketList.toString(),
							"Purchased Tickets List by Visitor ID");
				}
				return;
			} catch (NumberFormatException e) {
				zooFacade.getPanelDialogMessage().showErrorDialog("Invalid ID format.");
			}
		}
	}

	public void displayTicketPurchaseHistoryByReleaseDate() {
		while (true) {

			if (zooFacade.getTicketManager().getPurchasedTickets().isEmpty()) {
				zooFacade.getPanelDialogMessage().showInformationDialog("No purchased tickets to display");
				return;
			}

			StringBuilder ticketList = new StringBuilder();
			String purchaseDateString = zooFacade.getPanelDialogMessage().showInputDialog("Enter the purchase date:",
					"Find purchased tickets by Release Date");

			if (purchaseDateString == null) {
				return;
			}
			if (purchaseDateString.isEmpty()) {
				zooFacade.getPanelDialogMessage().showErrorDialog("The date field cannot be empty.");
				continue;
			}
			if (!zooFacade.getPersonValidation().validateDateField(purchaseDateString)) {
				continue;
			}
			try {
				Collection<List<Ticket>> purchasedTickets = zooFacade.getTicketManager().getPurchasedTickets().values();

				for (List<Ticket> tickets : purchasedTickets) {
					for (Ticket ticket : tickets) {
						if (purchaseDateString.equals("" + ticket.getReleaseDate())) {
							ticketList.append(ticket).append("\n");
						}
					}
				}
				if (ticketList.isEmpty()) {
					zooFacade.getPanelDialogMessage().showInformationDialog(
							"No tickets found with the entered purchase date : {" + purchaseDateString + "}");
				} else {
					zooFacade.getPanelDialogMessage().showListDialog(ticketList.toString(),
							"Ticket Purchase List by purchase date");
				}
				return;
			} catch (DateTimeParseException e) {
				zooFacade.getPanelDialogMessage().showErrorDialog("Invalid Date format.");
			}
		}
	}

	public void displayAllTicketPurchaseHistory() {
		StringBuilder ticketListStringBuilder = new StringBuilder();

		Collection<List<Ticket>> tickets = zooFacade.getTicketManager().getPurchasedTickets().values();

		if (tickets == null || tickets.isEmpty()) {
			zooFacade.getPanelDialogMessage().showInformationDialog("No purchased tickets found.");
			return;
		}
		for (List<Ticket> ticketList : tickets) {
			for (Ticket ticket : ticketList) {
				ticketListStringBuilder.append(ticket).append("\n");
			}
		}
		zooFacade.getPanelDialogMessage().showListDialog(ticketListStringBuilder.toString(), "Ticket Purchase History");
	}

	public void displayUsedTicketByEntryDate() {
		while (true) {

			if (zooFacade.getTicketManager().getUsedTickets().isEmpty()) {
				zooFacade.getPanelDialogMessage().showInformationDialog("No used tickets to display");
				return;
			}

			StringBuilder ticketList = new StringBuilder();
			String entryDateString = zooFacade.getPanelDialogMessage().showInputDialog("Enter the entry date:",
					"Find used tickets by Entry Date");

			if (entryDateString == null)
				return;

			if (entryDateString.isEmpty()) {
				zooFacade.getPanelDialogMessage().showErrorDialog("The date field cannot be empty.");
				continue;
			}

			if (!zooFacade.getPersonValidation().validateDateField(entryDateString))
				continue;
			try {
				Collection<List<Ticket>> usedTickets = zooFacade.getTicketManager().getUsedTickets().values();

				for (List<Ticket> tickets : usedTickets) {
					for (Ticket ticket : tickets) {
						if (entryDateString.equals("" + ticket.getEntryDate())) {
							ticketList.append(ticket).append("\n");
						}
					}
				}
				if (ticketList.isEmpty()) {
					zooFacade.getPanelDialogMessage().showInformationDialog(
							"No tickets found with the entered entry date : {" + entryDateString + "}");
				} else {
					zooFacade.getPanelDialogMessage().showListDialog(ticketList.toString(), "Used Tickets history");
				}
				return;
			} catch (DateTimeParseException e) {
				zooFacade.getPanelDialogMessage().showErrorDialog("Invalid date format.");
			}
		}
	}

	public void displayUsedTicketByID() {
		while (true) {

			if (zooFacade.getTicketManager().getUsedTickets().isEmpty()) {
				zooFacade.getPanelDialogMessage().showInformationDialog("No used tickets to display");
				return;
			}

			StringBuilder ticketList = new StringBuilder();
			String idString = zooFacade.getPanelDialogMessage().showInputDialog("Enter your ID:",
					"Find used tickets by your ID");

			if (idString == null) {
				return;
			}
			if (idString.isEmpty()) {
				zooFacade.getPanelDialogMessage().showErrorDialog("ID cannot be empty.");
				continue;
			}
			if (!zooFacade.getPersonValidation().isValidIDFormat(idString)) {
				continue;
			}
			try {
				Visitor visitor = zooFacade.getVisitorManager().findVisitorByID(idString);
				List<Ticket> tickets = zooFacade.getTicketManager().getUsedTickets().get(visitor.getVisitor_id());
				if (tickets == null || tickets.isEmpty()) {
					zooFacade.getPanelDialogMessage()
							.showInformationDialog("No tickets found with the entered ID  :  { " + visitor.getID() + " }");
				} else {
					for (Ticket ticket : tickets) {
						ticketList.append(ticket).append("\n");
					}
					zooFacade.getPanelDialogMessage().showListDialog(ticketList.toString(), "Used Tickets list");
				}
				return;
			} catch (NumberFormatException e) {
				zooFacade.getPanelDialogMessage().showErrorDialog("Invalid ID format.");
			}
			
		}
	}

	public void displayAllUsedTickets() {
		StringBuilder ticketListStringBuilder = new StringBuilder();
		if (zooFacade.getTicketManager().getUsedTickets().isEmpty()) {
			zooFacade.getPanelDialogMessage().showInformationDialog("No tickets to display");
			return;
		}

		Collection<List<Ticket>> usedTickets = zooFacade.getTicketManager().getUsedTickets().values();

		for (List<Ticket> tickets : usedTickets) {
			for (Ticket ticket : tickets) {
				ticketListStringBuilder.append(ticket).append("\n");
			}
		}
		zooFacade.getPanelDialogMessage().showListDialog(ticketListStringBuilder.toString(), "Used Tickets list");
	}
}
