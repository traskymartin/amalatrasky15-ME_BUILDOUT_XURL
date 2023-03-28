package com.crio.shorturl;
import java.util.HashMap;
import java.util.Map;

public class XUrlImpl implements XUrl {
    
    private static final String BASE_SHORT_URL = "http://short.url/";
    private static final int SHORT_URL_LENGTH = 9;
    
    private Map<String, String> longToShortUrlMap;
    private Map<String, Integer> longUrlHitCountMap;

    public XUrlImpl() {
        this.longToShortUrlMap = new HashMap<>();
        this.longUrlHitCountMap = new HashMap<>();
    }

    @Override
    public String registerNewUrl(String longUrl) {
        if (longToShortUrlMap.containsKey(longUrl)) {
            return longToShortUrlMap.get(longUrl);
        }
        
        String shortUrl = generateShortUrl();
        longToShortUrlMap.put(longUrl, shortUrl);
        longUrlHitCountMap.put(longUrl, 0);
        
        return shortUrl;
    }
    
    @Override
    public String registerNewUrl(String longUrl, String shortUrl) {
        if (longToShortUrlMap.containsValue(shortUrl)) {
            return null;
        }
        
        longToShortUrlMap.put(longUrl, shortUrl);
        longUrlHitCountMap.put(longUrl, 0);
        
        return shortUrl;
    }

    @Override
    public String getUrl(String shortUrl) {
        for (Map.Entry<String, String> entry : longToShortUrlMap.entrySet()) {
            if (entry.getValue().equals(shortUrl)) {
                longUrlHitCountMap.put(entry.getKey(), longUrlHitCountMap.get(entry.getKey()) + 1);
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public String delete(String longUrl) {
        if (longToShortUrlMap.containsKey(longUrl)) {
            String shortUrl = longToShortUrlMap.remove(longUrl);
            longUrlHitCountMap.remove(longUrl);
            
            // remove reverse mapping
            for (Map.Entry<String, String> entry : longToShortUrlMap.entrySet()) {
                if (entry.getValue().equals(shortUrl)) {
                    longToShortUrlMap.remove(entry.getKey());
                    break;
                }
            }
        }
        return null;
    }

    @Override
    public Integer getHitCount(String longUrl) {
        return longUrlHitCountMap.getOrDefault(longUrl, 0);
    }
    
    private String generateShortUrl() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return BASE_SHORT_URL + sb.toString();
    }
}
