package zooPackage.Interface;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import zooPackage.Entities.Promotions.Promotion;
import zooPackage.Entities.Tickets.Ticket;
import zooPackage.Enum.TicketType;

public interface TicketManager_Interface {
	public void createDefaultTickets();

	public void buyTicket();

	public void cancelTicket();

	public Ticket updateTicketHistory(Ticket ticketToUpdate);

	public void updateTicketInfo(Ticket ticket);

	public void cancelSelectedTickets(List<Ticket> selectedTickets);

	public Ticket createNewTicket(TicketType ticketType, Promotion promotion, int visitorID);

	public void addTicket(Ticket ticket);

	public void removeTicket(Ticket ticket);

	public HashSet<Ticket> getActiveTickets();

	public HashMap<Integer, List<Ticket>> getPurchasedTickets();

	public HashMap<Integer, List<Ticket>> getUsedTickets();

	public EnumSet<TicketType> getTicketTypes();

}
