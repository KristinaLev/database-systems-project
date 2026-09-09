package zooPackage.Management.Selection;

import java.util.List;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import zooPackage.Entities.Promotions.Promotion;
import zooPackage.Management.ZooFacade;

public class PromotionSelection {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static PromotionSelection instance;

	private PromotionSelection() {
	}

	public static synchronized PromotionSelection getInstance() {
		if (instance == null) {
			instance = new PromotionSelection();
		}
		return instance;
	}

	public Promotion selectPromotion() {
		Promotion[] promotions = new Promotion[zooFacade.getPromotionManager().getPromotions().size()];
		int count = 0;
		for (Promotion promotion : zooFacade.getPromotionManager().getPromotions()) {
			promotions[count] = promotion;
			count++;
		}

		JList<Promotion> promotionTypesJList = new JList<>(promotions);
		promotionTypesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JPanel panel = zooFacade.getPanelDisplay().createPromotionListPanel(promotionTypesJList, "Choose promotion:");

		int result;
		while (true) {
			result = zooFacade.getPanelDialogMessage().showConfirmDialog(panel, "Promotion Menu");
			if (result != 0) {
				zooFacade.getPanelDialogMessage().showInformationDialog("Choosing promotion canceled.");
				return null;
			}

			if (promotionTypesJList.getSelectedValuesList().isEmpty()) {
				zooFacade.getPanelDialogMessage().showWarningDialog(
						"Hi friend!\nIn order to get promotion you have to choose one promotion type.");
				continue;
			} else {
				List<Promotion> selectedValues = promotionTypesJList.getSelectedValuesList();
				Promotion selectedPromotion = selectedValues.get(0);
				return selectedPromotion;
			}
		}
	}
	
	

}
