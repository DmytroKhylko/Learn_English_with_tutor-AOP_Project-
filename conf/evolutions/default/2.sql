-- Relations schema
-- !Ups
CREATE TABLE relations (
  id int NOT NULL AUTO_INCREMENT,
  user varchar(45) NOT NULL,
  linkedUser varchar(45) NOT NULL,
  PRIMARY KEY (id)
);
-- !Downs
DROP TABLE relations;
