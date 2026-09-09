package zooPackage.Management.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JPanel;

import zooPackage.Exception.InvalidAgeException;
import zooPackage.Exception.InvalidIDException;
import zooPackage.Exception.InvalidPhoneNumberException;
import zooPackage.Exception.InvalidUserNameException;
import zooPackage.Management.ZooFacade;

public class PersonValidator {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static PersonValidator instance;

	private PersonValidator() {
	}

	public static PersonValidator getInstance() {
		if (instance == null) {
			instance = new PersonValidator();
		}
		return instance;
	}

	public boolean validatePersonalInfo(JPanel panel, boolean isEmployee) {
		while (true) {
			int result = zooFacade.getPanelDialogMessage().showConfirmDialog(panel, "Enter the registration details:");
			if (result != 0) {
				zooFacade.getPanelDialogMessage().showInformationDialog("Registration canceled.");
				zooFacade.getPanelFields().clearPersonalInfoPanel();
				return false;
			}

			String id = zooFacade.getPanelFields().getIdField().getText();
			String dob = zooFacade.getPanelFields().getDobField().getText();
			String phone = zooFacade.getPanelFields().getPhoneNumberField().getText();

			if (zooFacade.getPersonValidation().arePersonalInfoFieldsEmpty())
				continue;

			if (!zooFacade.getPersonValidation().isValidIDFormat(id)) {
				continue;
			}

			if (zooFacade.getPersonValidation().isVisitorIDTaken(id)
					|| zooFacade.getPersonValidation().isEmployeeIDTaken(id)) {
				zooFacade.getPanelFields().setIdField("");
				continue;
			}

			if (!zooFacade.getPersonValidation().validateDateOfBirth(dob, isEmployee)) {
				zooFacade.getPanelFields().setDobField("");
				continue;
			}

			if (!zooFacade.getPersonValidation().isValidPhoneNumberFormat(phone)) {
				zooFacade.getPanelFields().setPhoneNumberField("");
				continue;
			}

			if (zooFacade.getPersonValidation().isVisitorPhoneNumberTaken(id, phone)
					|| zooFacade.getPersonValidation().isEmployeePhoneNumberTaken(id, phone)) {
				zooFacade.getPanelFields().setPhoneNumberField("");
				continue;
			}
			
			return true;

		}
	}

	public boolean validateEmployeeLoginInfo(JPanel panel) {
		while (true) {
			int result = zooFacade.getPanelDialogMessage().showConfirmDialog(panel, "Enter new username and password:");
			if (result != 0) {
				zooFacade.getPanelDialogMessage().showInformationDialog("Registration canceled.");
				return false;
			}
			if (zooFacade.getPersonValidation()
					.validateEmployeeLoginFields(zooFacade.getPanelFields().getUserNameField().getText())) {
				return true;
			} else {
				zooFacade.getPanelFields().setUserNameField("");
			}
		}
	}

	public boolean isValidIDFormat(String id) {
		try {
			if (id == null || id.length() < 8 || id.length() > 10) {
				throw new InvalidIDException("ID number must be at least 8-10 digits long.");
			}
			Long.parseLong(id);
			return true;
		} catch (InvalidIDException | NumberFormatException e) {
			zooFacade.getPanelDialogMessage().showErrorDialog(e.getMessage());
			zooFacade.getPanelFields().setIdField("");
			return false;
		}
	}

	public boolean isVisitorIDTaken(String id) {
		if (zooFacade.getVisitorManager().findVisitorByID(id) != null) {
			zooFacade.getPanelDialogMessage().showErrorDialog("This ID already exists.");
			zooFacade.getPanelFields().setIdField("");
			return true;
		}
		return false;
	}

	public boolean isEmployeeIDTaken(String id) {
		if (zooFacade.getEmployeeManager().findEmployeeByID(id) != null) {
			zooFacade.getPanelDialogMessage().showErrorDialog("This ID already exists.");
			zooFacade.getPanelFields().setIdField("");
			return true;
		}
		return false;
	}

	public boolean checkIfVisitorLoginFieldsEmpty() {
		return zooFacade.getPanelFields().getIdField().getText().isEmpty();
	}

