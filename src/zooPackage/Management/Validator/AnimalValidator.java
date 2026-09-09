package zooPackage.Management.Validator;

import zooPackage.Entities.Animals.Bird.Penguin;
import zooPackage.Entities.Animals.Fish.AquariumFish;
import zooPackage.Entities.Animals.Fish.ClownFish;
import zooPackage.Entities.Animals.Fish.GoldFish;
import zooPackage.Entities.Animals.Herbivore.Elephant;
import zooPackage.Entities.Animals.Herbivore.Zebra;
import zooPackage.Entities.Animals.Insects.Bee;
import zooPackage.Entities.Animals.Insects.Spider;
import zooPackage.Entities.Animals.Predator.Lion;
import zooPackage.Entities.Animals.Reptile.Snake;
import zooPackage.Entities.Animals.Reptile.Turtle;
import zooPackage.Exception.InvalidAgeException;
import zooPackage.Exception.InvalidHeightException;
import zooPackage.Exception.InvalidLeaderPenguinHeightException;
import zooPackage.Exception.InvalidLengthException;
import zooPackage.Exception.InvalidWeightException;
import zooPackage.Management.AnimalManager;
import zooPackage.Management.ZooFacade;

public class AnimalValidator {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static AnimalManager zoo = zooFacade.getAnimalManager();
	private static AnimalValidator instance;

	private AnimalValidator() {
	}

	public static AnimalValidator getInstance() {
		if (instance == null) {
			instance = new AnimalValidator();
		}
		return instance;
	}

	public void validateReptile(int age, double weight, double length, String animalType)
			throws InvalidAgeException, InvalidWeightException, InvalidLengthException {
		int maxLifeSpan;
		double maxWeight;
		double maxLenght;
		double minWeight;
		double minLength;

		if ("Snake".equals(animalType)) {
			maxLifeSpan = Snake.LIFE_SPAN;
			maxWeight = zoo.getMaxSnakeWeight();
			maxLenght = zoo.getMaxSnakeLength();
			minWeight = zoo.getMinSnakeWeight();
			minLength = zoo.getMinSnakeLength();

		} else if ("Turtle".equals(animalType)) {
			maxLifeSpan = Turtle.LIFE_SPAN;
			maxWeight = zoo.getMaxTurtleWeight();
			maxLenght = zoo.getMaxTurtleLength();
			minWeight = zoo.getMinTurtleWeight();
			minLength = zoo.getMinTurtleLength();
		} else {
			throw new IllegalArgumentException("Unknown animal type: " + animalType);
		}

		if (age < zoo.getMinAge() || age > maxLifeSpan) {
			throw new InvalidAgeException(
					"Age must be " + zoo.getMinAge() + " or greater and less than " + maxLifeSpan + " years.");
		}
		if (weight < minWeight || weight > maxWeight) {
			throw new InvalidWeightException(
					"Weight must be greater than " + minWeight + " and less than " + maxWeight + " kg.");
		}
		if (length < minLength || length > maxLenght) {
			throw new InvalidLengthException(
					"Length must be longer than " + minLength + " and less than " + maxLenght + " m.");
		}
	}

	public void validatePredator(int age, double weight, String animalType)
			throws InvalidAgeException, InvalidWeightException {
		int maxLifeSpan = Lion.getLIFE_SPAN();
		double maxWeight = zoo.getMaxPredatorWeight();

		if (age < zoo.getMinAge() || age > maxLifeSpan) {
			throw new InvalidAgeException(
					"Age must be greater than " + zoo.getMinAge() + " and less than " + maxLifeSpan + " years.");
		}
		if (weight < zoo.getMinPredatorWeight() || weight > maxWeight) {
			throw new InvalidWeightException("Weight must be greater than " + zoo.getMinHerbivoreHeight()
					+ " and less than " + maxWeight + " kg.");
		}
	}

