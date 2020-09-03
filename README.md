# E-learning

E-learning RESTful API based on Java, SpringBoot, Spring security, JWT, hibernate with MySQL.

# Overview

- This is a big project with a lot of details involved in it, First the users are able to register and login, then they can get all the "Published" courses or get a specific course with its id without showing the lessons, assignments to them because they are not enrolled in this course, they also can not see the enrollment requests to this course because they are not instructors in this course.

- The users can add course but the courses are not gonna be published until they add at least one lesson and one assignment.

# REST API Endpoints

Open Postman 'https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=ar'

	http://localhost:8080
		
		/auth
			POST /register - Register - required: firstname, lastname, email, username, password, phone nubmer
			POST /login - Login - required: username, password
		/edit
			PUT /user - Edit user infro required: firstname, lastname, email, username, password, phone number
		/course
			POST /add-course - Add the course info - required: title, description
			POST /add-course-lesson/{course id} - Add the lessons to this course - required: reading
			PSOT /add-course-assignment/{course id} -- Add the assignments to this course - required: question, answer


# How to use 

Open Postman, make a POST request 'http://localhost:8080/auth/register' and type in the body in a JSON format an firstname, lastname, email, username, password, phone nubmer then send the request and it's registerd.
Then make another POST request 'http://lovalhost:8080/auth/login' and type in the body in a JSON format a username, password and send the request. It generate a token and sends it back to you -because of using JWT authorization-.
And after that all you requests must be authorized, to make that you have to add a header in the headers tab in postman the 'key' = 'Authorization', 'value' = 'Bearer <The generated token you got after logging in>.
Follow the same way for each request.

# Hints 

- All inputs and outputs user JSON format.
