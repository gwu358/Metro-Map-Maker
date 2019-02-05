/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;
import javafx.scene.paint.Color;
import mmm.data.MetroLine;
import mmm.data.mmmData;
/**
 *
 * @author guang
 */
import jtps.jTPS_Transaction;
public class EditLine implements jTPS_Transaction{
    MetroLine metroLine;
    String oldColorHex, colorHex;
    String oldName, name;
    boolean oldCircular, circular;
    double sX,sY,eX,eY;
    mmmData dataManager;
    public EditLine(MetroLine l, String n, String ch, boolean ccl, mmmData d) {
        metroLine=l;
        oldColorHex=l.getColorHex();
        oldName=l.getName1().getText();
        oldCircular=l.isCircular();
        colorHex=ch;
        name=n;
        this.circular=ccl;
        dataManager=d;
    }
    @Override
    public void doTransaction(){
        metroLine.setColorHex(colorHex);
        metroLine.setStroke(Color.valueOf(colorHex));
        metroLine.setName(name);
        metroLine.setCircular(circular);
        if(circular&&!oldCircular){
            sX=metroLine.getPoints().remove(0);     
            sY=metroLine.getPoints().remove(0);
            eY=metroLine.getPoints().remove(metroLine.getPoints().size()-1);      
            eX=metroLine.getPoints().remove(metroLine.getPoints().size()-1);  
            metroLine.getPoints().addAll(metroLine.getPoints().get(0), metroLine.getPoints().get(1));
            dataManager.removeShape(metroLine.getName1());
            dataManager.removeShape(metroLine.getName2());
        }
        else if(!circular&&oldCircular){
            metroLine.getPoints().remove(metroLine.getPoints().size()-2, metroLine.getPoints().size());
//            metroLine.getPoints().add(0, metroLine.getPoints().get(1));
//            metroLine.getPoints().add(0, metroLine.getPoints().get(1)-20);
//            metroLine.getPoints().addAll(metroLine.getPoints().get(metroLine.getPoints().size()-2)+20, metroLine.getPoints().get(metroLine.getPoints().size()-1));
            metroLine.getPoints().add(0, metroLine.getPoints().get(1));
            metroLine.getPoints().add(0, metroLine.getPoints().get(1)-20);
            metroLine.getPoints().addAll(metroLine.getPoints().get(metroLine.getPoints().size()-2)+20, metroLine.getPoints().get(metroLine.getPoints().size()-1));
            metroLine.Name1position();
            metroLine.Name2position();
            dataManager.addShape(metroLine.getName1());
            dataManager.addShape(metroLine.getName2());
        }
    }
    
    @Override
    public void undoTransaction(){        
        metroLine.setColorHex(oldColorHex);
        metroLine.setStroke(Color.valueOf(oldColorHex));
        metroLine.setName(oldName);
        metroLine.setCircular(oldCircular);
        if(circular&&!oldCircular){
            metroLine.getPoints().remove(metroLine.getPoints().size()-2, metroLine.getPoints().size());
            metroLine.getPoints().add(0, sY);
            metroLine.getPoints().add(0, sX);
            metroLine.getPoints().addAll(eX, eY);
            metroLine.Name1position();
            metroLine.Name2position();
            dataManager.addShape(metroLine.getName1());
            dataManager.addShape(metroLine.getName2());
        }
        else if(!circular&&oldCircular){
            metroLine.getPoints().remove(0, 2);
            metroLine.getPoints().remove(metroLine.getPoints().size()-2, metroLine.getPoints().size());
            metroLine.getPoints().addAll(metroLine.getPoints().get(0),metroLine.getPoints().get(1));
            dataManager.removeShape(metroLine.getName1());
            dataManager.removeShape(metroLine.getName2());
        }
    }
}
