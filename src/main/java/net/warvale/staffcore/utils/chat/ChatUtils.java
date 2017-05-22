package net.warvale.staffcore.utils.chat;

import org.bukkit.ChatColor;

public class ChatUtils {

    public static String scramble = ChatColor.MAGIC.toString();
    public static String bold = ChatColor.BOLD.toString();
    public static String strike = ChatColor.STRIKETHROUGH.toString();
    public static String underline = ChatColor.UNDERLINE.toString();
    public static String italics = ChatColor.ITALIC.toString();
    public static String reset = ChatColor.RESET.toString();

    public static String aqua = ChatColor.AQUA.toString();
    public static String black = ChatColor.BLACK.toString();
    public static String blue = ChatColor.BLUE.toString();
    public static String dark_aqua = ChatColor.DARK_AQUA.toString();
    public static String dark_blue = ChatColor.DARK_BLUE.toString();
    public static String dark_gray = ChatColor.DARK_GRAY.toString();
    public static String dark_green = ChatColor.DARK_GREEN.toString();
    public static String dark_purple = ChatColor.DARK_PURPLE.toString();
    public static String dark_red = ChatColor.DARK_RED.toString();
    public static String gold = ChatColor.GOLD.toString();
    public static String gray = ChatColor.GRAY.toString();
    public static String green = ChatColor.GREEN.toString();
    public static String purple = ChatColor.LIGHT_PURPLE.toString();
    public static String red = ChatColor.RED.toString();
    public static String white = ChatColor.WHITE.toString();
    public static String yellow = ChatColor.YELLOW.toString();

    public static String warning = red + "[!]";

    public static String divider = dark_gray + strike + "--------------------------------------";
    public static String tab = "   ";

    public static String repeat(int count, String with) {
        return new String(new char[count]).replace("\0", with);
    }

    public static String center(String s)
    {
        int le = ( 70 - s.length() ) / 2;
        StringBuilder newS = new StringBuilder();
        for ( int i = 0; i < le; i++ )
        {
            newS.append(" ");
        }
        newS.append(s);
        for ( int i = 0; i < le; i++ )
        {
            newS.append(" ");
        }
        return newS.toString();
    }

}
