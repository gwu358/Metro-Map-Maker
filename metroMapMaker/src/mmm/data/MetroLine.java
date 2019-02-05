/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.data;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import java.lang.Math;
import java.util.*;
import javafx.geometry.VPos;
import javafx.scene.text.TextAlignment;
/**
 *
 * @author Student
 */
public class MetroLine extends Polyline implements Draggable{
    double startX, startY, endX, endY;
    Text name1, name2;
    List<Station> stations = new ArrayList<>();
    String colorHex;
    Boolean circular;
        public MetroLine(String name, String colorHex) {
            circular=false;
            startX = 100.0;
            startY = 100.0;
            endX=600.0;
            endY=100.0;
            this.getPoints().addAll(startX, startY, endX, endY);
            this.setOpacity(1.0);
            name1=new DraggableText(true, this);
            name1.setText(name);
            name2=new DraggableText(false, this);
            name2.setText(name);
            this.colorHex=colorHex;
            this.setStroke(Color.valueOf(colorHex));
            this.setStrokeWidth(5);
            Name1position();
            Name2position();
    }
    
    public void addStationToLine(Station station){
        List<Double> xpos=new ArrayList<>();
        List<Double> ypos=new ArrayList<>();
        xpos.add(this.getPoints().get(0));
        ypos.add(this.getPoints().get(1));
        for(int i=0; i<stations.size(); i++){
            xpos.add(stations.get(i).getCenterX());
            ypos.add(stations.get(i).getCenterY());
        }
        xpos.add(this.getPoints().get(getPoints().size()-2));
        ypos.add(this.getPoints().get(getPoints().size()-1));
        int p=0;
        double sd=Integer.MAX_VALUE;
        for(int i=0; i<xpos.size(); i++){
            double distance=Math.sqrt(Math.pow((xpos.get(i)-station.getCenterX()), 2)+Math.pow((ypos.get(i)-station.getCenterY()), 2));
            if(distance <sd){
                sd=distance;
                p=i;
            }
        }
        if(p==0){
            stations.add(0, station);
            this.getPoints().add(2, station.getCenterX());
            this.getPoints().add(3, station.getCenterY());
        }
        else if(p==xpos.size()-1){
            stations.add(station);
            this.getPoints().add(this.getPoints().size()-2, station.getCenterX());
            this.getPoints().add(this.getPoints().size()-2, station.getCenterY());
        }
        else{
            double d1=Math.sqrt(Math.pow((xpos.get(p-1)-station.getCenterX()), 2)+Math.pow((ypos.get(p-1)-station.getCenterY()), 2));
            double d2=Math.sqrt(Math.pow((xpos.get(p+1)-station.getCenterX()), 2)+Math.pow((ypos.get(p+1)-station.getCenterY()), 2));
//            if((xpos.get(p-1)<station.getCenterX()&&xpos.get(p)>station.getCenterX())||
//                    (ypos.get(p-1)<station.getCenterY()&&ypos.get(p)>station.getCenterY())){
            if(d1<=d2){
                stations.add(p-1, station);
                this.getPoints().add(p*2, station.getCenterX());
                this.getPoints().add(p*2+1, station.getCenterY());
            }
            else{
                stations.add(p, station);
                this.getPoints().add(p*2+2, station.getCenterX());
                this.getPoints().add(p*2+3, station.getCenterY());
            }
        }
        p=stations.indexOf(station);
//        System.out.println("p " + p + " " + stations.size());
        if(p != 0){
            station.getNeighbors().add(stations.get(p-1));
            stations.get(p-1).getNeighbors().add(station);
        }
        if(p != stations.size()-1){
            station.getNeighbors().add(stations.get(p+1));
            stations.get(p+1).getNeighbors().add(station);
        }
    }
    
