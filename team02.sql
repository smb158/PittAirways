--Bret Gourdie and Steven Bauer
--bwg8 and smb158

DROP TABLE flight CASCADE constraints;
DROP TABLE plane CASCADE constraints;
DROP TABLE price CASCADE constraints;
DROP TABLE customer CASCADE constraints;
DROP TABLE reservation CASCADE constraints;
DROP TABLE reservation_detail CASCADE constraints;
DROP TABLE our_sys_time CASCADE constraints;

--Create all the tables
CREATE TABLE PLANE (
plane_type char(4) not null,
manufacture varchar2(10),
plane_capacity int,
last_service date,
year int,
constraint pk_plane primary key(plane_type));

CREATE TABLE FLIGHT (
flight_number varchar2(3) not null,
plane_type char(4) not null,
departure_city varchar2(3),
arrival_city varchar2(3),
departure_time varchar2(4),
arrival_time varchar2(4),
weekly_schedule varchar2(7),
constraint pk_flight primary key(flight_number),
constraint fk_plane foreign key(plane_type) references PLANE(plane_type));

CREATE TABLE PRICE (
departure_city varchar2(3) not null,
arrival_city varchar2(3) not null,
high_price int,
low_price int,
constraint pk_price primary key(departure_city,arrival_city));

CREATE TABLE CUSTOMER (
cid varchar2(9) not null,
salutation varchar2(3),
first_name varchar2(30),
last_name varchar2(30),
credit_card_num varchar2(16),
credit_card_expire date,
street varchar2(30),
city varchar2(30),
state varchar2(2),
phone varchar2(10),
email varchar2(30),
constraint pk_customer primary key(cid));

CREATE TABLE RESERVATION (
reservation_number varchar2(5) not null,
cid varchar2(9) not null,
cost int,
reservation_date date,
ticketed varchar2(1),
constraint pk_reservation primary key(reservation_number),
constraint fk_customer foreign key(cid) references CUSTOMER(cid));

CREATE TABLE RESERVATION_DETAIL (
reservation_number varchar2(5) not null,
flight_number varchar2(3) not null,
flight_date date,
leg int not null,
constraint pk_reservation_detail primary key(reservation_number,leg),
constraint fk_reservation_detail foreign key(reservation_number) references RESERVATION(reservation_number),
constraint fk_reservation_detail2 foreign key(flight_number) references FLIGHT(flight_number));

CREATE TABLE OUR_SYS_TIME (
c_date date not null,
constraint pk_our_sys_time primary key(c_date));

commit;


--Bret Gourdie and Steven Bauer
--bwg8 and smb158

--You should create a trigger, called adjustTicket, that adjusts the cost of a reservation when the price of one of its legs changes before the ticket is issued.

CREATE OR REPLACE TRIGGER adjustTicket
	AFTER UPDATE ON price
	FOR EACH ROW
	DECLARE 

	t_flight_num varchar2(3);
	t_res_num varchar2(5);
	t_ticket varchar2(1);

	t_diff_high number;
	t_diff_low number;
	t_flight_type number;

	t_res_date date;
	t_fl_date date;

	-- Select all of the flight numbers affected by comparing the departure city and arrival cities
	CURSOR flightsAffected 
	IS
		SELECT flight_number FROM flight F
		WHERE :new.departure_city = F.departure_city AND :new.arrival_city = F.arrival_city;
	
	BEGIN
		-- Determine difference in high price
		t_diff_high := :new.high_price - :old.high_price;

		-- Determine difference in low price
		t_diff_low := :new.low_price - :old.low_price;

		-- Iterate through flights matching the updated flights departure/arrival cities
		FOR curr_flight IN flightsAffected LOOP

			-- Process reservation numbers of affected flight
			FOR curr_reserv_num IN (SELECT reservation_number INTO t_res_num FROM reservation_detail R
						WHERE curr_flight.flight_number = R.flight_number)
			LOOP
				-- Get the flight date
				SELECT DISTINCT flight_date INTO t_fl_date FROM reservation_detail R
				WHERE R.reservation_number = curr_reserv_num.reservation_number;

				-- Figure out if its a direct or not
				SELECT COUNT(*) INTO t_flight_type FROM reservation_detail R
				WHERE R.reservation_number = curr_reserv_num.reservation_number GROUP BY R.reservation_number;

				-- Has the person already bought their ticket?
				SELECT ticketed, reservation_date INTO t_ticket, t_res_date FROM reservation R
				WHERE curr_reserv_num.reservation_number = R.reservation_number;

				-- If they have not bought their ticket yet, then update their price
				IF t_ticket = 'N' THEN
					-- If direct flight
					IF t_flight_type = 1 THEN
						-- If flight reservation was made same day, update high cost
						IF trunc(t_fl_date, 'DDD') = trunc(t_res_date, 'DDD') THEN
							UPDATE reservation R SET R.cost = t_diff_high + R.cost
							WHERE R.reservation_number = curr_reserv_num.reservation_number;
						ELSE
						-- Not same day so update low cost
							UPDATE reservation R SET R.cost = t_diff_low + R.cost
							WHERE R.reservation_number = curr_reserv_num.reservation_number;
						END IF;

					-- If not direst
					ELSE
						UPDATE reservation R SET R.cost = t_diff_low + R.cost
						WHERE R.reservation_number = curr_reserv_num.reservation_number;
					END IF;
				END IF;
			END LOOP;
		END LOOP;
	END;
	/

