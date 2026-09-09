package zooPackage.Management.Utils;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import zooPackage.Entities.Promotions.Promotion;
import zooPackage.Entities.Users.Visitor;
import zooPackage.Management.ZooFacade;

public class PromotionUtils {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static PromotionUtils instance;

	private PromotionUtils() {
	}

	public static PromotionUtils getInstance() {
		if (instance == null) {
			instance = new PromotionUtils();
		}
		return instance;
	}

	public boolean getPromotionInfo(JPanel panel) {
		while (true) {
			int result = zooFacade.getPanelDialogMessage().showConfirmDialog(panel, "Promotion Adding");
			if (result != 0) {
				zooFacade.getPanelDialogMessage().showInformationDialog("Promotion Adding canceled.");
				zooFacade.getPanelFields().setDescriptionField("");
				zooFacade.getPanelFields().setPercentageField("");
				return false;
			}
			if (zooFacade.getPromotionValidation().validatePromotionFields(
					zooFacade.getPanelFields().getDescriptionField().getText(),
					zooFacade.getPanelFields().getPercentageField().getText(), 0)) {
				return true;
			}
		}
	}

	public List<Long> getAllSubscriberIDs() {
		List<Long> subscribersID = new ArrayList<Long>();
		for (Visitor visitor : zooFacade.getSubscriptionManager().getSubscribers()) {
			subscribersID.add(visitor.getID());
		}
		if (subscribersID.isEmpty())
			return null;
		return subscribersID;
	}

	public static StringBuilder promotionStringBuilder() {
		StringBuilder promotionInfo = new StringBuilder();
		int count = 1;

		for (Promotion promotion : zooFacade.getPromotionManager().getPromotions()) {
			promotionInfo.append(count).append(". ").append(promotion).append("\n");
			count++;
		}

		return promotionInfo;
	}

	public void updatePromotionPanel(Promotion promo, int flag) {
		while (true) {
			String orgDescription = promo.getDescription();
			int orgPercentage = promo.getDiscountPercentage();
			JPanel panel = zooFacade.getPanelDisplay().createPromotionUpdatingPanel(promo, flag);
			int result = zooFacade.getPanelDialogMessage().showConfirmDialog(panel, "Promotion Update Menu");
			if (result != 0) {
				zooFacade.getPanelDialogMessage().showInformationDialog("Promotion updating canceled.");
				handlePromotionFields();
				return;
			}
			if (!zooFacade.getPromotionValidation().validatePromotionFields(
					zooFacade.getPanelFields().getDescriptionField().getText(),
					zooFacade.getPanelFields().getPercentageField().getText(), flag)) {
				return;
			}
			try {
				int percentage = Integer.parseInt(zooFacade.getPanelFields().getPercentageField().getText());
				String description = zooFacade.getPanelFields().getDescriptionField().getText();
				zooFacade.getPromotionManager().updatePromotion(promo, description, percentage);
				zooFacade.getPromotionManager().updatePromotionMap(promo, orgDescription, orgPercentage);
				zooFacade.getPanelDialogMessage().showInformationDialog("Promotion updated!");
				handlePromotionFields();
				return;
			} catch (NumberFormatException e) {
				zooFacade.getPanelDialogMessage().showErrorDialog("Invalid percentage format.");
			}
		}
	}

	public int generatePromotionNumber() {
		int promoCounter = zooFacade.getPromotionManager().getPromotions().size();
		return ++promoCounter;
	}

	public boolean hasPendingUpdatesForSubscriber(Long subscriber) {
		return zooFacade.getSubscriptionManager().getSubscribersToNotifyOnUpdates().contains(subscriber);
	}

	public static boolean hasPendingPromotionsForSubscriber(Long subscriber) {
		return zooFacade.getSubscriptionManager().getSubscribersToNotify().contains(subscriber);
	}

	public static void handlePromotionFields() {
		zooFacade.getPanelFields().setPercentageField("");
		zooFacade.getPanelFields().setDescriptionField("");
	}
}
