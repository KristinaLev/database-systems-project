package zooPackage.Management;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.swing.JList;
import javax.swing.JPanel;
import zooPackage.DB.DBUtil;
import zooPackage.DB.TicketDAO;
import zooPackage.Entities.Promotions.Promotion;
import zooPackage.Entities.Tickets.Ticket;
import zooPackage.Entities.Users.Employee;
import zooPackage.Entities.Users.Visitor;
import zooPackage.Enum.StatusCheck;
import zooPackage.Enum.SubscriptionType;
import zooPackage.Enum.TicketType;
import zooPackage.Interface.TicketManager_Interface;

public class TicketManager implements TicketManager_Interface {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private HashSet<Ticket> tickets = new HashSet<>();
	private HashMap<Integer, List<Ticket>> ticketHistory = new HashMap<>();
	private HashMap<Integer, List<Ticket>> usedTickets = new HashMap<>();
	private HashMap<Integer, List<Ticket>> canceledTickets = new HashMap<>();
	private EnumSet<TicketType> ticketTypes = EnumSet.allOf(TicketType.class);
	private static TicketManager instance;

	private TicketManager() {
	}

	public static TicketManager getInstance() {
		if (instance == null) {
			instance = new TicketManager();
		}
		return instance;
	}

	public void addTicketToZooAndDatabase(Ticket ticket) {
		addTicket(ticket);
		try (Connection conn = DBUtil.getConnection()) {
			TicketDAO dao = new TicketDAO(conn);
			dao.insertTicketToDatabase(ticket);
		} catch (SQLException e) {
			e.printStackTrace();
			zooFacade.getPanelDialogMessage()
					.showDBErrorDialog("Failed to save Ticket [id = " + ticket.getTicket_ID() + "] to database.");
		}
	}

	@Override
	public void createDefaultTickets() {
		LocalDate currentDate = LocalDate.now();
		LocalDate expDate = currentDate.plusYears(1);

		Employee emp = zooFacade.getEmployeeManager().getEmployees().iterator().next();
		int emp_id = emp.getEmployee_ID();

		Visitor vis = zooFacade.getVisitorManager().getVisitors().iterator().next();
		int vis_id = vis.getVisitor_id();

		Ticket ticket1 = new Ticket(0, emp_id, vis_id, null, TicketType.ADULT, 71.00, StatusCheck.NO, StatusCheck.NO,
				currentDate, expDate, null);
		Ticket ticket2 = new Ticket(0, emp_id, vis_id, null, TicketType.ADULT, 71.00, StatusCheck.NO, StatusCheck.NO,
				currentDate, expDate, null);

		zooFacade.getTicketManager().addTicketToZooAndDatabase(ticket1);
		zooFacade.getTicketManager().addTicketToZooAndDatabase(ticket2);
		zooFacade.getVisitorManager().addTicketToVisitor(vis_id, ticket1);
		zooFacade.getVisitorManager().addTicketToVisitor(vis_id, ticket2);
	}

	@Override
	public void buyTicket() {
		String visitor_id = zooFacade.getLoginManager().loginVisitor();
		zooFacade.getPanelFields().setIdField("");

		if (visitor_id == null) {
			return;
		}

		Visitor visitor = zooFacade.getVisitorManager().findVisitorByID(visitor_id);
		if (visitor == null) {
			zooFacade.getPanelDialogMessage()
					.showInformationDialog("No visitor with this ID was found.\nPlease register as a new visitor.");
			visitor = zooFacade.getVisitorManager().createVisitor();
			if (visitor == null) {
				return;
			}
		}

		if (zooFacade.getSubscriptionValidation().isVisitorSubscribed(visitor_id)) {
			handleSubscribedVisitor(visitor);
		} else {
			handleNonSubscribedVisitor(visitor);
		}
	}

	private void handleSubscribedVisitor(Visitor visitor) {
		String subType = zooFacade.getSubscriptionValidation()
				.checkWhatSubscriptionVisitorSubscribed(visitor.getVisitor_id());

		if (subType == null)
			return;

		if (subType.equals(SubscriptionType.PROMOTION.getDisplayName())) {
			handleTicketPurchase(visitor, false);
		} else {
			zooFacade.getPanelDialogMessage()
					.showInformationDialog("You already have an annual subscription.\nTicket purchase is not allowed.");
		}
	}

	private void handleNonSubscribedVisitor(Visitor visitor) {
		boolean wantsToSubscribe = zooFacade.getSubscriptionManager().askToSubscribe(visitor.getID());

		if (!wantsToSubscribe) {
			handleTicketPurchase(visitor, true);
			return;
		}

		String subChoice = zooFacade.getSubscriptionSelection()
				.selectSubscruptionType(new String[] { "Annual Subscription", "Promotion Subscription" });

		if (subChoice == null)
			return;

		switch (subChoice) {
		case "Annual Subscription":
			zooFacade.getSubscriptionSelection().handleAnnualSubscriptionSelection(visitor);
			break;
		case "Promotion Subscription":
			handleTicketPurchase(visitor, false);
			break;
		default:
			zooFacade.getPanelDialogMessage().showErrorDialog("Invalid subscription choice.");
		}
	}

