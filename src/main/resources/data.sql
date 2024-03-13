MERGE INTO GENRE_DICTIONARY g
    USING (SELECT 1 as id, 'Комедия' AS tiltle
           FROM dual
           union
           SELECT 2, 'Драма'
           FROM dual
           union
           SELECT 3, 'Мультфильм'
           FROM dual
           union
           SELECT 4, 'Триллер'
           FROM dual
           union
           SELECT 5, 'Документальный'
           FROM dual
           union
           SELECT 6, 'Боевик'
           FROM dual) b
ON (g.TITLE = b.tiltle)
WHEN NOT MATCHED THEN
    INSERT (TITLE)
    VALUES (b.tiltle);


MERGE INTO MPA_DICTIONARY mpa
    USING (SELECT 1 as id, 'G' AS tiltle, 'у фильма нет возрастных ограничений' as DESCRIPTION
           FROM dual
           union
           SELECT 2, 'PG', 'детям рекомендуется смотреть фильм с родителями'
           FROM dual
           union
           SELECT 3, 'PG-13', 'детям до 13 лет просмотр не желателен'
           FROM dual
           union
           SELECT 4, 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого'
           FROM dual
           union
           SELECT 5, 'NC-17', 'лицам до 18 лет просмотр запрещён'
           FROM dual) b
ON (mpa.TITLE = b.tiltle)
WHEN NOT MATCHED THEN
    INSERT (TITLE, DESCRIPTION)
    VALUES (b.tiltle, b.DESCRIPTION);

