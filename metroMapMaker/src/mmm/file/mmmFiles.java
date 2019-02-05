package mmm.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;//
import java.io.OutputStream;//
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.*;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonString;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.io.File;
import mmm.data.mmmData;
import mmm.data.DraggableEllipse;
import mmm.data.*;
import mmm.data.Draggable;
import mmm.data.DraggableImage;
import mmm.data.DraggableText;
import static mmm.data.Draggable.*;
import java.math.BigDecimal;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javax.imageio.ImageIO;
import mmm.gui.mmmWorkspace;

/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class mmmFiles implements AppFileComponent {
    // FOR JSON LOADING
    static final String JSON_BG_COLOR = "background_color";
    static final String JSON_BG_IMAGE = "background_image";
    static final String JSON_COLOR = "color";
    static final String JSON_RED = "red";
    static final String JSON_GREEN = "green";
    static final String JSON_BLUE = "blue";
    static final String JSON_NAME = "name";
    static final String JSON_HEAD = "head";
    static final String JSON_TAIL = "tail";
    static final String JSON_LABEL = "label";
    static final String JSON_FONT = "font";
    static final String JSON_NAME_POSITION="name_position";
    static final String JSON_COLORHEX = "colorHex";
    static final String JSON_CIRCULAR="circular";
    static final String JSON_ALPHA = "alpha";
    static final String JSON_SHAPES = "shapes";
    static final String JSON_POINTS = "points";
    static final String JSON_RADIUS = "radius";
    static final String JSON_ROTATE = "rotate";
    static final String JSON_SHAPE = "shape";
    static final String JSON_STATIONS = "stations";
    static final String JSON_STATION_NAMES = "station_names";
    static final String JSON_LINES = "lines";
    static final String JSON_IMAGES = "images";
    static final String JSON_TYPE = "type";
    static final String JSON_X = "x";
    static final String JSON_Y = "y";
    static final String JSON_TEXT = "text_content";
    static final String JSON_WIDTH = "width";
    static final String JSON_HEIGHT = "height";
    static final String JSON_FILL_COLOR = "fill_color";
    static final String JSON_OUTLINE_COLOR = "outline_color";
    static final String JSON_OUTLINE_THICKNESS = "outline_thickness";
    static final String JSON_FONT_FAMILY="font_family";
    static final String JSON_FONT_SIZE = "font_size";
    static final String JSON_LOCATION="location";
    static final String JSON_ISBOLD="isBold";
    static final String JSON_PATH="path";
    static final String JSON_HASOUTLINE="hasOutline";
    static final String JSON_ISITALICS="isItalics";
    static final String DEFAULT_DOCTYPE_DECLARATION = "<!doctype html>\n";
    static final String DEFAULT_ATTRIBUTE_VALUE = "";
  
    
 
    /**
     * This method is for saving user work, which in the case of this
     * application means the data that together draws the metro map maker.
     * 
     * @param data The data management component for this application.
     * 
     * @param filePath Path (including file name/extension) to where
     * to save the data to.
     * 
     * @throws IOException Thrown should there be an error writing 
     * out data to the file.
     */
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	mmmData dataManager = (mmmData)data;
	
	// FIRST THE BACKGROUND COLOR
        String fileName=filePath.substring(filePath.lastIndexOf('\\')+1);
	Color bgColor = dataManager.getBackgroundColor();
	JsonObject bgColorJson = makeJsonColorObject(bgColor);
//        }
	// NOW BUILD THE JSON OBJCTS TO SAVE
        JsonArrayBuilder linesBuilder = Json.createArrayBuilder();
        JsonArrayBuilder stationsBuilder = Json.createArrayBuilder();
        JsonArrayBuilder imageBuilder = Json.createArrayBuilder();
        JsonArrayBuilder labelBuilder = Json.createArrayBuilder();
	ObservableList<Node> shapes = dataManager.getShapes();
	for (Node node : shapes) {
            if(node instanceof MetroLine){
                MetroLine shape = (MetroLine)node;
                ObservableList<Double> points=shape.getPoints();
                List<Station> stations=shape.getStations();                
                JsonObject shapeJson;               
                shapeJson = Json.createObjectBuilder()
                        .add(JSON_NAME, shape.getName1().getText())
                        .add(JSON_CIRCULAR, shape.isCircular())
                        .add(JSON_COLOR,makeJsonColorObject((Color)shape.getStroke()))
                        .add(JSON_COLORHEX, shape.getColorHex())
                        .add(JSON_POINTS, this.makeJsonPointObject(points))
                        .add(JSON_HEAD, this.makeFontAndColorObject((DraggableText)shape.getName1()))
                        .add(JSON_TAIL, this.makeFontAndColorObject((DraggableText)shape.getName2()))
                        .add(JSON_STATION_NAMES, this.makeJsonStationsObject(stations))
                        .build();
                linesBuilder.add(shapeJson);
            }
            if(node instanceof Station){
                Station shape = (Station)node;        
//                System.out.println(shape.getName().getRotate());
                JsonObjectBuilder builder=Json.createObjectBuilder();
                    builder.add(JSON_NAME, shape.getName().getText())
                    .add(JSON_X, shape.getCenterX())
                    .add(JSON_Y, shape.getCenterY())
                    .add(JSON_RADIUS, shape.getRadius())
                    .add(JSON_COLORHEX, shape.getColorHex())
                    .add(JSON_ROTATE, shape.getName().getRotate())
                    .add(JSON_LABEL, this.makeFontAndColorObject((DraggableText)shape.getName()))
                    .add(JSON_NAME_POSITION, shape.getNamePosition());
//                    if(shape.getMetroLine()!=null)
//                        builder.add("metroLine", shape.getMetroLine().getName1().getText());
//                    else
//                        builder.add("metroLine", "");
                stationsBuilder.add(builder.build());
            }                           
//            if((node instanceof DraggableRectangle)||node instanceof DraggableEllipse){
//                Shape shape = (Shape)node;
//                Draggable draggableShape = ((Draggable)shape);
//                String type = draggableShape.getShapeType();
////                double x = draggableShape.getX();
////                double y = draggableShape.getY();
////                double width = draggableShape.getWidth();
////                double height = draggableShape.getHeight();
//                JsonObject fillColorJson = makeJsonColorObject((Color)shape.getFill());
//                JsonObject shapeJson;
////                if(shape.getStroke()!=null){
//                    JsonObject outlineColorJson = makeJsonColorObject((Color)shape.getStroke());
//                    double outlineThickness = shape.getStrokeWidth();
//                    shapeJson = Json.createObjectBuilder()
//                        .add(JSON_TYPE, type)
////                        .add(JSON_X, x)
////                        .add(JSON_Y, y)
////                        .add(JSON_WIDTH, width)
////                        .add(JSON_HEIGHT, height)
//                        .add(JSON_FILL_COLOR, fillColorJson)
//                        .add(JSON_OUTLINE_COLOR, outlineColorJson)
//                        .add(JSON_OUTLINE_THICKNESS, outlineThickness).build();
////                }
////                else{
////                    shapeJson = Json.createObjectBuilder()
////                        .add(JSON_TYPE, type)
////                        .add(JSON_X, x)
////                        .add(JSON_Y, y)
////                        .add(JSON_WIDTH, width)
////                        .add(JSON_HEIGHT, height)
////                        .add(JSON_FILL_COLOR, fillColorJson).build();
////                }
//                
//                arrayBuilder.add(shapeJson);
//            }//
            else if(node instanceof DraggableText&&((DraggableText)node).getMetroLine()==null&&
                    ((DraggableText)node).getStation()==null){
                DraggableText draggableShape = ((DraggableText)node);
                String text=draggableShape.getText();
                double x = draggableShape.getX();
                double y = draggableShape.getY();
                String family=draggableShape.getFont().getFamily();
                double size=draggableShape.getFont().getSize();
                JsonObjectBuilder builder=Json.createObjectBuilder();
                    builder= Json.createObjectBuilder()
                            .add(JSON_TEXT, text)
                            .add(JSON_X, x)
                            .add(JSON_Y, y)
                            .add(JSON_FONT, this.makeFontAndColorObject(draggableShape));
                labelBuilder.add(builder.build());
            }
//                else{
//                    shapeJson = Json.createObjectBuilder()
//                            .add(JSON_TYPE, type)
//                            .add(JSON_X, x)
//                            .add(JSON_Y, y)
//                            .add(JSON_TEXT, text)
//                            .add(JSON_FONT_FAMILY, family)
//                            .add(JSON_FONT_SIZE, size)
//                            .add(JSON_ISBOLD, draggableShape.isBold()?"T":"F")
//                            .add(JSON_ISITALICS, draggableShape.isItalics()?"T":"F")
//                            .add(JSON_FILL_COLOR, fillColorJson)
//                            .add(JSON_HASOUTLINE, "N").build();
//                }
//                arrayBuilder.add(shapeJson);
//            }//
            else if(node instanceof DraggableImage&&node!=dataManager.getBackgroundImage()){                
                imageBuilder.add(this.makeImageObject((DraggableImage)node));
            }
	}
//	JsonArray shapesArray = arrayBuilder.build();
	
	// THEN PUT IT ALL TOGETHER IN A JsonObject
        JsonObjectBuilder builder= Json.createObjectBuilder()
                .add(JSON_NAME, fileName)
                .add(JSON_WIDTH, dataManager.getWorkpsace().getCanvas().getWidth())
                .add(JSON_HEIGHT, dataManager.getWorkpsace().getCanvas().getHeight())
		.add(JSON_BG_COLOR, bgColorJson);
                if((DraggableImage)dataManager.getBackgroundImage()!=null)
                    builder.add(JSON_BG_IMAGE, ((DraggableImage)dataManager.getBackgroundImage()).getPath());
                else
                   builder.add(JSON_BG_IMAGE, "");
		builder.add(JSON_LINES, linesBuilder)
                .add(JSON_STATIONS, stationsBuilder)
                .add(JSON_IMAGES, imageBuilder)
                .add(JSON_LABEL, labelBuilder);
	JsonObject dataManagerJSO=builder.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();
	// INIT THE WRITER
        if(!filePath.contains(".json"))
            filePath+=".json";
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    private JsonObject makeImageObject(DraggableImage draggableShape){
        if(draggableShape==null)
            return null;
                double x = draggableShape.getX();
                double y = draggableShape.getY();
                String path=draggableShape.getPath();
                JsonObject shapeJson = Json.createObjectBuilder()
                        .add(JSON_X, x)
                        .add(JSON_Y, y)
                        .add(JSON_PATH,path).build();
                return shapeJson;
    }
    private JsonObject makeFontAndColorObject(DraggableText t){
        DraggableText shape = t;
        DraggableText draggableShape = ((DraggableText)shape);
        String family=draggableShape.getFont().getFamily();
        double size=draggableShape.getFont().getSize();
        JsonObject fillColorJson = makeJsonColorObject((Color)shape.getFill());
        JsonObject shapeJson;
        shapeJson= Json.createObjectBuilder()
                .add(JSON_FONT_FAMILY, family)
                .add(JSON_FONT_SIZE, size)
                .add(JSON_ISBOLD, draggableShape.isBold()?"T":"F")
                .add(JSON_ISITALICS, draggableShape.isItalics()?"T":"F")
                .add(JSON_COLORHEX, shape.getColorHex()).build();
        return shapeJson;
    }
                
    private JsonObject makeJsonColorObject(Color color) {
	JsonObject colorJson = Json.createObjectBuilder()
		.add(JSON_RED, color.getRed())
		.add(JSON_GREEN, color.getGreen())
		.add(JSON_BLUE, color.getBlue())
		.add(JSON_ALPHA, color.getOpacity()).build();
	return colorJson;
    }
    
    private JsonArray makeJsonPointObject(ObservableList<Double> points){
        JsonArrayBuilder pointsJson = Json.createArrayBuilder();
        for(double point: points) {
             pointsJson.add(point);
        }
        return pointsJson.build();
    }
        private JsonArray makeJsonStationsObject(List<Station> stations){
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for(Station s: stations) {
             builder.add(s.getName().getText());
        }
        return builder.build();
    }
      
    /**
     * This method loads data from a JSON formatted file into the data 
     * management component and then forces the updating of the workspace
     * such that the user may edit the data.
     * 
     * @param data Data management component where we'll load the file into.
     * 
     * @param filePath Path (including file name/extension) to where
     * to load the data from.
     * 
     * @throws IOException Thrown should there be an error reading
     * in data from the file.
     */
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
	// CLEAR THE OLD DATA OUT
	mmmData dataManager = (mmmData)data;
//	dataManager.resetData();
	
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
	
	// LOAD THE BACKGROUND COLOR
	Color bgColor = loadColor(json, JSON_BG_COLOR);
	dataManager.setBackgroundColor(bgColor);
	Double w=this.getDataAsDouble(json, JSON_WIDTH);
        Double h=this.getDataAsDouble(json, JSON_HEIGHT);
        dataManager.getWorkpsace().getCanvas().setMinSize(w, h);
        dataManager.getWorkpsace().getCanvas().setMaxSize(w, h);
        if(!json.getJsonString(JSON_BG_IMAGE).equals("")){
            DraggableImage bgi = new DraggableImage(){
                @Override
                    public void drag(int x, int y){};
            };
            bgi.setImage(new Image("file:"+this.getDataAsString(json, JSON_BG_IMAGE)));
            bgi.setLocationAndSize(w/2-bgi.getImage().getWidth()/2, h/2-bgi.getImage().getHeight()/2, 0, 0);
            bgi.setPath(this.getDataAsString(json, JSON_BG_IMAGE));
            dataManager.getShapes().add(0, bgi);
            dataManager.setBackgroundImage(bgi);
        }
            
        //NOW LOAD ALL THE STATIONS
        JsonArray jsonStationArray = json.getJsonArray(JSON_STATIONS);
        for (int i = 0; i < jsonStationArray.size(); i++) {
	    JsonObject jsonShape = jsonStationArray.getJsonObject(i);
	    Station shape = loadStation(jsonShape);
	    dataManager.addShape(shape);
            dataManager.addShape(shape.getName());
            dataManager.getWorkpsace().getStationCB().getItems().add(shape);
	}
        
	// AND NOW LOAD ALL THE LINES
	JsonArray jsonLineArray = json.getJsonArray(JSON_LINES);
	for (int i = 0; i < jsonLineArray.size(); i++) {
	    JsonObject jsonShape = jsonLineArray.getJsonObject(i);
	    MetroLine shape = loadLine(jsonShape, dataManager);
	    dataManager.addShape(shape);
            if(!shape.isCircular()){
                dataManager.addShape(shape.getName1());
                dataManager.addShape(shape.getName2());
            }
            dataManager.getWorkpsace().getLineCB().getItems().add(shape);
	}
        //LOAD ALL THE IMAGES
        JsonArray jsonImageArray = json.getJsonArray(JSON_IMAGES);
        for (int i = 0; i < jsonImageArray.size(); i++){
            JsonObject obj=jsonImageArray.getJsonObject(i);
            DraggableImage img = new DraggableImage();
            img.setImage(new Image("file:"+this.getDataAsString(obj, JSON_PATH)));
            img.setLocationAndSize(getDataAsDouble(obj, JSON_X),getDataAsDouble(obj, JSON_Y), 0, 0);
            img.setPath(this.getDataAsString(jsonImageArray.getJsonObject(i), JSON_PATH));
            dataManager.addShape(img);
        }
        //LOAD ALL THE TEXT LABEL
        JsonArray jsonLabelArray = json.getJsonArray(JSON_LABEL);
        for (int i = 0; i < jsonLabelArray.size(); i++){
            JsonObject label=jsonLabelArray.getJsonObject(i);
            DraggableText t=new DraggableText(this.getDataAsString(label, JSON_TEXT));
            JsonObject font=label.getJsonObject(JSON_FONT);
            this.changeFont(label, (DraggableText)t);
            dataManager.addShape(t);
        }
        //station and Text on Top && record neighbors
        ArrayList<Node> temp=new ArrayList<>();
        for(Node node: dataManager.getShapes()){
            if(node instanceof Station||node instanceof Text){
                temp.add(node);
            }
            //record neighors
            if(node instanceof MetroLine){
                MetroLine l = (MetroLine) node;
                List<Station> list = l.getStations();
                for(int i = 1; i < list.size(); i++){
                    list.get(i-1).getNeighbors().add(list.get(i));
                    list.get(i).getNeighbors().add(list.get(i-1));
                }
            }
        }
        for(Node node: temp){
            dataManager.getShapes().remove(node);
            dataManager.getShapes().add(node);
        }
        
//        for(int i=0;i<size; i++){
//            if(dataManager.getShapes().get(i) instanceof Station){
//                dataManager.addShape(((Station)dataManager.getShapes().get(i)).getName());
//            }
//            else if(dataManager.getShapes().get(i) instanceof MetroLine){
//                dataManager.addShape(((MetroLine)dataManager.getShapes().get(i)).getName1());
//                dataManager.addShape(((MetroLine)dataManager.getShapes().get(i)).getName2());
//            }
//        }
    }
    private Integer getDataAsInteger(JsonObject json, String dataName) {
	JsonValue value = json.get(dataName);
	JsonNumber number = (JsonNumber)value;
	return number.bigDecimalValue().intValue();	
    }
    private double getDataAsDouble(JsonObject json, String dataName) {
	JsonValue value = json.get(dataName);
	JsonNumber number = (JsonNumber)value;
	return number.bigDecimalValue().doubleValue();	
    }
    
    private String getDataAsString(JsonObject json, String dataName) {
	JsonValue value = json.get(dataName);
	JsonString str = (JsonString)value;
        if(str==null)
            return "";
	return str.getString();	
    }
    
    private void changeFont(JsonObject obj, DraggableText t){
        String family=this.getDataAsString(obj, JSON_FONT_FAMILY);
            Double size=this.getDataAsDouble(obj, JSON_FONT_SIZE);
            String bold=this.getDataAsString(obj, JSON_ISBOLD);
            String italics=this.getDataAsString(obj, JSON_ISITALICS);
            String ch=this.getDataAsString(obj, JSON_COLORHEX);
            t.setFill(Color.valueOf(ch));
            t.setColorHex(ch);
        if(bold.equals("T")&&italics.equals("T")){
            t.setFont(Font.font(family, FontWeight.BOLD, FontPosture.ITALIC, size));
            t.setBold(true);    t.setItalics(true);
        }
        else if(bold.equals("F")&&italics.equals("F")){
            t.setFont(Font.font(family, FontWeight.NORMAL, FontPosture.REGULAR, size));
            t.setBold(false);    t.setItalics(false);
        }
        else if(bold.equals("T")&&italics.equals("F")){
            t.setFont(Font.font(family, FontWeight.BOLD, FontPosture.REGULAR, size));
            t.setBold(true);    t.setItalics(false);
        }
        else{
            t.setFont(Font.font(family, FontWeight.NORMAL, FontPosture.ITALIC, size));
            t.setBold(false);    t.setItalics(true);
        }
    }
    
    private DraggableImage loadImage(JsonObject jsonShape){
            double x = getDataAsDouble(jsonShape, JSON_X);
            double y = getDataAsDouble(jsonShape, JSON_Y);
            DraggableImage draggableShape =new DraggableImage();
            draggableShape.setImage(new Image("file:"+this.getDataAsString(jsonShape, JSON_PATH)));
            draggableShape.setLocationAndSize(x, y, 0, 0);
            draggableShape.setPath(this.getDataAsString(jsonShape, JSON_PATH));
            return draggableShape;
    }
    
    private MetroLine loadLine(JsonObject jsonShape, mmmData dataManager){
        String name=this.getDataAsString(jsonShape, JSON_NAME);
        MetroLine line=new MetroLine(name, this.getDataAsString(jsonShape, JSON_COLORHEX));       
        this.changeFont(jsonShape.getJsonObject(JSON_HEAD),(DraggableText)line.getName1());
        this.changeFont(jsonShape.getJsonObject(JSON_TAIL),(DraggableText)line.getName2());
        line.getPoints().clear();
        JsonArray pointArray = jsonShape.getJsonArray(JSON_POINTS);
        for(int i=0; i<pointArray.size(); i++){               
            line.getPoints().add(((JsonNumber)pointArray.get(i)).bigDecimalValue().doubleValue());
        }
        line.Name1position();
        line.Name2position();
        JsonArray stationArray = jsonShape.getJsonArray(JSON_STATION_NAMES);
            for(int i=0; i<stationArray.size(); i++){
                String stationName=stationArray.getString(i);
                for(int j=0;j<dataManager.getShapes().size(); j++){             
                    if(dataManager.getShapes().get(j) instanceof Station){
                        if(((Station)dataManager.getShapes().get(j)).getName().getText().equals(stationName)){
                            ((Station)dataManager.getShapes().get(j)).getMetroLines().add(line);
                            line.getStations().add(((Station)dataManager.getShapes().get(j)));
//                            break;
                        }
                    }
                }
            }
            if(jsonShape.getBoolean(JSON_CIRCULAR)==true){
            line.setCircular(true);
            dataManager.editLine(line, line.getName1().getText(), line.getColorHex(), true);
        }
        return line;
    }
    
    private Station loadStation(JsonObject jsonShape){
        Station station=new Station(this.getDataAsString(jsonShape, JSON_NAME), this.getDataAsString(jsonShape, JSON_COLORHEX));
            station.setCenterX(this.getDataAsDouble(jsonShape, JSON_X));
            station.setCenterY(this.getDataAsDouble(jsonShape, JSON_Y));
            station.setRadius(this.getDataAsDouble(jsonShape, JSON_RADIUS));
            station.getName().setRotate(this.getDataAsDouble(jsonShape, JSON_ROTATE));
            JsonObject label=jsonShape.getJsonObject(JSON_LABEL);
            this.changeFont(label, (DraggableText)station.getName());
            station.setNamePositionValue(this.getDataAsInteger(jsonShape, JSON_NAME_POSITION));
            station.setNamePosition();
            station.setRadius(this.getDataAsDouble(jsonShape, JSON_RADIUS));
        return station;
    }
    private Node loadShape(JsonObject jsonShape, mmmData dataManager) {
	// FIRST BUILD THE PROPER SHAPE TYPE
	String type = jsonShape.getString(JSON_TYPE);
	Node shape;
        if(type.equals("MetroLine")){
            String name=this.getDataAsString(jsonShape, JSON_NAME);
            shape=new MetroLine(name, this.getDataAsString(jsonShape, JSON_COLORHEX));
            ((MetroLine)shape).getPoints().clear();
            JsonArray pointArray = jsonShape.getJsonArray(JSON_POINTS);
            for(int i=0; i<pointArray.size(); i++){               
                ((MetroLine)shape).getPoints().add(((JsonNumber)pointArray.get(i)).bigDecimalValue().doubleValue());
            }
        }
        else {
            
            shape=new Station(this.getDataAsString(jsonShape, JSON_NAME), this.getDataAsString(jsonShape, JSON_COLORHEX));
            ((Station)shape).setCenterX(this.getDataAsDouble(jsonShape, JSON_X));
            ((Station)shape).setCenterY(this.getDataAsDouble(jsonShape, JSON_Y));
            ((Station)shape).setRadius(this.getDataAsDouble(jsonShape, "radius"));
            MetroLine l;
            String name=this.getDataAsString(jsonShape, "MetroLine");
            if(!name.equals("")){
                for(int i=0; i<dataManager.getShapes().size(); i++){
                    if(dataManager.getShapes().get(i) instanceof MetroLine){
                        if(((MetroLine)dataManager.getShapes().get(i)).getName1().equals(name)){
                            l=(MetroLine)dataManager.getShapes().get(i);
                            ((MetroLine)dataManager.getShapes().get(i)).getStations().add((Station)shape);
                            ((Station)shape).getMetroLines().add(l);
                            break;
                        }
                    }
                }               
            }
        }
	if (type.equals(RECTANGLE)) {
	    shape = new DraggableRectangle();
	}
	else if (type.equals(ELLIPSE)){
	    shape = new DraggableEllipse();
	}
        else if(type.equals(TEXT)){
//            shape = new DraggableText();
        }
        else if(type.equals(IMAGE)){
            shape = new DraggableImage();
        }
//	if(type.equals(RECTANGLE)||type.equals(ELLIPSE)){
//            // THEN LOAD ITS FILL AND OUTLINE PROPERTIES
//            Color fillColor = loadColor(jsonShape, JSON_FILL_COLOR);
//            Color outlineColor = loadColor(jsonShape, JSON_OUTLINE_COLOR);
//            double outlineThickness = getDataAsDouble(jsonShape, JSON_OUTLINE_THICKNESS);
//            ((Shape)shape).setFill(fillColor);
//            ((Shape)shape).setStroke(outlineColor);
//            ((Shape)shape).setStrokeWidth(outlineThickness);
//
//            // AND THEN ITS DRAGGABLE PROPERTIES
//            double x = getDataAsDouble(jsonShape, JSON_X);
//            double y = getDataAsDouble(jsonShape, JSON_Y);
//            double width = getDataAsDouble(jsonShape, JSON_WIDTH);
//            double height = getDataAsDouble(jsonShape, JSON_HEIGHT);
//            Draggable draggableShape = (Draggable)shape;
//            draggableShape.setLocationAndSize(x, y, width, height);
//        }
        if(type.equals(TEXT)){
            shape=new Text();
            Color fillColor = loadColor(jsonShape, JSON_FILL_COLOR);
            ((DraggableText)shape).setFill(fillColor);
            if(getDataAsString(jsonShape, JSON_HASOUTLINE).equals("Y")){
                Color outlineColor = loadColor(jsonShape, JSON_OUTLINE_COLOR);
                double outlineThickness = getDataAsDouble(jsonShape, JSON_OUTLINE_THICKNESS);
                ((DraggableText)shape).setStroke(outlineColor);
                ((DraggableText)shape).setStrokeWidth(outlineThickness);
            }
            // AND THEN ITS DRAGGABLE PROPERTIES
            double x = getDataAsDouble(jsonShape, JSON_X);
            double y = getDataAsDouble(jsonShape, JSON_Y);
            String content=getDataAsString(jsonShape, JSON_TEXT);
            String family = getDataAsString(jsonShape, JSON_FONT_FAMILY);
            double size = getDataAsDouble(jsonShape, JSON_FONT_SIZE);
            String bold = getDataAsString(jsonShape, JSON_ISBOLD);
            String italics = getDataAsString(jsonShape, JSON_ISITALICS);
            DraggableText draggableShape = (DraggableText)shape;
            draggableShape.setLocationAndSize(x, y, 0, 0);
            draggableShape.setText(content);
            if(bold.equals("T")&&italics.equals("T"))
                draggableShape.setFont(Font.font(family, FontWeight.BOLD, FontPosture.ITALIC, size));
            if(bold.equals("F")&&italics.equals("F"))
                draggableShape.setFont(Font.font(family, FontWeight.NORMAL, FontPosture.REGULAR, size));
            if(bold.equals("T")&&italics.equals("F"))
                draggableShape.setFont(Font.font(family, FontWeight.BOLD, FontPosture.REGULAR, size));
            if(bold.equals("F")&&italics.equals("T"))
                draggableShape.setFont(Font.font(family, FontWeight.NORMAL, FontPosture.ITALIC, size));
        }
        if(type.equals(IMAGE)){
            // AND THEN ITS DRAGGABLE PROPERTIES
            double x = getDataAsDouble(jsonShape, JSON_X);
            double y = getDataAsDouble(jsonShape, JSON_Y);
            DraggableImage draggableShape = (DraggableImage)shape;
            draggableShape.setImage(new Image("file:"+this.getDataAsString(jsonShape, JSON_PATH)));
            draggableShape.setLocationAndSize(x, y, 0, 0);
            draggableShape.setPath(this.getDataAsString(jsonShape, JSON_PATH));
        }
	// ALL DONE, RETURN IT
	return shape;
    }
    
    private Color loadColor(JsonObject json, String colorToGet) {
	JsonObject jsonColor = json.getJsonObject(colorToGet);
	double red = getDataAsDouble(jsonColor, JSON_RED);
	double green = getDataAsDouble(jsonColor, JSON_GREEN);
	double blue = getDataAsDouble(jsonColor, JSON_BLUE);
	double alpha = getDataAsDouble(jsonColor, JSON_ALPHA);
	Color loadedColor = new Color(red, green, blue, alpha);
	return loadedColor;
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    /**
     * This method is provided to satisfy the compiler, but it
     * is not used by this application.
     */
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        // WE ARE NOT USING THIS, THOUGH PERHAPS WE COULD FOR EXPORTING
        // IMAGES TO VARIOUS FORMATS, SOMETHING OUT OF THE SCOPE
        // OF THIS ASSIGNMENT
        saveData(data, filePath+" Metro");
        mmmData dataManager = (mmmData)data;
        	mmmWorkspace workspace = dataManager.getWorkpsace();
	Pane canvas = workspace.getCanvas();
	WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
	File file = new File(filePath+" Metro.png");
	try {
	    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
	}
	catch(IOException ioe) {
	    ioe.printStackTrace();
	}
    }
    
    /**
     * This method is provided to satisfy the compiler, but it
     * is not used by this application.
     */
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
	// AGAIN, WE ARE NOT USING THIS IN THIS ASSIGNMENT
    }
}
