/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;

import mmm.data.DraggableText;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import jtps.jTPS_Transaction;

/**
 *
 * @author Student
 */
public class FontItalics implements jTPS_Transaction{
    DraggableText text;
    public FontItalics(DraggableText t){
        text=t; 
    }
    
    @Override
    public void doTransaction(){
        Font font;
        if(!text.isItalics()){            
                if(text.isBold())
                    font=Font.font(text.getFont().getFamily(),FontWeight.BOLD, FontPosture.ITALIC, text.getFont().getSize());              
                else
                    font=Font.font(text.getFont().getFamily(),FontWeight.NORMAL, FontPosture.ITALIC, text.getFont().getSize());                
                text.setItalics(true);
            }
            else{
                if(text.isBold())
                    font=Font.font(text.getFont().getFamily(), FontWeight.BOLD,FontPosture.REGULAR, text.getFont().getSize());              
                else
                    font=Font.font(text.getFont().getFamily(), FontWeight.NORMAL,FontPosture.REGULAR, text.getFont().getSize());
                text.setItalics(false);
            }
        text.setFont(font);
    }
    
    @Override
    public void undoTransaction(){        
        doTransaction();
    }
}
