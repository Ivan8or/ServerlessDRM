package online.umbcraft.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Future;

public class PlaintextKeyScraper extends KeyScraper {

    @Override
    public Future<Boolean> containsKey(final String targetURL, final String key) {

        return executor.submit(
                () -> {
                    URL url = new URL(targetURL);
                    URLConnection conn = url.openConnection();
                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isr);

                    String line = null;
                    boolean matched = false;
                    while ((line = reader.readLine()) != null) {

                        if(key.equals(line.strip())) {
                            matched = true;
                            break;
                        }
                    }
                    reader.close();
                    isr.close();
                    is.close();

                    return matched;
                }
        );

//        throw new MalformedURLException("url is not in the form of " +
//                "https://{site}/{path}/{to}/{file}.txt");
    }

    @Override
    public boolean validURL(String potentialURL) {

        return true;

    }
}
