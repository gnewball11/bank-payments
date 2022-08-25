# Bank Assignment

### Technical Requirements

* Minimum Docker version 20.10.17, build 100c701
* Minimum docker-compose version 1.25.0, build unknown


### How to run it?
After installing Docker and Docker Compose you can run this line standing on the root of the payment folder

* `docker-compose build`
* `docker-compose up -d`

Once all the containers are up and running you can check the swagger documentation [here](http://localhost:8080/swagger-ui/)

Attached to the email where you will find the repo where the source code is hosted you will find a Postman 

### Some assumptions:

* As Credit Card and Debit Card payments are accepted I decided to model the Account having a credit or a debit card or both, and control the case where a payment method is not supported because the account doesn't have a card that supports the type of payment.