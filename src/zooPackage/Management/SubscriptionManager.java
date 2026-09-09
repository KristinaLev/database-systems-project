package zooPackage.Management;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import javax.swing.JPanel;
import zooPackage.DB.DBUtil;
import zooPackage.DB.SubscriptionDAO;
import zooPackage.Entities.Promotions.Promotion;
import zooPackage.Entities.Subscriptions.Subscription;
import zooPackage.Entities.Users.Visitor;
import zooPackage.Enum.SubscriptionType;
import zooPackage.Enum.TicketType;

public class SubscriptionManager {

	private static ZooFacade zooFacade = ZooFacade.getInstance();

	private List<Visitor> subscribers;
	private List<Subscription> subscriptions;
	private HashMap<Integer, Subscription> subscriberSubscription = new HashMap<>();
	private HashMap<Integer, List<Promotion>> subscriberPromotions = new HashMap<>();
	private List<Long> subscribersToNotify = new ArrayList<>();
	private List<Long> subscribersToNotifyOnUpdates = new ArrayList<>();
	private EnumSet<SubscriptionType> subscriptionTypes = EnumSet.allOf(SubscriptionType.class);

	private static SubscriptionManager instance;

	private SubscriptionManager() {
		subscribers = new ArrayList<>();
		subscriptions = new ArrayList<>();
	}

	public static synchronized SubscriptionManager getInstance() {
		if (instance == null) {
			instance = new SubscriptionManager();
		}
		return instance;
	}

	public boolean askToSubscribe(long visitorID) {
		int confirmExit = zooFacade.getPanelDialogMessage()
				.showConfirmDialog("Do you want to subscribe to our Annual or Promotion subscription ?");
		if (confirmExit == 0) {
			zooFacade.getPanelDialogMessage().showInformationDialog("Thank you, you will comtinue to the next step.");
			return true;
		}
		return false;
	}

	public void createDefaultSubscriptions() {
		try (Connection conn = DBUtil.getConnection()) {
			SubscriptionDAO subDAO = new SubscriptionDAO(conn);

			Subscription sub1 = new Subscription(0, SubscriptionType.SINGLE, "Adult",
					TicketType.ADULT.getAnnualPrice());
			int sub1_id = subDAO.insertSubscriptionAndGetId(sub1);
			sub1.setSubscription_id(sub1_id);
			addSubscription(sub1);

			Subscription sub2 = new Subscription(0, SubscriptionType.COUPLE, "Couple",
					TicketType.COUPLE.getAnnualPrice());
			int sub2_id = subDAO.insertSubscriptionAndGetId(sub2);
			sub2.setSubscription_id(sub2_id);
			addSubscription(sub2);

			Subscription sub3 = new Subscription(0, SubscriptionType.PARENT, "Parent + 4",
					TicketType.PARENT_PLUS_FOUR.getAnnualPrice());
			int sub3_id = subDAO.insertSubscriptionAndGetId(sub3);
			sub3.setSubscription_id(sub3_id);
			addSubscription(sub3);

			Subscription sub4 = new Subscription(0, SubscriptionType.FAMILY, "Couple + 5",
					TicketType.COUPLE_PLUS_FIVE.getAnnualPrice());
			int sub4_id = subDAO.insertSubscriptionAndGetId(sub4);
			sub4.setSubscription_id(sub4_id);
			addSubscription(sub4);

		} catch (SQLException e) {
			e.printStackTrace();
			zooFacade.getPanelDialogMessage().showDBErrorDialog("Failed to insert default subscriptions.");
		}

	}

	public boolean createNewSubscription(TicketType ticketType, SubscriptionType type, int visitorID) {
		for (Visitor subscriber : getSubscribers()) {
			if (subscriber.getVisitor_id() == visitorID) {
				return false;
			}
		}
		double price = ticketType.getAnnualPrice();

		Subscription newSub = new Subscription(0, type, ticketType.getTypeName(), price);

		Visitor visitor = zooFacade.getVisitorManager().findVisitorByVisitor_ID(visitorID);

		try (Connection conn = DBUtil.getConnection()) {
			SubscriptionDAO subDAO = new SubscriptionDAO(conn);
			int subscription_id = subDAO.insertSubscriptionAndGetId(newSub);

			newSub.setSubscription_id(subscription_id);
			addSubscription(newSub);
			addSubscriptionToMap(visitorID, newSub);
			subDAO.insertSubscriber(visitor, newSub.getSubscription_id());
			zooFacade.getPanelDialogMessage().showInformationDialog("Subscription completed");
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			zooFacade.getPanelDialogMessage().showDBErrorDialog("Failed to insert subscriptions.");
			return false;
		}
	}

