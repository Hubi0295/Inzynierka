CREATE TABLE "users"
(
    id        serial primary key,
    uuid      varchar not null,
    username     varchar not null,
    password     varchar not null,
    email  varchar not null,
    role      varchar not null,
    isEnable    boolean DEFAULT false,
    isLock boolean DEFAULT true
);