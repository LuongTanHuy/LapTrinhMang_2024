package laptrinhmang.ltm_chat.service.server;

import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import laptrinhmang.ltm_chat.ModelChat.Friend_ListMessage;
import laptrinhmang.ltm_chat.ModelChat.Group_ListMessage;
import laptrinhmang.ltm_chat.controller.ServerController;
import laptrinhmang.ltm_chat.model.*;
import laptrinhmang.ltm_chat.ModelChat.SocketUser;
import laptrinhmang.ltm_chat.ModelChat.UserChat;
import com.google.gson.Gson;

import java.io.*;
import java.io.File;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Server {
    private static Set<SocketUser> clientSockets = new HashSet<>();
    private ServerController serverController;
    private ServerSocket serverSocket;
    public Server(ServerController serverController) {
        this.serverController = serverController;
    }

    public String quantityClient()
    {
        return String.valueOf(clientSockets.size());
    }

    public void StartServer(int Port) {
        try {
            this.serverSocket = new ServerSocket(Port);

            while (!serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();

                    LocalDateTime connectTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

                    clientSockets.add(new SocketUser(0, clientSocket, "_", connectTime.format(formatter), "_", "_", "_", "_", String.valueOf(clientSocket.getInetAddress())));

                    Platform.runLater(() ->
                            this.serverController.addNotification(new SocketUser(0, clientSocket, "_", connectTime.format(formatter), "_", "_", "_", "_", String.valueOf(clientSocket.getInetAddress())))
                    );

                    new Thread(new ClientHandler(clientSocket, this.serverController)).start();
                } catch (SocketException e) {
                    if (serverSocket.isClosed()) {
                        break;
                    } else {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopServer() {
        try {
            if (this.serverSocket != null && !this.serverSocket.isClosed()) {
                this.serverSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private DataInputStream  input;
        private DataOutputStream  output;
        private ServerController serverController;
        private Authentication authentication = new Authentication();
        private ConnectDB connectDB = new ConnectDB();
        private String query;
        private String jsonRequest;

        public ClientHandler(Socket socket, ServerController serverController) {
            this.socket = socket;
            this.serverController = serverController;
        }

        @Override
        public void run() {
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
                while (true) {
                    int messageCount = input.readInt();
                    String requestType = input.readUTF();

                    if (messageCount != 0) {
                        switch (requestType) {
                            case "SignUp":
                                handleSignUp(messageCount);
                                break;
                            case "Login":
                                handleLogin(messageCount);
                                break;
                            case "ListFriend":
                                handleListFriend();
                                break;
                            case "ListGroup":
                                handeListGroup();
                                break;
                            case "Search":
                                handleSearch();
                                break;
                            case "CreateGroup":
                                handleCreateGroup();
                                break;
                            case "UpdateGroupName":
                                handleUpdateGroupName();
                                break;
                            case "RemoveGroup":
                                handleRemoveGroup();
                                break;
                            case "AddMemberGroup":
                                handleAddMemberGroup();
                                break;
                            case "RemoveMemberGroup":
                                handleRemoveMemberGroup();
                                break;
                            case "ChatOneToMany":
                                handleChatOneToMany();
                                break;
                            case "ChatOneToOne":
                                handleChatOneToOne();
                                break;
                            case "ListChatOneToOne":
                                handleListChatOneToOne();
                                break;
                            case "ListChatOneToMany":
                                handleListChatOneToMany();
                                break;
                            case "sendFileToServer":
                                handleSendFileToServer();
                                break;
                            case "FileOneToOne":
                                FileOneToOne();
                                break;
                            case "FileOneToMany":
                                FileOneToMany();
                                break;
                            case "DownFile":
                                responseDownFile();
                                break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    LocalDateTime connectTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

                    SocketUser socketUser = new SocketUser();
                    for (SocketUser getSocketUser : clientSockets) {
                        if (getSocketUser.getSocket() == this.socket) {
                            socketUser = getSocketUser;
                        }
                    }

                    clientSockets.remove(socketUser);
                    socketUser.setTimeOut(connectTime.format(formatter));
                    socketUser.setStatus("Đã dừng");
                    this.serverController.updateNotification(socketUser);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private void handleRemoveMemberGroup() {
            try {
                jsonRequest = input.readUTF();

                Gson gson = new Gson();
                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                Map<String, Object> requestMap = gson.fromJson(jsonRequest, type);

                int token = ((Double) requestMap.get("token")).intValue();
                List<Double> memberIdsDouble = (List<Double>) requestMap.get("memberIds");
                List<Integer> memberIds = memberIdsDouble.stream().map(Double::intValue).collect(Collectors.toList());

                boolean allSuccess = connectDB.removeMembersFromGroup(String.valueOf(token), memberIds);
                String nameRoom = connectDB.getNameRoomByToken(token);
                List<Integer> remainingMembers = connectDB.getAllMembersInRoom(token);

                // Gửi phản hồi cho client yêu cầu
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("token", token);
                responseMap.put("success", allSuccess);
                responseMap.put("nameRoom", nameRoom);
                responseMap.put("remainingMembers", remainingMembers);

                String jsonResponse = gson.toJson(responseMap);
                output.writeInt(1);
                output.writeUTF("RemoveMemberGroupResponse");
                output.writeUTF(jsonResponse);
                output.flush();

                // Gửi thông báo tới tất cả các thành viên còn lại
                notifyRemainingMembers(token, nameRoom, remainingMembers);

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error in handleRemoveMemberGroup: " + e.getMessage());
            }
        }

        private void notifyRemainingMembers(int token, String nameRoom, List<Integer> remainingMembers) {
            Gson gson = new Gson();
            try {
                for (SocketUser socketUser : Server.clientSockets) {
                    if (remainingMembers.contains(socketUser.getId())) {
                        DataOutputStream memberOutput = new DataOutputStream(socketUser.getSocket().getOutputStream());

                        Map<String, Object> notifyMap = new HashMap<>();
                        notifyMap.put("token", token);
                        notifyMap.put("nameRoom", nameRoom);
                        notifyMap.put("remainingMembers", remainingMembers);

                        String notifyJson = gson.toJson(notifyMap);

                        memberOutput.writeInt(1);
                        memberOutput.writeUTF("UpdateGroupMembers");
                        memberOutput.writeUTF(notifyJson);
                        memberOutput.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error in notifyRemainingMembers: " + e.getMessage());
            }
        }

        private void handleAddMemberGroup() {
            try {
                // Đọc yêu cầu từ client
                jsonRequest = input.readUTF();

                // Parse JSON request
                Gson gson = new Gson();
                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                Map<String, Object> requestMap = gson.fromJson(jsonRequest, type);

                int token = ((Double) requestMap.get("token")).intValue();
                List<Double> memberIdsDouble = (List<Double>) requestMap.get("memberIds");
                List<Integer> memberIds = memberIdsDouble.stream().map(Double::intValue).collect(Collectors.toList());
                String nameRoom = (String) requestMap.getOrDefault("nameRoom", null);

                System.out.println("Parsed member IDs: " + memberIds + ", Token: " + token + ", NameRoom: " + nameRoom);

                // Thêm các userId vào bảng room
                boolean allSuccess = true;
                for (int userId : memberIds) {
                    boolean success = connectDB.addUserToRoom(userId, 0, nameRoom, token);
                    if (!success) {
                        allSuccess = false;
                        System.err.println("Failed to add userID " + userId + " to group with token " + token);
                    }
                }

                if (nameRoom == null || nameRoom.isEmpty()) {
                    nameRoom = connectDB.getNameRoomByToken(token);
                }

                List<Integer> allMemberIds = connectDB.getAllMembersInRoom(token);

                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("memberIds", allMemberIds);
                responseMap.put("token", token);
                responseMap.put("nameRoom", nameRoom);
                responseMap.put("success", allSuccess);

                String jsonResponse = gson.toJson(responseMap);
                output.writeInt(1);
                output.writeUTF(jsonResponse);
                output.flush();

                System.out.println("Response sent to client: " + jsonResponse);

                // Gửi thông báo tới tất cả các thành viên trong nhóm
                for (SocketUser socketUser : Server.clientSockets) {
                    if (allMemberIds.contains(socketUser.getId())) {
                        DataOutputStream memberOutput = new DataOutputStream(socketUser.getSocket().getOutputStream());

                        Map<String, Object> notifyMap = new HashMap<>();
                        notifyMap.put("token", token);
                        notifyMap.put("nameRoom", nameRoom);
                        notifyMap.put("memberIds", allMemberIds);

                        String notifyJson = gson.toJson(notifyMap);

                        memberOutput.writeInt(1);
                        memberOutput.writeUTF("UpdateAddGroupMembers");
                        memberOutput.writeUTF(notifyJson);
                        memberOutput.flush();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error handling AddGroup request: " + e.getMessage());
            }
        }

        private void handleRemoveGroup() {
            try {
                int groupToken = input.readInt(); // Nhận token từ client

                // Lấy danh sách thành viên trong nhóm trước khi xóa
                List<Integer> allMemberIds = connectDB.getAllMembersInRoom(groupToken);

                // Xóa nhóm khỏi DB
                boolean success = connectDB.updateReceiverID(groupToken);

                // Gửi phản hồi về client yêu cầu
                output.writeInt(1);
                output.writeUTF("RemoveGroupResponse");
                output.writeBoolean(success);
                output.writeInt(groupToken);
                output.flush();

                if (success) {
                    System.out.println("Xóa nhóm thành công cho token: " + groupToken);

                    // Gửi thông báo tới tất cả thành viên trong nhóm
                    Gson gson = new Gson();
                    for (SocketUser socketUser : Server.clientSockets) {
                        if (allMemberIds.contains(socketUser.getId())) {
                            DataOutputStream memberOutput = new DataOutputStream(socketUser.getSocket().getOutputStream());

                            Map<String, Object> notifyMap = new HashMap<>();
                            notifyMap.put("token", groupToken);

                            String notifyJson = gson.toJson(notifyMap);

                            memberOutput.writeInt(1);
                            memberOutput.writeUTF("GroupRemovedNotification");
                            memberOutput.writeUTF(notifyJson);
                            memberOutput.flush();
                        }
                    }
                } else {
                    System.out.println("Xóa nhóm thất bại cho token: " + groupToken);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void handleUpdateGroupName() {
            try {
                int token = input.readInt();
                String newGroupName = input.readUTF();

                System.out.println("Server received UpdateGroupName request:");
                System.out.println("  token: " + token);
                System.out.println("  newGroupName: " + newGroupName);

                // Cập nhật tên nhóm trong DB
                Map<String, Object> result = connectDB.updateGroupNameByToken(token, newGroupName);

                boolean isUpdated = (boolean) result.get("success");
                int returnedToken = (int) result.get("token");
                String returnedGroupName = (String) result.get("newGroupName");
                List<Integer> userIds = (List<Integer>) result.get("userIDs");

                // Gửi phản hồi cho client yêu cầu
                output.writeInt(1);
                output.writeUTF("UpdateGroupNameResponse");
                output.writeBoolean(isUpdated);
                output.writeInt(returnedToken);
                output.writeUTF(returnedGroupName);
                output.writeInt(userIds.size());
                for (int userId : userIds) {
                    output.writeInt(userId);
                }
                output.flush();

                // Nếu cập nhật thành công, gửi thông báo đến tất cả các thành viên
                if (isUpdated) {
                    System.out.println("Group name updated successfully, notifying members...");
                    Gson gson = new Gson();
                    for (SocketUser socketUser : Server.clientSockets) {
                        if (userIds.contains(socketUser.getId())) {
                            DataOutputStream memberOutput = new DataOutputStream(socketUser.getSocket().getOutputStream());

                            Map<String, Object> notifyMap = new HashMap<>();
                            notifyMap.put("token", returnedToken);
                            notifyMap.put("newGroupName", returnedGroupName);

                            String notifyJson = gson.toJson(notifyMap);

                            memberOutput.writeInt(1);
                            memberOutput.writeUTF("GroupNameUpdatedNotification");
                            memberOutput.writeUTF(notifyJson);
                            memberOutput.flush();
                        }
                    }
                } else {
                    System.out.println("Failed to update group name in the database.");
                }
            } catch (IOException e) {
                System.out.println("Error in handleUpdateGroupName: " + e.getMessage());
                e.printStackTrace();
            }
        }

        private void handleCreateGroup() {
            try {
                String jsonRequest = input.readUTF();
                Gson gson = new Gson();
                Map<String, Object> requestMap = gson.fromJson(jsonRequest, Map.class);

                // Kiểm tra dữ liệu từ client
                if (requestMap == null || !requestMap.containsKey("room") || !requestMap.containsKey("memberIds")) {
                    throw new IllegalArgumentException("Dữ liệu requestMap không hợp lệ!");
                }

                // Lấy dữ liệu từ requestMap
                Map<String, Object> room = (Map<String, Object>) requestMap.get("room");
                List<Double> memberIdsRaw = (List<Double>) requestMap.get("memberIds");

                String roomName = (String) room.get("name");
                int userId = ((Double) room.get("userId")).intValue();
                int token = ((Double) room.get("token")).intValue();

                // Đổi danh sách memberIds từ Double sang Integer
                List<Integer> memberIds = new ArrayList<>();
                for (Double id : memberIdsRaw) {
                    memberIds.add(id.intValue());
                }

                // Thêm nhóm vào cơ sở dữ liệu
                connectDB.addGroup(userId, roomName, token, memberIds);

                // Tạo phản hồi cho client yêu cầu
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("status", "success");
                responseMap.put("message", "Nhóm " + roomName + " được tạo thành công!");
                responseMap.put("groupName", roomName);
                responseMap.put("memberCount", memberIds.size());
                responseMap.put("token", token);
                String jsonResponse = gson.toJson(responseMap);
                output.writeInt(1);
                output.writeUTF("CreateGroupResponse");
                output.writeUTF(jsonResponse);
                output.flush();

                // Thông báo đến tất cả các thành viên trong nhóm
                for (SocketUser socketUser : Server.clientSockets) {
                    if (memberIds.contains(socketUser.getId())) {
                        DataOutputStream memberOutput = new DataOutputStream(socketUser.getSocket().getOutputStream());

                        Map<String, Object> notifyMap = new HashMap<>();
                        notifyMap.put("token", token);
                        notifyMap.put("groupName", roomName);
                        notifyMap.put("memberCount", memberIds.size());

                        String notifyJson = gson.toJson(notifyMap);

                        memberOutput.writeInt(1);
                        memberOutput.writeUTF("GroupCreatedNotification");
                        memberOutput.writeUTF(notifyJson);
                        memberOutput.flush();
                    }
                }
            } catch (IOException e) {
                System.err.println("Lỗi khi đọc dữ liệu từ client: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Lỗi xử lý dữ liệu: " + e.getMessage());
            }
        }

        private void handleSearch() throws IOException {
            String keyword = input.readUTF(); // Nhận từ khóa tìm kiếm từ client
            List<String> searchResults = connectDB.searchUsersByNameOrPhoneOrRoom(keyword);

            // Gửi kết quả tìm kiếm về client
            output.writeInt(searchResults.size());
            output.writeUTF("Search");
            for (String result : searchResults) {
                output.writeUTF(result);
            }
            output.flush();
        }

        private void responseDownFile()
        {
            try {
                String path = input.readUTF();
                String fileName = input.readUTF();

                File file = new File(path);
                if (file.exists()) {
                    output.writeInt(1);
                    output.writeUTF("DownFile");
                    output.writeLong(file.length());
                    output.writeUTF(fileName);

                    // Gửi file cho client
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void FileOneToMany()
        {
            try {
                String typeMessage = input.readUTF();
                String fileName = input.readUTF();
                String path = input.readUTF();
                int userID_Send = Integer.parseInt(input.readUTF());
                int token = Integer.parseInt(input.readUTF());//token Room
                String nameGroup = input.readUTF();
                String fullName = input.readUTF();

                int roomID= connectDB.roomID(new Room(0,userID_Send,token,nameGroup,0));
                if(roomID != 0){
                    List<Room> listIDMemberGroup = connectDB.listMemberOfRoom(token);
                    for (SocketUser clientSocket : clientSockets) {
                        if (clientSocket.getSocket() != this.socket) {
                            for (Room room : listIDMemberGroup) {
                                if (clientSocket.getId() == room.getUserId()) {
                                    DataOutputStream output = new DataOutputStream(clientSocket.getSocket().getOutputStream());
                                    List<String> listMessage = new ArrayList<>();
                                    listMessage.add("FileOneToMany");
                                    listMessage.add(fileName);
                                    listMessage.add(path);
                                    listMessage.add("file");
                                    listMessage.add(this.getTime());
                                    listMessage.add(fullName);
                                    listMessage.add(String.valueOf(userID_Send));
                                    listMessage.add(String.valueOf(token));
                                    listMessage.add(nameGroup);

                                    String[] sendMessage = listMessage.toArray(new String[0]);
                                    this.response(sendMessage, output);
                                }
                            }
                            //Save message
                            connectDB.saveMessage(new Message(fileName, roomID, "", typeMessage,path));
                        }
                    }

                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void FileOneToOne()
        {
            try {
                String typeMessage = input.readUTF();
                String fileName = input.readUTF();
                String path = input.readUTF();
                int userID_Send = Integer.parseInt(input.readUTF());
                int userID_Receive = Integer.parseInt(input.readUTF());

                int roomID= connectDB.roomID(new Room(0,userID_Send,0,"",userID_Receive));
                if(roomID != 0){
                    for (SocketUser clientSocket : clientSockets) {
                        // Kiểm tra xem client có phải là client đã gửi tin nhắn hay không
                        if (clientSocket.getId() == userID_Receive) {
                            //Save message
                            if(connectDB.saveMessage(new Message(fileName,roomID,"",typeMessage,path))){
                                DataOutputStream output = new DataOutputStream(clientSocket.getSocket().getOutputStream());
                                List<String> listMessage = new ArrayList<>();
                                listMessage.add("FileOneToOne");
                                listMessage.add(fileName);
                                listMessage.add(this.getTime());
                                listMessage.add("file");
                                listMessage.add(path);
                                listMessage.add(String.valueOf(userID_Send));

                                String[] sendMessage = listMessage.toArray(new String[0]);
                                this.response(sendMessage,output);
                            };

                        }
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void handleSendFileToServer()
        {
            try {

                String fileName = input.readUTF(); // Đọc tên file từ client
                long fileSize = input.readLong();// Đọc kích thước file

                for (SocketUser socketUser : clientSockets) {
                    if (socketUser.getSocket() == this.socket) {
                        // Tạo đường dẫn file đích để lưu trên server
                        File saveFile = new File("D:\\DACS4\\LTM_CHAT\\src\\main\\resources\\laptrinhmang\\ltm_chat\\" + socketUser.getId() + "\\" + fileName);
                        FileOutputStream fileOutputStream = new FileOutputStream(saveFile);

                        // Đọc dữ liệu file và ghi ra file đích
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        long totalBytesRead = 0;

                        while ((bytesRead = input.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, bytesRead);
                            totalBytesRead += bytesRead;

                            // Kiểm tra xem đã nhận đủ dữ liệu chưa
                            if (totalBytesRead >= fileSize) break;
                        }
                    }
                }


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void  handleChatOneToMany()
        {
            try {
                String typeMessage = input.readUTF();
                String message = input.readUTF();
                int userID_Send = Integer.parseInt(input.readUTF());
                int token = Integer.parseInt(input.readUTF());//token Room
                String nameGroup = input.readUTF();
                String fullName = input.readUTF();

                int roomID= connectDB.roomID(new Room(0,userID_Send,token,nameGroup,0));
                if(roomID != 0){
                    List<Room> listIDMemberGroup = connectDB.listMemberOfRoom(token);
                    for (SocketUser clientSocket : clientSockets) {
                        if (clientSocket.getSocket() != this.socket) {
                            for (Room room : listIDMemberGroup) {
                                if (clientSocket.getId() == room.getUserId()) {
                                    DataOutputStream output = new DataOutputStream(clientSocket.getSocket().getOutputStream());
                                    List<String> listMessage = new ArrayList<>();
                                    listMessage.add("ChatOneToMany");
                                    listMessage.add(fullName);
                                    listMessage.add(message);
                                    listMessage.add(this.getTime());
                                    listMessage.add(String.valueOf(token));
                                    listMessage.add(nameGroup);


                                    String[] sendMessage = listMessage.toArray(new String[0]);
                                    this.response(sendMessage, output);
                                }
                            }
                            //Save message
                            connectDB.saveMessage(new Message(message, roomID, "", typeMessage));
                        }
                    }

                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void handleChatOneToOne()
        {
            try {
                String typeMessage = input.readUTF();
                String message = input.readUTF();
                int userID_Send = Integer.parseInt(input.readUTF());
                int userID_Receive = Integer.parseInt(input.readUTF());

                int roomID= connectDB.roomID(new Room(0,userID_Send,0,"",userID_Receive));
                if(roomID != 0){
                    for (SocketUser clientSocket : clientSockets) {
                        // Kiểm tra xem client có phải là client đã gửi tin nhắn hay không
                        if (clientSocket.getId() == userID_Receive) {
                            //Save message
                            if(connectDB.saveMessage(new Message(message,roomID,"",typeMessage))){
                                DataOutputStream output = new DataOutputStream(clientSocket.getSocket().getOutputStream());
                                List<String> listMessage = new ArrayList<>();
                                listMessage.add("ChatOneToOne");
                                listMessage.add(message);
                                listMessage.add(this.getTime());
                                listMessage.add("text");//type
                                listMessage.add("");//path
                                listMessage.add(String.valueOf(userID_Receive));
                                listMessage.add(String.valueOf(userID_Send));

                                String[] sendMessage = listMessage.toArray(new String[0]);
                                this.response(sendMessage,output);
                            };

                        }
                    }

                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void handleListChatOneToOne()
        {
            try {
                int userID = Integer.parseInt(input.readUTF());
                int receiver = Integer.parseInt(input.readUTF());

                List<Friend_ListMessage> listChat = connectDB.listChatFriend(new Room(0,userID,0,"",receiver));

                output.writeInt(listChat.size());
                output.writeUTF("ListChatOneToOne");
                for(int i= 0;i< listChat.size();i++){
                    output.writeUTF(listChat.get(i).getMessage());
                    output.writeUTF(listChat.get(i).getSendTimeNotFormat());
                    output.writeUTF(listChat.get(i).getType());
                    output.writeUTF(listChat.get(i).getPath());
                    output.writeUTF(String.valueOf(listChat.get(i).getReceiverID()));
                    output.writeUTF(String.valueOf(listChat.get(i).getUserID()));
                }
                 output.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void handleListChatOneToMany()
        {
            try {
                int token = Integer.parseInt(input.readUTF());

                List<Group_ListMessage> listChat = connectDB.listChatGroup(new Room(0,0,token,"",0));

                output.writeInt(listChat.size());
                output.writeUTF("ListChatOneToMany");
                for(int i= 0;i< listChat.size();i++){
                    output.writeUTF(String.valueOf(listChat.get(i).getUserID()));
                    output.writeUTF(listChat.get(i).getFullName());
                    output.writeUTF(listChat.get(i).getMessage());
                    output.writeUTF(listChat.get(i).getSendTimeNotFormat());
                    output.writeUTF(listChat.get(i).getType());
                    output.writeUTF(listChat.get(i).getPath());
                    output.writeUTF(listChat.get(i).getNameRoom());
                    output.writeUTF(String.valueOf(listChat.get(i).getToken()));
                }
                output.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private String getTime()
        {
            LocalDateTime now = LocalDateTime.now();

            // Định dạng thời gian theo mong muốn
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Chuyển thời gian thành chuỗi
           return now.format(formatter);
        }

        private void handleListFriend()
        {
            try {
                int userID = Integer.parseInt(input.readUTF());
                List<Integer> listIDFriend = connectDB.listIDFriend(userID);

                if (listIDFriend != null){
                    output.writeInt(listIDFriend.size());
                    output.writeUTF("ListFriend");

                    for(int i=0;i< listIDFriend.size();i++){
                        User user = connectDB.getUserByID(listIDFriend.get(i));
                        output.writeUTF(String.valueOf(user.getID()));
                        output.writeUTF(user.getFullName());
                    }
                    output.flush();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void handeListGroup() {
            try {
                // Đọc userID từ client
                int userID = Integer.parseInt(input.readUTF());

                // Lấy danh sách nhóm (receiverID = 0 cho group chat)
                List<Room> listRoom = connectDB.listRoom(userID); // Modified to align with the `listRoom` method

                if (listRoom != null && !listRoom.isEmpty()) {
                    // Gửi kích thước danh sách nhóm
                    output.writeInt(listRoom.size());
                    output.writeUTF("ListGroup");

                    // Gửi thông tin từng nhóm
                    for (Room room : listRoom) {
                        output.writeUTF(String.valueOf(room.getId()));
                        output.writeUTF(String.valueOf(room.getToken())); // Token nhóm
                        output.writeUTF(room.getName()); // Tên nhóm
                        output.writeUTF(String.valueOf(connectDB.memberGroupCount(room.getToken()))); // Số thành viên
                    }
                } else {
                    // Nếu không có nhóm nào
                    output.writeInt(0);
                    output.writeUTF("ListGroup");
                }

                output.flush();
            } catch (IOException e) {
                System.err.println("Error in handleListGroup: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }

        private void handleLogin(int messageCount) {
            try {
                int phone = 0;
                String password = "";

                // Đọc thông tin đăng nhập từ client
                for (int i = 1; i <= messageCount; i++) {
                    switch (i) {
                        case 1 -> phone = Integer.parseInt(input.readUTF());
                        case 2 -> password = input.readUTF();
                    }
                }

                // Thực hiện đăng nhập
                User user = this.authentication.Login(phone, password);
                if (user != null) {
                    for (SocketUser socketUser : clientSockets) {
                        if (socketUser.getSocket() == this.socket) {
                            // Cập nhật thông tin người dùng vào SocketUser
                            socketUser.setId(user.getID());
                            socketUser.setFullName(user.getFullName());
                            socketUser.setStatus("Đang hoạt động");
                            socketUser.setQuantityFriend(String.valueOf(this.connectDB.listIDFriend(user.getID()).size()));

                            // Lấy số nhóm (receiverID = 0 cho nhóm)
                            socketUser.setQuantityGroup(String.valueOf(this.connectDB.listRoom(user.getID()).size()));

                            // Gửi thông tin lên ServerController
                            this.serverController.updateNotification(socketUser);

                            // Gửi phản hồi thành công về client
                            String[] responseLogin = {"Login", String.valueOf(user.getID()), user.getFullName()};
                            this.response(responseLogin, this.output);
                        }
                    }
                } else {
                    // Gửi phản hồi thất bại về client
                    String[] responseLogin = {"Login", "Failed"};
                    this.response(responseLogin, this.output);
                }
            } catch (IOException e) {
                System.err.println("Error in handleLogin: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }

        private void handleSignUp(int messageCount)
        {
            try {
                int    phone = 0;
                String fullName = "";
                String password ="";

                for (int i = 1; i < messageCount; i++) {
                    switch (i) {
                        case 1:
                            fullName = input.readUTF();
                            break;
                        case 2:
                            phone = Integer.parseInt(input.readUTF());
                            break;
                        case 3:
                            password = input.readUTF();
                            break;
                    }
                }

                User user = this.authentication.SignUp(fullName, Long.valueOf(phone), password);

                createFolder(String.valueOf(user.getID()));

                clientSockets.add(new SocketUser(user.getID(),this.socket));
                String[] responseSignUp = {"SignUp",String.valueOf(user.getID()), user.getFullName()};
                this.response(responseSignUp,this.output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static boolean createFolder(String folderName)
        {
            try {
                // Đường dẫn gốc tới thư mục cần tạo
                String basePath = "D:/DACS4/LTM_CHAT/src/main/resources/laptrinhmang/ltm_chat";

                // Tạo đường dẫn đầy đủ tới thư mục mới
                String fullDirectoryPath = basePath + File.separator + folderName;

                // Tạo đối tượng File đại diện cho thư mục
                File directory = new File(fullDirectoryPath);

                // Kiểm tra và tạo thư mục nếu nó chưa tồn tại
                if (!directory.exists()) {
                    return directory.mkdir(); // Tạo thư mục và trả về true nếu thành công
                } else {
                    System.out.println("Thư mục đã tồn tại: " + fullDirectoryPath);
                    return false; // Thư mục đã tồn tại
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false; // Có lỗi xảy ra
            }
        }

        private void response(String[] messages,DataOutputStream output)
        {
            try {
                output.writeInt(messages.length);
                for (String message : messages) {
                    output.writeUTF(message);
                }
                output.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
