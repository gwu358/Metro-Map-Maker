/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;

import jtps.jTPS_Transaction;
import javafx.scene.shape.Shape;
import javafx.scene.paint.Paint;
/**
 *
 * @author Student
 */
public class ColorOutline implements jTPS_Transaction{
    Shape shape;    Paint old, current;
    public ColorOutline(Shape s, Paint c){
        this.shape=s;  this.old=s.getStroke(); current=c;
    }
    
    @Override
    public void doTransaction(){
        shape.setStroke(current);
    }
    
    @Override
    public void undoTransaction(){        
        shape.setStroke(old);
    }
}
