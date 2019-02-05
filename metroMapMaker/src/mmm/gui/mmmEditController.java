package mmm.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
//import javafx.scene.control.ComboBox;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.text.Text;
import javafx.scene.control.TextInputDialog;
import djf.AppTemplate;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.Node;
import javafx.scene.shape.Line;
//import javafx.scene.image.ImageView;
import java.awt.image.BufferedImage;
import java.util.Collections;
import javax.imageio.ImageIO;

import mmm.data.*;

import java.util.Optional;
import javafx.scene.layout.Region;

/**
 * This class responds to interactions with other UI logo editing controls.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class mmmEditController {
    AppTemplate app;
    mmmData dataManager;
    
    public mmmEditController(AppTemplate initApp) {
	app = initApp;
	dataManager = (mmmData)app.getDataComponent();
    }
    
    /**
     * This method handles the response for selecting either the
     * selection or removal tool.
     */
    public void processSelectSelectionTool() {
	// CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.DEFAULT);
	
	// CHANGE THE STATE
	dataManager.setState(mmmState.SELECTING_SHAPE);	
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
    }
    
    /**
     * This method handles a user request to remove the selected shape.
     */
    public void processRemoveSelectedShape() {
	// REMOVE THE SELECTED SHAPE IF THERE IS ONE
	dataManager.removeSelectedShape();
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
    }
    
    public void processMoveStatonLabel(){
        dataManager.moveStationLabel();
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(false);
    }
    public void processRotateStatonLabel(){
        dataManager.rotateStationLabel();
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(false);
    }
    
    public void processNewLine(ComboBox<MetroLine> lineCB){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add new line");
        dialog.setContentText("Please enter name of the line:");           
        // Traditional way to get the response value.
        ColorPicker cp=new ColorPicker(Color.BLACK);        
        cp.setPadding(new Insets(130,0,0,100));
        dialog.getDialogPane().getChildren().add(cp);
        Optional<String> result = dialog.showAndWait();        
        result.ifPresent(e->{                                   
            Color c = (Color) cp.getValue();
            String hex = String.format( "#%02X%02X%02X",
            (int)( c.getRed() * 255 ),
            (int)( c.getGreen() * 255 ),
            (int)( c.getBlue() * 255 ) );
            MetroLine l=new MetroLine(result.get(), hex);
            dataManager.addLine(l, lineCB);
//            ((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().add(l);
//            ((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().add(l.getName1());
//            ((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().add(l.getName2());
            
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();           
            workspace.reloadWorkspace(dataManager);
            refreshNodes();
            app.getGUI().updateToolbarControls(false);
        });

    }
    public void processRemoveLine(ComboBox<MetroLine> lineCB){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Remove Line Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you ok with deleting this metro line?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            MetroLine l=dataManager.getSelectedMetroLine();
            dataManager.removeLine(l, lineCB);
//            ((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().remove(l);
//            ((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().remove(l.getName1());
//            ((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().remove(l.getName2());
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
            workspace.reloadWorkspace(dataManager);
        }
    }
    
    public void processEditLine(MetroLine l){
        TextInputDialog dialog = new TextInputDialog(l.getName1().getText());
        dialog.setTitle("Edit Line");
        dialog.setContentText("Line name:");           
        // Traditional way to get the response value.
        ColorPicker cp=new ColorPicker(Color.valueOf(l.getColorHex()));   
        CheckBox cb=new CheckBox("Circular");
        cp.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        cb.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        cb.setSelected(l.isCircular());
//        cp.setPadding(new Insets(130,0,0,80));
//        cb.setPadding(new Insets(130,0,0,100));
        VBox vb=new VBox(cb,cp);
//        dialog.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
        dialog.getDialogPane().getChildren().remove(0, 2);
        dialog.getDialogPane().getChildren().add(vb);
        Optional<String> result = dialog.showAndWait();        
        result.ifPresent(e->{
            Color c = (Color) cp.getValue();
            String hex = String.format( "#%02X%02X%02X",
            (int)( c.getRed() * 255 ),
            (int)( c.getGreen() * 255 ),
            (int)( c.getBlue() * 255 ) );
            dataManager.editLine(l, result.get(), hex, cb.isSelected());
//            ((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().add(l);
//            ((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().add(l.getName1());
//            ((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().add(l.getName2());
            
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();           
            workspace.reloadWorkspace(dataManager);
        });
    }
    
    public void processAddStationToLine(){
        if(!dataManager.getSelectedMetroLine().getStations().contains(dataManager.getSelectedStation())){
            dataManager.addStationToLine();
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
            workspace.reloadWorkspace(dataManager);
            app.getGUI().updateToolbarControls(false);
        }
    }
    public void processRemoveStationFromLine(){
        if(dataManager.getSelectedMetroLine().getStations().contains(dataManager.getSelectedStation())){
            dataManager.removeStationFromLine();
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
            workspace.reloadWorkspace(dataManager);
            app.getGUI().updateToolbarControls(false);
        }
    }
    
    public void processListStations(){
        if(dataManager.getSelectedMetroLine()!=null){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Metro Map Maker - Metro Line Stops");
            alert.setHeaderText("Stops in "+dataManager.getSelectedMetroLine().getName1().getText());
            String stops="";
            for(Station s:dataManager.getSelectedMetroLine().getStations()){
                stops+=("• "+s.getName().getText()+"\n");
            }
            alert.setContentText(stops);
            alert.showAndWait();
        }
    }
    
