package online.umbcraft.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class GreedyHashParser extends HashParser {

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
                    Set<String> allTokens = reader.lines().collect(Collectors.toSet());
                    matched = allTokens.contains(key);
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
