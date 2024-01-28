package src.test.tests;

import com.google.common.io.Files;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class APICaller extends TestBases {
    static ConfigurationReader CR = new ConfigurationReader();

    public static Response Public_GetAPI_Caller(String URL, String Path, List<String> Requestheaders) throws Exception {
        RestAssured.baseURI = URL;
        RequestSpecification request = RestAssured.given();
        Response response;
        if (!Requestheaders.isEmpty()) {
            for (int count = 1; count < Requestheaders.size(); count += 2) {
                Path = Path + Requestheaders.get(count - 1) + "=" + Requestheaders.get(count) + "&";

            }

        }

        System.out.println("Testing Started for: " + URL + Path);
        response = request.headers("user-agent", "Application").auth().basic("unicc", "5NJjoVm-RV8u9Qun4hnt").given().when().get(Path);


        System.out.println("Status code: " + response.getStatusCode());

        return response;
    }

}