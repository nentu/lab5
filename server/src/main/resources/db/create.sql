DROP TYPE IF EXISTS organization_type CASCADE;

DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users(
        id int generated always as identity PRIMARY KEY,
        login varchar(50) UNIQUE NOT NULL,
        password bytea NOT NULL,
        salt varchar(10) NOT NULL
    );

CREATE TYPE organization_type AS ENUM (
    'COMMERCIAL',
    'PUBLIC',
    'PRIVATE_LIMITED_COMPANY',
    'OPEN_JOINT_STOCK_COMPANY'
    );

DROP TABLE IF EXISTS organization CASCADE;
CREATE TABLE organization
(
    id       int generated always as identity primary key,
    fullName text              not null,
    type     organization_type not null
);



DROP TYPE IF EXISTS worker_coordinates, worker_position CASCADE;
CREATE TYPE worker_coordinates as
(
    x int,
    y float
);
CREATE TYPE worker_position as enum (
    'CLEANER',
    'MANAGER_OF_CLEANING',
    'ENGINEER',
    'LEAD_DEVELOPER',
    'HEAD_OF_DEPARTMENT'
    );



DROP TABLE IF EXISTS worker CASCADE;
CREATE TABLE worker
(
    id             int generated always as identity primary key,
    creationDate   timestamptz default current_timestamp,
    ownerId        int references users (id) not null,

    organizationID int references organization (id),

    name           text               not null,
    salary         float,

    startDate      timestamp          not null,
    endDate        timestamp,

    coordinates    worker_coordinates not null,
    position       worker_position
);


drop function if exists worker_update_restrict;
create or replace function worker_update_restrict() returns trigger as
$$
begin
    raise exception 'updates of creation date and owner id are restricted';
end;
$$ language plpgsql;

--DROP trigger IF EXISTS trigger_worker_update_restrict;
--create or replace trigger trigger_worker_update_restrict
--    before update of id, creationDate, ownerId
--    on worker
--    for row
--execute function worker_update_restrict();

-- insert into organization values
--                              (default, 'test', 'PUBLIC'),
--                              (default, 'test2', 'COMMERCIAL');
--
-- select * from organization

--with i as (insert into organization values (default, 'test', 'PUBLIC') returning id)
--INSERT
--INTO worker
--VALUES (default,
--        default,
--        1,
--        (select id from i),
--        'test',
--        324.3,
--        current_timestamp,
--        current_timestamp,
--        '(234,555.555)',
--        'CLEANER');
--
--select *
--from worker