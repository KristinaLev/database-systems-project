package zooPackage.Entities.Animals;

public abstract class Animal {
    protected int animal_id;
	protected int age;
	protected int happiness;

	public Animal(int animal_id, int age, int happiness) {
		this.animal_id = animal_id;
		this.age = age;
		this.happiness = happiness;
	}

	public int getAnimalId() {
        return animal_id;
    }

    public void setAnimalId(int animal_id) {
        this.animal_id = animal_id;
    }
    
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getHappiness() {
		return happiness;
	}

	public void setHappiness(int happiness) {
		this.happiness = happiness;
	}

	public abstract String makeNoise();

	public abstract int getLifeSpan();

	protected abstract int getDefaultNumberOfAnimals();

	public abstract double feed();

}
