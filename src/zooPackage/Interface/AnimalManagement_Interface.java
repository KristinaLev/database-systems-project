package zooPackage.Interface;

import zooPackage.Entities.Animals.Bird.Bird;
import zooPackage.Entities.Animals.Herbivore.Herbivore;
import zooPackage.Entities.Animals.Insects.Insects;
import zooPackage.Entities.Animals.Predator.Predator;
import zooPackage.Entities.Animals.Reptile.Reptile;

public interface AnimalManagement_Interface {

	public void deathNote();

	// Adding methods
	public void addAnimalMenu();

	public void addFish();

	public void addRandomFish(int numberOfFish);

	public void addBird(Class<? extends Bird> birdClass);

	public void addInsect(Class<? extends Insects> insectClass);

	public void addHerbivore(Class<? extends Herbivore> herbivoreClass);

	public void addPredator(Class<? extends Predator> predatorClass);

	public void addReptile(Class<? extends Reptile> reptileClass);

}
