CREATE TABLE IF NOT EXISTS users (
  id integer PRIMARY KEY,
  email varchar,
  login varchar,
  name varchar,
  birthday date
);

CREATE TABLE IF NOT EXISTS friends (
  user1_id integer,
  user2_id integer,
  PRIMARY KEY (user1_id, user2_id)
);

CREATE TABLE IF NOT EXISTS films (
  id integer PRIMARY KEY,
  name varchar,
  description text,
  releaseDate date,
  duration integer,
  rating_id integer
);

CREATE TABLE IF NOT EXISTS rating (
  id integer PRIMARY KEY,
  name varchar
);

CREATE TABLE IF NOT EXISTS genres (
  id integer PRIMARY KEY,
  name varchar
);

CREATE TABLE IF NOT EXISTS film_genre (
  film_id integer,
  genre_id integer
);

CREATE TABLE IF NOT EXISTS film_likes (
  film_id integer,
  user_id integer,
  PRIMARY KEY (film_id, user_id)
);

ALTER TABLE friends ADD FOREIGN KEY (user1_id) REFERENCES users (id);

ALTER TABLE friends ADD FOREIGN KEY (user2_id) REFERENCES users (id);

ALTER TABLE film_genre ADD FOREIGN KEY (film_id) REFERENCES films (id);

ALTER TABLE film_genre ADD FOREIGN KEY (genre_id) REFERENCES genres (id);

ALTER TABLE film_likes ADD FOREIGN KEY (film_id) REFERENCES films (id);

ALTER TABLE film_likes ADD FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE films ADD FOREIGN KEY (rating_id) REFERENCES rating (id);
