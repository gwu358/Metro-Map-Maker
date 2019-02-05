/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;

import javafx.scene.text.Text;
import mmm.data.DraggableText;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import jtps.jTPS_Transaction;

/**
 *
 * @author Student
 */
public class FontChange implements jTPS_Transaction{
    DraggableText text;    Font old, current;
    public FontChange(Text t, Font f){
        text=(DraggableText)t; old=t.getFont();    current=f;
    }
    
    @Override
    public void doTransaction(){
        if(text.isBold()&&text.isItalics())
            text.setFont(Font.font(current.getFamily(), FontWeight.BOLD, FontPosture.ITALIC, current.getSize()));
        if((!text.isBold()) && text.isItalics())
            text.setFont(Font.font(current.getFamily(), FontWeight.NORMAL, FontPosture.ITALIC, current.getSize()));
        if(text.isBold()&&(!text.isItalics()))
            text.setFont(Font.font(current.getFamily(), FontWeight.BOLD, FontPosture.REGULAR, current.getSize()));
        if((!text.isBold()) && (!text.isItalics()))
            text.setFont(Font.font(current.getFamily(), FontWeight.NORMAL, FontPosture.REGULAR, current.getSize()));
        
    }
    
    @Override
    public void undoTransaction(){        
        if(text.isBold()&&text.isItalics())
            text.setFont(Font.font(old.getFamily(), FontWeight.BOLD, FontPosture.ITALIC, old.getSize()));
        if((!text.isBold()) && text.isItalics())
            text.setFont(Font.font(old.getFamily(), FontWeight.NORMAL, FontPosture.ITALIC, old.getSize()));
        if(text.isBold()&&(!text.isItalics()))
            text.setFont(Font.font(old.getFamily(), FontWeight.BOLD, FontPosture.REGULAR, old.getSize()));
        if((!text.isBold()) && (!text.isItalics()))
            text.setFont(Font.font(old.getFamily(), FontWeight.NORMAL, FontPosture.REGULAR, old.getSize()));
    }
}
