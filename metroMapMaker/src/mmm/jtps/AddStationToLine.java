/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;
import jtps.jTPS_Transaction;
import mmm.data.mmmData;
import mmm.data.MetroLine;
import mmm.data.Station;
/**
 *
 * @author guang
 */
public class AddStationToLine implements jTPS_Transaction{
    mmmData dataManager;
    MetroLine metroLine;
    Station station;
    public AddStationToLine(mmmData d) {
        dataManager=d;
        metroLine=d.getSelectedMetroLine();
        station=d.getSelectedStation();
    }
    @Override
    public void doTransaction(){
        metroLine.addStationToLine(station);
        station.getMetroLines().add(metroLine);
    }
    
    @Override
    public void undoTransaction(){        
        metroLine.removeStationFromLine(station);
        station.getMetroLines().remove(metroLine);
    }
}
