package zooPackage.Management;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import zooPackage.DB.DBUtil;
import zooPackage.DB.PromotionDAO;
import zooPackage.Entities.Promotions.Promotion;
import zooPackage.Entities.Users.Visitor;
import zooPackage.Enum.SubscriptionType;
import zooPackage.Interface.PromotionManager_Interface;

public class PromotionManager implements PromotionManager_Interface {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private List<Promotion> promotions;
	private Map<String, Promotion> updatedPromotionsMap = new HashMap<>();

	private static PromotionManager instance;

	private PromotionManager() {
		promotions = new ArrayList<>();
	}

	public static synchronized PromotionManager getInstance() {
		if (instance == null) {
			instance = new PromotionManager();
		}
		return instance;
	}

	public void addPromotionToZooAndDatabase(Promotion promo) {
		getPromotions().add(promo);
		try (Connection conn = DBUtil.getConnection()) {
			PromotionDAO dao = new PromotionDAO(conn);
			dao.insertPromotionToDatabase(promo);

		} catch (SQLException e) {
			e.printStackTrace();
			zooFacade.getPanelDialogMessage()
					.showDBErrorDialog("Failed to save " + promo.getDescription() + " to database.");
		}
	}

	@Override
	public void createDefaultPromotions() {
		Promotion promo1 = new Promotion(1, "New subscriber discount", 15);
		Promotion promo2 = new Promotion(2, "Holiday Sale", 45);
		Promotion promo3 = new Promotion(3, "Sale", 10);
		Promotion promo4 = new Promotion(4, "1 + 1", 50);
		addPromotionToZooAndDatabase(promo1);
		addPromotionToZooAndDatabase(promo2);
		addPromotionToZooAndDatabase(promo3);
		addPromotionToZooAndDatabase(promo4);
	}

	@Override
	public void createNewPromotion() {
		while (true) {
			JPanel promotionPanel = zooFacade.getPanelDisplay().createPromotionAddingPanel();
			if (!zooFacade.getPromotionUtils().getPromotionInfo(promotionPanel)) {
				return;
			}
			try {
				int percentage = Integer.parseInt(zooFacade.getPanelFields().getPercentageField().getText());
				Promotion newPromotion = new Promotion(zooFacade.getPromotionUtils().generatePromotionNumber(),
						zooFacade.getPanelFields().getDescriptionField().getText(), percentage);
				addPromotionToZooAndDatabase(newPromotion);
				zooFacade.getPanelDialogMessage().showInformationDialog("Promotion added!");
				zooFacade.getPanelFields().setPercentageField("");
				zooFacade.getPanelFields().setDescriptionField("");
				return;
			} catch (NumberFormatException e) {
				zooFacade.getPanelDialogMessage().showErrorDialog("Invalid percentage format.");
			}
		}
	}

	@Override
	public void addPromotion(Promotion promotion) {
		getPromotions().add(promotion);
		String key = promotion.getDescription() + " with " + promotion.getDiscountPercentage() + "% off.";
		getUpdatedPromotionsMap().put(key, promotion);
		zooFacade.getPromotionUtils();
		zooFacade.getSubscriptionManager()
				.addSubscribersToUpdateNotify(zooFacade.getPromotionUtils().getAllSubscriberIDs());
	}

	@Override
	public void updatePromotion(Promotion updatedPromotion, String description, int percentage) {
		for (Promotion promo : getPromotions()) {
			if (promo.getPromotionID() == updatedPromotion.getPromotionID()) {
				promo.setDescription(description);
				promo.setDiscountPercentage(percentage);

				try (Connection conn = DBUtil.getConnection()) {
					PromotionDAO dao = new PromotionDAO(conn);
					dao.updatePromotion(promo);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	public void updatePromotionMap(Promotion promotion, String description, int percentage) {
		String orgKey = description + " with " + percentage + "% off.";
		String newKey = promotion.getDescription() + " with " + promotion.getDiscountPercentage() + "% off.";
		if (getUpdatedPromotionsMap().containsKey(orgKey)) {
			getUpdatedPromotionsMap().remove(orgKey);
			getUpdatedPromotionsMap().put(newKey, promotion);
		} else {
			getUpdatedPromotionsMap().put(newKey, promotion);
		}
	}

	@Override
	public void removePromotion(Promotion promo) {
		getPromotions().remove(promo);
	}

	@Override
	public void sendPromotions() {
		if (zooFacade.getPromotionValidation().checkIfSubscriberListEmpty())
			return;

		if (zooFacade.getPromotionValidation().checkIfPromotionListEmpty())
			return;

		zooFacade.getPromotionUtils();
		List<Long> subscriberIDs = zooFacade.getPromotionUtils().getAllSubscriberIDs();
		zooFacade.getSubscriptionManager().addSubscribersToNotify(subscriberIDs);

		zooFacade.getPanelDialogMessage().showInformationDialog(
				"All subscribers received the message about our promotions!\nThey will receive the message once they have logged into the system with their ID.");
	}

	@Override
	public void unsubscribeFromPromotion() {
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
			String visitor_id = zooFacade.getPanelFields().getIdField().getText();
			try {
				if (!zooFacade.getPersonValidation().checkIfVisitorLoginFieldsEmpty()) {
					Visitor subscriber = zooFacade.getLoginManager().subscriberLoginCheck(visitor_id);
					if (subscriber == null) {
						zooFacade.getPanelDialogMessage()
								.showInformationDialog("The ID does not exist in the subscribtion list.");
						zooFacade.getPanelFields().setIdField("");
						return;
					}

					String subscription = zooFacade.getSubscriptionValidation()
							.checkWhatSubscriptionVisitorSubscribed(subscriber.getVisitor_id());
					if (subscription.equals(SubscriptionType.PROMOTION.getDisplayName())) {
						zooFacade.getSubscriptionManager().removeSubscriber(subscriber);
						zooFacade.getPanelDialogMessage().showInformationDialog("Unsubscribing completed!");
					} else {
						zooFacade.getPanelDialogMessage()
								.showInformationDialog("The ID does not exist in the promotion subscribtion list.");
					}
					zooFacade.getPanelFields().setIdField("");
					return;

				} else
					zooFacade.getPanelDialogMessage().showWarningDialog(
							"Hi user!\nIn order to finish the unsubscrbtion you have to enter the ID");
			} catch (NumberFormatException e) {
				zooFacade.getPanelDialogMessage().showErrorDialog("Invalid ID format.");
				zooFacade.getPanelFields().setIdField("");
			}
		}
	}

	@Override
	public List<Promotion> getPromotions() {
		return promotions;
	}

	@Override
	public Map<String, Promotion> getUpdatedPromotionsMap() {
		return updatedPromotionsMap;
	}

	@Override
	public List<Promotion> getUpdatedPromotions() {
		return null;
	}
}
