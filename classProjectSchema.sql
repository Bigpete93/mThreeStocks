CREATE DATABASE project;

USE project;

CREATE TABLE week_data (week_date DATE, open FLOAT, high FLOAT, low FLOAT, close FLOAT, volume BIGINT, PRIMARY KEY (week_date));

CREATE TABLE day_data (day_date DATE, open FLOAT, high FLOAT, low FLOAT, close FLOAT, volume BIGINT, PRIMARY KEY (day_date));

CREATE TABLE 5_min_data (5_min_date DATE, 5_min_time TIME, open FLOAT, high FLOAT, low FLOAT, close FLOAT, volume BIGINT, PRIMARY KEY (5_min_date, 5_min_time));

--The ip here will need to change.
GRANT ALL ON project TO 'root'@'3.83.240.60';
GRANT ALL ON project TO 'root'@'localhost';
