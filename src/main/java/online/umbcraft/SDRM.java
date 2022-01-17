package online.umbcraft;

import online.umbcraft.hash.HashHelper;
import online.umbcraft.web.GreedyHashParser;
import online.umbcraft.web.HashParserType;
import online.umbcraft.web.HashParser;
import online.umbcraft.web.StreamedHashParser;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SDRM {

    private String documentURL;
    final private Map<String, Future<Boolean>> hashCache = new ConcurrentHashMap<>();
    private HashParserType parserType = HashParserType.GREEDY;

    final private Map<HashParserType, HashParser> parsers =
            Map.of( HashParserType.GREEDY, new GreedyHashParser(),
                    HashParserType.STREAMED, new StreamedHashParser());

    public SDRM() {
    }

    public SDRM(String documentURL) {
        setDocumentURL(documentURL);
    }

    public SDRM(HashParserType parserType) {
        setParserType(parserType);
    }

    public SDRM(String documentURL, HashParserType parserType) {
        setDocumentURL(documentURL);
        setParserType(parserType);
    }

    public void setDocumentURL(String documentURL) {
        this.documentURL = documentURL;
        hashCache.clear();
    }

    public void setParserType(HashParserType type) {
        parserType = type;
    }

    public void clearCache() {
        hashCache.clear();
    }

    public void closeParserExecutors() {
        HashParser.getExecutorService().shutdown();
    }

    public Future<Boolean> validateKey(String plaintext, String documentURL, HashParserType parserType) throws MalformedURLException {
        String keyHash = HashHelper.getHash(plaintext);
        if(hashCache.containsKey(keyHash))
            return hashCache.get(keyHash);

        HashParser parser = parsers.get(parserType);
        Future<Boolean> isValid = parser.containsKey(documentURL, keyHash);
        hashCache.put(keyHash, isValid);
        return isValid;
    }

    public Future<Boolean> validateKey(String plaintext, HashParserType parserType) throws MalformedURLException {
        return validateKey(plaintext, documentURL, parserType);
    }

    public Future<Boolean> validateKey(String plaintext, String documentURL) throws MalformedURLException {
        return validateKey(plaintext, documentURL, parserType);
    }

    public Future<Boolean> validateKey(String plaintext) throws MalformedURLException {
        return validateKey(plaintext, documentURL, parserType);
    }
}
