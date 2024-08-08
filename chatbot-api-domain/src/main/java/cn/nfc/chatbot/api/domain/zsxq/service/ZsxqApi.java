package cn.nfc.chatbot.api.domain.zsxq.service;

import cn.nfc.chatbot.api.domain.zsxq.IZsxqApi;
import cn.nfc.chatbot.api.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;
import cn.nfc.chatbot.api.domain.zsxq.model.req.AnswerReq;
import cn.nfc.chatbot.api.domain.zsxq.model.req.ReqData;
import cn.nfc.chatbot.api.domain.zsxq.model.res.AnswerRes;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ZsxqApi implements IZsxqApi {

    private Logger logger = LoggerFactory.getLogger(ZsxqApi.class);
    @Override
    public UnAnsweredQuestionsAggregates queryUnAnsweredQuestionsTopicId(String groupId, String cookie) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet("https://api.zsxq.com/v2/groups/"+groupId+"/topics?scope=unanswered_questions&count=20");
        get.addHeader("cookie",cookie);
        get.addHeader("Content-Type","application/json;charset=UTF-8");

        CloseableHttpResponse response = httpClient.execute(get);
        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            String jsonStr = EntityUtils.toString(response.getEntity());
            logger.info("拉取提问数据。groupId：{} jsonStr：{}", groupId, jsonStr);
            return JSON.parseObject(jsonStr,UnAnsweredQuestionsAggregates.class);
        }else {
            throw new RuntimeException("queryUnAnsweredQuestionsTopicId Err is Code"+response.getStatusLine().getStatusCode());
        }

    }
    @Override
    public boolean answer(String groupId, String cookie, String topicId, String text, boolean silenced) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://api.zsxq.com/v2/topics/"+topicId+"/answer");
        post.addHeader("cookie",cookie);
        post.addHeader("Content-Type","application/json;charset=UTF-8");
        post.addHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:128.0) Gecko/20100101 Firefox/128.0");

        /*String paraJson ="{\n" +
                "\t\"req_data\": {\n" +
                "\t\t\"image_ids\": [],\n" +
                "\t\t\"text\": \"你好\\n\"\n" +
                "\t}\n" +
                "}";*/

        AnswerReq answerReq = new AnswerReq(new ReqData(text, silenced));
        String paraJson = JSONObject.toJSONString(answerReq);

        StringEntity stringEntity = new StringEntity(paraJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);
        CloseableHttpResponse response = httpClient.execute(post);
        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            String jsonStr = EntityUtils.toString(response.getEntity());
            logger.info("回答问题结果。groupId：{} topicId：{} jsonStr：{}", groupId, topicId, jsonStr);
            AnswerRes answerRes = JSON.parseObject(jsonStr, AnswerRes.class);
            return answerRes.isSucceeded();
        }else {
            throw new RuntimeException("answer Err is Code"+response.getStatusLine().getStatusCode());
        }

    }
}
