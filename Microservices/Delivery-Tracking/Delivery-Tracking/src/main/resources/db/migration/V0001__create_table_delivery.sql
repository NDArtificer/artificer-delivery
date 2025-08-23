CREATE TABLE IF NOT EXISTS public.delivery
(
    id uuid NOT NULL,
    assigned_at timestamp(6) with time zone,
    courier_id uuid,
    currier_payout numeric(38,2),
    distance_fee numeric(38,2),
    expected_delivery_at timestamp(6) with time zone,
    fulfilled_at timestamp(6) with time zone,
    placed_at timestamp(6) with time zone,
    recipient_complement character varying(255) COLLATE pg_catalog."default",
    recipient_name character varying(255) COLLATE pg_catalog."default",
    recipient_number character varying(255) COLLATE pg_catalog."default",
    recipient_phone_number character varying(255) COLLATE pg_catalog."default",
    recipient_street character varying(255) COLLATE pg_catalog."default",
    recipient_zip_code character varying(255) COLLATE pg_catalog."default",
    sender_complement character varying(255) COLLATE pg_catalog."default",
    sender_name character varying(255) COLLATE pg_catalog."default",
    sender_number character varying(255) COLLATE pg_catalog."default",
    sender_phone_number character varying(255) COLLATE pg_catalog."default",
    sender_street character varying(255) COLLATE pg_catalog."default",
    sender_zip_code character varying(255) COLLATE pg_catalog."default",
    status smallint,
    total_cost numeric(38,2),
    total_items integer,
    CONSTRAINT delivery_pkey PRIMARY KEY (id),
    CONSTRAINT delivery_status_check CHECK (status >= 0 AND status <= 3)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.delivery
    OWNER to postgres;