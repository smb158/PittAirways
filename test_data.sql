--Sample data file for PittAirways
--Steve Bauer
--12/8/2013

--Sample Plane Tuples, first 4 from project.pdf the other 11 are new
INSERT INTO plane VALUES ('B737', 'Boeing', 125, to_date('09/09/2009','MM/DD/YYYY'), 1996);
INSERT INTO plane VALUES ('A320', 'Airbus', 155, to_date('10/01/2011','MM/DD/YYYY'), 2001);
INSERT INTO plane VALUES ('E145', 'Embraer', 50, to_date('06/15/2010','MM/DD/YYYY'), 2008);
INSERT INTO plane VALUES ('B777', 'Boeing', 250, to_date('01/31/2013','MM/DD/YYYY'), 2012);
--Insert some Boeing flights
INSERT INTO plane VALUES ('B747', 'Boeing', 125, to_date('09/09/2009','MM/DD/YYYY'), 1996);
INSERT INTO plane VALUES ('B757', 'Boeing', 225, to_date('09/09/2009','MM/DD/YYYY'), 1996);
INSERT INTO plane VALUES ('B750', 'Boeing', 125, to_date('09/09/2009','MM/DD/YYYY'), 1999);
INSERT INTO plane VALUES ('B751', 'Boeing', 127, to_date('10/10/2001','MM/DD/YYYY'), 1989);
--Insert some Airbus flights
INSERT INTO plane VALUES ('A950', 'Airbus', 555, to_date('10/01/2011','MM/DD/YYYY'), 2007);
INSERT INTO plane VALUES ('A385', 'Airbus', 55, to_date('12/01/2011','MM/DD/YYYY'), 2004);
INSERT INTO plane VALUES ('A381', 'Airbus', 315, to_date('01/01/2011','MM/DD/YYYY'), 2004);
--Insert some Embraer flights
INSERT INTO plane VALUES ('E675', 'Embraer', 150, to_date('06/15/2010','MM/DD/YYYY'), 2007);
INSERT INTO plane VALUES ('E901', 'Embraer', 35, to_date('03/15/2011','MM/DD/YYYY'), 2001);
INSERT INTO plane VALUES ('E900', 'Embraer', 75, to_date('06/15/2010','MM/DD/YYYY'), 2010);


--Insert flight schedule information, first 3 are from project.pdf all other are new
INSERT INTO flight VALUES ('153', 'A320', 'PIT', 'JFK', '1000', '1120', 'SMTWTFS');
INSERT INTO flight VALUES ('154', 'B737', 'JFK', 'DCA', '1230', '1320', 'S-TW-FS');
INSERT INTO flight VALUES ('552', 'E145', 'PIT', 'DCA', '1100', '1150', 'SM-WT-S');
--Insert new flight schedule information
INSERT INTO flight VALUES ('155', 'B777', 'JFK', 'PIT', '1100', '1250', 'SMTWTFS');
INSERT INTO flight VALUES ('156', 'B747', 'DCA', 'JFK', '1000', '1310', '-MTWTF-');
INSERT INTO flight VALUES ('157', 'B757', 'DCA', 'PIT', '1300', '1500', '-M-W-FS');
INSERT INTO flight VALUES ('158', 'B750', 'ABR', 'PIT', '0030', '0100', 'SMTWTFS');
INSERT INTO flight VALUES ('159', 'B751', 'PIT', 'ABR', '1000', '1120', '-MTWTFS');
INSERT INTO flight VALUES ('160', 'A950', 'ATL', 'PIT', '1115', '1320', 'S----F-');
INSERT INTO flight VALUES ('170', 'A385', 'PIT', 'ATL', '1000', '1120', '-MT-TF-');
INSERT INTO flight VALUES ('171', 'A381', 'ABR', 'JFK', '1000', '1125', 'SMT-TFS');
INSERT INTO flight VALUES ('172', 'E145', 'JFK', 'ABR', '1120', '1520', 'S-T-TFS');
INSERT INTO flight VALUES ('173', 'E675', 'ATL', 'JFK', '1600', '2020', 'SM--TFS');
INSERT INTO flight VALUES ('174', 'E901', 'JFK', 'ATL', '1400', '1720', '-MTWTFS');
INSERT INTO flight VALUES ('175', 'E900', 'DCA', 'ATL', '1500', '1750', 'SMTW--S');


