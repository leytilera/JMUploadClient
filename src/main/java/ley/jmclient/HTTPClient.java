package ley.jmclient;

import ley.jmclient.model.CategoryResponse;
import ley.jmclient.model.UploadResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;

public class HTTPClient {

    public static String UploadURL = "https://data.tilera.xyz/api/jensmemes/upload";
    public static String CategoryURL = "https://data.tilera.xyz/api/jensmemes/categories";

    public static UploadResponse upload(String token, String category, File image) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(UploadURL);
        MultipartEntityBuilder mpbuild = MultipartEntityBuilder.create();
        mpbuild.addTextBody("category", category);
        mpbuild.addTextBody("token", token);
        mpbuild.addBinaryBody("file", image);
        request.setEntity(mpbuild.build());
        HttpEntity response = client.execute(request).getEntity();
        return UploadResponse.read(response.getContent());
    }

    public static CategoryResponse categories() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(CategoryURL);
        HttpEntity response = client.execute(request).getEntity();
        return CategoryResponse.read(response.getContent());
    }

}
