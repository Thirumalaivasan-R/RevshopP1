package com.Revshop.revshop.model;


import jakarta.persistence.*;
import java.time.LocalDate;



	@Entity
	@Table(name = "seller")
	public class Seller {
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long sellerId;

	    @Column(name = "first_name", nullable = false)
	    private String firstName;

	    @Column(name = "last_name", nullable = false)
	    private String lastName;

	    @Column(name = "email", nullable = false, unique = true)
	    private String email;

	    @Column(name = "password", nullable = false)
	    private String password;

	    @Column(name = "phone_number", nullable = false, unique = true)
	    private String phoneNumber;

	    @Column(name = "company_name", nullable = false)
	    private String companyName;

	    @Column(name = "company_description", columnDefinition = "TEXT")
	    private String companyDescription;

	    @Column(name = "company_address", nullable = false)
	    private String companyAddress;

	    @Column(name = "state", nullable = false)
	    private String state;

	    @Column(name = "zipcode", nullable = false)
	    private String zipcode;

	    @Column(name = "registration_date")
	    private LocalDate registrationDate;
        private String image;
	    public Seller(Long sellerId, String firstName, String lastName, String email, String password,
				String phoneNumber, String companyName, String companyDescription, String companyAddress, String state,
				String zipcode, LocalDate registrationDate) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.password = password;
			this.phoneNumber = phoneNumber;
			this.companyName = companyName;
			this.companyDescription = companyDescription;
			this.companyAddress = companyAddress;
			this.state = state;
			this.zipcode = zipcode;
		}
	    
	    public Seller()
	    {
	    	
	    }

		public Long getSellerId() {
			return sellerId;
		}

		public void setSellerId(Long sellerId) {
			this.sellerId = sellerId;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		public String getCompanyName() {
			return companyName;
		}

		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}

		public String getCompanyDescription() {
			return companyDescription;
		}

		public void setCompanyDescription(String companyDescription) {
			this.companyDescription = companyDescription;
		}

		public String getCompanyAddress() {
			return companyAddress;
		}

		public void setCompanyAddress(String companyAddress) {
			this.companyAddress = companyAddress;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getZipcode() {
			return zipcode;
		}

		public void setZipcode(String zipcode) {
			this.zipcode = zipcode;
		}

		public LocalDate getRegistrationDate() {
			return registrationDate;
		}

		public void setRegistrationDate(LocalDate registrationDate) {
			this.registrationDate = registrationDate;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}
	    
	    
	   
}
