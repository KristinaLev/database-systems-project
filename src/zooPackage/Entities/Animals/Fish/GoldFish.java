package zooPackage.Entities.Animals.Fish;

import zooPackage.Enum.Colors;
import zooPackage.Enum.Pattern;

public class GoldFish extends Fish {

	public final static int LIFE_SPAN = 12;
	
	public GoldFish(int animal_id, int age, double length, Colors color, int happiness) {
		super(animal_id, age, length, Pattern.SMOOTH, color, happiness);
	}

	@Override
	public int getLifeSpan() {
		return LIFE_SPAN;
	}

	@Override
	public double feed() {
		return 1;
	}
}
