package zooPackage.Entities.Animals.Bird;

import zooPackage.Enum.BirdsName;

public class Parrot extends Bird {

	public static final int LIFE_SPAN = 12;
	
	public Parrot(int animal_id, BirdsName randomName, int age, double height, int happiness) {
		super(animal_id, randomName, age, height, happiness);
	}

	public Parrot(int animal_id, String name, int age, double height, int happiness) {
		super(animal_id, name, age, height, happiness);
	}

	@Override
	public int getLifeSpan() {
		return LIFE_SPAN;
	}

	@Override
	public String makeNoise() {
		return "parror";
	}

	@Override
	public double feed() {
		return 2;
	}

}
