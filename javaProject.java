/*
 * @authors
 * Section 001:
 * U1910009 - Akhadjonova Gulkhayo
 * U1910025 - Abdrashitova Elina
 * U1910042 - Mirsaidova Dilnozakhon
 * U1910076 - Dildora Asadova
 *  */


import java.sql.Connection;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;
import java.util.Scanner;
import java.util.Date;
import java.util.List;
import com.sun.prism.impl.Disposer.Record;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;


public class javaProject extends Application {
	//create table for info
	private TableView<Task> table = new TableView<>();
	//create ObservableList to put there info of importance for table from class Typ
    private final ObservableList<Typ> typData
            = FXCollections.observableArrayList(
                    new Typ("high"),
                    new Typ("medium"),
                    new Typ("low"));
	//create ObservableList to put there info  for table from class Task
   private ObservableList<Task> data
           = FXCollections.observableArrayList();
   //horizontal box
    final HBox hb = new HBox();
    //global variable to search user in sql
    String name;
	
   public void start(Stage stage) {
	   
	   Scanner input = new Scanner(System.in);//take input
    //Creating nodes
      TextField textField = new TextField();
      PasswordField pwdField = new PasswordField();
      Button button = new Button("Submit");
      button.setTranslateX(150);
      button.setTranslateY(75);
      
      Button button1 = new Button("Register new account");
      button1.setTranslateX(270);
      button1.setTranslateY(75);
      
      //Creating labels
      Label label1 = new Label("Name: ");
      Label label2 = new Label("Password: ");
      
      //Setting the message with read data
      Text text = new Text("");
      Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10);
      text.setFont(font);
      text.setTranslateX(15);
      text.setTranslateY(125);
      text.setFill(Color.BROWN);
      
      //connect to sql
      button.setOnAction(e -> {
         //Retrieving data
         String name = textField.getText();
         this.name = name;
         String pwd = pwdField.getText();
         String rightPassword = null;
        
         try (
		         
					// Step 1: Allocate a database 'Connection' object
				         Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/MyApp?autoReconnect=true&useSSL=false", "root", "12345");
				              
				 
				         // Step 2: Allocate a 'Statement' object in the Connection
				         Statement stmt = conn.createStatement();
				      )
				{
				         // Step 3: Execute a SQL SELECT query, the query result
			         String strSelect = "select * from userinfo where name= \"" + name + "\"";


		            ResultSet result = stmt.executeQuery(strSelect);
		            
		            while (result.next()) {
		            	rightPassword = result.getString("password");
		            }

			         // Step 4: Process the ResultSet by scrolling the cursor forward via next().
			         //  For each row, retrieve the contents of the cells with getXxx(columnName).
			         conn.close();
			      } catch(SQLException ex) {
			         ex.printStackTrace();
			      }  
         
         
        
         // check password of user in db
         if(pwd.equals(rightPassword)) {
            
            stage.close();//close registration window if logged successful
            
            EnterFromSql();//enter info that already existed
            Experiment();//enter new data and register in db
            
         } else {//if user password is wrong or doesnt exist
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong password");
          
            alert.show();//show alert window
         }
      });    
      button1.setOnAction(e -> {
          //Retrieving data
          String name = textField.getText();
          String pwd = pwdField.getText();
          text.setText("Account registered!");
          
          try (
			         
  				// Step 1: Allocate a database 'Connection' object
  			         Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/MyApp?autoReconnect=true&useSSL=false", "root", "12345");		 
  			         // Step 2: Allocate a 'Statement' object in the Connection
  			         Statement stmt = conn.createStatement();
        		  Statement stmt1 = conn.createStatement();
  			      )
  			{
  			         // Step 3: Execute a SQL SELECT query, the query result
  			         //  is returned in a 'ResultSet' object.				
  			       String strSelect = "insert into userinfo values ( \"" + name + "\"," + "\"" + pwd + "\"" + ")";
  			       String strSelect1 = "CREATE TABLE " + name + "(Date date, ToDo VARCHAR(20), Description VARCHAR(70));";

  			 
  			        int count= stmt.executeUpdate(strSelect);
  			        int count1= stmt.executeUpdate(strSelect1);
  			 
  			         // Step 4: Process the ResultSet by scrolling the cursor forward via next().
  			         //  For each row, retrieve the contents of the cells with getXxx(columnName).
  			         conn.close();
  			      } catch(SQLException ex) {
  			         ex.printStackTrace();
  			      }
       });
     
