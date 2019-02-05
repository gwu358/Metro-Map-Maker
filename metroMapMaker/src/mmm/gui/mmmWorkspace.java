package mmm.gui;
import java.io.IOException;
import java.util.HashMap;
import javafx.util.Callback;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
//import javafx.scene.control.ButtonBase;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.geometry.Insets;
import javafx.util.StringConverter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.ComboBox;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import mmm.data.*;
import static mmm.mmmLanguageProperty.*;
import mmm.data.mmmData;
import static mmm.data.mmmData.BLACK_HEX;
import static mmm.data.mmmData.WHITE_HEX;
import mmm.data.mmmState;
import djf.ui.AppYesNoCancelDialogSingleton;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppGUI;
import djf.AppTemplate;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import javafx.geometry.Pos;
//import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
//import static djf.settings.AppStartupConstants.PATH_IMAGES;
import static mmm.css.mmmStyle.*;
//import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;

/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class mmmWorkspace extends AppWorkspaceComponent {
    int c=0;
    // HERE'S THE APP
    AppTemplate app;
    
    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;
    
    // HAS ALL THE CONTROLS FOR EDITING
    VBox editToolbar;
    boolean action=true;
    // FIRST ROW
    VBox row1Box;
    Label lineLabel;
    ComboBox<MetroLine> lineCB;
    Button editLineButton;
    Button addLineButton;
    Button removeLineButton;
    Button addStationToLineButton;
    Button removeStationFromLineButton;
    Button listStationsButton;
    Slider lineThicknessSlider;
    
//    Button selectionToolButton;
//    Button removeButton;
//    Button rectButton;
//    Button ellipseButton;
    
    
//    //11 ROW
//    HBox row11Box;
//    Button imageButton;
//    Button textButton;
//    
//    //12 Row
//    HBox row12Box;
//    ComboBox<String> fontFamily;
//    ComboBox<Double> fontSize;
//    Button boldButton;
//    Button italicsButton;
    
    // SECOND ROW
    VBox row2Box;
    Label stationLabel;
    ComboBox<Station> stationCB;
    Button editStationButton;
    Button addStationButton;
    Button removeStationButton;
    Button snapButton;
    Button moveLabelButton;
    Button rotateLabelButton;
    Slider stationRadiusSlider;
//    Button moveToBackButton;
//    Button moveToFrontButton;

    // THIRD ROW
    SplitPane row3Box;
    ComboBox<Station> fromCB;
    ComboBox<Station> toCB;
    Button findRouteButton;
//    Label backgroundColorLabel;
//    ColorPicker backgroundColorPicker;

    // FORTH ROW
    VBox row4Box;
    Label decorLabel;
    Button setBGCButton;
    Button setIBGButton;
    Button addImageButton;
    Button addLabelButton;
    Button removeElementButton;
//    Label fillColorLabel;
//    ColorPicker fillColorPicker;
    
    // FIFTH ROW
    VBox row5Box;
    Label fontLabel;
    Button fontColorButton;
    Button boldButton;
    Button italicsButton;
    ComboBox<Double> fontSizeCB;
    ComboBox<String> fontFamilyCB;
//    Label outlineColorLabel;
//    ColorPicker outlineColorPicker;
        
    // SIXTH ROW
    VBox row6Box;
    Label navigationLabel;
    CheckBox showGrid;
    Button zoomInButton;
    Button zoomOutButton;
    Button enlargeButton;
    Button compressButton;
//    Label outlineThicknessLabel;
//    Slider outlineThicknessSlider;
    
    // SEVENTH ROW
//    HBox row7Box;
//    Button snapshotButton;
    
    // THIS IS WHERE WE'LL RENDER OUR DRAWING, NOTE THAT WE
    // CALL THIS A CANVAS, BUT IT'S REALLY JUST A Pane
    Pane canvas;
    Double canvasInitW;
    Double canvasInitH;
    // HERE ARE THE CONTROLLERS
    CanvasController canvasController;
    mmmEditController mmmEditController;    

    // HERE ARE OUR DIALOGS
    AppMessageDialogSingleton messageyesNoCancelDialogDialog;
    AppYesNoCancelDialogSingleton yesNoCancelDialog;
    
    // FOR DISPLAYING DEBUG STUFF
    Text debugText;

    /**
     * Constructor for initializing the workspace, note that this constructor
     * will fully setup the workspace user interface for use.
     *
     * @param initApp The application this workspace is part of.
     *
     * @throws IOException Thrown should there be an error loading application
     * data for setting up the user interface.
     */
    public mmmWorkspace(AppTemplate initApp) {
	// KEEP THIS FOR LATER
	app = initApp;
        
	// KEEP THE GUI FOR LATER
	gui = app.getGUI();
        
        // LAYOUT THE APP
        initLayout();
        
        // HOOK UP THE CONTROLLERS
        initControllers();
        
        // AND INIT THE STYLE FOR THE WORKSPACE
        initStyle();    
    }
    
    /**
     * Note that this is for displaying text during development.
     */
    public void setDebugText(String text) {
	debugText.setText(text);
    }
    
    // ACCESSOR METHODS FOR COMPONENTS THAT EVENT HANDLERS
    // MAY NEED TO UPDATE OR ACCESS DATA FROM
    

    public Pane getCanvas() {
	return canvas;
    }
        
    // HELPER SETUP METHOD
    private void initLayout() {
	// THIS WILL GO IN THE LEFT SIDE OF THE WORKSPACE
	editToolbar = new VBox();
	// ROW 1
        Callback<ListView<MetroLine>, ListCell<MetroLine>> factoryLineCB = lv -> new ListCell<MetroLine>() {
            @Override
            protected void updateItem(MetroLine item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName1().getText());
            }
        };
        Callback<ListView<Station>, ListCell<Station>> factoryStationCB = lv -> new ListCell<Station>() {
            @Override
            protected void updateItem(Station item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName().getText());
            }
        };
        

        HBox row11=new HBox();
        HBox row12=new HBox();
        FlowPane row13=new FlowPane();
	row1Box = new VBox();
        row11.setSpacing(20);
        row11.setAlignment(Pos.CENTER_LEFT);
        row1Box.getChildren().addAll(row11,row12,row13);
        lineLabel=new Label("Metro Lines  ");
        lineCB = new ComboBox<>();
        lineCB.setPromptText("Metro Line");
        lineCB.setPrefWidth(130);
        lineCB.setCellFactory(factoryLineCB);
        lineCB.setButtonCell(factoryLineCB.call(null));
        row11.getChildren().addAll(lineLabel, lineCB);
        editLineButton=gui.initChildTextButton(row11, BLACK_HEX.toLowerCase(), "edit Line", false);
        editLineButton.setShape(new Circle(25));
        editLineButton.setMinSize(50, 50);
        editLineButton.setMaxSize(50, 50);
        editLineButton.setStyle("-fx-font: 10 arial;-fx-background-color: #000000; ");
        row12.setSpacing(8);
        addLineButton = gui.initChildButton(row12, ADD_ICON.toString(), ADDLINE_TOOLTIP.toString(), false);
        addLineButton.prefHeightProperty().bind(row12.heightProperty());
        removeLineButton=gui.initChildButton(row12, REMOVE_ICON.toString(), REMOVELINE_TOOLTIP.toString(), false);
        removeLineButton.prefHeightProperty().bind(row12.heightProperty());
        addStationToLineButton=gui.initChildTextButton(row12, "Add\nStation", ADDSTATIONTOLINE_TOOLTIP.toString(), true);               
        removeStationFromLineButton=gui.initChildTextButton(row12, "Remove\nStation", REMOVESTATIONFROMLINE_TOOLTIP.toString(), true);
        listStationsButton = gui.initChildButton(row12, LISTSTATIONS_ICON.toString(), LISTSTATIONS_TOOLTIP.toString(), true);
        listStationsButton.prefHeightProperty().bind(row12.heightProperty());
        lineThicknessSlider = new Slider(1, 10, 1);
        row13.getChildren().add(lineThicknessSlider);
        lineThicknessSlider.prefWidthProperty().bind(row13.widthProperty());
        
	// ROW 2
        HBox row21=new HBox();
        HBox row22=new HBox();
        FlowPane row23=new FlowPane();
	row2Box = new VBox();
        row21.setSpacing(15);
        row21.setAlignment(Pos.CENTER_LEFT);
        row2Box.getChildren().addAll(row21,row22,row23);
        stationLabel=new Label("Metro Stations");
        stationCB = new ComboBox<>();
        stationCB.setPromptText("Metro Station");
        stationCB.setPrefWidth(130);
        stationCB.setCellFactory(factoryStationCB);
        stationCB.setButtonCell(factoryStationCB.call(null));
        row21.getChildren().addAll(stationLabel, stationCB);
        editStationButton=gui.initChildTextButton(row21, WHITE_HEX.toLowerCase(), "edit Station", false);
        editStationButton.setShape(new Circle(25));
        editStationButton.setMinSize(50, 50);
        editStationButton.setMaxSize(50, 50);
        editStationButton.setStyle("-fx-font: 10 arial;-fx-background-color: #ffffff; ");
        row22.setSpacing(10);
        addStationButton = gui.initChildButton(row22, ADD_ICON.toString(), ADDSTATION_TOOLTIP.toString(), false);
        addStationButton.prefHeightProperty().bind(row22.heightProperty());
        removeStationButton=gui.initChildButton(row22, REMOVE_ICON.toString(), REMOVESTATION_TOOLTIP.toString(), false);
        removeStationButton.prefHeightProperty().bind(row22.heightProperty());
        snapButton=gui.initChildTextButton(row22, "Snap", ADDSTATIONTOLINE_TOOLTIP.toString(), true);        
        snapButton.prefHeightProperty().bind(row22.heightProperty());
        moveLabelButton=gui.initChildTextButton(row22, "Move\nLabel", REMOVESTATIONFROMLINE_TOOLTIP.toString(), true);
        rotateLabelButton = gui.initChildButton(row22, ROTATELABEL_ICON.toString(), ROTATELABEL_TOOLTIP.toString(), true);
        rotateLabelButton.prefHeightProperty().bind(row22.heightProperty());
        stationRadiusSlider = new Slider(1, 10, 1);
        row23.getChildren().add(stationRadiusSlider);
        stationRadiusSlider.prefWidthProperty().bind(row23.widthProperty());

	// ROW 3
	row3Box = new SplitPane();
        VBox row3left=new VBox();
        FlowPane row3right=new FlowPane();       
        fromCB=new ComboBox<>();
        fromCB.setPromptText("From");
        toCB=new ComboBox<>();
        toCB.setPromptText("To");  
        fromCB.setCellFactory(factoryStationCB);
        fromCB.setButtonCell(factoryStationCB.call(null));
        toCB.setCellFactory(factoryStationCB);
        toCB.setButtonCell(factoryStationCB.call(null));
        row3left.getChildren().addAll(fromCB, toCB);
        fromCB.prefWidthProperty().bind(row3left.widthProperty());
        toCB.prefWidthProperty().bind(row3left.widthProperty());
        findRouteButton=gui.initChildButton(row3right, FINDROUTE_ICON.toString(), FINDROUTE_TOOLTIP.toString(), false);
        findRouteButton.prefHeightProperty().bind(row3right.heightProperty());
        row3Box.getItems().addAll(row3left,row3right);
        row3Box.setDividerPositions(0.8);

	// ROW 4
	row4Box = new VBox();
        HBox row41=new HBox();
        HBox row42=new HBox();
        row4Box.getChildren().addAll(row41, row42);
        decorLabel=new Label("Decor");
        row41.setSpacing(210);
        row42.setSpacing(18);
        row41.setAlignment(Pos.CENTER_LEFT);
        row41.getChildren().addAll(decorLabel); 
        setBGCButton=gui.initChildTextButton(row41, WHITE_HEX.toLowerCase(), "set Blackground", false);
        setBGCButton.setShape(new Circle(25));
        setBGCButton.setMinSize(50, 50);
        setBGCButton.setMaxSize(50, 50);
        setBGCButton.setStyle("-fx-font: 10 arial;-fx-background-color: #ffffff; ");        
        setIBGButton=gui.initChildTextButton(row42, "Set Image\nBackground", SETIBG_TOOLTIP.toString(), false);
        addImageButton=gui.initChildTextButton(row42, "Add\nImage", ADDIMAGE_TOOLTIP.toString(), false);
        addLabelButton=gui.initChildTextButton(row42, "Add\nLabel", ADDLABEL_TOOLTIP.toString(), false);
        removeElementButton=gui.initChildTextButton(row42, "Remove\nElement", REMOVEELEMENT_TOOLTIP.toString(), true);
       
