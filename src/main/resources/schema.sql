CREATE TABLE IF NOT EXISTS application_user
(
    id       INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name     varchar(50),
    login    varchar(100) not null,
    email    varchar(100) not null,
    birthday date
);

CREATE TABLE IF NOT EXISTS friendship
(
    initiator    integer references application_user (id) ON DELETE CASCADE,
    approver     integer references application_user (id) ON DELETE CASCADE,
    approve_date date,
    CONSTRAINT self_friendship_constraint CHECK (friendship.initiator != friendship.approver)
);



CREATE TABLE IF NOT EXISTS mpa_dictionary
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title       varchar(10) NOT NULL unique,
    description varchar2(1000)
);

CREATE TABLE IF NOT EXISTS genre_dictionary
(
    id    INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title varchar(50) NOT NULL unique
);

CREATE TABLE IF NOT EXISTS film
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title        varchar(40) NOT NULL,
    description  varchar(4000),
    release_date date,
    duration     integer,
    mvp integer references mpa_dictionary (id) on delete NO ACTION
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  INTEGER references film (id) ON DELETE CASCADE,
    genre_id INTEGER references genre_dictionary (id) ON DELETE CASCADE,
    constraint unique_genre unique (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS film_likes
(
    film_id INTEGER references film (id) ON DELETE CASCADE,
    user_id INTEGER references application_user (id) ON DELETE CASCADE,
    constraint unique_like unique (film_id, user_id)
);

