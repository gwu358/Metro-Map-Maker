/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;
import jtps.jTPS_Transaction;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
//import javafx.scene.layout.BackgroundImage;
//import javafx.scene.layout.BackgroundFill;
/**
 *
 * @author Student
 */
public class BackgroundChange implements jTPS_Transaction{
    Pane canvas;    Background oldFill, fill;
//    BackgroundImage oldImage, image;
    public BackgroundChange(Pane canvas, Background f){
        this.canvas=canvas;  oldFill=this.canvas.getBackground();
        fill=f;
//        if(canvas.getBackground()==null)
//            canvas.setBackground(Background.EMPTY);
//        if(canvas.getBackground().getFills().size()>0)
//            this.oldFill=canvas.getBackground().getFills().get(0); 
//        this.fill=f;
    }
//    public BackgroundChange(Pane canvas, BackgroundImage i){
//        this.canvas=canvas; 
//        if(canvas.getBackground().getImages().size()>0)
//            oldImage=canvas.getBackground().getImages().get(0);
//        image=i;
//    }
    @Override
    public void doTransaction(){
        canvas.setBackground(fill);
//        if (image==null&fill!=null)
//            canvas.getBackground().
//        else{
//            canvas.getBackground().getImages().set(0, image);
//        }        
    }
    
    @Override
    public void undoTransaction(){        
        canvas.setBackground(oldFill);
//        if (image==null)
//            canvas.getBackground().getFills().set(0, oldFill);
//        else{
//            canvas.getBackground().getImages().set(0, oldImage);
//        } 
    }
}
