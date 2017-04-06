package com.subsnj.actions;

import com.subsnj.ui.UISyncer;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Actions {

    public static void actionOpenFile() {
        UISyncer syncer = UISyncer.getINSTANCE();
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filters = new FileNameExtensionFilter(
                "Subtitle files", "srt", "sub");
        chooser.setDialogTitle("Open...");
        chooser.setFileFilter(filters);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setAcceptAllFileFilterUsed(false);

        int returnVal = chooser.showOpenDialog(syncer);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            syncer.receiveFile(chooser.getSelectedFile().getPath());
        }
    }
}
