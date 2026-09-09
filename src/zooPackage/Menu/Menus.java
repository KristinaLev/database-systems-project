package zooPackage.Menu;

import javax.swing.JOptionPane;

import zooPackage.Entities.Promotions.Promotion;
import zooPackage.Management.ZooFacade;

public class Menus {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static Menus instance;

	private Menus() {
	}

	public static Menus getInstance() {
		if (instance == null) {
			instance = new Menus();
		}
		return instance;
	}

	public void mainMenu() {
		while (true) {

			String menuPrompt = "Please select one of the choices below:\n\n1. Login as Employee\n2. Login as Visitor\n(Only if you have previously bought an entrance ticket)\n\n0. Exit\n\nEnter your choice:";
			String menuTitle = zooFacade.getAnimalManager().getZooName() + " Menu";
			int userChoice = zooFacade.getLoginManager().showPromtAndGetChoice(menuPrompt, menuTitle, 0, 2);

			switch (userChoice) {
			case 1:
				boolean flag = employeeAuthenticationMenu();
				if (flag) {
					employeeMenu();
				}
				break;
			case 2:
				boolean flag1 = zooFacade.getLoginManager().loginVisitorCase();
				zooFacade.getPanelFields().setIdField("");
				if (flag1) {
					visitorMenu();
				}
				break;
			case 0:
				int confirmExit = zooFacade.getPanelDialogMessage().showConfirmDialog("Are you sure you want to exit?");
				if (confirmExit == 0) {
					zooFacade.getPanelDialogMessage().showInformationDialog("Goodbye\nCome back soon!");
					return;
				}
				break;
			default:
				zooFacade.getPanelDialogMessage()
						.showErrorDialog("Invalid choice!\n\nPlease enter a number between 0 and 2.");
			}
		}
	}

	public void employeeMenu() {
		while (true) {

			String menuPrompt = "Please select one of the choices below:\n\n1. Display Zoo Details\n"
					+ "2. Display Animals\n3. Add Animal\n4. Add one year\n5. Feed Animals"
					+ "\n6. Listening to Animals\n7. Visitor Management System"
					+ "\n8. Promotion Management System\n9. Subscription Management\n\n0. Exit\n\nEnter your choice:";
			String menuTitle = zooFacade.getAnimalManager().getZooName() + " Menu";
			int userChoice = zooFacade.getLoginManager().showPromtAndGetChoice(menuPrompt, menuTitle, 0, 9);

			switch (userChoice) {
			case 1:
				zooFacade.getAnimalDisplay().printZooDetails();
				break;
			case 2:
				zooFacade.getAnimalDisplay().displayAnimalMenu();
				break;
			case 3:
				zooFacade.getAnimalManagement().addAnimalMenu();
				break;
			case 4:
				zooFacade.getAnimalManager().ageOneYear();
				zooFacade.getAnimalManagement().deathNote();
				zooFacade.getPanelDialogMessage().showInformationDialog("Hey friend !\r\n"
						+ "This action made all the animals in the zoo grow a year!\r\n"
						+ "Congratulations to our amazing animals!\r\n" + "\r\n"
						+ "Just note that this action causes the mood {happines} of the animals to drop\nIt's worth feeding them to lift the mood! :)");
				break;
			case 5:
				zooFacade.getAnimalDisplay().displayFoodConsuption();
				break;
			case 6:
				zooFacade.getAnimalDisplay().displayAnimalNoise();
				break;
			case 7:
				ticketMenu();
				break;
			case 8:
				promotionMenu();
				break;
			case 9:
				subscriptionMenu();
				break;
			case 0:
				zooFacade.getPanelDialogMessage().showInformationDialog("Exiting Employee Menu");
				return;
			default:
				zooFacade.getPanelDialogMessage()
						.showErrorDialog("Invalid choice!\n\nPlease enter a number between 0 and 9.");
			}
		}
	}

	public void visitorMenu() {
		while (true) {

			String menuPrompt = "Please select one of the choices below:\n\n1. Display Zoo Details\n2. Display Animals"
					+ "\n3. Feed Animals\n4. Listening to Animals\n\n0. Exit\n\nEnter your choice:";
			String menuTitle = zooFacade.getAnimalManager().getZooName() + " Menu";
			int userChoice = zooFacade.getLoginManager().showPromtAndGetChoice(menuPrompt, menuTitle, 0, 4);

			switch (userChoice) {
			case 1:
				zooFacade.getAnimalDisplay().printZooDetails();
				break;
			case 2:
				zooFacade.getAnimalDisplay().displayAnimalMenu();
				break;
			case 3:
				JOptionPane.showMessageDialog(null, zooFacade.getAnimalManager().feedAllAnimals(),
						"Daily Food Consumption", JOptionPane.INFORMATION_MESSAGE);
				zooFacade.getAnimalManager().setDefaultHappinessAfterFeeding();
				break;
			case 4:
				zooFacade.getAnimalDisplay().displayAnimalNoise();
				break;
			case 0:
				zooFacade.getPanelDialogMessage().showInformationDialog("Exiting Visitor Menu");
				return;
			default:
				zooFacade.getPanelDialogMessage()
						.showErrorDialog("Invalid choice!\n\nPlease enter a number between 1 and 4.");
			}
		}
	}

