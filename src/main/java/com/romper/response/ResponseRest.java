package com.romper.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResponseRest {
    private final ArrayList<HashMap<String, String>> metadata = new ArrayList<>();

    public List<HashMap<String, String>> getMetadata() {
        return metadata;
    }

    public void setMetadata(String type, String code, String data) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("code", code);
        map.put("data", data);

        metadata.add(map);
    }
}