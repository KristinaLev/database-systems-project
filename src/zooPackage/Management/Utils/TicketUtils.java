package zooPackage.Management.Utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import zooPackage.DB.DBUtil;
import zooPackage.DB.TicketDAO;
import zooPackage.Entities.Promotions.Promotion;
import zooPackage.Entities.Tickets.Ticket;
import zooPackage.Entities.Users.Visitor;
import zooPackage.Enum.StatusCheck;
import zooPackage.Enum.TicketType;
import zooPackage.Management.ZooFacade;

public class TicketUtils {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static TicketUtils instance;

	private TicketUtils() {
	}

	public static TicketUtils getInstance() {
		if (instance == null) {
			instance = new TicketUtils();
		}
		return instance;
	}

	public static String[] ticketTypeArrayBuilder(TicketType first, TicketType last) {

		EnumSet<TicketType> ticketTypes = EnumSet.range(first, last);
		String[] ticketInfo = new String[ticketTypes.size()];

		int count = 0;

		for (TicketType ticketType : ticketTypes) {
			double price = ticketType.getSinglePrice();
			ticketInfo[count] = "Ticket type: " + ticketType.getTypeName() + " - $" + price;
			count++;
		}
		return ticketInfo;
	}

	public String[] promotionTicketTypeArrayBuilder(TicketType first, TicketType last, Promotion promotion) {

		EnumSet<TicketType> ticketTypes = EnumSet.range(first, last);
		String[] ticketInfo = new String[ticketTypes.size()];

		int count = 0;

		for (TicketType ticketType : ticketTypes) {
			double price = (promotion == null) ? ticketType.getSinglePrice()
					: calculatePrice(ticketType, promotion.getDiscountPercentage());
			ticketInfo[count] = "Ticket type: " + ticketType.getTypeName() + " - $" + price;
			count++;
		}
		return ticketInfo;
	}

	public StringBuilder ticketStringBuilder(long id) {
		StringBuilder ticketList = new StringBuilder();
		Visitor visitor = zooFacade.getVisitorManager().findVisitorByID("" + id);
		for (Ticket ticket : zooFacade.getTicketManager().getActiveTickets()) {
			if (visitor.getVisitor_id() == ticket.getVisitor_ID()) {
				ticketList.append(ticket).append("\n");
			}
		}
		return ticketList;
	}

	public void addTicketToMap(HashMap<Integer, List<Ticket>> hashMap, int visitor_id, Ticket ticket) {
		List<Ticket> ticketList = hashMap.get(visitor_id);
		if (ticketList == null) {
			ticketList = new ArrayList<>();
			hashMap.put(visitor_id, ticketList);
		}
		ticketList.add(ticket);
	}

	public void updateTicketCancelationStatus(Ticket ticket) {
		try (Connection conn = DBUtil.getConnection()) {
			TicketDAO dao = new TicketDAO(conn);
			dao.updateTicketCancelationStatus(ticket.getTicket_ID(), "" + StatusCheck.YES);
			ticket.setIsCanceled(StatusCheck.YES);
		} catch (SQLException e) {
			e.printStackTrace();
			zooFacade.getPanelDialogMessage()
					.showDBErrorDialog("Failed to update ticket cancelation status from database.");
		}
	}

	public void updateTicketStatusIsUsed(Ticket ticket) {
		try (Connection conn = DBUtil.getConnection()) {
			TicketDAO dao = new TicketDAO(conn);
			dao.updateTicketStatus(ticket.getTicket_ID(), "" + StatusCheck.YES);
			ticket.setIsUsed(StatusCheck.YES);
		} catch (SQLException e) {
			e.printStackTrace();
			zooFacade.getPanelDialogMessage().showDBErrorDialog("Failed to update ticket active status from database.");
		}
	}

	public void updateTicketEntryDate(Ticket ticket) {
		try (Connection conn = DBUtil.getConnection()) {
			TicketDAO dao = new TicketDAO(conn);
			LocalDate currentDate = LocalDate.now();
			dao.updateTicketEntryDate(ticket.getTicket_ID(), Date.valueOf(currentDate));
			ticket.setEntryDate(currentDate);
		} catch (SQLException e) {
			e.printStackTrace();
			zooFacade.getPanelDialogMessage().showDBErrorDialog("Failed to update ticket active status from database.");
		}
	}

	public double calculatePrice(TicketType ticket, int discountPercentage) {

		double multiplier = (100.0 - discountPercentage) / 100.0;
		double price = ticket.getSinglePrice() * multiplier;
		return Double.parseDouble(zooFacade.getAnimalManager().getFormatter().format(price));
	}

}
