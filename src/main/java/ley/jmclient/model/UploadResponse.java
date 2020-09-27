package ley.jmclient.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class UploadResponse {

    public static UploadResponse read(InputStream json) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.fromJson(new InputStreamReader(json), UploadResponse.class);
    }

    @Expose
    public int status;

    @Expose
    public String error;

    @Expose
    public List<String> files;

}
