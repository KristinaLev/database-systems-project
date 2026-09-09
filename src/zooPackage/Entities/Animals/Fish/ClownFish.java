package zooPackage.Entities.Animals.Fish;

import zooPackage.Enum.Colors;
import zooPackage.Enum.Pattern;

public class ClownFish extends Fish {

	public final static int LIFE_SPAN = 8;
	
	public ClownFish(int animal_id, int age, double length, int happiness) {
		super(animal_id, age, length, Pattern.LINES, new Colors[] { Colors.BLACK, Colors.WHITE, Colors.ORANGE }, happiness);
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
