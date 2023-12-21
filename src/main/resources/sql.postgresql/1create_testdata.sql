ALTER SEQUENCE nutzer_id_seq RESTART WITH 1;
ALTER SEQUENCE buch_id_seq RESTART WITH 1;

INSERT INTO nutzer(nutzername, passwort)
VALUES ('Leseratte', '1234'),
       ('Bücherwurm', '1234');

INSERT INTO buch(isbn)

INSERT INTO buch(isbn, titel, autor, coverbild)
VALUES ('9783518472026', 'Was die Wahrheit uns bedeutet', 'Maya Angelou', 'https://covers.openlibrary.org/b/isbn/9783518472026.jpg');

INSERT INTO buch(isbn, titel, autor, coverbild, tid)