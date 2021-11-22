package project;

import javafx.application.Application;
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
    static Alert alert = new Alert(Alert.AlertType.ERROR);
    static Stage primaryStage;
    static Scene primaryScene;
    static BorderPane mainBorderPane = new BorderPane();
    StudentPane studentPane = new StudentPane();


    public void start(Stage stage) {
        primaryScene = new Scene(mainBorderPane);
        primaryStage = stage;
        registerSystem.setFont(new Font(40));
        mainBorderPane.setCenter(registerSystem);
        HBox buttonBox = new HBox(10);
        Button course = new Button("View course");
        Button student = new Button("View students details");
        Button save = new Button("Save");
        buttonBox.getChildren().addAll(course,student,save);
        buttonBox.setAlignment(Pos.CENTER);
        mainBorderPane.setBottom(buttonBox);
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

        course.setOnAction(e -> {
            setToCourse(primaryStage,primaryScene,coursePane);
            coursePane.getInfo();
        });

        student.setOnAction(e -> {
            setToStudent(primaryStage,primaryScene,studentPane);
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


        stage.setScene(primaryScene);
        stage.setWidth(700);
        stage.setHeight(600);
        stage.show();
    }

    static void setToMain(Stage stage, Scene scene, BorderPane pane) {
        scene.setRoot(pane);
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

    void setToStudent(Stage stage, Scene scene, StudentPane pane) {
        scene.setRoot(pane);
        stage.setWidth(650);
        stage.setHeight(650);
        stage.setTitle("Student");
        studentPane.studentCount = 0;
        studentPane.getStudentDetails();
    }

   

    public static void main(String[] args) {
        launch();
    }
}
