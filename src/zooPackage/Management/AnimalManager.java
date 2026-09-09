package zooPackage.Management;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.JOptionPane;
import zooPackage.DB.AnimalDAO;
import zooPackage.DB.DBUtil;
import zooPackage.Entities.Animals.*;
import zooPackage.Entities.Animals.Bird.*;
import zooPackage.Entities.Animals.Fish.*;
import zooPackage.Entities.Animals.Herbivore.*;
import zooPackage.Entities.Animals.Insects.*;
import zooPackage.Entities.Animals.Predator.*;
import zooPackage.Entities.Animals.Reptile.*;
import zooPackage.Enum.*;
import zooPackage.Interface.MainManager_Interface;

public class AnimalManager implements MainManager_Interface {

	private static AnimalManager instance;

	private AnimalManager() {
	}

	public static AnimalManager getInstance() {
		if (instance == null) {
			instance = new AnimalManager();
		}
		return instance;
	}

	// Zoo Attributes
	private final String zooName = "Zoorama";
	private final String zooAddress = "24 Rothschild St. Tel Aviv, Israel";
	private final NumberFormat FORMATTER = new DecimalFormat("0.00");
	private final Random RANDOM = new Random();
	private final int MIN_AGE = 1;
	private static List<Animal> animals = new ArrayList<>();

	// Fish Attributes
	private final double MAX_FISH_LENGTH = 50.0;
	private final double MIN_FISH_LENGTH = 1.0;
	private List<Fish> fish = new ArrayList<>();

	// Bird Attributes
	private final double MAX_PENGUIN_HEIGHT = 199.99;
	private final double MIN_PENGUIN_HEIGHT = 10.0;
	private final double MIN_PARROT_HEIGHT = 2.0;
	private final double MAX_PARROT_HEIGHT = 50.0;
	private final String MAIN_PENGUIN_NAME = "Big Mama";
	private final int MAIN_PENGUIN_AGE = 3;
	private final double MAIN_PENGUIN_HEIGHT = 200.0;
	private List<Bird> birds = new ArrayList<>();

	// Insects Attributes
	private List<Insects> insects = new ArrayList<>();

	// Herbivore Attributes
	private final double MIN_HERBIVORE_WEIGHT = 100.0;
	private final double MIN_HERBIVORE_HEIGHT = 40.0;
	private final double MAX_ELEPHANT_WEIGHT = 1500.0;
	private final double MAX_ZEBRA_WEIGHT = 450.0;
	private final double MAX_ELEPHANT_HEIGHT = 1000.0;
	private final double MAX_ZEBRA_HEIGHT = 250.0;
	private List<Herbivore> herbivores = new ArrayList<>();

	// Predator Attributes
	private final double MAX_PREDATOR_WEIGHT = 500.0;
	private final double MIN_PREDATOR_WEIGHT = 25.0;
	private List<Predator> predators = new ArrayList<>();

