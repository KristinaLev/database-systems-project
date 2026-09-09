package zooPackage.Interface;

import java.text.NumberFormat;
import java.util.List;
import zooPackage.Entities.Animals.AnimalRecord;
import zooPackage.Entities.Animals.Animal;
import zooPackage.Entities.Animals.Bird.Bird;
import zooPackage.Entities.Animals.Fish.Fish;
import zooPackage.Entities.Animals.Herbivore.Herbivore;
import zooPackage.Entities.Animals.Insects.Insects;
import zooPackage.Entities.Animals.Predator.Predator;
import zooPackage.Entities.Animals.Reptile.Reptile;
import zooPackage.Enum.BirdsName;
import zooPackage.Enum.Colors;
import zooPackage.Enum.HerbivoreName;
import zooPackage.Enum.Pattern;
import zooPackage.Enum.PredatorsName;
import zooPackage.Enum.ReptilesName;
import zooPackage.Enum.Sex;
import zooPackage.Enum.StatusCheck;

public interface MainManager_Interface {

	// General
	public void addAnimal(Animal animal);

	public int countAnimalsByType(Class<?> type);

	public String feedAllAnimals();

	public List<Animal> removeUnhappyOrOldAnimals(List<Animal> animals);

	public void ageOneYear();

	public void setDefaultHappinessAfterFeeding();

	public List<AnimalRecord> createDefaultAnimals();

	public String dominantColors();

	public Sex randomAnimalSex();

	public StatusCheck randomVenomousStatus();

	public int getRandomHappines();

	public int decreaseRandomHappines();

	// Fish
	public Pattern randomFishPattern();

	public Colors randomFishColor();

	public double randomFishLength();

	public int randomFishAge(int lifeSpan);

	// Birds
	public void sortPenguins(int choice);

	public BirdsName randomBirdName();

	public int randomPenguinAge();

	public int randomParrotAge();

	public double randomPenguinHeight();

	public double randomParrotHeight();

	// Insects
	public StatusCheck randomSpiderManStatus();

	public int randomBeeAge();

	public int randomSpiderAge();

	public Colors randomSpiderColor();

	// Herbivore
	public HerbivoreName randomHerbivoreName();

	public int randomZebraAge();

	public int randomElephantAge();

	public double randomZebraWeight();

	public double randomElephantWeight();

	public double randomZebraHeight();

	public double randomElephantHeight();

	// Predator
	public PredatorsName randomPredatorName();

	public int randomPredatorAge();

	public double randomPredatorWeight();

	// Reptile
	public ReptilesName randomReptileName();

	public StatusCheck randomNinjaStatus();

	public Colors randomReptileColor();

	public int randomSnakeAge();

	public int randomTurtleAge();

	public double randomSnakeWeight();

	public double randomTurtleWeight();

	public double randomSnakeLength();

	public double randomTurtleLength();

	// General getters/setters
	public String getZooName();

	public String getZooAddress();

	public NumberFormat getFormatter();

	public int getMinAge();

	public List<Animal> getAnimals();

	// Bird getters/setters
	public List<Bird> getBirds();

	public void setBirds(List<Bird> bird);

	public String getMainPenguinName();

	public int getMainPenguinAge();

	public double getMainPenguinHeight();

	public double getMaxPenguinHeight();

	public double getMinPenguinHeight();

	public double getMinParrotHeight();

	public double getMaxParrotHeight();

	// Insects getters/setters
	public List<Insects> getInsect();

	public void setInsect(List<Insects> insect);

	// Herbivore getters/stters
	public List<Herbivore> getHerbivore();

	public void setHerbivore(List<Herbivore> herbivore);

	public double getMinHerbivoreWeight();

	public double getMinHerbivoreHeight();

	public double getMaxElephantWeight();

	public double getMaxZebraWeight();

	public double getMaxElephantHeight();

	public double getMaxZebraHeight();

	// Predator getters/setters
	public List<Predator> getPredators();

	public void setPredators(List<Predator> predator);

	public double getMaxPredatorWeight();

	public double getMinPredatorWeight();

	// Reptile getters/setters
	public double getMaxSnakeLength();

	public double getMaxSnakeWeight();

	public double getMinSnakeWeight();

	public double getMinSnakeLength();

	public double getMaxTurtleLength();

	public double getMaxTurtleWeight();

	public double getMinTurtleWeight();

	public double getMinTurtleLength();

	public List<Reptile> getReptile();

	public void setReptile(List<Reptile> reptile);

	// Fish getters/setters
	public void setFish(List<Fish> newFish);

	public List<Fish> getFish();

	public double getMaxFishLength();

	public double getMinFishLength();

}
