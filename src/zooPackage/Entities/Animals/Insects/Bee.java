package zooPackage.Entities.Animals.Insects;

import java.util.Arrays;

import zooPackage.Enum.Colors;
import zooPackage.Enum.Pattern;
import zooPackage.Enum.StatusCheck;

public class Bee extends Insects {

	private final static int LIFE_SPAN = 4;

	public Bee(int animal_id, int age, StatusCheck venomous, Colors[] color, Pattern pattern, StatusCheck SpiderMan, int happiness) {
		super(animal_id, age, venomous, new Colors[] { Colors.BLACK, Colors.YELLOW }, Pattern.LINES, StatusCheck.NO, happiness);
	}

	@Override
	public String makeNoise() {
		return "BZZZ";
	}

	@Override
	public int getLifeSpan() {
		return getLIFE_SPAN();
	}

	@Override
	public double feed() {
		return 200;
	}

	@Override
	public String toString() {
		return String.format("%s - [Age: %d | Venomous: %s | Pattern: %s | Colors: %s]  -  Happines: %d ",
				getClass().getSimpleName(), age, venomous, pattern, Arrays.toString(color), happiness);
	}

	public static int getLIFE_SPAN() {
		return LIFE_SPAN;
	}
}