	public boolean checkIfEmployeeLoginFieldsEmpty() {
		return zooFacade.getPanelFields().getUserNameField().getText().isEmpty()
				|| zooFacade.getPanelFields().getPasswordField().getText().isEmpty();
	}

	public boolean validateEmployeeLoginFields(String userName) {
		if (checkIfEmployeeLoginFieldsEmpty()) {
			zooFacade.getPanelDialogMessage().showWarningDialog(
					"Hi user!\nIn order to finish the registration, you have to enter new username and password.");
			return false;
		}
		try {
			if (zooFacade.getEmployeeManager().findEmployeeByUsername(userName) != null)
				throw new InvalidUserNameException("This username is taken, try again.");
			return true;
		} catch (InvalidUserNameException e) {
			zooFacade.getPanelDialogMessage().showErrorDialog(e.getMessage());
			return false;
		}
	}

	public boolean arePersonalInfoFieldsEmpty() {
		if (zooFacade.getPanelFields().getFirstNameField().getText().isEmpty()
				|| zooFacade.getPanelFields().getLastNameField().getText().isEmpty()
				|| zooFacade.getPanelFields().getIdField().getText().isEmpty()
				|| zooFacade.getPanelFields().getDobField().getText().isEmpty()
				|| zooFacade.getPanelFields().getPhoneNumberField().getText().isEmpty()) {
			zooFacade.getPanelDialogMessage()
					.showWarningDialog("Hi user!\nIn order to finish the log-in you have to enter all the data");
			return true;
		}
		return false;
	}

	public boolean validateDateField(String dob) {
		try {
			LocalDate.parse(dob);
			return true;
		} catch (DateTimeParseException e) {
			zooFacade.getPanelDialogMessage().showErrorDialog("Date must be in the format yyyy-mm-dd.");
			return false;
		}
	}

	public boolean validateDateOfBirth(String dob, boolean isEmployee) {
		try {
			validateDateField(dob);
			if (isEmployee) {
				validateEmployeeAge(dob);
			}
			return true;
		} catch (InvalidAgeException e) {
			zooFacade.getPanelDialogMessage().showErrorDialog(e.getMessage());
			zooFacade.getPanelFields().setDobField("");
			return false;

		}
	}

	public void validateEmployeeAge(String dobString) throws InvalidAgeException {
		LocalDate currentDate = LocalDate.now();
		LocalDate dob = LocalDate.parse(dobString);
		int age = currentDate.getYear() - dob.getYear();
		if (age < zooFacade.getEmployeeManager().getLegalAge())
			throw new InvalidAgeException(
					"Age must be greater than " + zooFacade.getEmployeeManager().getLegalAge() + " years.");
	}

	public boolean isValidPhoneNumberFormat(String phone) {
		try {
			if (phone.isEmpty() || phone.length() < 10 || phone.length() > 15) {
				throw new InvalidPhoneNumberException("Phone number must be at least 8-15 digits long.");
			}
			return true;
		} catch (InvalidPhoneNumberException e) {
			zooFacade.getPanelDialogMessage().showErrorDialog(e.getMessage());
			zooFacade.getPanelFields().setIdField("");
			return false;
		}
	}

	public boolean isVisitorPhoneNumberTaken(String id, String phone) {
		if (zooFacade.getVisitorManager().findVisitorByID(id) != null) {
			if (zooFacade.getVisitorManager().findVisitorByID(id).getPhoneNumber().equals(phone)) {
				zooFacade.getPanelDialogMessage().showErrorDialog("This phone number already exists.");
				zooFacade.getPanelFields().clearPersonalInfoPanel();
				return true;
			}
		}
		return false;
	}

	public boolean isEmployeePhoneNumberTaken(String id, String phone) {
		if (zooFacade.getEmployeeManager().findEmployeeByID(id) != null) {
			if (zooFacade.getEmployeeManager().findEmployeeByID(id).getPhoneNumber().equals(phone)) {
				zooFacade.getPanelDialogMessage().showErrorDialog("This phone number already exists.");
				zooFacade.getPanelFields().clearPersonalInfoPanel();
				return true;
			}
		}
		return false;
	}

}
