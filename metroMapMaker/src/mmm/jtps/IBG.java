/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;

import mmm.data.mmmData;
import javafx.scene.Node;
import jtps.jTPS_Transaction;
import javafx.scene.image.ImageView;


/**
 *
 * @author Student
 */
public class IBG implements jTPS_Transaction{
    mmmData dataManager;
    ImageView iv;
    ImageView old;
    public IBG(mmmData d, ImageView node){
        dataManager=d;
        this.iv=node;
        iv.setX(d.getWorkpsace().getCanvas().getWidth()/2-iv.getImage().getWidth()/2);
        iv.setY(d.getWorkpsace().getCanvas().getHeight()/2-iv.getImage().getHeight()/2);
    }
    
    @Override
    public void doTransaction(){
        if(dataManager.getBackgroundImage()==null){
            dataManager.getShapes().add(0, iv);
        }
        else{
            old=dataManager.getBackgroundImage();
            dataManager.getShapes().set(0, iv);
        }
        dataManager.setBackgroundImage(iv);
    }
    
    @Override
    public void undoTransaction(){        
        if(old==null){
            dataManager.getShapes().remove(0);
            dataManager.setBackgroundImage(null);
        }
        else
            dataManager.getShapes().set(0, old);
        dataManager.setBackgroundImage(old);
    }
}
