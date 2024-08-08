package cn.nfc.chatbot.api.domain.ai.service;

import cn.nfc.chatbot.api.domain.ai.IZpqy;
import cn.nfc.chatbot.api.domain.ai.aggregates.AIAnswer;
import cn.nfc.chatbot.api.domain.ai.vo.Choices;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Zpqy implements IZpqy {


    @Value("${chatbot-api.Glm4ApiKey}")
    private String apiKey;

    @Override
    public String doGlm4(String question) throws IOException {
        long expSeconds = 3600; // Token expiration time in seconds

        String token = generateToken(apiKey, expSeconds);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://open.bigmodel.cn/api/paas/v4/chat/completions");
        post.addHeader("Authorization", "Bearer " + token);
        post.addHeader("Content-Type", "application/json");

        String paraJson = "{\n" +
                "    \"model\": \"glm-4\",\n" +
                "    \"messages\": [\n" +
                "        {\n" +
                "            \"role\": \"user\",\n" +
                "            \"content\": \""+question+"\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        StringEntity stringEntity = new StringEntity(paraJson, ContentType.APPLICATION_JSON);
        post.setEntity(stringEntity);

        try (CloseableHttpResponse response = httpClient.execute(post)) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String jsonStr = EntityUtils.toString(response.getEntity());
                AIAnswer aiAnswer = JSON.parseObject(jsonStr, AIAnswer.class);
                StringBuilder answer = new StringBuilder();
                List<Choices> choices = aiAnswer.getChoices();
                for (Choices choice : choices) {
                    answer.append(choice.getMessage().getContent());
                }
                return answer.toString();
            } else {
                throw new RuntimeException("answer Err is Code"+response.getStatusLine().getStatusCode());
            }
        }

    }

    private String generateToken(String apiKey, long expSeconds) {
        try {
            String[] keyParts = apiKey.split("\\.");
            String id = keyParts[0];
            String secret = keyParts[1];

            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            Date exp = new Date(nowMillis + expSeconds * 1000);

            Algorithm algorithm = Algorithm.HMAC256(secret);

            // 使用HashMap替代Map.of
            Map<String, Object> header = new HashMap<>();
            header.put("alg", "HS256");
            header.put("sign_type", "SIGN");

            return JWT.create()
                    .withHeader(header)
                    .withClaim("api_key", id)
                    .withClaim("exp", exp.getTime())
                    .withClaim("timestamp", now.getTime())
                    .sign(algorithm);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate token", e);
        }
    }


}
