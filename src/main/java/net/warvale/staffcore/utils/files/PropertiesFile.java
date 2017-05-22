package net.warvale.staffcore.utils.files;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesFile {
    public static final char SPLITCHAR = '=';
    public static final String SPLIT = String.valueOf(SPLITCHAR);

    public static void generateFresh(File write, String[] variables, String[] values) throws Exception {
        if (variables.length != values.length) {
            throw new Exception("Variables and values are not the same size");
        }
        if (write.exists()) {
            write.delete();
        }
        write.createNewFile();
        FileWriter writer = new FileWriter(write);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        for (int i = 0; i < variables.length; i++) {
            try {
                bufferedWriter.write(variables[i] + SPLIT + values[i] + "\n");
            } catch (Exception ex) {
                bufferedWriter.close();
                writer.close();
                throw new Exception("Error while writing", ex);
            }
        }
        bufferedWriter.close();
        writer.close();
    }

    private Map<String, String> data = new HashMap<>();

    public PropertiesFile(File config) throws Exception {
        if (!config.exists()) {
            throw new IOException("File does not exist");
        }
        FileReader reader;
        try {
            reader = new FileReader(config);
        } catch (FileNotFoundException e) {
            //Automatic Catch Statement
            throw new Exception("File does not exist", e);
        }
        BufferedReader bufferedReader = new BufferedReader(reader);
        String nextline;
        try {
            while ((nextline = bufferedReader.readLine()) != null) {
                if (!nextline.startsWith("#")) {
                    String[] properties = nextline.split(SPLIT);
                    if (properties.length >= 1) {
                        String key = properties[0];
                        String value = nextline.substring(key.length() + SPLIT.length());
                        data.put(properties[0], value);
                    } else {
                        throw new Exception("Invalid line " + nextline);
                    }
                }
            }
        } catch (IOException ex) {
            bufferedReader.close();
            reader.close();
            throw new Exception("An internal error has occurred", ex);
        }
    }

    public String getString(String s) {
        return data.get(s);
    }

    public Number getNumber(String s) throws ParseException {
        return NumberFormat.getInstance().parse(getString(s));
    }

    public static void update(File config, String key, String value) throws Exception {
        try {
            FileInputStream in = new FileInputStream(config);
            Properties props = new Properties();
            props.load(in);
            in.close();

            FileOutputStream out = new FileOutputStream(config);
            props.setProperty(key, value);
            props.store(out, null);
            out.close();
        } catch (Exception ex){
            throw new Exception("Could not update properties", ex);
        }
    }
}