--Insert some tuples into the price table
INSERT INTO price VALUES ('PIT', 'JFK', 250, 120);
INSERT INTO price VALUES ('JFK', 'PIT', 250, 120);
INSERT INTO price VALUES ('JFK', 'DCA', 220, 100);
INSERT INTO price VALUES ('DCA', 'JFK', 210, 90);
INSERT INTO price VALUES ('PIT', 'DCA', 200, 150);
INSERT INTO price VALUES ('DCA', 'PIT', 200, 150);
--Insert new tuples into price table
INSERT INTO price VALUES ('ABR', 'PIT', 200, 100);
INSERT INTO price VALUES ('PIT', 'ABR', 200, 100);
INSERT INTO price VALUES ('ATL', 'PIT', 325, 200);
INSERT INTO price VALUES ('PIT', 'ATL', 325, 200);
INSERT INTO price VALUES ('ABR', 'JFK', 200, 100);
INSERT INTO price VALUES ('JFK', 'ABR', 200, 100);
INSERT INTO price VALUES ('ATL', 'JFK', 325, 210);
INSERT INTO price VALUES ('JFK', 'ATL', 325, 210);
INSERT INTO price VALUES ('ABR', 'DCA', 275, 170);
INSERT INTO price VALUES ('DCA', 'ABR', 275, 170);
INSERT INTO price VALUES ('ATL', 'DCA', 350, 280);
INSERT INTO price VALUES ('DCA', 'ATL', 350, 280);



