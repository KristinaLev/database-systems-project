package zooPackage.Entities.Animals.Predator;

import zooPackage.Entities.Animals.Animal;
import zooPackage.Enum.PredatorsName;
import zooPackage.Enum.Sex;

public abstract class Predator extends Animal {

	private static final int DEFAULT_NUM_OF_PREDATORS = 4;

	protected PredatorsName randomName;
	protected String name;
	protected double weight;
	protected Sex sex;

	public Predator(int animal_id, PredatorsName randomName, int age, double weight, Sex sex, int happiness) {
		super(animal_id, age, happiness);
		this.randomName = randomName;
		this.weight = weight;
		this.sex = sex;
	}

	public Predator(int animal_id, String name, int age, double weight, Sex sex, int happiness) {
		super(animal_id, age, happiness);
		this.name = name;
		this.weight = weight;
		this.sex = sex;
	}

	public abstract int getLifeSpan();

	public abstract String makeNoise();

	public abstract double feed();

	public PredatorsName getRandomName() {
		return randomName;
	}

	public void setRandomName(PredatorsName name) {
		this.randomName = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	@Override
	public int getDefaultNumberOfAnimals() {
		return getDefaultNumOfPredators();
	}

	@Override
	public String toString() {
		String displayName = (name != null) ? name : (randomName != null ? randomName.toString() : "Unknown");
		return String.format("%s - [Name: %s | Age: %d | Weight: %.2f | Gender: %s]  -  Happines: %d ",
				getClass().getSimpleName(), displayName, age, weight, sex, happiness);
	}

	public static int getDefaultNumOfPredators() {
		return DEFAULT_NUM_OF_PREDATORS;
	}
}
