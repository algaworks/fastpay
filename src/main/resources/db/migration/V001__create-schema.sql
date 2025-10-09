create table public.credit_card (
    id varchar(255) not null,
    token varchar(255) not null,
    brand varchar(255),
    customer_code varchar(255),
    cvv varchar(255),
    exp_month integer,
    exp_year integer,
    expires_at timestamp with time zone,
    holder_document varchar(255),
    holder_name varchar(255),
    "number" varchar(255),
    assignment_expires_at timestamp with time zone,
    primary key (id)
);

create index idx_credit_card_customer_code on public.credit_card (customer_code);

create table public.payment (
    id varchar(255) not null,
    reference_code varchar(255),
    address_line1 varchar(255),
    address_line2 varchar(255),
    created_at timestamp with time zone,
    "document" varchar(255),
    expired boolean not null,
    expires_at timestamp with time zone,
    failed_at timestamp with time zone,
    full_name varchar(255),
    last_modified_at timestamp with time zone,
    "method" varchar(255),
    notified boolean not null,
    paid_at timestamp with time zone,
    phone varchar(255),
    refund_at timestamp with time zone,
    reply_to_url varchar(255),
    state varchar(255),
    status varchar(255),
    credit_card_id varchar(255),
    total_amount numeric(38, 2),
    "version" bigint,
    zip_code varchar(255),
    primary key (id)
);

create index idx_payment_credit_card_id on public.payment (credit_card_id);
create index idx_payment_reference_code on public.payment (reference_code);

alter table public.payment add constraint fk_payment_credit_card_id foreign key (credit_card_id) references public.credit_card(id);