package zooPackage.Management.Selection;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import zooPackage.Entities.Promotions.Promotion;
import zooPackage.Entities.Tickets.Ticket;
import zooPackage.Entities.Users.Visitor;
import zooPackage.Enum.SubscriptionType;
import zooPackage.Enum.TicketType;
import zooPackage.Management.ZooFacade;

public class TicketSelection {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static TicketSelection instance;

	private TicketSelection() {
	}

	public static TicketSelection getInstance() {
		if (instance == null) {
			instance = new TicketSelection();
		}
		return instance;
	}

	public Ticket selectEntryTicket(String visitorID) {
		if (visitorID == null) {
			return null;
		}

		List<Ticket> visitorTickets = new ArrayList<>();
		Visitor visitor = zooFacade.getVisitorManager().findVisitorByID(visitorID);
		for (Ticket ticket : zooFacade.getTicketManager().getActiveTickets()) {
			if (ticket.getVisitor_ID() == visitor.getVisitor_id()) {
				visitorTickets.add(ticket);
			}
		}

		if (visitorTickets.isEmpty()) {
			zooFacade.getPanelDialogMessage().showInformationDialog("No tickets found for this visitor.");
			return null;
		}

		Ticket[] tickets = visitorTickets.toArray(new Ticket[0]);
		JList<Ticket> ticketTypesJList = new JList<>(tickets);
		ticketTypesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel panel = zooFacade.getPanelDisplay().createTicketListPanel(ticketTypesJList,
				"Choose one ticket for entering to park:");

		int result;
		while (true) {
			result = zooFacade.getPanelDialogMessage().showConfirmDialog(panel, "Ticket Menu");
			if (result != 0) {
				zooFacade.getPanelDialogMessage().showInformationDialog("Choosing ticket canceled.");
				return null;
			}

			if (ticketTypesJList.getSelectedValuesList().isEmpty()) {
				zooFacade.getPanelDialogMessage()
						.showWarningDialog("Hi friend!\nIn order to enter to the park you have to choose one ticket.");
				continue;
			} else {
				List<Ticket> selectedValues = ticketTypesJList.getSelectedValuesList();
				Ticket selectedTicket = selectedValues.get(0);
				zooFacade.getTicketUtils().addTicketToMap(zooFacade.getTicketManager().getUsedTickets(),
						selectedTicket.getTicket_ID(), selectedTicket);
				return selectedTicket;
			}
		}
	}

	public boolean selectTicket(TicketType first, TicketType last, Visitor visitor, Promotion promotion, boolean flag) {
		zooFacade.getTicketUtils();
		String[] ticketInfo = zooFacade.getTicketUtils().promotionTicketTypeArrayBuilder(first, last, promotion);

		JList<String> ticketTypesJList = new JList<>(ticketInfo);
		ticketTypesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JPanel panel = zooFacade.getPanelDisplay().createTicketTypeListPanel(ticketTypesJList,
				"Choose which ticket you want to buy:");

		while (true) {
			int result = zooFacade.getPanelDialogMessage().showConfirmDialog(panel, "Ticket Types Menu");
			if (result != 0) {
				zooFacade.getPanelDialogMessage().showInformationDialog("Ticket purchase was cancelled");
				return false;
			}

			if (ticketTypesJList.getSelectedValuesList().isEmpty()) {
				zooFacade.getPanelDialogMessage().showWarningDialog(
						"Hi friend!\nYou must select one ticket from the list in order to purchase one.");
				continue;
			}

			if (!flag) {
				TicketType selectedTicketType = getSelectedTicketWithPromotionType(ticketTypesJList.getSelectedValue(),
						promotion);
				if (selectedTicketType == null) {
					zooFacade.getPanelDialogMessage().showErrorDialog("Selected ticket type not found.");
					continue;
				}

				Ticket newTicket = zooFacade.getTicketManager().createNewTicket(selectedTicketType, promotion,
						visitor.getVisitor_id());
				zooFacade.getVisitorManager().addTicketToVisitor(visitor.getVisitor_id(), newTicket);
				zooFacade.getTicketManager().addTicketToZooAndDatabase(newTicket);
				zooFacade.getSubscriptionManager().createNewSubscription(TicketType.PROMOTION,
						SubscriptionType.PROMOTION, visitor.getVisitor_id());
				zooFacade.getSubscriptionManager().addRelationBetweenSubscriberAndPromotion(visitor,
						promotion.getPromotionID());
				zooFacade.getPanelDialogMessage().showInformationDialog("Ticket purchase completed");
				return true;
			} else {
				TicketType selectedTicketType = getSelectedTicketType(ticketTypesJList.getSelectedValue());
				if (selectedTicketType == null) {
					zooFacade.getPanelDialogMessage().showErrorDialog("Selected ticket type not found.");
					continue;
				}

				Ticket newTicket = zooFacade.getTicketManager().createNewTicket(selectedTicketType, promotion,
						visitor.getVisitor_id());
				zooFacade.getVisitorManager().addTicketToVisitor(visitor.getVisitor_id(), newTicket);
				zooFacade.getTicketManager().addTicketToZooAndDatabase(newTicket);
				zooFacade.getPanelDialogMessage().showInformationDialog("Ticket purchase completed");
				return true;
			}
		}

	}

