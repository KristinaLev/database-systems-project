package zooPackage.Management.Utils;

import zooPackage.Entities.Subscriptions.Subscription;
import zooPackage.Entities.Users.Visitor;
import zooPackage.Management.ZooFacade;

public class SubscriptionUtils {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static SubscriptionUtils instance;

	private SubscriptionUtils() {
	}

	public static SubscriptionUtils getInstance() {
		if (instance == null) {
			instance = new SubscriptionUtils();
		}
		return instance;
	}

	public StringBuilder subscribersStringBuilder() {
		StringBuilder subscriberInfo = new StringBuilder();
		int count = 0;

		for (Visitor subscriber : zooFacade.getSubscriptionManager().getSubscribers()) {
			subscriberInfo.append("N. " + subscriber.getVisitor_id() + " Name: " + subscriber.getFirstName() + " "
					+ subscriber.getLastName() + "  |  ").append("ID: " + subscriber.getID()).append("\n");
			count++;
		}
		subscriberInfo.append("\nTotal subscribers in the zoo: " + count + "\n");
		return subscriberInfo;
	}

	public StringBuilder subscriptionStringBuilder() {
		StringBuilder subscriptionInfo = new StringBuilder();
		int count = 0;

		for (Subscription subscription : zooFacade.getSubscriptionManager().getSubscriptions()) {
			subscriptionInfo.append("N. " + subscription.getSubscription_id() + " Type: " + subscription.getType()
					+ "  |  Name: " + subscription.getName()).append("\n");
			count++;
		}
		subscriptionInfo.append("\nTotal subscription in the zoo: " + count + "\n");
		return subscriptionInfo;
	}

}
