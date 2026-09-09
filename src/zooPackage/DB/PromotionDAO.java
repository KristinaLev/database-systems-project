package zooPackage.DB;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import zooPackage.Entities.Promotions.Promotion;

public class PromotionDAO {
	private final Connection conn;

	public PromotionDAO(Connection conn) {
		this.conn = conn;
	}

	public void insertPromotionToDatabase(Promotion promo) throws SQLException {
		int promo_id;
		String sql = "INSERT INTO Promotion (description, discount) VALUES (?, ?) RETURNING promotion_id";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, promo.getDescription());
			stmt.setBigDecimal(2, BigDecimal.valueOf(promo.getDiscountPercentage()));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				promo_id = rs.getInt("promotion_id");
				promo.setPromotionID(promo_id);
			} else {
				throw new SQLException("Failed to retrieve generated promotion_id.");
			}
		}
	}

	public void insertSubscriptionToDatabase(Promotion promo) throws SQLException {
		int promo_id;
		String sql = "INSERT INTO Subscription (name, type, price, date, time) "
				+ "VALUES (?, ?, ?, ?, ?) RETURNING subscription_id";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, promo.getDescription());
			stmt.setBigDecimal(2, BigDecimal.valueOf(promo.getDiscountPercentage()));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				promo_id = rs.getInt("promotion_id");
				promo.setPromotionID(promo_id);
			} else {
				throw new SQLException("Failed to retrieve generated promotion_id.");
			}
		}
	}

	public void insertSubscribtionsToDatabase(Promotion promo) throws SQLException {
		int promo_id;
		String sql = "INSERT INTO Subscription (description, discount) VALUES (?, ?) RETURNING promotion_id";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, promo.getDescription());
			stmt.setBigDecimal(2, BigDecimal.valueOf(promo.getDiscountPercentage()));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				promo_id = rs.getInt("promotion_id");
				promo.setPromotionID(promo_id);
			} else {
				throw new SQLException("Failed to retrieve generated promotion_id.");
			}
		}
	}

	public void updatePromotion(Promotion updatedPromotion) {
		String sql = "UPDATE Promotion SET description = ?, discount = ? WHERE promotion_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, updatedPromotion.getDescription());
			stmt.setInt(2, updatedPromotion.getDiscountPercentage());
			stmt.setInt(3, updatedPromotion.getPromotionID());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deletePromotionFromDatabase(Promotion promo) throws SQLException {
		int promo_id = promo.getPromotionID();
		try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Promotion WHERE promotion_id = ?")) {
			stmt.setInt(1, promo_id);
			stmt.executeUpdate();
		}
	}

	public List<Promotion> loadAllPromotionfromDB() {
		List<Promotion> promotions = new ArrayList<>();

		String sql = "SELECT * FROM Promotion";

		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				int promo_id = rs.getInt("promotion_id");
				String description = rs.getString("description");
				int discount = rs.getInt("discount");
				Promotion promo = new Promotion(promo_id, description, discount);
				promotions.add(promo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return promotions;
	}
}
