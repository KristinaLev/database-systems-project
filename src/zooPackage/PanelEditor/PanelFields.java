package zooPackage.PanelEditor;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PanelFields {

	private static PanelFields instance;

	private PanelFields() {
	}

	public static PanelFields getInstance() {
		if (instance == null) {
			instance = new PanelFields();
		}
		return instance;
	}

	public JTextField firstNameField = new JTextField(20);
	public JTextField lastNameField = new JTextField(20);
	public JTextField idField = new JTextField(20);
	public JTextField dobField = new JTextField(20);
	public JTextField phoneNumberField = new JTextField(15);
	public JTextField userNameField = new JTextField(20);
	public JTextField passwordField = new JPasswordField();
	public JTextField percentageField = new JTextField(20);
	public JTextField descriptionField = new JTextField(40);
	public JTextField dbPasswordField = new JPasswordField();

	public JTextField getDbPasswordField() {
		return dbPasswordField;
	}

	public void setDbPasswordField(String password) {
		dbPasswordField.setText(password);
	}

	public JTextField getFirstNameField() {
		return firstNameField;
	}

	public JTextField getLastNameField() {
		return lastNameField;
	}

	public JTextField getIdField() {
		return idField;
	}

	public JTextField getDobField() {
		return dobField;
	}

	public JTextField getPhoneNumberField() {
		return phoneNumberField;
	}

	public JTextField getUserNameField() {
		return userNameField;
	}

	public JTextField getPasswordField() {
		return passwordField;
	}

	public JTextField getDescriptionField() {
		return descriptionField;
	}

	public JTextField getPercentageField() {
		return percentageField;
	}

	public void setFirsNameField(String firstName) {
		getFirstNameField().setText(firstName);
	}

	public void setLastNameField(String lastName) {
		getLastNameField().setText(lastName);
	}

	public void setIdField(String id) {
		getIdField().setText(id);
	}

	public void setDobField(String dob) {
		getDobField().setText(dob);
	}

	public void setPhoneNumberField(String phone) {
		getPhoneNumberField().setText(phone);
	}

	public void setUserNameField(String userName) {
		getUserNameField().setText(userName);
	}

	public void setPasswordField(String password) {
		getPasswordField().setText(password);
	}

	public void setDescriptionField(String description) {
		getDescriptionField().setText(description);
	}

	public void setPercentageField(String percentage) {
		getPercentageField().setText(percentage);
	}

	public void clearPersonalInfoPanel() {
		setFirsNameField("");
		setLastNameField("");
		setIdField("");
		setDobField("");
		setPhoneNumberField("");
		setUserNameField("");
		setPasswordField("");
	}

}
