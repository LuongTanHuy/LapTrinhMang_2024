module laptrinhmang.ltm_chat {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires org.kordamp.ikonli.fontawesome5;
    requires com.google.gson;
    requires java.desktop;
    requires javafx.media;

    opens laptrinhmang.ltm_chat to javafx.fxml;
    exports laptrinhmang.ltm_chat;
    exports laptrinhmang.ltm_chat.controller;
    opens laptrinhmang.ltm_chat.controller to javafx.fxml;
    opens laptrinhmang.ltm_chat.model to javafx.base;
    opens laptrinhmang.ltm_chat.ModelChat to javafx.base;
}