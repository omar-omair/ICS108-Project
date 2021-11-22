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

public class App extends Application{
    private Label registerSystem = new Label("Registration System");
    static ArrayList<Course> coursesList = new ArrayList<Course>();
    static ArrayList<Student> studentsList = new ArrayList<Student>();
    ArrayList<Course> notRegisteredCourseList = new ArrayList<>();
    ArrayList<Course> registeredCourseList = new ArrayList<>();
    ListView<Course> registered = new ListView<>();
    TextField studentID = new TextField();
    ComboBox<Course> notRegistered = new ComboBox<>();
    int studentCount = 0;
    Alert alert = new Alert(Alert.AlertType.ERROR);
    static Stage primaryStage;
    static Scene PrimaryScene;
    static BorderPane mainBorderPane;

    public void start(Stage stage) {
        BorderPane borderPane = new BorderPane();
        Scene main = new Scene(borderPane);
        primaryStage = stage;
        PrimaryScene = main;
        mainBorderPane = borderPane;
        registerSystem.setFont(new Font(40));
        borderPane.setCenter(registerSystem);
        HBox buttonBox = new HBox(10);
        Button course = new Button("View course");
        Button student = new Button("View students details");
        Button save = new Button("Save");
        buttonBox.getChildren().addAll(course,student,save);
        buttonBox.setAlignment(Pos.CENTER);
        borderPane.setBottom(buttonBox);
        BorderPane.setMargin(buttonBox, new Insets(12,12,70,12));

        try(FileInputStream fis = new FileInputStream("res\\Registration.dat");
            ObjectInputStream ois = new ObjectInputStream(fis))
        {
            coursesList = (ArrayList<Course>) ois.readObject();
            studentsList = (ArrayList<Student>) ois.readObject();
        }
        catch(IOException | ClassNotFoundException e) {
            System.out.println(e);
        }

        CoursePane coursePane = new CoursePane();
        

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
        Button previous2 = new Button("< previous");
        Button search2 = new Button("Search");
        HBox buttonsbox = new HBox(10);
        buttonsbox.getChildren().addAll(back2,previous2,next2,register,drop,search2);
        grid.add(buttonsbox,1,6);


        course.setOnAction(e -> {
            setToCourse(primaryStage,PrimaryScene,coursePane);
            coursePane.getInfo();
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


        back2.setOnMouseClicked(e -> {
            setToMain(stage,main);
        });

        next2.setOnMouseClicked(e -> {
            if(studentCount < studentsList.size() - 1) {
                studentCount++;
            }
            getStudentDetails();
        });
        previous2.setOnMouseClicked(e -> {
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

    void setToMain(Stage stage, Scene main) {
        stage.setScene(main);
        stage.setWidth(700);
        stage.setHeight(600);
        stage.setTitle("main");
    }

    void setToCourse(Stage stage, Scene scene, CoursePane pane) {
        scene.setRoot(pane);
        stage.setWidth(900);
        stage.setHeight(500);
        stage.setTitle("Courses");
    }

   

    public static void main(String[] args) {
        launch();
    }
}
