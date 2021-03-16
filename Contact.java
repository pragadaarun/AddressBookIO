public class Contact {
	int id;
	String firstname;
	String lastname;
	String address;
	String city;
	String state;
	String zip;
	String phone;
	String email;

	public Contact(String firstname, String lastname, String address, String city, String state, String zip,
			String phone, String email) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phone = phone;
		this.email = email;
		System.out.println("Contact created");
	}
	public Contact(int id,String firstname, String lastname, String address, String city, String state, String zip,
			String phone, String email) {
		this(firstname,lastname,address,city,state,zip,phone,email);
		this.id=id;
}
	

	public void print() {
		System.out.println("firstname: " + this.firstname);
		System.out.println("lastname: " + this.lastname);
		System.out.println("address: " + this.address);
		System.out.println("city: " + this.city);
		System.out.println("state: " + this.state);
		System.out.println("zip: " + this.zip);
		System.out.println("phone: " + this.phone);
		System.out.println("email " + this.email);

	}
	
	public String getFirstName()
	{
		return this.firstname;
	}
	
	public String getLastName()
	{
		return this.lastname;
	}
	public String getEmail()
	{
		return this.email;
	}
	public String getPhoneNo()
	{
		return this.phone;
	}
	public String getZip()
	{
		return this.zip;
	}
	public String getCity()
	{
		return this.city;
	}
	public String getState()
	{
		return this.state;
	}

	public boolean equals(Contact p) {
		if (this.lastname == p.lastname)
			return false;
		if (this.city == p.city)
			return false;
		if (this.state == p.state)
			return false;
		if (this.zip == p.zip)
			return false;
		if (this.address == p.address)
			return false;
		if (this.phone == p.phone)
			return false;
		if (this.email == p.email)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Contact : " + "firstname= " + firstname  + ", lastname= " + lastname + ", address="
				+ address + ", city=" + city  + ", state=" + state + ", zip=" + zip 
				+ ", number=" + phone  + ", email=" + email;
	}

}