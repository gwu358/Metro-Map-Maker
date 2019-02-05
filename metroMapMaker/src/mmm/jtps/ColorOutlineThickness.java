/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;

import javafx.scene.shape.Shape;
import jtps.jTPS_Transaction;

/**
 *
 * @author Student
 */
public class ColorOutlineThickness implements jTPS_Transaction{
    Shape shape;    double old, current;
    public ColorOutlineThickness(Shape s, double w){
        this.shape=s;  this.old=s.getStrokeWidth(); current=w;
    }
    
    @Override
    public void doTransaction(){
        shape.setStrokeWidth(current);
    }
    
    @Override
    public void undoTransaction(){        
        shape.setStrokeWidth(old);
    }
}
