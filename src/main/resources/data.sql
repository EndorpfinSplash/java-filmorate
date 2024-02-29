MERGE INTO GENRE_DICTIONARY g
    USING (SELECT 'Комедия.' AS tiltle
           FROM dual
           union
           SELECT 'Драма.'
           FROM dual
           union
           SELECT 'Мультфильм.'
           FROM dual
           union
           SELECT 'Триллер.'
           FROM dual
           union
           SELECT 'Боевик.'
           FROM dual) b
ON (g.TITLE = b.tiltle)
WHEN NOT MATCHED THEN
    INSERT (TITLE)
    VALUES (b.tiltle);


MERGE INTO MVP_DICTIONARY mvp
    USING (SELECT 'G' AS tiltle, 'у фильма нет возрастных ограничений' as DESCRIPTION
           FROM dual
           union
           SELECT 'PG', 'детям рекомендуется смотреть фильм с родителями'
           FROM dual
           union
           SELECT 'PG-13', 'детям до 13 лет просмотр не желателен'
           FROM dual
           union
           SELECT 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого'
           FROM dual
           union
           SELECT 'NC-17', 'лицам до 18 лет просмотр запрещён'
           FROM dual) b
ON (mvp.TITLE = b.tiltle)
WHEN NOT MATCHED THEN
    INSERT (TITLE, DESCRIPTION)
    VALUES (b.tiltle, b.DESCRIPTION);

