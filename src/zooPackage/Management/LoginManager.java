package zooPackage.Management;

import javax.swing.JPanel;
import zooPackage.Entities.Tickets.Ticket;
import zooPackage.Entities.Users.Visitor;
import zooPackage.Enum.SubscriptionType;
import zooPackage.Exception.InvalidIDException;
import zooPackage.Exception.InvalidPasswordException;
import zooPackage.Interface.LoginManager_Interface;

public class LoginManager implements LoginManager_Interface {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static LoginManager instance;

	private LoginManager() {
	}

	public static LoginManager getInstance() {
		if (instance == null) {
			instance = new LoginManager();
		}
		return instance;
	}

	@Override
	public boolean loginEmployeeCase() {
		JPanel loginInfoPanel = zooFacade.getPanelDisplay().createEmployeeLoginInfoPanel();

		while (true) {
			int result = zooFacade.getPanelDialogMessage().showConfirmDialog(loginInfoPanel,
					"Enter the login details:");
			if (result != 0) {
				zooFacade.getPanelDialogMessage().showInformationDialog("Login canceled.");
				return false;
			}

			if (!zooFacade.getPersonValidation().checkIfEmployeeLoginFieldsEmpty()) {
				try {
					zooFacade.getPasswordManager().employeeLoginCheck(
							zooFacade.getPanelFields().getUserNameField().getText(),
							zooFacade.getPanelFields().getPasswordField().getText());
					zooFacade.getPanelDialogMessage().showInformationDialog("Login succeeded");
					zooFacade.getPanelFields().setUserNameField("");
					zooFacade.getPanelFields().setPasswordField("");
					return true;
				} catch (InvalidPasswordException e) {
					handleIInvalidPasswordException(e);
				}
			} else
				zooFacade.getPanelDialogMessage()
						.showWarningDialog("Hi user!\nIn order to finish the log-in you have to enter all the data");
		}
	}

	public String loginVisitor() {
		JPanel loginInfoPanel = zooFacade.getPanelDisplay().createVisitorLoginInfoPanel();
		while (true) {
			int result = zooFacade.getPanelDialogMessage().showConfirmDialog(loginInfoPanel, "Enter your visitor ID:");
			if (result != 0) {
				zooFacade.getPanelDialogMessage().showInformationDialog("Login canceled.");
				return null;
			}
			try {
				if (zooFacade.getPersonValidation().checkIfVisitorLoginFieldsEmpty()) {
					zooFacade.getPanelDialogMessage().showWarningDialog(
							"Hi user!\nIn order to finish the log-in you have to enter all the data");
					continue;
				}
				return zooFacade.getPanelFields().getIdField().getText();
			} catch (NumberFormatException e) {
				handleNumberFormatException();
			}
		}
	}

	@Override
	public boolean loginVisitorCase() {
		JPanel loginInfoPanel = zooFacade.getPanelDisplay().createVisitorLoginInfoPanel();

		while (true) {
			int result = zooFacade.getPanelDialogMessage().showConfirmDialog(loginInfoPanel, "Enter your visitor ID:");
			if (result != 0) {
				zooFacade.getPanelDialogMessage().showInformationDialog("Login canceled.");
				return false;
			}

			String visitorID = zooFacade.getPanelFields().getIdField().getText();
			try {
				if (zooFacade.getPersonValidation().checkIfVisitorLoginFieldsEmpty()) {
					zooFacade.getPanelDialogMessage().showWarningDialog(
							"Hi user!\nIn order to finish the log-in you have to enter all the data");
					continue;
				}
				Visitor visitor = zooFacade.getVisitorManager().findVisitorByID(visitorID);
				try {
					visitorLoginCheck(visitorID);
					zooFacade.getPanelDialogMessage().showInformationDialog("Login succeeded");

					boolean haveSubscription = zooFacade.getSubscriptionValidation()
							.validateVisitorSubscription(visitorID);
					if (haveSubscription) {
						String type = zooFacade.getSubscriptionValidation()
								.checkWhatSubscriptionVisitorSubscribed(visitor.getVisitor_id());
						if (type.equals("" + SubscriptionType.PROMOTION.getDisplayName())) {
							Ticket ticket = zooFacade.getTicketSelection().selectEntryTicket(visitorID);
							if (ticket == null) {
								return false;
							}
							zooFacade.getTicketManager().updateTicketInfo(ticket);
							zooFacade.getPromotionValidation().checkAndDisplayPromotionsForVisitor(visitor);
							zooFacade.getPromotionValidation().checkAndDisplayUpdatedPromotionsForVisitor(visitor);
							zooFacade.getPanelDialogMessage().showInformationDialog("Entering the park.");
							return true;
						}
						return true;
					}

					boolean haveTicket = zooFacade.getTicketValidationUtils().validateVisitorTickets(visitorID);
					if (!haveTicket) {
						return false;
					}

					Ticket ticket = zooFacade.getTicketSelection().selectEntryTicket(visitorID);
					if (ticket == null) {
						return false;
					}

					zooFacade.getTicketManager().updateTicketInfo(ticket);
					zooFacade.getPromotionValidation().checkAndDisplayPromotionsForVisitor(visitor);
					zooFacade.getPromotionValidation().checkAndDisplayUpdatedPromotionsForVisitor(visitor);
					zooFacade.getPanelDialogMessage().showInformationDialog("Entering the park.");
					return true;

				} catch (InvalidIDException e) {
					handleInvalidIDException(e);
				}
			} catch (NumberFormatException e) {
				handleNumberFormatException();
			}
		}
	}

