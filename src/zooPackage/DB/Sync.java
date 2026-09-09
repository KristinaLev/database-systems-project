package zooPackage.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import zooPackage.Entities.Animals.AnimalRecord;
import zooPackage.Entities.Promotions.Promotion;
import zooPackage.Entities.Subscriptions.Subscription;
import zooPackage.Entities.Tickets.Ticket;
import zooPackage.Entities.Users.Employee;
import zooPackage.Entities.Users.Visitor;
import zooPackage.Enum.StatusCheck;
import zooPackage.Management.ZooFacade;

public class Sync {
	private static Sync instance;
	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private final List<String> syncedEntities = new ArrayList<>();

	private Sync() {
	}

	public static Sync getInstance() {
		if (instance == null) {
			instance = new Sync();
		}
		return instance;
	}

	private boolean testDatabaseConnection(String password) {
		try {
			DBUtil.setPassword(password);
			try (Connection conn = DBUtil.getConnection()) {
				return true;
			}
		} catch (SQLException e) {
			return false;
		}
	}

	public void SYNC_DB() {

		while (true) {
			int option = JOptionPane.showConfirmDialog(null, zooFacade.getPanelFields().getDbPasswordField(),
					"Enter PostgreSQL password", JOptionPane.OK_CANCEL_OPTION);
			if (option != 0) {
				System.exit(0);
			}
			String password = new String(zooFacade.getPanelFields().getDbPasswordField().getText());
			if (password.isEmpty() || password == null) {
				zooFacade.getPanelDialogMessage().showErrorDialog("Password cannot be empty. Please try again.");
				zooFacade.getPanelFields().setDbPasswordField("");
				continue;
			}
			if (testDatabaseConnection(password)) {
				DBUtil.setPassword(password);
				break;
			} else {
				zooFacade.getPanelFields().setDbPasswordField("");
				zooFacade.getPanelDialogMessage().showErrorDialog("Incorrect password. Please try again.");
			}
		}

		syncEmployeeFromDatabase();
		syncAnimalFromDatabase();
		syncSubscriptionFromDatabase();
		syncPromotionFromDatabase();
		syncVisitorFromDatabase();
		syncTicketFromDatabase();
		syncSubscribersFromDatabase();
		syncSubscriberPromotionsFromDatabase();
		syncSubscriberSubscriptionFromDatabase();

//		StringBuilder sb = new StringBuilder("The following entities were successfully synced from the database:\n");
//		for (String entity : syncedEntities) {
//			sb.append("- ").append(entity).append("\n");
//		}
//		zooFacade.getPanelDialogMessage().showInformationDialog(sb.toString());
	}

	public void syncAnimalFromDatabase() {
		try (Connection conn = DBUtil.getConnection()) {
			AnimalDAO dao = new AnimalDAO(conn);

			List<AnimalRecord> animalRecords = dao.getAllAnimalRecords();

			if (animalRecords.isEmpty()) {
				zooFacade.getAnimalManagement().addDefaultAnimalsToDatabase();
				animalRecords = dao.getAllAnimalRecords();
			}

			zooFacade.getAnimalManager().getAnimals().clear();
			for (AnimalRecord record : animalRecords) {
				zooFacade.getAnimalManager().getAnimals().add(record.getAnimal());
			}

			syncedEntities.add("Animals");

		} catch (SQLException e) {
			e.printStackTrace();
			zooFacade.getPanelDialogMessage().showDBErrorDialog("Failed to sync animals from database.");
		}
	}

	public void syncTicketFromDatabase() {
		try (Connection conn = DBUtil.getConnection()) {
			TicketDAO dao = new TicketDAO(conn);
			List<Ticket> tickets = dao.loadAllTicketsFromDB();

			if (tickets.isEmpty()) {
				zooFacade.getTicketManager().createDefaultTickets();
				return;
			}

			zooFacade.getTicketManager().clearAllTicketMaps();

			for (Ticket ticket : tickets) {
				zooFacade.getTicketUtils().addTicketToMap(zooFacade.getTicketManager().getPurchasedTickets(),
						ticket.getVisitor_ID(), ticket);

				if (ticket.getIsUsed() == StatusCheck.YES) {
					zooFacade.getTicketUtils().addTicketToMap(zooFacade.getTicketManager().getUsedTickets(),
							ticket.getVisitor_ID(), ticket);
				} else if (ticket.getIsCanceled() == StatusCheck.YES) {
					zooFacade.getTicketUtils().addTicketToMap(zooFacade.getTicketManager().getCanceledTickets(),
							ticket.getVisitor_ID(), ticket);
				} else {
					zooFacade.getTicketManager().addTicket(ticket);
				}
			}

			syncedEntities.add("Tickets");

		} catch (SQLException e) {
			e.printStackTrace();
			zooFacade.getPanelDialogMessage().showDBErrorDialog("Failed to sync tickets from database.");
		}
	}

