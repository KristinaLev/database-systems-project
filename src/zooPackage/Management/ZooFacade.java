package zooPackage.Management;

import zooPackage.DB.*;
import zooPackage.Management.Selection.*;
import zooPackage.Management.Utils.*;
import zooPackage.Management.Validator.*;
import zooPackage.Management.Display.*;
import zooPackage.PanelEditor.*;
import zooPackage.Menu.Menus;

public class ZooFacade {

	private AnimalManagement animalManagement;
	private AnimalManager animalManager;
	private EmployeeManager employeeManager;
	private LoginManager loginManager;
	private PasswordManager passwordManager;
	private PromotionManager promotionManager;
	private SubscriptionManager subscriptionManager;
	private TicketManager ticketManager;
	private VisitorManager visitorManager;

	private AnimalDisplay animalDisplay;
	private PromotionDisplay promotionDisplay;
	private SubscriptionDisplay subscriptionDisplay;
	private TicketDisplay ticketDisplay;

	private PromotionSelection promotionSelection;
	private SubscriptionSelection subscriptionSelection;
	private TicketSelection ticketSelection;

	private PromotionUtils promotionUtils;
	private SubscriptionUtils subscriptionUtils;
	private TicketUtils ticketUtils;

	private AnimalValidator animalValidation;
	private PersonValidator personValidation;
	private PromotionValidator promotionValidation;
	private SubscriptionValidator subscriptionValidation;
	private TicketValidator ticketValidation;

	private Menus menus;

	// Data Base
	private AnimalDAO animal_dao;
	private DBUtil db_Util;
	private EmployeeDAO emp_dao;
	private PromotionDAO promo_dao;
	private Sync sync;
	private TicketDAO ticket_dao;
	private VisitorDAO visitor_dao;

	private PanelDialogMessage panelDialogMessage;
	private PanelDisplay panelDisplay;
	private PanelFields panelFields;

	private static ZooFacade instance;

	private ZooFacade() {

		ticketManager = TicketManager.getInstance();
		ticketDisplay = TicketDisplay.getInstance();
		ticketSelection = TicketSelection.getInstance();
		ticketValidation = TicketValidator.getInstance();
		ticketUtils = TicketUtils.getInstance();

		promotionManager = PromotionManager.getInstance();
		promotionDisplay = PromotionDisplay.getInstance();
		promotionSelection = PromotionSelection.getInstance();
		promotionValidation = PromotionValidator.getInstance();
		promotionUtils = PromotionUtils.getInstance();

		subscriptionManager = SubscriptionManager.getInstance();
		subscriptionSelection = SubscriptionSelection.getInstance();
		subscriptionDisplay = SubscriptionDisplay.getInstance();
		subscriptionUtils = SubscriptionUtils.getInstance();
		subscriptionValidation = SubscriptionValidator.getInstance();

		visitorManager = VisitorManager.getInstance();
		employeeManager = EmployeeManager.getInstance();
		personValidation = PersonValidator.getInstance();

		loginManager = LoginManager.getInstance();
		passwordManager = PasswordManager.getInstance();

		animalManager = AnimalManager.getInstance();
		animalManagement = AnimalManagement.getInstance();
		animalDisplay = AnimalDisplay.getInstance();
		animalValidation = AnimalValidator.getInstance();

		menus = Menus.getInstance();
		sync = Sync.getInstance();

		panelDialogMessage = PanelDialogMessage.getInstance();
		panelDisplay = PanelDisplay.getInstance();
		panelFields = PanelFields.getInstance();

	}

	public static ZooFacade getInstance() {
		if (instance == null) {
			instance = new ZooFacade();
		}
		return instance;
	}

	/*
	 * Ticket Getters/Setters
	 */
	public TicketManager getTicketManager() {
		return ticketManager;
	}

	public TicketDisplay getTicketDisplay() {
		return ticketDisplay;
	}

	public TicketSelection getTicketSelection() {
		return ticketSelection;
	}

	public TicketValidator getTicketValidationUtils() {
		return ticketValidation;
	}

	public TicketUtils getTicketUtils() {
		return ticketUtils;
	}

