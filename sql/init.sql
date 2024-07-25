--Table: public.task
SELECT 'CREATE DATABASE busdb'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'busdb');

DROP TABLE IF EXISTS task;

CREATE TABLE IF NOT EXISTS task
(
    id SERIAL,
    "lineRef" VARCHAR(10) NOT NULL COLLATE pg_catalog."default",
    "lineName" VARCHAR(50) NOT NULL COLLATE pg_catalog."default",
    "stopRef" INTEGER NOT NULL,
	"stopName" VARCHAR (50) NOT NULL COLLATE pg_catalog."default",
	"directionRef" VARCHAR (10) COLLATE pg_catalog."default",
	"occupancy" VARCHAR (20) COLLATE pg_catalog."default",
	"arrivalTime" VARCHAR (30) COLLATE pg_catalog."default",
    CONSTRAINT task_pkey PRIMARY KEY (id,"lineRef","stopRef","directionRef","arrivalTime")
);

ALTER TABLE IF EXISTS task
    OWNER to bus;

GRANT ALL ON TABLE task TO bus WITH GRANT OPTION;

GRANT ALL ON TABLE task TO bus;