    public void removeStationFromLine(Station station){
        int index=stations.indexOf(station);
        this.getPoints().remove(index*2+2);
        this.getPoints().remove(index*2+2);
        int i = stations.indexOf(station);
        if(i != 0){
            station.getNeighbors().remove(stations.get(i-1));
            stations.get(i-1).getNeighbors().remove(station);
        }
        if(i != stations.size()-1){
            station.getNeighbors().remove(stations.get(i+1));
            stations.get(i+1).getNeighbors().remove(station);
        }
        this.stations.remove(station);
    }
    public mmmState getStartingState() {
	return mmmState.STARTING_METROLINE;
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
//	double newX = getX() + diffX;
//	double newY = getY() + diffY;
//	xProperty().set(newX);
//	yProperty().set(newY);
	startX = x;
	startY = y;
    }
    
    public String cT(double x, double y) {
	return "(x,y): (" + x + "," + y + ")";
    }
    
    @Override
    public void size(int x, int y) {
//        setX(startX);
//        setY(startY);
//	double width = x - startX;
//	widthProperty().set(width);
//	double height = y - startY;
//	heightProperty().set(height);	
    }
    
    @Override
    public void setLocationAndSize(double initX, double initY, double initWidth, double initHeight) {
//	xProperty().set(initX);
//	yProperty().set(initY);
//	widthProperty().set(initWidth);
//	heightProperty().set(initHeight);
    }
    @Override
    public String getShapeType() {
	return METROLINE;
    }
        public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }
    public void setName(String name){
        name1.setText(name);
        name2.setText(name);
    }
    public Text getName1(){
        return name1;
    }
    public Text getName2(){
        return name2;
    }
    
    public void Name1position(){
        double point1x=this.getPoints().get(0);
        double point1y=this.getPoints().get(1);
//        double point2x=this.getPoints().get(2);
//        double point2y=this.getPoints().get(3);
//        double length=Math.sqrt(Math.pow(point2x-point1x, 2)-Math.pow(point2y-point1y, 2));
//        name1.setX(point1x-(point2x-point1x)/length*50);       
//        name1.setY(point1y-(point2y-point1y)/length*50);
        ((DraggableText)name1).setSX(point1x-name1.getLayoutBounds().getWidth()-20);       
        ((DraggableText)name1).setSY(point1y);
        name1.setTextAlignment(TextAlignment.CENTER);
    }

    public void Name2position(){
        int size=this.getPoints().size();
//        double point1x=this.getPoints().get(size-4);
//        double point1y=this.getPoints().get(size-3);
        double point2x=this.getPoints().get(size-2);
        double point2y=this.getPoints().get(size-1);
//        double length=Math.sqrt(Math.pow(point2x-point1x, 2)-Math.pow(point2y-point1y, 2));
//        name2.setX(point2x+(point2x-point1x)/length*50);
//        name2.setY(point2y+(point2y-point1y)/length*50);
        ((DraggableText)name2).setSX(point2x+20);
        ((DraggableText)name2).setSY(point2y);
        name2.setTextAlignment(TextAlignment.CENTER);
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }
    
    public String getColorHex() {
        return colorHex;
    }

//    public void makeCircular(Boolean c) {
//        if(!circular.equals(c)){
//            if(c){
//                this.getPoints().addAll(this.getPoints().get(0),this.getPoints().get(1));
//            }
//            else{
//                this.getPoints().remove(getPoints().size()-1);
//                this.getPoints().remove(getPoints().size()-1);
//            }
//        }
//    }
    
    public void setCircular(Boolean circular) {
        this.circular = circular;
    }

    public Boolean isCircular() {
        return circular;
    }
    
    public void setHeadPoint(Double x, Double y){
        this.getPoints().set(0, x);
        this.getPoints().set(1, y);
        Name1position();
    }
    public void setTailPoint(Double x, Double y){
        this.getPoints().set(this.getPoints().size()-2, x);
        this.getPoints().set(this.getPoints().size()-1, y);
        Name2position();
    }
}
