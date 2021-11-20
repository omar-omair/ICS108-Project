package project;

import javafx.application.Application;
import javafx.collections.FXCollections;
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
import java.util.*;
import java.io.FileInputStream;
import java.io.*;

public class App extends Application{
    private Label registerSystem = new Label("Registration System");
    TextField courseID = new TextField();
    TextField courseName = new TextField();
    TextField courseDays = new TextField();
    TextField courseLocation = new TextField();
    TextField courseTime = new TextField();
    TextField courseStatus = new TextField();
    ArrayList<Course> coursesList = new ArrayList<Course>();
    ArrayList<Student> studentsList = new ArrayList<Student>();
    ListView<String> studentList = new ListView<>();
    Pane panel = new Pane();
    Label numberLabel = new Label("");

    public void start(Stage stage) throws Exception{
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
        
        
        
        FileInputStream fis = new FileInputStream("res\\Registration.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);
        coursesList = (ArrayList<Course>) ois.readObject();
        studentsList = (ArrayList<Student>) ois.readObject();
        Scene courses = new Scene(panel);
        ListView<Course> courseList = new ListView<>(FXCollections.observableArrayList(coursesList));
        courseList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        GridPane details = new GridPane();
        details.setHgap(10);
        details.setVgap(5);
        Label[] labels = {new Label("ID"),new Label("Name"),
                         new Label("Days"),new Label("Location"), new Label("Time"),
                         new Label("Status")};
        TextField[] fields = {courseID,courseName,courseDays,courseLocation,courseTime,courseStatus};
        Button back = new Button("Back");
        Button pervious = new Button("< Pervious");
        Button next = new Button("Next >");
        Button search = new Button("Search");
        Button[] buttons = {back, pervious, next, search};
        HBox buttonHbox = new HBox(10);
        for(int i=0; i<labels.length; i++){
           details.addColumn(0, labels[i]);
           details.addColumn(1, fields[i]);
           fields[i].setPrefWidth(270);
           if(i <= 3)
            buttonHbox.getChildren().add(buttons[i]);
        }
        
        panel.getChildren().addAll(details,courseList,studentList,buttonHbox,numberLabel);
        studentList.setPrefHeight(350);
        details.setLayoutX(280);
        details.setLayoutY(100);
        studentList.setLayoutX(630);
        studentList.setLayoutY(58);
        courseList.setLayoutX(10);
        courseList.setLayoutY(10);
        buttonHbox.setLayoutY(430);
        buttonHbox.setLayoutX(300);
        numberLabel.setLayoutX(630);
        numberLabel.setLayoutY(38);

        courseList.getSelectionModel().selectedItemProperty().addListener(t -> {
            int courseIndex = courseList.getSelectionModel().getSelectedIndex();
            courseID.setText(coursesList.get(courseIndex).getCourseID());
            courseName.setText(coursesList.get(courseIndex).getCourseName());
            courseDays.setText(coursesList.get(courseIndex).getCourseDays());
            courseTime.setText(coursesList.get(courseIndex).getCourseTime());
            courseLocation.setText(coursesList.get(courseIndex).getCourseLocation());
            if(coursesList.get(courseIndex).getAvailableSeats() == 0) {
                courseStatus.setText("Closed");
            }
            else {
                courseStatus.setText("Open");
            }

            ArrayList<String> registered = new ArrayList<String>();
            int count = 0;
            for(int i = 0; i< studentsList.size(); i++) {
                for(int j =0; j < studentsList.get(i).getCourses().size(); j++) {
                    if(studentsList.get(i).getCourses().get(j).getCourseID().equals(courseID.getText())) {
                        registered.add(count, studentsList.get(i).getStudID());
                        count++;
                    }
                }
                
            }
            numberLabel.setText("There are " + (count+1) + " students registered in " + courseID.getText());
            studentList.setItems(FXCollections.observableArrayList(registered));
        });



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
            stage.setTitle("Courses");
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
            stage.setTitle("main");
            ArrayList<String> empty = new ArrayList<String>();
            courseList.getSelectionModel().clearSelection();
            courseID.setText("");
            courseName.setText("");
            courseDays.setText("");
            courseTime.setText("");
            courseStatus.setText("");
            courseLocation.setText("");
            numberLabel.setText("");
            studentList.setItems(FXCollections.observableArrayList(empty));
        });

        back2.setOnAction(e -> {
            stage.setScene(main);
            stage.setWidth(700);
            stage.setHeight(600);
            stage.setTitle("main");
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