//	fillColorLabel = new Label("Fill Color");
//	fillColorPicker = new ColorPicker(Color.valueOf(WHITE_HEX));
//	row4Box.getChildren().add(fillColorLabel);
//	row4Box.getChildren().add(fillColorPicker);
	
	// ROW 5
	row5Box = new VBox();
        HBox row51=new HBox();
        HBox row52=new HBox();
        row5Box.getChildren().addAll(row51, row52);
        fontLabel=new Label("Font");
        row51.setAlignment(Pos.CENTER_LEFT);
        row51.setSpacing(220);
        row52.setSpacing(2);
        row51.getChildren().addAll(fontLabel);
        fontColorButton=gui.initChildTextButton(row51, WHITE_HEX.toLowerCase(), "set font Color", true);
        fontColorButton.setShape(new Circle(25));
        fontColorButton.setMinSize(50, 50);
        fontColorButton.setMaxSize(50, 50);
        fontColorButton.setStyle("-fx-font: 10 arial;-fx-background-color: #ffffff; ");     
        boldButton=gui.initChildButton(row52, BOLD_ICON.toString(), BOLD_TOOLTIP.toString(), true);
        italicsButton=gui.initChildButton(row52, ITALICS_ICON.toString(), ITALICS_TOOLTIP.toString(), true);
        fontSizeCB=new ComboBox<>();
        fontSizeCB.setDisable(true);
        for(Integer i=8;i<=48;i+=2){
            fontSizeCB.getItems().add(i.doubleValue());
        }
        fontSizeCB.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        fontFamilyCB=new ComboBox<>();
        fontFamilyCB.setDisable(true);
        fontFamilyCB.getItems().addAll("System","Arial","Times New Roman","Calibri","Verdana","Georgia");
        row52.getChildren().addAll(fontSizeCB, fontFamilyCB);
