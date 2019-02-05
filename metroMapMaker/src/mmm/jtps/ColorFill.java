/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;

import jtps.jTPS_Transaction;
import javafx.scene.shape.Shape;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import mmm.data.DraggableText;
/**
 *
 * @author Student
 */
public class ColorFill implements jTPS_Transaction{
    Shape shape;    Color old, current;
    String oldColorHex, colorHex;
    public ColorFill(Shape s, Paint c){
        this.shape=s;  
        this.old=(Color)s.getFill(); 
        current=(Color)c;      
                
        colorHex = String.format( "#%02X%02X%02X",
            (int)( current.getRed() * 255 ),
            (int)( current.getGreen() * 255 ),
            (int)( current.getBlue() * 255 ) );      
        oldColorHex = String.format( "#%02X%02X%02X",
            (int)( old.getRed() * 255 ),
            (int)( old.getGreen() * 255 ),
            (int)( old.getBlue() * 255 ) );
    }
    
    @Override
    public void doTransaction(){
        shape.setFill(current);
        if(shape instanceof DraggableText){
            DraggableText t=(DraggableText)shape;
            t.setColorHex(colorHex);
        }
        
    }
    
    @Override
    public void undoTransaction(){        
        shape.setFill(old);
        if(shape instanceof DraggableText){
            DraggableText t=(DraggableText)shape;
            t.setColorHex(oldColorHex);
        }
    }
}