--Insert some customers into the database
--We not only developed the database for PittAirways but are their first two customers!
INSERT INTO customer VALUES ('1', 'Mr', 'Steven', 'Bauer', '1234567890000000', to_date('07/12/2016','MM/DD/YYYY'), ' 1234 Fake St.', 'Pittsburgh', 'PA','1231234567','smb158@pitt.edu');
INSERT INTO customer VALUES ('2', 'Mr', 'Bret', 'Gourdie', '0000000123456789', to_date('06/01/2016','MM/DD/YYYY'), ' 1234 Fake Dr.', 'Pittsburgh', 'PA','4128675309','bwg8@pitt.edu');
--Insert 13 more customers into the database
INSERT INTO customer VALUES ('3', 'Mr', 'Billy', 'Thorton', '1000000000000000', to_date('01/01/2015','MM/DD/YYYY'), ' 1234 Average St.', 'Pittsburgh', 'PA','4121111111','billy_thorton@gmail.com');
INSERT INTO customer VALUES ('4', 'Mr', 'Joe', 'Schmoe', '1200000000000000', to_date('01/02/2014','MM/DD/YYYY'), ' 1234 Schmoe St.', 'Pittsburgh', 'PA','4122222222','joe_schmoe@gmail.com');
INSERT INTO customer VALUES ('5', 'Ms', 'Casey', 'Conaboy', '1230000000000000', to_date('01/07/2018','MM/DD/YYYY'), ' 1234 Conaboy St.', 'Pittsburgh', 'PA','4123333333','casey_conaboy@aim.com');
INSERT INTO customer VALUES ('6', 'Mrs', 'Kayla', 'Cranberry', '1234000000000000', to_date('08/19/2017','MM/DD/YYYY'), ' 4321 Anywhere Dr.', 'Erie', 'PA','4124444444','Cranberries4life@aol.com');
INSERT INTO customer VALUES ('7', 'Mrs', 'Anna', 'Cranberry', '1234500000000000', to_date('08/11/2017','MM/DD/YYYY'), ' 9999 Dawson St.', 'Erie', 'PA','4128465783','anna_cranberry@hotmail.com');
INSERT INTO customer VALUES ('8', 'Mr', 'Jon', 'Bonjovi', '1425364758697746', to_date('08/19/2017','MM/DD/YYYY'), ' 4164 Venice St.', 'Cleveland', 'OH','8384657243','iloveemail@email.com');
INSERT INTO customer VALUES ('9', 'Mr', 'Bon', 'jonjovi', '1425364757697746', to_date('02/19/2017','MM/DD/YYYY'), ' 4165 Venice St.', 'Cleveland', 'OH','8384656243','iloveemail2@email.com');
INSERT INTO customer VALUES ('10', 'Mr', 'Barack', 'Obama', '1925364757697756', to_date('11/01/2019','MM/DD/YYYY'), ' 1 whitehouse dr.', 'Washington', 'DC','8384659243','yoitsobeezy@whitehouse.gov');
INSERT INTO customer VALUES ('11', 'Mrs', 'Machelle', 'Obama', '1925364757690756', to_date('11/02/2019','MM/DD/YYYY'), ' 1 whitehouse dr.', 'Washington', 'DC','8384659242','mrspresident@whitehouse.gov');
INSERT INTO customer VALUES ('12', 'Ms', 'Lorri', 'Gorditta', '9999999999999999', to_date('09/09/2014','MM/DD/YYYY'), ' 7 taco st.', 'Orlando', 'FL','8084659242','Lorri_Rox@hotmail.com');
INSERT INTO customer VALUES ('13', 'Ms', 'Jodi', 'Julian', '8888888888888888', to_date('09/03/2017','MM/DD/YYYY'), ' 745 FancyPants st.', 'Denver', 'CO','8084659240','denveristhebest@hotmail.com');
INSERT INTO customer VALUES ('14', 'Mr', 'Chris', 'Roberts', '1212121212121212', to_date('05/03/2020','MM/DD/YYYY'), ' 9451 Bates st.', 'Austin', 'TX','8184659240','christherockstar@hotmail.com');
INSERT INTO customer VALUES ('15', 'Mrs', 'Lucy', 'Laserbeam', '9876567898765432', to_date('01/14/2017','MM/DD/YYYY'), ' 2345 Outerspace Dr.', 'New York', 'NY','8084677240','lucy_laserbeam@hotmail.com');


--Insert into reservation THESE ALL NEED CHANGED TO MATCH STUFF ABOVE
INSERT INTO reservation VALUES ('1', '1', 120, to_date('01/16/2014','MM/DD/YYYY'), 'N');
INSERT INTO reservation VALUES ('2', '2', 250, to_date('02/17/2014','MM/DD/YYYY'), 'Y');
INSERT INTO reservation VALUES ('3', '15', 220, to_date('03/09/2012','MM/DD/YYYY'), 'N');
INSERT INTO reservation VALUES ('4', '7', 210, to_date('04/09/2012','MM/DD/YYYY'), 'Y');
INSERT INTO reservation VALUES ('5', '8', 150, to_date('05/29/2011','MM/DD/YYYY'), 'N');
INSERT INTO reservation VALUES ('6', '14', 200, to_date('05/09/2012','MM/DD/YYYY'), 'Y');
INSERT INTO reservation VALUES ('7', '5', 200, to_date('06/19/2013','MM/DD/YYYY'), 'N');
INSERT INTO reservation VALUES ('8', '6', 325, to_date('07/09/2013','MM/DD/YYYY'), 'Y');
INSERT INTO reservation VALUES ('9', '13', 325, to_date('08/14/2013','MM/DD/YYYY'), 'N');
INSERT INTO reservation VALUES ('10', '3', 200, to_date('08/09/2013','MM/DD/YYYY'), 'Y');
INSERT INTO reservation VALUES ('11', '4', 200, to_date('09/01/2011','MM/DD/YYYY'), 'N');
INSERT INTO reservation VALUES ('12', '1', 275, to_date('11/09/2011','MM/DD/YYYY'), 'Y');
INSERT INTO reservation VALUES ('13', '13', 275, to_date('09/30/2013','MM/DD/YYYY'), 'N');
INSERT INTO reservation VALUES ('14', '2', 275, to_date('10/09/2012','MM/DD/YYYY'), 'Y');
INSERT INTO reservation VALUES ('15', '1', 350, to_date('09/09/2013','MM/DD/YYYY'), 'N');
INSERT INTO reservation VALUES ('16', '3', 350, to_date('09/18/2014','MM/DD/YYYY'), 'N');
INSERT INTO reservation VALUES ('17', '2', 350, to_date('11/09/2012','MM/DD/YYYY'), 'N');
INSERT INTO reservation VALUES ('18', '8', 90, to_date('02/11/2014','MM/DD/YYYY'), 'Y');
INSERT INTO reservation VALUES ('19', '6', 100, to_date('03/09/2014','MM/DD/YYYY'), 'Y');
INSERT INTO reservation VALUES ('20', '2', 100, to_date('05/23/2014','MM/DD/YYYY'), 'Y');



