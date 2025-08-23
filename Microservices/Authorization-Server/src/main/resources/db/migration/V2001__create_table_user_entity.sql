CREATE TABLE IF NOT EXISTS public.user_entity
(
    id bigint NOT NULL,
    created_at timestamp(6) with time zone,
    email character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    type character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT user_entity_pkey PRIMARY KEY (id),
    CONSTRAINT uk4xad1enskw4j1t2866f7sodrx UNIQUE (email),
    CONSTRAINT user_entity_type_check CHECK (type::text = ANY (ARRAY['ADMIN'::character varying, 'CLIENT'::character varying]::text[]))
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.user_entity
    OWNER to postgres;