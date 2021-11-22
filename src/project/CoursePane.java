package project;

import javafx.collections.FXCollections;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.util.*;


public class CoursePane extends Pane {

    private ListView<Course> courseList = new ListView<>();
    private ListView<String> studentList = new ListView<>();
    private Label numberLabel = new Label("");
    private TextField courseID = new TextField();
    private TextField courseName = new TextField();
    private TextField courseDays = new TextField();
    private TextField courseLocation = new TextField();
    private TextField courseTime = new TextField();
    private TextField courseStatus = new TextField();

    CoursePane() {
        // putting the info from the ArrayList to the ListView and making it a single selection only.
        courseList.setItems(FXCollections.observableArrayList(App.coursesList));
        courseList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        

        Label[] labels = {new Label("ID"),new Label("Name"),
                         new Label("Days"),new Label("Location"), new Label("Time"),
                         new Label("Status")};

        TextField[] fields = {courseID,courseName,courseDays,courseLocation,courseTime,courseStatus};
        Button back = new Button("Back");
        Button previous = new Button("< previous");
        Button next = new Button("Next >");
        Button search = new Button("Search");
        Button[] buttons = {back, previous, next, search};
        HBox buttonsHBox = new HBox(10);

        // a grid for the labels and their TextFields
        GridPane details = new GridPane();
        details.setHgap(10);
        details.setVgap(5);

        // adding the Labels and their TextFields to the grid.
        for(int i=0; i<labels.length; i++){
           details.addColumn(0, labels[i]);
           details.addColumn(1, fields[i]);
           fields[i].setPrefWidth(270);
           if(i <= 3)
            buttonsHBox.getChildren().add(buttons[i]); // adding the buttons their button box.
        }

        // adding all the nodes to the master pane.
        this.getChildren().addAll(details,courseList,studentList,buttonsHBox,numberLabel);

        // putting the nodes in a specific coordinates in the master pane.
        studentList.setPrefHeight(350);
        details.setLayoutX(280);
        details.setLayoutY(100);
        studentList.setLayoutX(630);
        studentList.setLayoutY(58);
        courseList.setLayoutX(10);
        courseList.setLayoutY(10);
        buttonsHBox.setLayoutY(430);
        buttonsHBox.setLayoutX(300);
        numberLabel.setLayoutX(630);
        numberLabel.setLayoutY(38);

        // button Handlers for back, previous, next, search.
        back.setOnAction(e -> {
            App.setToMain(App.primaryStage,App.primaryScene,App.mainBorderPane);
            resetCourseMenu(); // to make the selection empty.
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
            // checking if there is a match with any of the courses.
            for (int i = 0; i < App.coursesList.size(); i++) {
                if(searched.equals(App.coursesList.get(i).getCourseID())) {
                    courseList.getSelectionModel().select(i);
                    break;
                }
                else if(i == App.coursesList.size() - 1) {
                    App.alert.setHeaderText("Course not found");
                    App.alert.setContentText("Try again with a different input or pick a course from the list");
                    App.alert.show();
                }
            }

        });
        
        // the ListView Listener that changes the Text of the TextFields depending on the current selection index. 
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

    // this method empties the student id ListView and every TextField in the course pane.
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
    
}


