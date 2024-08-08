package cn.nfc.chatbot.api.domain.ai.aggregates;

import cn.nfc.chatbot.api.domain.ai.vo.Choices;
import cn.nfc.chatbot.api.domain.ai.vo.Usage;

import java.util.List;

public class AIAnswer {
    private List<Choices> choices;

    private int created;

    private String id;

    private String model;

    private String request_id;

    private Usage usage;

    public void setChoices(List<Choices> choices){
        this.choices = choices;
    }
    public List<Choices> getChoices(){
        return this.choices;
    }
    public void setCreated(int created){
        this.created = created;
    }
    public int getCreated(){
        return this.created;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setModel(String model){
        this.model = model;
    }
    public String getModel(){
        return this.model;
    }
    public void setRequest_id(String request_id){
        this.request_id = request_id;
    }
    public String getRequest_id(){
        return this.request_id;
    }
    public void setUsage(Usage usage){
        this.usage = usage;
    }
    public Usage getUsage(){
        return this.usage;
    }
}
