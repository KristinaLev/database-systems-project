CREATE TABLE Animal (
	animal_id SERIAL NOT NULL,
	age INT,
	happiness INT,
	type VARCHAR(30),
	CONSTRAINT pk_animal PRIMARY KEY (animal_id)
);

CREATE TABLE Bird (
	bird_id INT NOT NULL,
	name VARCHAR(50),
	height NUMERIC(5,2),
	species VARCHAR(30),
	CONSTRAINT pk_bird PRIMARY KEY (bird_id),
	CONSTRAINT fk_bird_animal FOREIGN KEY (bird_id) REFERENCES Animal(animal_id)
);

CREATE TABLE Penguin (
	penguin_id INT NOT NULL,
	CONSTRAINT pk_penguin PRIMARY KEY (penguin_id),
	CONSTRAINT fk_penguin_bird FOREIGN KEY (penguin_id) REFERENCES Bird(bird_id)
);

CREATE TABLE Parrot (
	parrot_id INT NOT NULL,
	CONSTRAINT pk_parrot PRIMARY KEY (parrot_id),
	CONSTRAINT fk_parrot_bird FOREIGN KEY (parrot_id) REFERENCES Bird(bird_id)
);

CREATE TABLE Fish (
	fish_id INT NOT NULL,
	length NUMERIC(5,2),
	pattern VARCHAR(50),
	species VARCHAR(30),
	CONSTRAINT pk_fish PRIMARY KEY (fish_id),
	CONSTRAINT fk_fish_animal FOREIGN KEY (fish_id) REFERENCES Animal(animal_id)
);

CREATE TABLE Aquarium (
	aquarium_id INT NOT NULL,
	CONSTRAINT pk_aquarium PRIMARY KEY (aquarium_id),
	CONSTRAINT fk_aquarium_fish FOREIGN KEY (aquarium_id) REFERENCES Fish(fish_id)
);

CREATE TABLE Clown (
	clown_id INT NOT NULL,
	CONSTRAINT pk_clown PRIMARY KEY (clown_id),
	CONSTRAINT fk_clown_fish FOREIGN KEY (clown_id) REFERENCES Fish(fish_id)
);

CREATE TABLE Gold (
	gold_id INT NOT NULL,
	CONSTRAINT pk_gold PRIMARY KEY (gold_id),
	CONSTRAINT fk_gold_fish FOREIGN KEY (gold_id) REFERENCES Fish(fish_id)
);

CREATE TABLE Predator (
	predator_id INT NOT NULL,
	name VARCHAR(50),
	weight NUMERIC(5,2),
	sex VARCHAR(50),
	species VARCHAR(30),
	CONSTRAINT pk_predator PRIMARY KEY (predator_id),
	CONSTRAINT fk_predator_animal FOREIGN KEY (predator_id) REFERENCES Animal(animal_id)
);

CREATE TABLE Lion (
	lion_id INT NOT NULL,
	CONSTRAINT pk_lion PRIMARY KEY (lion_id),
	CONSTRAINT fk_lion_predator FOREIGN KEY (lion_id) REFERENCES Predator(predator_id)
);

CREATE TABLE Tiger (
	tiger_id INT NOT NULL,
	CONSTRAINT pk_tiger PRIMARY KEY (tiger_id),
	CONSTRAINT fk_tiger_predator FOREIGN KEY (tiger_id) REFERENCES Predator(predator_id)
);

CREATE TABLE Herbivore (
	herbivore_id INT NOT NULL,
	name VARCHAR(50),
	weight NUMERIC(5,2),
	height NUMERIC(5,2),
	pattern VARCHAR(50),
	sex VARCHAR(50),
	species VARCHAR(30),
	CONSTRAINT pk_herbivore PRIMARY KEY (herbivore_id),
	CONSTRAINT fk_herbivore_animal FOREIGN KEY (herbivore_id) REFERENCES Animal(animal_id)
);

CREATE TABLE Zebra (
	zebra_id INT NOT NULL,
	CONSTRAINT pk_zebra PRIMARY KEY (zebra_id),
	CONSTRAINT fk_zebra_herbivore FOREIGN KEY (zebra_id) REFERENCES Herbivore(herbivore_id)
);

CREATE TABLE Elephant (
	elephant_id INT NOT NULL,
	CONSTRAINT pk_elephant PRIMARY KEY (elephant_id),
	CONSTRAINT fk_elephant_herbivore FOREIGN KEY (elephant_id) REFERENCES Herbivore(herbivore_id)
);

CREATE TABLE Insect (
	insect_id INT NOT NULL,
	venomous BOOLEAN,
	spiderman BOOLEAN,
	pattern VARCHAR(50),
	species VARCHAR(30),
	CONSTRAINT pk_insect PRIMARY KEY (insect_id),
	CONSTRAINT fk_insect_animal FOREIGN KEY (insect_id) REFERENCES Animal(animal_id)
);

CREATE TABLE Bee (
	bee_id INT NOT NULL,
	CONSTRAINT pk_bee PRIMARY KEY (bee_id),
	CONSTRAINT fk_bee_insect FOREIGN KEY (bee_id) REFERENCES Insect(insect_id)
);