--insert into reservation_detail THESE ALL NEED CHANGED TO MATCH STUFF ABOVE
INSERT INTO reservation_detail VALUES ('1', '153', to_date('09/09/2014','MM/DD/YYYY'), 0);
INSERT INTO reservation_detail VALUES ('2', '153', to_date('09/09/2014','MM/DD/YYYY'), 1);
INSERT INTO reservation_detail VALUES ('3', '155', to_date('01/01/2012','MM/DD/YYYY'), 2);
INSERT INTO reservation_detail VALUES ('4', '156', to_date('12/25/2012','MM/DD/YYYY'), 3);
INSERT INTO reservation_detail VALUES ('5', '157', to_date('09/09/2011','MM/DD/YYYY'), 0);
INSERT INTO reservation_detail VALUES ('6', '158', to_date('09/09/2012','MM/DD/YYYY'), 0);
INSERT INTO reservation_detail VALUES ('7', '159', to_date('09/09/2013','MM/DD/YYYY'), 0);
INSERT INTO reservation_detail VALUES ('8', '160', to_date('09/09/2013','MM/DD/YYYY'), 0);
INSERT INTO reservation_detail VALUES ('9', '170', to_date('09/09/2013','MM/DD/YYYY'), 1);
INSERT INTO reservation_detail VALUES ('10', '171', to_date('09/09/2013','MM/DD/YYYY'), 0);
INSERT INTO reservation_detail VALUES ('11', '172', to_date('09/09/2011','MM/DD/YYYY'), 0);
INSERT INTO reservation_detail VALUES ('12', '173', to_date('09/09/2011','MM/DD/YYYY'), 0);
INSERT INTO reservation_detail VALUES ('13', '174', to_date('09/09/2013','MM/DD/YYYY'), 0);
INSERT INTO reservation_detail VALUES ('14', '175', to_date('09/09/2013','MM/DD/YYYY'), 0);
INSERT INTO reservation_detail VALUES ('15', '175', to_date('09/09/2013','MM/DD/YYYY'), 0);
INSERT INTO reservation_detail VALUES ('16', '175', to_date('09/09/2013','MM/DD/YYYY'), 0);
INSERT INTO reservation_detail VALUES ('17', '158', to_date('09/09/2012','MM/DD/YYYY'), 1);
INSERT INTO reservation_detail VALUES ('18', '153', to_date('09/09/2014','MM/DD/YYYY'), 0);
INSERT INTO reservation_detail VALUES ('19', '157', to_date('04/19/2014','MM/DD/YYYY'), 1);
INSERT INTO reservation_detail VALUES ('20', '170', to_date('08/10/2014','MM/DD/YYYY'), 0);


--Set the system date
insert into our_sys_time values (TO_DATE('11/12/2013', 'MM/DD/YYYY'));

