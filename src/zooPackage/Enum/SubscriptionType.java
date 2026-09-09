package zooPackage.Enum;

public enum SubscriptionType {

	SINGLE("Single subscription"), COUPLE("Couple subscription"), PARENT("Parent subscription"),
	FAMILY("Family subscription"), PROMOTION("Promotion subscription"), DEFAULT("Default subscription");

	private final String displayName;

	SubscriptionType(String typeName) {
		this.displayName = typeName;
	}

	public String getDisplayName() {
		return displayName;
	}

}
