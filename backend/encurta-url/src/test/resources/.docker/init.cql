CREATE KEYSPACE e_url_keyspace
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 3};

USE e_url_keyspace;

CREATE TABLE IF NOT EXISTS table_url_encurtada (
    hash text,
    url_original text,
    
    PRIMARY KEY (hash)
);
