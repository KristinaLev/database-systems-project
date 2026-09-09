package zooPackage.Entities.Subscriptions;

import java.util.Objects;

import zooPackage.Enum.SubscriptionType;

public class Subscription {

	private int subscription_id;
	private SubscriptionType type;
	private String name;
	private double price;

	public Subscription(int subscription_id, SubscriptionType type, String name, double price) {
		this.subscription_id = subscription_id;
		this.type = type;
		this.name = name;
		this.price = price;
	}

	public int getSubscription_id() {
		return subscription_id;
	}

	public void setSubscription_id(int subscription_id) {
		this.subscription_id = subscription_id;
	}

	public SubscriptionType getType() {
		return type;
	}

	public void setType(SubscriptionType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Subscription that = (Subscription) obj;
		return Double.compare(that.price, price) == 0 && Objects.equals(type, that.type)
				&& Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, name, price);
	}

	public String toString() {
		return "Subscription: [ " + subscription_id + ". " + name + " | " + price + "% ]";
	}
}
