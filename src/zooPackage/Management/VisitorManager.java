package zooPackage.Management;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import zooPackage.DB.DBUtil;
import zooPackage.DB.VisitorDAO;
import zooPackage.Entities.Tickets.Ticket;
import zooPackage.Entities.Users.Employee;
import zooPackage.Entities.Users.Visitor;
import zooPackage.Interface.VisitorManager_Interface;

public class VisitorManager implements VisitorManager_Interface {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private HashSet<Visitor> visitors = new HashSet<>();
	private static VisitorManager instance;

	private VisitorManager() {

	}

	public static VisitorManager getInstance() {
		if (instance == null) {
			instance = new VisitorManager();
		}
		return instance;
	}

	@Override
	public void createDefaultVisitors() {
		try (Connection conn = DBUtil.getConnection()) {
			VisitorDAO visitorDAO = new VisitorDAO(conn);

			Employee emp = zooFacade.getEmployeeManager().getEmployees().iterator().next();

			Visitor visitor1 = new Visitor(0, "Igor", "Chikati", 11111111, LocalDate.parse("1998-02-19"), "0546419400",
					emp.getEmployee_ID());
			int visitorId1 = visitorDAO.insertVisitorAndGetId(visitor1);
			visitor1.setVisitor_id(visitorId1);
			addVisitor(visitor1);

			Visitor visitor2 = new Visitor(0, "Amit", "Shlomo", 22222222, LocalDate.parse("1998-02-19"), "0546419400",
					emp.getEmployee_ID());
			int visitorId2 = visitorDAO.insertVisitorAndGetId(visitor2);
			visitor2.setVisitor_id(visitorId2);
			addVisitor(visitor2);

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to insert default visitors.", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public Visitor createVisitor() {
		while (true) {
			JPanel personalInfoPanel = zooFacade.getPanelDisplay().createPersonalInfoPanel();

			if (!zooFacade.getPersonValidation().validatePersonalInfo(personalInfoPanel, false)) {
				return null;
			}

			try (Connection conn = DBUtil.getConnection()) {
				VisitorDAO visitorDAO = new VisitorDAO(conn);

				Visitor newVisitor = getNewVisitorInfo();
				if (newVisitor == null) {
					return null;
				}
				
				int visitorId = visitorDAO.insertVisitorAndGetId(newVisitor);
				newVisitor.setVisitor_id(visitorId);
				addVisitor(newVisitor);

				zooFacade.getPanelFields().clearPersonalInfoPanel();
				return newVisitor;

			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Failed to create visitor due to a database error.",
						"Database Error", JOptionPane.ERROR_MESSAGE);
				return null;
			} catch (NumberFormatException | DateTimeParseException e) {
				JOptionPane.showMessageDialog(null, "Invalid input for ID or date of birth.", "Input Error",
						JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
	}

	public Visitor getNewVisitorInfo() {
		try {
			Employee loggedEmployee = zooFacade.getPasswordManager().getLoggedInEmployee();
			int employee_id = loggedEmployee.getEmployee_ID();
			String fName = zooFacade.getPanelFields().getFirstNameField().getText();
			String lName = zooFacade.getPanelFields().getLastNameField().getText();
			long id = Long.parseLong(zooFacade.getPanelFields().getIdField().getText());
			LocalDate dob = LocalDate.parse(zooFacade.getPanelFields().getDobField().getText());
			String phone = zooFacade.getPanelFields().getPhoneNumberField().getText();

			Visitor newVisitor = new Visitor(0, fName, lName, id, dob, phone, employee_id);
			return newVisitor;

		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void addTicketToVisitor(int visitorID, Ticket newTicket) {
		for (Visitor visitor : zooFacade.getVisitorManager().getVisitors()) {
			if (visitor.getVisitor_id() == visitorID) {
				zooFacade.getTicketUtils().addTicketToMap(zooFacade.getTicketManager().getPurchasedTickets(), visitorID,
						newTicket);
				break;
			}
		}
	}

	@Override
	public Visitor findVisitorByID(String visitorID) {
		for (Visitor visitor : getVisitors()) {
			if (visitorID.equals("" + visitor.getID()))
				return visitor;
		}
		return null;
	}

	public Visitor findVisitorByVisitor_ID(int visitor_ID) {
		for (Visitor visitor : getVisitors()) {
			if (visitor_ID == visitor.getVisitor_id())
				return visitor;
		}
		zooFacade.getPanelDialogMessage().showInformationDialog("No visitor with this ID was found in the system.\n"
				+ "Please register as a new visitor and enter your personal details.");
		return null;
	}

	@Override
	public void addVisitor(Visitor visitor) {
		visitors.add(visitor);
	}

	@Override
	public HashSet<Visitor> getVisitors() {
		return visitors;
	}

}