	public TicketType getSelectedTicketType(String selectedValue) {
		for (TicketType ticket : zooFacade.getTicketManager().getTicketTypes()) {
			double price = ticket.getSinglePrice();
			String ticketTypeString = "Ticket type: " + ticket.getTypeName() + " - $" + price;
			if (ticketTypeString.equals(selectedValue)) {
				return ticket;
			}
		}
		return null;
	}

	public TicketType getSelectedTicketWithPromotionType(String selectedValue, Promotion promotion) {
		for (TicketType ticket : zooFacade.getTicketManager().getTicketTypes()) {
			double price = (promotion == null) ? ticket.getSinglePrice()
					: zooFacade.getTicketUtils().calculatePrice(ticket, promotion.getDiscountPercentage());
			String ticketTypeString = "Ticket type: " + ticket.getTypeName() + " - $" + price;
			if (ticketTypeString.equals(selectedValue)) {
				return ticket;
			}
		}
		return null;
	}

	public String selectTicketType(String[] categories) {
		JComboBox<String> categoryComboBox = new JComboBox<>(categories);
		JPanel panel = zooFacade.getPanelDisplay().createTicketTypeCategoryBoxPanel(categoryComboBox);

		while (true) {
			int result = zooFacade.getPanelDialogMessage().showConfirmDialog(panel, "Category Menu:");
			if (result != 0) {
				zooFacade.getPanelDialogMessage().showInformationDialog("Buying canceled.");
				return null;
			}
			return (String) categoryComboBox.getSelectedItem();
		}
	}

	public void selectTicketForCancel(Visitor visitor) {
		if (visitor == null) {
			return;
		}

		List<Ticket> visitorTickets = getVisitorTickets(visitor.getVisitor_id());

		if (visitorTickets.isEmpty()) {
			zooFacade.getPanelDialogMessage().showInformationDialog("No tickets found with the entered ID.");
			return;
		}

		Ticket[] tickets = visitorTickets.toArray(new Ticket[0]);
		JList<Ticket> ticketTypesJList = new JList<>(tickets);
		ticketTypesJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		JPanel panel = zooFacade.getPanelDisplay().createTicketListPanel(ticketTypesJList,
				"Choose the ticket do you want to cancel:\n(Use Ctrl Key for choosing more then 1 ticket)");

		zooFacade.getTicketManager().handleTicketCancellation(ticketTypesJList, panel);
	}

	public List<Ticket> getVisitorTickets(int visitorID) {
		List<Ticket> visitorTickets = new ArrayList<>();
		for (Ticket ticket : zooFacade.getTicketManager().getActiveTickets()) {
			if (ticket.getVisitor_ID() == visitorID) {
				visitorTickets.add(ticket);
			}
		}
		return visitorTickets;
	}

}
