package com.subsnj.listeners;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;

/**
 * A class that all JSpinners must implement. Upon gaining focus, the text
 * within them will be selected.
 *
 * @author Morgan
 */
public class SpinnerFocusListener implements FocusListener {

    @Override
    public void focusGained(FocusEvent e) {
        SwingUtilities.invokeLater(((JFormattedTextField) e.getSource())::selectAll);
    }

    @Override
    public void focusLost(FocusEvent e) {
    }

}