//    public void processLineThicknessDragged(double t){
//        if(dataManager.getSelectedMetroLine()!=null){
//            dataManager.getSelectedMetroLine().setStrokeWidth(t);
//        }
//    }
    public void processLineThicknessReleased(double t){
        if(dataManager.getSelectedMetroLine()!=null){
            dataManager.setLineThickness(dataManager.getSelectedMetroLine(), t);
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
            workspace.reloadWorkspace(dataManager);
            app.getGUI().updateToolbarControls(false);
        }
    }
    public void processStationRadiusReleased(double t){
        if(dataManager.getSelectedStation()!=null){
            dataManager.setStationRadius(dataManager.getSelectedStation(), t);
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
            workspace.reloadWorkspace(dataManager);
            app.getGUI().updateToolbarControls(false);
        }
    }
    public void processNewStation(ComboBox<Station> stationCB){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add new station");
        dialog.setContentText("Please enter name of the station:");           
        // Traditional way to get the response value.
        ColorPicker cp=new ColorPicker();        
        cp.setPadding(new Insets(130,0,0,100));
        dialog.getDialogPane().getChildren().add(cp);
        Optional<String> result = dialog.showAndWait();        
        result.ifPresent(e->{
            Color c = (Color) cp.getValue();
            String hex = String.format( "#%02X%02X%02X",
            (int)( c.getRed() * 255 ),
            (int)( c.getGreen() * 255 ),
            (int)( c.getBlue() * 255 ) );            
            Station station=new Station(result.get(), hex);
            dataManager.addStation(station, stationCB);
//            ((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().add(station);
//            ((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().add(station.getName());
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
            workspace.reloadWorkspace(dataManager);
            app.getGUI().updateToolbarControls(false);
        });       
    }
    public void processRemoveStation(ComboBox<Station> stationCB){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Remove Station Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you ok with deleting this station?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Station s=dataManager.getSelectedStation();
            dataManager.removeStation(s, stationCB);
//            ((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().remove(s);
//            ((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().remove(s.getName());
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
            workspace.reloadWorkspace(dataManager);
        }
    }
    public void processEditStation(Station s){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Station Fill Color");          
        // Traditional way to get the response value.
        ColorPicker cp=new ColorPicker(Color.valueOf(s.getColorHex()));   
        cp.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        VBox vb=new VBox(cp);
        dialog.getDialogPane().getChildren().remove(3);
        dialog.getDialogPane().getChildren().remove(0, 2);
        dialog.getDialogPane().getChildren().add(vb);
        Optional<String> result = dialog.showAndWait();        
        result.ifPresent(e->{
            Color c = (Color) cp.getValue();
            String hex = String.format( "#%02X%02X%02X",
            (int)( c.getRed() * 255 ),
            (int)( c.getGreen() * 255 ),
            (int)( c.getBlue() * 255 ) );
            dataManager.editStation(s, hex);           
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();           
            workspace.reloadWorkspace(dataManager);
        });
    }
    
    public void processSnapToGrid(){
        if(dataManager.getSelectedShape() instanceof Station){
            dataManager.snapStationToGrid();
        }
        else if(dataManager.getSelectedShape() instanceof DraggableText &&((DraggableText)dataManager.getSelectedShape()).getMetroLine()!=null){
            dataManager.snapLineEndToGrid();
        }
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();           
            workspace.reloadWorkspace(dataManager);
    }
    //    public void processSelectLine(String name){
