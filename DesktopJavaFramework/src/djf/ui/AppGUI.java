package djf.ui;
import java.util.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import java.io.*;
import javafx.scene.text.Text;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import djf.controller.AppFileController;
import djf.AppTemplate;
import static djf.settings.AppPropertyType.*;
import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import java.net.URL;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.HashMap;
/**
 * This class provides the basic user interface for this application,
 * including all the file controls, but not including the workspace,
 * which would be customly provided for each app.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public class AppGUI {
    // THIS HANDLES INTERACTIONS WITH FILE-RELATED CONTROLS
    protected AppFileController fileController;
    public List<String> savedFiles;
    // THIS IS THE APPLICATION WINDOW
    protected Stage primaryStage;

    // THIS IS THE STAGE'S SCENE GRAPH
    protected Scene primaryScene;

    // THIS PANE ORGANIZES THE BIG PICTURE CONTAINERS FOR THE
    // APPLICATION AppGUI. NOTE THAT THE WORKSPACE WILL GO
    // IN THE CENTER REGION OF THE appPane
    protected BorderPane appPane;
    
    // THIS IS THE TOP PANE WHERE WE CAN PUT TOOLBAR
    protected FlowPane topToolbarPane;
    protected SplitPane welcomePane;
    // THIS IS THE FILE TOOLBAR AND ITS CONTROLS
    protected HBox fileToolbar;
    protected HBox urToolbar;
    protected HBox aboutToolbar;
    // FILE TOOLBAR BUTTONS
    protected Button newButton;
    protected Button loadButton;
    protected Button saveButton;
    protected Button saveAsButton;
    protected Button exportButton;
    
    protected Button aboutButton;
    protected Button undoButton;
    protected Button redoButton;
    
    // THIS DIALOG IS USED FOR GIVING FEEDBACK TO THE USER
    protected AppYesNoCancelDialogSingleton yesNoCancelDialog;
    
    // THIS TITLE WILL GO IN THE TITLE BAR
    protected String appTitle;
    
    /**
     * This constructor initializes the file toolbar for use.
     * 
     * @param initPrimaryStage The window for this application.
     * 
     * @param initAppTitle The title of this application, which
     * will appear in the window bar.
     * 
     * @param app The app within this gui is used.
     */
    public AppGUI(  Stage initPrimaryStage, 
		    String initAppTitle, 
		    AppTemplate app){
	// SAVE THESE FOR LATER
	primaryStage = initPrimaryStage;
	appTitle = initAppTitle;
	       
        // INIT THE TOOLBAR
        initTopToolbar(app);
        	
        // AND FINALLY START initTopToolbar(UP THE WINDOW (WITHOUT THE WORKSPACE)
        initWindow();
        
        // INIT THE STYLESHEET AND THE STYLE FOR THE FILE TOOLBAR
        initStylesheet(app);
        initFileToolbarStyle();        
    }
    
    /**
     * Accessor method for getting the file toolbar controller.
     */
    public AppFileController getFileController() { return fileController; }
    
    /**
     * Accessor method for getting the application pane, within which all
     * user interface controls are ultimately placed.
     * 
     * @return This application GUI's app pane.
     */
    public BorderPane getAppPane() { return appPane; }
    
    /**
     * Accessor method for getting the toolbar pane in the top, within which
     * other toolbars are placed.
     * 
     * @return This application GUI's app pane.
     */    
    public FlowPane getTopToolbarPane() {
        return topToolbarPane;
    }
    
    /**
     * Accessor method for getting the file toolbar pane, within which all
     * file controls are ultimately placed.
     * 
     * @return This application GUI's app pane.
     */    
    public HBox getFileToolbar() {
        return fileToolbar;
    }
    
    /**
     * Accessor method for getting this application's primary stage's,
     * scene.
     * 
     * @return This application's window's scene.
     */
    public Scene getPrimaryScene() { return primaryScene; }
    
    /**
     * Accessor method for getting this application's window,
     * which is the primary stage within which the full GUI will be placed.
     * 
     * @return This application's primary stage (i.e. window).
     */    
    public Stage getWindow() { return primaryStage; }

    /**
     * This method is used to activate/deactivate toolbar buttons when
     * they can and cannot be used so as to provide foolproof design.
     * 
     * @param saved Describes whether the loaded Page has been saved or not.
     */
    public void updateToolbarControls(boolean saved) {
        // THIS TOGGLES WITH WHETHER THE CURRENT COURSE
        // HAS BEEN SAVED OR NOT
        saveButton.setDisable(saved);
        fileController.markAsEdited(this);
        // ALL THE OTHER BUTTONS ARE ALWAYS ENABLED
        // ONCE EDITING THAT FIRST COURSE BEGINS
	newButton.setDisable(false);
        loadButton.setDisable(false);
	exportButton.setDisable(false);

        // NOTE THAT THE NEW, LOAD, AND EXIT BUTTONS
        // ARE NEVER DISABLED SO WE NEVER HAVE TO TOUCH THEM
    }

    /****************************************************************************/
    /* BELOW ARE ALL THE PRIVATE HELPER METHODS WE USE FOR INITIALIZING OUR AppGUI */
    /****************************************************************************/
    
    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initTopToolbar(AppTemplate app) {
        fileToolbar = new HBox();
        aboutToolbar=new HBox();
        urToolbar=new HBox();
        
        // HERE ARE OUR FILE TOOLBAR BUTTONS, NOTE THAT SOME WILL
        // START AS ENABLED (false), WHILE OTHERS DISABLED (true)
        newButton = initChildButton(fileToolbar,	NEW_ICON.toString(),	    NEW_TOOLTIP.toString(),	false);
        loadButton = initChildButton(fileToolbar,	LOAD_ICON.toString(),	    LOAD_TOOLTIP.toString(),	false);
        saveButton = initChildButton(fileToolbar,	SAVE_ICON.toString(),	    SAVE_TOOLTIP.toString(),	true);
        saveAsButton = initChildButton(fileToolbar,	SAVE_AS_ICON.toString(),    SAVE_AS_TOOLTIP.toString(),	false);
        exportButton = initChildButton(fileToolbar,	EXPORT_ICON.toString(),	    EXPORT_TOOLTIP.toString(),	false);
        aboutButton = initChildButton(aboutToolbar,	ABOUT_ICON.toString(),	    ABOUT_TOOLTIP.toString(),	false);
        undoButton = initChildButton(urToolbar,         UNDO_ICON.toString(),       UNDO_TOOLTIP.toString(),	true);
        redoButton = initChildButton(urToolbar,         REDO_ICON.toString(),       REDO_TOOLTIP.toString(),	true);
        
	// AND NOW SETUP THEIR EVENT HANDLERS
        fileController = new AppFileController(app);
        newButton.setOnAction(e -> {
            fileController.handleNewRequest();
        });
        loadButton.setOnAction(e -> {
            fileController.handleLoadRequest();
        });
        saveButton.setOnAction(e -> {
            fileController.handleSaveRequest();
        });
        saveAsButton.setOnAction(e -> {
            fileController.handleSaveAsRequest();
        });
        exportButton.setOnAction(e -> {
            fileController.handleExportRequest();
        });

        aboutButton.setOnAction(e -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("About Matro Map Maker");
            alert.setHeaderText(null);
            ImageView iv=new ImageView(new Image("file:./images/app.png"));
            iv.setFitHeight(50); iv.setFitWidth(50);
            alert.setGraphic(iv);
            alert.setContentText("Matro Map Maker \nFramework:Desktop Java Framework \nDeveloper:Gordon Wu \nYear of Work: 2017");
            alert.showAndWait();
        });
        
        
        // NOW PUT THE FILE TOOLBAR IN THE TOP TOOLBAR, WHICH COULD
        // ALSO STORE OTHER TOOLBARS
        
        topToolbarPane=new FlowPane();
        topToolbarPane.getChildren().addAll(fileToolbar,urToolbar,aboutToolbar);
    }

    // INITIALIZE THE WINDOW (i.e. STAGE) PUTTING ALL THE CONTROLS
    // THERE EXCEPT THE WORKSPACE, WHICH WILL BE ADDED THE FIRST
    // TIME A NEW Page IS CREATED OR LOADED
    private void initWindow() {
	PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // SET THE WINDOW TITLE
        primaryStage.setTitle(appTitle);

        // START FULL-SCREEN OR NOT, ACCORDING TO PREFERENCES
        primaryStage.setMaximized("true".equals(props.getProperty(START_MAXIMIZED)));

        // ADD THE TOOLBAR ONLY, NOTE THAT THE WORKSPACE
        // HAS BEEN CONSTRUCTED, BUT WON'T BE ADDED UNTIL
        // THE USER STARTS EDITING A COURSE
        appPane = new BorderPane();       
        welcome();
        primaryScene = new Scene(appPane);

        // SET THE APP PANE PREFERRED SIZE ACCORDING TO THE PREFERENCES
        double prefWidth = Double.parseDouble(props.getProperty(PREF_WIDTH));
        double prefHeight = Double.parseDouble(props.getProperty(PREF_HEIGHT));
        appPane.setPrefWidth(prefWidth);
        appPane.setPrefHeight(prefHeight);

        // SET THE APP ICON
        String appIcon = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(APP_LOGO);
        primaryStage.getIcons().add(new Image(appIcon));

        // NOW TIE THE SCENE TO THE WINDOW
        primaryStage.setScene(primaryScene);
    }
    
    /**
     * This is a public helper method for initializing a simple button with
     * an icon and tooltip and placing it into a toolbar.
     * 
     * @param toolbar Toolbar pane into which to place this button.
     * 
     * @param icon Icon image file name for the button.
     * 
     * @param tooltip Tooltip to appear when the user mouses over the button.
     * 
     * @param disabled true if the button is to start off disabled, false otherwise.
     * 
     * @return A constructed, fully initialized button placed into its appropriate
     * pane container.
     */
    public Button initChildTextButton(Pane toolbar, String text, String tooltip, boolean disabled){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        Button button=new Button();
        button.setDisable(disabled);
        button.setText(text);
        Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip));
        button.setTooltip(buttonTooltip);
        toolbar.getChildren().add(button);
