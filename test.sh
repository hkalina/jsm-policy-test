
cd agent
mvn install wildfly:deploy

cd ../manager
if [ -d target/site/css ]; then
    mvn site
fi
mvn surefire-report:report

cd ..
xdg-open manager/target/site/surefire-report.html