//	outlineColorLabel = new Label("Outline Color");
//	outlineColorPicker = new ColorPicker(Color.valueOf(BLACK_HEX));
//	row5Box.getChildren().add(outlineColorLabel);
//	row5Box.getChildren().add(outlineColorPicker);
	
	// ROW 6
	row6Box = new VBox();
        HBox row61=new HBox();
        HBox row62=new HBox();
        row6Box.getChildren().addAll(row61, row62);
        navigationLabel=new Label("Navigation");
        showGrid=new CheckBox("show Grid");
        row61.setSpacing(150);
        row62.setSpacing(30);
        row61.getChildren().addAll(navigationLabel, showGrid);       
        zoomInButton=gui.initChildButton(row62, ZOOMIN_ICON.toString(), SETIBG_TOOLTIP.toString(), false);
        zoomOutButton=gui.initChildButton(row62, ZOOMOUT_ICON.toString(), ADDIMAGE_TOOLTIP.toString(), false);
        compressButton=gui.initChildButton(row62, COMPRESS_ICON.toString(), COMPRESS_TOOLTIP.toString(), false);
        enlargeButton=gui.initChildButton(row62, ENLARGE_ICON.toString(), ENLARGE_TOOLTIP.toString(), false);

//	outlineThicknessLabel = new Label("Outline Thickness");
//	outlineThicknessSlider = new Slider(0, 10, 1);
//	row6Box.getChildren().add(outlineThicknessLabel);
//	row6Box.getChildren().add(outlineThicknessSlider);
	
	// ROW 7