//        for(Node node:dataManager.getShapes()){           
//            if(node instanceof MetroLine){
//                if(((MetroLine)node).getName1().getText().equals(name)){
//                    dataManager.setSelectedMetroLine(((MetroLine)node));
//                    break;
//                }
//            }
//        }  
//        ((mmmWorkspace)app.getWorkspaceComponent()).reloadWorkspace(dataManager);
//    }
        public void processSelectLine(MetroLine l){
        dataManager.setSelectedMetroLine(l);
        dataManager.setSelectedShape(l);
        ((mmmWorkspace)app.getWorkspaceComponent()).reloadWorkspace(dataManager);
    }
    public void processSelectStation(Station s){
        dataManager.setSelectedStation(s);
        dataManager.setSelectedShape(s);
        ((mmmWorkspace)app.getWorkspaceComponent()).reloadWorkspace(dataManager);
    }
    
    public void processBGC(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Background Color");          
        // Traditional way to get the response value.
        ColorPicker cp=new ColorPicker(dataManager.getBackgroundColor());   
        cp.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        VBox vb=new VBox(cp);
        dialog.getDialogPane().getChildren().remove(3);
        dialog.getDialogPane().getChildren().remove(0, 2);
        dialog.getDialogPane().getChildren().add(vb);
        Optional<String> result = dialog.showAndWait();        
        result.ifPresent(e->{
            Color c = (Color) cp.getValue();
            processSelectBackgroundColor(c);
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();           
            workspace.reloadWorkspace(dataManager);
        });
    }
    public void processIBG(){
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = 
                new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extFilterjpg = 
                new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterPNG = 
                new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
        FileChooser.ExtensionFilter extFilterpng = 
                new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters()
                .addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);
        //Show open file dialog
            File file = fileChooser.showOpenDialog(null);
//            System.out.println(file.getPath());
            try {
                DraggableImage myImageView=new DraggableImage(){
                    @Override
                    public void drag(int x, int y){};
                };
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                myImageView.setImage(image);
                myImageView.setPath(file.getPath());
                dataManager.doBackgroundImage(myImageView);
                mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
                workspace.reloadWorkspace(dataManager);
                app.getGUI().updateToolbarControls(false);
            } catch (Exception ex) {
//                Logger.getLogger("GUI").log(Level.SEVERE, null, ex);
            }
        // CHANGE THE STATE
//        dataManager.setState(mmmState.STARTING_IMAGE);

        // ENABLE/DISABLE THE PROPER BUTTONS

    }
//    public void processCopySelectedShape() {
//	// Copy THE SELECTED SHAPE IF THERE IS ONE
//	dataManager.copySelectedShape();
//	
//	// ENABLE/DISABLE THE PROPER BUTTONS
//	mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
//	workspace.reloadWorkspace(dataManager);
//	//app.getGUI().updateToolbarControls(false);
//    }
//    
//    public void processPasteShape() {
//	// Copy THE SELECTED SHAPE IF THERE IS ONE
//	dataManager.pasteShape();
//	
//	// ENABLE/DISABLE THE PROPER BUTTONS
//	mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
//	workspace.reloadWorkspace(dataManager);
//	app.getGUI().updateToolbarControls(false);
//    }
    
    /**
     * This method processes a user request to start drawing a rectangle.
     */
    public void processSelectRectangleToDraw() {
	// CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.CROSSHAIR);
	
	// CHANGE THE STATE
	dataManager.setState(mmmState.STARTING_RECTANGLE);

	// ENABLE/DISABLE THE PROPER BUTTONS
	mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * This method provides a response to the user requesting to start
     * drawing an ellipse.
     */
//    public void processSelectEllipseToDraw() {
//	// CHANGE THE CURSOR
//	Scene scene = app.getGUI().getPrimaryScene();
//	scene.setCursor(Cursor.CROSSHAIR);
//	
//	// CHANGE THE STATE
//	dataManager.setState(mmmState.STARTING_ELLIPSE);
//
//	// ENABLE/DISABLE THE PROPER BUTTONS
//	mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
//	workspace.reloadWorkspace(dataManager);
//	app.getGUI().updateToolbarControls(false);
//    }
    
    public void processNewImage() {
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = 
                new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extFilterjpg = 
                new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterPNG = 
                new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
        FileChooser.ExtensionFilter extFilterpng = 
                new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters()
                .addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);
        //Show open file dialog
            File file = fileChooser.showOpenDialog(null);
//            System.out.println(file.getPath());
            try {
                DraggableImage myImageView=new DraggableImage();
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                myImageView.setImage(image);
                myImageView.setPath(file.getPath());
                dataManager.addNode(myImageView);
                mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
                workspace.reloadWorkspace(dataManager);
                app.getGUI().updateToolbarControls(false);
            } catch (Exception ex) {
//                Logger.getLogger("GUI").log(Level.SEVERE, null, ex);
            }
        // CHANGE THE STATE
//        dataManager.setState(mmmState.STARTING_IMAGE);

        // ENABLE/DISABLE THE PROPER BUTTONS
    }
    
    public void processNewLabel(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Label");
        dialog.setContentText("Please enter name of the label:");           
        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();        
        result.ifPresent(e->{
            dataManager.addNode(new DraggableText(result.get()));
//            ((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().add(station);
//            ((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().add(station.getName());
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
            workspace.reloadWorkspace(dataManager);
            app.getGUI().updateToolbarControls(false);
        });       
    }
    public void processRemoveElement(){
        dataManager.removeNode(dataManager.getSelectedShape());
        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
        app.getGUI().updateToolbarControls(false);
    }
