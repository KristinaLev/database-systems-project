package zooPackage.Management.Display;

import zooPackage.Management.ZooFacade;

public class SubscriptionDisplay {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static SubscriptionDisplay instance;

	private SubscriptionDisplay() {
	}

	public static SubscriptionDisplay getInstance() {
		if (instance == null) {
			instance = new SubscriptionDisplay();
		}
		return instance;
	}

	public void dispalySubcribtions() {
		if (zooFacade.getSubscriptionManager().getSubscriptions().isEmpty()) {
			return;
		}
		String promt = zooFacade.getSubscriptionUtils().subscriptionStringBuilder().toString();
		zooFacade.getPanelDialogMessage().showListDialog(promt, "Subcribtions List");
	}

	public void dispalySubscribers() {
		if (zooFacade.getPromotionValidation().checkIfSubscriberListEmpty()) {
			return;
		}
		String promt = zooFacade.getSubscriptionUtils().subscribersStringBuilder().toString();
		zooFacade.getPanelDialogMessage().showListDialog(promt, "Subscribers List");
	}

}