//	row7Box = new HBox();
//	snapshotButton = gui.initChildButton(row7Box, SNAPSHOT_ICON.toString(), SNAPSHOT_TOOLTIP.toString(), false);
	
	// NOW ORGANIZE THE EDIT TOOLBAR
	editToolbar.getChildren().add(row1Box);
	editToolbar.getChildren().add(row2Box);
	editToolbar.getChildren().add(row3Box);
	editToolbar.getChildren().add(row4Box);
	editToolbar.getChildren().add(row5Box);
	editToolbar.getChildren().add(row6Box);
//	editToolbar.getChildren().add(row7Box);
	editToolbar.setPrefWidth(333);
	// WE'LL RENDER OUR STUFF HERE IN THE CANVAS
            
	canvas = new Pane();
	debugText = new Text();
	canvas.getChildren().add(debugText);
	debugText.setX(100);
	debugText.setY(100);
	
	// AND MAKE SURE THE DATA MANAGER IS IN SYNCH WITH THE PANE
	mmmData data = (mmmData)app.getDataComponent();
	data.setShapes(canvas.getChildren());

	// AND NOW SETUP THE WORKSPACE
	workspace = new BorderPane();
        Pane canvasWrap=new Pane(canvas);
        canvas.prefWidthProperty().bind(canvasWrap.widthProperty());
        canvas.prefHeightProperty().bind(canvasWrap.heightProperty());
        ((BorderPane)workspace).setCenter(canvasWrap);
        ((BorderPane)workspace).setLeft(new ScrollPane(editToolbar));
        