	public void handleTicketPurchase(Visitor visitor, boolean isRegularPurchase) {

		Promotion promotion = null;
		if (!isRegularPurchase) {
			if (!isRegularPurchase && zooFacade.getPromotionValidation().checkIfPromotionListEmpty()) {
				return;
			}
			promotion = zooFacade.getPromotionSelection().selectPromotion();
			if (promotion == null)
				return;
		}

		String category = zooFacade.getTicketSelection()
				.selectTicketType(new String[] { "Single", "Couple", "Parent", "Family" });

		if (category == null)
			return;

		switch (category) {
		case "Single":
			zooFacade.getTicketSelection().selectTicket(TicketType.ADULT, TicketType.DISABLED, visitor, promotion,
					isRegularPurchase);
			break;
		case "Couple":
			zooFacade.getTicketSelection().selectTicket(TicketType.COUPLE, TicketType.COUPLE, visitor, promotion,
					isRegularPurchase);
			break;
		case "Parent":
			zooFacade.getTicketSelection().selectTicket(TicketType.PARENT_PLUS_ONE, TicketType.PARENT_PLUS_SIX_OR_MORE,
					visitor, promotion, isRegularPurchase);
			break;
		case "Family":
			zooFacade.getTicketSelection().selectTicket(TicketType.COUPLE_PLUS_ONE, TicketType.COUPLE_PLUS_SIX_OR_MORE,
					visitor, promotion, isRegularPurchase);
			break;
		default:
			zooFacade.getPanelDialogMessage().showErrorDialog("Invalid choice!");
		}
	}

	@Override
	public void cancelTicket() {
		while (true) {
			if (getActiveTickets().isEmpty()) {
				zooFacade.getPanelDialogMessage().showInformationDialog("No active tickets to remove");
				return;
			}

			String idString = zooFacade.getPanelDialogMessage().showInputDialog("Enter your ID:", "Find tickets by ID");
			if (idString == null) {
				zooFacade.getPanelDialogMessage().showInformationDialog("Returning to Ticket Management System Menu");
				return;
			}

			if (!zooFacade.getPersonValidation().isValidIDFormat(idString))
				continue;

			Visitor newVisitor = zooFacade.getVisitorManager().findVisitorByID(idString);

			if (newVisitor == null) {
				zooFacade.getPanelDialogMessage()
						.showInformationDialog("No visitor with this ID was found in the system.\n"
								+ "Please register as a new visitor and enter your personal details.");
				continue;
			}

			zooFacade.getTicketSelection().selectTicketForCancel(newVisitor);
			return;
		}
	}

	public void handleTicketCancellation(JList<Ticket> ticketTypesJList, JPanel panel) {
		while (true) {
			int result = zooFacade.getPanelDialogMessage().showConfirmDialog(panel, "Cancel Menu");
			if (result != 0) {
				zooFacade.getPanelDialogMessage().showInformationDialog("The cancellation has been cancelled");
				return;
			}

			if (ticketTypesJList.getSelectedValuesList().isEmpty()) {
				zooFacade.getPanelDialogMessage().showWarningDialog(
						"Hi friend!\nIn order to finish the ticket cancellation you have to choose at least one ticket.");
				continue;
			}

			cancelSelectedTickets(ticketTypesJList.getSelectedValuesList());
			zooFacade.getPanelDialogMessage().showInformationDialog("The tickets have been removed.");
			return;
		}
	}

	@Override
	public void updateTicketInfo(Ticket ticket) {
		Ticket ticketToUpdate = updateTicketHistory(ticket);
		zooFacade.getTicketUtils().updateTicketStatusIsUsed(ticketToUpdate);
		zooFacade.getTicketUtils().updateTicketEntryDate(ticketToUpdate);
		removeTicket(ticketToUpdate);
	}

	@Override
	public Ticket updateTicketHistory(Ticket ticketToUpdate) {
		List<Ticket> tickets = getPurchasedTickets().get(ticketToUpdate.getVisitor_ID());
		for (Ticket ticket : tickets) {
			if (ticket.getTicket_ID() == ticketToUpdate.getTicket_ID()) {
				return ticket;
			}
		}
		return null;
	}

	@Override
	public void cancelSelectedTickets(List<Ticket> selectedTickets) {
		for (Ticket ticket : selectedTickets) {
			Ticket ticketToUpdate = updateTicketHistory(ticket);
			zooFacade.getTicketUtils().updateTicketCancelationStatus(ticketToUpdate);
			removeTicket(ticket);
		}
	}

	@Override
	public Ticket createNewTicket(TicketType ticketType, Promotion promotion, int visitorID) {
		LocalDate currentDate = LocalDate.now();
		LocalDate expDate = currentDate.plusYears(1);

		Employee loggedEmployee = zooFacade.getPasswordManager().getLoggedInEmployee();
		int employee_id = loggedEmployee.getEmployee_ID();

		double price = (promotion == null) ? ticketType.getSinglePrice()
				: zooFacade.getTicketUtils().calculatePrice(ticketType, promotion.getDiscountPercentage());

		Integer promotionId = (promotion != null) ? promotion.getPromotionID() : null;

		return new Ticket(0, employee_id, visitorID, promotionId, ticketType, price, StatusCheck.NO, StatusCheck.NO,
				currentDate, expDate, null);
	}

	@Override
	public void addTicket(Ticket ticket) {
		getActiveTickets().add(ticket);
	}

	public void addTicketToPurchaseHistory(Ticket ticket) {
		getActiveTickets().add(ticket);
	}

	@Override
	public void removeTicket(Ticket ticket) {
		getActiveTickets().remove(ticket);
	}

	@Override
	public HashSet<Ticket> getActiveTickets() {
		return tickets;
	}

	@Override
	public HashMap<Integer, List<Ticket>> getPurchasedTickets() {
		return ticketHistory;
	}

	@Override
	public HashMap<Integer, List<Ticket>> getUsedTickets() {
		return usedTickets;
	}

	public HashMap<Integer, List<Ticket>> getCanceledTickets() {
		return canceledTickets;
	}

	@Override
	public EnumSet<TicketType> getTicketTypes() {
		return ticketTypes;
	}

	public void clearAllTicketMaps() {
		getActiveTickets().clear();
		getPurchasedTickets().clear();
		getUsedTickets().clear();
		getCanceledTickets().clear();

	}

}
