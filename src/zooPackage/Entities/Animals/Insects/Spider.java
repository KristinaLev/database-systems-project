package zooPackage.Entities.Animals.Insects;

import java.util.Arrays;
import zooPackage.Enum.Colors;
import zooPackage.Enum.Pattern;
import zooPackage.Enum.StatusCheck;

public class Spider extends Insects {

	protected final static int LIFE_SPAN = 12;

	public Spider(int animal_id, int age, StatusCheck venomous, Colors[] color, Pattern pattern, StatusCheck SpiderMan, int happiness) {
		super(animal_id, age, venomous, color, pattern, SpiderMan, happiness);
	}

	@Override
	public String makeNoise() {
		return "Spider";
	}

	@Override
	public int getLifeSpan() {
		return getLIFE_SPAN();
	}

	@Override
	public double feed() {
		return 10;
	}

	@Override
	public String toString() {
		return String.format(
				"%s - [Age: %d | Venomous: %s | Colors: %s | Pattern %s | SpiderMan: %s]  -  Happines: %d ",
				getClass().getSimpleName(), age, venomous, Arrays.toString(color), pattern, SpiderMan, happiness);
	}

	public static int getLIFE_SPAN() {
		return LIFE_SPAN;
	}
}
