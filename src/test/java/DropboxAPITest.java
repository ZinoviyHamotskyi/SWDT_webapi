import io.restassured.RestAssured;

import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;

import java.io.*;

import java.net.URL;

class FileDownloader {

}

public class DropboxAPITest {

    private final String token = "oqXBISqXi3oAAAAAAAAAAf72ZlomugEFRkksyA2m8IBFFOdCEC2roFvR7xQK7rto";
    private final String imageLocalPath = "kpi.png";

    private final String uploadURL = "https://content.dropboxapi.com/2/files/upload";
    private final String getMetadataURL = "https://api.dropboxapi.com/2/files/get_metadata";
    private final String deleteURL = "https://api.dropboxapi.com/2/files/delete_v2";

    @Test
    public void a_testUpload() throws IOException {

        JSONObject apiArg = new JSONObject();
        apiArg.put("mode","add");
        apiArg.put("autorename", true);
        apiArg.put("path", "/kpi.png");

        File file = new File(imageLocalPath);
        System.out.print(file);

        RestAssured.given()
                .headers("Dropbox-API-Arg", apiArg.toJSONString(),
                        "Content-Type","text/plain; " +
                                "charset=dropbox-cors-hack",
                        "Authorization", "Bearer " + token)
                .body(FileUtils.readFileToByteArray(file))
                .when().post(uploadURL)
                .then().statusCode(200);

        file.delete();
    }

    @Test
    public void b_testGetMetadata(){
        JSONObject requestParam = new JSONObject();
        requestParam.put("path","/kpi.png");
        requestParam.put("include_media_info",true);

        RestAssured.given()
                .headers("Authorization", "Bearer " + token,
                        "Content-Type","application/json")
                .body(requestParam.toJSONString())
                .when().post(getMetadataURL)
                .then().statusCode(200);
    }

    @Test
    public void c_testDelete(){
        JSONObject requestParam = new JSONObject();
        requestParam.put("path","/kpi.png");

        RestAssured.given()
                .headers("Authorization", "Bearer " + token,
                        "Content-Type","application/json")
                .body(requestParam.toJSONString())
                .when().post(deleteURL)
                .then().statusCode(200);

    }


}
