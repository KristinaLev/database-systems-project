package zooPackage.Entities.Promotions;

public class Promotion {
	private String description;
	private int discountPercentage;
	protected int promotion_id;

	public Promotion(int promotion_id, String description, int discountPercentage) {
		this.promotion_id = promotion_id;
		this.description = description;
		this.discountPercentage = discountPercentage;
	}

	public int getPromotionID() {
		return promotion_id;
	}

	public void setPromotionID(int promotion_id) {
		this.promotion_id = promotion_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(int discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	@Override
	public String toString() {
		return "Promotion: [ " + description + "  -  " + discountPercentage + "% ]";
	}
}
