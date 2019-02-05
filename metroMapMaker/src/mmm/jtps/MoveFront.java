/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmm.jtps;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import jtps.jTPS_Transaction;

/**
 *
 * @author Student
 */
public class MoveFront implements jTPS_Transaction{
    ObservableList<Node>shapes;
    Node selectedShape;
    int position;
    public MoveFront(ObservableList<Node>shapes, Node node){
        this.shapes=shapes;
        selectedShape=node;
        position=shapes.indexOf(node);
    }
    
    @Override
    public void doTransaction(){
        shapes.remove(selectedShape);
	shapes.add(selectedShape);
    }
    @Override
    public void undoTransaction(){  
        shapes.remove(selectedShape);
        if (shapes.isEmpty()) {
        }
        else{
            ArrayList<Node> temp = new ArrayList<>();
            for (Node node : shapes)
                temp.add(node);
            shapes.clear();
            for(int i=0;i<position;i++)
                shapes.add(temp.get(i));
            shapes.add(selectedShape);
            for (int i=position; i<temp.size();i++)
                shapes.add(temp.get(i));
        }
    }
}
