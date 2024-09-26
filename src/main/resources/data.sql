SET REFERENTIAL_INTEGRITY FALSE ;

TRUNCATE TABLE FRIENDS;

TRUNCATE TABLE FILM_LIKES;

TRUNCATE TABLE GENRES RESTART IDENTITY;

TRUNCATE TABLE FILMS RESTART IDENTITY;

TRUNCATE TABLE USERS RESTART IDENTITY;

TRUNCATE TABLE RATING RESTART IDENTITY;

SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO USERS (email, login, name, birthday) VALUES
('john.doe@example.com', 'john_doe', 'John Doe', '1990-05-15'),
('jane.smith@example.com', 'jane_smith', 'Jane Smith', '1985-12-30'),
('alex.jones@example.com', 'alex_jones', 'Alex Jones', '2000-07-21'),
('mary.johnson@example.com', 'mary_j', 'Mary Johnson', '1995-04-10');

INSERT INTO RATING (name) VALUES
('G'),
('PG'),
('PG-13'),
('R'),
('NC-17');

INSERT INTO FILMS (name, description, releaseDate, duration, rating_id) VALUES
('The Matrix', 'A hacker discovers reality is an illusion.', '1999-03-31', 136, 4),
('Inception', 'A thief enters dreams to steal secrets.', '2010-07-16', 148, 3),
('Interstellar', 'A team of explorers travel through a wormhole in space.', '2014-11-07', 169, 3),
('The Godfather', 'The aging patriarch of an organized.', '1972-03-24', 175, 4);

INSERT INTO GENRES (name) VALUES
('Комедия'),
('Драма'),
('Мультфильм'),
('Триллер'),
('Документальный'),
('Боевик');

INSERT INTO FILM_GENRE (film_id, genre_id) VALUES
(1, 1),  -- The Matrix is Science Fiction
(1, 2),  -- The Matrix is also Action
(2, 1),  -- Inception is Science Fiction
(2, 4),  -- Inception is also Adventure
(3, 1),  -- Interstellar is Science Fiction
(3, 4),  -- Interstellar is also Adventure
(4, 3),  -- The Godfather is Drama
(4, 5);  -- The Godfather is also Crime

INSERT INTO FILM_LIKES (film_id, user_id) VALUES
(1, 1),  -- John Doe likes The Matrix
(1, 2),  -- Jane Smith likes The Matrix
(2, 3),  -- Alex Jones likes Inception
(3, 1),  -- John Doe likes Interstellar
(4, 4);  -- Mary Johnson likes The Godfather