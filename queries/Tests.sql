
SELECT * FROM Animal;
SELECT * FROM Bird;
SELECT * FROM Predator;
SELECT * FROM Herbivore;
SELECT h.*, a.species FROM Herbivore h JOIN Animal a ON a.animal_id = h.herbivore_id;
SELECT * FROM Reptile;
SELECT * FROM Insect;
SELECT h.*, a.* FROM Insect h JOIN Animal a ON a.animal_id = h.insect_id;
SELECT * FROM Fish;

ALTER TABLE Subscription DROP COLUMN exp_date;
ALTER TABLE Subscriber ADD exp_date DATE;
ALTER TABLE Ticket ALTER COLUMN ret_date TYPE VARCHAR(10);
ALTER TABLE Ticket RENAME COLUMN can_be_canceled TO is_canceled;

DELETE FROM Subscriber_get_Promotion;
DELETE FROM Employee_Care_For_Animal;
DELETE FROM Ticket;
DELETE FROM Subscriber;
DELETE FROM Visitor;
DELETE FROM Employee;
DELETE FROM Person;
DELETE FROM Subscription;
DELETE FROM Promotion;
DELETE FROM Animal;

SELECT * FROM AnimalColor;
SELECT * FROM Animal;
SELECT * FROM Employee_Care_For_Animal;
SELECT * FROM Person;
SELECT * FROM Visitor;
SELECT * FROM Subscriber;
SELECT * FROM Employee;
SELECT * FROM Promotion;
SELECT * FROM Subscription;
SELECT * FROM Subscriber_get_Promotion;
SELECT * FROM Ticket;


SELECT e.employee_id, e.username, p.* FROM Employee e JOIN Person p ON p.person_id = e.person_id;
SELECT v.visitor_id, v.employee_id, p.* FROM Visitor v JOIN Person p ON p.person_id = v.person_id;
SELECT p.f_name, p.l_name, t.* FROM Person p JOIN Visitor v ON v.person_id = p.person_id JOIN Ticket t ON t.visitor_id = v.visitor_id ;
SELECT sb.*, s.subscriber_id FROM Subscription sb JOIN Subscriber s ON sb.subscription_id = s.subscription_id;
SELECT sp.promotion_id, sp.subscriber_id, p.description, p.discount FROM Subscriber_get_Promotion sp JOIN Promotion p ON p.promotion_id = sp.promotion_id;


