;              
CREATE USER IF NOT EXISTS SA SALT '2d78c4e66d95bd28' HASH 'c4fb6e44d4cde7e67b7eff7fea0ca84b662bb8ea74deea8dfb261941dd64168c' ADMIN;            
CREATE SCHEMA IF NOT EXISTS PROJECTION AUTHORIZATION SA;       
CREATE SCHEMA IF NOT EXISTS PERSON AUTHORIZATION SA;           
CREATE SCHEMA IF NOT EXISTS ADDRESS AUTHORIZATION SA;          
CREATE SCHEMA IF NOT EXISTS SAGA AUTHORIZATION SA;             
CREATE CACHED TABLE ADDRESS.ADDRESS(
    ADDRESS_ID VARCHAR(255) NOT NULL,
    PERSON_ID VARCHAR(255),
    STREET_AND_NUMBER VARCHAR(255),
    ZIP_CODE VARCHAR(255)
);        
ALTER TABLE ADDRESS.ADDRESS ADD CONSTRAINT ADDRESS.CONSTRAINT_E PRIMARY KEY(ADDRESS_ID);       
-- 0 +/- SELECT COUNT(*) FROM ADDRESS.ADDRESS; 
