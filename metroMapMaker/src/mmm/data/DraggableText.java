package mmm.data;

import javafx.scene.text.Text;

/**
 *
 * @author Student
 */
public class DraggableText extends Text implements Draggable{
    double startX;
    double startY;
    Boolean Bold=false;
    Boolean Italics=false;
    Boolean head;
    MetroLine metroLine;
    Station station;
    String colorHex="#000000";
    
    public DraggableText(boolean head, MetroLine metroLine) {    
	setX(50.0);
	setY(50.0);
	setWrappingWidth(0.0);
//        setHeight(0.0);
	setOpacity(1.0);
	startX = 50.0;
	startY = 50.0;
        this.head=head;
        this.metroLine=metroLine;
    }
    public DraggableText(Station s){
        setX(50.0);
	setY(50.0);
	setWrappingWidth(0.0);
//        setHeight(0.0);
	setOpacity(1.0);
	startX = 50.0;
	startY = 50.0;
        this.station=s;
    }
    
    public DraggableText(String s){
        setX(50.0);
	setY(50.0);
	setWrappingWidth(0.0);
	setOpacity(1.0);
	startX = 50.0;
	startY = 50.0;
        this.setText(s);
    }
    
    @Override
    public mmmState getStartingState() {
	return mmmState.STARTING_RECTANGLE;
    }
    
    @Override
    public void start(int x, int y) {
	startX = x;
	startY = y;
    }
    
    @Override
    public void drag(int x, int y) {
	double diffX = x - startX;
	double diffY = y - startY;
	double newX = getX() + diffX;
	double newY = getY()+ diffY;
	xProperty().set(newX);
	yProperty().set(newY);
	startX = x;
	startY = y;
        if(this.metroLine!=null){
            if(head){
        metroLine.getPoints().set(0, metroLine.getPoints().get(0)+diffX);
        metroLine.getPoints().set(1, metroLine.getPoints().get(1)+diffY);
        }
        else{
        metroLine.getPoints().set(metroLine.getPoints().size()-2, metroLine.getPoints().get(metroLine.getPoints().size()-2)+diffX);
        metroLine.getPoints().set(metroLine.getPoints().size()-1, metroLine.getPoints().get(metroLine.getPoints().size()-1)+diffY);
        }
        }
    }

    public String cT(double x, double y) {
	return "(x,y): (" + x + "," + y + ")";
    }
    
    @Override
    public void size(int x, int y) {
	double width = x - getX();
//	widthProperty().set(width);
	double height = y - getY();
//	heightProperty().set(height);	
    }
    
    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
	xProperty().set(initX);
	yProperty().set(initY);
//	widthProperty().set(initWidth);
//	heightProperty().set(initHeight);
    }
    
    @Override
    public String getShapeType() {
	return TEXT;
    }
//    @Override
    public double getHeight(){return getWrappingWidth();};
    
//    @Override
    public double getWidth(){ return getWrappingWidth();};
    
//    public DraggableText Clone(){
//        DraggableText c=new DraggableText();
//        c.setText(this.getText());
//        c.setFill(this.getFill());
//        c.setStroke(this.getStroke());
//        c.setStrokeWidth(this.getStrokeWidth());
//        c.setFont(this.getFont());
//        c.setLocationAndSize(getX(), getY(), getWidth(), getHeight());
//        return c;
//    }
    
    public boolean isBold(){
        return Bold;
    }
    public boolean isItalics(){
        return Italics;
    }

    public void setBold(Boolean Bold) {
        this.Bold = Bold;
    }

    public void setItalics(Boolean Italics) {
        this.Italics = Italics;
    }
    
    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public MetroLine getMetroLine() {
        return metroLine;
    }

    public Station getStation() {
        return station;
    }

    public Boolean isHead() {
        return head;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }
    public void setSX(double startX) {
        this.startX = startX;
        setX(startX);
    }

    public void setSY(double startY) {
        this.startY = startY;
        setY(startY);
    }
}
