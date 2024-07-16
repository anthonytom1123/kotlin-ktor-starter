DROP TABLE IF EXISTS task;
CREATE TABLE task(
    id SERIAL PRIMARY KEY,
    lineRef VARCHAR(10),
    lineName VARCHAR(50),
    stopRef INTEGER,
    stopName VARCHAR(50),
    directionRef VARCHAR(10),
    occupancy VARCHAR(20),
    arrivalTime VARCHAR(30)
);

INSERT INTO task (name, description, priority) VALUES ('cleaning', 'Clean the house', 'Low');
INSERT INTO task (lineRef, lineName, stopRef, stopName, directionRef, occupancy, arrivalTime)
VALUES (
    '1001',
    'Test Line',
    '000000',
    'Divisadero St & Clay St',
    'OB',
    'seatsAvailable',
    '2024-06-27T18:29:59Z'
)