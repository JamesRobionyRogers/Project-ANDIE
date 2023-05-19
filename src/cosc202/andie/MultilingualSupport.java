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
        SetLanguage language = SetLanguage.getInstance();
        actions = new ArrayList<Action>();
        actions.add(new ChangeEnglish(language.getTranslated("english"), Icons.LANGUAGE_GLOBAL,language.getTranslated("change_english"), Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new ChangeSpanish(language.getTranslated("spanish"), Icons.LANGUAGE_GLOBAL,language.getTranslated("change_spanish"), Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new ChangeGerman(language.getTranslated("german"), Icons.LANGUAGE_GLOBAL,language.getTranslated("change_german"), Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new ChangePortuguese(language.getTranslated("portuguese"), Icons.LANGUAGE_GLOBAL,language.getTranslated("change_portuguese"), Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new ChangeItalian(language.getTranslated("italian"), Icons.LANGUAGE_GLOBAL,language.getTranslated("change_italian"), Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new ChangeChinese(language.getTranslated("chinese"), Icons.LANGUAGE_GLOBAL,language.getTranslated("change_chinese"), Integer.valueOf(KeyEvent.VK_G)));
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

        ChangeEnglish(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
    }

        public void actionPerformed(ActionEvent e) {
            SetLanguage lang =  SetLanguage.getInstance();
                lang.setLanguage(0);
        }

    }

    public class ChangeSpanish extends ImageAction{

    ChangeSpanish(String name, ImageIcon icon, String desc, Integer mnemonic) {
        super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            SetLanguage lang =  SetLanguage.getInstance();
                lang.setLanguage(1);
        }

    }

    public class ChangeGerman extends ImageAction{

        ChangeGerman(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }   

        public void actionPerformed(ActionEvent e) {
            SetLanguage lang =  SetLanguage.getInstance();
                lang.setLanguage(2);
        }

    }

    public class ChangePortuguese extends ImageAction{

        ChangePortuguese(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            SetLanguage lang =  SetLanguage.getInstance();
                lang.setLanguage(3);
        }

    }
    public class ChangeItalian extends ImageAction{

        ChangeItalian(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            SetLanguage lang =  SetLanguage.getInstance();
                lang.setLanguage(4);
        }

    }
    public class ChangeChinese extends ImageAction{

        ChangeChinese(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            SetLanguage lang =  SetLanguage.getInstance();
                lang.setLanguage(5);
        }

    }
}
