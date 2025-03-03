CREATE DATABASE rsvp;

USE rsvp;

CREATE TABLE posts (
  post_id VARCHAR(8) NOT NULL,
  comments MEDIUMTEXT NULL,
  image MEDIUMBLOB NULL,
  PRIMARY KEY (`post_id`)
);

SELECT * FROM posts;