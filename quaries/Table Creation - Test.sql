CREATE TABLE Animal (
	animal_id SERIAL NOT NULL,
	age INT,
	happiness INT,
	type VARCHAR(30),
	species VARCHAR(30),
	CONSTRAINT pk_animal PRIMARY KEY (animal_id)
);

CREATE TABLE Bird (
	bird_id INT NOT NULL,
	name VARCHAR(50),
	height NUMERIC(9,2),
	CONSTRAINT pk_bird PRIMARY KEY (bird_id),
	CONSTRAINT fk_bird_animal FOREIGN KEY (bird_id) REFERENCES Animal(animal_id) ON DELETE CASCADE
);

CREATE TABLE Fish (
	fish_id INT NOT NULL,
	length NUMERIC(9,2),
	pattern VARCHAR(50),
	CONSTRAINT pk_fish PRIMARY KEY (fish_id),
	CONSTRAINT fk_fish_animal FOREIGN KEY (fish_id) REFERENCES Animal(animal_id) ON DELETE CASCADE
);

CREATE TABLE Predator (
	predator_id INT NOT NULL,
	name VARCHAR(50),
	weight NUMERIC(9,2),
	sex VARCHAR(50),
	CONSTRAINT pk_predator PRIMARY KEY (predator_id),
	CONSTRAINT fk_predator_animal FOREIGN KEY (predator_id) REFERENCES Animal(animal_id) ON DELETE CASCADE
);

CREATE TABLE Herbivore (
	herbivore_id INT NOT NULL,
	name VARCHAR(50),
	weight NUMERIC(9,2),
	height NUMERIC(9,2),
	pattern VARCHAR(50),
	sex VARCHAR(50),
	CONSTRAINT pk_herbivore PRIMARY KEY (herbivore_id),
	CONSTRAINT fk_herbivore_animal FOREIGN KEY (herbivore_id) REFERENCES Animal(animal_id) ON DELETE CASCADE
);

CREATE TABLE Insect (
	insect_id INT NOT NULL,
	venomous VARCHAR(10),
	spiderman VARCHAR(10),
	pattern VARCHAR(50),
	CONSTRAINT pk_insect PRIMARY KEY (insect_id),
	CONSTRAINT fk_insect_animal FOREIGN KEY (insect_id)
	REFERENCES Animal(animal_id)
	ON DELETE CASCADE
);

CREATE TABLE Reptile (
	reptile_id INT NOT NULL,
	name VARCHAR(50),
	length NUMERIC(9,2),
	weight NUMERIC(9,2),
	venomous VARCHAR(10),
	ninja VARCHAR(10),
	CONSTRAINT pk_reptile PRIMARY KEY (reptile_id),
	CONSTRAINT fk_reptile_animal FOREIGN KEY (reptile_id) 
	REFERENCES Animal(animal_id)
	ON DELETE CASCADE
);


CREATE TABLE AnimalColor (
    animal_id INT NOT NULL,
    color_name VARCHAR(30) NOT NULL,
    CONSTRAINT pk_animal_color PRIMARY KEY (animal_id, color_name),
    FOREIGN KEY (animal_id) REFERENCES Animal(animal_id) ON DELETE CASCADE
);

CREATE TABLE Person (
    person_id BIGINT NOT NULL,
    f_name VARCHAR(30),
    l_name VARCHAR(50),
    dob DATE,
    phone_number VARCHAR(50),
    CONSTRAINT pk_person PRIMARY KEY (person_id)
);


CREATE TABLE Employee (
    employee_id SERIAL NOT NULL,
    person_id BIGINT NOT NULL,
    username VARCHAR(100) NOT NULL,
	password_hash TEXT NOT NULL,
    CONSTRAINT pk_employee PRIMARY KEY (employee_id),
    CONSTRAINT fk_person FOREIGN KEY (person_id) REFERENCES Person(person_id)
);


CREATE TABLE Employee_Care_For_Animal (
    employee_id INT NOT NULL,
    animal_id INT NOT NULL,
    care_date DATE NOT NULL,
    care_time TIME NOT NULL,
	food_amount NUMERIC(9, 2),
	CONSTRAINT pk_employee_animal PRIMARY KEY (employee_id, animal_id, care_date, care_time),
    CONSTRAINT fk_employee FOREIGN KEY (employee_id) REFERENCES Employee(employee_id),
    CONSTRAINT fk_animal FOREIGN KEY (animal_id) REFERENCES Animal(animal_id)
);

CREATE TABLE Subscription (
	subscription_id SERIAL NOT NULL,
	type VARCHAR(50),
	name VARCHAR(50),
	price NUMERIC(7, 2),
	CONSTRAINT unique_subscription UNIQUE (type, name, price),
	CONSTRAINT pk_subscription PRIMARY KEY (subscription_id)
);

CREATE TABLE Visitor (
    visitor_id SERIAL NOT NULL,
    person_id BIGINT NOT NULL,
    employee_id INT NOT NULL,
    CONSTRAINT pk_visitor PRIMARY KEY (visitor_id),
    CONSTRAINT fk_person FOREIGN KEY (person_id) REFERENCES Person(person_id),
    CONSTRAINT fk_employee FOREIGN KEY (employee_id) REFERENCES Employee(employee_id)
);

CREATE TABLE Subscriber (
    subscriber_id INT NOT NULL,
	subscription_id INT NOT NULL,
	subscription_time TIME,
	subscription_date DATE,
	exp_date DATE,
    CONSTRAINT pk_subscriber PRIMARY KEY (subscriber_id),
    CONSTRAINT fk_visitor FOREIGN KEY (subscriber_id) REFERENCES Visitor(visitor_id) ON DELETE CASCADE,
    CONSTRAINT fk_subscription FOREIGN KEY (subscription_id) REFERENCES Subscription(subscription_id)
);

CREATE TABLE Promotion (
	promotion_id SERIAL NOT NULL,
	description VARCHAR(200),
	discount INT,
	CONSTRAINT pk_promotion PRIMARY KEY (promotion_id)
);

CREATE TABLE Subscriber_get_Promotion (
	promotion_id INT NOT NULL,
	subscriber_id INT NOT NULL,
	assigned_date DATE, 
	assigned_time TIME,
	CONSTRAINT pk_subscriber_promotion PRIMARY KEY (promotion_id, subscriber_id, assigned_date, assigned_time),
	CONSTRAINT fk_subscriber FOREIGN KEY (subscriber_id) REFERENCES Subscriber(subscriber_id) ON DELETE CASCADE,
    CONSTRAINT fk_promotion FOREIGN KEY (promotion_id) REFERENCES Promotion(promotion_id)
);

CREATE TABLE Ticket (
	ticket_id SERIAL NOT NULL,
	visitor_id INT NOT NULL,
	employee_id INT NOT NULL,
	promotion_id INT,
	type VARCHAR(50),
	price NUMERIC(6,2),
	is_canceled VARCHAR(10),
	is_used VARCHAR(10),
	rls_date DATE,
	exp_date DATE,
	entry_date DATE,
	CONSTRAINT pk_ticket PRIMARY KEY (ticket_id, visitor_id, employee_id),
	CONSTRAINT fk_owned_by_visitor FOREIGN KEY (visitor_id) REFERENCES Visitor(visitor_id) ON DELETE CASCADE,
	CONSTRAINT fk_issued_by_employee FOREIGN KEY (employee_id) REFERENCES Employee(employee_id),
	CONSTRAINT fk_ticket_promotion FOREIGN KEY (promotion_id) REFERENCES Promotion(promotion_id)
);






