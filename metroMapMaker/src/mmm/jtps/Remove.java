/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;
import jtps.jTPS_Transaction;
import mmm.data.Draggable;
import mmm.gui.mmmWorkspace;
import djf.AppTemplate;
import javafx.collections.ObservableList;
import javafx.scene.Node;
/**
 *
 * @author Student
 */
public class Remove implements jTPS_Transaction{
    ObservableList<Node> shapes;
    Node node;

    public Remove(ObservableList<Node> shapes, Node node){
        this.shapes=shapes;
        this.node=node;
    }
    
    @Override
    public void doTransaction(){
        shapes.remove(node);
    }
    
    @Override
    public void undoTransaction(){        
        shapes.add(node);
    }
}
