package cosc202.andie;

import java.util.*;
import java.util.prefs.Preferences;

//class to set language, then each thing will come here to get their words.
//The language will default english, the button pressing will change the language for hopegfully all becuase all will refer to here for names.
//Have static instance, singleton class
/**
 * Language operation to set the different languages of the GUI
 * 
 * Different codes from the action refer to different languages to change to
 * 
 * @author Sola Woodhouse
 * @version 1.0
 */
public class SetLanguage {

    private String language;
    private String country;
    private Locale locale;
    private Preferences prefs = Preferences.userNodeForPackage(SetLanguage.class);
    private static SetLanguage lang = new SetLanguage();
    private ResourceBundle bundle;

        /**
         * Constructor - singleton class so has a static instance
         * sets default to english
         * gets first bundle
         */
    private SetLanguage() {
        locale = (new Locale(prefs.get("language", "en"), prefs.get("country", "NZ")));
        // language = "English";
        // country = "NZ";
        bundle = ResourceBundle.getBundle("cosc202.andie.LanguageBundles.LanguageBundle");
        //System.out.println(locale);
    }

    /**
     * Accessor of language instance from other classes
     * @return instance
     */
    public static SetLanguage getInstance() {
        return lang;
    }

    /**
     * Sets the language to what is selected by the code
     * 0 = English
     * 1 = Spanish
     * 2 = German
     * 3 = Portuguese
     * 4 = Italian
     * 5 = Chinese
     * 
     * once language is set, the main method of Andie.java is called @see Andie which then refreshes the GUI to the new language 
     * @param code
     */
    public void setLanguage(int code) {
        if (code == 0) {
            language = "en";
            country = "NZ";
            //System.out.println("English");
        } else if (code == 1) {
            language = "es";
            country = "ES";
            //System.out.println("Spanish");
        }else if (code==2){
            language = "de";
            country = "DE";
            //System.out.println("German");
        }else if (code==3){
            language = "pt";
            country = "BR";
            //System.out.println("Portugese");
        }else if (code==4){
            language = "it";
            country = "IT";
            //System.out.println("Italian");
        }else if (code==5){
            language = "zh";
            country = "CH";
            //System.out.println("Chinese");
        }
        prefs.put("language", language);
        prefs.put("country", country);
        locale = (new Locale(prefs.get("language", language), prefs.get("country", country)));
        //System.out.println(locale);
        bundle = ResourceBundle.getBundle("cosc202.andie.LanguageBundles.LanguageBundle", locale);
        try {
            Andie.main(null);
            //System.out.println("trying main");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Method to retrieve localised strings from other classes
     * takes string and gets the translated string from the correct LanguageBundle
     * returns the translated string 
     * 
     * All localised strings translated to ASCII code for accents and chinese characters from this site
     * https://www.lddgo.net/en/convert/string-unicode
     * @param translateThis
     * @return translated string
     */
    public String getTranslated(String translateThis) {

        String translated = bundle.getString(translateThis);
        //System.out.println(translated);
        return translated;
    }
}
