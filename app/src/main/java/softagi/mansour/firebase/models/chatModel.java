package softagi.mansour.firebase.models;

public class chatModel
{
    private String senderId;
    private String message;
    private long time;
    private int type;
    private String id;

    public chatModel(String senderId, String message, long time, int type, String id) {
        this.senderId = senderId;
        this.message = message;
        this.time = time;
        this.type = type;
        this.id = id;
    }

    public chatModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}