CREATE TABLE Spider (
	spider_id INT NOT NULL,
	CONSTRAINT pk_spider PRIMARY KEY (spider_id),
	CONSTRAINT fk_spider_insect FOREIGN KEY (spider_id) REFERENCES Insect(insect_id)
);

CREATE TABLE Reptile (
	reptile_id INT NOT NULL,
	name VARCHAR(50),
	length NUMERIC(5,2),
	weight NUMERIC(5,2),
	venomous BOOLEAN,
	ninja BOOLEAN,
	species VARCHAR(30),
	CONSTRAINT pk_reptile PRIMARY KEY (reptile_id),
	CONSTRAINT fk_reptile_animal FOREIGN KEY (reptile_id) REFERENCES Animal(animal_id)
);

CREATE TABLE Snake (
	snake_id INT NOT NULL,
	CONSTRAINT pk_snake PRIMARY KEY (snake_id),
	CONSTRAINT fk_snake_reptile FOREIGN KEY (snake_id) REFERENCES Reptile(reptile_id)
);

CREATE TABLE Turtle (
	turtle_id INT NOT NULL,
	CONSTRAINT pk_turtle PRIMARY KEY (turtle_id),
	CONSTRAINT fk_turtle_reptile FOREIGN KEY (turtle_id) REFERENCES Reptile(reptile_id)
);

CREATE TABLE Employee (
	employee_id INT NOT NULL,
	username VARCHAR(100) NOT NULL,
	f_name VARCHAR(50),
	l_name VARCHAR(50),
	id INT NOT NULL,
	dob DATE,
	phone_number VARCHAR(50),
	CONSTRAINT pk_employee PRIMARY KEY (employee_id)
);

CREATE TABLE Employee_Care_For_Animal (
	employee_id INT NOT NULL,
	animal_id INT NOT NULL,
	CONSTRAINT pk_employee_animal PRIMARY KEY (employee_id, animal_id),
	CONSTRAINT fk_employee FOREIGN KEY (employee_id) REFERENCES Employee(employee_id),
	CONSTRAINT fk_animal FOREIGN KEY (animal_id) REFERENCES Animal(animal_id)
);

CREATE TABLE Visitor (
	visitor_id INT NOT NULL,
	employee_id INT NOT NULL,
	f_name VARCHAR(50),
	l_name VARCHAR(50),
	id INT NOT NULL,
	dob DATE,
	phone_number VARCHAR(50),
	CONSTRAINT pk_visitor PRIMARY KEY (visitor_id),
	CONSTRAINT fk_registered_by_employee FOREIGN KEY (employee_id) REFERENCES Employee(employee_id)
);

CREATE TABLE Promotion (
	promotion_id SERIAL NOT NULL,
	description VARCHAR(200),
	discount NUMERIC(3,2),
	CONSTRAINT pk_promotion PRIMARY KEY (promotion_id)
);

CREATE TABLE Ticket (
	ticket_id INT NOT NULL,
	visitor_id INT NOT NULL,
	employee_id INT NOT NULL,
	promotion_id INT,
	type VARCHAR(50),
	price NUMERIC(6,2),
	can_be_canceled BOOLEAN,
	is_used BOOLEAN,
	rls_date DATE,
	exp_date DATE,
	ret_date DATE,
	CONSTRAINT pk_ticket PRIMARY KEY (ticket_id, visitor_id, employee_id),
	CONSTRAINT fk_owned_by_visitor FOREIGN KEY (visitor_id) REFERENCES Visitor(visitor_id),
	CONSTRAINT fk_issued_by_employee FOREIGN KEY (employee_id) REFERENCES Employee(employee_id),
	CONSTRAINT fk_ticket_promotion FOREIGN KEY (promotion_id) REFERENCES Promotion(promotion_id)
);

CREATE TABLE Visitor_Subscribed_To_Promotion (
	visitor_id INT NOT NULL,
	promotion_id INT NOT NULL,
	CONSTRAINT pk_visitor_promotion PRIMARY KEY (visitor_id, promotion_id),
	CONSTRAINT fk_visitor FOREIGN KEY (visitor_id) REFERENCES Visitor(visitor_id),
	CONSTRAINT fk_promotion FOREIGN KEY (promotion_id) REFERENCES Promotion(promotion_id)
);

CREATE TABLE Color (
    color_id SERIAL NOT NULL,
    name VARCHAR(50) UNIQUE NOT NULL,
	CONSTRAINT pk_color PRIMARY KEY (color_id)
);

CREATE TABLE AnimalColor (
    animal_id INT NOT NULL,
    color_id INT NOT NULL,
    CONSTRAINT pk_animal_color PRIMARY KEY (animal_id, color_id),
    CONSTRAINT fk_animal FOREIGN KEY (animal_id) REFERENCES Animal(animal_id),
    CONSTRAINT fk_color FOREIGN KEY (color_id) REFERENCES Color(color_id)
);



