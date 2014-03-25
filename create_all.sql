CREATE TABLE PLANE(
	plane_type char(4),
	manufacture varchar(10),
	plane_capacity int,
	last_service date,
	year int,
	CONSTRAINT plane_type_pk PRIMARY KEY(plane_type)
	);

COMMIT;

--------------------------------------------------------- F L I G H T  P R I C I N G  I N F O R M A T I O N
CREATE TABLE PRICE(
	departure_city varchar(3),
	arrival_city varchar(3),
	high_price int,
	low_price int,
	CONSTRAINT flight_path_pk PRIMARY KEY(departure_city, arrival_city)
	);
	
COMMIT;
---------------------------------------------------------- F L I G H T  S C H E D U L E  I N F O R M A T I O N
CREATE TABLE FLIGHT(
	flight_number varchar(3),
	plane_type char(4),
	departure_city varchar(3),
	arrival_city varchar(3),
	departure_time varchar(4),
	arrival_time varchar(4),
	weekly_schedule varchar(7),
	CONSTRAINT flight_num_pk PRIMARY KEY(flight_number),
	CONSTRAINT plane_type_fk FOREIGN KEY(plane_type) REFERENCES PLANE(plane_type));
    
COMMIT;
---------------------------------------------------------- C U S T O M E R  I N F O R M A T I O N
CREATE TABLE CUSTOMER(
	cid varchar(9),
	salutation varchar(3),
	first_name varchar(30),
	last_name varchar(30),
	credit_card_num varchar(16),
	credit_card_expire date,
	street varchar(30),
	city varchar(30),
	state varchar(2),
	phone varchar(10),
	email varchar(30),
	CONSTRAINT customer_id_pk PRIMARY KEY(cid),
	CONSTRAINT customer_salutation_value CHECK (VALUE(salutation) IN('Mr', 'Mrs', 'Ms')));

COMMIT;
---------------------------------------------------------- R E S E R V A T I O N  I N F O R M A T I O N
CREATE TABLE RESERVATION(
	reservation_number varchar(5),
	cid varchar(9),
	cost int,
	reservation_date date,
	ticketed varchar(1),
	CONSTRAINT res_number_pk PRIMARY KEY(reservation_number),
	CONSTRAINT cid_fk FOREIGN KEY (cid) REFERENCES CUSTOMER(cid),
	CONSTRAINT ticketed_check CHECK (VALUE(ticketed) IN ('Y', 'N')));

COMMIT;


CREATE TABLE RESERVATION_DETAIL(
	reservation_number varchar(5),
	flight_number varchar(3),
	flight_date date,
	leg int,
	CONSTRAINT res_leg_pk PRIMARY KEY(reservation_number, leg),
	CONSTRAINT reservation_fk FOREIGN KEY(reservation_number) REFERENCES RESERVATION(reservation_number),
	CONSTRAINT flight_num_fk FOREIGN KEY(flight_number) REFERENCES FLIGHT(flight_number));
    
COMMIT;

---------------------------------------------------------- O U R  T I M E  I N F O
CREATE TABLE OUR_SYS_TIME(
    c_date date,
    CONSTRAINT date_PK PRIMARY KEY(c_date));

COMMIT;