	public void validateHerbivore(int age, double weight, double height, String animalType)
			throws InvalidAgeException, InvalidWeightException, InvalidHeightException {
		int maxLifeSpan;
		double maxWeight;
		double maxHeight;
		if ("Zebra".equals(animalType)) {
			maxLifeSpan = Zebra.getLIFE_SPAN();
			maxWeight = zoo.getMaxZebraWeight();
			maxHeight = zoo.getMaxZebraHeight();
		} else if ("Elephant".equals(animalType)) {
			maxLifeSpan = Elephant.LIFE_SPAN;
			maxWeight = zoo.getMaxElephantWeight();
			maxHeight = zoo.getMaxElephantHeight();
		} else {
			throw new IllegalArgumentException("Unknown animal type: " + animalType);
		}

		if (age < zoo.getMinAge() || age > maxLifeSpan) {
			throw new InvalidAgeException(
					"Age must be greater than " + zoo.getMinAge() + " and less than " + maxLifeSpan + " years.");
		}
		if (weight < zoo.getMinHerbivoreWeight() || weight > maxWeight) {
			throw new InvalidWeightException("Weight must be greater than " + zoo.getMinHerbivoreWeight()
					+ " and less than " + maxWeight + " kg.");
		}
		if (height < zoo.getMinHerbivoreHeight() || height > maxWeight) {
			throw new InvalidHeightException(
					"Height must be higher than " + zoo.getMinPenguinHeight() + " and less than " + maxHeight + " cm.");
		}
	}

	public void validateInsect(int age, String animalType) throws InvalidAgeException {
		int maxLifeSpan;

		if ("Bee".equals(animalType)) {
			maxLifeSpan = Bee.getLIFE_SPAN();

		} else if ("Spider".equals(animalType)) {
			maxLifeSpan = Spider.getLIFE_SPAN();

		} else {
			throw new IllegalArgumentException("Unknown animal type: " + animalType);
		}

		if (age < zoo.getMinAge() || age > maxLifeSpan) {
			throw new InvalidAgeException(
					"Age must be " + zoo.getMinAge() + " or greater and less than " + maxLifeSpan + " years.");
		}
	}

	public void validatePenguin(int age, double height)
			throws InvalidAgeException, InvalidHeightException, InvalidLeaderPenguinHeightException {
		if (height > zoo.getMainPenguinHeight()) {
			throw new InvalidLeaderPenguinHeightException(
					"This penguin is taller than the current leader {" + zoo.getMainPenguinHeight() + " cm}.");
		}
		if (age < zoo.getMinAge() || age > Penguin.LIFE_SPAN) {
			throw new InvalidAgeException(
					"Age must be greater than " + zoo.getMinAge() + " and less than " + Penguin.LIFE_SPAN + " years.");
		}
		if (height < zoo.getMinPenguinHeight() || height > zoo.getMainPenguinHeight()) {
			throw new InvalidHeightException("Height must be higher than " + zoo.getMinPenguinHeight()
					+ " and less than " + zoo.getMainPenguinHeight() + " cm.");
		}
	}

	public void validateFish(int age, double length, String animalType)
			throws InvalidAgeException, InvalidLengthException {
		int maxLifeSpan;
		double maxLength = zoo.getMaxFishLength();

		if ("AquariumFish".equals(animalType))
			maxLifeSpan = AquariumFish.LIFE_SPAN;
		else if ("GoldFish".equals(animalType))
			maxLifeSpan = GoldFish.LIFE_SPAN;

		else if ("ClownFish".equals(animalType)) {
			maxLifeSpan = ClownFish.LIFE_SPAN;

		} else {
			throw new IllegalArgumentException("Unknown fish type: " + animalType);
		}

		if (age < zoo.getMinAge() || age > maxLifeSpan) {
			throw new InvalidAgeException(
					"Age must be greater than " + zoo.getMinAge() + " and less than " + maxLifeSpan + " years.");
		}

		if (length < zoo.getMinFishLength() || length > maxLength) {
			throw new InvalidAgeException(
					"Length must be longer than " + zoo.getMinFishLength() + " and less than " + maxLength + " cm.");
		}
	}
}
