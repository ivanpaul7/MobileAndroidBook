
DROP TABLE IF EXISTS SyncBooks;

CREATE TABLE SyncBooks (
    id integer NOT NULL PRIMARY KEY,
    title varchar(128) NOT NULL DEFAULT '',
    author varchar(128) NOT NULL DEFAULT '',
    isFromServer integer,
    isModified integer,
    isActive integer
);


DROP TABLE IF EXISTS UserToken;

CREATE TABLE UserToken (
    token varchar(256) NOT NULL DEFAULT ''
);
