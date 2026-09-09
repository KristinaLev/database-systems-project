package zooPackage.Management.Selection;

import java.util.EnumSet;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import zooPackage.Entities.Promotions.Promotion;
import zooPackage.Entities.Users.Visitor;
import zooPackage.Enum.SubscriptionType;
import zooPackage.Enum.TicketType;
import zooPackage.Management.ZooFacade;

public class SubscriptionSelection {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static SubscriptionSelection instance;

	private SubscriptionSelection() {
	}

	public static SubscriptionSelection getInstance() {
		if (instance == null) {
			instance = new SubscriptionSelection();
		}
		return instance;
	}

	public String selectSubscruptionType(String[] categories) {

		JComboBox<String> categoryComboBox = new JComboBox<>(categories);
		JPanel panel = zooFacade.getPanelDisplay().createTicketTypeCategoryBoxPanel(categoryComboBox);

		while (true) {
			int result = zooFacade.getPanelDialogMessage().showConfirmDialog(panel, "Subscription Category Menu:");
			if (result != 0) {
				zooFacade.getPanelDialogMessage().showInformationDialog("Subscription canceled.");
				return null;
			}
			return (String) categoryComboBox.getSelectedItem();
		}
	}

	public TicketType getSubscriptionTicketType(String category, String selectedValue) {
		for (TicketType ticket : zooFacade.getTicketManager().getTicketTypes()) {
			zooFacade.getTicketUtils();
			double price = ticket.getAnnualPrice();
			String ticketTypeString = "Subscription: " + ticket.getTypeName() + " - $" + price;
			if (ticketTypeString.equals(selectedValue)) {
				return ticket;
			}
		}
		return null;
	}

	public static String[] subscriptionArrayBuilder(TicketType first, TicketType last) {

		EnumSet<TicketType> ticketTypes = EnumSet.range(first, last);
		String[] subscriptionInfo = new String[ticketTypes.size()];

		int count = 0;

		for (TicketType ticketType : ticketTypes) {
			double price = ticketType.getAnnualPrice();
			subscriptionInfo[count] = "Subscription: " + ticketType.getTypeName() + " - $" + price;
			count++;
		}

		return subscriptionInfo;
	}

	public static String[] subscriptionTypeArrayBuilder(SubscriptionType first, SubscriptionType last) {
		EnumSet<SubscriptionType> subscriptionTypes = EnumSet.range(first, last);
		String[] subscriptionInfo = new String[subscriptionTypes.size()];

		int count = 0;

		for (SubscriptionType type : subscriptionTypes) {
			subscriptionInfo[count] = type.getDisplayName();
			count++;
		}

		return subscriptionInfo;
	}

	public boolean selectAnnualSubscription(String subscriptionType, TicketType first, TicketType last,
			double discountPercentage, int visitorID, Promotion promotion) {

		String[] subscriptionInfo = subscriptionArrayBuilder(first, last);

		JList<String> subscriptionTypesJList = new JList<>(subscriptionInfo);
		subscriptionTypesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel panel = zooFacade.getPanelDisplay().createTicketTypeListPanel(subscriptionTypesJList,
				"Choose which subscription you want to buy:");

		while (true) {
			int result = zooFacade.getPanelDialogMessage().showConfirmDialog(panel, "Subscriptions Menu");
			if (result != 0) {
				zooFacade.getPanelDialogMessage().showInformationDialog("Subscription purchase was cancelled");
				return false;
			}

			if (subscriptionTypesJList.getSelectedValuesList().isEmpty()) {
				zooFacade.getPanelDialogMessage().showWarningDialog(
						"Hi friend!\nYou must select one subscription from the list in order to purchase one.");
				continue;
			}

			TicketType selectedTicketType = getSubscriptionTicketType(subscriptionType,
					subscriptionTypesJList.getSelectedValue());
			if (selectedTicketType == null) {
				zooFacade.getPanelDialogMessage().showErrorDialog("Selected subscription type not found.");
				return false;
			}
			
			for (SubscriptionType type : SubscriptionType.values())
				if (type.getDisplayName().equals(subscriptionType))
					zooFacade.getSubscriptionManager().createNewSubscription(selectedTicketType, type, visitorID);
			return true;
		}
	}

	public boolean selectPromotionSubscription(String subscriptionType, TicketType first, TicketType last,
			double discountPercentage, int visitorID, Promotion promotion) {

		String[] subscriptionInfo = subscriptionArrayBuilder(first, last);

		JList<String> subscriptionTypesJList = new JList<>(subscriptionInfo);
		subscriptionTypesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel panel = zooFacade.getPanelDisplay().createTicketTypeListPanel(subscriptionTypesJList,
				"Choose which subscription you want to buy:");

		while (true) {
			int result = zooFacade.getPanelDialogMessage().showConfirmDialog(panel, "Subscriptions Menu");
			if (result != 0) {
				zooFacade.getPanelDialogMessage().showInformationDialog("Subscription purchase was cancelled");
				return false;
			}

			if (subscriptionTypesJList.getSelectedValuesList().isEmpty()) {
				zooFacade.getPanelDialogMessage().showWarningDialog(
						"Hi friend!\nYou must select one subscription from the list in order to purchase one.");
				continue;
			}

			TicketType selectedTicketType = getSubscriptionTicketType(subscriptionType,
					subscriptionTypesJList.getSelectedValue());
			if (selectedTicketType == null) {
				zooFacade.getPanelDialogMessage().showErrorDialog("Selected subscription type not found.");
				return false;
			}
			
			for (SubscriptionType type : SubscriptionType.values())
				if (type.getDisplayName().equals(subscriptionType))
					zooFacade.getSubscriptionManager().createNewSubscription(selectedTicketType, type, visitorID);
			return true;
		}
	}

	
	public void handleAnnualSubscriptionSelection(Visitor newVisitor) {

		String[] options = subscriptionTypeArrayBuilder(SubscriptionType.SINGLE, SubscriptionType.FAMILY);

		String selected = zooFacade.getSubscriptionSelection().selectSubscruptionType(options);
		if (selected == null) {
			return;
		}

		switch (selected) {
		case "Single subscription":
			zooFacade.getSubscriptionSelection().selectAnnualSubscription(selected, TicketType.ADULT,
					TicketType.DISABLED, 0, newVisitor.getVisitor_id(), null);
			break;
		case "Couple subscription":
			zooFacade.getSubscriptionSelection().selectAnnualSubscription(selected, TicketType.COUPLE,
					TicketType.COUPLE, 0, newVisitor.getVisitor_id(), null);
			break;
		case "Parent subscription":
			zooFacade.getSubscriptionSelection().selectAnnualSubscription(selected, TicketType.PARENT_PLUS_ONE,
					TicketType.PARENT_PLUS_SIX_OR_MORE, 0, newVisitor.getVisitor_id(), null);
			zooFacade.getSubscriptionManager().addSubscriber(newVisitor);
			break;
		case "Family subscription":
			zooFacade.getSubscriptionSelection().selectAnnualSubscription(selected, TicketType.COUPLE_PLUS_ONE,
					TicketType.COUPLE_PLUS_SIX_OR_MORE, 0, newVisitor.getVisitor_id(), null);
			break;
		default:
			zooFacade.getPanelDialogMessage().showErrorDialog("Invalid sub-category choice!");
		}
	}


}
