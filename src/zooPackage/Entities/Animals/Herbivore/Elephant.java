package zooPackage.Entities.Animals.Herbivore;

import zooPackage.Enum.HerbivoreName;
import zooPackage.Enum.Pattern;
import zooPackage.Enum.Sex;

public class Elephant extends Herbivore {

	public static final int LIFE_SPAN = 80;

	public Elephant(int animal_id, HerbivoreName randomName, int age, double weight, double height, Pattern pattern,
			Sex sex, int happiness) {
		super(animal_id, randomName, age, weight, height, Pattern.SMOOTH, sex, happiness);
	}

	public Elephant(int animal_id, String name, int age, double weight, double height, Pattern pattern, Sex sex,
			int happiness) {
		super(animal_id, name, age, weight, height, Pattern.SMOOTH, sex, happiness);
	}

	@Override
	public String makeNoise() {
		return "Wooooo";
	}

	@Override
	public int getLifeSpan() {
		return LIFE_SPAN;
	}

	@Override
	public double feed() {
		if (this.getSex() == Sex.MALE) {
			return 100;
		} else {
			return 120;
		}
	}

}
