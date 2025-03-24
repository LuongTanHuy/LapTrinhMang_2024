package laptrinhmang.ltm_chat.service.Client;

import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import laptrinhmang.ltm_chat.Group;
import laptrinhmang.ltm_chat.ModelChat.Friend_ListMessage;
import laptrinhmang.ltm_chat.ModelChat.Group_ListMessage;
import laptrinhmang.ltm_chat.controller.AddGroupController;
import laptrinhmang.ltm_chat.controller.ClientController;
import laptrinhmang.ltm_chat.controller.EditGroupNameController;
import laptrinhmang.ltm_chat.model.Room;
import laptrinhmang.ltm_chat.model.User;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class Client {
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private ClientController chatCTL;
    private List<User> listFriend = new ArrayList<>();
    private List<Room> listRoom = new ArrayList<>();
    private AddGroupController addGroupController;
    private EditGroupNameController editGroupNameController;
    private int currentUserID;

    public int getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(int userID) {
        this.currentUserID = userID;
    }

    public Client(String serverAddress, ClientController chatCTL)
    {
        try {
            socket = new Socket(serverAddress, 8888);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            this.chatCTL = chatCTL;

            // Thread để nhận tin nhắn từ server
            new Thread(() -> {
                while (true) {
                    try {
                        int messageCount = input.readInt();
                        String responseType = input.readUTF();

                        if (messageCount != 0) {
                            switch (responseType) {
                                case "SignUp":
                                case "Login":
                                    handleResponseLoginAndSignUp(messageCount);
                                    break;
                                case "ListFriend":
                                    handleResponseListFriend(messageCount);
                                    break;
                                case "ListGroup":
                                    handleResponseListGroup(messageCount);
                                    break;
                                case "CreateGroupResponse":
                                    handleCreateGroupResponse();
                                    break;
                                case "GroupCreatedNotification":
                                    handleGroupCreatedNotification();
                                    break;
                                case "RemoveGroupResponse":
                                    handleRemoveGroupResponse();
                                    break;
                                case "UpdateGroupNameResponse":
                                    handleUpdateGroupNameResponse();
                                    break;
                                case "Search":
                                    handleSearchResponse(messageCount);
                                    break;
                                case "GroupNameUpdatedNotification":
                                    handleGroupNameUpdatedNotification();
                                    break;
                                case "UpdateAddGroupMembers":
                                    handleUpdateAddGroupMembers();
                                    break;
                                case "RemoveMemberGroupResponse":
                                    handleRemoveMemberGroupResponse();
                                    break;
                                case "GroupRemovedNotification":
                                    handleGroupRemovedNotification();
                                    break;
                                case "UpdateGroupMembers":
                                    handleUpdateGroupMembers();
                                    break;
                                case "ChatOneToMany":
                                    handleResponseChatOneToMany();
                                    break;
                                case "ChatOneToOne":
                                    handleResponseChatOneToOne();
                                    break;
                                case "ListChatOneToOne":
                                    handleResponseListChatFriend(messageCount);
                                    break;
                                case "ListChatOneToMany":
                                    handleResponseListChatGroup(messageCount);
                                    break;
                                case "FileOneToOne":
                                    handleFileOneToOne();
                                    break;
                                case "FileOneToMany":
                                    handleFileOneToMany();
                                    break;
                                case "DownFile":
                                    handleDownFile();
                                    break;
                            }

                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteGroupRequest(String requestType, Map<String, Object> requestData) {
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(requestData);
            output.writeInt(1);
            output.writeUTF(requestType);
            output.writeUTF(jsonData);
            output.flush();

            System.out.println("Request sent: " + requestType + ", Data: " + jsonData);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to send delete group request: " + e.getMessage());
        }
    }

    private void handleRemoveMemberGroupResponse() {
        try {
            String jsonResponse = input.readUTF();

            // Parse JSON
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> responseMap = gson.fromJson(jsonResponse, type);

            int token = ((Double) responseMap.get("token")).intValue();
            boolean success = (Boolean) responseMap.get("success");
            String nameRoom = (String) responseMap.get("nameRoom");
            List<Double> remainingMembersDouble = (List<Double>) responseMap.get("remainingMembers");
            List<Integer> remainingMembers = remainingMembersDouble.stream().map(Double::intValue).collect(Collectors.toList());

            if (success) {
                Platform.runLater(() -> {
                    chatCTL.updateGroupMemberCount(nameRoom, String.valueOf(remainingMembers.size()), token);
                });
            } else {
                System.err.println("Failed to remove members from group: " + nameRoom);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error in handleRemoveMemberGroupResponse: " + e.getMessage());
        }
    }

    private void handleUpdateGroupMembers() {
        try {
            String notifyJson = input.readUTF();

            // Parse JSON
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> notifyMap = gson.fromJson(notifyJson, type);

            int token = ((Double) notifyMap.get("token")).intValue();
            String nameRoom = (String) notifyMap.get("nameRoom");
            List<Double> remainingMembersDouble = (List<Double>) notifyMap.get("remainingMembers");
            List<Integer> remainingMembers = remainingMembersDouble.stream().map(Double::intValue).collect(Collectors.toList());

            Platform.runLater(() -> {
                chatCTL.updateGroupMemberCount(nameRoom, String.valueOf(remainingMembers.size()), token);
            });
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error in handleUpdateGroupMembers: " + e.getMessage());
        }
    }

//    private void handleRemoveMemberGroupResponse() {
//        try {
//            String jsonResponse = input.readUTF();
//
//            Gson gson = new Gson();
//            Type type = new TypeToken<Map<String, Object>>() {}.getType();
//            Map<String, Object> responseMap = gson.fromJson(jsonResponse, type);
//
//            // Lấy dữ liệu từ phản hồi
//            int token = ((Double) responseMap.get("token")).intValue();
//            boolean success = (boolean) responseMap.get("success");
//            String nameRoom = (String) responseMap.get("nameRoom");
//            List<Double> remainingMembersDouble = (List<Double>) responseMap.get("remainingMembers");
//
//            List<Integer> remainingMembers = remainingMembersDouble.stream().map(Double::intValue).collect(Collectors.toList());
//
//            if (success) {
//                Platform.runLater(() -> {
//                    String memberGroupCount = String.valueOf(remainingMembers.size());
//                    chatCTL.updateGroupMemberCount(nameRoom, memberGroupCount, token);
//                    System.out.println("Cập nhật nhóm thành công: Tên nhóm: " + nameRoom + ", Số thành viên: " + memberGroupCount);
//                });
//            } else {
//                System.err.println("Không thể xóa thành viên khỏi nhóm với token: " + token);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.err.println("Lỗi khi xử lý phản hồi RemoveMemberGroupResponse: " + e.getMessage());
//        }
//    }

    public void addGroupRequest(String requestType, Map<String, Object> requestData) {
        try {
            Gson gson = new Gson();
            String jsonData = gson.toJson(requestData);
            output.writeInt(1);
            output.writeUTF(requestType);
            output.writeUTF(jsonData);
            output.flush();
            System.out.println("Request sent: " + requestType + ", Data: " + jsonData);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to send group request: " + e.getMessage());
        }
    }

    private void handleUpdateAddGroupMembers() {
        try {
            String jsonResponse = input.readUTF();

            // Parse JSON response
            Gson gson = new Gson();
            Type responseType = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> responseMap = gson.fromJson(jsonResponse, responseType);

            String nameRoom = (String) responseMap.get("nameRoom");
            int token = ((Double) responseMap.get("token")).intValue();
            List<Double> memberIdsDouble = (List<Double>) responseMap.get("memberIds");
            List<Integer> memberIds = memberIdsDouble.stream().map(Double::intValue).collect(Collectors.toList());

            Platform.runLater(() -> chatCTL.updateGroupMemberCount(nameRoom, String.valueOf(memberIds.size()), token));
            System.out.println("Handled UpdateGroupMembers: " + responseMap);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error handling UpdateGroupMembers: " + e.getMessage());
        }
    }

    public void sendSearchRequest(String keyword) {
        try {
            System.out.println("Sending search request with keyword: " + keyword);
            output.writeInt(1);
            output.writeUTF("Search");
            output.writeUTF(keyword);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleSearchResponse(int messageCount) throws IOException {
        List<String> searchResults = new ArrayList<>();
        for (int i = 0; i < messageCount; i++) {
            searchResults.add(input.readUTF());
        }
        Platform.runLater(() -> {
            chatCTL.updateSearchResults(searchResults);
        });
    }

    private void handleUpdateGroupNameResponse() {
        try {
            boolean isUpdated = input.readBoolean();
            int token = input.readInt();
            String groupName = input.readUTF();

            int memberCount = input.readInt();
            List<Integer> userIDs = new ArrayList<>();
            for (int i = 0; i < memberCount; i++) {
                userIDs.add(input.readInt());
            }

            if (isUpdated) {
                System.out.println("Group name updated successfully!");

                Platform.runLater(() -> {
                    chatCTL.updateCardGroup(groupName, String.valueOf(memberCount), token);
                });
            } else {
                System.out.println("Failed to update group name.");
            }
        } catch (IOException e) {
            System.out.println("Error in handleUpdateGroupNameResponse: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleGroupNameUpdatedNotification() {
        try {
            String jsonResponse = input.readUTF();

            // Parse JSON response
            Gson gson = new Gson();
            Type responseType = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> notifyMap = gson.fromJson(jsonResponse, responseType);

            int token = ((Double) notifyMap.get("token")).intValue();
            String newGroupName = (String) notifyMap.get("newGroupName");

            Platform.runLater(() -> chatCTL.updateCardGroup(newGroupName, null, token));

            System.out.println("Group name updated notification handled for token: " + token);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error handling GroupNameUpdatedNotification: " + e.getMessage());
        }
    }

    private void handleRemoveGroupResponse() {
        try {
            boolean success = input.readBoolean();
            int groupToken = input.readInt();

            if (success) {
                chatCTL.removeCardGroup(groupToken);
            } else {
                System.out.println("Xóa nhóm thất bại cho token: " + groupToken);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleGroupRemovedNotification() {
        try {
            String jsonResponse = input.readUTF();

            Gson gson = new Gson();
            Type responseType = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> notifyMap = gson.fromJson(jsonResponse, responseType);

            int groupToken = ((Double) notifyMap.get("token")).intValue();

            Platform.runLater(() -> chatCTL.removeCardGroup(groupToken));

            System.out.println("Group removed notification handled for token: " + groupToken);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error handling GroupRemovedNotification: " + e.getMessage());
        }
    }

    public void sendRemoveGroupRequest(int token) {
        try {
            output.writeInt(1);
            output.writeUTF("RemoveGroup");
            output.writeInt(token);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateGroupName(int token, String newGroupName) {
        try {
            output.writeInt(1);
            output.writeUTF("UpdateGroupName");
            output.writeInt(token);
            output.writeUTF(newGroupName);
            output.flush();
        } catch (IOException e) {
            System.out.println("Error while updating group name: " + e.getMessage());
        }
    }

    public void sendGroupRequest(String messageType, Map<String, Object> requestMap) {
        try {
            Gson gson = new Gson();
            // Chuyển requestMap sang JSON
            String jsonRequest = gson.toJson(requestMap);
            output.writeInt(1);
            output.writeUTF(messageType);
            output.writeUTF(jsonRequest);
            output.flush();
            System.out.println("Gửi json đến server: " + messageType + " - " + jsonRequest);
        } catch (IOException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
    }

    private void handleCreateGroupResponse() {
        try {
            String responseJson = input.readUTF();
            Gson gson = new Gson();
            Map<String, Object> response = gson.fromJson(responseJson, Map.class);

            // Trích xuất thông tin từ phản hồi
            String status = (String) response.get("status");
            String message = (String) response.get("message");
            String groupName = (String) response.get("groupName");
            Double memberCountDouble = (Double) response.get("memberCount");
            Integer token = ((Double) response.get("token")).intValue();
            int memberCount = memberCountDouble.intValue();

            // Kiểm tra trạng thái phản hồi
            if ("success".equals(status)) {
                Platform.runLater(() -> {
                    chatCTL.cardGroup(groupName, String.valueOf(memberCount), token);
                });
            } else {
                System.err.println("Tạo nhóm thất bại: " + message);
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc phản hồi từ server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleGroupCreatedNotification() {
        try {
            String notifyJson = input.readUTF();

            Gson gson = new Gson();
            Type notifyType = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> notifyMap = gson.fromJson(notifyJson, notifyType);

            int token = ((Double) notifyMap.get("token")).intValue();
            String groupName = (String) notifyMap.get("groupName");
            int memberCount = ((Double) notifyMap.get("memberCount")).intValue();

            Platform.runLater(() -> {
                chatCTL.cardGroup(groupName, String.valueOf(memberCount), token);
            });

            System.out.println("Group created notification handled: " + groupName);

        } catch (IOException e) {
            System.err.println("Error handling GroupCreatedNotification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //    public void sendSearchRequest(String keyword) {
    //        try {
    //            output.writeInt(1);
    //            output.writeUTF("Search");
    //            output.writeUTF(keyword);
    //            output.flush();
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //    }
    //

    private void handleDownFile() {
        try {
            Long fileLength = input.readLong();
            String fileName = input.readUTF();

            if (fileLength > 0) {
                FileOutputStream fileOutputStream = new FileOutputStream("D:\\" + fileName);
                byte[] buffer = new byte[4096];
                int bytesRead;
                long totalBytesRead = 0;

                while ((bytesRead = input.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                    if (totalBytesRead >= fileLength) {
                        break;
                    }
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void requestDownFile(String path,String message){
        try {
          List<String> list = new ArrayList<>();
          list.add("DownFile");
          list.add(path);
          list.add(message);

          this.sendRequest(list);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleFileOneToMany(){
        try {
            String fileName = input.readUTF();
            String path = input.readUTF();
            String typeMessage = input.readUTF();
            String sendTime = input.readUTF();
            String fullName = input.readUTF();
            int userID = Integer.parseInt(input.readUTF());
            int token = Integer.parseInt(input.readUTF());
            String nameGroup = input.readUTF();

            this.chatCTL.addMessageGroup(new Group_ListMessage(fileName,sendTime,typeMessage,path,fullName,token,nameGroup,userID));
            if(this.chatCTL.room.getToken() == token){
                Platform.runLater(()-> {this.chatCTL.addChatOneToMany("Left",new Group_ListMessage(fileName,sendTime,typeMessage,path,fullName,token,nameGroup,userID));});
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleFileOneToOne(){
        try {
            String fileName = input.readUTF();
            String sendTime = input.readUTF();
            String typeMessage = input.readUTF();
            String path = input.readUTF();
            int receiverID = Integer.parseInt(input.readUTF());

            this.chatCTL.addMessageFriend(new Friend_ListMessage(fileName,sendTime,typeMessage,path,receiverID,this.chatCTL.getUser().getID()));
            if(this.chatCTL.receiver.getID() == receiverID){
                Platform.runLater(()-> {this.chatCTL.addChatOneToOne("Left",new Friend_ListMessage(fileName,sendTime,typeMessage,path,receiverID,this.chatCTL.getUser().getID()));});
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendFileToServer(File file,String type) {
        try {
            output = new DataOutputStream(socket.getOutputStream());
            FileInputStream fileInputStream = new FileInputStream(file);
            String fileName = file.getName();
            String path = "D:\\DACS4\\LTM_CHAT\\src\\main\\resources\\laptrinhmang\\ltm_chat\\"+chatCTL.getUser().getID()+"\\"+fileName;

            output.writeInt(1);
            output.writeUTF("sendFileToServer");
            output.writeUTF(fileName);// Gửi tên file
            output.writeLong(file.length());// Gửi kích thước file

            // Gửi nội dung file
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }

            switch (type){
                case "OneToOne":
                    this.chatCTL.sendFileChatOneToOne(fileName,path);
                    break;
                case "OneToMany":
                    this.chatCTL.sendFileChatOneToMany(fileName,path);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleResponseListChatFriend(int messageCount)
    {
        try {
            List<Friend_ListMessage> listChat = new ArrayList<>();
            //Read response
            for (int i=0;i< messageCount;i++){
                String message = input.readUTF();
                String sendTime = input.readUTF();
                String typeMessage = input.readUTF();
                String path = input.readUTF();
                int receiverID = Integer.parseInt(input.readUTF());
                int userID = Integer.parseInt(input.readUTF());

                listChat.add(new Friend_ListMessage(message,sendTime,typeMessage,path,receiverID,userID));
            }

            this.chatCTL.setListMessageFriend(listChat);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleResponseListChatGroup(int messageCount) {
        try {
            List<Group_ListMessage> listChat = new ArrayList<>();
            //Read response
            for (int i = 0; i < messageCount; i++) {
                int userID = Integer.parseInt(input.readUTF());
                String fullName = input.readUTF();
                String message = input.readUTF();
                String sendTime = input.readUTF();
                String typeMessage = input.readUTF();
                String path = input.readUTF();
                String nameRoom  = input.readUTF();
                int token = Integer.parseInt(input.readUTF());

                listChat.add(new Group_ListMessage(message,sendTime,typeMessage,path,fullName,token,nameRoom,userID));
            }
            this.chatCTL.setListMessageGroup(listChat);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleResponseChatOneToMany()
    {
        try {
            String fullName = input.readUTF();
            String message = input.readUTF();
            String sendTime = input.readUTF();
            int token = Integer.parseInt(input.readUTF());
            String nameGroup = input.readUTF();

            this.chatCTL.addMessageGroup(new Group_ListMessage(message,sendTime,"text","",fullName,token,nameGroup,0));

            if(this.chatCTL.room.getToken() == token){
                Platform.runLater(()-> {this.chatCTL.addChatOneToMany("Left",new Group_ListMessage(message,sendTime,"text","",fullName,token,nameGroup,0));});
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleResponseChatOneToOne()
    {
        try {
            String message = input.readUTF();
            String time = input.readUTF();
            String type = input.readUTF();
            String path = input.readUTF();
            int receiverID = Integer.parseInt(input.readUTF());
            int userID = Integer.parseInt(input.readUTF());

            this.chatCTL.addMessageFriend(new Friend_ListMessage(message,time,type,path,receiverID,userID));
            if(this.chatCTL.receiver.getID() == userID){
                Platform.runLater(()-> {this.chatCTL.addChatOneToOne("Left",new Friend_ListMessage(message,time,type,path,receiverID,userID));});
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleResponseListGroup(int messageCount)
    {
        try {
            for(int i= 0;i< messageCount;i++){
                Room room = new Room();

                int idRoom = Integer.parseInt(input.readUTF());
                int token = Integer.parseInt(input.readUTF());
                String name = input.readUTF();
                String memberGroupCount = input.readUTF();

                room.setId(idRoom);
                room.setToken(token);
                room.setName(name);
                room.setMemberGroupCount(memberGroupCount);
                listRoom.add(room);
            }

            Platform.runLater(() ->this.chatCTL.menu(listFriend,listRoom));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleResponseListFriend(int messageCount)
    {
        try {
            for(int i= 0;i< messageCount;i++){
                User user = new User();

                int userID = Integer.parseInt(input.readUTF());
                String fullName = input.readUTF();

                user.setID(userID);
                user.setFullName(fullName);

                if(listFriend.size() >= 1){
                    for(int j = 0;j<listFriend.size();j++){
                        if(userID != listFriend.get(j).getID()){
                            listFriend.add(user);
                        }
                    }
                }else{
                    listFriend.add(user);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleResponseLoginAndSignUp(int messageCount) {
        try {
            int userID = 0;
            String fullName = "";

            for (int i = 1; i < messageCount; i++) {
                switch (i) {
                    case 1 -> userID = Integer.parseInt(input.readUTF());
                    case 2 -> fullName = input.readUTF();
                }
            }

            // Lưu userID sau khi đăng nhập thành công
            this.currentUserID = userID;
            this.setCurrentUserID(userID); // Thiết lập userID trong Client

            // Yêu cầu danh sách bạn bè và nhóm
            requestListFriendOrGroup(String.valueOf(userID), "ListFriend");
            requestListFriendOrGroup(String.valueOf(userID), "ListGroup");

            int finalUserID = userID;
            String finalFullName = fullName;

            Platform.runLater(() -> {
                if (this.chatCTL == null) {
                    System.err.println("chatCTL is not initialized!");
                    return;
                }

                this.chatCTL.setUser(new User(finalUserID, finalFullName, null, null));
                this.chatCTL.showFrameHello();

                // Gọi mở giao diện tạo nhóm (nếu cần)
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void requestListFriendOrGroup(String userID,String type)
    {
        List<String> message = new ArrayList<>();
        message.add(type);
        message.add(userID);
        this.sendRequest(message);
    }

    public void sendRequest(List<String> messages)
    {
        try {
            output.writeInt(messages.size());
            for (String message : messages) {
                output.writeUTF(message);
            }
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //Request Message

    public void clearListFriend()
    {
        this.listFriend.clear();
    }

    public void clearListRoom()
    {
        this.listRoom.clear();
    }
    public void clearMessage(){
        this.chatCTL.getListMessageGroup();
        this.chatCTL.getListMessageFriend();
    }
    public void clearMessageFriend(){}

}
