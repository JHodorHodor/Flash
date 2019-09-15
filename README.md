
[If you want to open source code in IntelliJ, probably you should choose: Run -> Edit Configuration -> VM Options and type:
"--module-path /usr/lib/jvm/java-11-openjdk-amd64/javafx-sdk-11.0.2/lib/ --add-modules=javafx.controls,javafx.fxml,javafx.graphics,javafx.base --add-exports=javafx.base/com.sun.javafx.event=ALL-UNNAMED"]


INSTALLATION:

0. Prefered: java 11. You will also need postgreSQL.

1. Download javafx 11.0.2: 
https://gluonhq.com/products/javafx/ 

---Download flash.zip and unzip it

2. Open start.sh and change: [JAVAFX_PATH] for path to your javafx/lib files, for example:
 /usr/lib/jvm/java-11-openjdk-am64/javafx-sdk-11.0.2/lib/

4. In config.properties file change database name, user name and password.

5. psql < create.sql

6. ./start.sh


SHORT DESCRIPTION:

App can be used to practice vocabulary. You can practice a few languages, manage words. Also you have acces to various statistics.

HAVE FUN!
