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
public class RotateStationLabel implements jTPS_Transaction{
    Station station;

    public RotateStationLabel(Station station) {
        this.station = station;
    }
    @Override
    public void doTransaction(){
        if(station.getRotate()==90.0)
            station.getName().setRotate(0);
        else
            station.getName().setRotate(90);
    }
    @Override
    public void undoTransaction(){
        if(station.getRotate()==90.0)
            station.getName().setRotate(0);
        else
            station.getName().setRotate(90);
    }
}
