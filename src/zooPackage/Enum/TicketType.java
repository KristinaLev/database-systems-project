package zooPackage.Enum;

public enum TicketType {

	ADULT("Adult", 35, 200), KID("Kid", 15, 50), POLICEMAN("Policeman", 25, 150), STUDENT("Student", 25, 120),
	SOLDIER("Soldier", 25, 120), VETERAN("Veteran", 20, 100), DISABLED("Disabled", 10, 100), COUPLE("Couple", 55, 400),
	COUPLE_PLUS_ONE("Couple + 1", 60, 440), COUPLE_PLUS_TWO("Couple + 2", 65, 480),
	COUPLE_PLUS_THREE("Couple + 3", 70, 500), COUPLE_PLUS_FOUR("Couple + 4", 75, 520),
	COUPLE_PLUS_FIVE("Couple + 5", 80, 550), COUPLE_PLUS_SIX_OR_MORE("Couple + 6 or more", 100, 600),
	PARENT_PLUS_ONE("Parent + 1", 45, 300), PARENT_PLUS_TWO("Parent + 2", 55, 320),
	PARENT_PLUS_THREE("Parent + 3", 60, 330), PARENT_PLUS_FOUR("Parent + 4", 65, 350),
	PARENT_PLUS_FIVE("Parent + 5", 70, 370), PARENT_PLUS_SIX_OR_MORE("Parent + 6 or more", 80, 400),
	PROMOTION("For promotion subscriber", 0, 250);
	;

	private final String typeName;
	private final int singlePrice;
	private final int annualPrice;

	TicketType(String typeName, int singlePrice, int annualPrice) {
		this.typeName = typeName;
		this.singlePrice = singlePrice;
		this.annualPrice = annualPrice;
	}

	public String getTypeName() {
		return typeName;
	}

	public int getSinglePrice() {
		return singlePrice;
	}

	public int getAnnualPrice() {
		return annualPrice;
	}

}