//    public void processSelectText() {
//         DraggableText t=new DraggableText();
//         editingText(t);
//        ((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().add(t);
//        t.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            public void handle(MouseEvent click) {
//                if (click.getClickCount() == 2){
//                    editingText(t);
//            }
//            }          
//        });
//        // CHANGE THE STATE
//        dataManager.setState(mmmState.STARTING_TEXT);
//
//        // ENABLE/DISABLE THE PROPER BUTTONS
//        mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
//        workspace.reloadWorkspace(dataManager);
//	app.getGUI().updateToolbarControls(false);
//    }
    
    /**
     * This method processes a user request to move the selected shape
     * down to the back layer.
     */
//    public void processMoveSelectedShapeToBack() {
//	dataManager.moveSelectedShapeToBack();
//        ((mmmWorkspace)app.getWorkspaceComponent()).reloadWorkspace(dataManager);
//	app.getGUI().updateToolbarControls(false);
//    }
    
    /**
     * This method processes a user request to move the selected shape
     * up to the front layer.
     */
//    public void processMoveSelectedShapeToFront() {
//	dataManager.moveSelectedShapeToFront();
//        ((mmmWorkspace)app.getWorkspaceComponent()).reloadWorkspace(dataManager);
//	app.getGUI().updateToolbarControls(false);
//    }
    
    public void processSelectedFontFamily(String f) {
        if(f!=null){
            dataManager.setCurrentFontFamily(f);
            ((mmmWorkspace)app.getWorkspaceComponent()).reloadWorkspace(dataManager);
	    app.getGUI().updateToolbarControls(false);
        }
    }
    public void processSelectedFontSize(Double s) {
        if(s!=null){
            dataManager.setCurrentFontSize(s);
            ((mmmWorkspace)app.getWorkspaceComponent()).reloadWorkspace(dataManager);
	    app.getGUI().updateToolbarControls(false);
        }       
    }
    public void processBold() {
        dataManager.setCurrentFontBold();
        ((mmmWorkspace)app.getWorkspaceComponent()).reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
        
    }
    public void processItalics() {
        dataManager.setCurrentFontItalics();
        ((mmmWorkspace)app.getWorkspaceComponent()).reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * This method processes a user request to select a fill color for
     * a shape.
     */
    public void processSelectTextFillColor() {
	TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Color");          
        // Traditional way to get the response value.
        ColorPicker cp=new ColorPicker((Color)((Text)dataManager.getSelectedShape()).getFill());   
        cp.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        VBox vb=new VBox(cp);
        dialog.getDialogPane().getChildren().remove(3);
        dialog.getDialogPane().getChildren().remove(0, 2);
        dialog.getDialogPane().getChildren().add(vb);
        Optional<String> result = dialog.showAndWait();        
        result.ifPresent(e->{
            Color c = (Color) cp.getValue();
            dataManager.setCurrentFillColor(c);
            mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();           
            workspace.reloadWorkspace(dataManager);
        });
    }
    
    /**
     * This method processes a user request to select the outline
     * color for a shape.
     */
    public void processSelectOutlineColor() {
//	mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
////	Color selectedColor = workspace.getOutlineColorPicker().getValue();
//	if (selectedColor != null) {
//	    dataManager.setCurrentOutlineColor(selectedColor);
//	    app.getGUI().updateToolbarControls(false);
//            ((mmmWorkspace)app.getWorkspaceComponent()).reloadWorkspace(dataManager);
//	}    
    }
    
    /**
     * This method processes a user request to select the 
     * background color.
     */
    private void processSelectBackgroundColor(Color selectedColor) {
	mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
	if (selectedColor != null) {
	    dataManager.setBackgroundColor(selectedColor);
            ((mmmWorkspace)app.getWorkspaceComponent()).reloadWorkspace(dataManager);
	    app.getGUI().updateToolbarControls(false);
	}
    }
    
    
    
    /**
     * This method processes a user request to select the outline
     * thickness for shape drawing.
     */
    public void processSelectOutlineThickness() {
//	mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
//	int outlineThickness = (int)workspace.getOutlineThicknessSlider().getValue();
//	dataManager.setCurrentOutlineThickness(outlineThickness);
//        ((mmmWorkspace)app.getWorkspaceComponent()).reloadWorkspace(dataManager);
//	app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * This method processes a user request to take a snapshot of the
     * current scene.
     */
    public void processSnapshot() {
	mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
	Pane canvas = workspace.getCanvas();
	WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
	File file = new File("Logo.png");
	try {
	    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
	}
	catch(IOException ioe) {
	    ioe.printStackTrace();
	}
    }
    
    public void editingText(DraggableText t){
        TextInputDialog dialog = new TextInputDialog(t.getText());
        dialog.setTitle("Text");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter the Text:");       
        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(e->{              
        t.setText(result.get());
      
        });
        ((mmmWorkspace)app.getWorkspaceComponent()).reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
    }
    
    public void processEnlarge(boolean refresh){
        mmmWorkspace workspace=(mmmWorkspace)app.getWorkspaceComponent();
        Pane canvas=workspace.getCanvas();
        Double w=canvas.getWidth()+100;
        Double h=canvas.getHeight()+100;
        dataManager.mapSizeChange(canvas, w, h, refresh);
        ((mmmWorkspace)app.getWorkspaceComponent()).reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
            
    }
    public void processCompress(boolean refresh){
        mmmWorkspace workspace=(mmmWorkspace)app.getWorkspaceComponent();
        Pane canvas=workspace.getCanvas();
        Double w=canvas.getWidth()-100;
        Double h=canvas.getHeight()-100;
        if(w>=200&&h>=200)
            dataManager.mapSizeChange(canvas, w, h, refresh);
        ((mmmWorkspace)app.getWorkspaceComponent()).reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
    }

    public void processUndo(){
        dataManager.undo();
        ((mmmWorkspace)app.getWorkspaceComponent()).reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
    }
    public void processRedo(){
        dataManager.redo();
        ((mmmWorkspace)app.getWorkspaceComponent()).reloadWorkspace(dataManager);
	app.getGUI().updateToolbarControls(false);
    }
    public void processAddGrid(Double w, Double h){
        dataManager.addGrid(w, h);
    }
     public void processRemoveGrid(){
        dataManager.removeGrid();
    }
    public void refreshNodes(){
        ArrayList<Node> temp=new ArrayList<>();
            for(Node node: dataManager.getShapes()){               
                if(node instanceof Station||node instanceof Text){
                    temp.add(node);                   
                }
            }
            for(Node node: temp){
                dataManager.getShapes().remove(node);
                dataManager.getShapes().add(node);
            }
    }
    public String colorToColorHex(Color c){
        return String.format( "#%02X%02X%02X",
            (int)( c.getRed() * 255 ),
            (int)( c.getGreen() * 255 ),
            (int)( c.getBlue() * 255 ) ); 
    }
    
//    private Map<Station, Boolean> vis = new HashMap<Station, Boolean>();
//
//    private Map<Station, Station> prev = new HashMap<Station, Station>();
    
    private Map<Station, Station> map = new HashMap<>();
    private List<Station> list = new LinkedList<>();
    private Queue<Station> queue = new LinkedList<>();
    public void processFindRoute(Station start, Station end){
        Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Find route");
            alert.setHeaderText("Stops from "+start.getName().getText()+" to "+end.getName().getText());
        
        //BFS
        Station current = start;
        queue.add(current);
        map.put(start, null);
        while((current = queue.poll()) != null){
            if (current.equals(end)){              
                String stops = "Stops in between:\n"; 
                String str = ("• "+end.getName().getText());
//                System.out.println(map);
                while((end = map.get(end)) != null){
                    str = ("• "+end.getName().getText()+"\n") + str;
                }
                alert.setContentText(stops + str);
                alert.showAndWait();
                map.clear();
                list.clear();
                queue.clear();
                return;
            }else{
                for(Station nb: current.getNeighbors()){
                    if(!map.containsKey(nb)){
                        queue.add(nb);
                        map.put(nb, current);
                    }
                }
            }
        }
        map.clear();
        alert.setContentText("No path between these two stops.");
        alert.showAndWait();   
    }
}
