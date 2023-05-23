package cosc202.andie.actions.tool;

/**
 * <p>
 * Function that rolls
 * 
 * Supports functionality on mac, windows and linux
 * 
 * </p>
 * 
 * @see https://www.youtube.com/watch?v=dQw4w9WgXcQ
 * @author Jess Tyrrell
 * @version 1.0
 */

public class Roll{

    public Roll(){};

    public void applyRoll(){

        Runtime rt = Runtime.getRuntime();
        String url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        String operatingSystem = System.getProperty("os.name").toLowerCase();

        if(operatingSystem.indexOf("win") >= 0){
            try{
            
            rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            }
            catch (Exception ex){
            }
        }

        else if(operatingSystem.indexOf("mac") >= 0){
        try{
            rt.exec("open " + url);
            }
            catch (Exception ex){
            }

        } else{
            try{
                String[] browsers = { "google-chrome", "firefox", "mozilla", "epiphany", "konqueror",
                                 "netscape", "opera", "links", "lynx" };
 
                StringBuffer cmd = new StringBuffer();
                    for (int i = 0; i < browsers.length; i++){
                            if(i == 0){
                                cmd.append(String.format("%s \"%s\"", browsers[i], url));
                    }else{
                                cmd.append(String.format(" || %s \"%s\"", browsers[i], url)); 
                    }
                    rt.exec(new String[] { "sh", "-c", cmd.toString() });
                }
            } catch (Exception e){

            }
        }}
    }



