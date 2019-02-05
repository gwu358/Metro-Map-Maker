/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;
import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;
import mmm.data.Station;
import mmm.data.MetroLine;
import mmm.data.mmmData;

/**
 *
 * @author guang
 */
public class RemoveStation implements jTPS_Transaction{
    Station station;
    mmmData dataManager;
    ComboBox<Station> comboBox;
    public RemoveStation(Station s, mmmData dm, ComboBox<Station> cb){
        station=s;
        dataManager=dm;
        comboBox=cb;
    }
    @Override
    public void doTransaction(){
        dataManager.removeShape(station.getName());
        dataManager.removeShape(station);       
        comboBox.getItems().remove(station);
        dataManager.setSelectedStation(null);
        for(MetroLine line:station.getMetroLines()){
            line.removeStationFromLine(station);
        }
        if(dataManager.getSelectedShape()==station){
            dataManager.unhighlightShape(dataManager.getSelectedShape());
            dataManager.setSelectedShape(null);           
        }
    }
    
    @Override
    public void undoTransaction(){        
        dataManager.addShape(station);
        dataManager.addShape(station.getName());
        comboBox.getItems().add(station);
        for(MetroLine line:station.getMetroLines()){
            line.addStationToLine(station);
        }
    }
}
