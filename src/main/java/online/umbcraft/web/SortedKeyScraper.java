package online.umbcraft.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class SortedKeyScraper extends KeyScraper {

    @Override
    public Future<Boolean> containsKey(final String targetURL, final String key) {

        return executor.submit(
                () -> {
                    URL url = new URL(targetURL);
                    URLConnection conn = url.openConnection();
                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isr);

                    String line = reader.readLine();
                    if(!line.equals("----------BEGIN KEYS----------")) {
                        throw new MalformedURLException("Invalid Document at URL: " + targetURL);
                    }
                    boolean matched = false;
                    Object[] allTokens = reader.lines().toArray();
                    int potentialIndex = Arrays.binarySearch(allTokens, key);

                    if(     potentialIndex >= 0 &&
                            potentialIndex < allTokens.length &&
                            allTokens[potentialIndex].equals(key))
                        matched = true;

                    reader.close();
                    isr.close();
                    is.close();

                    return matched;
                }
        );
    }

    @Override
    public boolean validURL(String potentialURL) {

        return true;

    }
}
