cd agent
mvn install wildfly:deploy
cd ../manager
mvn site surefire-report:report
cd ..
xdg-open manager/target/site/surefire-report.html

