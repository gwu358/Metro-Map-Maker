/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;
import jtps.jTPS_Transaction;
import mmm.data.Station;
/**
 *
 * @author guang
 */
public class MoveStationLabel implements jTPS_Transaction{
    Station station;

    public MoveStationLabel(Station station) {
        this.station = station;
    }
    @Override
    public void doTransaction(){
        station.setNamePositionValue(station.getNamePosition()+1);
        station.setNamePosition();
    }
    @Override
    public void undoTransaction(){
        station.setNamePositionValue(station.getNamePosition()-1);
        station.setNamePosition();
    }
}
