/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.data;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.text.Text;
/**
 *
 * @author Student
 */
public class Station extends Circle implements Draggable {
    final double space=5;
    double startCenterX;
    double startCenterY;
    int namePosition;
    Text name;
    String colorHex;
    List<MetroLine> metroLines = new ArrayList<>();
    List<Station> neighbors = new ArrayList<>();
    boolean visited=false;
    public Station(String n, String colorHex) {
	setCenterX(100.0);
	setCenterY(120.0);
	this.setRadius(5.0);
	setOpacity(1.0);
        namePosition=1;
	startCenterX = 0.0;
	startCenterY = 0.0;
        this.name=new DraggableText(this){
            @Override
            public void drag(int x, int y){        
            }
        };
        this.name.setText(n);
        this.setStrokeWidth(1);
        this.colorHex=colorHex;
        this.setFill(Color.valueOf(colorHex));
        this.setStroke(Color.valueOf("#000000"));       
        setNamePosition();
    }
    
    @Override
    public mmmState getStartingState() {
	return mmmState.STARTING_STATION;
    }
    
    @Override
    public void start(int x, int y) {
	startCenterX = x;
	startCenterY = y;
    }
    
    @Override
    public void drag(int x, int y) {
	double diffX = x - startCenterX;
	double diffY = y - startCenterY;
	double newX = getCenterX() + diffX;
	double newY = getCenterY() + diffY;
	setCenterX(newX);
	setCenterY(newY);
	startCenterX = x;
	startCenterY = y;
//        name.setX(this.getCenterX()+this.getRadius()+3);
//        name.setY(this.getCenterY()+this.getRadius()-3);
        this.setNamePosition();
        for(MetroLine metroLine: metroLines){
            if(!metroLine.isCircular()){
            int lx=metroLine.getStations().indexOf(this)*2+2;
            int ly=metroLine.getStations().indexOf(this)*2+3;
            metroLine.getPoints().set(lx, metroLine.getPoints().get(lx)+diffX);
            metroLine.getPoints().set(ly, metroLine.getPoints().get(ly)+diffY);
            }
            else{
                if(metroLine.getStations().indexOf(this)==0){
                    metroLine.getPoints().set(0, metroLine.getPoints().get(0)+diffX);
                    metroLine.getPoints().set(1, metroLine.getPoints().get(1)+diffY); 
                    metroLine.getPoints().set(metroLine.getPoints().size()-2, metroLine.getPoints().get(metroLine.getPoints().size()-2)+diffX);
                    metroLine.getPoints().set(metroLine.getPoints().size()-1, metroLine.getPoints().get(metroLine.getPoints().size()-1)+diffY); 
                }
                else{
                    int lx=metroLine.getStations().indexOf(this)*2;
                    int ly=metroLine.getStations().indexOf(this)*2+1;
                    metroLine.getPoints().set(lx, metroLine.getPoints().get(lx)+diffX);
                    metroLine.getPoints().set(ly, metroLine.getPoints().get(ly)+diffY); 
                }
            }
        }
    }
    
    @Override
    public void size(int x, int y) {
//	double width = x - startCenterX;
//	double height = y - startCenterY;
//	double centerX = startCenterX + (width / 2);
//	double centerY = startCenterY + (height / 2);
//	setCenterX(centerX);
//	setCenterY(centerY);
//	setRadius(width / 2);
	
    }
        
//    @Override
    public double getX() {
	return getCenterX() - getRadius();
    }

//    @Override
    public double getY() {
	return getCenterY() - getRadius();
    }

//    @Override
    public double getWidth() {
	return getRadius() * 2;
    }

//    @Override
    public double getHeight() {
	return getRadius() * 2;
    }
        
    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
	setCenterX(initX + (initWidth/2));
	setCenterY(initY + (initHeight/2));
	setRadius(initWidth/2);
//	setRadiusY(initHeight/2);
    }
    
    @Override
    public String getShapeType() {
	return STATION;
    }
//    public DraggableEllipse Clone(){
//        DraggableEllipse c=new DraggableEllipse();
////        c.startCenterX=startCenterX;
////        c.startCenterY=startCenterY;
//        c.setFill(this.getFill());
//        c.setStroke(this.getStroke());
//        c.setStrokeWidth(this.getStrokeWidth());
//        c.setLocationAndSize(getX(), getY(), getWidth(), getHeight());
//        return c;
//    }
    @Override
    public double getStartX() {
        return startCenterX;
    }
    @Override
    public double getStartY() {
        return startCenterY;
    }

    public void setStartX(double startCenterX) {
        this.startCenterX = startCenterX;
    }

    public void setStartY(double startCenterY) {
        this.startCenterY = startCenterY;
    }
    
    
    public Text getName(){
        return name;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }
    
    public String getColorHex() {
        return colorHex;
    }   

    public List<MetroLine> getMetroLines() {
        return metroLines;
    }
    
    public List<Station> getNeighbors(){
        return neighbors;
    }
    
    public void setNamePosition(){
//        System.out.println("W:"+name.getLayoutBounds().getWidth()+" H:"+name.getLayoutBounds().getHeight()+" D: "+name.getLayoutBounds().getDepth());
        if(namePosition==0){
            this.name.setX(this.getCenterX()-this.getRadius()-space-name.getLayoutBounds().getWidth());
            this.name.setY(this.getCenterY()-this.getRadius()-space);
        }
        if(namePosition==1){
            this.name.setX(this.getCenterX()+this.getRadius()+space);
            this.name.setY(this.getCenterY()-this.getRadius()-space);
        }
        if(namePosition==2){
            this.name.setX(this.getCenterX()+this.getRadius()+space);
            this.name.setY(this.getCenterY()+this.getRadius()+space);
        }
        if(namePosition==3){
            this.name.setX(this.getCenterX()-this.getRadius()-space-name.getLayoutBounds().getWidth());
            this.name.setY(this.getCenterY()+this.getRadius()+space);
        }
    }

    public void setNamePositionValue(int namePosition) {
        if(namePosition<0)
            namePosition+=4;    
        this.namePosition = namePosition%4;
    }

    public int getNamePosition() {
        return namePosition;
    }  

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    
    @Override
    public String toString(){
        return name.getText();
    }
}
