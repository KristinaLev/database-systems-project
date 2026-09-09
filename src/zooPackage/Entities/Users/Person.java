package zooPackage.Entities.Users;

import java.time.LocalDate;

public abstract class Person {
	protected String firstName;
	protected String lastName;
	protected long ID;
	protected LocalDate dob;
	protected String phoneNumber;

	public Person(String fName, String lName, long ID, LocalDate dob, String phoneNum) {
		this.firstName = fName;
		this.lastName = lName;
		this.ID = ID;
		this.dob = dob;
		this.phoneNumber = phoneNum;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public LocalDate getDOB() {
		return dob;
	}

	public void setDOB(LocalDate dOB) {
		dob = dOB;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
