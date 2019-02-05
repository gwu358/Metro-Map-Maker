/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;

import djf.AppTemplate;
import mmm.data.Draggable;
import mmm.gui.mmmWorkspace;
import jtps.jTPS_Transaction;

/**
 *
 * @author Student
 */
public class Drag implements jTPS_Transaction{
    int oldX, oldY;
            Double X,Y;
    Draggable d;
    public Drag(Draggable d, int oldX, int oldY){
        this.d=d;
        this.oldX=oldX; this.oldY=oldY;
        X=d.getStartX(); Y=d.getStartY();
    }
    
    @Override
    public void doTransaction(){
//        System.out.println("2. oldX "+oldX+"  oldY    "+ oldY);
//        System.out.println("3. X    "+X+"  Y    "+ Y);
        d.drag(X.intValue(), Y.intValue());       
    }
    
    @Override
    public void undoTransaction(){  
//        System.out.println("2. oldX "+oldX+"  oldY    "+ oldY);
//        System.out.println("3. X    "+X+"  Y    "+ Y);
//System.out.println(oldX+" utx uty "+oldY);
        d.drag(oldX, oldY);
//        System.out.println(d.get" yo ");
//        d.setLocationAndSize(oldX, oldY, d.getWidth(), d.getHeight());
    }
    
}
