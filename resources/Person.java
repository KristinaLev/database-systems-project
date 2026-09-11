package zooPackage;

import java.util.Objects;

public abstract class Person {

	protected int ID;
	protected String firstName;
	protected String lastName;
	protected String DOB; // Date of Birth
	protected String phoneNumber;

	public Person(String fName, String lName, int iD, String dOB, String phoneNum) {
		this.firstName = fName;
		this.lastName = lName;
		this.ID = iD;
		this.DOB = dOB;
		this.phoneNumber = phoneNum;
	}

	public int getID() {
		return ID;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getDOB() {
		return DOB;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public abstract String getUserName();

//	@Override
//	public int hashCode() {
//		return Objects.hash(ID);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Person other = (Person) obj;
//		return ID == other.ID;
//	}

}
