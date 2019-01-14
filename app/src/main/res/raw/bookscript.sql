
DROP TABLE IF EXISTS Books;

CREATE TABLE Books (
    id integer NOT NULL PRIMARY KEY,
    title varchar(128) NOT NULL DEFAULT '',
    author varchar(128) NOT NULL DEFAULT ''
);
INSERT INTO books (id, title, author) VALUES (1, "The Idiot", "Fyodor Dostoevsky");
INSERT INTO books (id, title, author) VALUES (2, "Crime and punishment","Fyodor Dostoievsky");
INSERT INTO books (id, title, author) VALUES (3, "Anna Karenina","Leo Tolstoi");
INSERT INTO books (id, title, author) VALUES (4, "War and peace","Leo Tolstoi");

DROP TABLE IF EXISTS UserToken;

CREATE TABLE UserToken (
    token varchar(256) NOT NULL DEFAULT ''
);
