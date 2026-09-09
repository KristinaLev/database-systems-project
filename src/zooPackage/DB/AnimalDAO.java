package zooPackage.DB;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import zooPackage.Entities.Animals.*;
import zooPackage.Entities.Animals.Bird.*;
import zooPackage.Entities.Animals.Fish.*;
import zooPackage.Entities.Animals.Herbivore.*;
import zooPackage.Entities.Animals.Insects.*;
import zooPackage.Entities.Animals.Predator.*;
import zooPackage.Entities.Animals.Reptile.*;
import zooPackage.Enum.*;

public class AnimalDAO {
	private final Connection conn;

	public AnimalDAO(Connection conn) {
		this.conn = conn;
	}

	public int insertAnimalAndGetId(int age, int happiness, String type, String species) throws SQLException {
		String sql = "INSERT INTO Animal (age, happiness, type, species) VALUES (?, ?, ?, ?) RETURNING animal_id";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, age);
			stmt.setInt(2, happiness);
			stmt.setString(3, type);
			stmt.setString(4, species);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("animal_id");
				}
			}
		}
		throw new SQLException("Failed to insert Animal and retrieve animal_id.");
	}

	public void updateAnimalAgeAndHappiness(int animalId, int newAge, int newHappiness) {
		String sql = "UPDATE Animal SET age = ?, happiness = ? WHERE animal_id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, newAge);
			stmt.setInt(2, newHappiness);
			stmt.setInt(3, animalId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void insertAnimalColors(int animal_id, Colors[] colors) throws SQLException {
		String sql = "INSERT INTO AnimalColor (animal_id, color_name) VALUES (?, ?) ON CONFLICT (animal_id, color_name) DO NOTHING";

		try (PreparedStatement insertStmt = conn.prepareStatement(sql)) {
			for (Colors color : colors) {
				insertStmt.setInt(1, animal_id);
				insertStmt.setString(2, color.name());
				insertStmt.addBatch();
			}
			insertStmt.executeBatch();
		}
	}

	public void insertSubAnimalToDatabase(Animal animal, String type, String species, int flag) throws SQLException {
		int animal_id = animal.getAnimalId();

		if (animal instanceof Bird bird) {
			String sql = "INSERT INTO Bird (bird_id, name, height) VALUES (?, ?, ?)";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, animal_id);
				if (flag == 1)
					stmt.setString(2, bird.getRandomName().name());
				else
					stmt.setString(2, bird.getName());
				stmt.setBigDecimal(3, BigDecimal.valueOf(bird.getHeight()));
				stmt.executeUpdate();
			}
		} else if (animal instanceof Predator predator) {
			String sql = "INSERT INTO Predator (predator_id, name, weight, sex) VALUES (?, ?, ?, ?)";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, animal_id);
				if (flag == 1)
					stmt.setString(2, predator.getRandomName().name());
				else
					stmt.setString(2, predator.getName());
				stmt.setBigDecimal(3, BigDecimal.valueOf(predator.getWeight()));
				stmt.setString(4, predator.getSex().name());
				stmt.executeUpdate();
			}
		} else if (animal instanceof Fish fish) {
			String sql = "INSERT INTO Fish (fish_id, length, pattern) VALUES (?, ?, ?)";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, animal_id);
				stmt.setBigDecimal(2, BigDecimal.valueOf(fish.getLength()));
				stmt.setString(3, fish.getPattern().name());
				stmt.executeUpdate();
			}
			insertAnimalColors(animal_id, fish.getColor());

		} else if (animal instanceof Herbivore herbivore) {
			String sql = "INSERT INTO Herbivore (herbivore_id, name, weight, height, pattern, sex) VALUES (?, ?, ?, ?, ?, ?)";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, animal_id);
				if (flag == 1)
					stmt.setString(2, herbivore.getRandomName().name());
				else
					stmt.setString(2, herbivore.getName());
				stmt.setBigDecimal(3, BigDecimal.valueOf(herbivore.getWeight()));
				stmt.setBigDecimal(4, BigDecimal.valueOf(herbivore.getHeight()));
				stmt.setString(5, herbivore.getPattern().name());
				stmt.setString(6, herbivore.getSex().name());
				stmt.executeUpdate();
			}
		} else if (animal instanceof Insects insect) {
			String sql = "INSERT INTO Insect (insect_id, venomous, spiderman, pattern) VALUES (?, ?, ?, ?)";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, animal_id);
				stmt.setString(2, insect.isVenomous().name());
				stmt.setString(3, insect.isSpiderMan().name());
				stmt.setString(4, insect.getPattern().name());
				stmt.executeUpdate();
			}
			insertAnimalColors(animal_id, insect.getColor());

		} else if (animal instanceof Reptile reptile) {
			String sql = "INSERT INTO Reptile (reptile_id, name, length, weight, venomous, ninja) VALUES (?, ?, ?, ?, ?, ?)";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, animal_id);
				if (flag == 1)
					stmt.setString(2, reptile.getRandomName().name());
				else
					stmt.setString(2, reptile.getName());
				stmt.setBigDecimal(3, BigDecimal.valueOf(reptile.getLength()));
				stmt.setBigDecimal(4, BigDecimal.valueOf(reptile.getWeight()));
				stmt.setString(5, reptile.isVenomous().name());
				stmt.setString(6, reptile.isNinga().name());
				stmt.executeUpdate();
			}
			insertAnimalColors(animal_id, reptile.getColor());

		} else {
			throw new IllegalArgumentException("Unknown animal subclass: " + animal.getClass().getSimpleName());
		}
	}

	public List<AnimalRecord> getAllAnimalRecords() throws SQLException {
		List<AnimalRecord> allAnimals = new ArrayList<>();

		allAnimals.addAll(getAllPredatorsFromDB());
		allAnimals.addAll(getAllBirdsFromDB());
		allAnimals.addAll(getAllFishFromDB());
		allAnimals.addAll(getAllHerbivoresFromDB());
		allAnimals.addAll(getAllInsectsFromDB());
		allAnimals.addAll(getAllReptilesFromDB());

		return allAnimals;
	}

	public List<AnimalRecord> getAllPredatorsFromDB() throws SQLException {
		List<AnimalRecord> predators = new ArrayList<>();

		String sql = """
				SELECT a.animal_id, a.age, a.happiness, a.species,
				       p.weight, p.sex, p.name
				FROM Animal a
				JOIN Predator p ON a.animal_id = p.predator_id
				WHERE a.type = 'Predator'
				""";

		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				int animalId = rs.getInt("animal_id");
				int age = rs.getInt("age");
				int happiness = rs.getInt("happiness");
				String species = rs.getString("species");
				double weight = rs.getDouble("weight");
				Sex sex = Sex.valueOf(rs.getString("sex"));
				String name = rs.getString("name");

				Predator predator;
				switch (species) {
				case "Tiger" -> predator = new Tiger(animalId, name, age, weight, sex, happiness);
				case "Lion" -> predator = new Lion(animalId, name, age, weight, sex, happiness);
				default -> throw new SQLException("Unknown predator species: " + species);
				}

				predators.add(new AnimalRecord("Predator", species, predator));
			}
		}

		return predators;
	}

	public List<AnimalRecord> getAllFishFromDB() throws SQLException {
		List<AnimalRecord> fishList = new ArrayList<>();

		String sql = """
				SELECT a.animal_id, a.age, a.happiness, a.species, f.*, c.color_name
				FROM Animal a
				JOIN Fish f ON a.animal_id = f.fish_id
				LEFT JOIN AnimalColor c ON f.fish_id = c.animal_id
				WHERE a.type = 'Fish'
				""";

		Map<Integer, List<Colors>> colorMap = new HashMap<>();

		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				int animalId = rs.getInt("animal_id");
				String colorStr = rs.getString("color_name");
				if (colorStr != null) {
					Colors color = Colors.valueOf(colorStr);
					colorMap.computeIfAbsent(animalId, k -> new ArrayList<>()).add(color);
				}
			}
		}

		String baseFishSql = """
				SELECT a.animal_id, a.age, a.happiness, a.species, f.*
				FROM Animal a
				JOIN Fish f ON a.animal_id = f.fish_id
				WHERE a.type = 'Fish'
				""";

		try (PreparedStatement stmt = conn.prepareStatement(baseFishSql); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				int animalId = rs.getInt("animal_id");
				int age = rs.getInt("age");
				int happiness = rs.getInt("happiness");
				String species = rs.getString("species");
				double length = rs.getDouble("length");
				Pattern pattern = Pattern.valueOf(rs.getString("pattern"));
				List<Colors> colors = colorMap.getOrDefault(animalId, List.of());

				Fish fish;
				switch (species) {
				case "AquariumFish" ->
					fish = new AquariumFish(animalId, age, length, pattern, colors.toArray(new Colors[0]), happiness);
				case "GoldFish" -> fish = new GoldFish(animalId, age, length, colors.get(0), happiness);
				case "ClownFish" -> fish = new ClownFish(animalId, age, length, happiness);
				default -> throw new SQLException("Unknown fish species: " + species);
				}

				fishList.add(new AnimalRecord("Fish", species, fish));
			}
		}

		return fishList;
	}

	public List<AnimalRecord> getAllBirdsFromDB() throws SQLException {
		List<AnimalRecord> birdList = new ArrayList<>();
		String baseSql = """
				SELECT a.animal_id, a.age, a.happiness, a.species, b.*
				FROM Animal a
				JOIN Bird b ON a.animal_id = b.bird_id
				WHERE a.type = 'Bird'
				""";

		try (PreparedStatement stmt = conn.prepareStatement(baseSql); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				int animalId = rs.getInt("animal_id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int happiness = rs.getInt("happiness");
				String species = rs.getString("species");
				double height = rs.getDouble("height");

				Bird bird;
				switch (species) {
				case "Parrot" -> bird = new Parrot(animalId, name, age, height, happiness);
				case "Penguin" -> bird = new Penguin(animalId, name, age, height, happiness);
				default -> throw new SQLException("Unknown bird species: " + species);
				}

				birdList.add(new AnimalRecord("Bird", species, bird));
			}
		}

		return birdList;
	}

	public List<AnimalRecord> getAllHerbivoresFromDB() throws SQLException {
		List<AnimalRecord> herbivoreList = new ArrayList<>();
		String baseSql = """
				SELECT a.animal_id, a.age, a.happiness, a.species, h.*
				FROM Animal a
				JOIN Herbivore h ON a.animal_id = h.herbivore_id
				WHERE a.type = 'Herbivore'
				""";

		try (PreparedStatement stmt = conn.prepareStatement(baseSql); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				int animalId = rs.getInt("animal_id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int happiness = rs.getInt("happiness");
				String species = rs.getString("species");
				double height = rs.getDouble("height");
				double weight = rs.getDouble("weight");
				Pattern pattern = Pattern.valueOf(rs.getString("pattern"));
				Sex sex = Sex.valueOf(rs.getString("sex"));

				Herbivore herbivore;
				switch (species) {
				case "Zebra" -> herbivore = new Zebra(animalId, name, age, weight, height, pattern, sex, happiness);
				case "Elephant" ->
					herbivore = new Elephant(animalId, name, age, weight, height, pattern, sex, happiness);
				default -> throw new SQLException("Unknown herbivore species: " + species);
				}

				herbivoreList.add(new AnimalRecord("Herbivore", species, herbivore));
			}
		}

		return herbivoreList;
	}

	public List<AnimalRecord> getAllReptilesFromDB() throws SQLException {
		List<AnimalRecord> reptileList = new ArrayList<>();
		String sql = """
				SELECT a.animal_id, a.age, a.happiness, a.species, r.*, c.color_name
				FROM Animal a
				JOIN Reptile r ON a.animal_id = r.reptile_id
				LEFT JOIN AnimalColor c ON r.reptile_id = c.animal_id
				WHERE a.type = 'Reptile'
				""";

		Map<Integer, List<Colors>> colorMap = new HashMap<>();

		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				int animalId = rs.getInt("animal_id");
				String colorStr = rs.getString("color_name");
				if (colorStr != null) {
					Colors color = Colors.valueOf(colorStr);
					colorMap.computeIfAbsent(animalId, k -> new ArrayList<>()).add(color);
				}
			}
		}

		String baseSql = """
				SELECT a.animal_id, a.age, a.happiness, a.species, r.*
				FROM Animal a
				JOIN Reptile r ON a.animal_id = r.reptile_id
				WHERE a.type = 'Reptile'
				""";

		try (PreparedStatement stmt = conn.prepareStatement(baseSql); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				int animalId = rs.getInt("animal_id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				int happiness = rs.getInt("happiness");
				String species = rs.getString("species");
				double length = rs.getDouble("length");
				double weight = rs.getDouble("weight");
				StatusCheck isVenomous = StatusCheck.valueOf(rs.getString("venomous"));
				StatusCheck isNinja = StatusCheck.valueOf(rs.getString("ninja"));
				List<Colors> colors = colorMap.getOrDefault(animalId, List.of());

				Reptile reptile;
				switch (species) {
				case "Snake" -> reptile = new Snake(animalId, name, age, weight, length, isVenomous,
						colors.toArray(new Colors[0]), isNinja, happiness);
				case "Turtle" -> reptile = new Turtle(animalId, name, age, weight, length, isVenomous,
						colors.toArray(new Colors[0]), isNinja, happiness);
				default -> throw new SQLException("Unknown reptile species: " + species);
				}

				reptileList.add(new AnimalRecord("Reptile", species, reptile));
			}
		}

		return reptileList;
	}

	public List<AnimalRecord> getAllInsectsFromDB() throws SQLException {
		List<AnimalRecord> reptileList = new ArrayList<>();
		String sql = """
				SELECT a.animal_id, a.age, a.happiness, a.species, i.*, c.color_name
				FROM Animal a
				JOIN Insect i ON a.animal_id = i.insect_id
				LEFT JOIN AnimalColor c ON i.insect_id = c.animal_id
				WHERE a.type = 'Insect'
				""";

		Map<Integer, List<Colors>> colorMap = new HashMap<>();

		try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				int animalId = rs.getInt("animal_id");
				String colorStr = rs.getString("color_name");
				if (colorStr != null) {
					Colors color = Colors.valueOf(colorStr);
					colorMap.computeIfAbsent(animalId, k -> new ArrayList<>()).add(color);
				}
			}
		}

		String baseSql = """
				SELECT a.animal_id, a.age, a.happiness, a.species, i.*
				FROM Animal a
				JOIN Insect i ON a.animal_id = i.insect_id
				WHERE a.type = 'Insect'
				""";

		try (PreparedStatement stmt = conn.prepareStatement(baseSql); ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				int animalId = rs.getInt("animal_id");
				int age = rs.getInt("age");
				int happiness = rs.getInt("happiness");
				String species = rs.getString("species");
				StatusCheck isVenomous = StatusCheck.valueOf(rs.getString("venomous"));
				StatusCheck isSpiderman = StatusCheck.valueOf(rs.getString("spiderman"));
				List<Colors> colors = colorMap.getOrDefault(animalId, List.of());
				Pattern pattern = Pattern.valueOf(rs.getString("pattern"));

				Insects insect;
				switch (species) {
				case "Spider" -> insect = new Spider(animalId, age, isVenomous, colors.toArray(new Colors[0]), pattern,
						isSpiderman, happiness);
				case "Bee" -> insect = new Bee(animalId, age, isVenomous, colors.toArray(new Colors[0]), pattern,
						isSpiderman, happiness);
				default -> throw new SQLException("Unknown insect species: " + species);
				}

				reptileList.add(new AnimalRecord("Insect", species, insect));
			}
		}

		return reptileList;
	}

	public void deleteAnimalFromDatabase(Animal animal) throws SQLException {
		int animal_id = animal.getAnimalId();

		if (animal instanceof Bird) {
			try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Bird WHERE bird_id = ?")) {
				stmt.setInt(1, animal_id);
				stmt.executeUpdate();
			}
		} else if (animal instanceof Predator) {
			try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Predator WHERE predator_id = ?")) {
				stmt.setInt(1, animal_id);
				stmt.executeUpdate();
			}
		} else if (animal instanceof Reptile) {
			try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Reptile WHERE reptile_id = ?")) {
				stmt.setInt(1, animal_id);
				stmt.executeUpdate();
			}
		} else if (animal instanceof Herbivore) {
			try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Herbivore WHERE herbivore_id = ?")) {
				stmt.setInt(1, animal_id);
				stmt.executeUpdate();
			}
		} else if (animal instanceof Insects) {
			try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Insect WHERE insect_id = ?")) {
				stmt.setInt(1, animal_id);
				stmt.executeUpdate();
			}
		} else if (animal instanceof Fish) {
			try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Fish WHERE fish_id = ?")) {
				stmt.setInt(1, animal_id);
				stmt.executeUpdate();
			}
		}

		try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Animal WHERE animal_id = ?")) {
			stmt.setInt(1, animal_id);
			stmt.executeUpdate();
		}
	}

}
