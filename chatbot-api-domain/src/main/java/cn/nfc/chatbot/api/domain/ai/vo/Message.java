package cn.nfc.chatbot.api.domain.ai.vo;

public class Message
{
    private String content;

    private String role;

    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }
    public void setRole(String role){
        this.role = role;
    }
    public String getRole(){
        return this.role;
    }
}

