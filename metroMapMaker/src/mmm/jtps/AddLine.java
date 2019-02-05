/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;
import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;
import mmm.data.mmmData;
import mmm.data.MetroLine;
/**
 *
 * @author guang
 */
public class AddLine implements jTPS_Transaction{
    MetroLine metroLine;
    mmmData dataManager;
    ComboBox<MetroLine> comboBox;
    
    public AddLine(MetroLine l, mmmData d, ComboBox<MetroLine> cb){
        metroLine=l;
        dataManager=d;
        comboBox=cb;
    }
    @Override
    public void doTransaction(){
        dataManager.addShape(metroLine);
        dataManager.addShape(metroLine.getName1());
        dataManager.addShape(metroLine.getName2());
        comboBox.getItems().add(metroLine);
    }
    
    @Override
    public void undoTransaction(){        
        dataManager.removeShape(metroLine);
        dataManager.removeShape(metroLine.getName1());
        dataManager.removeShape(metroLine.getName2());
        comboBox.getItems().remove(metroLine);
        dataManager.setSelectedMetroLine(null);
        if(dataManager.getSelectedShape()==metroLine){
            dataManager.unhighlightShape(metroLine);
            dataManager.setSelectedShape(null);
        }
    }
}
