package cosc202.andie;

import cosc202.andie.actions.ActionCollection;
import cosc202.andie.actions.ImageAction;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;
//import java.util.prefs.Preferences;



/**
 * Actions provided by the languages menu
 * 
 * The languages menu contains actions to change the language of the GUI
 * These actions don't affect the image
 * 
 * @author Sola Woodhouse
 * @version 1.0
 */

public class MultilingualSupport implements ActionCollection {
    
    protected ArrayList<Action> actions;


    public MultilingualSupport() {
        actions = new ArrayList<Action>();
        actions.add(new ChangeEnglish("english", Icons.LANGUAGE_GLOBAL,"change_english", Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new ChangeSpanish("spanish", Icons.LANGUAGE_GLOBAL,"change_spanish", Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new ChangeGerman("german", Icons.LANGUAGE_GLOBAL,"change_german", Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new ChangePortuguese("portuguese", Icons.LANGUAGE_GLOBAL,"change_portuguese", Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new ChangeItalian("italian", Icons.LANGUAGE_GLOBAL,"change_italian", Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new ChangeChinese("chinese", Icons.LANGUAGE_GLOBAL,"change_chinese", Integer.valueOf(KeyEvent.VK_G)));
    }

    public JMenu createMenu() {
        SetLanguage language = SetLanguage.getInstance();
        JMenu fileMenu = new JMenu(language.getTranslated("language"));

        for(Action action: actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
    }

    @Override
    public ArrayList<Action> getToolbarActions() { return null; }

    public class ChangeEnglish extends ImageAction{

        ChangeEnglish(String name, String icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
    }

        public void actionPerformed(ActionEvent e) {
            SetLanguage lang =  SetLanguage.getInstance();
                lang.setLanguage(0);
        }

    }

    public class ChangeSpanish extends ImageAction{

    ChangeSpanish(String name, String icon, String desc, Integer mnemonic) {
        super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            SetLanguage lang =  SetLanguage.getInstance();
                lang.setLanguage(1);
        }

    }

    public class ChangeGerman extends ImageAction{

        ChangeGerman(String name, String icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }   

        public void actionPerformed(ActionEvent e) {
            SetLanguage lang =  SetLanguage.getInstance();
                lang.setLanguage(2);
        }

    }

    public class ChangePortuguese extends ImageAction{

        ChangePortuguese(String name, String icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            SetLanguage lang =  SetLanguage.getInstance();
                lang.setLanguage(3);
        }

    }
    public class ChangeItalian extends ImageAction{

        ChangeItalian(String name, String icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            SetLanguage lang =  SetLanguage.getInstance();
                lang.setLanguage(4);
        }

    }
    public class ChangeChinese extends ImageAction{

        ChangeChinese(String name, String icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            SetLanguage lang =  SetLanguage.getInstance();
                lang.setLanguage(5);
        }

    }
}
