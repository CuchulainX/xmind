Create OSGI Maven Environment
https://books.sonatype.com/mcookbook/reference/ch01s04.html

Use cmd no powershell
mvn org.ops4j:maven-pax-plugin:create-project -DgroupId=org.xmind.releng -DartifactId=org.xmind.cathy.releng -Dversion=1.0.0-WINTERMUTE

when built
cd org.xmind.cathy.releng
 mvn clean install
 
 this execute the PAX Runner envir
 mvn install pax:provision

from the Apache Felix command prompt
install http://mirror.dkd.de/apache/felix/org.apache.felix.webconsole-4.2.14.jar



 REM mvn pax:import-bundle -DgroudId=org.apache.felix -DartifactId=org.apache.felix.webconsole -Dversion=1.2.8
  mvn pax:import-bundle -DgroudId=org.apache.felix -DartifactId=org.apache.felix.webconsole -Dversion=4.2.12

