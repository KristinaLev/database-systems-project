package zooPackage.Entities.Animals.Fish;

import zooPackage.Enum.Colors;
import zooPackage.Enum.Pattern;

public class AquariumFish extends Fish {

	public final static int LIFE_SPAN = 25;

	public AquariumFish(int animal_id, int age, double length, Pattern pattern, Colors[] color, int happiness) {
		super(animal_id, age, length, pattern, color, happiness);
	}

	@Override
	public int getLifeSpan() {
		return LIFE_SPAN;
	}

	@Override
	public double feed() {
		if (this.getAge() < 3) {
			return 3;
		} else {
			return (3 + this.getLength());
		}
	}
}
