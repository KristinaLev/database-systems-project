package zooPackage.Interface;

import zooPackage.Entities.Users.Employee;
import zooPackage.Exception.InvalidPasswordException;

public interface PasswordManager_Interface {

	public void registerEmployee(Employee emp);

	public void employeeLoginCheck(String userName, String pass) throws InvalidPasswordException;

	public Employee authenticateEmployee(String userName, String pass);
}
