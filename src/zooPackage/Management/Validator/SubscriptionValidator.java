package zooPackage.Management.Validator;

import java.util.HashMap;
import zooPackage.Entities.Promotions.Promotion;
import zooPackage.Entities.Subscriptions.Subscription;
import zooPackage.Entities.Users.Visitor;
import zooPackage.Enum.SubscriptionType;
import zooPackage.Management.ZooFacade;

public class SubscriptionValidator {
	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static SubscriptionValidator instance;

	private SubscriptionValidator() {
	}

	public static SubscriptionValidator getInstance() {
		if (instance == null) {
			instance = new SubscriptionValidator();
		}
		return instance;
	}

	public boolean isVisitorSubscribed(String visitorID) {
		for (Visitor subscriber : zooFacade.getSubscriptionManager().getSubscribers()) {
			if (("" + subscriber.getID()).equals(visitorID)) {
				return true;
			}
		}
		return false;
	}

	public String checkWhatSubscriptionVisitorSubscribed(int visitorID) {
		HashMap<Integer, Subscription> archive = zooFacade.getSubscriptionManager().getSubscriberSubscription();

		if (archive.containsKey(visitorID)) {
			Subscription sub = archive.get(visitorID);
			return sub.getType().getDisplayName();
		}

		return null;
	}



	public boolean validateVisitorSubscription(String visitorID) {
		Visitor visitor = zooFacade.getVisitorManager().findVisitorByID(visitorID);
		int visitor_id = visitor.getVisitor_id();

		if (zooFacade.getSubscriptionManager().getSubscriberSubscription().containsKey(visitor_id)) {
			String subType = checkWhatSubscriptionVisitorSubscribed(visitor_id);
			if (!subType.equals("" + SubscriptionType.PROMOTION))
				zooFacade.getPanelDialogMessage()
						.showInformationDialog("Welcome " + visitor.getFirstName() + " " + visitor.getLastName());
			return true;
		}
		return false;
	}

	public boolean validateSubscriberPromotion(String visitorID, int promotion_id) {
		Visitor visitor = zooFacade.getVisitorManager().findVisitorByID(visitorID);
		int visitor_id = visitor.getVisitor_id();

		if (zooFacade.getSubscriptionManager().getSubscriberPromotions().containsKey(visitor_id)) {
			for (Promotion promo : zooFacade.getSubscriptionManager().getSubscriberPromotions().get(visitor_id)) {
				if (promo.getPromotionID() == promotion_id) {
					zooFacade.getPanelDialogMessage()
							.showInformationDialog("Dear " + visitor.getFirstName() + " " + visitor.getLastName()
									+ ", you've already used this promotion\n"
									+ "You will be notified when there are new discounts.");
				}
			}
			return true;
		}
		return false;
	}
}
