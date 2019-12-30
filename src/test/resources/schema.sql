CREATE TABLE hash_table
(
  hash_index integer not null,
  primary key(hash_index)
);

CREATE TABLE dna (
  id integer AUTO_INCREMENT,
  hash_index integer NOT NULL,
  dna varchar(4096),
  mutant boolean not NULL ,
  PRIMARY KEY (id),
  FOREIGN KEY (hash_index) REFERENCES hash_table(hash_index) ON DELETE CASCADE


);


