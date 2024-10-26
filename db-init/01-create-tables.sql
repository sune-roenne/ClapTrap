create table predicate_request (
    request_id uuid default gen_random_uuid() not null,
    predicate_id varchar(500) not null,
    request_time timestamp default now()::timestamp not null,
    private_key varchar(4000),
    public_key varchar
);

create unique index idx_predicate_request_predicate_id on predicate_request(predicate_id);

create table extent_request (
    request_id uuid default gen_random_uuid() not null,
    extent_id varchar(500) not null,
    request_time timestamp default now()::timestamp not null,
    symmetric_key varchar(4000)
);

create unique index idx_extent_request_extent_id on extent_request(extent_id);
