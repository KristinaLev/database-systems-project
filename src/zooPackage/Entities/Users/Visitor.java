package zooPackage.Entities.Users;

import java.time.LocalDate;

import zooPackage.Interface.Observer;

public class Visitor extends Person implements Observer {

	protected int visitor_id;
	protected int registered_by_employee_id;

	public Visitor(int visitor_id, String fName, String lName, long id, LocalDate dob, String phoneNum,
			int employee_id) {
		super(fName, lName, id, dob, phoneNum);
		this.visitor_id = visitor_id;
		this.registered_by_employee_id = employee_id;
	}

	public int getVisitor_id() {
		return visitor_id;
	}

	public void setVisitor_id(int visitor_id) {
		this.visitor_id = visitor_id;
	}

	public int getRegistered_by_employee_id() {
		return registered_by_employee_id;
	}

	public void setRegistered_by_employee_id(int registered_by_employee_id) {
		this.registered_by_employee_id = registered_by_employee_id;
	}

	@Override
	public String toString() {
		return "Visitor: " + firstName + " " + lastName + " | " + ID + " | " + dob + " | " + phoneNumber + "\n";
	}

	@Override
	public String notifySubscribers(String promotion) {
		return "Dear " + firstName + ", you have a new promotion\\s:\n" + promotion;
	}

}
