package zooPackage.Management;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import zooPackage.Entities.Users.Employee;
import zooPackage.Exception.InvalidPasswordException;
import zooPackage.Interface.PasswordManager_Interface;

public class PasswordManager implements PasswordManager_Interface {

	private HashMap<String, String> passwords = new HashMap<>();
	private HashMap<String, Employee> employees = new HashMap<>();
	private static PasswordManager instance;
	private Employee loggedIn;

	private PasswordManager() {
	}

	public static PasswordManager getInstance() {
		if (instance == null) {
			instance = new PasswordManager();
		}
		return instance;
	}

	@Override
	public void registerEmployee(Employee emp) {
		employees.put(emp.getUserName(), emp);
		passwords.put(emp.getUserName(), emp.getPasswordHash());
	}

	@Override
	public void employeeLoginCheck(String userName, String pass) throws InvalidPasswordException {
		loggedIn = authenticateEmployee(userName, pass);
		if (loggedIn == null) {
			throw new InvalidPasswordException("Invalid username or password\n");
		}
	}

	public static String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : hashedBytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 algorithm not found", e);
		}
	}

	@Override
	public Employee authenticateEmployee(String userName, String rawPassword) {
		Employee employee = employees.get(userName);
		if (employee != null) {
			String inputHash = hashPassword(rawPassword);
			if (employee.getPasswordHash() != null && employee.getPasswordHash().equals(inputHash)) {
				return employee;
			}
		}
		return null;
	}

	public void clear() {
		employees.clear();
		passwords.clear();
	}

	public Employee getLoggedInEmployee() {
		return loggedIn;
	}

	public HashMap<String, String> getPasswords() {
		return passwords;
	}

	public HashMap<String, Employee> getEmployees() {
		return employees;
	}
}
