package zooPackage.Entities.Animals;

public class AnimalRecord {
	private final String type;
	private final String species;
	private final Animal animal;

	public AnimalRecord(String type, String species, Animal animal) {
		this.type = type;
		this.species = species;
		this.animal = animal;
	}

	public String getType() {
		return type;
	}

	public String getSpecies() {
		return species;
	}

	public Animal getAnimal() {
		return animal;
	}
}
