package project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.geometry.*;
import javafx.event.*;
import javafx.scene.shape.*;

public class App extends Application{
    private Label registerSystem = new Label("Registration System");
    
    
    public void start(Stage stage) {
        registerSystem.setFont(new Font(40));
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(registerSystem);
        borderPane.setCenter(registerSystem);
        HBox buttonBox = new HBox(10);
        Button course = new Button("View course");
        Button student = new Button("View students details");
        Button save = new Button("Save");
        buttonBox.getChildren().addAll(course,student,save);
        buttonBox.setAlignment(Pos.CENTER);
        borderPane.setBottom(buttonBox);
        BorderPane.setMargin(buttonBox, new Insets(12,12,70,12));
        Scene main = new Scene(borderPane);

        
        Pane panel = new Pane();
        Scene courses = new Scene(panel);
        ListView courseList = new ListView();
        ListView studentsList = new ListView();
        GridPane details = new GridPane();
        details.setHgap(10);
        details.setVgap(5);
        Label[] labels = {new Label("ID"),new Label("Name"),
                         new Label("Days"),new Label("Location"), new Label("Time"),
                         new Label("Status")};

        Button back = new Button("Back");
        Button pervious = new Button("< Pervious");
        Button next = new Button("Next >");
        Button search = new Button("Search");
        Button[] buttons = {back, pervious, next, search};
        HBox buttonHbox = new HBox(10);
        for(int i=0; i<labels.length; i++){
           details.addColumn(0, labels[i]);
           details.addColumn(1, new TextField());
           if(i <= 3)
            buttonHbox.getChildren().add(buttons[i]);
        }
        panel.getChildren().addAll(details,courseList,studentsList,buttonHbox);
        details.setLayoutX(320);
        details.setLayoutY(100);
        studentsList.setLayoutX(630);
        studentsList.setLayoutY(10);
        courseList.setLayoutX(10);
        courseList.setLayoutY(10);
        buttonHbox.setLayoutY(430);
        buttonHbox.setLayoutX(300);


        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 40));
        Scene studentDetails = new Scene(grid);
        Label[] labels2 = {new Label("Student ID"), new Label("Registered Courses"), 
                           new Label("Not Registered Courses")};
        TextField studentID = new TextField();
        ListView registered = new ListView();
        ComboBox notRegistered = new ComboBox();
        grid.addColumn(1,studentID,registered,notRegistered);
        for (int i = 0; i < labels2.length; i++) {
            grid.addColumn(0,labels2[i]);
        }
        Button register = new Button("Register");
        Button drop = new Button("Drop");
        Button back2 = new Button("Back");
        Button next2 = new Button("Next >");
        Button pervious2 = new Button("< Pervious");
        HBox buttonsbox = new HBox(10);
        buttonsbox.getChildren().addAll(back2,pervious2,next2,register,drop);
        grid.add(buttonsbox,1,6);


        course.setOnAction(e -> {
            stage.setScene(courses);
            stage.setWidth(900);
            stage.setHeight(500);
        });

        student.setOnAction(e -> {
            stage.setScene(studentDetails);
            stage.setWidth(650);
            stage.setHeight(650);
        });

        back.setOnAction(e -> {
            stage.setScene(main);
            stage.setWidth(700);
            stage.setHeight(600);
        });

        back2.setOnAction(e -> {
            stage.setScene(main);
            stage.setWidth(700);
            stage.setHeight(600);
        });

        stage.setScene(main);
        stage.setWidth(700);
        stage.setHeight(600);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
