package zooPackage.Entities.Animals.Reptile;

import zooPackage.Enum.Colors;
import zooPackage.Enum.ReptilesName;
import zooPackage.Enum.StatusCheck;

public class Turtle extends Reptile {

	public static final int LIFE_SPAN = 120;

	public Turtle(int animal_id, ReptilesName randomName, int age, double weight, double length, StatusCheck venomous,
			Colors[] color, StatusCheck ninja, int happiness) {
		super(animal_id, randomName, age, weight, length, StatusCheck.NO, color, ninja, happiness);
	}

	public Turtle(int animal_id, String name, int age, double weight, double length, StatusCheck venomous,
			Colors[] color, StatusCheck ninja, int happiness) {
		super(animal_id, name, age, weight, length, StatusCheck.NO, color, ninja, happiness);
	}

	@Override
	public String makeNoise() {
		return "turtturt";
	}

	@Override
	public int getLifeSpan() {
		return LIFE_SPAN;
	}

	@Override
	public double feed() {
		return 2;
	}

}
