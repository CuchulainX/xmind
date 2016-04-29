Create OSGI Maven Environment
https://books.sonatype.com/mcookbook/reference/ch01s04.html

This created the org.xmind.cathy.relend folder
it is not part of original build
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

*** I'm getting problems with the Apache Felix plugins
they are not creating the web.console correctly

All the osgi problems and such prompted me to go down the Gradle root
 - ive spent just too much time on Maven
 - the build file is massive
 
