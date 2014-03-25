prompt INSERT reservations
insert into reservation values('100','1',0,to_date('10/12/13', 'MM/DD/YYY'),'Y');
insert into reservation values('200','10',0,to_date('9/12/13', 'MM/DD/YYY'),'Y');
insert into reservation values('300','21',0,to_date('10/1/13', 'MM/DD/YYY'),'Y');
insert into reservation values('301','100',0,to_date('10/22/13', 'MM/DD/YYY'),'Y');
insert into reservation values('302','11',0,to_date('11/2/13', 'MM/DD/YYY'),'Y');

commit;

prompt INSERT reservations details
insert into reservation_detail values('100','352',to_date('11/12/13','MM/DD/YYYY'),1);
insert into reservation_detail values('200','352',to_date('11/12/13','MM/DD/YYYY'),2);
insert into reservation_detail values('200','039',to_date('11/12/13','MM/DD/YYYY'),1);
insert into reservation_detail values('200','334',to_date('1/12/14','MM/DD/YYYY'),3);
insert into reservation_detail values('200','402',to_date('1/12/13','MM/DD/YYYY'),4);
insert into reservation_detail values('300','352',to_date('11/12/13','MM/DD/YYYY'),1);
insert into reservation_detail values('301','352',to_date('11/12/13','MM/DD/YYYY'),1);
insert into reservation_detail values('302','039',to_date('11/12/13','MM/DD/YYYY'),1);
insert into reservation_detail values('302','352',to_date('11/12/13','MM/DD/YYYY'),2);

commit;

