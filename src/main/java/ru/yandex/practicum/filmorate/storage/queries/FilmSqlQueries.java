package ru.yandex.practicum.filmorate.storage.queries;

public class FilmSqlQueries {
    public static final String FIND_FILM_BY_ID_QUERY = """
                    SELECT
                    	F.ID,
                    	F.NAME,
                    	F.DESCRIPTION,
                    	F.RELEASEDATE,
                    	F.DURATION,
                    	F.RATING_ID,
                    	R.NAME AS RATING_NAME,
                    	LISTAGG(DISTINCT CONCAT(GENRE_ID, '/', G.NAME)) FILTER (
                    	WHERE GENRE_ID IS NOT NULL) AS GENRES,
                    	LISTAGG(DISTINCT FL.USER_ID) AS LIKES
                    FROM
                    	FILMS F
                    LEFT JOIN RATING R ON
                    	F.RATING_ID = R.ID
                    LEFT JOIN FILM_GENRE FG ON
                    	F.ID = FG.FILM_ID
                    LEFT JOIN GENRES G ON
                    	FG.GENRE_ID = G.ID
                    LEFT JOIN FILM_LIKES FL ON
                    	F.ID = FL.FILM_ID
                    GROUP BY
                    	F.ID,
                    	F.NAME,
                    	F.DESCRIPTION,
                    	F.RELEASEDATE,
                    	F.DURATION,
                    	F.RATING_ID,
                    	R.NAME
                    HAVING
                    	F.ID = ?;
            """;
    public static final String FIND_ALL_FILMS_QUERY = """
                    SELECT
                    	F.ID,
                    	F.NAME,
                    	F.DESCRIPTION,
                    	F.RELEASEDATE,
                    	F.DURATION,
                    	F.RATING_ID,
                    	R.NAME AS RATING_NAME,
                    	LISTAGG(DISTINCT CONCAT(GENRE_ID, '/', G.NAME)) FILTER (
                    	WHERE GENRE_ID IS NOT NULL) AS GENRES,
                    	LISTAGG(DISTINCT FL.USER_ID) AS LIKES
                    FROM
                    	FILMS F
                    LEFT JOIN RATING R ON
                    	F.RATING_ID = R.ID
                    LEFT JOIN FILM_GENRE FG ON
                    	F.ID = FG.FILM_ID
                    LEFT JOIN GENRES G ON
                    	FG.GENRE_ID = G.ID
                    LEFT JOIN FILM_LIKES FL ON
                    	F.ID = FL.FILM_ID
                    GROUP BY
                    	F.ID,
                    	F.NAME,
                    	F.DESCRIPTION,
                    	F.RELEASEDATE,
                    	F.DURATION,
                    	F.RATING_ID,
                    	R.NAME;
            """;
    public static final String INSERT_FILM_QUERY = """
            INSERT INTO FILMS(NAME, DESCRIPTION, RELEASEDATE, DURATION, RATING_ID)
            VALUES(?, ?, ?, ?, ?);
            """;
    public static final String INSERT_GENRE_QUERY = """
            INSERT INTO FILM_GENRE(FILM_ID, GENRE_ID)
             VALUES (?,?);
             """;
    public static final String FIND_ALL_GENRES_QUERY = """
            SELECT ID, NAME FROM GENRES;
            """;
    public static final String FIND_GENRE_BY_ID_QUERY = """
            SELECT ID, NAME FROM GENRES WHERE ID = ?;
             """;
    public static final String FIND_ALL_MPA_QUERY = """
            SELECT ID, NAME FROM RATING
             """;
    public static final String FIND_MPA_BY_ID_QUERY = """
            SELECT ID, NAME FROM RATING WHERE ID = ?;
             """;
    public static final String ADD_LIKE_QUERY = """
            INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?);
             """;
    public static final String REMOVE_LIKE_QUERY = """
            DELETE FROM FILM_LIKES WHERE FILM_ID=? AND USER_ID=?;
             """;
    public static final String UPDATE_FILM_QUERY = """
            UPDATE FILMS SET NAME = ?,
                 DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, RATING_ID = ?
                 WHERE ID = ?;
             """;
}
