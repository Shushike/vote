
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
Price is kept in cents.
_______________________________
###Curl examples
For windows use `Git Bash`

####Create a restaurant
>curl –v -i –X POST -d '{"name":"New Restaurant","description":"Delicious food satisfy any tastes","address":"Near by"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/votes/rest/admin/restaurants  --user admin@gmail.com:admin
####Get list of all restaurants sorted by name
>curl -s http://localhost:8080/votes/rest/admin/restaurants --user admin@gmail.com:admin
####Create a dish for restaurant {restraurantId}
>curl –v -i –X POST -d '{"name":"New dish","description":"Some letters","price":"1000"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/votes/rest/admin/restaurants/{restaurantId}/dishes  --user admin@gmail.com:admin
> 
>curl –v -i –X POST -d '{"name":"New dish","description":"Some letters","price":"1000"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/votes/rest/admin/restaurants/100003/dishes  --user admin@gmail.com:admin

####Get all dishes for restaurant {restraurantId} sorted by name
>curl –v http://localhost:8080/votes/rest/admin/restaurants/{restraurantId}/dishes  --user admin@gmail.com:admin
> 
>curl –v http://localhost:8080/votes/rest/admin/restaurants/100003/dishes  --user admin@gmail.com:admin
>
>curl –v http://localhost:8080/votes/rest/restaurants/100003/dishes  --user  user@yahoo.com:password

####Get dishes by name for restaurant {restraurantId} sorted by price
>curl –v http://localhost:8080/votes/rest/admin/restaurants/{restraurantId}/dishes?name=%  --user admin@gmail.com:admin
> 
 >curl –v http://localhost:8080/votes/rest/admin/restaurants/100003/dishes?name=bread  --user admin@gmail.com:admin

####Update dish with {dishId} for restaurant {restraurantId}
>curl –v -i –X PUT -d '{"name":" Seabass","description":" Fried slices of fresh seabass","price":"4200"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/votes/rest/admin/restaurants/{restraurantId}/dishes/{dishId}  --user admin@gmail.com:admin
>
>curl –v -i –X PUT -d '{"name":"Seabass","description":" Fried slices of fresh seabass","price":"4200"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/votes/rest/admin/restaurants/100003/dishes/100006  --user admin@gmail.com:admin

Windows cmd:
>curl -v -i -X PUT -d "{\"name\":\"Seabass\",\"description\":\" Fried slices of fresh seabass\",\"price\":\"4200\"}" -H Content-Type:application/json;charset=UTF-8 http://localhost:8080/votes/rest/admin/restaurants/100003/dishes/100006  --user admin@gmail.com:admin

####Delete the dish with {dishId} for restaurant {restraurantId}

>curl -v -i -X DELETE http://localhost:8080/votes/rest/admin/restaurants/{restraurantId}/dishes/{dishId} --user admin@gmail.com:admin
>
>curl -v -i -X DELETE http://localhost:8080/votes/rest/admin/restaurants/100003/dishes/100007 --user admin@gmail.com:admin


####Get the dish with {dishId} for restaurant {restraurantId}
>curl –v http://localhost:8080/votes/rest/admin/restaurants/{restraurantId}/dishes/{dishId}  --user admin@gmail.com:admin
> 
>curl –v http://localhost:8080/votes/rest/admin/restaurants/100003/dishes/100006  --user admin@gmail.com:admin

####Create a menu for restaurant {restraurantId}
>curl –v -i –X POST -d '{"date":"2021-02-05","description":"Grand opening"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/votes/rest/admin/restaurants/{restraurantId}/menus  --user admin@gmail.com:admin
>curl –v -i –X POST -d '{"date":"2021-02-05","description":"Grand opening"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/votes/rest/admin/restaurants/100003/menus  --user admin@gmail.com:admin
> 
>curl –v -i –X POST -d '{"date":"2021-02-05","description":"Grand opening", "dish":[{"id":"100005"}, {"id":"100006"}]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/votes/rest/admin/restaurants/100003/menus  --user admin@gmail.com:admin

####Update the menu with {menuId} for restaurant {restraurantId}
>If menu was voted and its date is changing ModifyForbiddenException is
throws
> 
> curl –v -i –X PUT -d '{"date":"2021-02-05","description":"Our grand opening"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/votes/rest/admin/restaurants/{restraurantId}/menus/{menuId}  --user admin@gmail.com:admin
> 
>curl –v -i –X PUT -d '{"date":"2021-02-05","description":"Our grand opening"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/votes/rest/admin/restaurants/100003/menus/100016  --user admin@gmail.com:admin

Windows cmd:
>curl -v -i -X PUT -d "{\"date\":\"2021-02-05\",\"description\":\"Our grand opening\", \"dish\":[{\"id\":\"100031\"}]}" -H Content-Type:application/json;charset=UTF-8 http://localhost:8080/votes/rest/admin/restaurants/100027/menus/100032  --user admin@gmail.com:admin

####Delete the menu with {menuId} for restaurant {restraurantId}
>curl -v -i -X DELETE http://localhost:8080/votes/rest/admin/restaurants/{restraurantId}/menus/{menuId} --user admin@gmail.com:admin
> 
>curl -v -i -X DELETE http://localhost:8080/votes/rest/admin/restaurants/100003/menus/100012 --user admin@gmail.com:admin