//        button.prefHeightProperty().bind(toolbar.heightProperty());
        return button;
    }
    public Button initChildButton(Pane toolbar, String icon, String tooltip, boolean disabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
	
	// LOAD THE ICON FROM THE PROVIDED FILE
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(icon);
        Image buttonImage = new Image(imagePath);
	
	// NOW MAKE THE BUTTON
        Button button = new Button();
        button.setDisable(disabled);
        button.setGraphic(new ImageView(buttonImage));
        Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip));
        button.setTooltip(buttonTooltip);
	
	// PUT THE BUTTON IN THE TOOLBAR
        toolbar.getChildren().add(button);
//	button.prefHeightProperty().bind(toolbar.heightProperty());
	// AND RETURN THE COMPLETED BUTTON
        return button;
    }
    
   /**
     *  Note that this is the default style class for the top file toolbar
     * and that style characteristics for this type of component should be 
     * put inside app_properties.xml.
     */
    public static final String CLASS_BORDERED_PANE = "bordered_pane";

   /**
     *  Note that this is the default style class for the file buttons in
     * the top file toolbar and that style characteristics for this type
     * of component should be put inside app_properties.xml.
     */
    public static final String CLASS_FILE_BUTTON = "file_button";
    
    /**
     * This function sets up the stylesheet to be used for specifying all
     * style for this application. Note that it does not attach CSS style
     * classes to controls, that must be done separately.
     */
    private void initStylesheet(AppTemplate app) {
	// SELECT THE STYLESHEET
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	String stylesheet = props.getProperty(APP_PATH_CSS);
	stylesheet += props.getProperty(APP_CSS);
        Class appClass = app.getClass();
	URL stylesheetURL = appClass.getResource(stylesheet);
	String stylesheetPath = stylesheetURL.toExternalForm();
	primaryScene.getStylesheets().add(stylesheetPath);	
    }
    
    /**
     * This function specifies the CSS style classes for the controls managed
     * by this framework.
     */
    private void initFileToolbarStyle() {
	topToolbarPane.getStyleClass().add(CLASS_BORDERED_PANE);
        fileToolbar.getStyleClass().add(CLASS_BORDERED_PANE);
        aboutToolbar.getStyleClass().add(CLASS_BORDERED_PANE);
        urToolbar.getStyleClass().add(CLASS_BORDERED_PANE);
	newButton.getStyleClass().add(CLASS_FILE_BUTTON);
	loadButton.getStyleClass().add(CLASS_FILE_BUTTON);
	saveButton.getStyleClass().add(CLASS_FILE_BUTTON);
        saveAsButton.getStyleClass().add(CLASS_FILE_BUTTON);
	exportButton.getStyleClass().add(CLASS_FILE_BUTTON);
        aboutButton.getStyleClass().add(CLASS_FILE_BUTTON);
        undoButton.getStyleClass().add(CLASS_FILE_BUTTON);
        redoButton.getStyleClass().add(CLASS_FILE_BUTTON);
    }
    public Button getUndoButton(){
        return undoButton;
    }
    public Button getRedoButton(){
        return redoButton;
    }
    /**
     * Create the welcome Dialog.
     */
    public void welcome(){
        welcomePane=new SplitPane();
        VBox left=new VBox();
        left.setSpacing(50);
        left.setAlignment(Pos.CENTER);
        Text recentWork=new Text("Recent Work");
        recentWork.setFont(new Font(32));
        left.getChildren().add(recentWork);
        VBox right=new VBox();
        //left
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("recent.txt")))) {
            String name, path;
            savedFiles=new ArrayList<>();
            while( (name = reader.readLine() ) != null) {
                 savedFiles.add(name);
        }
        for(int i=savedFiles.size()-1;i>=0;i--){
            int x=i;
            String str=savedFiles.get(x);
            if(!savedFiles.get(x).equals("")){
            Text mapText=new Text(savedFiles.get(x));
            mapText.setFont(new Font(32));
            mapText.setUnderline(true);
            mapText.setFill(Color.valueOf("#6666ff"));
            mapText.setOnMouseClicked(e->{
                String filePath=System.getProperty("user.dir")+"/work/"+str;
                if(!filePath.contains(".json"))
                    filePath+=".json";
            fileController.setSelectedFile(filePath);
            fileController.promptToOpen();
            appPane.setTop(topToolbarPane);
        });
            left.getChildren().add(mapText);
            }
        }
        }
        catch(Exception e) {
        e.printStackTrace();
        }
        //right
        right.setSpacing(111);
        right.setAlignment(Pos.TOP_CENTER);
//         left.getChildren().add();
        Text newMapText=new Text("Create New Metro Map");
        newMapText.setFont(new Font(32));
        newMapText.setFill(Color.valueOf("#6666ff"));
        newMapText.setUnderline(true);
        Text closeText=new Text("Close");
        right.getChildren().addAll(new ImageView(new Image("file:./images/cover.png")), newMapText, closeText);
        newMapText.setOnMouseClicked(e->{
            fileController.handleNewRequest();
            appPane.setTop(topToolbarPane);
        });       
        //Close
        closeText.setFont(new Font(32));
        closeText.setUnderline(true);
        closeText.setOnMouseClicked(e->{
            appPane.setTop(topToolbarPane);
            welcomePane.getItems().removeAll(left, right);
        });
        welcomePane.getItems().addAll(left, right);
        appPane.setCenter(welcomePane);
    }
}
