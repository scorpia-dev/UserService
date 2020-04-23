# UserService

## About App

This is a "User" RESTful Web Service with Spring/Spring Boot/Hibernate H2 in memory database

The Data objects are defined as followed:
```
User:
    id: <int>
    lastName: <string>
    firstName: <string>
    emails: List<Email>
    phoneNumbers: List<PhoneNumber>
```
```
Email:
    id: <int>
    mail: <string>
```    
```    
PhoneNumber:
    id: <int>
    number: <string>    
 ```


## You can

#### User
------

*Create a new User*
 
```
POST - http://localhost:8080/user
```
```JSON
{
    "firstName": "<first name here>",
    "lastName": "last name here"
}
```

* **Read an existing member by their id**
```
GET - http://localhost:8080/user/{userId} i.e. http://localhost:8080/user/1
 ```
 
 * **Read an existing member by their name**
```
GET - http://localhost:8080/user/{firstName}/{lastName} i.e. http://localhost:8080/user/firstname/lastname
 ```
* **Delete an existing member**
```
DELETE - http://localhost:8080/user/id/{userId} i.e. http://localhost:8080/user/id/1
```

#### Email
------
* **add additional email data**
```
PUT - http://localhost:8080/email/add/{userId} i.e. http://localhost:8080/email/add/1
```
```JSON
{
    "mail": "test@test.com"
}
```
* **update existing email data**
```
PUT - http://localhost:8080/email/update/{emailId} i.e. http://localhost:8080/email/update/1
```
```JSON
{
    "mail": "update@test.com"
}
```

#### PhoneNumber
------
* **add additional phone data**
```
PUT - http://localhost:8080/phonenumber/add/{userId} i.e. http://localhost:8080/phonenumber/add/1
```
```JSON
{
	"number": "12345678910"
}
```

* **update existing phone data**
```
PUT - http://localhost:8080/phonenumber/update/{phoneNumberId} i.e. http://localhost:8080/phonenumber/update/1
```
```JSON
{
	"number": "12345678910"
}
```


### Spec -
------
* Accepts JSON 
* Response in JSON 
* JDK8 or higher
* Build with Maven
* Data storage: (in memory) database
* Lombok has been used to reduce boilerplate code

### Running
Run as a Spring Boot App

### To do's
* add a phone number validation library to accept differnt country codes etc 
* create Swagger API document for the API



