package zooPackage.Management.Validator;

import zooPackage.Entities.Promotions.Promotion;
import zooPackage.Entities.Users.Visitor;
import zooPackage.Exception.InvalidUserNameException;
import zooPackage.Management.ZooFacade;
import zooPackage.Management.Utils.PromotionUtils;

public class PromotionValidator {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static PromotionValidator instance;

	private PromotionValidator() {
	}

	public static synchronized PromotionValidator getInstance() {
		if (instance == null) {
			instance = new PromotionValidator();
		}
		return instance;
	}

	public boolean validatePromotionFields(String description, String percentage, int flag) {
		try {
			for (Promotion promo : zooFacade.getPromotionManager().getPromotions()) {
				if (promo.getDescription().equals(description) && flag != 2) {
					zooFacade.getPanelFields().setDescriptionField("");
					zooFacade.getPanelFields().setPercentageField("");
					throw new InvalidUserNameException("This promotion (description) already exists, try again.");
				}
			}
			if (description.isEmpty() || percentage.isEmpty()) {
				zooFacade.getPanelDialogMessage().showWarningDialog(
						"Hi user!\nIn order to finish the promotion adding, you have to enter new promotion description and her percentage\nFor example: \"Holiday Sales - 45%\" .");
				return false;
			}
			return validatePercentage(percentage);
		} catch (InvalidUserNameException e) {
			zooFacade.getPanelDialogMessage().showErrorDialog(e.getMessage());
			return false;
		}
	}

	public boolean validatePercentage(String percentage) {
		try {
			int value = Integer.parseInt(percentage);
			if (value < 1 || value > 100) {
				zooFacade.getPanelDialogMessage().showWarningDialog("Invalid percentage (1 - 100)%");
				zooFacade.getPanelFields().setPercentageField("");
				return false;
			}
			return true;
		} catch (NumberFormatException e) {
			zooFacade.getPanelDialogMessage()
					.showWarningDialog("Invalid input. Please enter a numerical value (1 - 100)%");
			zooFacade.getPanelFields().setPercentageField("");
			return false;
		}
	}

	public void checkAndDisplayPromotionsForVisitor(Visitor visitor) {

		boolean isSubscribed = zooFacade.getSubscriptionValidation()
				.isVisitorSubscribed(zooFacade.getPanelFields().getIdField().getText());
		zooFacade.getPromotionUtils();
		boolean hasPendingPromotions = PromotionUtils.hasPendingPromotionsForSubscriber(visitor.getID());

		if (isSubscribed && hasPendingPromotions) {
			zooFacade.getPromotionDisplay().displayPromotionList();
			zooFacade.getSubscriptionManager().removeSubscriberFromNotifyList(visitor.getID());
		}
	}

	// To notify subscribers that we have new promotions
	public void checkAndDisplayUpdatedPromotionsForVisitor(Visitor visitor) {
		if (zooFacade.getPromotionUtils().hasPendingUpdatesForSubscriber(visitor.getID())) {
			zooFacade.getSubscriptionManager().notifySubscriber(visitor.getID());
			zooFacade.getSubscriptionManager().removeSubscriberFromUpdateNotifyList(visitor.getID());
			if (zooFacade.getSubscriptionManager().getSubscribersToNotifyOnUpdates().isEmpty()) {
				zooFacade.getPromotionManager().getUpdatedPromotions().clear();
			}
		}
	}

	public boolean checkIfSubscriberListEmpty() {
		if (zooFacade.getSubscriptionManager().getSubscribers().isEmpty()) {
			zooFacade.getPanelDialogMessage()
					.showInformationDialog("There are currently no subscribers to the mailing list.");
			return true;
		}
		return false;
	}

	public boolean checkIfPromotionListEmpty() {
		if (zooFacade.getPromotionManager().getPromotions().isEmpty()) {
			zooFacade.getPanelDialogMessage().showInformationDialog("There are currently no promotions.");
			return true;
		}
		return false;
	}

}
