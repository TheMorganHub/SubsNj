package com.subsnj.util;

import java.awt.Color;
import java.awt.Cursor;
import java.io.File;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Utils {

    public static final Cursor WAIT_CURSOR = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    public static final Cursor DEFAULT_CURSOR = Cursor.getDefaultCursor();
    public static final Color ERROR_RED = new Color(0xBA0800);
    public static final Color SUCCESSFUL_GREEN = new Color(0x3E8927);
    public static final Color WARNING_ORANGE = new Color(0xC98C00);
    

    /**
     * The amount of ms in an hour.
     */
    public static long HOUR_IN_MS = 3600000;

    /**
     * The amount of ms in minute.
     */
    public static long MIN_IN_MS = 60000;

    /**
     * The amount of ms in a second.
     */
    public static long SEC_IN_MS = 1000;

    /**
     * Displays an error message using the {@link JOptionPane#ERROR_MESSAGE}
     * variant of dialog.
     * <p>
     * <b>Note:</b> The error message will be split into 15-word long lines.</p>
     *
     * @param title The title of the dialog.
     * @param message The error message.
     * @param parent The owner of this dialog.
     */
    public static void showErrorMessage(String title, String message, java.awt.Component parent) {
        JOptionPane.showMessageDialog(parent, splitIntoLines(message), title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showMessage(String title, String message, java.awt.Component parent) {
        JOptionPane.showMessageDialog(parent, splitIntoLines(message), title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showWarningMessage(String title, String message, java.awt.Component parent) {
        JOptionPane.showMessageDialog(parent, splitIntoLines(message), title, JOptionPane.WARNING_MESSAGE);
    }

    public static long convertToMs(int h, int m, int s, int ms) {
        return (h * HOUR_IN_MS) + (m * MIN_IN_MS) + (s * SEC_IN_MS) + ms;
    }

    public static File renameFile(File file, String newName) {
        String properPath = file.getPath().substring(0, file.getPath().lastIndexOf(File.separator));
        String newNameWithoutExt = newName.substring(0, newName.lastIndexOf(".") == -1
                ? newName.length() : newName.lastIndexOf("."));
        return new File((properPath + File.separator + newNameWithoutExt + ".srt").replaceAll("\\s", "_"));
    }

    /**
     * Empties the text contents of a given {@code JEditorPane}.
     *
     * @param pane The {@code JEditorPane} from which to remove text.
     */
    public static void emptyDoc(JEditorPane pane) {
        Document doc = pane.getDocument();
        try {
            doc.remove(0, doc.getLength());
        } catch (BadLocationException e) {
        }
    }

    /**
     * Splits a given string into lines comprised of 15 words each. Line breaks
     * added to {@code message} by the user will be honoured and treated as
     * paragraphs.
     *
     * @param message a string.
     * @return a {@code String} split into lines.
     */
    public static String splitIntoLines(String message) {
        String[] messageSplitLineBreak = message.split("\n");
        StringBuilder mainBuilder = new StringBuilder();
        for (int i = 0; i < messageSplitLineBreak.length; i++) {
            String[] whiteSpaceSplit = messageSplitLineBreak[i].split(" ");
            StringBuilder tempBuilder = new StringBuilder();
            for (int j = 0; j < whiteSpaceSplit.length; j++) {
                tempBuilder.append(whiteSpaceSplit[j]).append(" ");
                if (j != 0 && j % 14 == 0
                        && message.length() > tempBuilder.length() + mainBuilder.length()) {
                    tempBuilder.append("\n");
                }
            }
            mainBuilder.append(tempBuilder).append("\n");
        }
        return mainBuilder.toString();
    }

    public static File appendToFileName(File file, String str) {
        String properPath = file.getPath().substring(0, file.getPath().lastIndexOf(File.separator));
        String fileNameWithoutExt = file.getName().substring(0, file.getName().lastIndexOf(".") == -1
                ? file.getName().length() : file.getName().lastIndexOf(".")) + str.replaceAll("\\s", "_");
        return new File((properPath + File.separator + fileNameWithoutExt + ".srt"));
    }

    /**
     * Appends a given string to the {@code StyledDocument} of a {@code pane} as
     * an <b>error</b>, that is, using the {@code Style} 'ErrorStyle'.
     * <p>
     * This method will create the style if the {@code JTextPane} doesn't
     * already have it.</p>
     *
     * @param pane a {@code JTextPane} with a {@code StyledDocument}.
     * @param fontName the name of the font to be used
     * @param fontSize The fontSize to be used.
     * @param s a {@code String}.
     */
    public static void appendAsError(JTextPane pane, String fontName, int fontSize, String s) {
        if (pane.getStyle("ErrorStyle") == null) {
            Style errorStyle = pane.addStyle("ErrorStyle", null);
            StyleConstants.setForeground(errorStyle, ERROR_RED);
            StyleConstants.setFontFamily(errorStyle, fontName);
            StyleConstants.setFontSize(errorStyle, fontSize);
        }

        StyledDocument doc = (StyledDocument) pane.getDocument();
        try {
            doc.insertString(doc.getLength(), s, pane.getStyle("ErrorStyle"));
        } catch (BadLocationException ex) {
        }
    }

    public static void appendAsGreen(JTextPane pane, String fontName, int fontSize, String s) {
        if (pane.getStyle("greenStyle") == null) {
            Style greenStyle = pane.addStyle("greenStyle", null);
            StyleConstants.setForeground(greenStyle, SUCCESSFUL_GREEN);
            StyleConstants.setFontFamily(greenStyle, fontName);
            StyleConstants.setFontSize(greenStyle, fontSize);
        }

        StyledDocument doc = (StyledDocument) pane.getDocument();
        try {
            doc.insertString(doc.getLength(), s, pane.getStyle("greenStyle"));
        } catch (BadLocationException ex) {
        }
    }

    public static void appendAsOrange(JTextPane pane, String fontName, int fontSize, String s) {
        if (pane.getStyle("orangeStyle") == null) {
            Style orangeStyle = pane.addStyle("orangeStyle", null);
            StyleConstants.setForeground(orangeStyle, WARNING_ORANGE);
            StyleConstants.setFontFamily(orangeStyle, fontName);
            StyleConstants.setFontSize(orangeStyle, fontSize);
        }

        StyledDocument doc = (StyledDocument) pane.getDocument();
        try {
            doc.insertString(doc.getLength(), s, pane.getStyle("orangeStyle"));
        } catch (BadLocationException ex) {
        }
    }

    /**
     * Appends a given string to the {@code StyledDocument} of a {@code pane} as
     * a <b>message</b>, that is, using the {@code Style} 'MessageStyle'.
     * <p>
     * This method will create the style if the {@code JTextPane} doesn't
     * already have it.</p>
     *
     * @param pane a {@code JTextPane} with a {@code StyledDocument}.
     * @param fontName the name of the font to be used
     * @param fontSize The size of the font to be used.
     * @param s a {@code String}.
     */
    public static void appendAsMessage(JTextPane pane, String fontName, int fontSize,
            String s) {
        if (pane.getStyle("MessageStyle") == null) {
            Style messageStyle = pane.addStyle("MessageStyle", null);
            StyleConstants.setForeground(messageStyle, Color.BLACK);
            StyleConstants.setFontFamily(messageStyle, fontName);
            StyleConstants.setFontSize(messageStyle, fontSize);
        }

        StyledDocument doc = (StyledDocument) pane.getDocument();
        try {
            doc.insertString(doc.getLength(), s, pane.getStyle("MessageStyle"));
        } catch (BadLocationException ex) {
        }
    }
    
    public static int percentage(int partial, int total) {
        return Math.round((float) partial / (float) total * 100);
    }
}