	public boolean employeeAuthenticationMenu() {
		while (true) {

			String menuPrompt = "Select the login type:\n\n1. Log-in\n2. Registrate"
					+ "\n\n0. Exit\n\nEnter your choice:";
			String menuTitle = "Employee Authentication Menu";
			int userChoice = zooFacade.getLoginManager().showPromtAndGetChoice(menuPrompt, menuTitle, 0, 2);

			switch (userChoice) {
			case 1:
				boolean flag = zooFacade.getLoginManager().loginEmployeeCase();
				if (flag) {
					return true;
				}
				return false;
			case 2:
				boolean flag1 = zooFacade.getLoginManager().registrateEmployeeCase();
				if (!flag1) {
					return false;
				}
			case 0:
				zooFacade.getPanelDialogMessage().showInformationDialog("Back to Authentication Menu");
				return false;
			default:
				zooFacade.getPanelDialogMessage().showErrorDialog("Invalid selection.");
				break;
			}
		}
	}

	public void ticketMenu() {
		while (true) {

			String menuPrompt = "Please select one of the choices below:\n\n1. Buy a ticket\n2. Cancel a ticket"
					+ "\n3. Ticket search\n4. Print tickets purchase history data"
					+ "\n5. Print used tickets data\n\n0. Return to Employee Menu\n\nEnter your choice:";
			String menuTitle = "Ticket Management System Menu";
			int userChoice = zooFacade.getLoginManager().showPromtAndGetChoice(menuPrompt, menuTitle, 0, 5);

			switch (userChoice) {
			case 1:
				zooFacade.getTicketManager().buyTicket();
				break;
			case 2:
				zooFacade.getTicketManager().cancelTicket();
				break;
			case 3:
				printActiveTicketsMenu();
				break;
			case 4:
				printTicketsHistoryPurchasedMenu();
				break;
			case 5:
				printEntranceHistoryMenu();
				break;
			case 0:
				zooFacade.getPanelDialogMessage().showInformationDialog("Back to Main Employee Menu");
				return;
			default:
				zooFacade.getPanelDialogMessage()
						.showErrorDialog("Invalid Input!\n\nPlease enter a number between 0 and 5\n\n");
			}
		}
	}

	public void promotionMenu() {
		while (true) {

			String menuPrompt = "1. Add new promotion\n2. Update promotion\n3. Display Promotions List\n"
					+ "4. Unsubscribe from Promotions\n\n" + "0. Return to Main Menu\n\nEnter your choice:";
			String menuTitle = "Promotion Management System Menu";
			int userChoice = zooFacade.getLoginManager().showPromtAndGetChoice(menuPrompt, menuTitle, 0, 4);

			switch (userChoice) {
			case 1:
				zooFacade.getPromotionManager().createNewPromotion();
				break;
			case 2:
				promotionUpdateMenu();
				break;
			case 3:
				zooFacade.getPromotionDisplay().displayPromotionList();
				break;
			// case 4:
			// zooFacade.getPromotionManager().sendPromotions();
			// break;
			case 4:
				zooFacade.getPromotionManager().unsubscribeFromPromotion();
				break;
			case 0:
				zooFacade.getPanelDialogMessage().showInformationDialog("Back to Main Menu");
				return;
			default:
				zooFacade.getPanelDialogMessage()
						.showErrorDialog("Invalid Input!\n\nPlease enter a number between 0 and 4\n\n");
			}
		}
	}

	public void subscriptionMenu() {
		while (true) {

			String menuPrompt = "1. Display Subscription List\n" + "2. Unsubscribe\n"
					+ "3. Display Subscribers List\n\n0. Return to Main Menu\n\nEnter your choice:";

			String menuTitle = "Subscription Management System Menu";
			int userChoice = zooFacade.getLoginManager().showPromtAndGetChoice(menuPrompt, menuTitle, 0, 3);

			switch (userChoice) {
			case 1:
				zooFacade.getSubscriptionDisplay().dispalySubcribtions();
				break;
			case 2:
				zooFacade.getSubscriptionManager().unsubscribe();
				break;
			case 3:
				zooFacade.getSubscriptionDisplay().dispalySubscribers();
				break;
			case 0:
				zooFacade.getPanelDialogMessage().showInformationDialog("Back to Main Menu");
				return;
			default:
				zooFacade.getPanelDialogMessage()
						.showErrorDialog("Invalid Input!\n\nPlease enter a number between 0 and 3\n\n");
			}
		}
	}

