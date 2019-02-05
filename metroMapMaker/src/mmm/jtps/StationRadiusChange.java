/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;
import mmm.data.Station;
import jtps.jTPS_Transaction;

/**
 *
 * @author guang
 */
public class StationRadiusChange implements jTPS_Transaction{
    Station station;
    double oldRadius, radius;

    public StationRadiusChange(Station s, double r) {
        this.station = s;
        radius=r;
        oldRadius=s.getRadius();
    }
    
    @Override
    public void doTransaction(){
        station.setRadius(radius);
        station.setNamePosition();
    }
    
    @Override
    public void undoTransaction(){        
        station.setRadius(oldRadius);
        station.setNamePosition();
    }
}
