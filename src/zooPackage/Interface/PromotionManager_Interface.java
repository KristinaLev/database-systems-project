package zooPackage.Interface;

import java.util.List;
import java.util.Map;
import zooPackage.Entities.Promotions.Promotion;

public interface PromotionManager_Interface {

	public void createDefaultPromotions();

	public void createNewPromotion();

	public void addPromotion(Promotion promotion);

	public void updatePromotion(Promotion updatedPromotion, String description, int percentage);

	public void removePromotion(Promotion promo);

	public void sendPromotions();

	//public void notifySubscriber(Long subscriberID);

	public void unsubscribeFromPromotion();

	//public boolean subscribe(Visitor visitor);

	public List<Promotion> getPromotions();
	
	/*
	 * public void unsubscribe(Visitor visitor);
	 * 
	 * public void addSubscribersToNotify(List<Long> subscribers);
	 * 
	 * public void removeSubscriberFromNotifyList(Long subscriber);
	 * 
	 * public void addSubscribersToUpdateNotify(List<Long> subscribers);
	 * 
	 * public void removeSubscriberFromUpdateNotifyList(Long subscriber);
	 * 
	 * 
	 * public List<Visitor> getSubscribers();
	 * 
	 * public List<Long> getSubscribersToNotify();
	 * 
	 * public List<Long> getSubscribersToNotifyOnUpdates();
	 */
	public List<Promotion> getUpdatedPromotions();

	public Map<String, Promotion> getUpdatedPromotionsMap();

}
