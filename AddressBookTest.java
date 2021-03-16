import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class AddressBookTest {
	
	@Test
	public void givenContactInDB_shouldMatchContactCount() {
		AddressBook book = new AddressBook();
		List<Contact> data = book.readContact();
		Assert.assertEquals(3, data.size());
	}
	
	@Test
	public void givenNewLastname_WhenUpdated_shouldMatch() {
		AddressBook book = new AddressBook();
		List<Contact> data = book.readContact();
		book.updateLastname("Mani", "Pragada");
		boolean result = book.checkContactInSyncWithDB("Mani");
		Assert.assertTrue(result);
	}
	
	@Test
	public void givenCity_shouldReturnContacts() {
		AddressBook book = new AddressBook();
		List<Contact> data = book.readContactByCity("Rajahmundry");
		Assert.assertEquals(2, data.size());
		
	}
	
	@Test
	public void givenState_shouldReturnContacts() {
		AddressBook book = new AddressBook();
		List<Contact> data = book.readContactByState("AP");
		Assert.assertEquals(3, data.size());
		
	}
	
	@Test
	public void givenNewContact_WhenAdded_shouldMatch() {
		AddressBook book = new AddressBook();
		List<Contact> data = book.readContact();
		book.add("Mani", "Pragada","23","Narendrapuram","AndhraPradesh","533294","91660397552","King","Friends");
		boolean result = book.checkContactInSyncWithDB("Arun");
		Assert.assertTrue(result);
	}
	
	@Test
	public void givenNewContact_WhenAdded_shouldMatch1() {
		AddressBook book = new AddressBook();
		List<Contact> data = book.readContact();
		book.add("Lavanya", "Ti","23","Rajahmundry","AndhraPradesh","530104","99637181178","ss","family");
		boolean result = book.checkContactInSyncWithDB("sita");
		Assert.assertTrue(result);
	}


}