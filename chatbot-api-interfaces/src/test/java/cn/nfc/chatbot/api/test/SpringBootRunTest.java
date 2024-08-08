package cn.nfc.chatbot.api.test;

import cn.nfc.chatbot.api.domain.ai.IZpqy;
import cn.nfc.chatbot.api.domain.zsxq.IZsxqApi;
import cn.nfc.chatbot.api.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;
import cn.nfc.chatbot.api.domain.zsxq.model.vo.Topics;
import cn.nfc.chatbot.api.domain.zsxq.service.ZsxqApi;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRunTest {

    private Logger logger = LoggerFactory.getLogger(SpringBootRunTest.class);
    @Value("${chatbot-api.groupId}")
    private String groupId;

    @Value("${chatbot-api.cookie}")
    private String cookie;

    @Resource
    private IZsxqApi zsxqApi;
    @Resource
    private IZpqy zpqy;

    @Test
    public void text_zsxqApi()throws IOException{
        UnAnsweredQuestionsAggregates unAnsweredQuestionsAggregates = zsxqApi.queryUnAnsweredQuestionsTopicId(groupId, cookie);
        logger.info("测试结果：{}", JSON.toJSONString(unAnsweredQuestionsAggregates));

        List<Topics> topics = unAnsweredQuestionsAggregates.getResp_data().getTopics();

        for (Topics topic : topics) {
            String topicId = topic.getTopic_id();
            String text = topic.getQuestion().getText();
            logger.info("topicId:{} text:{}",topics,text);

            zsxqApi.answer(groupId,cookie,topicId,text,false);
        }
    }

    @Test
    public void testZpqy() throws IOException{
        String response = zpqy.doGlm4("帮我写一个java冒泡排序");
        logger.info("测试结果:{}",response);
    }
}
