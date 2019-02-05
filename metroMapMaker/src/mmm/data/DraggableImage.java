/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.data;
import javafx.scene.image.ImageView;
/**
 *
 * @author Student
 */
public class DraggableImage extends ImageView implements Draggable{
    double startX;
    double startY;
    String path;
    boolean bg;
    public DraggableImage() {
	setX(0.0);
	setY(0.0);
//	setFitWidth(0.0);
//	setFitHeight(0.0);
	setOpacity(1.0);
	startX = 0.0;
	startY = 0.0;
        bg=false;
    }
    
    @Override
    public mmmState getStartingState() {
	return mmmState.STARTING_IMAGE;
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
	double newY = getY() + diffY;
	xProperty().set(newX);
	yProperty().set(newY);
	startX = x;
	startY = y;
    }
    
    public String cT(double x, double y) {
	return "(x,y): (" + x + "," + y + ")";
    }
    
    @Override
    public void size(int x, int y) {
//	double width = x - getX();
//	widthProperty().set(width);
//	double height = y - getY();
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
	return IMAGE;
    }
    
//    @Override
    public double getWidth(){return getImage().getWidth();};
    
//    @Override
    public double getHeight(){return getImage().getHeight();};
    
//    public DraggableImage Clone(){
//        DraggableImage c=new DraggableImage();
//        c.setImage(this.getImage());
////        c.startX=startX;
////        c.startY=startY;
//        c.setLocationAndSize(getX(), getY(), getWidth(), getHeight());
//        return c;
//    }
    
    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setBg(boolean bg) {
        this.bg = bg;
    }

    public boolean isBg() {
        return bg;
    }
    
}
