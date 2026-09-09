package zooPackage.Management;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import zooPackage.DB.DBUtil;
import zooPackage.DB.EmployeeDAO;
import zooPackage.Entities.Users.Employee;
import zooPackage.Interface.EmployeeManager_Interface;

public class EmployeeManager implements EmployeeManager_Interface {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static EmployeeManager instance;
	private static LinkedHashSet<Employee> employees = new LinkedHashSet<Employee>();
	private static final int LEGAL_AGE = 18;

	private EmployeeManager() {
	}

	public static EmployeeManager getInstance() {
		if (instance == null) {
			instance = new EmployeeManager();
		}
		return instance;
	}

	@Override
	public void createDefaultEmployees() {
		try (Connection conn = DBUtil.getConnection()) {
			EmployeeDAO employeeDAO = new EmployeeDAO(conn);

			String hashedPassword1 = PasswordManager.hashPassword("igor");
			String hashedPassword2 = PasswordManager.hashPassword("IBRW");
			String hashedPassword3 = PasswordManager.hashPassword("9999");

			Employee employee1 = new Employee(0, "igor", "Igor", "Chikati", 33333333, LocalDate.parse("1998-02-19"),
					"0546419400");
			employee1.setPasswordHash(hashedPassword1);
			employee1.setEmployee_ID(employeeDAO.insertEmployeeAndGetId(employee1));

			Employee employee2 = new Employee(0, "IBRW", "IBRW", "Chikati", 44444444, LocalDate.parse("1998-02-19"),
					"0546419400");
			employee2.setPasswordHash(hashedPassword2);
			employee2.setEmployee_ID(employeeDAO.insertEmployeeAndGetId(employee2));

			Employee employee3 = new Employee(0, "9999", "9999", "9999", 99999999, LocalDate.parse("1998-02-19"),
					"0546419400");
			employee3.setPasswordHash(hashedPassword3);
			employee3.setEmployee_ID(employeeDAO.insertEmployeeAndGetId(employee3));

			zooFacade.getSync().syncEmployeeFromDatabase();

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to insert default employees.", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public boolean createEmployee() {
		while (true) {
			JPanel personalInfoPanel = zooFacade.getPanelDisplay().createPersonalInfoPanel();
			JPanel loginInfoPanel = zooFacade.getPanelDisplay().createEmployeeLoginInfoPanel();

			if (!zooFacade.getPersonValidation().validatePersonalInfo(personalInfoPanel, true)
					|| !zooFacade.getPersonValidation().validateEmployeeLoginInfo(loginInfoPanel)) {
				return false;
			}

			try (Connection conn = DBUtil.getConnection()) {
				EmployeeDAO employeeDAO = new EmployeeDAO(conn);

				Employee newEmployee = getNewEmployeeInfo();

				if (newEmployee == null) {
					return false;
				}

				String rawPassword = zooFacade.getPanelFields().getPasswordField().getText();
				String hashedPassword = PasswordManager.hashPassword(rawPassword);

				newEmployee.setPasswordHash(hashedPassword);

				int employeeId = employeeDAO.insertEmployeeAndGetId(newEmployee);
				newEmployee.setEmployee_ID(employeeId);
				addEmployee(newEmployee);
				zooFacade.getPasswordManager().registerEmployee(newEmployee);

				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				zooFacade.getPanelDialogMessage().showDBErrorDialog("Failed to insert employee.");
				return false;
			}
		}
	}

	public Employee getNewEmployeeInfo() {
		try {
			String username = zooFacade.getPanelFields().getUserNameField().getText();
			String fName = zooFacade.getPanelFields().getFirstNameField().getText();
			String lName = zooFacade.getPanelFields().getLastNameField().getText();
			long id = Long.parseLong(zooFacade.getPanelFields().getIdField().getText());
			LocalDate dob = LocalDate.parse(zooFacade.getPanelFields().getDobField().getText());
			String phone = zooFacade.getPanelFields().getPhoneNumberField().getText();

			Employee newEmployee = new Employee(0, username, fName, lName, id, dob, phone);
			return newEmployee;

		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Employee findEmployeeByID(String employeeID) {
		for (Employee employee : getEmployees()) {
			if (employeeID.equals("" + employee.getID()))
				return employee;
		}
		return null;
	}

	public Employee findEmployeeByUsername(String username) {
		for (Employee emp : zooFacade.getEmployeeManager().getEmployees())
			if (emp.getUserName().equals(username)) {
				return emp;
			}
		return null;
	}

	@Override
	public void addEmployee(Employee emp) {
		employees.add(emp);
	}

	@Override
	public HashSet<Employee> getEmployees() {
		return employees;
	}

	@Override
	public int getLegalAge() {
		return LEGAL_AGE;
	}

}
