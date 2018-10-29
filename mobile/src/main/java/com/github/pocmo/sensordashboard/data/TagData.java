package com.github.pocmo.sensordashboard.data;

public class TagData {
    private String tagName;
    private long timestamp;

    public TagData(String pTagName, long pTimestamp) {
        tagName = pTagName;
        timestamp = pTimestamp;
    }

    public String getTagName() {
        return tagName;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
