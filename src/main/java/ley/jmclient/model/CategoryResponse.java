package ley.jmclient.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class CategoryResponse {

    public static CategoryResponse read(InputStream json) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.fromJson(new InputStreamReader(json), CategoryResponse.class);
    }

    @Expose
    public int status;

    @Expose
    public List<Category> categories;

}
