/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;
import mmm.data.MetroLine;
import jtps.jTPS_Transaction;
/**
 *
 * @author guang
 */
public class LineThicknessChange implements jTPS_Transaction{
    MetroLine metroLine;
    double oldThickness, thickness;

    public LineThicknessChange(MetroLine metroLine, double thickness) {
        this.metroLine = metroLine;
        this.thickness = thickness;
        oldThickness=metroLine.getStrokeWidth();
    }
    
    @Override
    public void doTransaction(){
        metroLine.setStrokeWidth(thickness);
    }
    
    @Override
    public void undoTransaction(){        
        metroLine.setStrokeWidth(oldThickness);
    }
}
