/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import jtps.jTPS_Transaction;


/**
 *
 * @author Student
 */
public class Add implements jTPS_Transaction{
    ObservableList<Node> shapes;
    Node node;

    public Add(ObservableList<Node> shapes, Node node){
        this.shapes=shapes;
        this.node=node;
    }
    
    @Override
    public void doTransaction(){
        shapes.add(node);
    }
    
    @Override
    public void undoTransaction(){        
        shapes.remove(node);
    }
}
