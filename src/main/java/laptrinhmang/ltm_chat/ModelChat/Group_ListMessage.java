package laptrinhmang.ltm_chat.ModelChat;

import laptrinhmang.ltm_chat.model.Message;

public class Group_ListMessage extends Message {

private String fullName,nameRoom;
private int token,userID;

    public Group_ListMessage(String message, String sendTime, String type, String path, String fullName, int token, String nameRoom,int userID) {
        super(message, sendTime, type, path);
        this.fullName = fullName;
        this.token = token;
        this.nameRoom = nameRoom;
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
