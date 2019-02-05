/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;
import javafx.scene.paint.Color;
import jtps.jTPS_Transaction;
import mmm.data.Station;
/**
 *
 * @author guang
 */
public class EditStation implements jTPS_Transaction{
    Station station;
    String oldColorHex, colorHex;

    public EditStation(Station station, String colorHex) {
        this.station = station;
        oldColorHex=station.getColorHex();
        this.colorHex = colorHex;
    }
    
    @Override
    public void doTransaction(){
        station.setColorHex(colorHex);
        station.setFill(Color.valueOf(colorHex));
    }
    
    @Override
    public void undoTransaction(){
        station.setColorHex(colorHex);
        station.setFill(Color.valueOf(oldColorHex));
    }
}
