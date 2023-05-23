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
        try {
            Locale.setDefault(new Locale(prefs.get("language", "en"), prefs.get("country", "NZ")));
            bundle = ResourceBundle.getBundle("cosc202.andie.LanguageBundles.LanguageBundle");
        }
        // Catching exception when the user has a different primary language - tested on MacOS
        catch (MissingResourceException missingResource) {
            // TODO: Throwing java.lang.NullPointerException & java.lang.ExceptionInInitializerError
            // ExceptionHandler.displayError("Your primary language is not supported by ANDIE. Defaulting language to New Zealand English.");
            
            // Setting the bundle to the default NZ English
            bundle = ResourceBundle.getBundle("cosc202.andie.LanguageBundles.LanguageBundle", new Locale("en", "NZ"));
        }
        // Catching is baseName or locale is null in the bundle
        catch (NullPointerException nullPointer) {
            ExceptionHandler.debugException(nullPointer);
        }

        // Catching any other exception
        catch (Exception e) {
            ExceptionHandler.debugException(e);
        }
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
        switch(code){
            case 0: 
                language = "en";
                country = "NZ";
                break;
            case 1:
                language = "es";
                country = "ES";
                break;
            case 2:
                language = "de";
                country = "DE";
                break;
            case 3:
                language = "pt";
                country = "BR";
                break;
            case 4:
                language = "it";
                country = "IT";
                break;
            case 5:
                language = "zh";
                country = "CH";
                break;

        }
        prefs.put("language", language);
        prefs.put("country", country);
        locale = (new Locale(prefs.get("language", language), prefs.get("country", country)));
        bundle = ResourceBundle.getBundle("cosc202.andie.LanguageBundles.LanguageBundle", locale);
        try {
            Andie.main(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            ExceptionHandler.displayError(SetLanguage.getInstance().getTranslated("general_error"));
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

        try {
            String translated = bundle.getString(translateThis);
            return translated;
        } catch (MissingResourceException e) {
            ExceptionHandler.displayError("Untranslated feature: " + translateThis + "\nProceeding with no translation");
            return translateThis;
        }
    }
}
