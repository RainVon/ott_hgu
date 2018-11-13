package com.fiberhome.boxTool;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class JPanelFrame extends JFrame {
    MyJTextPane jtp = new MyJTextPane() ;

    class MyJTextPane extends JTextPane {
        public void append(String s, int color){
            //setText(getText()+s);
            Document docs = this.getDocument();
            this.setBackground(new Color(43,43,43));
            SimpleAttributeSet attrset2 = new SimpleAttributeSet();
            StyleConstants.setForeground(attrset2, new Color(203,120,50));
            SimpleAttributeSet attrset0 = new SimpleAttributeSet();
            StyleConstants.setForeground(attrset0, new Color(129,164,107));
            SimpleAttributeSet attrset1 = new SimpleAttributeSet();
            StyleConstants.setForeground(attrset1, new Color(253,107,104));

            StyleConstants.setFontSize(attrset0, 15);
            StyleConstants.setFontSize(attrset1, 15);
            StyleConstants.setFontSize(attrset2, 15);
            try {
                if (color == 1)
                    docs.insertString(docs.getLength(), s, attrset1);
                if (color == 0)
                    docs.insertString(docs.getLength(), s, attrset0);
                if (color == 2)
                    docs.insertString(docs.getLength(), s, attrset2);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    public JPanelFrame() {
        init();
    }

    private void init() {

        jtp.setBackground(new Color(43,43,43));
        jtp.append("控制台：version 1.0", 2);
    }
}
