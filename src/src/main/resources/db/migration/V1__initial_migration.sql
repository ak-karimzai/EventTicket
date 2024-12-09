CREATE TABLE "users" (
                         "id" bigint PRIMARY KEY,
                         "name" text NOT NULL,
                         "email" text NOT NULL,
                         "phone_number" text,
                         "username" text NOT NULL,
                         "password" text NOT NULL,
                         "role" text NOT NULL,
                         "joined_at" timestamp NOT NULL
);

CREATE TABLE "categories" (
                              "id" bigint PRIMARY KEY,
                              "title" text NOT NULL,
                              "created_by" bigint NOT NULL,
                              "created_date" timestamp NOT NULL DEFAULT (now()),
                              "last_modified_by" bigint,
                              "last_modified_date" timestamp
);

CREATE TABLE "events" (
                          "id" bigint PRIMARY KEY,
                          "title" text NOT NULL,
                          "artist" text,
                          "date" timestamp NOT NULL,
                          "description" text,
                          "category_id" bigint NOT NULL,
                          "created_by" bigint NOT NULL,
                          "created_date" timestamp NOT NULL DEFAULT (now()),
                          "last_modified_by" bigint,
                          "last_modified_date" timestamp
);

CREATE TABLE "tickets" (
                           "id" bigint PRIMARY KEY,
                           "title" text NOT NULL,
                           "description" text,
                           "price" float8 NOT NULL,
                           "event_id" bigint NOT NULL,
                           "created_by" bigint NOT NULL,
                           "created_date" timestamp NOT NULL DEFAULT (now()),
                           "last_modified_by" bigint,
                           "last_modified_date" timestamp
);

CREATE TABLE "orders" (
                          "id" bigint PRIMARY KEY,
                          "user_id" bigint NOT NULL,
                          "order_placed" timestamp NOT NULL,
                          "order_paid" bool NOT NULL
);

CREATE TABLE "order_items" (
                               "id" bigint PRIMARY KEY,
                               "order_id" bigint NOT NULL,
                               "ticket_id" bigint NOT NULL,
                               "amount" int NOT NULL
);

ALTER TABLE "categories"
    ADD FOREIGN KEY ("created_by") REFERENCES "users" ("id") ON DELETE SET NULL,
    ADD FOREIGN KEY ("last_modified_by") REFERENCES "users" ("id") ON DELETE SET NULL;

ALTER TABLE "events"
    ADD FOREIGN KEY ("category_id") REFERENCES "categories" ("id") ON DELETE CASCADE,
    ADD FOREIGN KEY ("created_by") REFERENCES "users" ("id") ON DELETE SET NULL,
    ADD FOREIGN KEY ("last_modified_by") REFERENCES "users" ("id") ON DELETE SET NULL;

ALTER TABLE "tickets"
    ADD FOREIGN KEY ("event_id") REFERENCES "events" ("id") ON DELETE CASCADE,
    ADD FOREIGN KEY ("created_by") REFERENCES "users" ("id") ON DELETE SET NULL,
    ADD FOREIGN KEY ("last_modified_by") REFERENCES "users" ("id") ON DELETE SET NULL;

ALTER TABLE "orders"
    ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE;

ALTER TABLE "order_items"
    ADD FOREIGN KEY ("order_id") REFERENCES "orders" ("id") ON DELETE CASCADE,
    ADD FOREIGN KEY ("ticket_id") REFERENCES "tickets" ("id") ON DELETE CASCADE;
