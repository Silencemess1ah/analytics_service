CREATE TABLE IF NOT EXISTS analytics_event (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    receiver_id bigint NOT NULL,
    actor_id bigint NOT NULL,
    event_type varchar(64) NOT NULL,
    received_at timestamptz DEFAULT current_timestamp
);

CREATE INDEX IF NOT EXISTS events_idx ON analytics_event(receiver_id, event_type, received_at DESC);