//        FlowPane temp=new FlowPane(canvas);
	
    }
    
    // HELPER SETUP METHOD
    private void initControllers() {
	// MAKE THE EDIT CONTROLLER
	mmmEditController = new mmmEditController(app);
        //r1  
        addLineButton.setOnAction(e->{
	    mmmEditController.processNewLine(lineCB);
	});
        removeLineButton.setOnAction(e->{
	    mmmEditController.processRemoveLine(lineCB);
	});
        lineCB.setOnAction(e->{
            if(lineCB.getValue()!=null){
            mmmEditController.processSelectLine(lineCB.getValue());
            editLineButton.setText(lineCB.getValue().getColorHex().toLowerCase());
            editLineButton.setStyle("-fx-font: 10 arial;-fx-background-color: "+lineCB.getValue().getColorHex()+"; ");
            }
	});
        addStationToLineButton.setOnAction(e->{
	    mmmEditController.processAddStationToLine();
	});
        removeStationFromLineButton.setOnAction(e->{
	    mmmEditController.processRemoveStationFromLine();
	});
        editLineButton.setOnAction(e->{
            if(lineCB.getValue()!=null)
	    mmmEditController.processEditLine(lineCB.getValue());
	});
        listStationsButton.setOnAction(e->{
            mmmEditController.processListStations();
	});
//        lineThicknessSlider.setOnMouseDragged(e->{
//            mmmEditController.processLineThicknessDragged(lineThicknessSlider.getValue());
//	});
        lineThicknessSlider.setOnMouseReleased(e->{
            mmmEditController.processLineThicknessReleased(lineThicknessSlider.getValue());
	});
        //r2
        stationCB.setOnAction(e->{
            if(stationCB.getValue()!=null){
//                System.out.println(c++);
                mmmEditController.processSelectStation(stationCB.getValue());
                editStationButton.setText(stationCB.getValue().getColorHex().toLowerCase());
                editStationButton.setStyle("-fx-font: 10 arial;-fx-background-color: "+stationCB.getValue().getColorHex()+"; ");
            }
	});
	addStationButton.setOnAction(e->{
	    mmmEditController.processNewStation(stationCB);
	});
        removeStationButton.setOnAction(e->{
	    mmmEditController.processRemoveStation(stationCB);
	});
        editStationButton.setOnAction(e->{
            if(stationCB.getValue()!=null)
                mmmEditController.processEditStation(stationCB.getValue());
	});
        snapButton.setOnAction(e->{
//            System.out.println(this.getCanvas().getWidth()+" w h "+this.getCanvas().getHeight());
            mmmEditController.processSnapToGrid();
	});
        moveLabelButton.setOnAction(e->{
            mmmEditController.processMoveStatonLabel();
	});
        this.rotateLabelButton.setOnAction(e->{
            mmmEditController.processRotateStatonLabel();
	});
        this.stationRadiusSlider.setOnMouseReleased(e->{
            mmmEditController.processStationRadiusReleased(stationRadiusSlider.getValue());
	});
        //r3
        findRouteButton.setOnAction(e->{
            if(fromCB.getValue()!=null&&toCB.getValue()!=null){
                mmmEditController.processFindRoute(fromCB.getValue(), toCB.getValue());
            }
	});
        //r4
        this.setBGCButton.setOnAction(e->{
            mmmEditController.processBGC();
	});
        this.setIBGButton.setOnAction(e->{
            mmmEditController.processIBG();
	});
        addImageButton.setOnAction(e->{
	    mmmEditController.processNewImage();
	});
        addLabelButton.setOnAction(e->{
            mmmEditController.processNewLabel();
	});
        removeElementButton.setOnAction(e->{
            mmmEditController.processRemoveElement();
	});
        
        //r5
        fontColorButton.setOnAction(e->{
            mmmEditController.processSelectTextFillColor();
	});	
        boldButton.setOnAction(e->{
            mmmEditController.processBold();
	});
        italicsButton.setOnAction(e->{
            mmmEditController.processItalics();
	});
        this.fontFamilyCB.setOnAction(e->{           
                if(action){
                    mmmEditController.processSelectedFontFamily(fontFamilyCB.getValue());}
	});
        this.fontSizeCB.setOnAction(e->{
                if(action){
                mmmEditController.processSelectedFontSize(fontSizeCB.getValue());
                    }                   
	});
        //r6
        zoomInButton.setOnAction(e->{
            canvas.setScaleX(canvas.getScaleX()+0.1);
            canvas.setScaleY(canvas.getScaleY()+0.1);
	});
        zoomOutButton.setOnAction(e->{
            canvas.setScaleX(canvas.getScaleX()-0.1);
            canvas.setScaleY(canvas.getScaleY()-0.1);
	});
        enlargeButton.setOnAction(e->{
            if(canvasInitW==null){
            canvasInitW=canvas.getWidth();
            canvasInitH=canvas.getHeight();
            }
            mmmEditController.processEnlarge(showGrid.isSelected());
        });
        compressButton.setOnAction(e->{
            if(canvasInitW==null){
            canvasInitW=canvas.getWidth();
            canvasInitH=canvas.getHeight();
            }
            mmmEditController.processCompress(showGrid.isSelected());
        });
        showGrid.setOnAction(e->{
            if(showGrid.isSelected())
                mmmEditController.processAddGrid(canvas.getWidth(), canvas.getHeight());
            else
                mmmEditController.processRemoveGrid();
        });
        gui.getPrimaryScene().setOnKeyPressed(e->{
            if (e.getCode() == KeyCode.A)
                canvas.setTranslateX(canvas.getTranslateX()-100);
            else if (e.getCode() == KeyCode.D) 
                canvas.setTranslateX(canvas.getTranslateX()+100);
            else if (e.getCode() == KeyCode.W) 
                canvas.setTranslateY(canvas.getTranslateY()-100);
            else if (e.getCode() == KeyCode.S) 
                canvas.setTranslateY(canvas.getTranslateY()+100);           
        });

//        canvas.setOnKeyPressed(new EventHandler<KeyEvent>()
//    {
//        @Override
//        public void handle(KeyEvent e)
//        {
//            System.out.println("Presss");
//            if (e.getCode() == KeyCode.A) {System.out.println("a");
//                canvas.setTranslateX(canvas.getTranslateX()-100);}
//            else if (e.getCode() == KeyCode.D) 
//                canvas.setTranslateX(canvas.getTranslateX()+100);
//            else if (e.getCode() == KeyCode.W) 
//                canvas.setTranslateX(canvas.getTranslateY()-100);
//            else if (e.getCode() == KeyCode.S) 
//                canvas.setTranslateX(canvas.getTranslateY()+100);           
//        }
//    });
    
	// NOW CONNECT THE BUTTONS TO THEIR HANDLERS
//	selectionToolButton.setOnAction(e->{
//	    mmmEditController.processSelectSelectionTool();
//	});
//	removeButton.setOnAction(e->{
//	    mmmEditController.processRemoveSelectedShape();
//	});
//	rectButton.setOnAction(e->{
//	    mmmEditController.processSelectRectangleToDraw();
//	});
//	ellipseButton.setOnAction(e->{
//	    mmmEditController.processSelectEllipseToDraw();
//	});
//	textButton.setOnAction(e->{
//	    mmmEditController.processSelectText();
//	});
//        fontFamily.setOnAction(e->{
//            if(!fontFamily.isDisable())
//                mmmEditController.processSelectedFontFamily(fontFamily.getValue());
//        });
//        fontSize.setOnAction(e->{
//            if(!fontSize.isDisable())
//                mmmEditController.processSelectedFontSize(fontSize.getValue());
//        });
//        boldButton.setOnAction(e->{
//            mmmEditController.processBold();
//        });
//        italicsButton.setOnAction(e->{
//            mmmEditController.processItalics();
//        });
//	moveToBackButton.setOnAction(e->{
//	    mmmEditController.processMoveSelectedShapeToBack();
//	});
//	moveToFrontButton.setOnAction(e->{
//	    mmmEditController.processMoveSelectedShapeToFront();
//	});
//	backgroundColorPicker.setOnAction(e->{
//	    mmmEditController.processSelectBackgroundColor();
//	});
//	fillColorPicker.setOnAction(e->{ 
//	    mmmEditController.processSelectFillColor();
//	});
//	outlineColorPicker.setOnAction(e->{
//	    mmmEditController.processSelectOutlineColor();
//	});
//	outlineThicknessSlider.valueProperty().addListener(e-> {
//	    mmmEditController.processSelectOutlineThickness();
//	});
//	snapshotButton.setOnAction(e->{
//	    mmmEditController.processSnapshot();
//	});
        gui.getUndoButton().setOnAction(e->{
            mmmEditController.processUndo();
        });
        gui.getRedoButton().setOnAction(e->{
            mmmEditController.processRedo();
        });
	// MAKE THE CANVAS CONTROLLER	
	canvasController = new CanvasController(app);
	canvas.setOnMousePressed(e->{
	    canvasController.processCanvasMousePress((int)e.getX(), (int)e.getY());
	});
	canvas.setOnMouseReleased(e->{
	    canvasController.processCanvasMouseRelease((int)e.getX(), (int)e.getY());
	});
	canvas.setOnMouseDragged(e->{
	    canvasController.processCanvasMouseDragged((int)e.getX(), (int)e.getY());
	});
    }

    // HELPER METHOD
    public void loadSelectedShapeSettings(Shape shape) {
	if (shape != null) {
	    Color fillColor = (Color)shape.getFill();
	    Color strokeColor = (Color)shape.getStroke();
	    double lineThickness = shape.getStrokeWidth();
//	    fillColorPicker.setValue(fillColor);
//	    outlineColorPicker.setValue(strokeColor);
//	    outlineThicknessSlider.setValue(lineThickness);	    
	}
    }
    
    public void loadSelectedTextSettings(DraggableText text) {
	if (text != null) {
            Color fillColor = (Color)text.getFill();
//            fillColorPicker.setValue(fillColor);
//            fontFamily.setDisable(true);
//            fontSize.setDisable(true);
//	    fontFamily.setValue(text.getFont().getFamily());           
//            fontSize.setValue(text.getFont().getSize());
//            fontSize.setDisable(false);
//            fontFamily.setDisable(false);
	}
    }

    /**
     * This function specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed. Note that the
     * tag editor controls are added and removed dynamicaly as the application
     * runs so they will have their style setup separately.
     */
    public void initStyle() {
	// NOTE THAT EACH CLASS SHOULD CORRESPOND TO
	// A STYLE CLASS SPECIFIED IN THIS APPLICATION'S
	// CSS FILE
	canvas.getStyleClass().add(CLASS_RENDER_CANVAS);
	
	// COLOR PICKER STYLE
//	fillColorPicker.getStyleClass().add(CLASS_BUTTON);
//	outlineColorPicker.getStyleClass().add(CLASS_BUTTON);
//	backgroundColorPicker.getStyleClass().add(CLASS_BUTTON);
	
	editToolbar.getStyleClass().add(CLASS_EDIT_TOOLBAR);
	row1Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
        row2Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);       
	row3Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
