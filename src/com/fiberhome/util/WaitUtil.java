package com.fiberhome.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class WaitUtil extends JDialog {
    private final JPanel contentPanel = new JPanel();

    public WaitUtil(String title) {
        setBounds(0, 0, 280, 120);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        {
            JLabel lblLoading = new JLabel(title);
            lblLoading.setForeground(Color.DARK_GRAY);
            //lblLoading.setOpaque(false);
            lblLoading.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(WaitUtil.class.getResource("wait.gif"))));
            lblLoading.setFont(new Font("宋体", Font.BOLD, 15));
            lblLoading.setBounds(0, 0, 252, 94);
            contentPanel.add(lblLoading);
        }

        setModalityType(ModalityType.APPLICATION_MODAL);
        setLocationRelativeTo(null);
    }
}