      //Adding labels for nodes
      HBox box = new HBox(5);
      box.setPadding(new Insets(25, 5 , 5, 50));
      box.getChildren().addAll(label1, textField, label2, pwdField);
      Group root = new Group(box, button, text,button1);
      //Setting the stage
      Scene scene = new Scene(root, 595, 150, Color.LIGHTGRAY);
     
      
      stage.setTitle("Log in");
      stage.setScene(scene);
      stage.show();
   }
   
   public static void main(String args[]){
      launch(args);
   }
   

   class EditingCell extends TableCell<Task, String> {

       private TextField textField;

       private EditingCell() {
       }

       @Override
       public void startEdit() {
           if (!isEmpty()) {
               super.startEdit();
               createTextField();
               setText(null);
               setGraphic(textField);
               textField.selectAll();
           }
       }

       @Override
       public void cancelEdit() {
           super.cancelEdit();

           setText((String) getItem());
           setGraphic(null);
       }

       @Override
       public void updateItem(String item, boolean empty) {
           super.updateItem(item, empty);

           if (empty) {
               setText(item);
               setGraphic(null);
           } else {
               if (isEditing()) {
                   if (textField != null) {
                       textField.setText(getString());
                   }
                   setText(null);
                   setGraphic(textField);
               } else {
                   setText(getString());
                   setGraphic(null);
               }
           }
       }

       private void createTextField() {
           textField = new TextField(getString());
           textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
           textField.setOnAction((e) -> commitEdit(textField.getText()));
           textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
               if (!newValue) {
                   System.out.println("Commiting " + textField.getText());
                   commitEdit(textField.getText());
               }
           });
       }

       private String getString() {
           return getItem() == null ? "" : getItem();
       }
   }

   class DateEditingCell extends TableCell<Task, Date> {

       private DatePicker datePicker;

       private DateEditingCell() {
       }

       @Override
       public void startEdit() {
           if (!isEmpty()) {
               super.startEdit();
               createDatePicker();
               setText(null);
               setGraphic(datePicker);
           }
       }

       @Override
       public void cancelEdit() {
           super.cancelEdit();

           setText(getDate().toString());
           setGraphic(null);
       }

       @Override
       public void updateItem(Date item, boolean empty) {
           super.updateItem(item, empty);

           if (empty) {
               setText(null);
               setGraphic(null);
           } else {
               if (isEditing()) {
                   if (datePicker != null) {
                       datePicker.setValue(getDate());
                   }
                   setText(null);
                   setGraphic(datePicker);
               } else {
                   setText(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                   setGraphic(null);
               }
           }
       }

       private void createDatePicker() {
           datePicker = new DatePicker(getDate());
           datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
           datePicker.setOnAction((e) -> {
               System.out.println("Committed: " + datePicker.getValue().toString());
               commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
           });
//           datePicker.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//               if (!newValue) {
//                   commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//               }
//           });
       }

       private LocalDate getDate() {
           return getItem() == null ? LocalDate.now() : getItem().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
       }
   }

   class ComboBoxEditingCell extends TableCell<Task, Typ> {

       private ComboBox<Typ> comboBox;

       private ComboBoxEditingCell() {
       }

       @Override
       public void startEdit() {
           if (!isEmpty()) {
               super.startEdit();
               createComboBox();
               setText(null);
               setGraphic(comboBox);
           }
       }

       @Override
       public void cancelEdit() {
           super.cancelEdit();

           setText(getTyp().getTyp());
           setGraphic(null);
       }

       @Override
       public void updateItem(Typ item, boolean empty) {
           super.updateItem(item, empty);

           if (empty) {
               setText(null);
               setGraphic(null);
           } else {
               if (isEditing()) {
                   if (comboBox != null) {
                       comboBox.setValue(getTyp());
                   }
                   setText(getTyp().getTyp());
                   setGraphic(comboBox);
               } else {
                   setText(getTyp().getTyp());
                   setGraphic(null);
               }
           }
       }

       private void createComboBox() {
           comboBox = new ComboBox<>(typData);
           comboBoxConverter(comboBox);
           comboBox.valueProperty().set(getTyp());
           comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
           comboBox.setOnAction((e) -> {
               System.out.println("Committed: " + comboBox.getSelectionModel().getSelectedItem());
               commitEdit(comboBox.getSelectionModel().getSelectedItem());
           });
       }

       private void comboBoxConverter(ComboBox<Typ> comboBox) {
           // Define rendering of the list of values in ComboBox drop down. 
           comboBox.setCellFactory((c) -> {
               return new ListCell<Typ>() {
                   @Override
                   protected void updateItem(Typ item, boolean empty) {
                       super.updateItem(item, empty);

                       if (item == null || empty) {
                           setText(null);
                       } else {
                           setText(item.getTyp());
                       }
                   }
               };
           });
       }

       private Typ getTyp() {
           return getItem() == null ? new Typ("") : getItem();
       }
   }

   public static class Typ {

       private final SimpleStringProperty typ;

       public Typ(String typ) {
           this.typ = new SimpleStringProperty(typ);
       }

       public String getTyp() {
           return this.typ.get();
       }

       public StringProperty typProperty() {
           return this.typ;
       }

       public void setTyp(String typ) {
           this.typ.set(typ);
       }

       @Override
       public String toString() {
           return typ.get();
       }

   }
   
   public static class Project {
       private final SimpleStringProperty name;
       private final SimpleListProperty<Task> persons;

       public Project(String name, List<Task> persons) {
           this.name = new SimpleStringProperty(name);
           
           this.persons = new SimpleListProperty<>();
           this.persons.setAll(persons);
       }
       
       public String getName() {
           return name.get();
       }

       public StringProperty nameProperty() {
           return this.name;
       }

       public void setName(String name) {
           this.name.set(name);
       }
       
       public List<Task> getPersons() {
           return this.persons.get();
       }
       
       public SimpleListProperty<Task> personsProperty() {
           return this.persons;
       }
       
       public void setPersons(List<Task> persons) {
           this.persons.setAll(persons);
       }
       
   }

   public static class Task {


       private final SimpleStringProperty firstName;
       private final SimpleObjectProperty<Typ> typ;
       private final SimpleObjectProperty<Date> birthday;
       private final SimpleStringProperty descr;

       public Task(String firstName, Typ typ, Date bithday, String descr) {
           this.firstName = new SimpleStringProperty(firstName);
           this.typ = new SimpleObjectProperty(typ);
           this.birthday = new SimpleObjectProperty(bithday);
           this.descr = new SimpleStringProperty(descr);
       }

       public String getFirstName() {
           return firstName.get();
       }

       public StringProperty firstNameProperty() {
           return this.firstName;
       }

       public void setFirstName(String firstName) {
           this.firstName.set(firstName);
       }
       
       public String getDescr() {
           return descr.get();
       }

       public StringProperty DescrProperty() {
           return this.descr;
       }

       public void setDescr(String descr) {
           this.descr.set(descr);
       }

       public Typ getTypObj() {
           return typ.get();
       }

       public ObjectProperty<Typ> typObjProperty() {
           return this.typ;
       }

       public void setTypObj(Typ typ) {
           this.typ.set(typ);
       }

       public Date getBirthday() {
           return birthday.get();
       }

       public ObjectProperty<Date> birthdayProperty() {
           return this.birthday;
       }

       public void setBirthday(Date birthday) {
           this.birthday.set(birthday);
       }

   }

   private class ButtonCell extends TableCell<Record, Boolean> {
       final Button cellButton = new Button("Complete");
       
       ButtonCell(){
           
       	//Action when the button is pressed
           cellButton.setOnAction(new EventHandler<ActionEvent>(){

               @Override
               public void handle(ActionEvent t) {
                   // get Selected Item
            	   Task currentPerson = (Task) ButtonCell.this.getTableView().getItems().get(ButtonCell.this.getIndex());
               	
               	
               	
               	try (
     			         
    					// Step 1: Allocate a database 'Connection' object
    				         Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/myapp?autoReconnect=true&useSSL=false", "root", "12345");
    				              
    				 
    				         // Step 2: Allocate a 'Statement' object in the Connection
    				         Statement stmt = conn.createStatement();
    				      )
    				{
    				  
				
			         String strSelect = "delete from " + name + " where ToDo = \"" + currentPerson.getFirstName() + "\"";
				
			 
			         int count= stmt.executeUpdate(strSelect);
			 
			         // Step 4: Process the ResultSet by scrolling the cursor forward via next().
			         //  For each row, retrieve the contents of the cells with getXxx(columnName).
			         conn.close();
			      } catch(SQLException ex) {
			         ex.printStackTrace();
			      }
      	
               	//remove selected item from the table list
               	data.remove(currentPerson);
               }
           });
       }

       //Display button if the row is not empty
       @Override
       protected void updateItem(Boolean t, boolean empty) {
       super.updateItem(t, empty);
       if(!empty){
       setGraphic(cellButton);
       }
       else{
       setGraphic(null);
       }
       }
       
   }
   
   public void EnterFromSql() {
	   try (
		         
				// Step 1: Allocate a database 'Connection' object
			         Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/myapp?autoReconnect=true&useSSL=false", "root", "12345");
			              
			 
			         // Step 2: Allocate a 'Statement' object in the Connection
			         Statement stmt = conn.createStatement();
			      )
			{
			         // Step 3: Execute a SQL SELECT query, the query result
			         //  is returned in a 'ResultSet' object.
	  
		
	         String strSelect = "select * from " + name;

           ResultSet result = stmt.executeQuery(strSelect);
           while (result.next()) {
           
        	   String ToDo = result.getString("ToDo");
        	   Date d = result.getDate("Date");
        	   String des = result.getString("description");
        	   //data from sql to Task class
               data.add(new Task(ToDo,new Typ("medium"),new Date(),des));
            }
           	
	         conn.close();
	      } catch(SQLException ex) {
	         ex.printStackTrace();
	      }    		   
	   
   }

   public void Experiment() {
	   //new window
       Stage newStage = new Stage();
 
       //parameters
       Scene scene = new Scene(new Group(),Color.LIGHTGRAY);
       newStage.setWidth(650);
       newStage.setHeight(520);
       newStage.setTitle("DeadlineR");

       //to add new info 
       table.setEditable(true);
       Callback<TableColumn<Task, String>, TableCell<Task, String>> cellFactory
               = (TableColumn<Task, String> param) -> new EditingCell();
       Callback<TableColumn<Task, Date>, TableCell<Task, Date>> dateCellFactory
               = (TableColumn<Task, Date> param) -> new DateEditingCell();
       Callback<TableColumn<Task, Typ>, TableCell<Task, Typ>> comboBoxCellFactory
               = (TableColumn<Task, Typ> param) -> new ComboBoxEditingCell();
       
        //Task column
       TableColumn<Task, String> firstNameCol = new TableColumn("Task");
       firstNameCol.setMinWidth(100);
       firstNameCol.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
       firstNameCol.setCellFactory(cellFactory);
       firstNameCol.setOnEditCommit(
               (TableColumn.CellEditEvent<Task, String> t) -> {
                   ((Task) t.getTableView().getItems()
                   .get(t.getTablePosition().getRow()))
                   .setFirstName(t.getNewValue());

               });
     //Importance column
       TableColumn<Task, Typ> lastNameCol = new TableColumn("Importance");
       lastNameCol.setMinWidth(100);
       lastNameCol.setCellValueFactory(cellData -> cellData.getValue().typObjProperty());
       lastNameCol.setCellFactory(comboBoxCellFactory);
       lastNameCol.setOnEditCommit(
               (TableColumn.CellEditEvent<Task, Typ> t) -> {
                   ((Task) t.getTableView().getItems()
                   .get(t.getTablePosition().getRow()))
                   .setTypObj(t.getNewValue());

               });
     //Deadline column
       TableColumn<Task, Date> deadline = new TableColumn("Deadline");
       deadline.setMinWidth(100);
       deadline.setCellValueFactory(cellData -> cellData.getValue().birthdayProperty());
       deadline.setCellFactory(dateCellFactory);
       deadline.setOnEditCommit(
               (TableColumn.CellEditEvent<Task, Date> t) -> {
                   ((Task) t.getTableView().getItems()
                   .get(t.getTablePosition().getRow()))
                   .setBirthday(t.getNewValue());

               });
       
       
     //Description column
       TableColumn<Task, String> descriptionCol = new TableColumn("Description");
       descriptionCol.setMinWidth(200);
       descriptionCol.setCellValueFactory(cellData -> cellData.getValue().DescrProperty());
       descriptionCol.setCellFactory(cellFactory);
       descriptionCol.setOnEditCommit(
               (TableColumn.CellEditEvent<Task, String> t) -> {
                   ((Task) t.getTableView().getItems()
                   .get(t.getTablePosition().getRow()))
                   .setDescr(t.getNewValue());

               });
       
       
       //set pre-data
       table.setItems(data);
       //set columns in table
       table.getColumns().addAll(firstNameCol, lastNameCol, deadline, descriptionCol);

       //for user input of task
       final TextField addFirstName = new TextField();
       addFirstName.setPromptText("task");
       addFirstName.setPrefWidth(250);

     //for user input of description
       final TextField addDescr = new TextField();
       addDescr.setPrefWidth(300);
       addDescr.setPromptText("description");

       //add button
       //adds new info to the table(javafx) and db(sql)
       final Button addButton = new Button("Add");
       addButton.setOnAction((ActionEvent e)
               -> {
                   data.add(new Task(
                                   addFirstName.getText(),
                                   new Typ("medium"),
                                   new Date(), 
                                   addDescr.getText()));
               
                   try (
      			         
           				// Step 1: Allocate a database 'Connection' object
           			         Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/myapp?autoReconnect=true&useSSL=false", "root", "12345");
           			              
           			 
           			         // Step 2: Allocate a 'Statement' object in the Connection
           			         Statement stmt = conn.createStatement();
           			      )
           			{
                	   String strSelect = "insert into " + name + " values (\"2021-01-10\"," + "\"" + addFirstName.getText() + "\"," + "\"" + addDescr.getText()+ "\")";
 
           			         int count= stmt.executeUpdate(strSelect);
           			 
           			         // Step 4: Process the ResultSet by scrolling the cursor forward via next().
           			         //  For each row, retrieve the contents of the cells with getXxx(columnName).
           			         conn.close();
           			      } catch(SQLException ex) {
           			         ex.printStackTrace();
           			      }
                                 
                   addFirstName.clear();
                   addDescr.clear();
               }
       );

       //Insert Button
       TableColumn col_action = new TableColumn<>();
       table.getColumns().add(col_action);
       
       col_action.setCellValueFactory(
               new Callback<TableColumn.CellDataFeatures<Record, Boolean>, 
               ObservableValue<Boolean>>() {

           @Override
           public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Record, Boolean> p) {
               return new SimpleBooleanProperty(p.getValue() != null);
           }
       });

       //Adding the Button to the cell
       col_action.setCellFactory(
               new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {

           @Override
           public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> p) {
               return new ButtonCell();
           }
           
       
       });
       //add to box textfields and button
       hb.getChildren().addAll(addFirstName,addDescr, addButton);
       hb.setSpacing(3);
 
       final VBox vbox = new VBox();
       vbox.setSpacing(5);
       vbox.setPadding(new Insets(10, 0, 0, 10));
       vbox.getChildren().addAll( table, hb);
       

       ((Group) scene.getRoot()).getChildren().addAll(vbox);

      
       newStage.setScene(scene);
       newStage.show();
   }
   
}