
import java.io.*;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.*;



public class AddressBook {
	Map<String, Contact> Book = new HashMap<String, Contact>();
	public static List<Contact> contactList;
	AddressBookDBService dbService;
	
	public AddressBook(List<Contact> employeePayrollList) {
		contactList=employeePayrollList;
		}
	public AddressBook() {
		this.dbService =   new AddressBookDBService();
	}

	public enum IOService{
		DB_IO,FILE_IO,CONSOLE_IO};
	

	public void addContact(String firstname, Contact person) {
		Book.put(firstname, person);
		System.out.println("Contact added: " + firstname);
		person.print();
	}

	public void edit(String firstname, String field, String newValue) {
		if (Book.size() == 0) {
			System.out.println("empty book");
			return;
		}

		Contact p = Book.get(firstname);
		if (field.equalsIgnoreCase("firstname"))
			p.firstname = newValue;
		if (field.equalsIgnoreCase("lastname"))
			p.lastname = newValue;
		if (field.equalsIgnoreCase("address"))
			p.address = newValue;
		if (field.equalsIgnoreCase("city"))
			p.city = newValue;
		if (field.equalsIgnoreCase("state"))
			p.state = newValue;
		if (field.equalsIgnoreCase("zip"))
			p.zip = newValue;
		if (field.equalsIgnoreCase("phone number"))
			p.phone = newValue;
		if (field.equalsIgnoreCase("email"))
			p.email = newValue;
		System.out.println("Edited successfull : " + field + " to " + newValue);
		p.print();

	}

	public void deleteContact(String firstname) {
		if (Book.containsKey(firstname)) {
			Book.remove(firstname);
			System.out.println("Contact successfully deleted");
		} else
			System.out.println("Contact not found");
	}

	// Method to add data to a file
	public void writeData(ArrayList<Contact> contacts) {
		StringBuffer contactBuffer = new StringBuffer();
		contacts.forEach(contact -> {
			String contactData = contact.toString().concat("\n");
			contactBuffer.append(contactData);
		});
		try {
			Files.write(Paths.get("addressFile.txt"), contactBuffer.toString().getBytes());

		} catch (IOException e) {

		}
	}

	// Method to read data from a file
	public long readData() {
		List<String> contactList = null;
		try {
			contactList = Files.lines(new File("addressFile.txt").toPath()).map(line -> line.trim())
					.collect(Collectors.toList());

		} catch (IOException e) {

		}
		
		return contactList.size();

	}

	// method to read data from csv file
	public int readCsv() throws IOException {
		int count = 0;

		try (Reader reader = Files.newBufferedReader(Paths.get("SampleCsv.csv"));) {
			CsvToBean<Contact> csvToBean = new CsvToBeanBuilder<Contact>(reader).withType(Contact.class)
					.withIgnoreLeadingWhiteSpace(true).build();

			Iterator<Contact> csvUserIterator = csvToBean.iterator();

			while (csvUserIterator.hasNext()) {
				count++;
				Contact contact = csvUserIterator.next();
				System.out.println("FirstName : " + contact.getFirstName());
				System.out.println("LastName : " + contact.getLastName());
				System.out.println("Email : " + contact.getEmail());
				System.out.println("PhoneNo : " + contact.getPhoneNo());
				System.out.println("zip : " + contact.getZip());
				System.out.println("city : " + contact.getCity());
				System.out.println("state : " + contact.getState());

			}
		}
		return count;
	}

	// Method to write to a CSV File
	public static int writeCsv(ArrayList<Contact> contacts)
			throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
		Writer writer = Files.newBufferedWriter(Paths.get("SampleCsv.csv"));
		StatefulBeanToCsv<Contact> beanToCsv = new StatefulBeanToCsvBuilder<Contact>(writer)
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).build();
		beanToCsv.write(contacts);
		return contacts.size();
	}

	// Method to read from JSON file using GSON
	public static int readJSON() throws IOException {
		int count = 0;
		Reader reader = Files.newBufferedReader(Paths.get("contacts.json"));
		List<Contact> users = new Gson().fromJson(reader, new TypeToken<List<Contact>>() {
		}.getType());
		for (Contact user : users) {
			System.out.println(user);
			count++;
		}
		return count;
	}

	// method to write json
	public static int writeJSON(ArrayList<Contact> contacts) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(contacts);
		FileWriter writer = new FileWriter("contacts.json");
		writer.write(json);
		writer.close();
		return contacts.size();
	}
	
	public List<Contact> readContact()
	{
		contactList= dbService.readContacts();
		return contactList;
	}

	public void updateLastname(String firstname, String lastname) {
			int result = dbService.updateLastName(firstname,lastname);
			if (result == 0)
				return;
			Contact contact = this.getContact(firstname);
			if (contact != null)
				contact.lastname = lastname;

		}

	private Contact getContact(String firstname) {
		return contactList.stream().filter(contact -> contact.firstname.equalsIgnoreCase(firstname)).findFirst().orElse(null);
	}

	public boolean checkContactInSyncWithDB(String firstName) {
		List<Contact> ContactDataList = dbService.getContacts(firstName);
		return ContactDataList.get(0).equals(getContact(firstName));
	}
	public List<Contact> readContactByCity(String city) {
		return dbService.getContactsByCity(city);
	}
	public List<Contact> readContactByState(String state) {
		return dbService.getContactsByState(state);

	}
	public void add(String firstName, String lastName,String address, String city, String state, String zip,String phone,String email,String type) {
		try {
			contactList.add(dbService.add(firstName,lastName,address,city,state,zip,phone,email,type));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
}