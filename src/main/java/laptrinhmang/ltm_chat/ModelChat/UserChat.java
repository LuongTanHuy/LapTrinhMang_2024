package laptrinhmang.ltm_chat.ModelChat;

public class UserChat {
    private int userID, phone;
    private String fullName,message, sendTime,typeMessage, path;

    public UserChat(int userID, String fullName, String message, String sendTime, String typeMessage, String path, int phone){
        this.userID = userID;
        this.fullName = fullName;
        this.message = message;
        this.sendTime = sendTime;
        this.typeMessage = typeMessage;
        this.path = path;
        this.phone = phone;
    }

    public UserChat(int userID, String fullName, String message, String sendTime, String typeMessage, int phone){
        this.userID = userID;
        this.fullName = fullName;
        this.message = message;
        this.sendTime = sendTime;
        this.typeMessage = typeMessage;
        this.phone = phone;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
}
