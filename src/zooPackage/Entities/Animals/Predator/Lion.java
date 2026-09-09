package zooPackage.Entities.Animals.Predator;

import zooPackage.Enum.PredatorsName;
import zooPackage.Enum.Sex;

public class Lion extends Predator {

	protected final static int LIFE_SPAN = 15;

	public Lion(int animal_id, PredatorsName name, int age, double weight, Sex sex, int happiness) {
		super(animal_id, name, age, weight, sex, happiness);
	}

	public Lion(int animal_id, String name, int age, double weight, Sex sex, int happiness) {
		super(animal_id, name, age, weight, sex, happiness);
	}

	@Override
	public String makeNoise() {
		return "ROAR";
	}

	@Override
	public int getLifeSpan() {
		return getLIFE_SPAN();
	}

	@Override
	public double feed() {
		if (this.getSex() == Sex.MALE) {
			return Math.min(this.getAge() * this.getWeight() * 0.02, 25);
		} else {
			return Math.min(this.getAge() * this.getWeight() * 0.03, 25);
		}
	}

	public static int getLIFE_SPAN() {
		return LIFE_SPAN;
	}

}
