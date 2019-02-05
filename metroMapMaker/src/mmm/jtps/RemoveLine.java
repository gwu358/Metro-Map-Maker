/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;
import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;
import mmm.data.MetroLine;
import mmm.data.mmmData;

/**
 *
 * @author guang
 */
public class RemoveLine implements jTPS_Transaction{
    MetroLine metroLine;
    mmmData dataManager;
    ComboBox<MetroLine> comboBox;
    public RemoveLine(MetroLine l, mmmData d, ComboBox<MetroLine> cb){
        metroLine=l;
        dataManager=d;
        comboBox=cb;
    }
    @Override
    public void doTransaction(){
        dataManager.removeShape(metroLine);
        dataManager.removeShape(metroLine.getName1());
        dataManager.removeShape(metroLine.getName2());
        comboBox.getItems().remove(metroLine);
        dataManager.setSelectedMetroLine(null);
        if(dataManager.getSelectedShape()==metroLine){
            dataManager.unhighlightShape(dataManager.getSelectedShape());
            dataManager.setSelectedShape(null);           
        }           
    }
    
    @Override
    public void undoTransaction(){        
        dataManager.addShape(metroLine);
        dataManager.addShape(metroLine.getName1());
        dataManager.addShape(metroLine.getName2());
        comboBox.getItems().add(metroLine);
    }
}
