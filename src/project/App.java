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
    TextField studentID = new TextField();
    ArrayList<Course> coursesList = new ArrayList<Course>();
    ArrayList<Student> studentsList = new ArrayList<Student>();
    ArrayList<Course> notRegisteredCourseList = new ArrayList<>();
    ArrayList<Course> registeredCourseList = new ArrayList<>();
    ListView<Course> registered = new ListView<>();
    ListView<Course> courseList = new ListView<>();
    ComboBox<Course> notRegistered = new ComboBox<>();
    ListView<String> studentList = new ListView<>();
    Pane panel = new Pane();
    Label numberLabel = new Label("");
    int studentCount = 0;
    Alert alert = new Alert(Alert.AlertType.ERROR);
    

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
        courseList.setItems(FXCollections.observableArrayList(coursesList));
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
        notRegistered.setPrefWidth(410);
        grid.addColumn(1,studentID,registered,notRegistered);
        for (int i = 0; i < labels2.length; i++) {
            grid.addColumn(0,labels2[i]);
        }
        Button register = new Button("Register");
        Button drop = new Button("Drop");
        Button back2 = new Button("Back");
        Button next2 = new Button("Next >");
        Button pervious2 = new Button("< Pervious");
        Button search2 = new Button("Search");
        HBox buttonsbox = new HBox(10);
        buttonsbox.getChildren().addAll(back2,pervious2,next2,register,drop,search2);
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
            studentCount = 0;
            getStudentDetails();
        });

        save.setOnMouseClicked(e -> {
            try(FileOutputStream fos = new FileOutputStream("res\\Registration.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(coursesList);
                oos.writeObject(studentsList);
            }
            catch(IOException ex) {
                System.out.println(ex);
            }
        });

        back.setOnAction(e -> {
            setToMain(stage, main);
            resetCourseMenu();
        });

        pervious.setOnMouseClicked(e -> {
            if(courseList.getSelectionModel().getSelectedIndex() > 0) {
                courseList.getSelectionModel().select(courseList.getSelectionModel().getSelectedIndex() - 1);
            }
        });

        next.setOnMouseClicked(e -> {
            if(courseList.getSelectionModel().getSelectedIndex() < coursesList.size() - 1) {
                courseList.getSelectionModel().select(courseList.getSelectionModel().getSelectedIndex() + 1);
            }
        });

        search.setOnMouseClicked(e -> {
            String searched = courseID.getText();
            for (int i = 0; i < coursesList.size(); i++) {
                if(searched.equals(coursesList.get(i).getCourseID())) {
                    courseList.getSelectionModel().select(i);
                    break;
                }
                else if(i == coursesList.size() - 1) {
                    alert.setHeaderText("Course not found");
                    alert.setContentText("Try again with a different input or pick a course from the list");
                    alert.show();
                }
            }

        });


        back2.setOnMouseClicked(e -> {
            setToMain(stage,main);
        });

        next2.setOnMouseClicked(e -> {
            if(studentCount < studentsList.size() - 1) {
                studentCount++;
            }
            getStudentDetails();
        });
        pervious2.setOnMouseClicked(e -> {
            if(studentCount > 0) {
                studentCount--;
            }
            getStudentDetails();
        });

        search2.setOnMouseClicked(e -> {
            String searched = studentID.getText();
            for (int i = 0; i < studentsList.size(); i++) {
                if(searched.equals(studentsList.get(i).getStudID())) {
                    studentCount = i;
                    getStudentDetails();
                    break;
                }
                else if(i == studentsList.size() - 1) {
                    alert.setHeaderText("Student not found");
                    alert.setContentText("Try again with a different input");
                    alert.show();
                }
            }
        });

        register.setOnMouseClicked(e -> {
            Course registeredCourse = notRegistered.getSelectionModel().getSelectedItem();
            if(registeredCourse.getAvailableSeats() > 0) {
                registeredCourse.setAvailableSeats(registeredCourse.getAvailableSeats() - 1);
                notRegisteredCourseList.remove(registeredCourse);
                studentsList.get(studentCount).getCourses().add(registeredCourse);
                getStudentDetails();
            }
            else {
                alert.setHeaderText("Closed Course");
                alert.setContentText("Try a different course");
                alert.show();
            }
        });

        drop.setOnMouseClicked(e -> {
            Course droppedCourse = registered.getSelectionModel().getSelectedItem();
            studentsList.get(studentCount).getCourses().remove(droppedCourse);
            droppedCourse.setAvailableSeats(droppedCourse.getAvailableSeats() + 1);
            getStudentDetails();

        });

        stage.setScene(main);
        stage.setWidth(700);
        stage.setHeight(600);
        stage.show();
    }

    void getStudentDetails() {
        notRegisteredCourseList.clear();
        studentID.setText(studentsList.get(studentCount).getStudID());
        registeredCourseList = studentsList.get(studentCount).getCourses();
        for(int i = 0; i < coursesList.size(); i++){
            if(!(registeredCourseList.contains(coursesList.get(i)))){
                notRegisteredCourseList.add(coursesList.get(i));
            }
        }
        notRegistered.setItems(FXCollections.observableArrayList(notRegisteredCourseList));
        registered.setItems(FXCollections.observableArrayList(registeredCourseList));
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

    void setToMain(Stage stage, Scene main) {
        stage.setScene(main);
        stage.setWidth(700);
        stage.setHeight(600);
        stage.setTitle("main");
    }

    public static void main(String[] args) {
        launch();
    }
}