####Get list of menus for restaurant {restraurantId}
>curl –v http://localhost:8080/votes/rest/admin/restaurants/{restraurantId}/menus  --user admin@gmail.com:admin
> 
>curl –v http://localhost:8080/votes/rest/admin/restaurants/100003/menus  --user admin@gmail.com:admin

####Get dish list for menu {menuId}
>curl –v http://localhost:8080/votes/rest/restaurants/{restraurantId}/menus/{menuId}/dishes  --user admin@gmail.com:admin
> 
>curl –v http://localhost:8080/votes/rest/restaurants/100003/menus/100012/dishes  --user admin@gmail.com:admin
####Get menu for restaurant by date
>curl –v "http://localhost:8080/votes/rest/admin/restaurants/{restaurantId}/menus/by?menuDate=2020-11-04" --user admin@gmail.com:admin
>
>curl –v "http://localhost:8080/votes/rest/admin/restaurants/100003/menus/by?menuDate=2020-11-04" --user admin@gmail.com:admin

####Get list of menus by date
>curl –v "http://localhost:8080/votes/rest/menus/by?menuDate=2020-11-04" --user admin@gmail.com:admin

####Get complex information about restaurant {restraurantId}
>curl -v http://localhost:8080/votes/rest/admin/restaurants/{restraurantId}/complex --user admin@gmail.com:admin
> 
>curl -v http://localhost:8080/votes/rest/admin/restaurants/100003/complex --user admin@gmail.com:admin

####Get votes number for restaurant {restraurantId} by date
>curl –v "http://localhost:8080/votes/rest/restaurants/{restraurantId}/votes-number?voteDate=2020-11-04"  --user admin@gmail.com:admin
>
>curl –v "http://localhost:8080/votes/rest/restaurants/100003/votes-number?voteDate=2020-11-04"  --user admin@gmail.com:admin

####Get votes number for all restaurants by date
>In restaurant names order
> 
>curl –v "http://localhost:8080/votes/rest/restaurants/votes-number?voteDate=2020-11-04" --user admin@gmail.com:admin


####Get votes for restaurants for period
>In date descending order and ascending restaurant name
>
>curl –v "http://localhost:8080/votes/rest/restaurants/votes-number?startDate=2020-11-04" --user admin@gmail.com:admin
>
>curl –v "http://localhost:8080/votes/rest/restaurants/votes-number?startDate=2020-11-04&endDate=2020-11-06" --user admin@gmail.com:admin

####Get list of restaurants with menu for period
>curl –v "http://localhost:8080/votes/rest/restaurants/filter?startDate=2020-11-04&endDate=2020-11-06" --user admin@gmail.com:admin


####Create an administrator
>curl –v -i –X POST -d '{"name":"New","email":"new@gmail.com", "roles":["ADMIN"],"password":"newPass"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/votes/rest/admin/users/ --user admin@gmail.com:admin
####Register user
>curl -v -i -X POST -d '{"name":"New User","email":"test@mail.ru","password":"test-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/votes/rest/profile/register
####Get profile
>curl –v http://localhost:8080/votes/rest/profile --user user@yahoo.com:password
####Get user {userId}
>curl -v http://localhost:8080/votes/rest/admin/users/{userId} --user admin@gmail.com:admin
>
>curl -v http://localhost:8080/votes/rest/admin/users/100001 --user admin@gmail.com:admin
####Get all users
>curl -v http://localhost:8080/votes/rest/admin/users --user admin@gmail.com:admin

####Get menus with dishes and vote flag by date
>In order of restaurant names
> 
>curl –v "http://localhost:8080/votes/rest/profile/menus/by?menuDate=2020-11-04" --user user@yahoo.com:password

####Get menus with dishes and vote flag for period 
>In descending order by menu date and ascending by restaurant name
> 
>curl –v "http://localhost:8080/votes/rest/profile/menus/by?startDate=2020-11-04&endDate=2020-11-20" --user user@yahoo.com:password

####Get voted menus for period in descending order by menu date
>curl –v "http://localhost:8080/votes/rest/profile/menus/filter?startDate=2020-11-04&endDate=2020-11-06" --user user@yandex.ru:password

####Vote for menu {menuId}
>If other menu was already voted that vote is removed and new one for {menuId} is created
>
>curl –v -i –X PUT "http://localhost:8080/votes/rest/profile/menus/{menuId}" --user user@yandex.ru:password
>
>curl –v -i –X PUT "http://localhost:8080/votes/rest/profile/menus/100016" --user user@yandex.ru:password

>Vote can’t be set for menu in the past or after 11:00 on menu date
>
>curl –v -i –X PUT "http://localhost:8080/votes/rest/profile/menus/100012" --user user@yandex.ru:password

####Delete vote {id}
>curl –v -i –X DELETE http://localhost:8080/votes/rest/profile/votes/{id} --user user@yandex.ru:password
>
>curl –v -i –X DELETE http://localhost:8080/votes/rest/profile/votes/100018 --user user@yandex.ru:password

####Get list of all restaurants sorted by name
>curl -s http://localhost:8080/votes/rest/restaurants --user user@yandex.ru:password
