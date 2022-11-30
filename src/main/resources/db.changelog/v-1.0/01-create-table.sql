create table if not exists notification
(
    notification_id   integer generated always as identity primary key,
    chat_id           integer not null,
    notification_text text    not null,
    notification_data timestamp    not null
);


