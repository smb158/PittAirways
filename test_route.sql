prompt DISPLAY flights from A-->X->B
select * from flight f1, flight f2 where f1.departure_city='&1' and f2.arrival_city='&2' and f1.arrival_city=f2.departure_city;

prompt DISPLAY direct flights from A --> B
select * from flight f where f.departure_city='&1' and f.arrival_city='&2';
