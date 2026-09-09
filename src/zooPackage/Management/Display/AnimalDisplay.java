package zooPackage.Management.Display;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import zooPackage.DB.DBUtil;
import zooPackage.DB.EmployeeDAO;
import zooPackage.Entities.Animals.Animal;
import zooPackage.Entities.Animals.Bird.*;
import zooPackage.Entities.Animals.Fish.*;
import zooPackage.Entities.Animals.Herbivore.*;
import zooPackage.Entities.Animals.Insects.*;
import zooPackage.Entities.Animals.Predator.*;
import zooPackage.Entities.Animals.Reptile.*;
import zooPackage.Enum.Colors;
import zooPackage.Exception.InvalidChoiceException;
import zooPackage.Management.ZooFacade;
import zooPackage.Music.Music;
import zooPackage.PanelEditor.PanelDisplay;

public class AnimalDisplay {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private static AnimalDisplay instance;

	private AnimalDisplay() {
	}

	public static synchronized AnimalDisplay getInstance() {
		if (instance == null) {
			instance = new AnimalDisplay();
		}
		return instance;
	}

	public void displayWelcomeMessage() {
		zooFacade.getPanelDialogMessage().showInformationDialog("Hello!!!\nWelcome to Zoorama!\n");
	}

	public void displayAnimalMenu() {
		String[] animalTypes = { "Herbivore", "Predator", "Reptile", "Bird", "Fish", "Insects", "All the animals" };
		String type = (String) JOptionPane.showInputDialog(null, "Select the animal type:", "Display Animal",
				JOptionPane.QUESTION_MESSAGE, null, animalTypes, animalTypes[0]);

		if (type == null) {
			return;

		} else if (type.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please choose a animal type.");
		}
		switch (type) {
		case "Herbivore":
			String[] herbivoreTypes = { "Zebra", "Elephant" };
			String herbivoreType = (String) JOptionPane.showInputDialog(null, "Select the animal type:",
					"Display Animal", JOptionPane.QUESTION_MESSAGE, null, herbivoreTypes, herbivoreTypes[0]);

			if (herbivoreType == null) {
				return;
			}

			if (herbivoreType == "Zebra") {
				displayAnimalsByType(Zebra.class);
			} else
				displayAnimalsByType(Elephant.class);

			break;
		case "Predator":
			String[] predatorTypes = { "Tiger", "Lion" };
			String predatorType = (String) JOptionPane.showInputDialog(null, "Select the animal type:",
					"Display Animal", JOptionPane.QUESTION_MESSAGE, null, predatorTypes, predatorTypes[0]);

			if (predatorType == null) {
				return;
			}

			if (predatorType == "Tiger") {
				displayAnimalsByType(Tiger.class);
			} else
				displayAnimalsByType(Lion.class);

			break;
		case "Reptile":
			String[] reptileTypes = { "Snake", "Turtle" };
			String reptileType = (String) JOptionPane.showInputDialog(null, "Select the animal type:", "Display Animal",
					JOptionPane.QUESTION_MESSAGE, null, reptileTypes, reptileTypes[0]);

			if (reptileType == null) {
				return;
			}

			if (reptileType == "Snake") {
				displayAnimalsByType(Snake.class);
			} else
				displayAnimalsByType(Turtle.class);

			break;
		case "Bird":
			String[] birdTypes = { "Penguin", "Parrot" };
			String birdType = (String) JOptionPane.showInputDialog(null, "Select the animal type:", "Display Animal",
					JOptionPane.QUESTION_MESSAGE, null, birdTypes, birdTypes[0]);
			boolean flag1 = true;
			if (birdType == null) {
				return;
			}
			switch (birdType) {
			case "Penguin":
				do {
					String sortInput = JOptionPane.showInputDialog(null,
							"How would you like to manage the penguins?\n\n"
									+ "1. Sort by name in ascending alphabetical order\n"
									+ "2. Sort by height in descending order\n" + "3. Sort by age in ascending order\n"
									+ "4. Display without sorting\n\n");
					if (sortInput == null) {
						JOptionPane.showMessageDialog(null, "Sort operation cancelled.");
						return;
					}
					try {
						int userChoice = Integer.parseInt(sortInput);
						if (userChoice < 1 || userChoice > 4) {
							throw new InvalidChoiceException(
									"Invalid choice!\n\nPlease choose:\nFor sorting by name: 1\n"
											+ "For sorting by height: 2\n" + "For sorting by age: 3\n"
											+ "For displaing without sorting: 4\n");
						} else {
							flag1 = false;
							if (userChoice >= 1 || userChoice <= 3) {
								zooFacade.getAnimalManager().sortPenguins(userChoice);
								displayAnimalsByType(Penguin.class);
							} else {
								displayAnimalsByType(Penguin.class);
							}
						}
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null,
								"Invalid input!\n\nPlease choose:\nFor sorting by name: 1\n"
										+ "For sorting by height: 2\n" + "For sorting by age: 3\n"
										+ "For displaing without sorting: 4\n",
								"Error", JOptionPane.ERROR_MESSAGE);
					} catch (InvalidChoiceException e) {
						JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						continue;
					}
				} while (flag1);
				break;
			case "Parrot":
				displayAnimalsByType(Parrot.class);
				flag1 = false;
				break;
			default:
				JOptionPane.showMessageDialog(null, "Invalid selection.");
				break;
			}

			break;
		case "Fish":

			String[] fishTypes = { "The whole aquarium", "AquariumFish", "GoldFish", "ClownFish" };
			String fishType = (String) JOptionPane.showInputDialog(null, "Select the animal type:", "Display Animal",
					JOptionPane.QUESTION_MESSAGE, null, fishTypes, fishTypes[0]);
			boolean flag = true;
			do {
				if (fishType == null) {
					return;
				}
				switch (fishType) {
				case "The whole aquarium":
					displayAquarium();
					flag = false;
					break;
				case "AquariumFish":
					displayAnimalsByType(AquariumFish.class);
					flag = false;
					break;
				case "GoldFish":
					displayAnimalsByType(GoldFish.class);
					flag = false;
					break;
				case "ClownFish":
					displayAnimalsByType(ClownFish.class);
					flag = false;
					break;
				default:
					JOptionPane.showMessageDialog(null, "Invalid selection.");
					break;
				}
			} while (flag);

			break;
		case "Insects":
			String[] insectTypes = { "Spider", "Bee" };
			String insectType = (String) JOptionPane.showInputDialog(null, "Select the animal type:", "Display Animal",
					JOptionPane.QUESTION_MESSAGE, null, insectTypes, insectTypes[0]);

			if (insectType == null) {
				return;
			}

			if (insectType == "Spider") {
				displayAnimalsByType(Spider.class);
			} else
				displayAnimalsByType(Bee.class);

			break;
		case "All the animals":
			displayAnimalsByType(Animal.class);
			break;
		default:
			JOptionPane.showMessageDialog(null, "Invalid selection.");
			break;
		}
	}

	public void displayAquarium() {
		List<Animal> fishes = zooFacade.getAnimalManager().getAnimals().stream()
				.filter(animal -> animal instanceof Fish).collect(Collectors.toList());

		StringBuilder allFish = new StringBuilder();
		StringBuilder allFishColor = new StringBuilder();

		for (int i = 0; i < fishes.size(); i++) {
			allFish.append("Fish ").append(i + 1).append(": ").append(fishes.get(i)).append("\n");
		}

		int colorCounter = 0;
		Colors[][] colorCheck = new Colors[fishes.size()][];
		for (int i = 0; i < fishes.size(); i++) {
			colorCheck[i] = ((Fish) fishes.get(i)).getColor();
			colorCounter += colorCheck[i].length;
		}

		Colors[] colorsLine = new Colors[colorCounter];
		colorCounter = 0;
		for (Colors[] colors : colorCheck) {
			for (Colors color : colors) {
				colorsLine[colorCounter++] = color;
			}
		}

		LinkedHashSet<Colors> uniqueColorsSet = new LinkedHashSet<>(Arrays.asList(colorsLine));

		for (Colors color : uniqueColorsSet) {
			allFishColor.append(color).append(" | ");
		}

		String aquariumContent = "Number of fish in aquarium is: " + fishes.size() + "\n" + allFish.toString()
				+ "\nNumber of colors the fish contain: " + uniqueColorsSet.size() + "\n" + allFishColor.toString()
				+ "\n\nMost dominant colors:\n" + zooFacade.getAnimalManager().dominantColors();

		String backgroundImage = "resorces\\aquarium.jpg";
		float opacity = 0.4f;
		SwingUtilities.invokeLater(() -> {
			PanelDisplay.showTextPane(aquariumContent, backgroundImage, opacity, "Aquarium");
		});
	}

	public void displayAnimalsByType(Class<? extends Animal> animalClass) {
		StringBuilder animalInfo = new StringBuilder();
		for (Animal animal : zooFacade.getAnimalManager().getAnimals()) {
			if (animalClass.isInstance(animal)) {
				animalInfo.append(animal.toString()).append("\n");
			}
		}

		if (animalInfo.length() == 0) {
			JOptionPane.showMessageDialog(null, "No animals of type " + animalClass.getSimpleName() + " found.",
					animalClass.getSimpleName() + " Information", JOptionPane.INFORMATION_MESSAGE);
		} else {
			String backgroundImage = "resorces\\" + animalClass.getSimpleName() + ".jpg";
			float opacity = 0.4f;
			SwingUtilities.invokeLater(() -> {
				PanelDisplay.showTextPane(animalInfo.toString(), backgroundImage, opacity,
						"The " + animalClass.getSimpleName() + " list");
			});
		}
	}

	public void displayAnimalNoise() {
		StringBuilder allNoises = new StringBuilder();

		for (Animal animal : zooFacade.getAnimalManager().getAnimals()) {
			allNoises.append(animal.makeNoise()).append(" | ");
		}

		if (allNoises.length() > 0) {
			allNoises.setLength(allNoises.length() - 3);
		}
		String backgroundImage = "resorces\\noise.jpg";
		Music.playMusic("resorces\\MakeNoise.wav");
		float opacity = 0.4f;
		SwingUtilities.invokeLater(() -> {
			PanelDisplay.showTextPane(allNoises.toString(), backgroundImage, opacity, "The sound of the Animals");
		});
	}

	public void displayFoodConsuption() {
		try (Connection conn = DBUtil.getConnection()) {
			EmployeeDAO employeeDAO = new EmployeeDAO(conn);
			employeeDAO.employeeCareForAnimal();
			JOptionPane.showMessageDialog(null, zooFacade.getAnimalManager().feedAllAnimals(), "Daily Food Consumption",
					JOptionPane.INFORMATION_MESSAGE);
			zooFacade.getAnimalManager().setDefaultHappinessAfterFeeding();

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to save to database.", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	public void printZooDetails() {
		zooFacade.getPanelDialogMessage().showInformationDialog("Zoo name: " + zooFacade.getAnimalManager().getZooName()
				+ "\nZoo address: " + zooFacade.getAnimalManager().getZooAddress() + "\n\n\n"
				+ "Predators\n Number of Lions: " + zooFacade.getAnimalManager().countAnimalsByType(Lion.class) + "\n"
				+ " Number of Tigers: " + zooFacade.getAnimalManager().countAnimalsByType(Tiger.class) + "\n\n"
				+ "Herbivores\n Number of Zebras: " + zooFacade.getAnimalManager().countAnimalsByType(Zebra.class)
				+ "\n" + " Number of Elephants: " + zooFacade.getAnimalManager().countAnimalsByType(Elephant.class)
				+ "\n\n" + "Reptiles\n Number of Snakes: "
				+ zooFacade.getAnimalManager().countAnimalsByType(Snake.class) + "\n" + " Number of Turtles: "
				+ zooFacade.getAnimalManager().countAnimalsByType(Turtle.class) + "\n\n"
				+ "Insects\n Number of Spiders: " + zooFacade.getAnimalManager().countAnimalsByType(Spider.class) + "\n"
				+ " Number of Bees: " + zooFacade.getAnimalManager().countAnimalsByType(Bee.class) + "\n\n"
				+ "Birds\n Number of Penguins: " + zooFacade.getAnimalManager().countAnimalsByType(Penguin.class) + "\n"
				+ " Number of Parrots: " + zooFacade.getAnimalManager().countAnimalsByType(Parrot.class) + "\n\n"
				+ "Fish\n Number of Aquarium Fish: "
				+ zooFacade.getAnimalManager().countAnimalsByType(AquariumFish.class) + "\n" + " Number of Gold Fish: "
				+ zooFacade.getAnimalManager().countAnimalsByType(GoldFish.class) + "\n" + " Number of Clown Fish: "
				+ zooFacade.getAnimalManager().countAnimalsByType(ClownFish.class));
	}
}
