package org.example.projectfinal;

import javafx.scene.control.Label;

public class HelloController {

    private Label welcomeText;

    public HelloController(Label welcomeText) {
        this.welcomeText = welcomeText;
        initialize();
    }

    public void initialize() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    protected void onHelloButtonClick() {
        welcomeText.setText("Hello, JavaFX!");
    }
}
