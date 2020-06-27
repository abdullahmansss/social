package softagi.mansour.firebase.models;

public class roomModel
{
    private String uId;
    private String ownerName;
    private String roomImage;
    private String roomId;
    private String roomTitle;
    private boolean isPrivate;

    public roomModel() {
    }

    public roomModel(String uId, String ownerName, String roomImage, String roomId, String roomTitle, boolean isPrivate) {
        this.uId = uId;
        this.ownerName = ownerName;
        this.roomImage = roomImage;
        this.roomId = roomId;
        this.roomTitle = roomTitle;
        this.isPrivate = isPrivate;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }
}