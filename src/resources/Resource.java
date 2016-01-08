/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.image.Image;

/**
 *
 * @author JCHAUT
 */
public class Resource {

    public static final String LANGAGEPATH = "resources/language";
    public static final String TITLE = "3KLES Manager";
    public static final String VERSION = "1.0ß";
    public static final Image LOGO_ICON = new Image(Resource.class.getResourceAsStream("/resources/images/logo.png"));

    public static String AK = "iXnP0VCWD6MI5VMa";
    public static String AS = "6yR3SICd8ej513ORkRPt2TGfHp02o8Rh";
    public static String CK = "W5kg2j4VkSqUl1Pcs4pNYfjQ3RKTngW1";
    public static String DOMAIN = "3kles-consulting.com";
    public static String API = "https://eu.api.ovh.com/1.0/";

    /*Format Date*/
    public static final SimpleDateFormat sdfYMD8 = new SimpleDateFormat("YYYYmmdd");
    public static final SimpleDateFormat sdfYMD8_HHmmss = new SimpleDateFormat("YYYYmmdd_HHmmss");

    public static String parseContent(String content, String regex, int group) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        if (m.find()) {
            System.out.println(m.group(group));
            return m.group(group);
        }
        return null;
    }
}
