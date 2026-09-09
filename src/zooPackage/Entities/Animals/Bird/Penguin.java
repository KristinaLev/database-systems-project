package zooPackage.Entities.Animals.Bird;

import zooPackage.Enum.BirdsName;

public class Penguin extends Bird {

	public static final int LIFE_SPAN = 6;
	
	public Penguin(int animal_id, BirdsName randomName, int age, double height, int happiness) {
		super(animal_id, randomName, age, height, happiness);
	}

	public Penguin(int animal_id, String name, int age, double height, int happiness) {
		super(animal_id, name, age, height, happiness);
	}

	@Override
	public String makeNoise() {
		return "squack";
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
