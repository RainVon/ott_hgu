package com.fiberhome.boxTool;

import com.fiberhome.util.DataUtil;
import org.omg.CORBA.Environment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

public class Main {
    private static String file1 = "";
    private static String file2 = "";
    private static String filename = "";
    //public static JTextArea jta= new JTextArea();
    public static JPanelFrame jPanelFrame = new JPanelFrame();
    public static String localPath = "";

    private void Show(JFrame jf, JButton jb1, JButton jb2, JButton jb3, JComboBox jcb) {

        new DropTarget(jPanelFrame.jtp, DnDConstants.ACTION_COPY_OR_MOVE,
                new DropTargetAdapter() {
                    @Override
                    public void drop(DropTargetDropEvent dtde)
                    {
                        try {
                            if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                                @SuppressWarnings("unchecked")
                                List<File> list = (List<File>) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                                for (File file : list) {
                                    if (file.getName().contains("update.zip")) {
                                        file1 = file.getAbsolutePath();
                                        System.out.println("file1 = " + file1);
                                        jPanelFrame.jtp.append("已选择机顶盒镜像：" + file1 + "\n", 2);
                                        jPanelFrame.jtp.paintImmediately( jPanelFrame.jtp.getBounds());
                                    }
                                    else if (file.getName().contains(".bin")) {
                                        file2 = file.getAbsolutePath();
                                        filename = file.getName();
                                        System.out.println("file2 = " + file2);
                                        jPanelFrame.jtp.append("已选择网关镜像：" + file2 + "\n", 2);
                                    }
                                    else JOptionPane.showMessageDialog(null, "无效文件，请选择正确的机顶盒和网关镜像文件！", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                                dtde.dropComplete(true);
                            }
                            else dtde.rejectDrop();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("[Main] DropTarget: " + e.toString());
                        }
                    }
                });

        jcb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = jcb.getSelectedIndex();
                switch (index) {
                    case 0:
                        DataUtil.set_signZIPbat_dir(localPath + "/key_mv100/");
                        jPanelFrame.jtp.append("\n已选择芯片：Hi3798MV100\n", 2);
                        break;
                    case 1:
                        DataUtil.set_signZIPbat_dir(localPath + "/key_mv310/");
                        jPanelFrame.jtp.append("\n已选择芯片：Hi3798MV300/310\n", 2);
                        break;
                }
            }
        });

        jb1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(jf, "FileDialog", FileDialog.LOAD);
                fd.setVisible(true);
                if(fd!=null) {
                    file1=fd.getDirectory() +fd.getFile();
                    System.out.println("file1 = " + file1);
                    if (fd.getFile().toString().contains("update.zip")) {
                        jPanelFrame.jtp.append("已选择机顶盒镜像：" + file1 + "\n", 2);
                        jPanelFrame.jtp.paintImmediately( jPanelFrame.jtp.getBounds());
                    }
                    else {
                        file1 = "";
                        JOptionPane.showMessageDialog(null, "无效文件，请选择正确的镜像！", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        jb2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(jf, "FileDialog", FileDialog.LOAD);
                fd.setVisible(true);
                if(fd!=null) {
                    file2=fd.getDirectory() +fd.getFile();
                    filename=fd.getFile();
                    System.out.println("file2 = " + file2);
                    if (fd.getFile().toString().contains(".bin")) {
                        jPanelFrame.jtp.append("已选择网关镜像：" + file2 + "\n", 2);
                        jPanelFrame.jtp.paintImmediately( jPanelFrame.jtp.getBounds());
                    }
                    else {
                        file2 = "";
                        JOptionPane.showMessageDialog(null, "无效文件，请选择正确的镜像！", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        jb3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( (null != file1 && !file1.equals("") && file1.contains("update.zip")) ) {
                    jPanelFrame.jtp.append("\n########################### 开始自动生成复合镜像文件 ###########################\n", 2);
                    jPanelFrame.jtp.paintImmediately( jPanelFrame.jtp.getBounds());
                    AutoMake.automake(file1, file2, filename);
                }
                else JOptionPane.showMessageDialog(null, "请先选择文件！", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        get_local_path();
        JFrame frame = new JFrame("复合镜像制作工具");
        JPanel p1 = new JPanel();
        p1.setBorder(new EmptyBorder(10, 20, 10, 20));
        p1.setLayout(new GridLayout(1,4, 10, 10));
        String str1[] = {"Hi3798MV100", "Hi3798MV300/310"};
        JComboBox jcb = new JComboBox(str1);
        JButton jbutton1 = new JButton("点击或拖拽选取机顶盒镜像");
        jbutton1.setFont(new Font("宋体", 0, 12));
        JButton jbutton2 = new JButton("点击或拖拽选取网关镜像");
        JButton jbutton3 = new JButton("点击生成复合镜像");
        jbutton2.setFont(new Font("宋体", 0, 12));
        jbutton3.setFont(new Font("宋体", 0, 12));
        p1.add(jcb);
        p1.add(jbutton1);
        p1.add(jbutton2);
        p1.add(jbutton3);
        JPanel p2 = new JPanel(new BorderLayout());
        p2.add(p1, BorderLayout.NORTH);
        jPanelFrame.jtp.setEditable(false);
        //jtp.setLineWrap(true);
        jPanelFrame.jtp.setFont(new Font("Default",Font.PLAIN,13));
        JScrollPane jsp = new JScrollPane(jPanelFrame.jtp);
        jsp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        p2.add(jsp, BorderLayout.CENTER);
        frame.add(p2);
        new Main().Show(frame, jbutton1, jbutton2, jbutton3, jcb);
        frame.setSize(850, 360);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static void get_local_path() {
        //java.net.URL url = Main.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            //String jarPath = java.net.URLDecoder.decode (url.getPath(), "utf-8");
            //localPath = new File(jarPath).getParent();
            localPath = new File(".").getAbsolutePath();
            File logfile = new File(localPath + "/out.log");
            if(!logfile.exists())
                logfile.createNewFile();
            System.setOut(new PrintStream(logfile));
            System.out.println("localPath = " + localPath);
            System.out.println("logfile = " + logfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

