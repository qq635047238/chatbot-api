package cn.nfc.chatbot.api.test;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

public class ApiTest {

    @Test
    public void query_unanswered_questions() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpGet get = new HttpGet("https://api.zsxq.com/v2/groups/48888552121258/topics?scope=unanswered_questions&count=20");
        get.addHeader("cookie","zsxq_access_token=3B9E5CDF-0CFC-00E6-B1E0-2CCE6C595B58_4672211BFFF35815; zsxqsessionid=c800f6538440ab73a73fbb809092a87c; abtest_env=product");
        get.addHeader("Content-Type","application/json;charset=UTF-8");

        CloseableHttpResponse response = httpClient.execute(get);
        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        }else {
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }

    @Test
    public void answer() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();


        HttpPost post = new HttpPost("https://api.zsxq.com/v2/topics/2855185455888241/answer");
        post.addHeader("cookie","zsxq_access_token=5B57C34E-2735-59F1-4468-A9952373A044_4672211BFFF35815; zsxqsessionid=f239e523a21f1968a339d31f77afe30b; abtest_env=product");
        post.addHeader("Content-Type","application/json;charset=UTF-8");

        String paraJson ="{\n" +
                "\t\"req_data\": {\n" +
                "\t\t\"image_ids\": [],\n" +
                "\t\t\"text\": \"你好\\n\"\n" +
                "\t}\n" +
                "}";

        StringEntity stringEntity = new StringEntity(paraJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);
        CloseableHttpResponse response = httpClient.execute(post);
        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        }else {
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }
}
