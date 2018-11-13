package com.fiberhome.util;

import java.io.*;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class FileUtil {

    public static boolean unZip(File zipFile, String descDir) {
        boolean flag = false;
        File pathFile = new File(descDir);
        if( !pathFile.exists() )
            pathFile.mkdirs();
        ZipFile zip = null;
        System.out.println("[FileUtil] 解压文件：" + zipFile.toString());
        try {
            zip = new ZipFile(zipFile, "GBK");
            for(Enumeration entries = zip.getEntries(); entries.hasMoreElements();){
                ZipEntry entry = (ZipEntry)entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                String outPath = (descDir+zipEntryName).replace("/", File.separator);

                File file = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
                if(!file.exists()){
                    file.mkdirs();
                }

                if(new File(outPath).isDirectory()){
                    continue;
                }

                //urlList.add(outPath);

                OutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[2048];
                int len;
                while((len=in.read(buf1))>0){
                    out.write(buf1,0,len);
                }
                in.close();
                out.close();
            }
            flag = true;
            zip.close();
        } catch (IOException e) {
            System.out.println("[FileUtil] 解压文件失败：" + e.getMessage());
        }
        return flag;
    }

    public static void zip(String zipFileName, String sourceFileName) throws Exception
    {
        System.out.println("[FileUtil] 压缩中...");

        ZipOutputStream out = new ZipOutputStream( new FileOutputStream(zipFileName));
        BufferedOutputStream bos = new BufferedOutputStream(out);
        out.setEncoding("GBK");
        File sourceFile = new File(sourceFileName);

        if(sourceFile.isDirectory()) {
            File[] flist = sourceFile.listFiles();
            for(int i=0;i<flist.length;i++)
            {
                compress(out,bos,flist[i],flist[i].getName());
            }
        }
        //compress(out,bos,sourceFile,"");
        bos.close();
        out.closeEntry();
        System.out.println("[FileUtil] 压缩完成");
    }

    public static void compress(ZipOutputStream out,BufferedOutputStream bos,File sourceFile,String base) throws Exception
    {
        if(sourceFile.isDirectory()) {
            File[] flist = sourceFile.listFiles();
            if(flist.length==0) {
                //System.out.println(base+"/");
                out.putNextEntry(  new ZipEntry(base+"/") );
            }
            else {
                for(int i=0;i<flist.length;i++)
                {
                    compress(out,bos,flist[i],base+"/"+flist[i].getName());
                }
            }
        }
        else {
            out.putNextEntry( new ZipEntry(base) );
            FileInputStream fos = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fos);

            int tag;
			byte[] buf = new byte[1024];
            //System.out.println(base);
            while((tag=bis.read(buf))!=-1)
            {
                out.write(buf, 0, tag);
            }
            bis.close();
            fos.close();
        }
    }

    public static boolean copyFile(String srcFileName, String destFileName, boolean override) {
        deleteDir(DataUtil.hgu_dir);
        File pathFile = new File(DataUtil.hgu_dir);
        pathFile.mkdirs();
        File srcFile = new File(srcFileName);
        if (!srcFile.exists()) {
            System.out.println("[FileUtil] 复制文件失败：原文件" + srcFileName + "不存在！");
            return false;
        } else if (!srcFile.isFile()) {
            System.out.println("[FileUtil] 复制文件失败：" + srcFileName + "不是一个文件！");
            return false;
        }
        File destFile = new File(destFileName);
        if (destFile.exists() && destFile.isFile()) {
            if(override) {
                System.out.println("[FileUtil] 目标文件:" + destFileName + "已存在，准备删除它！");
                if (!destFile.delete()) {
                    System.out.println("[FileUtil] 覆盖文件失败：删除目标文件" + destFileName + "失败！");
                    return false;
                }
            }else return true;
        }
        // 准备复制文件
        int byteread = 0;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            System.out.println("[FileUtil] 复制单个文件" + srcFileName + "至" + destFileName + "成功！");
            return true;
        } catch (Exception e) {
            System.out.println("[FileUtil] 复制文件失败：" + e.getMessage());
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean deleteFile(String FileName) {
        File destFile = new File(FileName);
        if (destFile.exists() && destFile.isFile()) {
            System.out.println("[FileUtil] 目标文件:" + FileName + "存在，准备删除它！");
            if (!destFile.delete()) {
                System.out.println("[FileUtil] 删除目标文件" + FileName + "失败！");
                return false;
            }
            else    return true;
        }
        else {
            System.out.println("[FileUtil] 目标文件:" + FileName + "不存在，删除失败！");
            return false;
        }

    }

    public static void deleteDir(String path) {
        //System.out.println("删除文件夹");
        File dir = new File(path);
        if (dir.exists()) {
            File[] tmp = dir.listFiles();
            for (int i = 0; i < tmp.length; i++) {
                if (tmp[i].isDirectory()) {
                    deleteDir(path + "/" + tmp[i].getName());
                } else {
                    tmp[i].delete();
                }
            }
            dir.delete();
        }
    }


    public static String getMD5(String filename) {/*
        FileInputStream fileInputStream = null;
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(new File(file));
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(MD5.digest()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        File file = new File(filename);
        try {
            String MD5 = DigestUtils.md5Hex(new FileInputStream(file));
            return MD5;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            return null;
        }
    }


}