--cancelReservation that cancels (deletes) all non-ticketed reservations for a flight, 12 hours prior and if the number of ticketed passengers fits in a smaller capacity plane, then the plane should be switched to the smaller-capacity plane.

CREATE OR REPLACE TRIGGER cancelReservation
 AFTER UPDATE ON OUR_SYS_TIME
FOR EACH ROW
	DECLARE 
		t_reservation_num varchar2(7);
		t_flight_num varchar2(7);
		t_ticket_count number;
		t_current_type varchar2(5);
		t_downgrade varchar2(5);
		CURSOR toCancel (curr_date date)
		IS 
			SELECT B.reservation_number, B.flight_number FROM reservation A, reservation_detail B, flight C 
			WHERE A.reservation_number=B.reservation_number AND C.flight_number=B.flight_number AND A.ticketed='N'
			AND TRUNC(B.flight_date,'DDD')=TRUNC(curr_date,'DDD')
			AND EXTRACT(HOUR FROM to_timestamp(C.departure_time, 'HH24MI'))
			 < EXTRACT(HOUR FROM to_timestamp(curr_date,'MM/DD/YYYY:HH24:MI'))+12;
BEGIN
	
		--open the cursor with the current system date
		OPEN toCancel(:new.c_date);
		FETCH toCancel INTO t_reservation_num, t_flight_num;
		
		--Loop through all the toCancel tuples
		WHILE (toCancel%FOUND) LOOP
		--Delete from reservation and reservation_detail where reservation number matches
		DELETE FROM reservation_detail WHERE reservation_detail.reservation_number=t_reservation_num;
		DELETE FROM reservation WHERE reservation.reservation_number=t_reservation_num;
		
		--Get number of tickets remaining for flight
		SELECT COUNT(*) INTO t_ticket_count FROM reservation_detail A WHERE t_flight_num = A.flight_number;
		
		--Get the current type of plane
		SELECT plane_type INTO t_current_type FROM flight WHERE flight_number=t_flight_num;
		
		--Find the smallest plane type that can accomodate the number of passengers
		SELECT plane_type INTO t_downgrade FROM (SELECT * FROM plane WHERE to_number(plane_capacity)>t_ticket_count ORDER BY plane_capacity ASC)
			WHERE rownum=1;
		
		--Check if the plane type found is different from the current type, if so downgrade!
		IF t_current_type != t_downgrade THEN
			UPDATE flight SET plane_type = t_downgrade WHERE flight_number=t_flight_num;
		END IF;
		
		FETCH toCancel INTO t_reservation_num, t_flight_num;
		END LOOP;
		
		CLOSE toCancel;
END;
/

--You should create a trigger called planeUpgrade, that changes the plane (type) of a flight to an immediately higher capacity plane (type), if it exists, when a new reservation is made on that flight and there are no available seats (i.e., the flight is fully booked). A change of plane will fail only if the currently assigned plane for the flight is the one with the biggest capacity.

--CREATE OR REPLACE PROCEDURE updatePlaneFromTrigger (new_flight_num in varchar2,new_type in char)
--AS
--BEGIN
--	UPDATE flight F SET F.plane_type = new_type
--	WHERE F.flight_number = new_flight_num;
--END;
--/

--CREATE OR REPLACE PROCEDURE alreadyLargestPlane (new_reservation_number in varchar2)
--AS
--BEGIN
--	DELETE FROM reservation_detail WHERE reservation_detail.reservation_number=new_reservation_number;
--	DELETE FROM reservation WHERE reservation.reservation_number=new_reservation_number;
--END;
--/

CREATE OR REPLACE TRIGGER planeUpgrade
	BEFORE INSERT ON reservation_detail
	FOR EACH ROW
	DECLARE 
	total_seats number;
	remaining_seats number;
	claimed_seats number;
	current_type varchar2(10);
	new_type varchar2(10);

	BEGIN
		--Get the total seats the plane can accomodate and the plane type
		SELECT P.plane_capacity, P.plane_type INTO total_seats, current_type FROM plane P, flight F
		WHERE :new.flight_number = F.flight_number AND P.plane_type = F.plane_type;

		--Count the number of seats that have already been claimed from reservation_detail
		SELECT (COUNT(*)) INTO claimed_seats FROM reservation_detail R
		WHERE R.flight_number=:new.flight_number AND R.flight_date=:new.flight_date;
		
		--Calculate the remaining seats
		remaining_seats := total_seats - claimed_seats;

		--Check if there are negative seats left, if not then we need to upgrade the plane
		IF remaining_seats <= 0 THEN
			--Find the smallest plane type that can accomodate the number of passengers
			SELECT plane_type INTO new_type FROM 
				(SELECT plane_type FROM plane WHERE total_seats<plane_capacity ORDER BY plane_capacity ASC)
			WHERE ROWNUM=1;
			--Update to the newly found type
			IF new_type <> current_type THEN
				UPDATE flight F SET F.plane_type = new_type
				WHERE F.flight_number = :new.flight_number;
			END IF;
		END IF;
	END;
	/

commit;