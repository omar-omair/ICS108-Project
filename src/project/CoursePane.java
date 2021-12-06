package project;

import javafx.collections.FXCollections;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.util.*;


public class CoursePane extends Pane {

    private ListView<Course> courseListView = new ListView<>();
    private ListView<String> studentListView = new ListView<>();
    private Label numberLabel = new Label("");
    private TextField courseID = new TextField();
    private TextField courseName = new TextField();
    private TextField courseDays = new TextField();
    private TextField courseLocation = new TextField();
    private TextField courseTime = new TextField();
    private TextField courseStatus = new TextField();

    CoursePane() {
        // putting the info from the ArrayList to the ListView and making it a single selection only.
        courseListView.setItems(FXCollections.observableArrayList(App.coursesList));
        courseListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        

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
            buttonsHBox.getChildren().add(buttons[i]); // adding the buttons to their button box.
        }

        // adding all the nodes to the master pane.
        this.getChildren().addAll(details,courseListView,studentListView,buttonsHBox,numberLabel);

        // putting the nodes in a specific coordinates in the master pane.
        studentListView.setPrefHeight(350);
        details.setLayoutX(280);
        details.setLayoutY(100);
        studentListView.setLayoutX(630);
        studentListView.setLayoutY(58);
        courseListView.setLayoutX(10);
        courseListView.setLayoutY(10);
        buttonsHBox.setLayoutY(430);
        buttonsHBox.setLayoutX(300);
        numberLabel.setLayoutX(630);
        numberLabel.setLayoutY(38);

        // button Handlers for back, previous, next, search.
        back.setOnAction(e -> {
            App.setToMain(App.primaryStage,App.primaryScene,App.mainBorderPane);
            search.setText("Search");
            resetCourseMenu(); // to make the selection empty.
        });

        previous.setOnMouseClicked(e -> {
            if(courseListView.getSelectionModel().getSelectedIndex() > 0) {
                courseListView.getSelectionModel().select(courseListView.getSelectionModel().getSelectedIndex() - 1);
            }
        });

        next.setOnMouseClicked(e -> {
            if(courseListView.getSelectionModel().getSelectedIndex() < App.coursesList.size() - 1) {
                courseListView.getSelectionModel().select(courseListView.getSelectionModel().getSelectedIndex() + 1);
            }
        });

        search.setOnMouseClicked(e -> {
            if(search.getText() == "Search"){
                resetCourseMenu();
                search.setText("Go >>");
            }
            else if(search.getText() == "Go >>") {
                String searched = courseID.getText().toUpperCase();
                // checking if there is a match with any of the courses.
                for (int i = 0; i < App.coursesList.size(); i++) {
                    if(searched.equals(App.coursesList.get(i).getCourseID())) {
                        courseListView.getSelectionModel().select(i);
                        search.setText("Search");
                        break;
                    }
                    else if(i == App.coursesList.size() - 1) {
                        App.alert.setHeaderText("Course not found");
                        App.alert.setContentText("Try again with a different input or pick a course from the list");
                        App.alert.show();
                    }
                }
        }
        });
        
        // the ListView Listener that changes the Text of the TextFields depending on the current selection index. 
        courseListView.getSelectionModel().selectedItemProperty().addListener(t -> {

            int courseIndex = courseListView.getSelectionModel().getSelectedIndex();
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
            studentListView.setItems(FXCollections.observableArrayList(registered));
            search.setText("Search");
        });
    }

    // this method empties the student id ListView and every TextField in the course pane.
    void resetCourseMenu() {
        ArrayList<String> empty = new ArrayList<String>();
        courseListView.getSelectionModel().clearSelection();
        courseID.setText("");
        courseName.setText("");
        courseDays.setText("");
        courseTime.setText("");
        courseStatus.setText("");
        courseLocation.setText("");
        numberLabel.setText("");
        studentListView.setItems(FXCollections.observableArrayList(empty));
    }
    
}