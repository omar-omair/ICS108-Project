package project;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.geometry.*;
import java.util.*;
import java.io.FileInputStream;
import java.io.*;

public class CoursePane extends Pane {
    ListView<Course> courseList = new ListView<>();
    ListView<String> studentList = new ListView<>();
    Alert alert = new Alert(Alert.AlertType.ERROR);
    Label numberLabel = new Label("");
    TextField courseID = new TextField();
    TextField courseName = new TextField();
    TextField courseDays = new TextField();
    TextField courseLocation = new TextField();
    TextField courseTime = new TextField();
    TextField courseStatus = new TextField();
    TextField studentID = new TextField();

    CoursePane() {

        courseList.setItems(FXCollections.observableArrayList(App.coursesList));
        courseList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        GridPane details = new GridPane();
        details.setHgap(10);
        details.setVgap(5);
        Label[] labels = {new Label("ID"),new Label("Name"),
                         new Label("Days"),new Label("Location"), new Label("Time"),
                         new Label("Status")};
        TextField[] fields = {courseID,courseName,courseDays,courseLocation,courseTime,courseStatus};
        Button back = new Button("Back");
        Button previous = new Button("< previous");
        Button next = new Button("Next >");
        Button search = new Button("Search");
        Button[] buttons = {back, previous, next, search};
        HBox buttonHbox = new HBox(10);

        for(int i=0; i<labels.length; i++){
           details.addColumn(0, labels[i]);
           details.addColumn(1, fields[i]);
           fields[i].setPrefWidth(270);
           if(i <= 3)
            buttonHbox.getChildren().add(buttons[i]);
        }
        
        this.getChildren().addAll(details,courseList,studentList,buttonHbox,numberLabel);

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

        back.setOnAction(e -> {
            setToMain(App.primaryStage,App.PrimaryScene,App.mainBorderPane);
            resetCourseMenu();
        });

        previous.setOnMouseClicked(e -> {
            if(courseList.getSelectionModel().getSelectedIndex() > 0) {
                courseList.getSelectionModel().select(courseList.getSelectionModel().getSelectedIndex() - 1);
            }
        });

        next.setOnMouseClicked(e -> {
            if(courseList.getSelectionModel().getSelectedIndex() < App.coursesList.size() - 1) {
                courseList.getSelectionModel().select(courseList.getSelectionModel().getSelectedIndex() + 1);
            }
        });

        search.setOnMouseClicked(e -> {
            String searched = courseID.getText();
            for (int i = 0; i < App.coursesList.size(); i++) {
                if(searched.equals(App.coursesList.get(i).getCourseID())) {
                    courseList.getSelectionModel().select(i);
                    break;
                }
                else if(i == App.coursesList.size() - 1) {
                    alert.setHeaderText("Course not found");
                    alert.setContentText("Try again with a different input or pick a course from the list");
                    alert.show();
                }
            }

        });
        
    }

    void resetCourseMenu() {
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
    }

    void setToMain(Stage stage, Scene scene, BorderPane pane ) {
        scene.setRoot(pane);
        stage.setWidth(700);
        stage.setHeight(600);
        stage.setTitle("main");
        
    }
    
    void getInfo(){
        courseList.getSelectionModel().selectedItemProperty().addListener(t -> {

            int courseIndex = courseList.getSelectionModel().getSelectedIndex();
            courseID.setText(App.coursesList.get(courseIndex).getCourseID());
            courseName.setText(App.coursesList.get(courseIndex).getCourseName());
            courseDays.setText(App.coursesList.get(courseIndex).getCourseDays());
            courseTime.setText(App.coursesList.get(courseIndex).getCourseTime());
            courseLocation.setText(App.coursesList.get(courseIndex).getCourseLocation());

            if(App.coursesList.get(courseIndex).getAvailableSeats() == 0) {
                courseStatus.setText("Closed");
            }
            else {
                courseStatus.setText("Open");
            }

            ArrayList<String> registered = new ArrayList<>();
            int count = 0;
            for(int i = 0; i< App.studentsList.size(); i++) {
                for(int j =0; j < App.studentsList.get(i).getCourses().size(); j++) {
                    if(App.studentsList.get(i).getCourses().get(j).getCourseID().equals(courseID.getText())) {
                        registered.add(count, App.studentsList.get(i).getStudID());
                        count++;
                    }
                }
                
            }

            numberLabel.setText("There are " + (count+1) + " students registered in " + courseID.getText());
            studentList.setItems(FXCollections.observableArrayList(registered));
        });
    }
}


