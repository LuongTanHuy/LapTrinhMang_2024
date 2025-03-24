package laptrinhmang.ltm_chat.ModelChat;

import laptrinhmang.ltm_chat.model.Message;

public class Friend_ListMessage extends Message {

    private int receiverID,userID;

    public Friend_ListMessage(String message, String sendTime, String type, String path,int receiverID, int userID) {
        super(message, sendTime, type, path);
        this.receiverID = receiverID;
        this.userID = userID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }


}
