package ru.yandex.practicum.filmorate.storage.queries;

public class UserSqlQueries {
    public static final String FIND_ALL_USERS_QUERY = """
            SELECT
            	ID,
            	EMAIL,
            	LOGIN,
            	NAME,
            	BIRTHDAY,
            	LISTAGG(f.USER2_ID) AS FRIENDS
            FROM
            	USERS u
            LEFT JOIN FRIENDS f ON
            	u.ID = f.USER1_ID
            GROUP BY
            	ID,
            	EMAIL,
            	LOGIN,
            	NAME,
            	BIRTHDAY;
                """;
    public static final String FIND_USER_BY_ID_QUERY = """
            SELECT
            	ID,
            	EMAIL,
            	LOGIN,
            	NAME,
            	BIRTHDAY,
            	LISTAGG(f.USER2_ID) AS FRIENDS
            FROM
            	USERS u
            LEFT JOIN FRIENDS f ON
            	u.ID = f.USER1_ID
            WHERE u.ID = ?
            GROUP BY
            	ID,
            	EMAIL,
            	LOGIN,
            	NAME,
            	BIRTHDAY;
                """;
    public static final String ADD_USER_QUERY = """
            INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY)
            VALUES (?,?,?,?);
            """;
    public static final String UPDATE_USER_QUERY = """
            UPDATE
            	USERS
            SET
            	EMAIL = ?,
            	LOGIN = ?,
            	NAME = ?,
            	BIRTHDAY = ?
            WHERE
            	ID = ?
            """;
    public static final String ADD_FRIEND_QUERY = """
            INSERT INTO FRIENDS (USER1_ID, USER2_ID)
            VALUES (?, ?);
            """;
    public static final String REMOVE_FRIEND_QUERY = """
            DELETE FROM FRIENDS WHERE USER1_ID = ? AND USER2_ID = ?;
            """;
}