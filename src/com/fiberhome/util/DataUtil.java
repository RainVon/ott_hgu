package com.fiberhome.util;

import com.fiberhome.boxTool.Main;

public class DataUtil {

    public static String signZIPbat_dir = Main.localPath + "/key_mv100/";
    public static String hgu_dir = Main.localPath + "/merge_dir/hgu/";

    private DataUtil() {
    }


    public static void set_signZIPbat_dir(String path) {
        signZIPbat_dir = path;
    }

    public static String get_signZIPbat_dir() {
        return signZIPbat_dir;
    }
}
