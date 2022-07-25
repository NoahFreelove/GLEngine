package GLEngine.Logging;

public class Logger {
    private static boolean enableLogging = true;

    public static void log(String msg){
        if(!enableLogging())
            return;
        System.out.println(msg);
    }
    public static void logError(String msg){
        if(!enableLogging())
            return;
        System.err.println(msg);
    }

    public static void log(String msg, LogType type){
        if(type == LogType.Error){
            logError(type + ": " + msg);
        }else
            log(type + ": " + msg);

    }


    public static boolean enableLogging() {
        return enableLogging;
    }

    public static void setEnableLogging(boolean enableLogging) {
        Logger.enableLogging = enableLogging;
    }
}

