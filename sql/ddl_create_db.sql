DROP TABLESPACE IF EXISTS ts_spring_boot_crm;
CREATE TABLESPACE ts_spring_boot_crm owner postgres location 'D:\PostgreSQL\tablespace';
CREATE DATABASE "db_spring_boot_crm"
WITH
  OWNER = "postgres"
  ENCODING = 'UTF8'
  TABLESPACE = "ts_spring_boot_crm";
