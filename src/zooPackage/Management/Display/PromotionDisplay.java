package zooPackage.Management.Display;

import zooPackage.Management.ZooFacade;
import zooPackage.Management.Utils.PromotionUtils;

public class PromotionDisplay {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static PromotionDisplay instance;

	private PromotionDisplay() {
	}

	public static synchronized PromotionDisplay getInstance() {
		if (instance == null) {
			instance = new PromotionDisplay();
		}
		return instance;
	}

	public void displayPromotionList() {
		if (zooFacade.getPromotionValidation().checkIfPromotionListEmpty()) {
			return;
		} else {
			displayPromotions();
		}
	}

	public void displayPromotions() {
		zooFacade.getPanelDialogMessage().showListDialog(PromotionUtils.promotionStringBuilder().toString(),
				"Promotion list");
	}
}
