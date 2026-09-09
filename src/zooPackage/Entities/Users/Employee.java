package zooPackage.Entities.Users;

import java.time.LocalDate;
import java.util.Objects;

public class Employee extends Person {

	protected int employee_id;
	protected String username;
	private String passwordHash;

	public Employee(int employee_id, String username, String fName, String lName, long id, LocalDate dob,
			String phoneNum) {
		super(fName, lName, id, dob, phoneNum);
		this.employee_id = employee_id;
		this.username = username;
	}

	public int getEmployee_ID() {
		return employee_id;
	}

	public void setEmployee_ID(int employee_id) {
		this.employee_id = employee_id;
	}

	public void setUserName(String username) {
		this.username = username;
	}

	public String getUserName() {
		return username;
	}

	public void setPasswordHash(String hash) {
		this.passwordHash = hash;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return ID == other.ID;
	}

	@Override
	public String toString() {
		return "No. Employee: " + employee_id + "[ Username: " + username + ", Full Name: " + firstName + " " + lastName
				+ ", ID: " + ID + ", " + dob + ", " + phoneNumber + "]";
	}

}