	/*
	 * Promotion Getters/Setters
	 */
	public PromotionManager getPromotionManager() {
		return promotionManager;
	}

	public PromotionDisplay getPromotionDisplay() {
		return promotionDisplay;
	}

	public PromotionSelection getPromotionSelection() {
		return promotionSelection;
	}

	public PromotionValidator getPromotionValidation() {
		return promotionValidation;
	}

	public PromotionUtils getPromotionUtils() {
		return promotionUtils;
	}

	/*
	 * Person Getters/Setters
	 */
	public VisitorManager getVisitorManager() {
		return visitorManager;
	}

	public EmployeeManager getEmployeeManager() {
		return employeeManager;
	}

	public PersonValidator getPersonValidation() {
		return personValidation;
	}

	/*
	 * Authentication Getters/Setters
	 */
	public LoginManager getLoginManager() {
		return loginManager;
	}

	public PasswordManager getPasswordManager() {
		return passwordManager;
	}

	/*
	 * Zoo Getters/Setters
	 */
	public AnimalManager getAnimalManager() {
		return animalManager;
	}

	public AnimalManagement getAnimalManagement() {
		return animalManagement;
	}

	public Menus getMenus() {
		return menus;
	}

	/*
	 * Panel Getters/Setters
	 */
	public PanelDialogMessage getPanelDialogMessage() {
		return panelDialogMessage;
	}

	public PanelDisplay getPanelDisplay() {
		return panelDisplay;
	}

	public PanelFields getPanelFields() {
		return panelFields;
	}

	/*
	 * Data Base getters/setters
	 */

	public Sync getSync() {
		return sync;
	}

	public AnimalDAO getAnimalDAO() {
		return animal_dao;
	}

	public void setAnimalDAO(AnimalDAO animal_dao) {
		this.animal_dao = animal_dao;
	}

	public VisitorDAO getVisitorDAO() {
		return visitor_dao;
	}

	public void setVisitorDAO(VisitorDAO visitor_dao) {
		this.visitor_dao = visitor_dao;
	}

	public TicketDAO getTicketDAO() {
		return ticket_dao;
	}

	public void setTicketDAO(TicketDAO ticket_dao) {
		this.ticket_dao = ticket_dao;
	}

	public PromotionDAO getPromoDAO() {
		return promo_dao;
	}

	public void setPromoDAO(PromotionDAO promo_dao) {
		this.promo_dao = promo_dao;
	}

	public EmployeeDAO getEmpDAO() {
		return emp_dao;
	}

	public void setEmpDAO(EmployeeDAO emp_dao) {
		this.emp_dao = emp_dao;
	}

	public SubscriptionManager getSubscriptionManager() {
		return subscriptionManager;
	}

	public void setSubscriptionManager(SubscriptionManager subscriptionManager) {
		this.subscriptionManager = subscriptionManager;
	}

	public SubscriptionSelection getSubscriptionSelection() {
		return subscriptionSelection;
	}

	public void setSubscriptionSelection(SubscriptionSelection subscriptionSelection) {
		this.subscriptionSelection = subscriptionSelection;
	}

	public SubscriptionDisplay getSubscriptionDisplay() {
		return subscriptionDisplay;
	}

	public void setSubscriptionDisplay(SubscriptionDisplay subscriptionDisplay) {
		this.subscriptionDisplay = subscriptionDisplay;
	}

	public SubscriptionUtils getSubscriptionUtils() {
		return subscriptionUtils;
	}

	public void setSubscriptionUtils(SubscriptionUtils subscriptionUtils) {
		this.subscriptionUtils = subscriptionUtils;
	}

	public AnimalDisplay getAnimalDisplay() {
		return animalDisplay;
	}

	public void setAnimalDisplay(AnimalDisplay animalDisplay) {
		this.animalDisplay = animalDisplay;
	}

	public AnimalValidator getAnimalValidation() {
		return animalValidation;
	}

	public void setAnimalValidation(AnimalValidator animalValidation) {
		this.animalValidation = animalValidation;
	}

	public DBUtil getDb_Util() {
		return db_Util;
	}

	public SubscriptionValidator getSubscriptionValidation() {
		return subscriptionValidation;
	}

}
