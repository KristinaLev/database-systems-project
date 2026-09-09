package zooPackage.Main;

import zooPackage.Management.ZooFacade;

public class Zoo_Main {

	private static ZooFacade zooFacade = ZooFacade.getInstance();

	public static void main(String[] args) {
		zooFacade.getSync().SYNC_DB();
		zooFacade.getAnimalDisplay().displayWelcomeMessage();
		zooFacade.getMenus().mainMenu();
	}
}
