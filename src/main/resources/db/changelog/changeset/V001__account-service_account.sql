CREATE TABLE owner (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    owner_id bigint NOT NULL,
    owner_type VARCHAR(50) NOT NULL
);

create table balance (
    id bigserial primary key,
     authorization_balance numeric not null default 0,
     current_balance       numeric not null default 0,
     created_at timestamptz default current_timestamp,
     updated_at timestamptz default current_timestamp,
     version bigint
);


CREATE TABLE account (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    number varchar(20) UNIQUE NOT NULL,
    owner_id bigint,
    balance_id bigint,
    type smallint NOT NULL,
    currency varchar(3) NOT NULL,
    status smallint DEFAULT 0,
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp,
    closed_at timestamptz DEFAULT current_timestamp,
    version INT,


    CONSTRAINT Check_MinimumLength CHECK (LENGTH(number) >= 12),
    CONSTRAINT fk_owner_id FOREIGN KEY (owner_id) REFERENCES owner (id) ON DELETE CASCADE
);

ALTER TABLE balance
    ADD COLUMN if not exists account_id bigint references account (id);

ALTER TABLE account ADD CONSTRAINT fk_balance_id FOREIGN KEY (balance_id) REFERENCES balance (id) ON DELETE CASCADE;