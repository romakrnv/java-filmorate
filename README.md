# java-filmorate
Filmorate is a platform that integrates film exploration with social interactions. It connects users through a shared interest in movies, enabling them to engage with both the film content and their social network. 

## ER Diagram (3NF):
![Schema Diagram](src/main/resources/db/er-diagram.png)

<details>
<summary><big><b>Описание Таблиц</b></big></summary>
  
[sql schema](src/main/resources/db/schema-diagram.sql)  

### Таблица user
- id (integer): Уникальный идентификатор пользователя. Это первичный ключ таблицы.
- email (varchar): Электронная почта пользователя.
- login (varchar): Логин пользователя.
- name (varchar): Имя пользователя.
- birthday (date): Дата рождения пользователя.

### Таблица friends
- user_id (integer): Идентификатор пользователя, который отправил запрос на дружбу. Это внешний ключ, ссылающийся на user.id.
- friend_id (integer): Идентификатор друга, с которым установлена связь. Это внешний ключ, ссылающийся на user.id.
- status (varchar): Статус запроса на дружбу (например, "ожидает", "принят", "отклонен").

### Таблица film
- id (integer): Уникальный идентификатор фильма. Это первичный ключ таблицы.
- name (varchar): Название фильма.
- description (text): Описание фильма.
- releaseDate (date): Дата выпуска фильма.
- duration (integer): Продолжительность фильма.
- rating (varchar): Возрастной рейтинг фильма (например, "PG", "R", "G").

### Таблица genre
- id (integer): Уникальный идентификатор жанра. Это первичный ключ таблицы.
- name (varchar): Название жанра.

### Таблица film_genre
- film_id (integer): Идентификатор фильма. Это внешний ключ, ссылающийся на film.id.
- genre_id (integer): Идентификатор жанра. Это внешний ключ, ссылающийся на genre.id.

### Таблица film_likes
- film_id (integer): Идентификатор фильма, который был лайкнут. Это внешний ключ, ссылающийся на film.id.
- user_id (integer): Идентификатор пользователя, который поставил лайк. Это внешний ключ, ссылающийся на user.id.

</details>

---

<details>
<summary><big><b>SQL queries</b></big></summary>

    
### Получение всех фильмов
```sql
SELECT * FROM film;
```

### Получение всех пользователей
```sql
SELECT * FROM user;
```

### Топ N наиболее популярных фильмов (по количеству лайков)
```sql
SELECT f.id, f.name, COUNT(fl.user_id) AS like_count
FROM film f
JOIN film_likes fl ON f.id = fl.film_id
GROUP BY f.id, f.name
ORDER BY like_count DESC
LIMIT 10;
```

### Список общих друзей с другим пользователем
```sql
SELECT u.id, u.email, u.login, u.name, u.birthday
FROM user u
JOIN friends f1 ON u.id = f1.friend_id
JOIN friends f2 ON u.id = f2.friend_id
WHERE f1.user_id = 'user_id1' AND f2.user_id = 'user_id2';
```

### Список лайков на фильме
```sql
SELECT u.id, u.email, u.login, u.name, u.birthday
FROM user u
JOIN film_likes fl ON u.id = fl.user_id
WHERE fl.film_id = 'film_id';
```

### Список фильмов, которым пользователь поставил лайк
```sql
SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.rating
FROM film f
JOIN film_likes fl ON f.id = fl.film_id
WHERE fl.user_id = 'user_id';
```
</details>

---
