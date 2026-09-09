package zooPackage.Interface;

import zooPackage.Entities.Users.Visitor;
import zooPackage.Exception.InvalidIDException;
import zooPackage.Exception.InvalidPasswordException;

public interface LoginManager_Interface {

	public boolean loginEmployeeCase();

	public boolean loginVisitorCase();

	public boolean registrateEmployeeCase();

	public Visitor authenticateVisitorID(String visitorID);

	public void visitorLoginCheck(String visitorID) throws InvalidIDException;

	public int showPromtAndGetChoice(String menuPrompt, String menuTitle, int minChoice, int maxChoice);

	public void handleInvalidIDException(InvalidIDException e);

	public void handleNumberFormatException();

	public void handleIInvalidPasswordException(InvalidPasswordException e);
}
