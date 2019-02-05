package mmm.data;

import mmm.jtps.*;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import static mmm.data.mmmState.SELECTING_SHAPE;
import static mmm.data.mmmState.SIZING_SHAPE;
import mmm.gui.mmmWorkspace;
import djf.components.AppDataComponent;
import djf.AppTemplate;
import java.util.ArrayList;
import jtps.jTPS;
import jtps.jTPS_Transaction;
/**
 * This class serves as the data management component for this application.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class mmmData implements AppDataComponent {
    jTPS jtps=new jTPS();
    // FIRST THE THINGS THAT HAVE TO BE SAVED TO FILES
    
    // THESE ARE THE SHAPES TO DRAW
    ObservableList<Node> shapes;            //STORE ALL THE ELEMENTS IN THE LIST
    ArrayList<Line> grid;                   //STORE THE LINES THAT FORMS THE GRID
    final Double grid_Length=50.0;          
    // THE BACKGROUND COLOR
    Color backgroundColor;
    ImageView backgroundImage;
    // AND NOW THE EDITING DATA

    // THIS IS THE SHAPE CURRENTLY BEING SIZED BUT NOT YET ADDED
    Node newShape;
  
    Node selectedShape;                 // THIS IS THE SHAPE CURRENTLY SELECTED
    Station selectedStation;            // THIS IS THE STATION CURRENTLY SELECTED
    MetroLine selectedMetroLine;        // THIS IS THE METROLINE CURRENTLY SELECTED
    Node copiedShape;                   // THIS IS THE SHAPE IN THE CLIPBOARD
    //For font not necessary
    String currentFontFamily;
    Double currentFontSize;
    
    // FOR FILL AND OUTLINE
    Color currentFillColor;
    Color currentOutlineColor;
    double currentBorderWidth;

    // CURRENT STATE OF THE APP
    mmmState state;

    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    
    // USE THIS WHEN THE SHAPE IS SELECTED
    Effect highlightedEffect;

    public static final String WHITE_HEX = "#FFFFFF";
    public static final String BLACK_HEX = "#000000";
    public static final String YELLOW_HEX = "#EEEE00";
    public static final Paint DEFAULT_BACKGROUND_COLOR = Paint.valueOf(WHITE_HEX);
    public static final Paint HIGHLIGHTED_COLOR = Paint.valueOf(YELLOW_HEX);
    public static final int HIGHLIGHTED_STROKE_THICKNESS = 3;
    final Integer gridLength=50;
    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public mmmData(AppTemplate initApp) {
	// KEEP THE APP FOR LATER
	app = initApp;

	// NO SHAPE STARTS OUT AS SELECTED
	newShape = null;
	selectedShape = null;
        
	// INIT THE COLORS
	currentFillColor = Color.web(WHITE_HEX);
	currentOutlineColor = Color.web(BLACK_HEX);
	currentBorderWidth = 1;
	
	// THIS IS FOR THE SELECTED SHAPE
	DropShadow dropShadowEffect = new DropShadow();
	dropShadowEffect.setOffsetX(0.0f);
	dropShadowEffect.setOffsetY(0.0f);
	dropShadowEffect.setSpread(1.0);
	dropShadowEffect.setColor(Color.YELLOW);
	dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
	dropShadowEffect.setRadius(15);
	highlightedEffect = dropShadowEffect;
    }
    
    /**
     * @return all shapes in the pane
     */
    public ObservableList<Node> getShapes() {
	return shapes;
    }
    
    /**
     * @return the background color
     */
    public Color getBackgroundColor() {
	return backgroundColor;
    }

    /**
     * @return the background image
     */
    public ImageView getBackgroundImage() {
        return backgroundImage;
    }
    
    public Color getCurrentFillColor() {
	return currentFillColor;
    }

    public Color getCurrentOutlineColor() {
	return currentOutlineColor;
    }

    public double getCurrentBorderWidth() {
	return currentBorderWidth;
    }
    public void addLine(MetroLine l, ComboBox<MetroLine> cb){
        jTPS_Transaction tran=new AddLine(l, this, cb);
        jtps.addTransaction(tran);
    }
    public void removeLine(MetroLine l, ComboBox<MetroLine> cb){
        jTPS_Transaction tran=new RemoveLine(l, this, cb);
        jtps.addTransaction(tran);
    }
    public void editLine(MetroLine l, String n, String ch, boolean ccl){
        jTPS_Transaction tran=new EditLine(l, n, ch, ccl, this);
        jtps.addTransaction(tran);
    }
    public void editStation(Station s, String ch){
        jTPS_Transaction tran=new EditStation(s, ch);
        jtps.addTransaction(tran);
    }
    public void addStationToLine(){
        jTPS_Transaction tran=new AddStationToLine(this);
        jtps.addTransaction(tran);
    }
    public void removeStationFromLine(){
        jTPS_Transaction tran=new RemoveStationFromLine(this);
        jtps.addTransaction(tran);
    }
    
    public void snapStationToGrid(){
        Double oldX=this.selectedStation.getCenterX();
        Double oldY=this.selectedStation.getCenterY();
        Integer left=oldX.intValue()/gridLength*gridLength;
        Integer right=left+gridLength;
        Double x=(oldX-left<right-oldX)?left.doubleValue():right.doubleValue();
        Integer up=oldY.intValue()/gridLength*gridLength;
        Integer down=up+gridLength;
        Double y=(oldY-up<down-oldY)?up.doubleValue():down.doubleValue();
        selectedStation.drag(x.intValue(), y.intValue());
        jTPS_Transaction tran=new Drag((Station)selectedShape, oldX.intValue(),oldY.intValue());
        jtps.addTransaction(tran);
//        System.out.println("4."+selectedStation.getCenterX()+" p "+selectedStation.getCenterY());
    }
    
    public void snapLineEndToGrid(){
        DraggableText t=(DraggableText)this.getSelectedShape();
        Double tx=t.getX(), ty=t.getY();
        Double oldX,oldY;
        if (t.isHead()){
            oldX=t.getMetroLine().getPoints().get(0);
            oldY=t.getMetroLine().getPoints().get(1);
        }
        else{
            oldX=t.getMetroLine().getPoints().get(t.getMetroLine().getPoints().size()-2);
            oldY=t.getMetroLine().getPoints().get(t.getMetroLine().getPoints().size()-1);
        }
        Integer left=oldX.intValue()/gridLength*gridLength;
        Integer right=left+gridLength;
        Double x=(oldX-left<right-oldX)?left.doubleValue():right.doubleValue();
        Integer up=oldY.intValue()/gridLength*gridLength;
        Integer down=up+gridLength;
        Double y=(oldY-up<down-oldY)?up.doubleValue():down.doubleValue();
//        System.out.println("1. oldX "+oldX+"    oldY "+oldY+"    x "+x+" y "+y);
        if(t.isHead()){
//            x=x-t.getLayoutBounds().getWidth()-20;
//            oldX=oldX-t.getLayoutBounds().getWidth()-20;
            t.getMetroLine().setHeadPoint(x, y);
        }
        else{
//            x=x+20;
//            oldX+=20;
            t.getMetroLine().setTailPoint(x, y);
        }
        
//        t.drag(x.intValue(), y.intValue());
//        System.out.println(tx.intValue()+" tx ty "+ty.intValue());
        jTPS_Transaction tran=new Drag(t, tx.intValue(),ty.intValue());
        jtps.addTransaction(tran);
//        System.out.println("2. oldX "+oldX+"    oldY "+oldY+"    x "+x+" y "+y);
//        System.out.println("h: "+t.getMetroLine().getPoints().get(0)+" "+t.getMetroLine().getPoints().get(1));
//        System.out.println("t: "+t.getMetroLine().getPoints().get(t.getMetroLine().getPoints().size()-2)+" "+t.getMetroLine().getPoints().get(t.getMetroLine().getPoints().size()-1));
//        System.out.println("name: "+t.getX()+"  " +t.getY());
    }
    public void moveStationLabel(){
        jTPS_Transaction tran=new MoveStationLabel(selectedStation);
        jtps.addTransaction(tran);
    }
    public void rotateStationLabel(){
        jTPS_Transaction tran=new RotateStationLabel(selectedStation);
        jtps.addTransaction(tran);
    }
    public void setLineThickness(MetroLine l, double t){
        jTPS_Transaction tran=new LineThicknessChange(l, t);
        jtps.addTransaction(tran);
    }
    public void setStationRadius(Station s, double r){
        jTPS_Transaction tran=new StationRadiusChange(s,r);
        jtps.addTransaction(tran);
    }
    
    public void addStation(Station s, ComboBox<Station> cb){
        jTPS_Transaction tran=new AddStation(s, this, cb);
        jtps.addTransaction(tran);
    }
    public void removeStation(Station s, ComboBox<Station> cb){
        jTPS_Transaction tran=new RemoveStation(s, this, cb);
        jtps.addTransaction(tran);
    } 
    
    public void setShapes(ObservableList<Node> initShapes) {
	shapes = initShapes;
    }
    
    public void setBackgroundColor(Color initBackgroundColor) {
	backgroundColor = initBackgroundColor;
	mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
	Pane canvas = workspace.getCanvas();
	BackgroundFill fill = new BackgroundFill(backgroundColor, null, null);
	Background background = new Background(fill);
        jTPS_Transaction tran=new BackgroundChange(canvas, background);
        jtps.addTransaction(tran);
//	canvas.setBackground(background);
    }
    public void setBackgroundImage(ImageView iv){
        this.backgroundImage=iv;
    }
        public void doBackgroundImage(ImageView iv) {
        jTPS_Transaction tran=new IBG(this, iv);
        jtps.addTransaction(tran);
//            backgroundImage=image;
//	mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
//	Pane canvas = workspace.getCanvas();
//	BackgroundImage bImage = new BackgroundImage(image, null, null, null, null);
////	Background background = new Background(fill);
//        jTPS_Transaction tran=new BackgroundChange(canvas, bImage);
//        jtps.addTransaction(tran);
//	canvas.setBackground(background);
    }
    public void addNode(Node n){
        jTPS_Transaction tran=new Add(shapes, n);    
        jtps.addTransaction(tran);
    }
    public void removeNode(Node n){
        jTPS_Transaction tran=new Remove(shapes, n);    
        jtps.addTransaction(tran);
    }
    
    public void setCurrentFontFamily(String family) {
        currentFontFamily=family;
            jTPS_Transaction tran=new FontChange((Text)selectedShape, Font.font(family,((Text)selectedShape).getFont().getSize()));
            jtps.addTransaction(tran);
//            ((DraggableText)selectedShape).setFont(Font.font(font));
    }
    
    public void setCurrentFontSize(Double size) {
        currentFontSize=size;
        jTPS_Transaction tran=new FontChange((Text)selectedShape, Font.font(((Text)selectedShape).getFont().getFamily(), size));
        jtps.addTransaction(tran);
//        ((DraggableText)selectedShape).setFont(Font.font(size));
    }
    
    public void setCurrentFontBold() {
        jTPS_Transaction tran=new FontBold((DraggableText)selectedShape);
        jtps.addTransaction(tran);
    }    
    
    public void setCurrentFontItalics() {
        jTPS_Transaction tran=new FontItalics((DraggableText)selectedShape);
        jtps.addTransaction(tran);
    }

    public void setCurrentFillColor(Color initColor) {
	currentFillColor = initColor;
	if (selectedShape != null){
            jTPS_Transaction tran=new ColorFill((Shape)selectedShape, (Paint)initColor);
            jtps.addTransaction(tran);
//	    ((Shape)selectedShape).setFill(initColor);
        }
    }

    public void setCurrentOutlineColor(Color initColor) {
	currentOutlineColor = initColor;
	if (selectedShape != null) {
            jTPS_Transaction tran=new ColorOutline((Shape)selectedShape, (Paint)initColor);
            jtps.addTransaction(tran);
//	    ((Shape)selectedShape).setStroke(initColor);
	}
    }

    public void setCurrentOutlineThickness(int initBorderWidth) {
	currentBorderWidth = initBorderWidth;
	if (selectedShape != null) {
            jTPS_Transaction tran=new ColorOutlineThickness((Shape)selectedShape, initBorderWidth);
            jtps.addTransaction(tran);
//	    ((Shape)selectedShape).setStrokeWidth(initBorderWidth);
	}
    }
    
    public void removeSelectedShape() {
	if (selectedShape != null) {
            jTPS_Transaction tran=new Remove(shapes, selectedShape);
            jtps.addTransaction(tran);
	   // shapes.remove(selectedShape);
	    selectedShape = null;            
	}
    }
    
    public void copySelectedShape() {
//	if (selectedShape != null) {
//            copiedShape=((Node)((Draggable)selectedShape).Clone());
//	}
    }
    
    public void pasteShape(){
//        ((Draggable)copiedShape).setLocationAndSize(((Draggable)copiedShape).getX()+5, 
//                ((Draggable)copiedShape).getY()+5, ((Draggable)copiedShape).getWidth(), ((Draggable)copiedShape).getHeight());
//                jTPS_Transaction tran=new Add(shapes, (Node)((Draggable)copiedShape).Clone());    
//        jtps.addTransaction(tran);  
//        
    }
    
    public void moveSelectedShapeToBack() {           
	if (selectedShape != null) {
            jTPS_Transaction tran=new MoveBack(shapes, selectedShape);
            jtps.addTransaction(tran);
//	    shapes.remove(selectedShape);
//	    if (shapes.isEmpty()) {
//		shapes.add(selectedShape);
//	    }
//	    else {
//		ArrayList<Node> temp = new ArrayList<>();
//		temp.add(selectedShape);
//		for (Node node : shapes)
//		    temp.add(node);
//		shapes.clear();
//		for (Node node : temp)
//		    shapes.add(node);
//	    }
	}
    }
    
    public void moveSelectedShapeToFront() {
	if (selectedShape != null) {
            jTPS_Transaction tran=new MoveFront(shapes, selectedShape);
            jtps.addTransaction(tran);
//	    shapes.remove(selectedShape);
//	    shapes.add(selectedShape);
	}
    }
 
    /**
     * This function clears out the HTML tree and reloads it with the minimal
     * tags, like html, head, and body such that the user can begin editing a
     * page.
     */
    @Override
    public void resetData() {
        ((mmmWorkspace)app.getWorkspaceComponent()).getLineCB().getItems().clear();
        ((mmmWorkspace)app.getWorkspaceComponent()).getStationCB().getItems().clear();
        ((mmmWorkspace)app.getWorkspaceComponent()).getFromCB().getItems().clear();
        ((mmmWorkspace)app.getWorkspaceComponent()).getToCB().getItems().clear();
	setState(SELECTING_SHAPE);
	newShape = null;
	selectedShape = null;
        selectedStation = null;
        selectedMetroLine = null;
	// INIT THE COLORS
	currentFillColor = Color.web(WHITE_HEX);
	currentOutlineColor = Color.web(BLACK_HEX);
        setBackgroundColor(Color.valueOf(WHITE_HEX));
        this.setBackgroundImage(null);
	shapes.clear();
	((mmmWorkspace)app.getWorkspaceComponent()).getCanvas().getChildren().clear();
        ((mmmWorkspace)app.getWorkspaceComponent()).resetCanvas();
    }

    public void selectSizedShape() {
	if (selectedShape != null)
	    unhighlightShape(selectedShape);
	selectedShape = newShape;
	highlightShape(selectedShape);
	newShape = null;
	if (state == SIZING_SHAPE) {
	    state = ((Draggable)selectedShape).getStartingState();
	}
    }
    
    public void unhighlightShape(Node shape) {
	selectedShape.setEffect(null);
    }
    
    public void highlightShape(Node shape) {
	shape.setEffect(highlightedEffect);
    }
    
    public void startNewRectangle(int x, int y) {
	DraggableRectangle newRectangle = new DraggableRectangle();
	newRectangle.start(x, y);
	newShape = newRectangle;
	initNewShape();
    }

    public void startNewEllipse(int x, int y) {
	DraggableEllipse newEllipse = new DraggableEllipse();
	newEllipse.start(x, y);
	newShape = newEllipse;
	initNewShape();
    }

    public void startNewImage(int x, int y) {
	DraggableImage newImage = new DraggableImage();
	newImage.start(x, y);
	newShape = newImage;
	initNewShape();
    } 
     
    public void startNewText(int x, int y) {
//	DraggableText newText = new DraggableText();
//	newText.start(x, y);
//	newShape = newText;
//	initNewShape();
    } 
    public void addGrid(Double w, Double h){
        grid=new ArrayList<>();
            for(Double i=grid_Length; i<w; i+=grid_Length){
                grid.add(new Line(i,0,i,h));
            }
            for(Double i=grid_Length; i<h; i+=grid_Length){
                grid.add(new Line(0,i,w,i));
            }
        for(Line l:grid)
            l.setStroke(Color.LIGHTGRAY);
            if(getBackgroundImage()==null){
                for(Line l: grid)
                    getShapes().add(0, l);
            }
            else{
                for(Line l: grid)
                    getShapes().add(1, l);
            }
    }
    public void removeGrid(){
        for(Line l:this.grid){
            getShapes().remove(l);
        }
    }
    public void mapSizeChange(Pane cvs, double w, double h, boolean refresh){
        if(this.backgroundImage!=null){
            backgroundImage.setX(w/2-backgroundImage.getImage().getWidth()/2);
            backgroundImage.setY(h/2-backgroundImage.getImage().getHeight()/2);
        }
        jTPS_Transaction tran=new MapSizeChange(cvs, this, w, h, refresh);
        jtps.addTransaction(tran);
    }
    public void initNewShape() {
	// DESELECT THE SELECTED SHAPE IF THERE IS ONE
	if (selectedShape != null) {
	    unhighlightShape(selectedShape);
	    selectedShape = null;
	}

	// USE THE CURRENT SETTINGS FOR THIS NEW SHAPE
	mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
        if(!(newShape instanceof ImageView)){
//	((Shape)newShape).setFill(workspace.getFillColorPicker().getValue());
//	((Shape)newShape).setStroke(workspace.getOutlineColorPicker().getValue());
//	((Shape)newShape).setStrokeWidth(workspace.getOutlineThicknessSlider().getValue());
        }
	// ADD THE SHAPE TO THE CANVAS       
        jTPS_Transaction tran=new Add(shapes, newShape);    
        jtps.addTransaction(tran);        
        
//	shapes.add(newShape);

	// GO INTO SHAPE SIZING MODE
	state = mmmState.SIZING_SHAPE;
    }

    public Node getNewShape() {
	return newShape;
    }

    public Node getSelectedShape() {
	return selectedShape;
    }

    public Station getSelectedStation() {
        return selectedStation;
    }

    public MetroLine getSelectedMetroLine() {
        return selectedMetroLine;
    }
    
    public void setSelectedShape(Node initSelectedShape) {
        if(selectedShape!=null)
            this.unhighlightShape(selectedShape);
        if(initSelectedShape!=null)           
            this.highlightShape(initSelectedShape);
	selectedShape = initSelectedShape;
    }

    public Node selectTopShape(int x, int y) {
	Node shape = getTopShape(x, y);
	if (shape == selectedShape)
	    return shape;
        if(this.backgroundImage!=null&&shape==this.backgroundImage){
            return null;
        }
        if(shape instanceof Line)
            return selectedShape;
	if (selectedShape != null) {
	    unhighlightShape(selectedShape);
	}
	if (shape != null) {
	    highlightShape(shape);
            if(shape instanceof Shape&&!(shape instanceof Text)){
                mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
                workspace.loadSelectedShapeSettings((Shape)shape);
            }
            else if(shape instanceof Station){
                mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();

            }
            else if(shape instanceof Text){
//                mmmWorkspace workspace = (mmmWorkspace)app.getWorkspaceComponent();
//                workspace.loadSelectedTextSettings((DraggableText)shape);
            }
	}
	selectedShape = shape;
	if (shape instanceof Station) {
            selectedStation=(Station)shape;
//	    ((Draggable)shape).start(x, y);
	}
	if (shape instanceof MetroLine) {
            selectedMetroLine=(MetroLine)shape;
	}
	return shape;
    }

    public Node getTopShape(int x, int y) {
	for (int i = shapes.size() - 1; i >= 0; i--) {
	    Node shape = shapes.get(i);
	    if (shape.contains(x, y)) {
		return shape;
	    }
	}
	return null;
    }

    public void addShape(Node shapeToAdd) {
	shapes.add(shapeToAdd);       
    }
    public void addShape(Shape shapeToAdd) {
    shapes.add(shapeToAdd);       
    }

    public void removeShape(Node shapeToRemove) {        
	shapes.remove(shapeToRemove);
    }

    public mmmState getState() {
	return state;
    }

    public void setState(mmmState initState) {
	state = initState;
    }

    public boolean isInState(mmmState testState) {
	return state == testState;
    }
    
    public void undo(){
        jtps.undoTransaction();
    }
    
    public void redo(){
        jtps.doTransaction();
    }
    
    public jTPS getJTPS(){
        return jtps;
    }
    
    @Override
    public void resetJTPS(){
        jtps=new jTPS();
        app.getGUI().getUndoButton().setDisable(true);
        app.getGUI().getRedoButton().setDisable(true);
    }

    public void setSelectedStation(Station selectedStation) {
        this.selectedStation = selectedStation;
    }

    public void setSelectedMetroLine(MetroLine selectedMetroLine) {
        this.selectedMetroLine = selectedMetroLine;
    }

    public AppTemplate getApp() {
        return app;
    }
    
    public mmmWorkspace getWorkpsace(){
        return (mmmWorkspace)app.getWorkspaceComponent();
    }

}
