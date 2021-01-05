
Java Enterprise Graduation Project 
===============================
Spring/JPA Enterprise application with authorization and access rights based on roles developed in Java with technologies: Maven, Spring MVC, Security, JPA(Hibernate), REST(Jackson), Java 8 Stream, H2 database.

Application represents a voting system without a frontend for deciding where to have lunch.

- 2 types of users: admin and regular users
- Admin can input a restaurant, its lunch menu of the day (2-5 items usually, just a dish name and price)
- Menu changes each day (admins do the updates)
- Users can vote on which restaurant they want to have lunch at
- Only one vote counted per user
- If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.
