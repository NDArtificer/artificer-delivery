CREATE TABLE IF NOT EXISTS public.courier
(
    id uuid NOT NULL,
    fullfilled_deliveries_quantity integer,
    last_fulfilled_delivery_at timestamp(6) with time zone,
    name character varying(255) COLLATE pg_catalog."default",
    pending_deliveries_quantity integer,
    phone character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT courier_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.courier
    OWNER to postgres;