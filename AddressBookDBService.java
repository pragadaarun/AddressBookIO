import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.StyledEditorKit.StyledTextAction;

public class AddressBookDBService {

	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/AddressBook?useSSL=false";
		String userName = "root";
		String password = "root";
		Connection connection;
		System.out.println("connecting to database : " + jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		System.out.println("connection is successfull " + connection);
		return connection;
	}

	public List<Contact> readContacts() {
		String sql = "SELECT * from contact;";
		return getConactDataUsingDB(sql);
	}

	private List<Contact> getConactDataUsingDB(String sql) {
		List<Contact> contactList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			contactList = this.getContactData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}

	private List<Contact> getContactData(ResultSet resultSet) {
		List<Contact> contactList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String firstname = resultSet.getString("firstname");
				String lastname = resultSet.getString("lastname");
				String address = resultSet.getString("address");
				String city = resultSet.getString("city");
				String state = resultSet.getString("state");
				String zip = resultSet.getString("zip");
				String phone = resultSet.getString("phno");
				String email = resultSet.getString("email");
				contactList.add(new Contact(id, firstname, lastname, address, city, state, zip, phone, email));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}

	public int updateLastName(String firstname, String lastname) {
		String sql = String.format("update contact set lastname='%s' where firstname ='%s';", lastname, firstname);
		try {
			Connection connecton = this.getConnection();
			Statement statement = connecton.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<Contact> getContacts(String firstName) {
		String sql = String.format("select * from contact where firstname = '%s';", firstName);
		return this.getConactDataUsingDB(sql);
	}

	public List<Contact> getContactsByCity(String city) {
		String sql = String.format("select * from contact where city = '%s';", city);
		return this.getConactDataUsingDB(sql);

	}

	public List<Contact> getContactsByState(String state) {
		String sql = String.format("select * from contact where state = '%s';", state);
		return this.getConactDataUsingDB(sql);
	}

	public Contact add(String firstName, String lastName, String address, String city, String state, String zip,
			String phone, String email, String type) throws SQLException {
		Connection connection = null;
		Contact contact = null;
		try {
			connection = this.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// to get addressbook ID/if not insert one
		int id = 0;
		try (Statement statement = connection.createStatement()) {
			String sqlForType = String.format("select id from addressbook where name = '%s';", type);
			ResultSet result = statement.executeQuery(sqlForType);
			if (result.next())
				id = result.getInt("id");
			else {
				String sqlForInsertion = String.format("insert into addressBook(name) values ('%s');", type);
				int rowAffected = statement.executeUpdate(sqlForInsertion, statement.RETURN_GENERATED_KEYS);
				if (rowAffected == 1)
					result = statement.getGeneratedKeys();
				if (result.next())
					id = result.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			connection.rollback();
		}

		// insert into contact
		String sqlForContact = String.format(
				"insert into contact(firstname,lastname,address,city,state,zip,phno,email) values ('%s','%s','%s','%s','%s','%s','%s','%s');",
				firstName, lastName, address, city, state, zip, phone, email);
		int contact_id = 0;
		try (Statement statement = connection.createStatement()) {
			int row = statement.executeUpdate(sqlForContact, statement.RETURN_GENERATED_KEYS);
			if (row == 1) {
				ResultSet result = statement.getGeneratedKeys();
				if(result.next())
				contact_id = result.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			connection.rollback();
		}
		
		//insert into contact_boo table
		String sqlForCombined = String.format("insert into contact_book(contact,book) values ('%d','%d');",contact_id,id);
		try(Statement statement=connection.createStatement())
		{
			int rows = statement.executeUpdate(sqlForCombined);
			if(rows==1)
			{
				contact = new Contact(contact_id,firstName,lastName,address,city,state,zip,phone,email);
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
			connection.rollback();
		}
		
		
		try {
			connection.commit();
		}catch(SQLException e)
		{
			e.printStackTrace();
			connection.rollback();
		}
		return contact;
	}
	

}