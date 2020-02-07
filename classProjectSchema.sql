CREATE DATABASE project;

USE project;

CREATE TABLE week_data (week_date DATE, open FLOAT, high FLOAT, low FLOAT, close FLOAT, volume BIGINT, PRIMARY KEY (week_date));

CREATE TABLE day_data (day_date DATE, open FLOAT, high FLOAT, low FLOAT, close FLOAT, volume BIGINT, PRIMARY KEY (day_date));

CREATE TABLE five_min_data (five_min_date DATE, five_min_time TIME, open FLOAT, high FLOAT, low FLOAT, close FLOAT, volume BIGINT, PRIMARY KEY (five_min_date, five_min_time));

--The ip here will need to change.
create user 'mysql'@'198.105.46.201' identified by 'mysql';
grant all privileges on project to 'mysql'@'198.105.46.201' identified by 'mysql';
