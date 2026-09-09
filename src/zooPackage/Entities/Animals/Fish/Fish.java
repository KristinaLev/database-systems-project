package zooPackage.Entities.Animals.Fish;

import java.util.Arrays;

import zooPackage.Entities.Animals.Animal;
import zooPackage.Enum.Colors;
import zooPackage.Enum.Pattern;

public abstract class Fish extends Animal {

	public final static int DEFAULT_NUM_OF_FISH = 10;

	protected double length;
	protected Pattern pattern;
	protected Colors[] colors;

	public Fish(int animal_id, int age, double length, Pattern pattern, Colors[] colors, int happiness) {
		super(animal_id, age, happiness);
		this.length = length;
		this.pattern = pattern;
		this.colors = colors;
	}

	public Fish(int animal_id, int age, double length, Pattern pattern, Colors color, int happiness) {
		this(animal_id, age, length, pattern, new Colors[] { color }, happiness);
	}

	public abstract int getLifeSpan();

	public String makeNoise() {
		return "blob";
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public Colors[] getColor() {
		return colors;
	}

	public void setColor(Colors[] colors) {
		this.colors = colors;
	}

	@Override
	public int getDefaultNumberOfAnimals() {
		return getDEFAULT_NUM_OF_FISH();
	}

	@Override
	public String toString() {
		return String.format("%s - [age: %d | length: %.2f | pattern: %s | colors: %s]  -  Happines: %d",
				getClass().getSimpleName(), age, length, pattern, Arrays.toString(colors), happiness);
	}

	public int getDEFAULT_NUM_OF_FISH() {
		return DEFAULT_NUM_OF_FISH;
	}
}
