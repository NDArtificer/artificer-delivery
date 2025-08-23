CREATE TABLE IF NOT EXISTS public.item
(
    id uuid NOT NULL,
    name character varying(255) COLLATE pg_catalog."default",
    quantity integer,
    delivery_id uuid NOT NULL,
    CONSTRAINT item_pkey PRIMARY KEY (id),
    CONSTRAINT fktmocsusce9k48we945lniyemh FOREIGN KEY (delivery_id)
        REFERENCES public.delivery (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.item
    OWNER to postgres;