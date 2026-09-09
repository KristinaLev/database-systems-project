package zooPackage.Interface;

import java.util.HashSet;

import zooPackage.Entities.Users.Employee;

public interface EmployeeManager_Interface {

	public void createDefaultEmployees();

	boolean createEmployee();
	
	public void addEmployee(Employee emp);

	public HashSet<Employee> getEmployees();

	public int getLegalAge();

	
}
