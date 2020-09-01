# E-learning

E-learning RESTful API based on Java, SpringBoot, Spring security, JWT, hibernate with MySQL.

# REST API Endpoints

Open Postman 'https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=ar'

	http://localhost:8080
		
		/auth
			POST /register - Register - required: firstname, lastname, email, username, password, phone nubmer
			POST /login - Login - required: username, password


# How to use 

Open Postman, make a POST request 'http://localhost:8080/auth/register' and type in the body in a JSON format an firstname, lastname, email, username, password, phone nubmer then send the request and it's registerd.
Then make another POST request 'http://lovalhost:8080/auth/login' and type in the body in a JSON format a username, password and send the request. It generate a token and sends it back to you -because of using JWT authorization-.
And after that all you requests must be authorized, to make that you have to add a header in the headers tab in postman the 'key' = 'Authorization', 'value' = 'Bearer <The generated token you got after logging in>.
Follow the same way for each request.

# Hints 

- All inputs and outputs user JSON format.