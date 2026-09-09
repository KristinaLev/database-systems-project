package zooPackage.Entities.Animals.Herbivore;

import zooPackage.Enum.HerbivoreName;
import zooPackage.Enum.Pattern;
import zooPackage.Enum.Sex;

public class Zebra extends Herbivore {

	private final static int LIFE_SPAN = 16;

	public Zebra(int animal_id, HerbivoreName randomName, int age, double weight, double height, Pattern pattern, Sex sex,
			int happiness) {
		super(animal_id, randomName, age, weight, height, Pattern.LINES, sex, happiness);
	}

	public Zebra(int animal_id, String name, int age, double weight, double height, Pattern pattern, Sex sex, int happiness) {
		super(animal_id, name, age, weight, height, Pattern.LINES, sex, happiness);
	}

	@Override
	public String makeNoise() {
		return "zebra";
	}

	@Override
	public int getLifeSpan() {
		return getLIFE_SPAN();
	}

	@Override
	public double feed() {
		if (this.getSex() == Sex.MALE) {
			return (this.getAge() * this.getWeight() * 0.015);
		} else {
			return (this.getAge() * this.getWeight() * 0.02);
		}
	}

	public static int getLIFE_SPAN() {
		return LIFE_SPAN;
	}

}
