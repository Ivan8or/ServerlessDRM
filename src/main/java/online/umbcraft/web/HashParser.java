package online.umbcraft.web;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class HashParser {

    protected static ExecutorService executor = Executors.newCachedThreadPool();

    public static void setExecutorService(ExecutorService newExecutor) {
        executor = newExecutor;
    }

    public static ExecutorService getExecutorService() {
        return executor;
    }

    public abstract Future<Boolean> containsKey(String targetURL, String base64hash) throws MalformedURLException;


    public abstract boolean validURL(String potentialURL);

}
