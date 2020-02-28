create table busses (
    id integer primary key,
    maker varchar(30),
    series varchar(30),
    seats integer,
    driverOne integer,
    driverTwo integer
);

create table drivers (
    id integer primary key,
    name varchar(30),
    surname varchar(30),
    ucmn varchar(30),
    hireDate date,
    releaseDate date
);