	public void addRelationBetweenSubscriberAndPromotion(Visitor subscriber, int promo_id) {
		try (Connection conn = DBUtil.getConnection()) {
			SubscriptionDAO subscriptionDAO = new SubscriptionDAO(conn);
			subscriptionDAO.insertRelationSubscriberAndPromotion(subscriber, promo_id);

		} catch (SQLException e) {
			e.printStackTrace();
			zooFacade.getPanelDialogMessage().showDBErrorDialog("Failed to insert subscriber's promotion.");
		}
	}

	public void addSubscriptionToMap(int visitor_id, Subscription sub) {
		HashMap<Integer, Subscription> hashMap = getSubscriberSubscription();
		if (!hashMap.containsKey(visitor_id)) {
			hashMap.put(visitor_id, sub);
		}
	}

	public boolean addSubscriber(Visitor visitor) {
		if (!subscribers.contains(visitor)) {
			subscribers.add(visitor);
			return true;
		}
		return false;
	}

	public void unsubscribe() {
		JPanel loginInfoPanel = zooFacade.getPanelDisplay().createVisitorLoginInfoPanel();

		if (zooFacade.getPromotionValidation().checkIfSubscriberListEmpty()) {
			return;
		}

		while (true) {
			int result = zooFacade.getPanelDialogMessage().showConfirmDialog(loginInfoPanel, "Enter your visitor ID:");
			if (result != 0) {
				zooFacade.getPanelDialogMessage().showInformationDialog("Unsubscribing canceled.");
				return;
			}
			try {
				String visitor_id = zooFacade.getPanelFields().getIdField().getText();
				if (zooFacade.getPersonValidation().checkIfVisitorLoginFieldsEmpty()) {
					zooFacade.getPanelDialogMessage().showWarningDialog(
							"Hi user!\nIn order to finish the unsubscrbtion you have to enter the ID");
				} else {
					Visitor subscriber = zooFacade.getLoginManager().subscriberLoginCheck(visitor_id);
					if (subscriber == null) {
						zooFacade.getPanelDialogMessage()
								.showInformationDialog("The ID does not exist in the subscribtion list.");
						zooFacade.getPanelFields().setIdField("");
						return;
					}
					zooFacade.getSubscriptionManager().removeSubscriber(subscriber);

					zooFacade.getPanelDialogMessage().showInformationDialog("Unsubscribing completed!");
					zooFacade.getPanelFields().setIdField("");
					return;
				}

			} catch (NumberFormatException e) {
				zooFacade.getPanelDialogMessage().showErrorDialog("Invalid ID format.");
				zooFacade.getPanelFields().setIdField("");
			}
		}
	}

	public void removeSubscriber(Visitor subscriber) {
		try (Connection conn = DBUtil.getConnection()) {
			SubscriptionDAO dao = new SubscriptionDAO(conn);

			dao.deleteSubscriberFromDatabase(subscriber);
			getSubscribers().remove(subscriber);
			removeSubscriberFromNotifyList(subscriber.getID());
			for (Visitor visitor : getSubscribers()) {
				System.out.println(visitor);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addSubscribersToNotify(List<Long> subscribers) {
		getSubscribersToNotify().addAll(subscribers);
	}

	public void removeSubscriberFromNotifyList(Long subscriber) {
		if (!getSubscribersToNotify().isEmpty())
			getSubscribersToNotify().remove(subscriber);
	}

	public void addSubscribersToUpdateNotify(List<Long> subscribers) {
		if (subscribers != null)
			for (Long id : subscribers) {
				zooFacade.getPromotionUtils();
				if (!zooFacade.getPromotionUtils().hasPendingUpdatesForSubscriber(id))
					getSubscribersToNotifyOnUpdates().addAll(subscribers);
			}
	}

	public void notifySubscriber(Long subscriberID) {
		Visitor visitor = zooFacade.getVisitorManager().findVisitorByID("" + subscriberID);
		StringBuilder promotionList = new StringBuilder();
		for (String key : zooFacade.getPromotionManager().getUpdatedPromotionsMap().keySet()) {
			promotionList.append(key).append("\n");
		}
		String message = visitor.notifySubscribers(promotionList.toString());
		zooFacade.getPanelDialogMessage().showInformationDialog(message.toString());
	}

	public void removeSubscriberFromUpdateNotifyList(Long subscriber) {
		getSubscribersToNotifyOnUpdates().remove(subscriber);
	}

	public List<Long> getSubscribersToNotify() {
		return subscribersToNotify;
	}

	public List<Long> getSubscribersToNotifyOnUpdates() {
		return subscribersToNotifyOnUpdates;
	}

	public void addSubscription(Subscription sub) {
		if (!subscriptions.contains(sub))
			subscriptions.add(sub);
	}

	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public List<Visitor> getSubscribers() {
		return subscribers;
	}

	public EnumSet<SubscriptionType> getSubscriptionTypes() {
		return subscriptionTypes;
	}

	public HashMap<Integer, Subscription> getSubscriberSubscription() {
		return subscriberSubscription;
	}

	public HashMap<Integer, List<Promotion>> getSubscriberPromotions() {
		return subscriberPromotions;
	}
}