//	backgroundColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	
	row4Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
//	fillColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	row5Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
//	outlineColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
	row6Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
//	outlineThicknessLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
//	row7Box.getStyleClass().add(CLASS_EDIT_TOOLBAR_ROW);
    }

    /**
     * This function reloads all the controls for editing 
     * the workspace.
     */
    @Override
    public void reloadWorkspace(AppDataComponent data) {
	mmmData dataManager = (mmmData)data;
        if(dataManager.getSelectedShape() instanceof MetroLine){
            MetroLine l=(MetroLine)dataManager.getSelectedShape();
            lineCB.setValue(l);
            this.lineThicknessSlider.setValue(l.getStrokeWidth());
            editLineButton.setText(l.getColorHex().toLowerCase());
            editLineButton.setStyle("-fx-font: 10 arial;-fx-background-color: "+l.getColorHex()+"; ");
        }
        else if(dataManager.getSelectedShape() instanceof Station){
            Station s=(Station)dataManager.getSelectedShape();
            stationCB.setValue(s);
            this.stationRadiusSlider.setValue(s.getRadius());
            editStationButton.setText(s.getColorHex().toLowerCase());
            editStationButton.setStyle("-fx-font: 10 arial;-fx-background-color: "+s.getColorHex()+"; ");
        }
         else if(dataManager.getSelectedShape() instanceof DraggableText){
             DraggableText t=(DraggableText)dataManager.getSelectedShape();
             fontColorButton.setText(t.getColorHex());
            this.fontColorButton.setStyle("-fx-font: 10 arial;-fx-background-color: "+t.getColorHex()+"; ");
            action=false;
            this.fontFamilyCB.setValue(t.getFont().getFamily());
            this.fontSizeCB.setValue(t.getFont().getSize());
            action=true;
         }
	if (dataManager.isInState(mmmState.STARTING_RECTANGLE)) {
//	    selectionToolButton.setDisable(false);
//	    removeButton.setDisable(true);
//	    rectButton.setDisable(true);
//	    ellipseButton.setDisable(false);
//            imageButton.setDisable(false);
//            textButton.setDisable(false);
	}
	else if (dataManager.isInState(mmmState.STARTING_ELLIPSE)) {
//	    selectionToolButton.setDisable(false);
//	    removeButton.setDisable(true);
//	    rectButton.setDisable(false);
//	    ellipseButton.setDisable(true);
//            imageButton.setDisable(false);
//            textButton.setDisable(false);
	}
        else if (dataManager.isInState(mmmState.STARTING_IMAGE)) {
//	    selectionToolButton.setDisable(false);
//	    removeButton.setDisable(true);
//	    rectButton.setDisable(false);
//	    ellipseButton.setDisable(false);
//            imageButton.setDisable(false);
//            textButton.setDisable(false);
	}
        else if (dataManager.isInState(mmmState.STARTING_TEXT)) {
//	    selectionToolButton.setDisable(false);
//	    removeButton.setDisable(true);
//	    rectButton.setDisable(false);
//	    ellipseButton.setDisable(false);
//            imageButton.setDisable(false);
//            textButton.setDisable(false);
	}       
	else if (dataManager.isInState(mmmState.SELECTING_SHAPE) 
		|| dataManager.isInState(mmmState.DRAGGING_SHAPE)
		|| dataManager.isInState(mmmState.DRAGGING_NOTHING)) {
	    boolean shapeIsNotSelected = dataManager.getSelectedShape() == null;
//	    selectionToolButton.setDisable(true);
//	    removeButton.setDisable(shapeIsNotSelected);
//	    rectButton.setDisable(false);
//	    ellipseButton.setDisable(false);
//            imageButton.setDisable(false);
//            textButton.setDisable(false);
//	    moveToFrontButton.setDisable(shapeIsNotSelected);
//	    moveToBackButton.setDisable(shapeIsNotSelected);
            
	}
        int mostRecentTransaction= dataManager.getJTPS().getMostRecentTransaction();
        int size=dataManager.getJTPS().getTransactions().size();
//        System.out.println(mostRecentTransaction+"   "+size);
        gui.getUndoButton().setDisable(mostRecentTransaction<0);
        gui.getRedoButton().setDisable(mostRecentTransaction >= size-1);
        //MetroLine
        this.editLineButton.setDisable(dataManager.getSelectedMetroLine()==null);
        this.listStationsButton.setDisable(dataManager.getSelectedMetroLine()==null);
        this.lineThicknessSlider.setDisable(dataManager.getSelectedMetroLine()==null);
        addStationToLineButton.setDisable(dataManager.getSelectedStation()==null||dataManager.getSelectedMetroLine()==null
                ||dataManager.getSelectedStation().getMetroLines().contains(dataManager.getSelectedMetroLine()));
        removeStationFromLineButton.setDisable(dataManager.getSelectedStation()==null||dataManager.getSelectedMetroLine()==null
                ||!dataManager.getSelectedStation().getMetroLines().contains(dataManager.getSelectedMetroLine()));
        //Station
        this.editStationButton.setDisable(dataManager.getSelectedStation()==null);
//        System.out.println(++c+"ref");
        this.snapButton.setDisable(!(dataManager.getSelectedShape() instanceof Station)&&
        !(dataManager.getSelectedShape() instanceof DraggableText &&((DraggableText)dataManager.getSelectedShape()).getMetroLine()!=null));
        this.moveLabelButton.setDisable(dataManager.getSelectedStation()==null);
        this.rotateLabelButton.setDisable(dataManager.getSelectedStation()==null);
        this.stationRadiusSlider.setDisable(dataManager.getSelectedStation()==null);
        //Route
//        this.findRouteButton.setDisable(this.fromCB.getValue()==null||this.toCB.getValue()==null);
        //decor       
//        setBGCButton.setStyle("-fx-font: 10 arial;-fx-background-color: "+s.getColorHex()+"; ");
        removeElementButton.setDisable(!((dataManager.getSelectedShape() instanceof DraggableText&& 
                ((DraggableText)dataManager.getSelectedShape()).getStation()==null&&
                ((DraggableText)dataManager.getSelectedShape()).getMetroLine()==null)||
                dataManager.getSelectedShape() instanceof DraggableImage));
        this.fontColorButton.setDisable(!(dataManager.getSelectedShape() instanceof DraggableText));
	this.fontFamilyCB.setDisable(!(dataManager.getSelectedShape() instanceof DraggableText));
        this.fontSizeCB.setDisable(!(dataManager.getSelectedShape() instanceof DraggableText));
        boldButton.setDisable(!(dataManager.getSelectedShape() instanceof DraggableText));
        italicsButton.setDisable(!(dataManager.getSelectedShape() instanceof DraggableText));
//        fillColorPicker.setDisable(dataManager.getSelectedShape() instanceof ImageView||dataManager.getSelectedShape() == null);
//        outlineColorPicker.setDisable(dataManager.getSelectedShape() instanceof ImageView||dataManager.getSelectedShape() == null);
//        outlineThicknessSlider.setDisable(dataManager.getSelectedShape() instanceof ImageView||dataManager.getSelectedShape() == null);
//	removeButton.setDisable(dataManager.getSelectedShape() == null);
//	backgroundColorPicker.setValue(dataManager.getBackgroundColor());
        if(stationCB.getItems().size()!=fromCB.getItems().size()){
            for(Station s:stationCB.getItems()){
                if(!fromCB.getItems().contains(s)){
                    fromCB.getItems().add(s);
                    toCB.getItems().add(s);
                }
            }
            for(Station s:fromCB.getItems()){
                if(!stationCB.getItems().contains(s)){
                    fromCB.getItems().remove(s);
                    toCB.getItems().remove(s);
                }
            }
        }
    }
    @Override
    public void resetWorkspace() {
//        canvas.setMinSize(canvasWrap.getWidth(), canvasWrap.getHeight());
//        canvas.setMaxSize(canvasWrap.getWidth(), canvasWrap.getHeight());
        // WE ARE NOT USING THIS, THOUGH YOU MAY IF YOU LIKE
    }

    public ComboBox<MetroLine> getLineCB() {
        return lineCB;
    }

    public ComboBox<Station> getStationCB() {
        return stationCB;
    }

    public ComboBox<Station> getFromCB() {
        return fromCB;
    }

    public ComboBox<Station> getToCB() {
        return toCB;
    }
    
    public String colorToColorHex(Color c){
        return String.format( "#%02X%02X%02X",
            (int)( c.getRed() * 255 ),
            (int)( c.getGreen() * 255 ),
            (int)( c.getBlue() * 255 ) ); 
    }
    public void resetCanvas(){
        if(this.canvasInitW!=null){
            canvas.setMinSize(canvasInitW, canvasInitH);
            canvas.setMaxSize(canvasInitW, canvasInitH);
        }
        if(canvas.getScaleX()!=1){
            canvas.setScaleX(1);
            canvas.setScaleY(1);
        }
        canvas.setTranslateX(0);
        canvas.setTranslateY(0);
        this.showGrid.setSelected(false);
    }
    
}