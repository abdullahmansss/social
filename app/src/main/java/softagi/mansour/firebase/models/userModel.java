package softagi.mansour.firebase.models;

public class userModel
{
    private String name;
    private String email;
    private String mobile;
    private String address;
    private String imageUrl;
    private String Uid;

    public userModel(String name, String email, String mobile, String address, String imageUrl, String uid) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.imageUrl = imageUrl;
        Uid = uid;
    }

    public userModel() {
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}