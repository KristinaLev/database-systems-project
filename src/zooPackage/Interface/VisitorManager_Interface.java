package zooPackage.Interface;

import java.util.HashSet;

import zooPackage.Entities.Tickets.Ticket;
import zooPackage.Entities.Users.Visitor;

public interface VisitorManager_Interface {

	public void createDefaultVisitors();

	public Visitor createVisitor();

	public void addTicketToVisitor(int visitorID, Ticket newTicket);

	public Visitor findVisitorByID(String visitorID);

	public void addVisitor(Visitor visitor);

	public HashSet<Visitor> getVisitors();

}
