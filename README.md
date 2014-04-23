code-shoot-out
==============

The point is to shoot it out with other languages.

First game, REST services
--------------
Description:
Create a set of REST services to view and manage a contact list.
Include the following end points:
  - List all contacts
  - View a contact
  - Add a contact
  - Update a contact
  - Remove/Delete a contact
  - Search Contacts
  
Each endpoint should conform to the following URI Specs
	GET contacts/listall
	GET contacts/entry/<contact id>
	PUT contacts/entry?... 
	POST contacts/entry/<contact id>?...
	DELETE contacts/entry/<contact id>
	
The attributes of a Contact are;
	First Name
	Last Name
	Organization
	Address (0..n)
	Phone (0..n)
	Email (0..n)
	Twitter (0..n)
	IM (0..n)
	
Address:
	Street1
	Street2
	City
	State
	PostalCode
	Country
	
Phone:
	digits
	type (home|work|mobile|other)
	
Email:
	address
	type (home|work|mobile|other)
	
Twitter:
	handle
	type (home|work|other)
	
IM:
	handle
	provider (AOL, Yahoo, Google, Microsoft, Linc, other)
	