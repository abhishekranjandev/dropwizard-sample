# dropwizard-sample

A Sample Phonebook Application Using Drop Wizard.

Dropwizard is an open source Java framework for the rapid development of RESTful Web
Services putting together everything you'll need. You can have a production-ready application,
making use of Jetty, Jersey, Jackson, JDBI, and Hibernate, as well as a large number of
additional libraries that Dropwizard includes, either in its core or as modules.

Before we start creating Dropwizard applications, we need to set up our development
environment, which will consist of, at least, Java (JDK 8), Maven, and MySQL.

Downloading and installing Maven

1.	 Maven installation is pretty straightforward. Just download Maven binaries from
http://maven.apache.org/download.cgi and extract the contents of the
package in a directory of your choice.

2.	 Modify the PATH environment variable, adding the Maven directory suffixed with \
bin , like C:\apache-maven-3.3.9\bin , so the mvn executable will be available
on all directories when using the command line or the terminal.

Downloading and installing MySQL

1.	 Download the MySQL Community Server installer for your operating system
from http://dev.mysql.com/downloads/mysql/#downloads .
2.	 Run the installer and select to install MySQL. Keep the proposed,
default installation settings.


Build the application by executing the following command in your terminal inside the
dw-sample directory:

$ mvn package

The output of this command will contain the [INFO] BUILD SUCCESS line, indicating that
the project was successfully built

Run the application and specify the configuration file as well:
$ java -jar target/dw-sample-0.0.1-SNAPSHOT.jar server config.yaml