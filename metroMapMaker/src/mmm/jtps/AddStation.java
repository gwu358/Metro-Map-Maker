/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;
import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;
import mmm.data.Station;
import mmm.data.mmmData;

/**
 *
 * @author guang
 */
public class AddStation implements jTPS_Transaction{
    Station station;
    mmmData dataManager;
    ComboBox<Station> comboBox;
    
    public AddStation(Station s, mmmData d, ComboBox<Station> cb){
        station=s;
        dataManager=d;
        comboBox=cb;
    }
    @Override
    public void doTransaction(){
        dataManager.addShape(station);
//        System.out.println(station.getName().getText());
        dataManager.addShape(station.getName());
        comboBox.getItems().add(station);
    }
    
    @Override
    public void undoTransaction(){        
        dataManager.removeShape(station);
        dataManager.removeShape(station.getName());
        comboBox.getItems().remove(station);
        dataManager.setSelectedStation(null);
        if(dataManager.getSelectedShape()==station){
            dataManager.unhighlightShape(station);
            dataManager.setSelectedShape(null);
        }
    }
}
