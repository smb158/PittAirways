prompt TEST trigger planeUpgrade

select * from flight where flight_number='352';

prompt TEST trigger cancelReservation

insert into our_sys_time values(to_date('12/12/2013 12:30', 'MM/DD/YYYY HH12:MI'));
commit;

select * from reservation r, reservation_detail rd where r.reservation_number =rd.reservation_number and 
rd.flight_number='352';
