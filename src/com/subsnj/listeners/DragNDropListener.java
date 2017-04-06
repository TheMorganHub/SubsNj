package com.subsnj.listeners;

import com.subsnj.ui.UISyncer;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

/**
 * A class that provides Drag and drop functionality for subtitles files for the
 * main interface.
 *
 * @author Morgan
 */
public class DragNDropListener extends DropTarget {

    public synchronized void drop(DropTargetDropEvent evt) {
        try {
            evt.acceptDrop(DnDConstants.ACTION_COPY);
            List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            for (File file : droppedFiles) {
                if (file.getPath().toLowerCase().endsWith(".srt")
                        || file.getPath().toLowerCase().endsWith(".sub")) {
                    UISyncer.getINSTANCE().receiveFile(file.getPath());
                }
            }
        } catch (java.awt.datatransfer.UnsupportedFlavorException | java.io.IOException ex) {
            ex.printStackTrace();
        }
    }
}
