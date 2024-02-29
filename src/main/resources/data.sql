MERGE INTO GENRE_DICTIONARY g
    USING (SELECT '�������.' AS tiltle
           FROM dual
           union
           SELECT '�����.'
           FROM dual
           union
           SELECT '����������.'
           FROM dual
           union
           SELECT '�������.'
           FROM dual
           union
           SELECT '������.'
           FROM dual) b
ON (g.TITLE = b.tiltle)
WHEN NOT MATCHED THEN
    INSERT (TITLE)
    VALUES (b.tiltle);


MERGE INTO MVP_DICTIONARY mvp
    USING (SELECT 'G' AS tiltle, '� ������ ��� ���������� �����������' as DESCRIPTION
           FROM dual
           union
           SELECT 'PG', '����� ������������� �������� ����� � ����������'
           FROM dual
           union
           SELECT 'PG-13', '����� �� 13 ��� �������� �� ���������'
           FROM dual
           union
           SELECT 'R', '����� �� 17 ��� ������������� ����� ����� ������ � ����������� ���������'
           FROM dual
           union
           SELECT 'NC-17', '����� �� 18 ��� �������� ��������'
           FROM dual) b
ON (mvp.TITLE = b.tiltle)
WHEN NOT MATCHED THEN
    INSERT (TITLE, DESCRIPTION)
    VALUES (b.tiltle, b.DESCRIPTION);

