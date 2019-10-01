#!/bin/bash
java --module-path  $JAVA_HOME/javafx-sdk-11.0.2/lib/ --add-modules=javafx.controls,javafx.fxml --add-exports=javafx.base/com.sun.javafx.event=ALL-UNNAMED -jar Flash.jar
