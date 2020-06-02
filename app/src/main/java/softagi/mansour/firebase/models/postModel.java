package softagi.mansour.firebase.models;

public class postModel
{
    private String userImage;
    private String userName;
    private long postTime;
    private String postText;
    private String postImage;
    private int type;
    private String postId;

    public postModel(String userImage, String userName, long postTime, String postText, String postImage, int type, String postId) {
        this.userImage = userImage;
        this.userName = userName;
        this.postTime = postTime;
        this.postText = postText;
        this.postImage = postImage;
        this.type = type;
        this.postId = postId;
    }

    public postModel() {
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}