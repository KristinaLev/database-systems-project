package zooPackage.Entities.Animals.Predator;

import zooPackage.Enum.PredatorsName;
import zooPackage.Enum.Sex;

public class Tiger extends Predator {

	protected static final int LIFE_SPAN = 15;

	public Tiger(int animal_id, String name, int age, double weight, Sex sex, int happiness) {
		super(animal_id, name, age, weight, sex, happiness);
	}

	public Tiger(int animal_id, PredatorsName name, int age, double weight, Sex sex, int happiness) {
		super(animal_id, name, age, weight, sex, happiness);
	}

	@Override
	public String makeNoise() {
		return "roar";
	}

	@Override
	public int getLifeSpan() {
		return LIFE_SPAN;
	}

	@Override
	public double feed() {

		if (this.getSex() == Sex.MALE) {
			return (this.getAge() * this.getWeight() * 0.02);
		} else {
			return (this.getAge() * this.getWeight() * 0.03);
		}
	}

}
