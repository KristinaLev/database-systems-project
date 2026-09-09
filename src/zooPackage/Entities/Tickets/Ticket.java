package zooPackage.Entities.Tickets;

import java.time.LocalDate;
import zooPackage.Enum.StatusCheck;
import zooPackage.Enum.TicketType;

public class Ticket {

	private int ticket_id;
	private int visitor_id;
	private int employee_id;
	private Integer promotion_id;
	private TicketType ticketType;
	private double ticketPrice;
	private StatusCheck isCanceled;
	private StatusCheck isUsed;
	private LocalDate releaseDate;
	private LocalDate expirationDate;
	private LocalDate entryDate;

	public Ticket(int ticket_id, int employee_id, int visitor_id, Integer promotion_id, TicketType ticketType,
			double ticketPrice, StatusCheck canBeCanceled, StatusCheck isUsed, LocalDate releaseDate,
			LocalDate expirationDate, LocalDate entryDate) {
		this.ticket_id = ticket_id;
		this.visitor_id = visitor_id;
		this.employee_id = employee_id;
		this.promotion_id = promotion_id;
		this.ticketType = ticketType;
		this.ticketPrice = ticketPrice;
		this.isCanceled = canBeCanceled;
		this.isUsed = isUsed;
		this.releaseDate = releaseDate;
		this.expirationDate = expirationDate;
		this.entryDate = entryDate;
	}

	public int getTicket_ID() {
		return ticket_id;
	}

	public void setTicket_ID(int ticket_id) {
		this.ticket_id = ticket_id;
	}

	public int getVisitor_ID() {
		return visitor_id;
	}

	public void setVisitor_ID(int visitor_id) {
		this.visitor_id = visitor_id;
	}

	public int getEmployee_ID() {
		return employee_id;
	}

	public void setEmployee_ID(int employee_id) {
		this.employee_id = employee_id;
	}

	public Integer getPromotion_ID() {
		return promotion_id;
	}

	public void setPromotion_ID(Integer promotion_id) {
		this.promotion_id = promotion_id;
	}

	public TicketType getTicketType() {
		return ticketType;
	}

	public void setTicketType(TicketType ticketType) {
		this.ticketType = ticketType;
	}

	public double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public StatusCheck getIsCanceled() {
		return isCanceled;
	}

	public void setIsCanceled(StatusCheck isCanceled) {
		this.isCanceled = isCanceled;
	}

	public StatusCheck getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(StatusCheck isUsed) {
		this.isUsed = isUsed;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	public LocalDate getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(LocalDate entryDate) {
		this.entryDate = entryDate;
	}

	@Override
	public String toString() {
		return "Ticket:  No." + ticket_id + "  |  Type:  " + ticketType.getTypeName() + "  |  Price:  $" + ticketPrice
				+ "  |  Visitor ID:  " + visitor_id + "  |  Rls. Date:  {" + releaseDate + "}  |  Exp. Date:  {"
				+ expirationDate + "}" + "  |  Is used: " + getIsUsed() + "  |  Entry Date:  {" + entryDate + "}";
	}

}
