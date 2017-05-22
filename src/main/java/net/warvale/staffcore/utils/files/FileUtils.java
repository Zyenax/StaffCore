package net.warvale.staffcore.utils.files;

import net.warvale.staffcore.StaffCore;

import java.io.*;
import java.util.logging.Level;

/**
 * Created by Draem on 5/19/2017.
 */
public class FileUtils {

    public static void copyFolder(File src, File dest) throws IOException {

        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdir();
            }

            String files[] = src.list();

            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                copyFolder(srcFile,destFile);
            }

        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
        }
    }

    public static void loadFile(String file)
    {
        File t = new File(StaffCore.get().getDataFolder(), file);
        StaffCore.get().getLogger().log(Level.INFO, "Writing new file: " + t.getAbsolutePath());

        try {
            t.createNewFile();
            FileWriter out = new FileWriter(t);
            InputStream is = StaffCore.get().getResource(file);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                out.write(line + "\n");
            }
            out.flush();
            is.close();
            isr.close();
            br.close();
            out.close();

            StaffCore.get().getLogger().log(Level.INFO, "Loaded Config: " + file + " successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean moveFile(File oldConfig) {
        StaffCore.get().getLogger().log(Level.INFO, "Moving outdated config file: " + oldConfig.getName());
        String name = oldConfig.getName();
        File configBackup = new File(StaffCore.get().getDataFolder(), getNextName(name, 0));
        return oldConfig.renameTo(configBackup);
    }

    private static String getNextName(String name, int n){
        File oldConfig = new File(StaffCore.get().getDataFolder(), name+".old"+n);
        if(!oldConfig.exists()){
            return oldConfig.getName();
        }
        else{
            return getNextName(name, n+1);
        }
    }

}
