package com.fiberhome.boxTool;

import com.fiberhome.util.DataUtil;
import com.fiberhome.util.FileUtil;
import com.fiberhome.util.WaitUtil;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AutoMake {

    public static String merge_dir = Main.localPath + "/merge_dir/";
    public static String unsignedzip_dir = Main.localPath + "/unsigned_update.zip";
    public static String finalZip_dir = Main.localPath + "/output/signed_update.zip";

    public static void automake(String filename1, String filename2, String hguname) {
        System.out.println("Main.localPath = " + Main.localPath);
        System.out.println("merge_dir = " + merge_dir);
        FileUtil.deleteDir(merge_dir);
        Main.jPanelFrame.jtp.append("1.解压机顶盒镜像", 2);
        final WaitUtil waitUtil = new WaitUtil("  解压中，请稍等...");
        SwingWorker<Integer, Void> sw = new SwingWorker<Integer, Void>() {
            @Override
            protected Integer doInBackground() throws Exception {
                if( FileUtil.unZip(new File(filename1), merge_dir) )    return 0;
                else    return 1;
            }
            @Override
            protected void done() {
                try {
                    if(waitUtil != null) {
                        waitUtil.dispose();
                    }
                    int result = get();
                    if (result == 0) {
                        Main.jPanelFrame.jtp.append(" OK\n", 0);
                        makeZip(filename2, hguname);
                    }
                    else
                        Main.jPanelFrame.jtp.append(" Fail\n", 1);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        sw.execute();
        waitUtil.setVisible(true);
    }

    public static void makeZip(String file, String filename) {
        Main.jPanelFrame.jtp.append("2.拷贝网关镜像文件", 2);
        if ( FileUtil.copyFile(file, DataUtil.hgu_dir + filename, true) )
            Main.jPanelFrame.jtp.append(" OK\n", 0);
        else
            Main.jPanelFrame.jtp.append(" Fail\n", 1);
        Main.jPanelFrame.jtp.append("3.压缩镜像文件", 2);
        Main.jPanelFrame.jtp.paintImmediately( Main.jPanelFrame.jtp.getBounds());

        final WaitUtil waitUtil = new WaitUtil("  压缩中，请稍等...");
        SwingWorker<Integer, Void> sw = new SwingWorker<Integer, Void>() {
            @Override
            protected Integer doInBackground() throws Exception {
                FileUtil.zip(unsignedzip_dir, merge_dir);
                return 0;
            }
            @Override
            protected void done() {
                Process ps = null;
                if(waitUtil != null) {
                    waitUtil.dispose();
                }
                Main.jPanelFrame.jtp.append(" OK\n", 0);
                Main.jPanelFrame.jtp.append("4.签名镜像文件", 2);
                Main.jPanelFrame.jtp.paintImmediately( Main.jPanelFrame.jtp.getBounds());
                try {
                    String cmd = "cmd /C " + DataUtil.get_signZIPbat_dir() + "signZIP.bat";
                    ps = Runtime.getRuntime().exec(cmd);
                    //ps.waitFor();
                    InputStream in = ps.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String tmp = null;
                    while((tmp=br.readLine())!=null) {
                        System.out.println("tmp:" + tmp);
                        if (tmp.equals("Finished")) {
                            Main.jPanelFrame.jtp.append(" OK\n", 0);
                            Main.jPanelFrame.jtp.append("5.生成新版本：" + finalZip_dir, 2);
                            String MD5 = FileUtil.getMD5(finalZip_dir);
                            Main.jPanelFrame.jtp.append("\nMD5：" + MD5, 2);
                        }
                    }
                    in.close();
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //int i = ps.exitValue();
                ps.destroy();
                /*
                if (i == 0) {
                    Main.jPanelFrame.jtp.append(" OK\n", 0);
                    Main.jPanelFrame.jtp.append("5.生成新版本：" + finalZip_dir, 2);

                } else
                    Main.jPanelFrame.jtp.append(" Fail\n", 1);*/
                /*
                try {
                    Runtime.getRuntime().exec("cmd /C start wmic process where name='cmd.exe' call terminate");
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        };
        sw.execute();
        waitUtil.setVisible(true);
    }
}
