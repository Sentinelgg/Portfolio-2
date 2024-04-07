package com.example.port2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class HelloApplication extends Application {
    ComboBox<String> base=new ComboBox<>();
    ComboBox<String> combo=new ComboBox<>();
    ComboBox<String> subjectModule=new ComboBox<>();
    ComboBox<String> course=new ComboBox<>();

    Button button =new Button("Add");
    TextArea area=new TextArea();
    TextField name= new TextField();
    @Override
    public void start(Stage stage) throws IOException {
        VBox box=new VBox(name,button,base,combo,subjectModule,course,area);
        fillCombo();
        combo.setOnAction(e->selCourse());
        subjectModule.setOnAction(e->selSub());
        base.setOnAction(e->selBase());
        course.setOnAction(e->selSubCourse());
        button.setOnAction(e->selName());
        Scene scene = new Scene(box, 750,750);
        stage.setTitle("Select Bachelor Program");
        stage.setScene(scene);
        stage.show();
    }
    Model model=new Model();
    void selBase(){
        combo.getItems().clear();
        for(String c: model.baseCourse(base.getValue()))
            combo.getItems().add(c);
    }

    void selSub(){
        course.getItems().clear();
        for(String s: model.subjectCourse(subjectModule.getValue()))
            course.getItems().add(s);
    }

    void fillCombo(){
        //MyDB db=new MyDB();
        //ArrayList<String> courses=
          //      db.query("select name from course;","name");
        for(String b:model.baseProgram())
          base.getItems().add(b);
        for(String s:model.subjectModule())
            subjectModule.getItems().add(s);
    }
    void selCourse(){
        area.setText(area.getText()+"\n"+combo.getValue());
    }
    void selSubCourse(){
        area.setText(area.getText()+"\n"+course.getValue());
    }

    void selName(){
        area.setText(area.getText()+"\n"+name.getText());
    }


    public static void main(String[] args) {
        launch();
    }



}


class MyDB{
    Connection conn = null;
    MyDB(){
        if(conn==null)open();
    }
    public void open(){
        try {
            String url = "jdbc:sqlite:identifier.sqlite";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("cannot open");
            if (conn != null) close();
        };
    }
    public void close(){
        try {
            if (conn != null) conn.close();
        } catch (SQLException e ) {
            System.out.println("cannot close");
        }
        conn=null;
    }
    public void cmd(String sql){
        if(conn==null)open();
        if(conn==null){System.out.println("No connection");return;}
        Statement stmt=null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e ) {
            System.out.println("Error in statement "+sql);
        }
        try {
            if (stmt != null) { stmt.close(); }
        } catch (SQLException e ) {
            System.out.println("Error in statement "+sql);
        }
    }
    public ArrayList<String> query(String query,String fld){
        ArrayList<String> res=new ArrayList<>();
        if(conn==null)open();
        if(conn==null){System.out.println("No connection");return res;}
        Statement stmt=null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String name = rs.getString(fld);
                res.add(name);
            }
        } catch (SQLException e ) {
            System.out.println("Error in statement "+query+" "+fld);
        }
        try {
            if (stmt != null) { stmt.close(); }
        } catch (SQLException e ) {
            System.out.println("Error in statement "+query+" "+fld);
        }
        return res;
    }
}



class Model{
    List<String> baseProgram(){return Arrays.asList("NatBach","HumTek","Electives", "Projects");}
    List<String> subjectModule(){return Arrays.asList("Computer Science","Informatik","Astrology");}
    List<String> baseCourse(String base){
        if(base.equals("NatBach")) {
            return Arrays.asList(
                    "BK1 Empirical Data",
                    "BK2 Experimental Methods",
                    "BK3 Theory of Natural Science",
                    "Logic and Discrete Mathematics",
                    "Functional Biology – Zoology",
                    "Linear Algebra",
                    "Organic Chemistry",
                    "Biological Chemistry",
                    "Statistical Models",
                    "Functional Programming and Language Implementations",
                    "Classical Mechanics",
                    "Environmental Science",
                    "Cell Biology",
                    "Functional biology – Botany",
                    "Supplementary Physics",
                    "Calculus",
                    "The Chemical Reaction",
                    "Scientific Computing",
                    "Energy and Climate Changes"
            );
        }
        if(base.equals("HumTek")){
            return Arrays.asList(
                    "Design og Konstruktion I+Workshop" ,
                    "Subjektivitet, Teknologi og Samfund I" ,
                    "Teknologiske systemer og artefakter I" ,
                    "Videnskabsteori" ,
                    "Design og Konstruktion II+Workshop" ,
                    "Subjektivitet, Teknologi og Samfund II" ,
                    "Bæredygtige teknologier" ,
                    "Kunstig intelligens" ,
                    "Medier og teknologi - datavisualisering" ,
                    "Teknologiske Systemer og Artefakter II - Sundhedsteknologi" ,
                    "Den (in)humane storby" ,
                    "Interactive Design in the Field" ,
                    "Organisation og ledelse af designprocesser"
            );
        }
        if(base.equals("Electives")){
            return Arrays.asList("Smart Cities"
                    ,"AI and Communication Practices",
                    "Data Science and Visualization"
            );
        }
        if(base.equals("Projects")){
            return Arrays.asList("BaseProject1 ", "BaseProject2 ", "BaseProject3 ", "Bachelorproject ");
        }

        return null;
    }


    List<String> subjectCourse(String base) {
        if (base.equals("Computer Science")) {
            return Arrays.asList("Essential Computing",
                    "Software Development","Interactive Digital Systems", "Subject module project in Computer Science" );
        }
        if (base.equals("Informatik")) {
            return Arrays.asList("Organisatorisk forandring og IT",
                    "BANDIT","Web Baserede IT Systemer", "Subject module project in Informatik" );
        }
        if (base.equals("Astrology")) {
            return Arrays.asList("Essential Astrology",
                    "Venus studies","Mars studies","Ascendant calculations", "Subject module project in Astrology" );
        }
        return null;
    }

}