	@Override
	public boolean registrateEmployeeCase() {
		if (zooFacade.getEmployeeManager().createEmployee()) {
			zooFacade.getPanelDialogMessage()
					.showInformationDialog("Registration complete! \nYou can now try to log-in.");
			zooFacade.getPanelFields().clearPersonalInfoPanel();
			return true;
		}
		zooFacade.getPanelFields().clearPersonalInfoPanel();
		return false;
	}

	@Override
	public Visitor authenticateVisitorID(String visitorID) {
		long id = Long.parseLong(visitorID);
		for (Visitor visitor : zooFacade.getVisitorManager().getVisitors()) {
			if (visitor.getID() == id) {
				return visitor;
			}
		}
		return null;
	}

	@Override
	public void visitorLoginCheck(String visitorID) throws InvalidIDException {
		Visitor loggedIn = authenticateVisitorID(visitorID);
		if (loggedIn == null) {
			throw new InvalidIDException(
					"Invalid Visitor ID.\n\nWhen purchasing the ticket you register in the system with your ID number.\nIf you are not in the system please go to buy a ticket through one of our employees.");
		}
	}

	public Visitor subscriberLoginCheck(String subscriberID) {
		for (Visitor subscriber : zooFacade.getSubscriptionManager().getSubscribers()) {
			if (subscriberID.equals("" + subscriber.getID())) {
				return subscriber;
			}
		}
		return null;
	}

	@Override
	public int showPromtAndGetChoice(String menuPrompt, String menuTitle, int minChoice, int maxChoice) {
		while (true) {
			String input = zooFacade.getPanelDialogMessage().showInputDialog(menuPrompt, menuTitle);
			if (input == null) {
				return 0;
			}
			try {
				int userChoice = Integer.parseInt(input);
				if (userChoice >= minChoice && userChoice <= maxChoice) {
					return userChoice;
				} else {
					zooFacade.getPanelDialogMessage().showErrorDialog("Invalid Input!\n\nPlease enter a number between "
							+ minChoice + " and " + maxChoice + ".\n\n");
				}
			} catch (NumberFormatException e) {
				zooFacade.getPanelDialogMessage().showErrorDialog(
						"Invalid Input!\n\nPlease enter a number between " + minChoice + " and " + maxChoice + ".\n\n");
			}
		}
	}

	@Override
	public void handleInvalidIDException(InvalidIDException e) {
		zooFacade.getPanelDialogMessage().showErrorDialog(e.getMessage());
		zooFacade.getPanelFields().setIdField("");
	}

	@Override
	public void handleNumberFormatException() {
		zooFacade.getPanelDialogMessage().showErrorDialog("Invalid ID format. Please enter a valid number ID.");
		zooFacade.getPanelFields().setIdField("");
	}

	@Override
	public void handleIInvalidPasswordException(InvalidPasswordException e) {
		zooFacade.getPanelDialogMessage().showErrorDialog(e.getMessage());
		zooFacade.getPanelFields().setUserNameField("");
		zooFacade.getPanelFields().setPasswordField("");

	}
}