	public void promotionUpdateMenu() {

		if (zooFacade.getPromotionValidation().checkIfPromotionListEmpty()) {
			return;
		}

		Promotion promotion = zooFacade.getPromotionSelection().selectPromotion();
		if (promotion == null) {
			return;
		}

		while (true) {

			String menuPrompt = "Choose what you want to update:\n\n" + "1. Update the description\n"
					+ "2. Update the percentage\n" + "3. Update all details"
					+ "\n\n0. Return to Promotion Menu\n\nEnter your choice:";
			String menuTitle = "Promotion Update Menu";
			int userChoice = zooFacade.getLoginManager().showPromtAndGetChoice(menuPrompt, menuTitle, 0, 3);

			switch (userChoice) {
			case 1: // Update the description
				zooFacade.getPromotionUtils().updatePromotionPanel(promotion, userChoice);
				return;
			case 2: // Update the percentage
				zooFacade.getPromotionUtils().updatePromotionPanel(promotion, userChoice);
				return;
			case 3: // Update all details
				zooFacade.getPromotionUtils().updatePromotionPanel(promotion, userChoice);
				return;
			case 0:
				zooFacade.getPanelDialogMessage().showInformationDialog("Back to Promotion Menu");
				return;
			default:
				zooFacade.getPanelDialogMessage()
						.showErrorDialog("Invalid Input!\n\nPlease enter a number between 0 and 3\n\n");
			}
		}
	}

	public void printActiveTicketsMenu() {
		while (true) {

			String menuPrompt = "How would you like to search the ticket?\n\n" + "1. By ID\n"
					+ "2. By purchase date\n3. Print all active tickets\n\n0. Return to Visitor Menu"
					+ "\n\nEnter yout choise:";
			String menuTitle = "Purchase History Menu";
			int userChoice = zooFacade.getLoginManager().showPromtAndGetChoice(menuPrompt, menuTitle, 0, 3);

			switch (userChoice) {
			case 1:
				zooFacade.getTicketDisplay().displayActiveTicketsByID();
				break;
			case 2:
				zooFacade.getTicketDisplay().displayActiveTicketsByReleaseDate();
				break;
			case 3:
				zooFacade.getTicketDisplay().displayAllActiveTickets();
				break;
			case 0:
				zooFacade.getPanelDialogMessage().showInformationDialog("Back to Ticket Menu");
				return;
			default:
				zooFacade.getPanelDialogMessage()
						.showErrorDialog("Invalid Input!\n\nPlease enter a number between 0 and 3\n\n");
			}
		}
	}

	public void printTicketsHistoryPurchasedMenu() {
		while (true) {

			String menuPrompt = "How would you like to print the ticket purchase history data?\n\n" + "1. By ID\n"
					+ "2. By purchase date\n3. Print all purchased tickets\n\n0. Return to Visitor Menu"
					+ "\n\nEnter yout choise:";
			String menuTitle = "Purchase History Menu";
			int userChoice = zooFacade.getLoginManager().showPromtAndGetChoice(menuPrompt, menuTitle, 0, 3);

			switch (userChoice) {
			case 1:
				zooFacade.getTicketDisplay().displayTicketPurchaseHistoryByID();
				break;
			case 2:
				zooFacade.getTicketDisplay().displayTicketPurchaseHistoryByReleaseDate();
				break;
			case 3:
				zooFacade.getTicketDisplay().displayAllTicketPurchaseHistory();
				break;
			case 0:
				zooFacade.getPanelDialogMessage().showInformationDialog("Back to Ticket Menu");
				return;
			default:
				zooFacade.getPanelDialogMessage()
						.showErrorDialog("Invalid Input!\n\nPlease enter a number between 0 and 3\n\n");
			}
		}
	}

	public void printEntranceHistoryMenu() {
		while (true) {

			String menuPrompt = "How would you like to print the entrances data?\n\n" + "1. By ID\n"
					+ "2. By entry date\n3. Print all used tickets\n\n0. Return to Visitor Menu"
					+ "\n\nEnter yout choise:";
			String menuTitle = "Entrance History Menu";
			int userChoice = zooFacade.getLoginManager().showPromtAndGetChoice(menuPrompt, menuTitle, 0, 3);

			switch (userChoice) {
			case 1:
				zooFacade.getTicketDisplay().displayUsedTicketByID();
				break;
			case 2:
				zooFacade.getTicketDisplay().displayUsedTicketByEntryDate();
				break;
			case 3:
				zooFacade.getTicketDisplay().displayAllUsedTickets();
				break;
			case 0:
				zooFacade.getPanelDialogMessage().showInformationDialog("Back to Ticket Menu");
				return;
			default:
				zooFacade.getPanelDialogMessage()
						.showErrorDialog("Invalid Input!\n\nPlease enter a number between 0 and 3\n\n");
			}
		}
	}
}
