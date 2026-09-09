package zooPackage.DB;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import zooPackage.Entities.Animals.AnimalRecord;
import zooPackage.Entities.Users.Employee;
import zooPackage.Management.ZooFacade;

public class EmployeeDAO {
	private final Connection conn;

	public EmployeeDAO(Connection conn) {
		this.conn = conn;
	}

	private ZooFacade zooFacade = ZooFacade.getInstance();

	public void insertPersonIfNotExists(Employee employee) throws SQLException {
		String checkSql = "SELECT 1 FROM Person WHERE person_id = ?";
		try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
			checkStmt.setLong(1, employee.getID());
			ResultSet rs = checkStmt.executeQuery();
			if (rs.next())
				return;

			String insertSql = "INSERT INTO Person (person_id, f_name, l_name, dob, phone_number) VALUES (?, ?, ?, ?, ?)";
			try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
				insertStmt.setLong(1, employee.getID());
				insertStmt.setString(2, employee.getFirstName());
				insertStmt.setString(3, employee.getLastName());
				insertStmt.setDate(4, Date.valueOf(employee.getDOB()));
				insertStmt.setString(5, employee.getPhoneNumber());
				insertStmt.executeUpdate();
			}
		}
	}

	public int insertEmployeeAndGetId(Employee employee) throws SQLException {
		insertPersonIfNotExists(employee);

		String sql = "INSERT INTO Employee (person_id, username, password_hash) VALUES (?, ?, ?) RETURNING employee_id";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setLong(1, employee.getID());
			stmt.setString(2, employee.getUserName());
			stmt.setString(3, employee.getPasswordHash());

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("employee_id");
			}
		}
		throw new SQLException("Failed to insert employee.");
	}

	public void employeeCareForAnimal() throws SQLException {
		Employee employee = zooFacade.getPasswordManager().getLoggedInEmployee();
		AnimalDAO dao = new AnimalDAO(conn);
		List<AnimalRecord> allRecords = dao.getAllAnimalRecords();
		String sql = "INSERT INTO Employee_Care_For_Animal (employee_id, animal_id, food_amount, care_date, care_time) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			for (AnimalRecord record : allRecords) {
				try {
					stmt.setInt(1, employee.getEmployee_ID());
					stmt.setInt(2, record.getAnimal().getAnimalId());
					stmt.setBigDecimal(3, BigDecimal.valueOf(record.getAnimal().feed()));
					stmt.setDate(4, Date.valueOf(LocalDate.now()));
					stmt.setTime(5, Time.valueOf(LocalTime.now()));
					stmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void deleteEmployeeFromDatabase(Employee employee) throws SQLException {
		int employee_id = employee.getEmployee_ID();
		try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Employee WHERE employee_id = ?")) {
			stmt.setInt(1, employee_id);
			stmt.executeUpdate();
		}
	}

	public List<Employee> getAllEmployees() {
		List<Employee> employees = new ArrayList<>();

		String sql = """
					SELECT e.employee_id, e.username, e.password_hash, p.*
					FROM Employee e
					JOIN Person p ON e.person_id = p.person_id
				""";

		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				int employee_id = rs.getInt("employee_id");
				String userName = rs.getString("username");
				String passwordHash = rs.getString("password_hash");
				String f_name = rs.getString("f_name");
				String l_name = rs.getString("l_name");
				long id = rs.getLong("person_id");
				LocalDate dob = rs.getDate("dob").toLocalDate();
				String phoneNum = rs.getString("phone_number");

				Employee employee = new Employee(employee_id, userName, f_name, l_name, id, dob, phoneNum);
				employee.setPasswordHash(passwordHash);
				employees.add(employee);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return employees;
	}
}
