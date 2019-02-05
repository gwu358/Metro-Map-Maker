/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;
import javafx.scene.layout.Pane;
import jtps.jTPS_Transaction;
import mmm.data.mmmData;
/**
 *
 * @author guang
 */
public class MapSizeChange implements jTPS_Transaction{
    Pane canvas;
    Double width, height;
    Double oldw, oldh;
    boolean refresh;
    mmmData dataManager;
    public MapSizeChange(Pane cvs, mmmData dm, double w, double h, boolean ref){
        this.canvas=cvs;
        width=w;
        height=h;
        oldw=cvs.getWidth();
        oldh=cvs.getHeight();
        refresh=ref;
        dataManager=dm;
    }
    @Override
    public void doTransaction(){
        canvas.setMinSize(width, height);
        canvas.setMaxSize(width, height);
        if(refresh){
                dataManager.removeGrid();
                dataManager.addGrid(width, height);

        }
    }
    @Override
    public void undoTransaction(){
        canvas.setMinSize(oldw, oldh);
        canvas.setMaxSize(oldw, oldh);
        if(refresh){
                dataManager.removeGrid();
                dataManager.addGrid(oldw, oldh);

        }
    }
}
