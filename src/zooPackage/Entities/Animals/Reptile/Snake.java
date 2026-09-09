package zooPackage.Entities.Animals.Reptile;

import zooPackage.Enum.Colors;
import zooPackage.Enum.ReptilesName;
import zooPackage.Enum.StatusCheck;

public class Snake extends Reptile {

	public static final int LIFE_SPAN = 22;

	public Snake(int animal_id, ReptilesName randomName, int age, double weight, double length, StatusCheck venomous, Colors[] color,
			StatusCheck ninja, int happiness) {
		super(animal_id, randomName, age, weight, length, venomous, color, StatusCheck.NO, happiness);
	}

	public Snake(int animal_id, String name, int age, double weight, double length, StatusCheck venomous,
			Colors[] color, StatusCheck ninja, int happiness) {
		super(animal_id, name, age, weight, length, venomous, color, StatusCheck.NO, happiness);
	}

	@Override
	public String makeNoise() {
		return "SSS";
	}

	@Override
	public int getLifeSpan() {
		return LIFE_SPAN;
	}

	@Override
	public double feed() {
		if (this.getLength() <= 2) {
			return 2;
		} else {
			return 2 + this.getLength();
		}
	}

}
