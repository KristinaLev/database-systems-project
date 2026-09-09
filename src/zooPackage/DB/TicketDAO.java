package zooPackage.DB;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import zooPackage.Entities.Tickets.Ticket;
import zooPackage.Enum.StatusCheck;
import zooPackage.Enum.TicketType;

public class TicketDAO {
	private final Connection conn;

	public TicketDAO(Connection conn) {
		this.conn = conn;
	}

	public void insertTicketToDatabase(Ticket ticket) throws SQLException {

		String checkSql = "SELECT 1 FROM Ticket WHERE ticket_id = ?";

		try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
			checkStmt.setLong(1, ticket.getTicket_ID());
			ResultSet rs = checkStmt.executeQuery();
			if (rs.next()) {
				return;
			}
		}

		String sql = "INSERT INTO Ticket (visitor_id, employee_id, promotion_id, type, price, is_canceled, is_used, rls_date, exp_date, entry_date) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING ticket_id";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, ticket.getVisitor_ID());

			stmt.setInt(2, ticket.getEmployee_ID());

			Integer promoId = ticket.getPromotion_ID();
			if (promoId == null)
				stmt.setNull(3, Types.INTEGER);
			else
				stmt.setInt(3, promoId);

			stmt.setString(4, ticket.getTicketType().name());
			stmt.setBigDecimal(5, BigDecimal.valueOf(ticket.getTicketPrice()));
			stmt.setString(6, ticket.getIsCanceled().name());
			stmt.setString(7, ticket.getIsUsed().name());

			stmt.setDate(8, Date.valueOf(ticket.getReleaseDate()));
			stmt.setDate(9, Date.valueOf(ticket.getExpirationDate()));

			LocalDate entryDate = ticket.getEntryDate();
			if (entryDate != null)
				stmt.setDate(10, Date.valueOf(entryDate));
			else
				stmt.setNull(10, Types.DATE);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					int generatedId = rs.getInt("ticket_id");
					ticket.setTicket_ID(generatedId);
				} else {
					throw new SQLException("Failed to retrieve generated ticket_id.");
				}
			}
		}
	}

	public void updateTicketStatus(int ticket_id, String ticket_status) {
		String sql = "UPDATE Ticket SET is_used = ? WHERE ticket_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, ticket_status);
			stmt.setInt(2, ticket_id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateTicketCancelationStatus(int ticket_id, String ticket_status) {
		String sql = "UPDATE Ticket SET is_canceled = ? WHERE ticket_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, ticket_status);
			stmt.setInt(2, ticket_id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateTicketEntryDate(int ticket_id, Date entry_date) {
		String sql = "UPDATE Ticket SET entry_date = ? WHERE ticket_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setDate(1, entry_date);
			stmt.setInt(2, ticket_id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteTicketFromDatabase(Ticket ticket) throws SQLException {
		long ticket_id = ticket.getTicket_ID();
		try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Ticket WHERE ticket_id = ?")) {
			stmt.setLong(1, ticket_id);
			stmt.executeUpdate();
		}
	}

	public Ticket loadTicketFromDB_by_ID(int id) {
		Ticket ticket = null;
		String sql = "SELECT * FROM Ticket WHERE ticket_id = ?";

		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setLong(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					int ticket_id = rs.getInt("ticket_id");
					int visitor_id = rs.getInt("visitor_id");
					int employee_id = rs.getInt("employee_id");
					Integer promotion_id = rs.getObject("promotion_id") != null ? rs.getInt("promotion_id") : null;

					TicketType type = TicketType.valueOf(rs.getString("type"));
					StatusCheck can_be_canceled = StatusCheck.valueOf(rs.getString("is_canceled"));
					StatusCheck is_used = StatusCheck.valueOf(rs.getString("is_used"));

					double price = rs.getDouble("price");

					LocalDate releaseDate = rs.getDate("rls_date").toLocalDate();
					LocalDate expirationDate = rs.getDate("exp_date").toLocalDate();
					Date entryDateSQL = rs.getDate("entry_date");
					LocalDate entryDate = (entryDateSQL != null) ? entryDateSQL.toLocalDate() : null;

					ticket = new Ticket(ticket_id, employee_id, visitor_id, promotion_id, type, price, can_be_canceled,
							is_used, releaseDate, expirationDate, entryDate);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ticket;
	}

	public List<Ticket> loadAllTicketsFromDB() {
		List<Ticket> tickets = new ArrayList<>();
		String sql = "SELECT ticket_id FROM Ticket";

		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				int ticket_id = rs.getInt("ticket_id");
				Ticket ticket = loadTicketFromDB_by_ID(ticket_id);
				tickets.add(ticket);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tickets;
	}

}
