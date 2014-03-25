//Bret Gourdie and Steve Bauer
//bwg8 & smb158

import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;

public class team02
{

	private Connection connection;
	private static String username;
	private static String password;
	
	public static void main(String args[]) throws SQLException
    {
		try {
			if(args.length == 2 && args[0] != null && args[1] != null){
				System.out.println("Thanks for providing your database login details!");
				username = args[0];
				password = args[1];
			}
			else{
				System.out.println("No database login details provided, using bwg8 account!");
				username = "bwg8"; //This is your username in oracle
				password = "3616714"; //This is your password in oracle
			}
			team02 instance = new team02(username, password);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public team02(String username, String password) throws IOException, SQLException
	{
		try
		{
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			//System.out.println("Successfully registered the Oracle driver.");
			String dburl = "jdbc:oracle:thin:@db10.cs.pitt.edu:1521:dbclass";
			connection = DriverManager.getConnection(dburl, username, password); 
		}
	    catch(Exception Ex)  //What to do with any exceptions
	    {
	      System.out.println("Error connecting to database.  Machine Error: " +
	            Ex.toString());
		Ex.printStackTrace();
	    }
		
		//Initialize the database
		//Populate database with sample data
		
		//Loop until the customer requests to exit
		System.out.println("Welcome to PittAirways!");
		System.out.println("Cheap Airfair is only a login away...\n\n");
		while(true){
			Scanner inScan = new Scanner(System.in);
			System.out.println(
			"|----------------|\n" + 
			"|   User Login   |\n" +
			"|----------------|\n" + 
			"|1) Customer     |\n" + 
			"|2) Administrator|\n" + 
			"|3) Close Program|\n" + 
			"|----------------|\n"
			);
			
			int userType;
			
			do{
				userType = inScan.nextInt();
			} while(userType < 1 || userType > 3);
			
			if(userType == 1)
			{
				customerMenu();
			}
			else if(userType == 2)
			{
				adminMenu();
			}
			else if(userType == 3)
			{
				System.out.println("Thanks for choosing PittAirways!");
				System.exit(0);
			}
		}
	}
	
	public int adminMenu() throws IOException, SQLException
	{
		int userChoiceInt = -1;
		
		while (true){
			
			Scanner inScan = new Scanner(System.in);
			
			userChoiceInt = -1;
			
			System.out.println("|-------------------------------------------|");
			System.out.println("|           Administrator Interface         |");
			System.out.println("|-------------------------------------------|");
			System.out.println("|1) Erase the database                      |");
			System.out.println("|2) Load schedule information               |");
			System.out.println("|3) Load pricing information                |");
			System.out.println("|4) Load plane information                  |");
			System.out.println("|5) Generate passenger manifest	on day      |");
			System.out.println("|6) Log Out                                 |");
			System.out.println("|-------------------------------------------|\n\n");
			
			
			do{
				userChoiceInt = inScan.nextInt();
				if(userChoiceInt < 1 || userChoiceInt > 6)
				{
					System.out.println("Invalid option, Please try again.\n\n");
				}
			} while(userChoiceInt < 1 || userChoiceInt > 6);
			
			switch (userChoiceInt) 
			{
				case 1:
					//erase the database
					eraseDatabase();
					break;
				case 2:
					//Load Schedule Information					
					loadScheduleInformation();
					//print out the output
					break;
				case 3:
					//Load Pricing Information				
					loadPricingInformation();
					//print the output
					
					break;
				case 4:
					//Load the plane information
					loadPlaneInformation();
					//print the output
					break;
				case 5:
					//Generate Passenger Manifest
					generatePassengerManifest();
					//get the return information and print it out
					break;
				default:
					return 0;
			}
		}
	}

	//Admin functions
	// 1) Erase the database
	//Checked
	private void eraseDatabase() throws SQLException {
		System.out.println("/!\\/!\\/!\\/!\\/!\\/!\\/!\\/!\\/!\\/!\\/!\\/!\\/!\\/!\\/!\\\n" + 
		"ARE YOU SURE YOU WANT TO DELETE THE DATABASE?\n" + 
		"Type yes to continue.");
		Scanner inScan = new Scanner(System.in);
		String answer = inScan.nextLine();
		
		if(!answer.equalsIgnoreCase("yes")){
			return;
		}
		
		System.out.print("Deleting all tuples from tables...");
		
		String deleteDetail = "DELETE FROM reservation_detail";
		PreparedStatement preparedStatement = this.connection.prepareStatement(deleteDetail);
		preparedStatement .executeUpdate();
		String deletePrice = "DELETE FROM price";
		preparedStatement = this.connection.prepareStatement(deletePrice);
		preparedStatement .executeUpdate();
		String deleteReservation = "DELETE FROM reservation";
		preparedStatement = this.connection.prepareStatement(deleteReservation);
		preparedStatement .executeUpdate();
		String deleteCustomer = "DELETE FROM customer";
		preparedStatement = this.connection.prepareStatement(deleteCustomer);
		preparedStatement .executeUpdate();
		String deleteFlight = "DELETE FROM flight";
		preparedStatement = this.connection.prepareStatement(deleteFlight);
		preparedStatement .executeUpdate();
		String deletePlane = "DELETE FROM plane";
		preparedStatement = this.connection.prepareStatement(deletePlane);
		preparedStatement .executeUpdate();
		String deleteDate = "DELETE FROM our_sys_time";
		preparedStatement = this.connection.prepareStatement(deleteDate);
		preparedStatement .executeUpdate();
		
		System.out.println(" done!");
	}
	// 2) Load Schedule Information
	//Checked
	private void loadScheduleInformation() throws SQLException {
		System.out.println("---Load Schedule Information---");

		Scanner inScan = new Scanner(System.in);
		try {
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			System.out.println("Enter schedule filename: ");
			String file_name = inScan.nextLine();
			
			FileInputStream fstream = null;
			try{
				fstream = new FileInputStream(file_name);
			}
			catch(Exception ex4){
				System.out.println("The filename you entered is invalid!");
				return;
			}
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			boolean loop = true;
			String ln;

			while(loop) {
				ln = br.readLine();
				if(ln == null) {
					loop = false;
					break;
				}
				
				String[] tokens = ln.split("[ ]+");
				if(tokens.length != 7){
					continue;
				}
				
				/*for(int i = 0; i < tokens.length; i++){
					System.out.print("tokens[" + i + "] = " + tokens[i] + "; ");
				}
				System.out.println();*/

				// Check if pricing info already exists
				String query = "SELECT * FROM FLIGHT WHERE flight_number='" + tokens[0] + "'";
				ResultSet res = statement.executeQuery(query);
				connection.commit();

				if(res.next()){
					System.out.println(tokens[0] + " --> " + tokens[1]);
					System.out.println("Schedule already included for this flight");
				}
				else {
					System.out.println("Loading " + ln);
					query = "INSERT INTO FLIGHT " + "VALUES (?,?,?,?,?,?,?)";

					PreparedStatement preStatement = connection.prepareStatement(query);
					preStatement.setString(1, tokens[0]);
					preStatement.setString(2, tokens[1]);
					preStatement.setString(3, tokens[2]);
					preStatement.setString(4, tokens[3]);
					preStatement.setString(5, tokens[4]);
					preStatement.setString(6, tokens[5]);
					preStatement.setString(7, tokens[6]);
					preStatement.executeUpdate();
				}
			}

			br.close();
			in.close();
			fstream.close();
			statement.close();
			connection.commit();
			connection.setAutoCommit(true);
		} catch (SQLIntegrityConstraintViolationException ex1){
		
			System.out.println("\nOne or more planes for these schedules do not exist;\nmake sure you have loaded the appropriate planes first!");
		
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	// 3) Load Pricing Information
	//Checked
	private void loadPricingInformation() throws SQLException {
		try{
			System.out.println("---Load Pricing Information---");
			Statement statement = connection.createStatement();
			connection.setAutoCommit(false);
			ResultSet res;
			Scanner inScan = new Scanner(System.in);

			System.out.println("Enter L to load pricing information or C to change pricing information");

			String option = "";
			do{
				option = inScan.nextLine();
			}while(!option.equalsIgnoreCase("L") && !option.equalsIgnoreCase("C"));
			
			String query = "";

			// L = load from file
			if((option).toUpperCase().equals("L")) {

				System.out.println("Enter the name of the pricing file you want to load: ");
				String inputFileName = inScan.nextLine();
				
				FileInputStream fstream = null;
				try{
					fstream = new FileInputStream(inputFileName);
				}
				catch(Exception ex5){
					System.out.println("The filename you entered is invalid!");
					return;
				}
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				boolean loop = true;
				String ln;

				// Loop through and attempt to add all new flight pricing
				while(loop) {	
					ln = br.readLine();
					if(ln == null) {
						loop = false;
						break;
					}
					
					String[] tokens = ln.split("[ ]+");
					if(tokens.length != 4){
						continue;
					}
					// Check if pricing info already exists
					query = "SELECT * FROM price WHERE departure_city='" + tokens[0] + "' AND arrival_city='" + tokens[1] + "'";
					res = statement.executeQuery(query);
					connection.commit();

					if(res.next()){
						System.out.println(tokens[0] + " --> " + tokens[1]);
						System.out.println("Pricing already included for this flight");
					}
					else {
						System.out.println("Loading " + ln);
						query = "INSERT INTO price " + "VALUES (?,?,?,?)";

						PreparedStatement preStatement = connection.prepareStatement(query);
						preStatement.setString(1, tokens[0]);
						preStatement.setString(2, tokens[1]);
						preStatement.setString(3, tokens[2]);
						preStatement.setString(4, tokens[3]);
						preStatement.executeUpdate();
					}
				}

				br.close();
				fstream.close();
				in.close();
				connection.commit();

			}
			//C == single entry
			else if((option).toUpperCase().equals("C")) {
				System.out.println("Enter Departure City: ");
				String departure_city = inScan.nextLine();

				System.out.println("Enter Arrival City: ");
				String arrival_city = inScan.nextLine();

				// Check if pricing info already exists
				query = "SELECT * FROM price WHERE departure_city='" + departure_city + "' AND arrival_city='" + arrival_city + "'";
				res = statement.executeQuery(query);
				connection.commit();
				ResultSetMetaData resMeta = res.getMetaData();

				System.out.println(resMeta.getColumnCount());
				
				if(!res.next()) {
					System.out.println("Flight plan does not exist");
				}
				else {
					System.out.println("Enter New High Price: ");
					String high_price = inScan.nextLine();

					System.out.println("Enter New Low Price: ");
					String low_price = inScan.nextLine();

					query = "UPDATE price SET high_price='" + high_price + "', low_price='" + low_price + "' WHERE departure_city='" + departure_city + "' AND arrival_city='" + arrival_city + "'";
					statement.executeUpdate(query);
					connection.commit();
					System.out.println("Price successfully updated!");
				}
			}
		connection.setAutoCommit(true);
		statement.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	// 4) Load Plane Information
	//Checked
	private void loadPlaneInformation() {
		Scanner inScan = new Scanner(System.in);		
		try{
			Statement statement = connection.createStatement();
			connection.setAutoCommit(false);
			
			System.out.println("Enter the filename: ");
			String fname = inScan.nextLine();
			
			ResultSet res;
			FileInputStream fstream = null;
			try{
				fstream = new FileInputStream(fname);
			}
			catch(Exception ex6){
				System.out.println("The filename you entered is invalid!");
			}
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			boolean loop = true;
			String ln;
			String query = "";

			while(loop) {
				ln = br.readLine();
				if(ln == null) {
					loop = false;
					break;
				}
				
				String[] tokens = ln.split("[ ]+");
				if(tokens.length != 5){
					continue;
				}
				// Check if pricing info already exists
				query = "SELECT * FROM plane WHERE plane_type='" + tokens[0]+"'";
				res = statement.executeQuery(query);
				connection.commit();

				if(res.next()){
					System.out.println(tokens[0] + " --> " + tokens[1]);
					System.out.println("Plane already exists");
				}
				else {
					System.out.println("Loading " + ln);
					query = "INSERT INTO plane " + "VALUES (?,?,?,?,?)";

					java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("mm/dd/yyyy"); // set up date format
					java.sql.Date theDate = new java.sql.Date(df.parse(tokens[3]).getTime()); // Parse string into java date

					// Prepare and execute insert statement
					PreparedStatement preStatement = connection.prepareStatement(query);
					preStatement.setString(1, tokens[0]);
					preStatement.setString(2, tokens[1]);
					preStatement.setInt(3, Integer.parseInt(tokens[2]));
					preStatement.setDate(4, theDate);
					preStatement.setInt(5, Integer.parseInt(tokens[4]));
					preStatement.executeUpdate();	
				}
			}
			
			br.close();
			in.close();
			fstream.close();
			statement.close();
			connection.commit();
			connection.setAutoCommit(true);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	// 5) Generate Passenger Manifest For Specific Flight On Given Day
	//Checked
	private void generatePassengerManifest() {	
		Scanner inScan = new Scanner(System.in);
		try {
			
			System.out.println("---Generate Passenger Manifest---");
			
			Statement statement = connection.createStatement();
			
			//Get user input
			System.out.println("Enter the flight number: ");
			String flight_number = inScan.nextLine();
			
			System.out.println("Enter the flight date: ");
			System.out.println("Example: MM/DD/YYYY");
			String string_flight_date = inScan.nextLine();
			SimpleDateFormat dateFormat = 
				new SimpleDateFormat("MM/dd/yyyy");
			java.sql.Date flight_date = 
				new java.sql.Date(
					dateFormat.parse(
						string_flight_date).getTime());
						
			//System.out.println("flight_date = " + flight_date);

			// Check if that flight number is running on the date given
			String query = "SELECT salutation, first_name, last_name " + 
					"FROM customer " + 
					"NATURAL JOIN reservation " + 
					"NATURAL JOIN reservation_detail " + 
					"WHERE flight_number = ? " + 
					"AND flight_date = ? " +
					"AND ticketed = 'Y'";
			
			PreparedStatement prep = connection.prepareStatement(query);
			prep.setString(1,flight_number);
			prep.setDate(2, flight_date);
			ResultSet res = prep.executeQuery();

			boolean noCustomers = true;

			
			while(res.next()){
				if(noCustomers){
					System.out.println("\nCustomers on flight #" + flight_number + " on " + string_flight_date);
					System.out.println("-------------------------------------------");
				}
				System.out.println(res.getString("salutation") + " " + res.getString("first_name") + " " + res.getString("last_name"));
				noCustomers = false;
			}
			
			if(noCustomers) {
				System.out.println("Sorry, the provided flight number does not exist on that date.");
			}
			
			System.out.println();

		statement.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}			
	}
	
	
	public int customerMenu() throws IOException
	{
		int userChoiceInt = -1;
			
		while (true){
			
			Scanner inScan = new Scanner(System.in);
			
			userChoiceInt = -1;
			
			System.out.println("|-------------------------------------------------------------------|");
			System.out.println("|                        Customer Interface                         |");
			System.out.println("|-------------------------------------------------------------------|");
			System.out.println("|1) Add customer                                                    |");
			System.out.println("|2) Show Customer info                                              |");
			System.out.println("|3) Find price for flight between cities                            |");
			System.out.println("|4) Find all routes between cities                                  |");
			System.out.println("|5) Find all routes between cities on given day	with available seats|");
			System.out.println("|6) Add reservation                                                 |");
			System.out.println("|7) Show reservation info                                           |");
			System.out.println("|8) Buy ticket from existing reservation                            |");
			System.out.println("|9) Log Out                                                         |");
			System.out.println("|-------------------------------------------------------------------|\n");
		
			do{
				userChoiceInt = inScan.nextInt();
				if(userChoiceInt < 1 || userChoiceInt > 9)
				{
					System.out.println("Invalid option, Please try again.\n\n");
				}
			} while(userChoiceInt < 1 || userChoiceInt > 9);
			
			switch (userChoiceInt) 
			{
				case 1:
					addCustomer();
					break;
				case 2:
					showCustomerInfo();
					break;
				case 3:
					findPriceBetweenCities();
					break;
				case 4:
					findAllRoutesBetweenCities();
					break;
				case 5:
					findAllRoutesBetweenCitiesAvailable();
					break;
				case 6:
					addReservation();
					break;
				case 7:
					showReservationInfo();
					break;
				case 8:
					buyTicketFromExistingReservation();
					break;
				case 9:
					return 0;
			}
		}		
	}

	//Customer Functions
	// 1) Add Customer
	//Checked
	public void addCustomer()
	{
		try 
		{
			Statement statement = connection.createStatement();
			connection.setAutoCommit(true);
			Scanner inScan = new Scanner(System.in);;
			
			String salutation;
			String first_name;
			String last_name;
			String street;
			String city;
			String state;
			String phone_number;
			String email_address;
			String credit_card_number;
			String credit_card_expiration_date;
			String query = "";
			
			System.out.println("---Add Customer---");
			
			System.out.println("Enter your salutation:");
			System.out.println("Example:Mr Mrs Ms");
			salutation = inScan.nextLine();
			System.out.println("Enter your first name:");
			first_name = inScan.nextLine();
			System.out.println("Enter your last name:");
			last_name = inScan.nextLine();
			System.out.println("Enter your credit card number:");
			credit_card_number = inScan.nextLine();
			System.out.println("Enter your credit card expiration date:");
			System.out.println("Example: 02/2020"); 
			credit_card_expiration_date = inScan.nextLine();
			System.out.println("Enter your street:");
			street = inScan.nextLine();
			System.out.println("Enter your city:");
			city = inScan.nextLine();
			System.out.println("Enter your state:");
			state = inScan.nextLine();
			System.out.println("Enter your phone number without dashes:");
			phone_number = inScan.nextLine();
			System.out.println("Enter your email address:");
			email_address = inScan.nextLine();
			
			//Parse expiration date
			SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("mm/yyyy");
			java.sql.Date exp_date = new java.sql.Date(dateFormat.parse(credit_card_expiration_date).getTime());
			
			//Get new ID number
			query = "SELECT MAX(TO_NUMBER(cid)) FROM CUSTOMER";
			ResultSet result = statement.executeQuery(query);
			String newId = "0";
			while(result.next()){
				newId = Integer.toString(result.getInt(1) + 1);
			}
			
			//System.out.println("newId = " + newId);
			
			query = "INSERT INTO CUSTOMER " + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preStatement = connection.prepareStatement(query);
			preStatement.setString(1, newId);
			preStatement.setString(2, salutation);
			preStatement.setString(3, first_name);
			preStatement.setString(4, last_name);
			preStatement.setString(5, credit_card_number);
			preStatement.setDate(6, exp_date);
			preStatement.setString(7, street);
			preStatement.setString(8, city);
			preStatement.setString(9, state);
			preStatement.setString(10, phone_number);
			preStatement.setString(11, email_address);
			preStatement.executeUpdate();
			
			statement.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	// 2) Show Customer Info, Given Customer Name
	//Checked
	public void showCustomerInfo()
	{		
		System.out.println("---Show customer information---");
		Scanner inScan = new Scanner(System.in);
		String firstName;
		String lastName;
		System.out.println("Enter Customer's first name:");
		firstName = inScan.nextLine();
		System.out.println("Enter Customer's last name:");
		lastName = inScan.nextLine();
		

		try{
			Statement statement = connection.createStatement(); 
			String query = "SELECT * FROM CUSTOMER WHERE first_name = '"+firstName+"' AND last_name = '"+lastName+"'";	

			ResultSet results = statement.executeQuery(query);
			boolean customer_found = false;
			// Display the customer information
			if(results.next()) 
			{
				System.out.println("\n---------------------------------------\n" +
				"Salutation: " + results.getString("salutation") + "\n" +
				"Customer: " + results.getString("first_name") + " " +
				results.getString("last_name") + "\n" +
				"Credit Card Number: " + results.getString("credit_card_num") + "\n" +
				"Credit Card Expiration: " + results.getDate("credit_card_expire") + "\n" +
				"Address: " + results.getString("street") + " " +
				results.getString("city") + ", " +
				results.getString("state") + "\n" +
				"Phone Number: " + results.getString("phone") + "\n" +
				"Email Address: " + results.getString("email") +
				"\n------------------------------------\n");
				customer_found=true;
			}
			statement.close();
			if(customer_found == false){
				System.out.println("This user was not found.");
			}
		}catch(Exception ex){
			System.out.println("Unexpected error, Please try again.");
		}
	}
	// 3) Find Price For Flights Between Two Cities
	//Checked
	public void findPriceBetweenCities()
	{
		Scanner inScan = new Scanner(System.in);;
		System.out.println("---Find price between two cities---");
		System.out.println("Enter the departure city:");
		String dep = inScan.nextLine();
		System.out.println("Enter the destination city:");
		String dest = inScan.nextLine();
		
		try 
		{
			Statement statement = connection.createStatement();
			String query = "SELECT high_price, low_price FROM PRICE WHERE departure_city = ? AND arrival_city = ?";
			PreparedStatement preStatement = connection.prepareStatement(query);
			preStatement.setString(1, dep);
			preStatement.setString(2, dest);
			
			ResultSet result = preStatement.executeQuery();
			
			int highprice_DepToDest = 0;
			int lowprice_DepToDest = 0;
			while(result.next()){
				highprice_DepToDest = result.getInt(1);
				lowprice_DepToDest = result.getInt(2);
			}
			
			//Same query, switch dest with dep (for dest-to-dep and round trip)
			query = "SELECT high_price, low_price FROM PRICE WHERE departure_city = ? AND arrival_city = ?";
			preStatement = connection.prepareStatement(query);
			preStatement.setString(1, dest);
			preStatement.setString(2, dep);
			
			result = preStatement.executeQuery();
			
			int highprice_DestToDep = 0;
			int lowprice_DestToDep = 0;
			while(result.next()){
				highprice_DestToDep = result.getInt(1);
				lowprice_DestToDep = result.getInt(2);
			}
			
			System.out.println(dep + " to " + dest + " high price: " + highprice_DepToDest + "; low price: " + lowprice_DepToDest);
			System.out.println(dest + " to " + dep + " high price: " + highprice_DestToDep + "; low price: " + lowprice_DestToDep);
			System.out.println(dep + " to " + dest + " to " + dep + " (roundtrip) high price: " + (highprice_DepToDest + highprice_DestToDep)
				+ "; low price: " + (lowprice_DepToDest + lowprice_DestToDep));
			
			
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	// 4) Find All Routes Between Two Cities
	//Checked
	public void findAllRoutesBetweenCities()
	{
		System.out.println("---Find All Routes Between Cities---");
		Scanner inScan = new Scanner(System.in);;
		System.out.println("Enter departure city: ");
		String departure_city = inScan.nextLine();
		System.out.println("Enter arrival city: ");
		String arrival_city = inScan.nextLine();
		
		try{
			//Direct
			Statement statement = connection.createStatement(); 
			System.out.println("Direct:");
			String query = "SELECT flight_number, departure_city, arrival_city, departure_time, arrival_time "+
			"FROM FLIGHT WHERE departure_city = '"+departure_city+"' AND arrival_city = '"+arrival_city+"'";
			
			ResultSet results = statement.executeQuery(query);
			boolean empty=true;
			while(results.next()) 
			{
				System.out.println("Record: " +
				results.getString(1) + " " + // flight number
				results.getString(2) + " " + //departure city
				results.getString(3) + " " + //arrival city
				results.getString(4) + " " + //departure time
				results.getString(5));	   //arrival time
				empty=false;
			}
			if(empty){
				System.out.println("No Direct Flights.");
			}
			//Connection
			System.out.println("Single Connection:");
			query = "SELECT A.flight_number, B.flight_number, A.departure_city, B.departure_city, A.departure_time, B.arrival_time, "+
			"A.weekly_schedule, B.weekly_schedule "+
			"FROM FLIGHT A, FLIGHT B WHERE A.departure_city='"+departure_city+"' AND B.arrival_city='"+arrival_city+"' "+
			"AND A.arrival_city = B.departure_city AND to_number(A.arrival_time) < to_number(B.departure_time) - 100";	
			
			results = statement.executeQuery(query);
			empty=true;
			while(results.next()) 
			{
				String A_weekly = results.getString(7);
				String B_weekly = results.getString(8);
				boolean same_day = false;
				for(int i=0;i<7;i++){
					if(A_weekly.charAt(i)==B_weekly.charAt(i)){
						same_day = true;
						break;
					}
				}
				if(same_day){
					System.out.println("Record: " + results.getString(1) + " " + results.getString(2) + " " + results.getString(3) + " " + results.getString(4) + " " + results.getString(5) + " " + results.getString(6));
					empty=false;
				}
				
			}
			if(empty){
				System.out.println("No Connecting Flights.");
			}
			statement.close();
		}catch(SQLException se){
			se.printStackTrace();
		}
	}
	// 5) Find All Routes With Available Seats Between Two Cities On Given Day
	public void findAllRoutesBetweenCitiesAvailable()
	{
		Scanner inScan = new Scanner(System.in);
		String query = "";
		String dep = "";
		do{
			System.out.println("Enter departure city: ");
			dep = inScan.nextLine();
		}
		while(dep.length() != 3);
		
		String arr = "";
		do{
			System.out.println("Enter arrival city: ");
			arr = inScan.nextLine();
		}
		while(arr.length() != 3);
		
		String fDate = "";
		do{
			System.out.println("Enter the date: ");
			System.out.println("Example: MM/DD/YYYY");
			fDate = inScan.nextLine();
		}
		while(fDate.length() != 10);

		// Figure out which day of the week was requested
		Calendar cal = Calendar.getInstance();
		String[] tokens = fDate.split("/");
		cal.set(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[0])-1, Integer.parseInt(tokens[1]));
		int day = cal.get(Calendar.DAY_OF_WEEK)-1;

		try{
			//Direct Flight
			Statement statement = connection.createStatement(); 
			System.out.println("Direct Flight:");
			query = "SELECT * FROM flight WHERE departure_city = '"+dep+"' AND arrival_city = '"+arr+"'";
			ResultSet results = statement.executeQuery(query);

			//Only display flights on the requested day that have available seats
			Statement statement2 = connection.createStatement();
			ResultSet res2;

			while(results.next()) {
				String flight_num = results.getString("flight_number");
				String plane_type = results.getString("plane_type");
				String departure_city = results.getString("departure_city");
				String arrival_city = results.getString("arrival_city");
				String departure_time = results.getString("arrival_time");
				String arrival_time = results.getString("departure_time");
				String weekly_schedule = results.getString("weekly_schedule");

				if(weekly_schedule.charAt(day) == '-') {
					//Flight unavailable
					continue;
				}
				
				//Grab the plane type and determine the capacity
				query = "SELECT plane_capacity FROM plane WHERE plane_type='"+plane_type+"'";
				res2 = statement2.executeQuery(query);
				int flight_cap = 0;
				if(res2.next()) {
					flight_cap = res2.getInt("plane_capacity");
				}
				else {
					continue;
				}

				//Check if there are remaining seats left
				query = "SELECT ("+flight_cap+"-COUNT(*)) FROM reservation " +
						"NATURAL JOIN reservation_detail " +
						"WHERE flight_number='"+flight_num+"' " +
						"GROUP BY flight_number";
				res2 = statement2.executeQuery(query);
				if(res2.next()) {
					int availSeats = res2.getInt(1);
					if(availSeats == 0) {
						continue;
					}
				}

				//This flight is available on the requested day and has open seats!
				System.out.println("Record: " + flight_num + " " + departure_city + " " + arrival_city + " " + departure_time+ " " + arrival_time);
			}

			// Connecting flight
			System.out.println("Single Connection Flight:");
			query = "SELECT * FROM flight A, flight B WHERE A.departure_city='"+dep+"' AND B.arrival_city='"+arr+
			"' AND A.arrival_city = B.departure_city AND to_number(A.arrival_time) < to_number(B.departure_time) - 100";		
			results = statement.executeQuery(query);
  
			while(results.next()) {
				// First Flight
				String flight1_num = results.getString(1);
				String flight1_type = results.getString(2);
				String flight1_dep = results.getString(3);
				String flight1_arr = results.getString(4);
				String flight1_dep_time = results.getString(5); 
				String flight1_arr_time = results.getString(6);
				String flight1_sched = results.getString(7);
				// Second Flight
				String flight2_num = results.getString(8);
				String flight2_type = results.getString(9);
				String flight2_dep = results.getString(10);
				String flight2_arr = results.getString(11);
				String flight2_dep_time = results.getString(12);
				String flight2_arr_time = results.getString(13);
				String flight2_sched = results.getString(14);

				// Check to make sure flights are available on requested day of week
				if((flight1_sched.charAt(day) == '-') || (flight2_sched.charAt(day) == '-')) {
					System.out.println("No flight on this day");
					continue;
				}


				// Get capacity of both planes
				query = "SELECT A.plane_capacity, B.plane_capacity FROM plane A, plane B WHERE " +
						"A.plane_type='"+flight1_type+"' AND B.plane_type='"+flight2_type+"'";
				res2 = statement2.executeQuery(query);
				int flight1_capacity;
				int flight2_capacity;
				if(res2.next()) {
					flight1_capacity = res2.getInt(1);
					flight2_capacity = res2.getInt(2);
				}
				else {
					continue;
				}


				// Get open seats for first flight
				query = "SELECT ("+flight1_capacity+"-COUNT(*)) FROM reservation " + 
						"NATURAL JOIN reservation_detail " +
						"WHERE flight_number='"+flight1_num+"' " +
						"GROUP BY flight_number";
				res2 = statement2.executeQuery(query);
				if(res2.next()) {
					int availSeats = res2.getInt(1);
					if(availSeats == 0) {
						continue;
					}
				}

				// Get open seats for second flight
				query = "SELECT ("+flight2_capacity+"-COUNT(*)) FROM reservation " + 
						"NATURAL JOIN reservation_detail " +
						"WHERE flight_number='"+flight2_num+"' " +
						"GROUP BY flight_number";
				res2 = statement2.executeQuery(query);
				if(res2.next()) {
					int availSeats = res2.getInt(1);
					if(availSeats == 0) {
						continue;
					}
				}
	
				// Both flights look good! lets print them out to the user.
				System.out.println("Record: ");
				System.out.println("Leg 1: " + flight1_num + " " + flight1_dep + " " + flight1_arr + " " + flight1_dep_time + " " + flight1_arr_time);
				System.out.println("Leg 2: " + flight2_num + " " + flight2_dep + " " + flight2_arr + " " + flight2_dep_time + " " + flight2_arr_time);
			}
		statement.close();
		statement2.close();
		} catch(Exception e9){
			System.out.println("Unexpected error, Please try again.");
		}
		System.out.println("\n");
	}
	// 6) Add Reservation
	public int addReservation()
	{
		/* oh boy, this method is a mess. We need to ask the user for their CID and the flight number for each leg of their flight. If they enter 0 for flight number that signifies that they are done making their reservation */
		
		Scanner inScan = new Scanner(System.in);
		String query = "";
		String cid = "";
		do{
			System.out.println("Enter your Customer ID: ");
			cid = inScan.nextLine();
		}
		while(cid.length() < 1 ||cid.length() > 9);
		
		try{
		Statement statement = connection.createStatement();
				query = "SELECT cid FROM customer WHERE cid='"+cid+"'";
				ResultSet results = statement.executeQuery(query);
				if(!results.next()){
					System.out.println("This customer ID does not exist. Please try again");
					return -1;
				}
			}catch(Exception e){
				System.out.println("Error selecting provided CID");
				return -1;
			}
		
		System.out.println("Enter flight number: ");
		int cur_flight;
		cur_flight = Integer.parseInt(inScan.nextLine());
		String flight_date [] = new String[4];
		if(cur_flight==0) return -1;
		try{
				query = "SELECT flight_number FROM flight WHERE flight_number='"+cur_flight+"'";
				Statement statement = connection.createStatement();
				ResultSet results = statement.executeQuery(query);
				if(!results.next()){
					System.out.println("This flight number does not exist.");
					return -1;
				}
			}catch(Exception e){
				System.out.println("Error selecting flights with provided flight_number");
				System.exit(1);
			}
			
			do{
				System.out.println("Enter flight date: ");
				System.out.println("Example: MM/DD/YYYY");
				flight_date[0] = inScan.nextLine();
			}
			while(flight_date[0].length() != 10);
			
			Calendar cal = Calendar.getInstance();
			String[] tokens = (flight_date[0]).split("/");
			cal.set(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[0])-1, Integer.parseInt(tokens[1]));
			int day = cal.get(Calendar.DAY_OF_WEEK)-1;
			
			try{
				query = "SELECT weekly_schedule FROM flight WHERE flight_number='"+cur_flight+"'";
				Statement statement = connection.createStatement();
				ResultSet results = statement.executeQuery(query);
				while(results.next()){
				// Check if the flight flies on that day of the week
					if((results.getString(1)).charAt(day) == '-') {
						System.out.println("No flight on this day");
						return -1;
					}
					break;
				}
			}catch(Exception e){
				System.out.println("Error selecting schedule for flight with provided flight_number.");
				System.exit(1);
			}
		int leg = 0;
		int flight_num [] = new int[4];
		flight_num[leg]=cur_flight;
		leg+=1;
		
		while(cur_flight>0 && leg<3){
			
			System.out.println("Enter flight number of next leg: ");
			cur_flight = Integer.parseInt(inScan.nextLine());
			if(cur_flight==0) break;
			
			try{
				query = "SELECT flight_number FROM flight";
				Statement statement = connection.createStatement();
				ResultSet results = statement.executeQuery(query);
				if(!results.next()){
					System.out.println("This flight number does not exist.");
					return -1;
				}
			}catch(Exception e){
				System.out.println("Error selecting flight with provided flight_number.");
				System.exit(1);
			}
			
			System.out.println("Enter flight date of next leg:");
			System.out.println("Example: mm/dd/yyyy");
			flight_date[leg] = inScan.nextLine();
			
			//turn the date into index
			cal = Calendar.getInstance();
			tokens = (flight_date[leg]).split("/");
			cal.set(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[0])-1, Integer.parseInt(tokens[1]));
			day = cal.get(Calendar.DAY_OF_WEEK)-1;
			
			try{
				query = "SELECT weekly_schedule FROM flight WHERE flight_number='"+cur_flight+"'";
				Statement statement = connection.createStatement();
				ResultSet results = statement.executeQuery(query);
				while(results.next()){
					//System.out.println("results.getString(1) = " + results.getString(1) + "; day = " + day);
					//Make sure the flight exists on requested day of the week
					if((results.getString(1)).charAt(day) == '-') {
						System.out.println("No flight on this day");
						return -1;
					}
					break;
				}
			}catch(Exception e){
				System.out.println("Error selecting schedule.");
				System.exit(1);
			}
			
			flight_num[leg]=cur_flight;
			leg+=1;
		}
		
		int next_res_num=0;
		try{
			Statement statement = connection.createStatement();
			query = "(SELECT MAX(to_number(reservation_number)) FROM reservation)";
			ResultSet results = statement.executeQuery(query);
			boolean empty=true;
			while(results.next()){
				next_res_num = (results.getInt(1))+1;
				empty=false;
				break;
			}
			if(empty){
				return -1;
			}
						
			int cost=0;
			
			if(leg==1){
				query="SELECT A.high_price FROM price A, flight B "+
				"WHERE A.departure_city=B.departure_city AND A.arrival_city=B.arrival_city "+
				"AND B.flight_number='"+flight_num[0]+"' "+
				"AND TRUNC((SELECT c_date FROM our_sys_time WHERE rownum=1),'DDD')=TRUNC(to_date('"+flight_date[0]+"','MM/DD/YYYY'),'DDD')";
				
				results = statement.executeQuery(query);
				empty=true;
				while(results.next()){
					cost = results.getInt(1);
					empty=false;
					break;
				}
				if(empty){
					query="SELECT A.low_price FROM price A, flight B "+
					"WHERE A.departure_city=B.departure_city AND A.arrival_city=B.arrival_city "+
					"AND B.flight_number='"+flight_num[0]+"' ";
					results = statement.executeQuery(query);
					empty=true;
					while(results.next()){
						cost = results.getInt(1);
						empty=false;
						break;
					}
					if(empty){
						System.out.println("Flight number doesn't exist.");
						return -1;
					}
				}
			}else{
				//For connections we always use low price for both legs
				query="SELECT A.low_price, C.low_price FROM price A, flight B, price C, flight D "+
				"WHERE A.departure_city=B.departure_city AND A.arrival_city=B.arrival_city "+
				"AND B.flight_number='"+flight_num[0]+"' "+
				"AND C.departure_city=D.departure_city AND C.arrival_city=D.arrival_city "+
				"AND D.flight_number='"+flight_num[1]+"' ";
				
				results = statement.executeQuery(query);
				empty=true;
				while(results.next()){
					cost = results.getInt(1);
					cost += results.getInt(2);
					empty=false;
					break;
				}				
				if(empty){
					return -1;
				}
				
				if(leg==4){
					if(flight_date[2].equals(flight_date[3])){
						query="SELECT A.low_price, C.low_price FROM price A, flight B, price C, flight D "+
						"WHERE A.departure_city=B.departure_city AND A.arrival_city=B.arrival_city "+
						"AND B.flight_number='"+flight_num[2]+"' "+
						"AND C.departure_city=D.departure_city AND C.arrival_city=D.arrival_city "+
						"AND D.flight_number='"+flight_num[3]+"' ";
						
						results = statement.executeQuery(query);
						empty=true;
						while(results.next()){
							cost += results.getInt(1);
							cost += results.getInt(2);
							empty=false;
							break;
						}				
						if(empty){
							System.out.println("Flight numbers return trip doesn't exist.");
							return -1;
						}
					}
				}	
			}
			
			connection.setAutoCommit(false);
			query = "INSERT INTO reservation VALUES ('"+next_res_num+"', "+"'"+cid+"', "+cost+", "+"(select * from our_sys_time), 'N')";			
			if(statement.executeUpdate(query)!=1){
				System.out.println("Failed to insert into reservation "+next_res_num+", "+cid+", "+cost);
				return -1;
			}
			//Insert the reservation for each of the legs
			for(int i=0;i<leg;i++)
			{
				//System.out.println("next_res_num = " + next_res_num + "; flight_num[i] = " + flight_num[i] + "; flight_date[i] = " + flight_date[i] + "; i = " + i);
				query = "INSERT INTO reservation_detail VALUES ('"+next_res_num+"', '"+flight_num[i]+"', to_date('"+flight_date[i]+"','MM/DD/YYYY'), "+i+")";
				if(statement.executeUpdate(query)!=1){
					System.out.println("Failed to insert reservation detail for reservation="+next_res_num+", flight_number='"+flight_num[i]+", date="+flight_date[i]+", leg="+i);
					return -1;
				}				
			}
			
			//If we made it here we successfully inserted the reservations for each leg
			System.out.println("Successfully added reservations for all legs, your reservation number is: "+next_res_num);
			
			
			connection.commit();
			connection.setAutoCommit(true);
			statement.close();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Unexpected error, Please try again.");
		}
		return next_res_num;
	}
	// 7) Show Reservation Info, Given Reservation Number
	//Checked
	public void showReservationInfo()
	{
		Scanner inScan = new Scanner(System.in);
		try {
			Statement statement = connection.createStatement();
			System.out.println("Enter your reservation number: ");
			String reservation_number = inScan.nextLine();

			String query = "SELECT * FROM reservation NATURAL JOIN reservation_detail NATURAL JOIN customer WHERE reservation_number = ?";
			PreparedStatement preState = connection.prepareStatement(query);
			preState.setInt(1, Integer.parseInt(reservation_number));
			ResultSet results = preState.executeQuery();
			boolean exists = false;
			while(results.next()){
				System.out.println(
					"Customer Name: " + results.getString("first_name") + " " + results.getString("last_name") + "\n" + 
					"Cost: " + results.getString("cost") + "\n" + 
					"Reservation Date: " + results.getString("reservation_date") + "\n" + 
					"Ticketed: " + results.getString("ticketed") + "\n" + 
					"Flight Number: " + results.getString("flight_number") + "\n" + 
					"Flight Date: " + results.getString("flight_date") + "\n" + 
					"Leg: " + results.getString("leg") + "\n"
				);
				exists = true;
			}
			if(!exists) {
				System.out.println("Invalid reservation number, Please try again.");
			}
			statement.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}		
	}
	// 8) Buy Ticket From Existing Reservation
	//Checked
	public void buyTicketFromExistingReservation()
	{
		Scanner inScan = new Scanner(System.in);;
		String reservation_number = "";
		do{
		System.out.println("Enter your reservation number: ");
		reservation_number = inScan.nextLine();
		}
		while(reservation_number.length() < 1 || reservation_number.length() > 5);
		
		//Find matching reservation
		try{
			//First make sure ticket has not been purchased
			Statement statement = connection.createStatement();
			connection.setAutoCommit(false);
			String query = "SELECT ticketed, cost FROM reservation WHERE reservation_number = ?";
			PreparedStatement prestate = connection.prepareStatement(query);
			prestate.setString(1, reservation_number);
			ResultSet ticketRes = prestate.executeQuery();
			
			//Purchase Ticket
			while(ticketRes.next()){
				if(ticketRes.getString("ticketed").equals("N")){
					query = "UPDATE reservation SET ticketed='Y' WHERE reservation_number='"+reservation_number+"'";
					
					if(statement.executeUpdate(query)!=1){
						System.out.println("Update Failed!");
						throw new Exception();
					}
					System.out.println("Ticket purchased for $" + ticketRes.getInt("cost") + "; thank you for your business!");
				}
				else{
					System.out.println("This ticket has already been paid for. You may have entered an incorrect reservation number.");
				}
			}
			connection.commit();
			statement.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
