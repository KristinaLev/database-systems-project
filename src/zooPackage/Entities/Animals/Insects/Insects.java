package zooPackage.Entities.Animals.Insects;

import zooPackage.Entities.Animals.Animal;
import zooPackage.Enum.Colors;
import zooPackage.Enum.Pattern;
import zooPackage.Enum.StatusCheck;

public abstract class Insects extends Animal {

	private static final int DEFAULT_NUM_OF_INSECTS = 6;

	protected StatusCheck venomous;
	protected Colors[] color;
	protected Pattern pattern;
	protected StatusCheck SpiderMan;

	public Insects(int animal_id, int age, StatusCheck venomous, Colors[] color, Pattern pattern, StatusCheck SpiderMan,
			int happiness) {
		super(animal_id, age, happiness);
		this.venomous = venomous;
		this.color = color;
		this.pattern = pattern;
		this.SpiderMan = SpiderMan;
	}

	@Override
	public int getDefaultNumberOfAnimals() {
		return getDefaultNumOfInsects();
	}

	public abstract String makeNoise();

	public abstract int getLifeSpan();

	public abstract String toString();

	public StatusCheck isSpiderMan() {
		return SpiderMan;
	}

	public void setSpiderMan(StatusCheck spiderMan) {
		SpiderMan = spiderMan;
	}

	public Colors[] getColor() {
		return color;
	}

	public void setColor(Colors[] color) {
		this.color = color;
	}

	public StatusCheck isVenomous() {
		return venomous;
	}

	public void setVenomous(StatusCheck venomous) {
		this.venomous = venomous;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public static int getDefaultNumOfInsects() {
		return DEFAULT_NUM_OF_INSECTS;
	}

}
