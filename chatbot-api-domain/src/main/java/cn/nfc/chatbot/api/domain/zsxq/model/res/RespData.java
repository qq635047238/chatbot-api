package cn.nfc.chatbot.api.domain.zsxq.model.res;

import cn.nfc.chatbot.api.domain.zsxq.model.vo.Topics;

import java.util.List;

public class RespData {

    private List<Topics> topics;

    public List<Topics> getTopics() {
        return topics;
    }

    public void setTopics(List<Topics> topics) {
        this.topics = topics;
    }
}