	public void syncEmployeeFromDatabase() {
		try (Connection conn = DBUtil.getConnection()) {
			EmployeeDAO dao = new EmployeeDAO(conn);
			List<Employee> employees = dao.getAllEmployees();

			if (employees.isEmpty()) {
				zooFacade.getEmployeeManager().createDefaultEmployees();
				return;
			}

			zooFacade.getEmployeeManager().getEmployees().clear();
			zooFacade.getPasswordManager().clear();

			for (Employee emp : employees) {
				zooFacade.getEmployeeManager().getEmployees().add(emp);
				zooFacade.getPasswordManager().registerEmployee(emp);
			}

				syncedEntities.add("Employees");

		} catch (SQLException e) {
			e.printStackTrace();
			zooFacade.getPanelDialogMessage().showDBErrorDialog("Failed to sync employees from database.");
		}
	}

	public void syncVisitorFromDatabase() {
		try (Connection conn = DBUtil.getConnection()) {
			VisitorDAO dao = new VisitorDAO(conn);
			List<Visitor> visitors = dao.getAllVisitors();

			if (visitors.isEmpty()) {
				zooFacade.getVisitorManager().createDefaultVisitors();
				return;
			}

			zooFacade.getVisitorManager().getVisitors().clear();
			zooFacade.getVisitorManager().getVisitors().addAll(visitors);

			syncedEntities.add("Visitors");

		} catch (SQLException e) {
			e.printStackTrace();
			zooFacade.getPanelDialogMessage().showDBErrorDialog("Failed to sync visitors from database.");
		}
	}

	public void syncPromotionFromDatabase() {
		try (Connection conn = DBUtil.getConnection()) {
			PromotionDAO dao = new PromotionDAO(conn);
			if (dao.loadAllPromotionfromDB().isEmpty()) {
				zooFacade.getPromotionManager().createDefaultPromotions();
				return;
			}

			List<Promotion> promos = dao.loadAllPromotionfromDB();

			zooFacade.getPromotionManager().getPromotions().clear();
			zooFacade.getPromotionManager().getPromotions().addAll(promos);

			syncedEntities.add("Promotions");

		} catch (SQLException e) {
			e.printStackTrace();
			zooFacade.getPanelDialogMessage().showDBErrorDialog("Failed to sync promotions from database.");
		}
	}

	public void syncSubscriptionFromDatabase() {
		try (Connection conn = DBUtil.getConnection()) {
			SubscriptionDAO dao = new SubscriptionDAO(conn);
			List<Subscription> subs = dao.getAllSubscriptions();

			if (subs.isEmpty()) {
				zooFacade.getSubscriptionManager().createDefaultSubscriptions();
				return;
			}

			zooFacade.getSubscriptionManager().getSubscriptions().clear();
			zooFacade.getSubscriptionManager().getSubscriptions().addAll(subs);

			syncedEntities.add("Subscriptions");

		} catch (SQLException e) {
			e.printStackTrace();
			zooFacade.getPanelDialogMessage().showDBErrorDialog("Failed to sync subscriptions from database.");
		}
	}

	public void syncSubscribersFromDatabase() {
		try (Connection conn = DBUtil.getConnection()) {
			SubscriptionDAO dao = new SubscriptionDAO(conn);
			List<Visitor> subscribers = dao.getAllSubscribers();

			if (subscribers.isEmpty()) {
				return;
			}

			zooFacade.getSubscriptionManager().getSubscribers().clear();
			zooFacade.getSubscriptionManager().getSubscribers().addAll(subscribers);

			syncedEntities.add("Subscribers");

		} catch (SQLException e) {
			e.printStackTrace();
			zooFacade.getPanelDialogMessage().showDBErrorDialog("Failed to sync subscribers from database.");
		}
	}

	public void syncSubscriberPromotionsFromDatabase() {
		try (Connection conn = DBUtil.getConnection()) {
			SubscriptionDAO dao = new SubscriptionDAO(conn);
			HashMap<Integer, List<Promotion>> subscriberPromotions = dao.getAllSubscriberPromotions();

			if (subscriberPromotions.isEmpty()) {
				return;
			}

			zooFacade.getSubscriptionManager().getSubscriberPromotions().clear();
			zooFacade.getSubscriptionManager().getSubscriberPromotions().putAll(subscriberPromotions);

			syncedEntities.add("Subscriber Promotions");

		} catch (SQLException e) {
			e.printStackTrace();
			zooFacade.getPanelDialogMessage().showDBErrorDialog("Failed to sync subscribers promotions from database.");
		}
	}

	public void syncSubscriberSubscriptionFromDatabase() {
		try (Connection conn = DBUtil.getConnection()) {
			SubscriptionDAO dao = new SubscriptionDAO(conn);
			HashMap<Integer, Subscription> subscriberSubscription = dao.getAllSubscriberSubscription();

			if (subscriberSubscription.isEmpty()) {
				return;
			}

			zooFacade.getSubscriptionManager().getSubscriberSubscription().clear();
			zooFacade.getSubscriptionManager().getSubscriberSubscription().putAll(subscriberSubscription);

			syncedEntities.add("Subscriber Subscriptions");

		} catch (SQLException e) {
			e.printStackTrace();
			zooFacade.getPanelDialogMessage()
					.showDBErrorDialog("Failed to sync subscribers subscription from database.");
		}
	}

}
