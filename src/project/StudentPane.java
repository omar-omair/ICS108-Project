package project;

import javafx.collections.FXCollections;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.util.*;

public class StudentPane extends GridPane {
    
    private ArrayList<Course> notRegisteredCourseList = new ArrayList<>();
    private ArrayList<Course> registeredCourseList = new ArrayList<>();
    private ListView<Course> registeredCourses = new ListView<>();
    private TextField studentID = new TextField();
    private ComboBox<Course> nonRegisteredCourses = new ComboBox<>();
    int studentCount = 0; // the index of the current student.

    StudentPane() {

        this.setVgap(15);
        this.setHgap(10);
        this.setPadding(new Insets(10, 10, 10, 40));

        Label[] labels = {new Label("Student ID"), new Label("Registered Courses"), 
                           new Label("Not Registered Courses")};

        nonRegisteredCourses.setPrefWidth(410);

        this.addColumn(1,studentID,registeredCourses,nonRegisteredCourses);

        for (int i = 0; i < labels.length; i++) {
            this.addColumn(0,labels[i]);
        }

        Button register = new Button("Register");
        Button drop = new Button("Drop");
        Button back = new Button("Back");
        Button next = new Button("Next >");
        Button previous = new Button("< previous");
        Button search = new Button("Search");

        HBox buttonsHBox = new HBox(10);
        buttonsHBox.getChildren().addAll(back,previous,next,register,drop,search);
        this.add(buttonsHBox,1,6);

        // button handlers for back, previous, next, search, register, and drop.
        back.setOnMouseClicked(e -> {
            App.setToMain(App.primaryStage,App.primaryScene,App.mainBorderPane);
        });

        next.setOnMouseClicked(e -> {
            if(studentCount < App.studentsList.size() - 1) {
                studentCount++;
            }
            getStudentDetails();
        });
        previous.setOnMouseClicked(e -> {
            if(studentCount > 0) {
                studentCount--;
            }
            getStudentDetails();
        });

        search.setOnMouseClicked(e -> {
            String searched = studentID.getText();
             // checking if there is a match with any of the student ids.
            for (int i = 0; i < App.studentsList.size(); i++) {
                if(searched.equals(App.studentsList.get(i).getStudID())) {
                    studentCount = i;
                    getStudentDetails();
                    break;
                }
                else if(i == App.studentsList.size() - 1) {
                    App.alert.setHeaderText("Student not found");
                    App.alert.setContentText("Try again with a different input");
                    App.alert.show();
                }
            }
        });

        register.setOnMouseClicked(e -> {
            Course registeredCourse = nonRegisteredCourses.getSelectionModel().getSelectedItem();
            if(registeredCourse.getAvailableSeats() > 0) {
                registeredCourse.setAvailableSeats(registeredCourse.getAvailableSeats() - 1);
                notRegisteredCourseList.remove(registeredCourse);
                App.studentsList.get(studentCount).getCourses().add(registeredCourse);
                getStudentDetails();
            }
            else {
                App.alert.setHeaderText("Closed Course");
                App.alert.setContentText("Try a different course");
                App.alert.show();
            }
        });

        drop.setOnMouseClicked(e -> {
            Course droppedCourse = registeredCourses.getSelectionModel().getSelectedItem();
            App.studentsList.get(studentCount).getCourses().remove(droppedCourse);
            droppedCourse.setAvailableSeats(droppedCourse.getAvailableSeats() + 1);
            getStudentDetails();

        });

    }

    // this method gets the info from the ArrayList and puts them into the right place.
    void getStudentDetails() {
        notRegisteredCourseList.clear(); // refreshing the unregistered list.
        studentID.setText(App.studentsList.get(studentCount).getStudID());
        registeredCourseList = App.studentsList.get(studentCount).getCourses();
        // putting every non-registered course into the comboBox.
        for(int i = 0; i < App.coursesList.size(); i++){
            if(!(registeredCourseList.contains(App.coursesList.get(i)))){
                notRegisteredCourseList.add(App.coursesList.get(i));
            }
        }
        nonRegisteredCourses.setItems(FXCollections.observableArrayList(notRegisteredCourseList));
        registeredCourses.setItems(FXCollections.observableArrayList(registeredCourseList));
    }
}
