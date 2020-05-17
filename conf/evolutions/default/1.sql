-- Users schema
-- !Ups
CREATE TABLE users (
  id int NOT NULL AUTO_INCREMENT,
  login varchar(45) NOT NULL UNIQUE ,
  password varchar(100) NOT NULL,
  status varchar(45) NOT NULL,
  PRIMARY KEY (id)
);
-- !Downs
DROP TABLE users;
