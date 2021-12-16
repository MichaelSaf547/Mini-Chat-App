/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 *
 * @author AL Lewaa Company
 */
public class ClientGUI extends Application{
    
    Button Send;
    FlowPane fp;
    TextField tf;
    TextArea Area;
    Label labelN;
    Label labelM;
    
    MenuBar mb;
    Menu Chat;
    MenuItem Save;
    MenuItem Open;
    BorderPane bp;
    
    VBox vbox;
    
    int height = 300;
    int width = 300;
    
    DataInputStream dis;
    PrintStream ps;
    Socket mysocket;
    
    FileChooser File_ChooserS;
    FileChooser File_ChooserO;
    
    String location;
    
    @Override
    public void init()
    {
        File_ChooserS = new FileChooser();
        File_ChooserS.setTitle("Save File");
        File_ChooserO = new FileChooser();
        File_ChooserO.setTitle("Open File");
        File_ChooserS.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        File_ChooserO.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        
        Save = new MenuItem("Save");
        Open = new MenuItem("Open");
        
        Chat = new Menu("Chat");
        Chat.getItems().addAll(Open, Save);
        
        mb = new MenuBar();
        mb.getMenus().addAll(Chat);
        
        bp = new BorderPane();
        bp.setTop(mb);
        
        labelN = new Label("Chat Massages");
        
        Area = new TextArea();
        Area.setPrefHeight(height);
        Area.setPrefWidth(width);
        Area.setEditable(false);
        
        labelM = new Label("Enter your message");
        
        tf = new TextField();
        tf.setPromptText("Enter your message");
        
        Send = new Button("Send");
        Send.setDefaultButton(true);
        
        fp = new FlowPane();
        fp.getChildren().addAll(labelM, tf, Send);
        
        
        vbox = new VBox();
        vbox.getChildren().addAll(bp, labelN, Area, fp);
        
        try {
            mysocket = new Socket("127.0.0.1", 5005);
            dis = new DataInputStream(mysocket.getInputStream());
            ps = new PrintStream(mysocket.getOutputStream());
            
            
        } catch (IOException ex) {
            Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public void Read(Stage primaryStage){
        location = File_ChooserO.showOpenDialog(primaryStage).toString();
        try {
            File file = new File(location);
            String name = file.getName();
            Area.clear();
            Scanner s = new Scanner(file).useDelimiter("\\s+");
            while (s.hasNextLine()) 
                {
                    Area.appendText(s.nextLine() + "\n");
                }
            primaryStage.setTitle(name);
        }
        
        
        catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }
    
    public void Write(Stage primaryStage)
    {
        FileChooser.ExtensionFilter extFilter = 
        new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        File_ChooserS.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = File_ChooserS.showSaveDialog(primaryStage);
        String name = file.getName();
        primaryStage.setTitle(name);
        if(file != null)
        {
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(file);
                fileWriter.write(Area.getText());
                fileWriter.close();
            } 
            catch (IOException ex) {
                Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    
    @Override
    public void start(Stage primaryStage) {
        
        Scene scene = new Scene(vbox, 500, 500);
        
        primaryStage.setTitle("Chat");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        Send.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                
                String msg = tf.getText();
                    
                if(msg.trim().isEmpty() == false)
                {
                    ps.println(msg.trim());
                    tf.setText("");
                }
                
            }
        });
        
        
        
        Open.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>(){
            
            @Override
            public void handle(ActionEvent event) {
                Read(primaryStage);
            }
    
        });
        
        Save.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                
                Write(primaryStage);
                
            }
        
        });
        
        
        
        new Thread(new Runnable(){
            @Override
            public void run() {
            
                while(true)
                {
                    try {
                        String msg = dis.readLine();
                        Area.appendText(msg + "\n");
                    } catch (IOException ex) {
                        Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
        }).start();
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() { //close scene
            @Override
            public void handle(WindowEvent t) {
                
                primaryStage.close();
                Platform.exit();
                System.exit(0);
            }
        });

        
    }
    
    
    
    public static void main(String args[])
    {
        launch(args);
    }
    
    
}
