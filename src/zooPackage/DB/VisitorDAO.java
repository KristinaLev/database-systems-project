package zooPackage.DB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import zooPackage.Entities.Users.Visitor;
import zooPackage.Management.ZooFacade;

public class VisitorDAO {
	private final Connection conn;
	private static ZooFacade zooFacade = ZooFacade.getInstance();

	public VisitorDAO(Connection conn) {
		this.conn = conn;
	}

	public void insertPersonIfNotExists(Visitor visitor) throws SQLException {
		String checkSql = "SELECT 1 FROM Person WHERE person_id = ?";
		try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
			checkStmt.setLong(1, visitor.getID());
			ResultSet rs = checkStmt.executeQuery();
			if (rs.next())
				return;

			String insertSql = "INSERT INTO Person (person_id, f_name, l_name, dob, phone_number) VALUES (?, ?, ?, ?, ?)";
			try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
				insertStmt.setLong(1, visitor.getID());
				insertStmt.setString(2, visitor.getFirstName());
				insertStmt.setString(3, visitor.getLastName());
				insertStmt.setDate(4, Date.valueOf(visitor.getDOB()));
				insertStmt.setString(5, visitor.getPhoneNumber());
				insertStmt.executeUpdate();
			}
		}
	}

	public void insertVisitorToDatabase(Visitor visitor) throws SQLException {
		long personalId = visitor.getID();

		String checkPersonSql = "SELECT 1 FROM Person WHERE personal_id = ?";
		try (PreparedStatement checkPersonStmt = conn.prepareStatement(checkPersonSql)) {
			checkPersonStmt.setLong(1, personalId);
			ResultSet rs = checkPersonStmt.executeQuery();
			if (!rs.next()) {
				String insertPersonSql = "INSERT INTO Person (personal_id, f_name, l_name, dob, phone_number) VALUES (?, ?, ?, ?, ?)";
				try (PreparedStatement personStmt = conn.prepareStatement(insertPersonSql)) {
					personStmt.setLong(1, personalId);
					personStmt.setString(2, visitor.getFirstName());
					personStmt.setString(3, visitor.getLastName());
					personStmt.setDate(4, Date.valueOf(visitor.getDOB()));
					personStmt.setString(5, visitor.getPhoneNumber());
					personStmt.executeUpdate();
				}
			}
		}

		String checkVisitorSql = "SELECT 1 FROM Visitor WHERE personal_id = ?";
		try (PreparedStatement checkVisitorStmt = conn.prepareStatement(checkVisitorSql)) {
			checkVisitorStmt.setLong(1, personalId);
			ResultSet rs = checkVisitorStmt.executeQuery();

			if (!rs.next()) {
				int employee_id = 3;
				if (zooFacade.getPasswordManager().getLoggedInEmployee() != null) {
					employee_id = zooFacade.getPasswordManager().getLoggedInEmployee().getEmployee_ID();
				}

				String insertVisitorSql = "INSERT INTO Visitor (personal_id, employee_id) VALUES (?, ?)";
				try (PreparedStatement visitorStmt = conn.prepareStatement(insertVisitorSql)) {
					visitorStmt.setLong(1, personalId);
					visitorStmt.setInt(2, employee_id);
					visitorStmt.executeUpdate();
				}
			}
		}
	}

	public int insertVisitorAndGetId(Visitor visitor) throws SQLException {
		insertPersonIfNotExists(visitor);

		String sql = "INSERT INTO Visitor (person_id, employee_id) VALUES (?, ?) RETURNING visitor_id";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setLong(1, visitor.getID());
			stmt.setInt(2, visitor.getRegistered_by_employee_id());
			/*
			 * if (visitor.getSubscription_id() != null) { stmt.setInt(3,
			 * visitor.getSubscription_id()); } else { stmt.setNull(3, Types.INTEGER); }
			 */
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("visitor_id");
			}
		}
		throw new SQLException("Failed to insert visitor.");
	}

	public void deleteVisitorFromDatabase(Visitor visitor) throws SQLException {
		int visitor_id = visitor.getVisitor_id();
		try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Visitor WHERE visitor_id = ?")) {
			stmt.setLong(1, visitor_id);
			stmt.executeUpdate();
		}
	}

	public List<Visitor> getAllVisitors() {
		List<Visitor> visitors = new ArrayList<>();
		String sql = """
				SELECT v.visitor_id, p.f_name, p.l_name, p.person_id, p.dob, p.phone_number, v.employee_id
				FROM Visitor v
				JOIN Person p ON v.person_id = p.person_id
				""";

		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				String fName = rs.getString("f_name");
				String lName = rs.getString("l_name");
				int visitorId = rs.getInt("visitor_id");
				int employeeId = rs.getInt("employee_id");
				long personId = rs.getLong("person_id");
				LocalDate dob = rs.getDate("dob").toLocalDate();
				String phone = rs.getString("phone_number");

				Visitor visitor = new Visitor(visitorId, fName, lName, personId, dob, phone, employeeId);
				visitors.add(visitor);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return visitors;
	}
}
