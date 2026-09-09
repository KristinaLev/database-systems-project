package zooPackage.Management;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import zooPackage.DB.AnimalDAO;
import zooPackage.DB.DBUtil;
import zooPackage.Entities.Animals.AnimalRecord;
import zooPackage.Entities.Animals.Animal;
import zooPackage.Entities.Animals.Bird.*;
import zooPackage.Entities.Animals.Fish.*;
import zooPackage.Entities.Animals.Herbivore.*;
import zooPackage.Entities.Animals.Insects.*;
import zooPackage.Entities.Animals.Predator.*;
import zooPackage.Entities.Animals.Reptile.*;
import zooPackage.Enum.*;
import zooPackage.Exception.*;
import zooPackage.Interface.AnimalManagement_Interface;
import zooPackage.Music.Music;

public class AnimalManagement implements AnimalManagement_Interface {

	private static ZooFacade zooFacade = ZooFacade.getInstance();
	private AnimalManager zoo = AnimalManager.getInstance();
	private static AnimalManagement instance;

	private AnimalManagement() {
	}

	public static AnimalManagement getInstance() {
		if (instance == null) {
			instance = new AnimalManagement();
		}
		return instance;
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                    GENERAL                                                                                       //                                        
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void addDefaultAnimalsToDatabase() throws SQLException {
		addRandomFish(Fish.DEFAULT_NUM_OF_FISH);
		List<AnimalRecord> animalRecords = zoo.createDefaultAnimals();
		try (Connection conn = DBUtil.getConnection()) {

			AnimalDAO animalDAO = new AnimalDAO(conn);

			for (AnimalRecord record : animalRecords) {
				try {
					Animal animal = record.getAnimal();
					if (animal instanceof Bird bird && "Big Mama".equals(bird.getName())) {
						animalDAO.insertSubAnimalToDatabase(animal, record.getType(), record.getSpecies(), 0);
					} else {
						animalDAO.insertSubAnimalToDatabase(animal, record.getType(), record.getSpecies(), 1);
					}
					zoo.addAnimal(animal);

				} catch (SQLException e) {
					e.printStackTrace();
					zooFacade.getPanelDialogMessage()
							.showDBErrorDialog("Failed to save " + record.getSpecies() + " to database.");
				}
			}
		}
	}

	@Override
	public void addAnimalMenu() {
		String[] animalTypes = { "Herbivore", "Predator", "Reptile", "Bird", "Fish", "Insects" };
		String type = (String) JOptionPane.showInputDialog(null, "Select the animal type:", "Add Animal",
				JOptionPane.QUESTION_MESSAGE, null, animalTypes, animalTypes[0]);

		if (type == null) {
			return;

		} else if (type.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Please choose a animal type.");
		}
		switch (type) {
		case "Herbivore":
			String[] herbivoreTypes = { "Zebra", "Elephant" };
			String herbivoreType = (String) JOptionPane.showInputDialog(null, "Select the animal type:", "Add Animal",
					JOptionPane.QUESTION_MESSAGE, null, herbivoreTypes, herbivoreTypes[0]);

			if (herbivoreType == null) {
				return;
			}

			if (herbivoreType == "Zebra") {
				addHerbivore(Zebra.class);
			} else
				addHerbivore(Elephant.class);

			break;
		case "Predator":
			String[] predatorTypes = { "Tiger", "Lion" };
			String predatorType = (String) JOptionPane.showInputDialog(null, "Select the animal type:", "Add Animal",
					JOptionPane.QUESTION_MESSAGE, null, predatorTypes, predatorTypes[0]);

			if (predatorType == null) {
				return;
			}

			if (predatorType == "Tiger") {
				addPredator(Tiger.class);
			} else
				addPredator(Lion.class);

			break;
		case "Reptile":
			String[] reptileTypes = { "Snake", "Turtle" };
			String reptileType = (String) JOptionPane.showInputDialog(null, "Select the animal type:", "Add Animal",
					JOptionPane.QUESTION_MESSAGE, null, reptileTypes, reptileTypes[0]);

			if (reptileType == null) {
				return;
			}

			if (reptileType == "Snake") {
				addReptile(Snake.class);
			} else
				addReptile(Turtle.class);

			break;
		case "Bird":
			String[] birdTypes = { "Penguin", "Parrot" };
			String birdType = (String) JOptionPane.showInputDialog(null, "Select the animal type:", "Add Animal",
					JOptionPane.QUESTION_MESSAGE, null, birdTypes, birdTypes[0]);

			if (birdType == null) {
				return;
			}

			if (birdType == "Penguin") {
				addBird(Penguin.class);
			} else
				addBird(Parrot.class);

			break;
		case "Fish":
			addFish();
			break;
		case "Insects":
			String[] insectTypes = { "Spider", "Bee" };
			String insectType = (String) JOptionPane.showInputDialog(null, "Select the animal type:", "Add Animal",
					JOptionPane.QUESTION_MESSAGE, null, insectTypes, insectTypes[0]);

			if (insectType == null) {
				return;
			}

			if (insectType == "Spider") {
				addInsect(Spider.class);
			} else
				addInsect(Bee.class);

			break;
		default:
			JOptionPane.showMessageDialog(null, "Invalid selection.");
			break;
		}

	}

	@Override
	public void deathNote() {

		List<Animal> removedAnimals = new ArrayList<>();
		removedAnimals = zoo.removeUnhappyOrOldAnimals(zoo.getAnimals());

		StringBuilder sb = new StringBuilder();
		for (Animal animal : removedAnimals) {
			sb.append(animal.toString()).append("\n");
		}
		if (!removedAnimals.isEmpty()) {
			JOptionPane.showMessageDialog(null, sb.toString(), "Removed Animals", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "No animals were removed.", "Removed Animals",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                    FISH                                                                                          //                                        
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void addFish() {
		String[] fishTypes = { "AquariumFish", "GoldFish", "ClownFish", "RandomFish" };
		String fishTypeName = (String) JOptionPane.showInputDialog(null, "Select the animal type:", "Display Animal",
				JOptionPane.QUESTION_MESSAGE, null, fishTypes, fishTypes[0]);

		if (fishTypeName == null) {
			return;
		}

		if (fishTypeName == "RandomFish") {
			boolean flag = true;
			do {
				String numberInput = JOptionPane.showInputDialog(null, "How many random fish do you want to add?");
				if (numberInput == null) {
					JOptionPane.showMessageDialog(null, "Random fish addition cancelled.");
					return;
				}
				try {
					int numberOfFish = Integer.parseInt(numberInput);
					if (numberOfFish < 1)
						throw new InvalidChoiceException("Invalid choice!\nPlease enter a positive number.");
					addRandomFish(numberOfFish);
					JOptionPane.showMessageDialog(null, numberOfFish + " random fish added successfully!");
					Music.playMusic("resorces\\Fish.wav");
					return;

				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Invalid input!\nPlease enter a valid positive number.",
							"Error", JOptionPane.ERROR_MESSAGE);
				} catch (InvalidChoiceException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			} while (flag);
			return;
		}

		JLabel ageLabel = new JLabel("Age:");
		JLabel lengthLabel = new JLabel("Length:");
		JLabel patternLabel = new JLabel("Pattern:");
		JLabel colorLabel = new JLabel("Color:");
		JLabel colorsLabel = new JLabel("Use Ctrl Key for adding more then 1 color");

		JTextField ageField = new JTextField(10);
		JTextField lengthField = new JTextField(10);
		JComboBox<Pattern> patternBox = new JComboBox<>(Pattern.values());

		JComboBox<Colors> colorBox = new JComboBox<>(
				new Colors[] { Colors.ORANGE, Colors.GOLD, Colors.YELLOW, Colors.BLACK });

		JList<Colors> colorList = new JList<>(Colors.values());
		colorList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane colorScrollPane = new JScrollPane(colorList);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;

		int padding = 10;

		panel.add(ageLabel, gbc);
		gbc.gridy++;
		panel.add(ageField, gbc);
		gbc.gridy++;
		gbc.insets = new Insets(padding, 0, 0, 0);
		panel.add(lengthLabel, gbc);
		gbc.gridy++;
		gbc.insets = new Insets(0, 0, 0, 0);
		panel.add(lengthField, gbc);
		gbc.gridy++;
		gbc.insets = new Insets(padding, 0, 0, 0);

		if (fishTypeName == "AquariumFish") {
			panel.add(patternLabel, gbc);
			gbc.gridy++;
			gbc.insets = new Insets(0, 0, 0, 0);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			panel.add(patternBox, gbc);
			gbc.insets = new Insets(padding, 0, 0, 0);
			gbc.gridy++;
			panel.add(colorLabel, gbc);
			gbc.gridy++;
			gbc.insets = new Insets(0, 0, 0, 0);
			panel.add(colorsLabel, gbc);
			gbc.gridy++;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			panel.add(colorScrollPane, gbc);
			gbc.insets = new Insets(padding, 0, 0, 0);

		} else if (fishTypeName == "GoldFish") {
			panel.add(colorLabel, gbc);
			gbc.gridy++;
			gbc.insets = new Insets(0, 0, 0, 0);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			panel.add(colorBox, gbc);
		}

		int result;
		boolean validInput = false;
		do {
			result = JOptionPane.showConfirmDialog(null, panel, "Enter the details for " + fishTypeName,
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.CANCEL_OPTION) {
				JOptionPane.showMessageDialog(null, fishTypeName + " addition cancelled.");
				return;
			}

			boolean dataComplete = !ageField.getText().isEmpty() && !lengthField.getText().isEmpty();

			if ("AquariumFish".equals(fishTypeName))
				dataComplete = dataComplete && !colorList.getSelectedValuesList().isEmpty();

			if ("GoldFish".equals(fishTypeName))
				dataComplete = dataComplete && colorBox.getSelectedItem() != null;

			if (!dataComplete) {
				JOptionPane.showMessageDialog(null, "Hi friend!\nIn order to finish the " + fishTypeName
						+ " addition you have to enter all the data", "Warning", JOptionPane.WARNING_MESSAGE);

			} else {
				try {
					int age = Integer.parseInt(ageField.getText());
					double length = Double.parseDouble(lengthField.getText());
					zooFacade.getAnimalValidation().validateFish(age, length, fishTypeName);
					validInput = true;
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid values for age and length.",
							"Error", JOptionPane.ERROR_MESSAGE);
				} catch (InvalidAgeException | InvalidLengthException e) {
					JOptionPane.showMessageDialog(null, ((Throwable) e).getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
					ageField.setText("");
					lengthField.setText("");
				}
			}
		} while (!validInput || result == JOptionPane.OK_OPTION
				&& (ageField.getText().isEmpty() || lengthField.getText().isEmpty()));

		int age = Integer.parseInt(ageField.getText());
		double length = Double.parseDouble(lengthField.getText());
		Pattern pattern = (Pattern) patternBox.getSelectedItem();
		Colors[] selectedColors = colorList.getSelectedValuesList().toArray(new Colors[0]);
		Colors color = (Colors) colorBox.getSelectedItem();
		int happiness = 100;

		Fish fish = null;

		try (Connection conn = DBUtil.getConnection()) {
			AnimalDAO animalDAO = new AnimalDAO(conn);
			int animal_id = animalDAO.insertAnimalAndGetId(age, happiness, "Fish", fishTypeName);

			if ("AquariumFish".equals(fishTypeName)) {
				fish = new AquariumFish(animal_id, age, length, pattern, selectedColors, happiness);
			} else if ("GoldFish".equals(fishTypeName)) {
				fish = new GoldFish(animal_id, age, length, color, happiness);
			} else {
				fish = new ClownFish(animal_id, age, length, happiness);
			}

			zoo.addAnimal(fish);
			animalDAO.insertSubAnimalToDatabase(fish, "Fish", fishTypeName, 0);

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to insert animal into database.", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}

		JOptionPane.showMessageDialog(null, fishTypeName + " added successfully!");
		Music.playMusic("resorces\\Fish.wav");

	}

	@Override
	public void addRandomFish(int numberOfFish) {
		for (int i = 0; i < numberOfFish; i++) {
			int animal_id;
			int type = zoo.getRANDOM().nextInt(3) + 1;
			String fishTypeName;
			int age;
			double length = zoo.randomFishLength();
			int happiness = zoo.getRandomHappines();

			if (i <= 3 && i >= 1) {
				type = i;
			}
			try (Connection conn = DBUtil.getConnection()) {
				AnimalDAO animalDAO = new AnimalDAO(conn);
				Fish fish;

				switch (type) {
				case 1: // Aquarium Fish
					age = zoo.randomFishAge(AquariumFish.LIFE_SPAN);
					Pattern pattern = zoo.randomFishPattern();
					int numberOfColors = 1 + zoo.getRANDOM().nextInt(6);
					Colors[] colors = new Colors[numberOfColors];
					for (int j = 0; j < numberOfColors; j++) {
						colors[j] = zoo.randomFishColor();
					}
					animal_id = animalDAO.insertAnimalAndGetId(age, happiness, "Fish", "AquariumFish");
					fish = new AquariumFish(animal_id, age, length, pattern, colors, happiness);
					fishTypeName = "AquariumFish";
					break;
				case 2: // Gold Fish
					age = zoo.randomFishAge(GoldFish.LIFE_SPAN);
					Colors[] allowedColors = { Colors.ORANGE, Colors.GOLD, Colors.YELLOW, Colors.BLACK };
					Colors color = allowedColors[zoo.getRANDOM().nextInt(allowedColors.length)];
					animal_id = animalDAO.insertAnimalAndGetId(age, happiness, "Fish", "GoldFish");
					fish = new GoldFish(animal_id, age, length, color, happiness);
					fishTypeName = "GoldFish";
					break;
				case 3: // Clown Fish
					age = zoo.randomFishAge(ClownFish.LIFE_SPAN);
					animal_id = animalDAO.insertAnimalAndGetId(age, happiness, "Fish", "ClownFish");
					fish = new ClownFish(animal_id, age, length, happiness);
					fishTypeName = "ClownFish";
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + type);
				}
				zoo.addAnimal(fish);
				animalDAO.insertSubAnimalToDatabase(fish, "Fish", fishTypeName, 1);
			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Failed to add random fish to database.", "Database Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                    BIRD                                                                                          //                                        
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void addBird(Class<? extends Bird> birdClass) {
		String animalTypeName = birdClass.getSimpleName();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel nameLabel = new JLabel("Name:");
		JLabel ageLabel = new JLabel("Age:");
		JLabel heightLabel = new JLabel("Height:");

		JTextField nameField = new JTextField(10);
		JTextField ageField = new JTextField(10);
		JTextField heightField = new JTextField(10);

		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(nameLabel, gbc);
		panel.add(nameField, gbc);
		panel.add(ageLabel, gbc);
		panel.add(ageField, gbc);
		panel.add(heightLabel, gbc);
		panel.add(heightField, gbc);

		int result;
		boolean validInput = false;
		do {
			result = JOptionPane.showConfirmDialog(null, panel, "Enter the details for the new " + animalTypeName,
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.CANCEL_OPTION) {
				JOptionPane.showMessageDialog(null, animalTypeName + " addition cancelled.");
				return;
			}
			if (nameField.getText().isEmpty() || ageField.getText().isEmpty() || heightField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Hi friend!\nIn order to finish the " + animalTypeName.toLowerCase()
						+ " addition you have to enter all the data", "Warning", JOptionPane.WARNING_MESSAGE);
			} else {
				try {
					zooFacade.getAnimalValidation().validatePenguin(Integer.parseInt(ageField.getText()),
							Double.parseDouble(heightField.getText()));
					validInput = true;
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid values for age and height.",
							"Error", JOptionPane.ERROR_MESSAGE);
				} catch (InvalidAgeException | InvalidHeightException | InvalidLeaderPenguinHeightException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					ageField.setText("");
					heightField.setText("");
				}
			}
		} while (!validInput);

		try (Connection conn = DBUtil.getConnection()) {
			AnimalDAO animalDAO = new AnimalDAO(conn);

			int age = Integer.parseInt(ageField.getText());
			double height = Double.parseDouble(heightField.getText());
			int happiness = 100;
			String name = nameField.getText();

			int animal_id = animalDAO.insertAnimalAndGetId(age, happiness, "Bird", animalTypeName);

			Constructor<? extends Bird> constructor = birdClass.getConstructor(int.class, String.class, int.class,
					double.class, int.class);
			Bird bird = constructor.newInstance(animal_id, name, age, height, happiness);

			zoo.addAnimal(bird);
			animalDAO.insertSubAnimalToDatabase(bird, "Bird", animalTypeName, 0);

			JOptionPane.showMessageDialog(null, animalTypeName + " added successfully!");
			Music.playMusic("resorces\\" + animalTypeName + ".wav");

		} catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error adding " + animalTypeName + ".");
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                         INSECT                                                                                   //                                        
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void addInsect(Class<? extends Insects> insectClass) {
		String animalTypeName = insectClass.getSimpleName();

		JLabel ageLabel = new JLabel("Age:");
		JLabel venomousStatusLabel = new JLabel("Venomous:");
		JLabel spiderManStatusLabel = new JLabel("SpiderMan:");
		JLabel colorLabel = new JLabel("Color:");
		JLabel patternLabel = new JLabel("Pattern:");

		JTextField ageField = new JTextField(10);
		JComboBox<StatusCheck> venomousStatusBox = new JComboBox<>(StatusCheck.values());
		JComboBox<StatusCheck> spiderManStatusBox = new JComboBox<>(StatusCheck.values());
		JComboBox<Colors> colorBox = new JComboBox<>(Colors.values());
		JComboBox<Pattern> patternBox = new JComboBox<>(Pattern.values());

		JList<Colors> colorList = new JList<>(Colors.values());
		colorList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		int padding = 10;

		panel.add(ageLabel, gbc);
		gbc.gridy++;
		panel.add(ageField, gbc);
		gbc.gridy++;
		gbc.insets = new Insets(padding, 0, 0, 0);
		panel.add(venomousStatusLabel, gbc);
		gbc.gridy++;
		gbc.insets = new Insets(0, 0, 0, 0);
		panel.add(venomousStatusBox, gbc);
		gbc.gridy++;
		gbc.insets = new Insets(padding, 0, 0, 0);

		if (insectClass == Spider.class) {
			panel.add(spiderManStatusLabel, gbc);
			gbc.gridy++;
			gbc.insets = new Insets(0, 0, 0, 0);
			panel.add(spiderManStatusBox, gbc);
			gbc.gridy++;
			gbc.insets = new Insets(padding, 0, 0, 0);
			panel.add(colorLabel, gbc);
			gbc.gridy++;
			gbc.insets = new Insets(0, 0, 0, 0);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			panel.add(colorBox, gbc);
			gbc.gridy++;
			gbc.insets = new Insets(padding, 0, 0, 0);
			panel.add(patternLabel, gbc);
			gbc.gridy++;
			gbc.insets = new Insets(0, 0, 0, 0);
			panel.add(patternBox, gbc);
		}

		int result;
		boolean validInput = false;
		do {
			result = JOptionPane.showConfirmDialog(null, panel, "Enter the details for " + animalTypeName,
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.CANCEL_OPTION) {
				JOptionPane.showMessageDialog(null, animalTypeName + " addition cancelled.");
				return;
			}
			if (ageField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Hi friend!\nIn order to finish the " + animalTypeName
						+ " addition you have to enter all the data", "Warning", JOptionPane.WARNING_MESSAGE);
			} else {
				try {
					int age = Integer.parseInt(ageField.getText());
					zooFacade.getAnimalValidation().validateInsect(age, animalTypeName);
					validInput = true;
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Invalid input!\n\nPlease enter valid values for age.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (InvalidAgeException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					ageField.setText("");
				}
			}
		} while (!validInput || result == JOptionPane.OK_OPTION && ageField.getText().isEmpty());

		try (Connection conn = DBUtil.getConnection()) {
			AnimalDAO animalDAO = new AnimalDAO(conn);

			int age = Integer.parseInt(ageField.getText());
			int happiness = 100;
			int animal_id = animalDAO.insertAnimalAndGetId(age, happiness, "Insect", animalTypeName);

			Colors[] selectedColors = colorList.getSelectedValuesList().toArray(new Colors[0]);
			Pattern pattern = (Pattern) patternBox.getSelectedItem();
			StatusCheck venomous = (StatusCheck) venomousStatusBox.getSelectedItem();
			StatusCheck spiderMan = insectClass == Spider.class ? (StatusCheck) spiderManStatusBox.getSelectedItem()
					: StatusCheck.NO;

			Constructor<? extends Insects> constructor = insectClass.getConstructor(int.class, int.class,
					StatusCheck.class, Colors[].class, Pattern.class, StatusCheck.class, int.class);
			Insects insect = constructor.newInstance(animal_id, age, venomous, selectedColors, pattern, spiderMan,
					happiness);

			zoo.addAnimal(insect);
			animalDAO.insertSubAnimalToDatabase(insect, "Insect", animalTypeName, 0);

			JOptionPane.showMessageDialog(null, animalTypeName + " added successfully!");
			Music.playMusic("resorces\\" + (animalTypeName.equals("Bee") ? "Bee.wav" : "Spider.wav"));
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error adding " + animalTypeName + ".", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                      HERBIVORE                                                                                   //                                        
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void addHerbivore(Class<? extends Herbivore> herbivoreClass) {
		String animalTypeName = herbivoreClass.getSimpleName();

		JLabel nameLabel = new JLabel("Name:");
		JLabel ageLabel = new JLabel("Age:");
		JLabel weightLabel = new JLabel("Weight:");
		JLabel heightLabel = new JLabel("Height:");
		JLabel genderLabel = new JLabel("Gender:");

		JTextField nameField = new JTextField(10);
		JTextField ageField = new JTextField(10);
		JTextField weightField = new JTextField(10);
		JTextField heightField = new JTextField(10);
		JComboBox<Sex> genderBox = new JComboBox<>(Sex.values());

		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(nameLabel);
		panel.add(nameField);
		panel.add(ageLabel);
		panel.add(ageField);
		panel.add(weightLabel);
		panel.add(weightField);
		panel.add(heightLabel);
		panel.add(heightField);
		panel.add(genderLabel);
		panel.add(genderBox);

		int result;
		boolean validInput = false;
		do {
			result = JOptionPane.showConfirmDialog(null, panel, "Enter the details for " + animalTypeName,
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.CANCEL_OPTION) {
				JOptionPane.showMessageDialog(null, animalTypeName + " addition cancelled.");
				return;
			}
			if (nameField.getText().isEmpty() || ageField.getText().isEmpty() || weightField.getText().isEmpty()
					|| heightField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Hi friend!\nIn order to finish the " + animalTypeName
						+ " addition you have to enter all the data", "Warning", JOptionPane.WARNING_MESSAGE);
			} else {
				try {
					int age = Integer.parseInt(ageField.getText());
					double weight = Double.parseDouble(weightField.getText());
					double height = Double.parseDouble(heightField.getText());
					zooFacade.getAnimalValidation().validateHerbivore(age, weight, height, animalTypeName);
					validInput = true;
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null,
							"Invalid input!\n\nPlease enter valid values for age, weight, and height.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (InvalidAgeException | InvalidWeightException | InvalidHeightException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					ageField.setText("");
					weightField.setText("");
					heightField.setText("");
				}
			}
		} while (!validInput);

		try (Connection conn = DBUtil.getConnection()) {
			AnimalDAO animalDAO = new AnimalDAO(conn);
			int age = Integer.parseInt(ageField.getText());
			double weight = Double.parseDouble(weightField.getText());
			double height = Double.parseDouble(heightField.getText());
			Sex gender = (Sex) genderBox.getSelectedItem();
			String name = nameField.getText();
			int happiness = 100;

			int animal_id = animalDAO.insertAnimalAndGetId(age, happiness, "Herbivore", animalTypeName);

			Constructor<? extends Herbivore> constructor = herbivoreClass.getConstructor(int.class, String.class,
					int.class, double.class, double.class, Pattern.class, Sex.class, int.class);
			Herbivore herbivore = constructor.newInstance(0, name, age, weight, height, Pattern.LINES, gender,
					happiness);

			herbivore.setAnimalId(animal_id);
			zoo.addAnimal(herbivore);
			animalDAO.insertSubAnimalToDatabase(herbivore, "Herbivore", animalTypeName, 0);

			JOptionPane.showMessageDialog(null, animalTypeName + " added successfully!");
			Music.playMusic("resorces\\" + animalTypeName + ".wav");

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error adding " + animalTypeName + ".", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                       PREDATOR                                                                                   //                                        
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void addPredator(Class<? extends Predator> predatorClass) {
		String animalTypeName = predatorClass.getSimpleName();

		JLabel nameLabel = new JLabel("Name:");
		JLabel ageLabel = new JLabel("Age:");
		JLabel weightLabel = new JLabel("Weight:");
		JLabel genderLabel = new JLabel("Gender:");

		JTextField nameField = new JTextField(10);
		JTextField ageField = new JTextField(10);
		JTextField weightField = new JTextField(10);
		JComboBox<Sex> genderBox = new JComboBox<>(Sex.values());

		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(nameLabel);
		panel.add(nameField);
		panel.add(ageLabel);
		panel.add(ageField);
		panel.add(weightLabel);
		panel.add(weightField);
		panel.add(genderLabel);
		panel.add(genderBox);

		int result;
		boolean validInput = false;
		do {
			result = JOptionPane.showConfirmDialog(null, panel, "Enter the details for " + animalTypeName,
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.CANCEL_OPTION) {
				JOptionPane.showMessageDialog(null, animalTypeName + " addition cancelled.");
				return;
			}
			if (nameField.getText().isEmpty() || ageField.getText().isEmpty() || weightField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Hi friend!\nIn order to finish the " + animalTypeName
						+ " addition you have to enter all the data", "Warning", JOptionPane.WARNING_MESSAGE);
			} else {
				try {
					int age = Integer.parseInt(ageField.getText());
					double weight = Double.parseDouble(weightField.getText());
					zooFacade.getAnimalValidation().validatePredator(age, weight, animalTypeName);
					validInput = true;
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null,
							"Invalid input!\n\nPlease enter valid values for age and weight.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (InvalidAgeException | InvalidWeightException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					ageField.setText("");
					weightField.setText("");
				}
			}
		} while (!validInput);

		try (Connection conn = DBUtil.getConnection()) {
			AnimalDAO animalDAO = new AnimalDAO(conn);
			int age = Integer.parseInt(ageField.getText());
			double weight = Double.parseDouble(weightField.getText());
			Sex gender = (Sex) genderBox.getSelectedItem();
			String name = nameField.getText();
			int happiness = 100;

			int animal_id = animalDAO.insertAnimalAndGetId(age, happiness, "Predator", animalTypeName);

			Constructor<? extends Predator> constructor = predatorClass.getConstructor(int.class, String.class,
					int.class, double.class, Sex.class, int.class);
			Predator predator = constructor.newInstance(0, name, age, weight, gender, happiness);

			predator.setAnimalId(animal_id);
			zoo.addAnimal(predator);
			animalDAO.insertSubAnimalToDatabase(predator, "Predator", animalTypeName, 0);

			JOptionPane.showMessageDialog(null, animalTypeName + " added successfully!");
			Music.playMusic("resorces\\" + animalTypeName + ".wav");

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error adding " + animalTypeName + ".", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                     REPTILE                                                                                      //                                        
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void addReptile(Class<? extends Reptile> reptileClass) {
		String animalTypeName = reptileClass.getSimpleName();

		JLabel nameLabel = new JLabel("Name:");
		JLabel ageLabel = new JLabel("Age:");
		JLabel weightLabel = new JLabel("Weight:");
		JLabel lengthLabel = new JLabel("Length:");
		JLabel venomousStatusLabel = new JLabel("Venomous:");
		JLabel ninjaStatusLabel = new JLabel("Ninja:");
		JLabel colorLabel = new JLabel("Color:");
		JLabel colorsLabel = new JLabel("Use Ctrl Key for selecting multiple colors");

		JTextField nameField = new JTextField(10);
		JTextField ageField = new JTextField(10);
		JTextField weightField = new JTextField(10);
		JTextField lengthField = new JTextField(10);
		JComboBox<StatusCheck> venomousStatusBox = new JComboBox<>(StatusCheck.values());
		JComboBox<StatusCheck> ninjaStatusBox = new JComboBox<>(StatusCheck.values());
		JComboBox<Colors> colorBox = new JComboBox<>(
				new Colors[] { Colors.GREEN, Colors.WHITE, Colors.BLACK, Colors.BROWN });
		JList<Colors> colorList = new JList<>(Colors.values());
		colorList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane colorScrollPane = new JScrollPane(colorList);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		int padding = 10;

		panel.add(nameLabel, gbc);
		gbc.gridy++;
		panel.add(nameField, gbc);
		gbc.gridy++;
		gbc.insets = new Insets(padding, 0, 0, 0);
		panel.add(ageLabel, gbc);
		gbc.gridy++;
		gbc.insets = new Insets(0, 0, 0, 0);
		panel.add(ageField, gbc);
		gbc.gridy++;
		gbc.insets = new Insets(padding, 0, 0, 0);
		panel.add(weightLabel, gbc);
		gbc.gridy++;
		gbc.insets = new Insets(0, 0, 0, 0);
		panel.add(weightField, gbc);
		gbc.gridy++;
		gbc.insets = new Insets(padding, 0, 0, 0);
		panel.add(lengthLabel, gbc);
		gbc.gridy++;
		gbc.insets = new Insets(0, 0, 0, 0);
		panel.add(lengthField, gbc);
		gbc.gridy++;
		gbc.insets = new Insets(padding, 0, 0, 0);

		if (reptileClass == Snake.class) {
			panel.add(venomousStatusLabel, gbc);
			gbc.gridy++;
			gbc.insets = new Insets(0, 0, 0, 0);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			panel.add(venomousStatusBox, gbc);
			gbc.insets = new Insets(padding, 0, 0, 0);
			gbc.gridy++;
			panel.add(colorLabel, gbc);
			gbc.gridy++;
			gbc.insets = new Insets(0, 0, 0, 0);
			panel.add(colorsLabel, gbc);
			gbc.gridy++;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			panel.add(colorScrollPane, gbc);
			gbc.insets = new Insets(padding, 0, 0, 0);

		} else if (reptileClass == Turtle.class) {
			panel.add(ninjaStatusLabel, gbc);
			gbc.gridy++;
			gbc.insets = new Insets(0, 0, 0, 0);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			panel.add(ninjaStatusBox, gbc);
			gbc.insets = new Insets(padding, 0, 0, 0);
			gbc.gridy++;
			panel.add(colorLabel, gbc);
			gbc.gridy++;
			gbc.insets = new Insets(0, 0, 0, 0);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			panel.add(colorBox, gbc);
		}

		int result;
		boolean validInput = false;
		do {
			result = JOptionPane.showConfirmDialog(null, panel, "Enter the details for " + animalTypeName,
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.CANCEL_OPTION) {
				JOptionPane.showMessageDialog(null, animalTypeName + " addition cancelled.");
				return;
			}
			if (nameField.getText().isEmpty() || ageField.getText().isEmpty() || weightField.getText().isEmpty()
					|| lengthField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Hi friend!\nIn order to finish the " + animalTypeName
						+ " addition, you have to enter all the data", "Warning", JOptionPane.WARNING_MESSAGE);
			} else {
				try {
					int age = Integer.parseInt(ageField.getText());
					double weight = Double.parseDouble(weightField.getText());
					double length = Double.parseDouble(lengthField.getText());
					zooFacade.getAnimalValidation().validateReptile(age, weight, length, animalTypeName);
					validInput = true;
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null,
							"Invalid input!\n\nPlease enter valid values for age, weight, and length.", "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (InvalidAgeException | InvalidWeightException | InvalidLengthException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					ageField.setText("");
					weightField.setText("");
					lengthField.setText("");
				}
			}
		} while (!validInput);

		try (Connection conn = DBUtil.getConnection()) {
			AnimalDAO animalDAO = new AnimalDAO(conn);

			int age = Integer.parseInt(ageField.getText());
			double weight = Double.parseDouble(weightField.getText());
			double length = Double.parseDouble(lengthField.getText());
			String name = nameField.getText();
			StatusCheck venomous = (StatusCheck) venomousStatusBox.getSelectedItem();
			StatusCheck ninja = (StatusCheck) ninjaStatusBox.getSelectedItem();
			Colors[] selectedColors = colorList.getSelectedValuesList().toArray(new Colors[0]);
			int happiness = 100;

			int animal_id = animalDAO.insertAnimalAndGetId(age, happiness, "Reptile", animalTypeName);

			Constructor<? extends Reptile> constructor = reptileClass.getConstructor(int.class, String.class, int.class,
					double.class, double.class, StatusCheck.class, Colors[].class, StatusCheck.class, int.class);
			Reptile reptile = constructor.newInstance(0, name, age, weight, length, venomous, selectedColors, ninja,
					happiness);
			reptile.setAnimalId(animal_id);

			zoo.addAnimal(reptile);
			animalDAO.insertSubAnimalToDatabase(reptile, "Reptile", animalTypeName, 0);

			JOptionPane.showMessageDialog(null, animalTypeName + " added successfully!");
			Music.playMusic("resorces\\" + animalTypeName + ".wav");

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error adding " + animalTypeName + ".", "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
