use BankManagementSystem;

DROP TABLE IF EXISTS statement;

CREATE TABLE statement (
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 10 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    account_number bigint NOT NULL,
    operation_date timestamp without time zone NOT NULL,
    beneficiary_number bigint NOT NULL,
    comment character varying(255) COLLATE pg_catalog."default",
    amount bigint NOT NULL,
    currency character varying(15) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT statement_pkey PRIMARY KEY (id)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;