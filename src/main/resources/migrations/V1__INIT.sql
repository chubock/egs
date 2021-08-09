create table categories
(
	id bigserial not null
		constraint categories_pkey
			primary key,
	uid varchar(255) not null
		constraint uk_categories_uid
			unique,
	version integer,
	name varchar(255) not null
		constraint uk_categories_name
			unique,
	create_date timestamp not null,
	last_update_date timestamp not null
);

create table products
(
	id bigserial not null
		constraint products_pkey
			primary key,
	uid varchar(255) not null
		constraint uk_products_uid
			unique,
	version integer,
	name varchar(255) not null,
	price integer,
	rate double precision,
	category_id bigint
		constraint fk_products_category_id
			references categories,
	create_date timestamp not null,
	last_update_date timestamp not null
);

create table users
(
	id bigserial not null
		constraint users_pkey
			primary key,
	uid varchar(255) not null
		constraint uk_users_uid
			unique,
	version integer,
	account_expired boolean not null,
	account_locked boolean not null,
	credential_expired boolean not null,
	enabled boolean not null,
	first_name varchar(255),
	last_name varchar(255),
	password varchar(255),
	role integer not null,
	username varchar(255),
	create_date timestamp not null,
	last_update_date timestamp not null
);

create table comments
(
	id bigserial not null
		constraint comments_pkey
			primary key,
	uid varchar(255) not null
		constraint uk_comments_uid
			unique,
	version integer,
	text text,
	product_id bigint not null
		constraint fk_comments_product_id
			references products,
	user_id bigint not null
		constraint fk_comments_user_id
			references users,
	create_date timestamp not null,
	last_update_date timestamp not null
);

create table rates
(
	id bigserial not null
		constraint rates_pkey
			primary key,
	uid varchar(255) not null
		constraint uk_rates_uid
			unique,
	version integer,
	rate integer not null
		constraint rates_rate_check
			check ((rate <= 5) AND (rate >= 1)),
	product_id bigint not null
		constraint fk_rates_product_id
			references products,
	user_id bigint not null
		constraint fk_rates_user_id
			references users,
	create_date timestamp not null,
	last_update_date timestamp not null
);
