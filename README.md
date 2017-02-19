# Login Solution
#### This is a simple solution backed by a minimal user interface and a security service that utilizes OAuth2 to simplificate login flow. 

*It's very easy to run the application*

1. Generate the necessary *jar* files and *docker images*
``` 
./mvnw clean package 
``` 

2. Start the *containers*
``` 
 docker-compose up 
``` 

3. Access ``http://localhost:8080``, type *test* and *12345* to see profiles page

4. Access ``http://localhost:8080/{username}/profiles`` to get the user profiles list