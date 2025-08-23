CREATE TABLE IF NOT EXISTS public.assigned_delivery
(
    id uuid NOT NULL,
    assigned_at timestamp(6) with time zone,
    courier_id uuid NOT NULL,
    CONSTRAINT assigned_delivery_pkey PRIMARY KEY (id),
    CONSTRAINT fkh2bsoltqf0eiispuhiq3p6o0x FOREIGN KEY (courier_id)
        REFERENCES public.courier (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.assigned_delivery
    OWNER to postgres;