	// Reptile Attributes
	private final double MAX_SNAKE_WEIGHT = 250.0;
	private final double MIN_SNAKE_WEIGHT = 10.0;
	private final double MAX_TURTLE_WEIGHT = 500.0;
	private final double MIN_TURTLE_WEIGHT = 5.0;
	private final double MAX_SNAKE_LENGTH = 10.0;
	private final double MIN_SNAKE_LENGTH = 0.2;
	private final double MAX_TURTLE_LENGTH = 1.5;
	private final double MIN_TURTLE_LENGTH = 0.1;
	private List<Reptile> reptiles = new ArrayList<>();

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                         GENERAL Methods                                                                 //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void addAnimal(Animal animal) {
		getAnimals().add(animal);
	}

	public int countAnimalsByType(Class<?> type) {
		int count = 0;
		for (Animal animal : getAnimals()) {
			if (type.isInstance(animal)) {
				count++;
			}
		}
		return count;
	}

	@Override
	public String feedAllAnimals() {

		Map<String, Double> foodConsumptionByType = new LinkedHashMap<>();
		Map<String, List<String>> categories = new LinkedHashMap<>();
		categories.put("Predators", Arrays.asList("Lion", "Tiger"));
		categories.put("Herbivores", Arrays.asList("Zebra", "Elephant"));
		categories.put("Reptiles", Arrays.asList("Snake", "Turtle"));
		categories.put("Birds", Arrays.asList("Penguin", "Parrot"));
		categories.put("Fish", Arrays.asList("AquariumFish", "GoldFish", "ClownFish"));
		categories.put("Insects", Arrays.asList("Spider", "Bee"));

		Map<String, String> foodTypeByAnimal = new HashMap<>();
		foodTypeByAnimal.put("Lion", "kg of meat");
		foodTypeByAnimal.put("Tiger", "kg of meat");
		foodTypeByAnimal.put("Zebra", "kg of grass");
		foodTypeByAnimal.put("Elephant", "kg of grass");
		foodTypeByAnimal.put("Snake", "mice");
		foodTypeByAnimal.put("Turtle", "kg of sea grass");
		foodTypeByAnimal.put("Spider", "fly");
		foodTypeByAnimal.put("Bee", "mg of sugar");
		foodTypeByAnimal.put("Penguin", "fish");
		foodTypeByAnimal.put("Parrot", "food portions");
		foodTypeByAnimal.put("AquariumFish", "food portions");
		foodTypeByAnimal.put("GoldFish", "food portions");
		foodTypeByAnimal.put("ClownFish", "food portions");

		for (Animal animal : getAnimals()) {
			double foodConsumed = animal.feed();
			String className = animal.getClass().getSimpleName();
			foodConsumptionByType.merge(className, foodConsumed, Double::sum);
		}

		StringBuilder consumptionSummary = new StringBuilder();

		for (String category : categories.keySet()) {
			consumptionSummary.append(category).append(":\n");
			List<String> animalsInCategory = categories.get(category);
			for (String animalType : animalsInCategory) {
				Double totalConsumption = foodConsumptionByType.getOrDefault(animalType, 0.0);
				String foodDetails = foodTypeByAnimal.get(animalType);

				consumptionSummary.append("  ").append(animalType).append(": ")
						.append(String.format("%.2f", totalConsumption)).append(" ").append(foodDetails).append("\n");

			}
			consumptionSummary.append("\n");
		}
		return consumptionSummary.toString();
	}

	@Override
	public List<Animal> removeUnhappyOrOldAnimals(List<Animal> animals) {
		List<Animal> removedAnimals = new ArrayList<>();
		Iterator<Animal> iterator = animals.iterator();

		try (Connection conn = DBUtil.getConnection()) {

			AnimalDAO dao = new AnimalDAO(conn);

			while (iterator.hasNext()) {
				Animal animal = iterator.next();
				boolean condition = animal.getHappiness() <= 0 || animal.getAge() > animal.getLifeSpan();

				if (condition) {
					dao.deleteAnimalFromDatabase(animal);
					removedAnimals.add(animal);
					iterator.remove();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return removedAnimals;
	}

	@Override
	public void ageOneYear() {
		try (Connection conn = DBUtil.getConnection()) {
			AnimalDAO animalDAO = new AnimalDAO(conn);
			for (Animal animal : getAnimals()) {
				int newAge = animal.getAge() + 1;
				int newHappiness = animal.getHappiness() - decreaseRandomHappines();

				animal.setAge(newAge);
				animal.setHappiness(newHappiness);
				animalDAO.updateAnimalAgeAndHappiness(animal.getAnimalId(), newAge, newHappiness);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setDefaultHappinessAfterFeeding() {
		try (Connection conn = DBUtil.getConnection()) {
			AnimalDAO animalDAO = new AnimalDAO(conn);
			for (Animal animal : getAnimals()) {
				animal.setHappiness(100);
				animalDAO.updateAnimalAgeAndHappiness(animal.getAnimalId(), animal.getAge(), 100);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<AnimalRecord> createDefaultAnimals() {

		List<AnimalRecord> animals = new ArrayList<>();

		try (Connection conn = DBUtil.getConnection()) {
			AnimalDAO animalDAO = new AnimalDAO(conn);

			for (int i = 0; i < Predator.getDefaultNumOfPredators(); i++) {
				int tiger_age = randomPredatorAge();
				int tiger_happiness = getRandomHappines();
				double tiger_weight = randomPredatorWeight();
				Sex tiger_sex = randomAnimalSex();
				PredatorsName tiger_name = randomPredatorName();
				int tiger_id = animalDAO.insertAnimalAndGetId(tiger_age, tiger_happiness, "Predator", "Tiger");
				Tiger tiger = new Tiger(tiger_id, tiger_name, tiger_age, tiger_weight, tiger_sex, tiger_happiness);
				animals.add(new AnimalRecord("Predator", "Tiger", tiger));

				int lion_age = randomPredatorAge();
				int lion_happiness = getRandomHappines();
				double lion_weight = randomPredatorWeight();
				Sex lion_sex = randomAnimalSex();
				PredatorsName lion_name = randomPredatorName();
				int lion_id = animalDAO.insertAnimalAndGetId(lion_age, lion_happiness, "Predator", "Lion");
				Lion lion = new Lion(lion_id, lion_name, lion_age, lion_weight, lion_sex, lion_happiness);
				animals.add(new AnimalRecord("Predator", "Lion", lion));
			}

			for (int i = 0; i < Reptile.getDefaultNumOfReptile(); i++) {
				int snake_age = randomSnakeAge();
				int snake_happiness = getRandomHappines();
				ReptilesName snake_name = randomReptileName();
				double snake_weight = randomSnakeWeight();
				double snake_length = randomSnakeLength();
				StatusCheck snake_isVenomous = randomVenomousStatus();
				StatusCheck snake_isNinja = StatusCheck.NO;
				Colors[] snake_colors = { randomReptileColor() };
				int snake_id = animalDAO.insertAnimalAndGetId(snake_age, snake_happiness, "Reptile", "Snake");
				Snake snake = new Snake(snake_id, snake_name, snake_age, snake_weight, snake_length, snake_isVenomous,
						snake_colors, snake_isNinja, snake_happiness);
				animals.add(new AnimalRecord("Reptile", "Snake", snake));

				int turtle_age = randomTurtleAge();
				int turtle_happiness = getRandomHappines();
				ReptilesName turtle_name = randomReptileName();
				double turtle_weight = randomTurtleWeight();
				double turtle_length = randomTurtleLength();
				StatusCheck turtle_isVenomous = randomVenomousStatus();
				StatusCheck turtle_isNinja = randomNinjaStatus();
				Colors[] turtle_colors = { Colors.GREEN };
				int turtle_id = animalDAO.insertAnimalAndGetId(turtle_age, turtle_happiness, "Reptile", "Turtle");
				Turtle turtle = new Turtle(turtle_id, turtle_name, turtle_age, turtle_weight, turtle_length,
						turtle_isVenomous, turtle_colors, turtle_isNinja, turtle_happiness);
				animals.add(new AnimalRecord("Reptile", "Turtle", turtle));
			}

			for (int i = 0; i < Herbivore.getDefaultNumOfHerbivores(); i++) {
				int zebra_age = randomZebraAge();
				int zebra_happiness = getRandomHappines();
				HerbivoreName zebra_name = randomHerbivoreName();
				double zebra_weight = randomZebraWeight();
				double zebra_height = randomZebraHeight();
				Sex zebra_sex = randomAnimalSex();
				Pattern zebra_pattern = Pattern.LINES;
				int zebra_id = animalDAO.insertAnimalAndGetId(zebra_age, zebra_happiness, "Herbivore", "Zebra");
				Zebra zebra = new Zebra(zebra_id, zebra_name, zebra_age, zebra_weight, zebra_height, zebra_pattern,
						zebra_sex, zebra_happiness);
				animals.add(new AnimalRecord("Herbivore", "Zebra", zebra));

				int elephant_age = randomElephantAge();
				int elephant_happiness = getRandomHappines();
				HerbivoreName elephant_name = randomHerbivoreName();
				double elephant_weight = randomElephantWeight();
				double elephant_height = randomElephantHeight();
				Sex elephant_sex = randomAnimalSex();
				Pattern elephant_pattern = Pattern.SMOOTH;
				int elephant_id = animalDAO.insertAnimalAndGetId(elephant_age, elephant_happiness, "Herbivore",
						"Elephant");
				Elephant elephant = new Elephant(elephant_id, elephant_name, elephant_age, elephant_weight,
						elephant_height, elephant_pattern, elephant_sex, elephant_happiness);
				animals.add(new AnimalRecord("Herbivore", "Elephant", elephant));
			}

			int mainPenguinId = animalDAO.insertAnimalAndGetId(MAIN_PENGUIN_AGE, 100, "Bird", "Penguin");
			Penguin mainPenguin = new Penguin(mainPenguinId, MAIN_PENGUIN_NAME, MAIN_PENGUIN_AGE, MAIN_PENGUIN_HEIGHT,
					100);
			animals.add(new AnimalRecord("Bird", "Penguin", mainPenguin));

			for (int i = 0; i < Bird.getDefaultNumOfBirds(); i++) {
				int penguin_age = randomPenguinAge();
				int penguin_happiness = getRandomHappines();
				BirdsName penguin_name = randomBirdName();
				double penguin_height = randomPenguinHeight();
				int penguin_id = animalDAO.insertAnimalAndGetId(penguin_age, penguin_happiness, "Bird", "Penguin");
				Penguin penguin = new Penguin(penguin_id, penguin_name, penguin_age, penguin_height, penguin_happiness);
				animals.add(new AnimalRecord("Bird", "Penguin", penguin));

				int parrot_age = randomParrotAge();
				int parrot_happiness = getRandomHappines();
				BirdsName parrot_name = randomBirdName();
				double parrot_height = randomParrotHeight();
				int parrot_id = animalDAO.insertAnimalAndGetId(parrot_age, parrot_happiness, "Bird", "Parrot");
				Parrot parrot = new Parrot(parrot_id, parrot_name, parrot_age, parrot_height, parrot_happiness);
				animals.add(new AnimalRecord("Bird", "Parrot", parrot));
			}

			for (int i = 0; i < Insects.getDefaultNumOfInsects(); i++) {
				int spider_age = randomSpiderAge();
				int spider_happiness = getRandomHappines();
				StatusCheck spider_isVenomous = randomVenomousStatus();
				Pattern spider_pattern = randomSpiderPattern();
				StatusCheck spider_isSpiderMan = randomSpiderManStatus();
				Colors[] spider_colors = new Colors[] { randomSpiderColor() };

				int spider_id = animalDAO.insertAnimalAndGetId(spider_age, spider_happiness, "Insect", "Spider");
				Spider spider = new Spider(spider_id, spider_age, spider_isVenomous, spider_colors, spider_pattern,
						spider_isSpiderMan, spider_happiness);
				animals.add(new AnimalRecord("Insect", "Spider", spider));

				int bee_age = randomBeeAge();
				int bee_happiness = getRandomHappines();
				StatusCheck bee_isVenomous = randomVenomousStatus();
				StatusCheck bee_isSpiderMan = StatusCheck.NO;
				Pattern bee_pattern = Pattern.LINES;
				Colors[] bee_colors = new Colors[] { Colors.BLACK, Colors.YELLOW };

				int bee_id = animalDAO.insertAnimalAndGetId(bee_age, bee_happiness, "Insect", "Bee");
				Bee bee = new Bee(bee_id, bee_age, bee_isVenomous, bee_colors, bee_pattern, bee_isSpiderMan,
						bee_happiness);
				animals.add(new AnimalRecord("Insect", "Bee", bee));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to create default predator animals.", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return animals;
	}

	public String dominantColors() {
		Map<Colors, Integer> colorCount = new EnumMap<>(Colors.class);

		for (Animal animal : getAnimals()) {
			if (animal instanceof Fish fish) {
				for (Colors color : fish.getColor()) {
					if (color != null) {
						colorCount.put(color, colorCount.getOrDefault(color, 0) + 1);
					}
				}
			}
		}

		List<Map.Entry<Colors, Integer>> sortedColors = new ArrayList<>(colorCount.entrySet());
		sortedColors.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

		if (sortedColors.size() >= 2) {
			return sortedColors.get(0).getKey() + " + " + sortedColors.get(1).getKey();
		} else if (sortedColors.size() == 1) {
			return sortedColors.get(0).getKey().toString();
		} else {
			return "No colors found";
		}
	}

	public Sex randomAnimalSex() {
		Sex[] sex = Sex.values();
		int index = getRANDOM().nextInt(sex.length);
		return sex[index];
	}

	public StatusCheck randomVenomousStatus() {
		StatusCheck[] isVenomus = StatusCheck.values();
		int index = getRANDOM().nextInt(isVenomus.length);
		return isVenomus[index];
	}

	public int getRandomHappines() {
		return 10 + getRANDOM().nextInt(90);
	}

	public int decreaseRandomHappines() {
		return 1 + getRANDOM().nextInt(19);
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                            FISH Methods                                                                 //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Pattern randomFishPattern() {
		Pattern[] pattern = Pattern.values();
		int index = getRANDOM().nextInt(pattern.length);
		return pattern[index];
	}

	public Colors randomFishColor() {
		Colors[] color = Colors.values();
		int index = getRANDOM().nextInt(color.length);
		return color[index];
	}

	public double randomFishLength() {
		return (getMinFishLength() + getRANDOM().nextDouble(getMaxFishLength() - getMinFishLength()));
	}

	public int randomFishAge(int lifeSpan) {
		int randomLifeSpan = 0;

		if (lifeSpan == AquariumFish.LIFE_SPAN)
			randomLifeSpan = AquariumFish.LIFE_SPAN;
		else if (lifeSpan == GoldFish.LIFE_SPAN)
			randomLifeSpan = GoldFish.LIFE_SPAN;
		else if (lifeSpan == ClownFish.LIFE_SPAN)
			randomLifeSpan = ClownFish.LIFE_SPAN;

		return (getMinAge() + getRANDOM().nextInt(randomLifeSpan - getMinAge()));
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                            BIRD Methods                                                                 //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void sortPenguins(int choice) {
		switch (choice) {
		case 1: // Sort by name (A to Z)
			getAnimals().sort((bird1, bird2) -> {
				if (bird1 instanceof Penguin && bird2 instanceof Penguin) {
					String name1 = ((Penguin) bird1).getName();
					String name2 = ((Penguin) bird2).getName();
					BirdsName randomName1 = ((Penguin) bird1).getRandomName();
					BirdsName randomName2 = ((Penguin) bird2).getRandomName();
					String effectiveName1 = name1 != null ? name1
							: (randomName1 != null ? randomName1.toString() : null);
					String effectiveName2 = name2 != null ? name2
							: (randomName2 != null ? randomName2.toString() : null);

					if (effectiveName1 == null)
						effectiveName1 = "";
					if (effectiveName2 == null)
						effectiveName2 = "";

					return effectiveName1.compareTo(effectiveName2);
				}
				return 0;
			});
			break;

		case 2: // Sort by height

			getAnimals().sort((bird1, bird2) -> {
				if (bird1 instanceof Penguin && bird2 instanceof Penguin) {
					return Double.compare(((Penguin) bird2).getHeight(), ((Penguin) bird1).getHeight());
				}
				return 0;
			});
			break;

		case 3: // Sort by age
			getAnimals().sort((bird1, bird2) -> {
				if (bird1 instanceof Penguin && bird2 instanceof Penguin) {
					return Integer.compare(((Penguin) bird2).getAge(), ((Penguin) bird1).getAge());
				}
				return 0;
			});
			break;

		default:

			break;
		}
	}

	public BirdsName randomBirdName() {
		BirdsName[] name = BirdsName.values();
		int index = getRANDOM().nextInt(name.length);
		return name[index];
	}

	public int randomPenguinAge() {
		return getMinAge() + getRANDOM().nextInt(Penguin.LIFE_SPAN - getMinAge());
	}

	public int randomParrotAge() {
		return getMinAge() + getRANDOM().nextInt(Parrot.LIFE_SPAN - getMinAge());
	}

	public double randomPenguinHeight() {
		double height = getMinPenguinHeight() + getRANDOM().nextDouble(getMaxPenguinHeight() - getMinPenguinHeight());
		return Double.parseDouble(getFormatter().format(height));
	}

	public double randomParrotHeight() {
		double height = getMinParrotHeight() + getRANDOM().nextDouble(getMaxParrotHeight() - getMinParrotHeight());
		return Double.parseDouble(getFormatter().format(height));
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                     INSECTS Methods                                                          //
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public StatusCheck randomSpiderManStatus() {
		StatusCheck[] isSpiderMan = StatusCheck.values();
		int index = getRANDOM().nextInt(isSpiderMan.length);
		return isSpiderMan[index];
	}

	public int randomBeeAge() {
		return getMinAge() + getRANDOM().nextInt(Bee.getLIFE_SPAN() - getMinAge());
	}

	public int randomSpiderAge() {
		return getMinAge() + getRANDOM().nextInt(Spider.getLIFE_SPAN());
	}

	public Colors randomSpiderColor() {
		Colors[] color = Colors.values();
		int index = getRANDOM().nextInt(color.length);
		return color[index];
	}

	public Pattern randomSpiderPattern() {
		Pattern[] pattern = Pattern.values();
		int index = getRANDOM().nextInt(pattern.length);
		return pattern[index];
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                       HERBIVORE Methods                                                                 //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public HerbivoreName randomHerbivoreName() {
		HerbivoreName[] name = HerbivoreName.values();
		int index = getRANDOM().nextInt(name.length);
		return name[index];
	}

	public int randomZebraAge() {
		return getMinAge() + getRANDOM().nextInt(Zebra.getLIFE_SPAN() - getMinAge());
	}

	public int randomElephantAge() {
		return getMinAge() + getRANDOM().nextInt(Elephant.LIFE_SPAN - getMinAge());
	}

	public double randomZebraWeight() {
		double weight = getMinHerbivoreWeight() + getRANDOM().nextDouble(getMaxZebraWeight() - getMinHerbivoreWeight());
		return Double.parseDouble(getFormatter().format(weight));
	}

	public double randomElephantWeight() {
		double weight = getMinHerbivoreWeight()
				+ getRANDOM().nextDouble(getMaxElephantWeight() - getMinHerbivoreWeight());
		return Double.parseDouble(getFormatter().format(weight));
	}

	public double randomZebraHeight() {
		double length = getMinHerbivoreHeight() + getRANDOM().nextDouble(getMaxZebraHeight() - getMinHerbivoreHeight());
		return Double.parseDouble(getFormatter().format(length));
	}

	public double randomElephantHeight() {
		double length = getMinHerbivoreHeight()
				+ getRANDOM().nextDouble(getMaxElephantHeight() - getMinHerbivoreHeight());
		return Double.parseDouble(getFormatter().format(length));
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                           PREDATOR Methods                                                              //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public PredatorsName randomPredatorName() {
		Random randomName = new Random();
		PredatorsName[] name = PredatorsName.values();
		int index = randomName.nextInt(name.length);
		return name[index];
	}

	public int randomPredatorAge() {
		return getMinAge() + getRANDOM().nextInt(Lion.getLIFE_SPAN() - getMinAge());
	}

	public double randomPredatorWeight() {
		double weight = getMinPredatorWeight()
				+ getRANDOM().nextDouble(getMaxPredatorWeight() - getMinPredatorWeight());
		return Double.parseDouble(getFormatter().format(weight));
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                           REPTILE Methods                                                               //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public ReptilesName randomReptileName() {
		ReptilesName[] name = ReptilesName.values();
		int index = getRANDOM().nextInt(name.length);
		return name[index];
	}

	public StatusCheck randomNinjaStatus() {
		StatusCheck[] isNinja = StatusCheck.values();
		int index = getRANDOM().nextInt(isNinja.length);
		return isNinja[index];
	}

	public Colors randomReptileColor() {
		Colors[] color = Colors.values();
		int index = getRANDOM().nextInt(color.length);
		return color[index];
	}

	public int randomSnakeAge() {
		return getMinAge() + getRANDOM().nextInt(Snake.LIFE_SPAN - getMinAge());
	}

	public int randomTurtleAge() {
		return getMinAge() + getRANDOM().nextInt(Turtle.LIFE_SPAN - getMinAge());
	}

	public double randomSnakeWeight() {
		double weight = getMinSnakeWeight() + getRANDOM().nextDouble(getMaxSnakeWeight() - getMinSnakeWeight());
		return Double.parseDouble(getFormatter().format(weight));
	}

	public double randomTurtleWeight() {
		double weight = getMinTurtleWeight() + getRANDOM().nextDouble(getMaxTurtleWeight() - getMinTurtleWeight());
		return Double.parseDouble(getFormatter().format(weight));
	}

	public double randomSnakeLength() {
		double weight = getMinSnakeLength() + getRANDOM().nextDouble(getMaxSnakeLength() - getMinSnakeLength());
		return Double.parseDouble(getFormatter().format(weight));
	}

	public double randomTurtleLength() {
		double weight = getMinTurtleLength() + getRANDOM().nextDouble(getMaxTurtleLength() - getMinTurtleLength());
		return Double.parseDouble(getFormatter().format(weight));
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
//                                                                Geters/Setters                                                                        //
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// GENERAL GETERS\SETTERS															                                                           //
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public String getZooName() {
		return zooName;
	}

	public String getZooAddress() {
		return zooAddress;
	}

	public NumberFormat getFormatter() {
		return FORMATTER;
	}

	public int getMinAge() {
		return MIN_AGE;
	}

	public List<Animal> getAnimals() {
		return animals;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// BIRD GETERS\SETTERS
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<Bird> getBirds() {
		return birds;
	}

	public void setBirds(List<Bird> bird) {
		birds = bird;
	}

	public String getMainPenguinName() {
		return MAIN_PENGUIN_NAME;
	}

	public int getMainPenguinAge() {
		return MAIN_PENGUIN_AGE;
	}

	public double getMainPenguinHeight() {
		return MAIN_PENGUIN_HEIGHT;
	}

	public double getMaxPenguinHeight() {
		return MAX_PENGUIN_HEIGHT;
	}

	public double getMinPenguinHeight() {
		return MIN_PENGUIN_HEIGHT;
	}

	public double getMinParrotHeight() {
		return MIN_PARROT_HEIGHT;
	}

	public double getMaxParrotHeight() {
		return MAX_PARROT_HEIGHT;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// INSECTS GETERS\SETTERS
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<Insects> getInsect() {
		return insects;
	}

	public void setInsect(List<Insects> insect) {
		insects = insect;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// HERBIVORES GETERS\SETTERS
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<Herbivore> getHerbivore() {
		return herbivores;
	}

	public void setHerbivore(List<Herbivore> herbivore) {
		herbivores = herbivore;
	}

	public double getMinHerbivoreWeight() {
		return MIN_HERBIVORE_WEIGHT;
	}

	public double getMinHerbivoreHeight() {
		return MIN_HERBIVORE_HEIGHT;
	}

	public double getMaxElephantWeight() {
		return MAX_ELEPHANT_WEIGHT;
	}

	public double getMaxZebraWeight() {
		return MAX_ZEBRA_WEIGHT;
	}

	public double getMaxElephantHeight() {
		return MAX_ELEPHANT_HEIGHT;
	}

	public double getMaxZebraHeight() {
		return MAX_ZEBRA_HEIGHT;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// PREDATORS GETERS\SETTERS
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<Predator> getPredators() {
		return predators;
	}

	public void setPredators(List<Predator> predator) {
		predators = predator;
	}

	public double getMaxPredatorWeight() {
		return MAX_PREDATOR_WEIGHT;
	}

	public double getMinPredatorWeight() {
		return MIN_PREDATOR_WEIGHT;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// REPTILE GETERS\SETTERS
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public double getMaxSnakeLength() {
		return MAX_SNAKE_LENGTH;
	}

	public double getMaxSnakeWeight() {
		return MAX_SNAKE_WEIGHT;
	}

	public double getMinSnakeWeight() {
		return MIN_SNAKE_WEIGHT;
	}

	public double getMinSnakeLength() {
		return MIN_SNAKE_LENGTH;
	}

	public double getMaxTurtleLength() {
		return MAX_TURTLE_LENGTH;
	}

	public double getMaxTurtleWeight() {
		return MAX_TURTLE_WEIGHT;
	}

	public double getMinTurtleWeight() {
		return MIN_TURTLE_WEIGHT;
	}

	public double getMinTurtleLength() {
		return MIN_TURTLE_LENGTH;
	}

	public List<Reptile> getReptile() {
		return reptiles;
	}

	public void setReptile(List<Reptile> reptile) {
		reptiles = reptile;
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 										Fish GETERS\SETTERS
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void setFish(List<Fish> newFish) {
		fish = newFish;
	}

	public List<Fish> getFish() {
		return fish;
	}

	public double getMaxFishLength() {
		return MAX_FISH_LENGTH;
	}

	public double getMinFishLength() {
		return MIN_FISH_LENGTH;
	}

	public Random getRANDOM() {
		return RANDOM;
	}
}
