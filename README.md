# E-learning

E-learning RESTful API based on Java, SpringBoot, Spring security, JWT, hibernate, MySQL, Docker.

# Overview

- This is a big E-learning application with a lot of details involved. No security leaks. 

- First the users are able to register and login, then they can get all the "Published" courses or get a specific course/s with its id/name without showing the lessons/assignments to them because they are not enrolled in this course

- Users also can not see the enrollment requests to courses because they are not instructors in this course.

- The users can add course but the courses are not gonna be published until they publish the course themselves.

- Instructors only can edit or delete their courses/lessons/assignments or even add new lessons/assignemnts after the course got published.

- Users are able to send enrollment request for a specific course and wait till one of the instructors of this course accept or reject him, if he got accepted he is enrolled in this course now, then he can reach all the course materials, but if he got rejected he can send another enrollment request, otherwise(accepted, pending) he can not send enrollment request.

- Only enrolled users can add a review(add feedback, add rank -must be out of 5-) for a course, and they are also allowed to edit the review or delete it. But instructors for this course can not add reviews.

- Enrolled users can submit there answers, and the grading system will grade the answers. Instructors can not submit answers.

- If the user submitted more than one answer of the same question only the last submission will be considered.

# Database Design

![database design](https://github.com/KickItLikeShika/E-learning/blob/master/databasedesign.jpg)


# REST API Endpoints

Open Postman 'https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop'

	http://localhost:8080
		
		/auth
			POST /register - Register - required: firstname, lastname, email, username, password, phone nubmer
			POST /login - Login - required: username, password
		/edit
			PUT /user - Update user infro required: firstname, lastname, email, username, password, phone number
		/course
			POST /add-course - Add the course info - required: title, description
			POST /add-course-lesson/{course id} - Add the lessons to this course - required: reading
			PSOT /add-course-assignment/{course id} - Add the assignments to this course - required: question, answer
			PUT /publish/{course id} - Publish the course if hidden
			PUT /hide/{course id} - Hide the course if published
			GET /get-all-courses -- Get back all the published courses
			GET /get-course-id/{course id} -- Get back the course with this id
			GET /get-course-name/{courses name} -- Get back all the courses with this name
			PUT /edit-course-info/{course id} - Update the course info - required: title description
			PUT / edit-course-lesson/{lesson id} - Update lesson - required: reading
			PUT /edit-course assignment/{assignment id} - Update assignment - required: question, correct asnwer
			DELETE /delete-course/{course id} - Delete a course
			DELETE /delete-course-lesson/{lesson id} - Delete a course lesson
			DELETE /delete-course-assignment/{assignment id} - Delete a course assignment 
		/enroll
			POST /send-enrollment-request/{course id} - Send enrollment request to the instructor of this course
			DELETE /cancel-enrollment-request/{enrollment request id} - Cancel the enrollment request to you have sent to this course
			POST /accept-enrollment-request/{enrollment request id} - Accept an enrollment request to a course (Instructors only)
			POST /reject-enrollment-request/{enrollment request id} - Reject an enrollment request to a course (Instructors only)
		/review
			POST /add-review/{course id} - Add a review for a course - required: feedback, rank out of 5
			PUT /edit-review/{review id} - Edit an existing review - required: feedback, rank out of 5
			DELETE /delete-review/{review id} - Delete and existing review
		/answer
			POST /submit-answer/{assignment id} - Submit an answer - required: answer

# How to use 

Open Postman, make a POST request 'http://localhost:8080/auth/register' and type in the body in a JSON format an firstname, lastname, email, username, password, phone nubmer then send the request and it's registerd.
Then make another POST request 'http://lovalhost:8080/auth/login' and type in the body in a JSON format a username, password and send the request. It generate a token and sends it back to you -because of using JWT authorization-.
And after that all you requests must be authorized, to make that you have to add a header in the headers tab in postman the 'key' = 'Authorization', 'value' = 'Bearer ^The generated token you got after logging in^'.
Follow the same way for each request.

# Hints 

- All inputs and outputs using JSON format.

- If you want to show the JSON format in postman for (lesson, assignment, etc..) remove `@JsonIgnore` annotation from the model classes.

- The questions must be `True or Flase` question or a math question. The grading system is not ready yet to receive another type of question.
