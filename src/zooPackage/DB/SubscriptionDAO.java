package zooPackage.DB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import zooPackage.Entities.Promotions.Promotion;
import zooPackage.Entities.Subscriptions.Subscription;
import zooPackage.Entities.Users.Visitor;
import zooPackage.Enum.SubscriptionType;
import zooPackage.Management.ZooFacade;

public class SubscriptionDAO {
	private final Connection conn;
	ZooFacade zooFacade = ZooFacade.getInstance();

	public SubscriptionDAO(Connection conn) {
		this.conn = conn;
	}

	public int insertSubscriptionAndGetId(Subscription sub) throws SQLException {
		String selectSQL = "SELECT subscription_id FROM Subscription WHERE type = ? AND name = ? AND price = ?";
		try (PreparedStatement selectStmt = conn.prepareStatement(selectSQL)) {
			selectStmt.setString(1, "" + sub.getType());
			selectStmt.setString(2, sub.getName());
			selectStmt.setDouble(3, sub.getPrice());
			ResultSet rs = selectStmt.executeQuery();
			if (rs.next()) {
				// Subscription already exists
				return rs.getInt("subscription_id");
			}
		}
		// Insert if not exists
		String sql = "INSERT INTO Subscription (type, name, price) " + "VALUES (?, ?, ?) RETURNING subscription_id";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, "" + sub.getType());
			stmt.setString(2, sub.getName());
			stmt.setDouble(3, sub.getPrice());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("subscription_id");
			}
		}
		throw new SQLException("Failed to insert subscription.");
	}

	public void insertRelationSubscriberAndPromotion(Visitor visitor_id, int promo_id) {
		String sql = "INSERT INTO Subscriber_get_Promotion (subscriber_id, promotion_id, assigned_date, assigned_time) "
				+ "VALUES (?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setLong(1, visitor_id.getVisitor_id());
			stmt.setInt(2, promo_id);
			stmt.setDate(3, Date.valueOf(LocalDate.now()));
			stmt.setTime(4, Time.valueOf(LocalTime.now()));
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int insertSubscriber(Visitor subscriber, int subscription_id) throws SQLException {
		zooFacade.getSubscriptionManager().addSubscriber(subscriber);
		Date subscription_date = Date.valueOf(LocalDate.now());
		Time subscription_time = Time.valueOf(LocalTime.now());
		Date exp_date = Date.valueOf(LocalDate.now().plusYears(1));

		String sql = "INSERT INTO Subscriber (subscriber_id, subscription_id, subscription_time, subscription_date, exp_date)"
				+ " VALUES (?, ?, ?, ?, ?) RETURNING subscription_id";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, subscriber.getVisitor_id());
			stmt.setInt(2, subscription_id);
			stmt.setTime(3, subscription_time);
			stmt.setDate(4, subscription_date);
			stmt.setDate(5, exp_date);

			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				return rs.getInt("subscription_id");
			else
				throw new SQLException("Failed to insert subscriber.");
		}
	}

	public void deleteSubscriberFromDatabase(Visitor subscriber) throws SQLException {
		int subscriber_id = subscriber.getVisitor_id();

		try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Subscriber WHERE subscriber_id = ?")) {
			stmt.setInt(1, subscriber_id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Subscription> getAllSubscriptions() {
		List<Subscription> subscriptions = new ArrayList<>();

		String sql = "SELECT * FROM Subscription";

		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				int subscription_id = rs.getInt("subscription_id");
				SubscriptionType type = SubscriptionType.valueOf(rs.getString("type"));
				String name = rs.getString("name");
				double price = rs.getDouble("price");

				Subscription subscription = new Subscription(subscription_id, type, name, price);
				subscriptions.add(subscription);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return subscriptions;
	}

	public List<Visitor> getAllSubscribers() {
		List<Visitor> subscribers = new ArrayList<>();

		String sql = "SELECT v.visitor_id, v.employee_id, p.* FROM Visitor v JOIN Person p ON p.person_id = v.person_id "
				+ "JOIN Subscriber s ON s.subscriber_id = v.visitor_id";

		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				String fName = rs.getString("f_name");
				String lName = rs.getString("l_name");
				int visitorId = rs.getInt("visitor_id");
				int employeeId = rs.getInt("employee_id");
				long personId = rs.getLong("person_id");
				LocalDate dob = rs.getDate("dob").toLocalDate();
				String phone = rs.getString("phone_number");

				Visitor subscriber = new Visitor(visitorId, fName, lName, personId, dob, phone, employeeId);
				subscribers.add(subscriber);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return subscribers;
	}

	public HashMap<Integer, List<Promotion>> getAllSubscriberPromotions() {
		HashMap<Integer, List<Promotion>> subscriberPromotions = new HashMap<>();

		String sql = "SELECT sp.promotion_id, sp.subscriber_id, p.description, p.discount "
				+ "FROM Subscriber_get_Promotion sp JOIN Promotion p ON p.promotion_id = sp.promotion_id;";

		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				int promotion_id = rs.getInt("promotion_id");
				int subscriber_id = rs.getInt("subscriber_id");
				String description = rs.getString("description");
				int discount = rs.getInt("discount");

				Promotion promo = new Promotion(promotion_id, description, discount);

				subscriberPromotions.computeIfAbsent(subscriber_id, k -> new ArrayList<>()).add(promo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return subscriberPromotions;
	}

	public HashMap<Integer, Subscription> getAllSubscriberSubscription() {
		HashMap<Integer, Subscription> subscriberSubscription = new HashMap<>();

		String sql = "SELECT sb.*, s.subscriber_id FROM Subscription sb "
				+ "JOIN Subscriber s ON sb.subscription_id = s.subscription_id";

		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				int subscription_id = rs.getInt("subscription_id");
				int subscriber_id = rs.getInt("subscriber_id");
				SubscriptionType type = SubscriptionType.valueOf(rs.getString("type"));
				String name = rs.getString("name");
				double price = rs.getDouble("price");

				Subscription sub = new Subscription(subscription_id, type, name, price);

				subscriberSubscription.putIfAbsent(subscriber_id, sub);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return subscriberSubscription;
	}

}
