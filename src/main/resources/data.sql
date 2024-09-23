SET REFERENTIAL_INTEGRITY FALSE ;

TRUNCATE TABLE FRIENDS;

TRUNCATE TABLE FILM_LIKES;

TRUNCATE TABLE GENRES RESTART IDENTITY;

TRUNCATE TABLE FILMS RESTART IDENTITY;

TRUNCATE TABLE USERS RESTART IDENTITY;

TRUNCATE TABLE RATING RESTART IDENTITY;

SET REFERENTIAL_INTEGRITY TRUE;

INSERT INTO USERS (id, email, login, name, birthday) VALUES
(1, 'john.doe@example.com', 'john_doe', 'John Doe', '1990-05-15'),
(2, 'jane.smith@example.com', 'jane_smith', 'Jane Smith', '1985-12-30'),
(3, 'alex.jones@example.com', 'alex_jones', 'Alex Jones', '2000-07-21'),
(4, 'mary.johnson@example.com', 'mary_j', 'Mary Johnson', '1995-04-10');

INSERT INTO RATING (id, name) VALUES
(1, 'G'),
(2, 'PG'),
(3, 'PR-13'),
(4, 'R'),
(5, 'X');

INSERT INTO FILMS (id, name, description, releaseDate, duration, rating_id) VALUES
(1, 'The Matrix', 'A hacker discovers reality is an illusion.', '1999-03-31', 136, 4),
(2, 'Inception', 'A thief enters dreams to steal secrets.', '2010-07-16', 148, 3),
(3, 'Interstellar', 'A team of explorers travel through a wormhole in space.', '2014-11-07', 169, 3),
(4, 'The Godfather', 'The aging patriarch of an organized.', '1972-03-24', 175, 4);

INSERT INTO GENRES (id, name) VALUES
(1, 'Science Fiction'),
(2, 'Action'),
(3, 'Drama'),
(4, 'Adventure'),
(5, 'Crime');

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