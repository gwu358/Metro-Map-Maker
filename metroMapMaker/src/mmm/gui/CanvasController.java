package mmm.gui;

import javafx.scene.Cursor;
import javafx.scene.Scene;
//import javafx.scene.shape.Shape;
import mmm.jtps.Drag;
import javafx.scene.Node;
import mmm.data.mmmData;
import mmm.data.Draggable;
import mmm.data.mmmState;
import static mmm.data.mmmState.DRAGGING_NOTHING;
import static mmm.data.mmmState.DRAGGING_SHAPE;
import static mmm.data.mmmState.SELECTING_SHAPE;
import static mmm.data.mmmState.SIZING_SHAPE;
import djf.AppTemplate;
import javafx.scene.text.Text;
import jtps.jTPS_Transaction;
import mmm.data.DraggableText;

/**
 * This class responds to interactions with the rendering surface.
 *
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public class CanvasController {
    int oldx=0,oldy=0;
    Node selected;
    AppTemplate app;
    jTPS_Transaction tran;
    public CanvasController(AppTemplate initApp) {
        app = initApp;
    }

    /**
     * Respond to mouse presses on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
    public void processCanvasMousePress(int x, int y) {
        mmmData dataManager = (mmmData) app.getDataComponent();
        if (dataManager.isInState(SELECTING_SHAPE)) {
            // SELECT THE TOP SHAPE
            Node shape = dataManager.selectTopShape(x, y);
            Scene scene = app.getGUI().getPrimaryScene();
//            if(shape instanceof Text && (!(shape instanceof DraggableText))){
//                return;
//        }
            // AND START DRAGGING IT
            if (shape != null) {
                if(shape instanceof Draggable)
                ((Draggable)shape).start(x, y);
                oldx=x; oldy=y; selected=shape;
                scene.setCursor(Cursor.MOVE);
                dataManager.setState(mmmState.DRAGGING_SHAPE);
                app.getGUI().updateToolbarControls(false);
            } else {
                scene.setCursor(Cursor.DEFAULT);
                dataManager.setState(DRAGGING_NOTHING);
                app.getWorkspaceComponent().reloadWorkspace(dataManager);
            }
        } 
//        else if (dataManager.isInState(mmmState.STARTING_RECTANGLE)) {
//            dataManager.startNewRectangle(x, y);
//        } else if (dataManager.isInState(mmmState.STARTING_ELLIPSE)) {
//            dataManager.startNewEllipse(x, y);
//        } 
//        else if (dataManager.isInState(mmmState.STARTING_IMAGE)) {
//            dataManager.startNewImage(x, y);
//        } else if (dataManager.isInState(mmmState.STARTING_TEXT)) {
//            dataManager.startNewText(x, y);
//        }
        mmmWorkspace workspace = (mmmWorkspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(dataManager);
    }

    /**
     * Respond to mouse dragging on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
    public void processCanvasMouseDragged(int x, int y) {
        mmmData dataManager = (mmmData) app.getDataComponent();
        if (dataManager.isInState(SIZING_SHAPE)) {
            Draggable newDraggableShape = (Draggable) dataManager.getNewShape();
            newDraggableShape.size(x, y);
        } else if (dataManager.isInState(DRAGGING_SHAPE)&&dataManager.getSelectedShape() instanceof Draggable) {
            Draggable selectedDraggableShape = (Draggable) dataManager.getSelectedShape();
            selectedDraggableShape.drag(x, y);
            app.getGUI().updateToolbarControls(false);
        }
    }

    /**
     * Respond to mouse button release on the rendering surface, which we call canvas,
     * but is actually a Pane.
     */
    public void processCanvasMouseRelease(int x, int y) {
        mmmData dataManager = (mmmData) app.getDataComponent();
        if (dataManager.isInState(SIZING_SHAPE)) {
            dataManager.selectSizedShape();
            app.getGUI().updateToolbarControls(false);
        } else if (dataManager.isInState(mmmState.DRAGGING_SHAPE)&&selected instanceof Draggable) {
           if(oldx!=x&&oldy!=y){
                tran=new Drag((Draggable)selected,oldx,oldy);
            dataManager.getJTPS().addTransaction(tran);
            app.getWorkspaceComponent().reloadWorkspace(dataManager);
           }
            dataManager.setState(SELECTING_SHAPE);
            Scene scene = app.getGUI().getPrimaryScene();
            scene.setCursor(Cursor.DEFAULT);
            app.getGUI().updateToolbarControls(false);
        } else if (dataManager.isInState(mmmState.DRAGGING_NOTHING)) {
            dataManager.setState(SELECTING_SHAPE);
        }
    }
}
