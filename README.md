Automatic test
--------------
To start test run:
```
./test.sh
```
Output will be shown in web browser.

Manual test
-----------
For clear test first prepare agent on the server:
```
cd agent
mvn install wildfly:deploy
```
And now you can run test:
```
cd manager
mvn test
```
