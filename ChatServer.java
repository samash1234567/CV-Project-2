import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Random;

/**
 * Chatserver class with rooms and users inside of it.
 *
 * @author sam
 * @version 1.0
 */
public class ChatServer {
    private ArrayList<ChatRoom> rooms;
    private ArrayList<User> users;
    private Admin admin;


    private int numOfRooms;
    private int capacity;
    private boolean isOpen;

    /**
     * Constructor to create a new Chat Server.
     *
     * @param capacity   user capacity
     * @param numOfRooms number of rooms capacity
     * @param admin      assigned admin to the server.
     */
    public ChatServer(int capacity, int numOfRooms, Admin admin) {
        // Set the initial value of class variables.
        this.capacity = capacity;
        this.admin = admin;
        this.numOfRooms = numOfRooms;
        isOpen = false;
        this.users = new ArrayList<>();
        this.rooms = new ArrayList<>();

        admin.assignServer(this);
    }

    /**
     * Open the Chat Server.
     *
     * @throws InterruptedException
     */
    public synchronized void open() throws InterruptedException {
        //while the chat server is open wait!
        while (isOpen) {
            wait();
        }
        isOpen = true;
        System.out.println("Chat Server is Opened.");
        notifyAll();
    }

    /**
     * Close the chat server, cant be closed until no users and no rooms.
     *
     * @throws InterruptedException
     */
    public synchronized void close() throws InterruptedException {
        //if users still in chatroom and there are chatrooms open wait!
        while (!users.isEmpty() && !rooms.isEmpty()) {
            wait();
            for (User user : users) { //force user/'s to leave!
                leave(user);
            }
        }
        isOpen = false;
        System.out.println("Chat Server is being Closed.");
        System.out.println("Chat Server is Closed.");
        notifyAll();

    }

    /**
     * Check if able to join and join the chat server.
     *
     * @param user user wanting to join
     * @return if successful in joining or not.
     */
    public boolean join(User user) {

        users.add(user);

        //if users are not unique
//        for (int i = 0; i < getNumberOfUsers(); i++) {
//            if (user.getID() == users.get(i).getID()) {
//                System.out.println("User " + user.getID() +
//                        " failed to join Chat Server (" + user.getWantToChat() + ").");
//                users.remove(user);
//                return false;
//            }
//        }

        //more than capacity
        if (getNumberOfUsers() > getCapacity()) {
            System.out.println("User " + user.getID() +
                    " failed to join Chat Server (" + user.getWantToChat() + ").");
            users.remove(user);
            return false;
        }

        //check if its not open
        if (!isOpen) {
            System.out.println("User " + user.getID() +
                    " failed to join Chat Server (" + user.getWantToChat() + ").");
            users.remove(user);
            return false;
        }


        System.out.println("User " + user.getID() + " admitted to Chat Server (" + user.getWantToChat() + ").");
        return true;

    }

    /**
     * User wants to leave the chat server.
     *
     * @param user user wanting to leave.
     */
    public void leave(User user) {
        // Code for a User to leave the Chat Server.

        if (isOpen) { //server open
            for (int i = 0; i < getNumberOfUsers(); i++) {
                if (user == users.get(i)) { //find user to leave
                    System.out.println("User " + user.getID() + " left the Chat Server.");
                    users.remove(user);
                }
            }
            System.out.println("Could not remove User " + user.getID() + " as is not in the Chat Server.");

        } else {
            System.out.println("Chat Server not Open");
        }
    }


    /**
     * Open a chat room
     *
     * @param chatRoomID ID of chatroom wanting to be opened.
     */
    public void openChatRoom(int chatRoomID) {
        // Code to open Chat Room.
        if (isOpen) { //server open
            ChatRoom currRoom = new ChatRoom(chatRoomID, 10);
            rooms.add(currRoom);

            for (int i = 0; i < getNumberOfRooms(); i++) {
                if (rooms.get(i).getChatRoomID() == chatRoomID) { //find room to open
                    try {
                        rooms.get(i).open();
                    } catch (InterruptedException ex) {
                        System.out.println("Interrupted Opening of Room");
                    }
                }
            }
        } else {
            System.out.println("Chat server not open.");
        }
    }


    /**
     * Close the chatroom
     *
     * @param chatRoomID ID of the chatroom wanting to close.
     */
    public void closeChatRoom(int chatRoomID) {

        if (isOpen) { //server open
            for (int i = 0; i < getNumberOfRooms(); i++) {
                if (rooms.get(i).getIsOpen()) { //check if room open
                    if (rooms.get(i).getChatRoomID() == chatRoomID) { //find room to close
                        try {
                            rooms.get(i).close();
                        } catch (InterruptedException ex) {
                            System.out.println("Interrupted Closed!");
                        }
                        rooms.remove(i);
                    }
                }
            }
        } else {
            System.out.println("Chat server is not open.");
        }
    }

    /**
     * User to enter the room.
     *
     * @param user       user wanting to enter
     * @param chatRoomID ID of the chatroom wanting to enter.
     * @return if successful in entering the room or not.
     */
    public boolean enterRoom(User user, int chatRoomID) {
        // Code to allow user to enter Chat Room.
        if (isOpen) { //server open
            for (int i = 0; i < getNumberOfRooms(); i++) {
                if (rooms.get(i).getIsOpen()) { //check if room open
                    if (rooms.get(i).getChatRoomID() == chatRoomID) { //find room to enter
                        return rooms.get(i).enterRoom(user);
                    }
                }
            }
            System.out.println("Failed to Enter Chat Room through Chat Server");
            return false;
        } else {
            System.out.println("Chat server is not open.");
            return false;
        }
    }

    /**
     * User to leave the chatroom.
     *
     * @param user       user wanting to leave.
     * @param chatRoomID ID of the room wanting to leave.
     */
    public void leaveRoom(User user, int chatRoomID) {
        // Code to allow user to leave Chat Room.
        if (isOpen) { //server open
            for (int i = 0; i < getNumberOfRooms(); i++) {
                if (rooms.get(i).getIsOpen()) { //check if room open
                    if (rooms.get(i).getChatRoomID() == chatRoomID) { //find room to leave
                        rooms.get(i).leaveRoom(user);
                    }
                }
            }
        } else {
            System.out.println("Chat server is not Open.");
        }
    }

    public int getMainRoom() {
        return rooms.get(0).getChatRoomID();
    }

    /**
     * Get a random room ID.
     *
     * @return the ID of the room.
     */
    public int getRoom() {

        Random random = new Random();

        int bound = random.nextInt(100);

        int numRooms = getNumberOfRooms();

        if (numRooms > 0) { //make sure there are rooms
            if (bound <= 50) { //random get room
                return rooms.get(1).getChatRoomID();
            } else { // random get room
                return rooms.get(2).getChatRoomID();
            }
        } else {
            System.out.println("There is no rooms available.");
            return -1;
        }
    }

    public int getNumberOfRooms() {
        return rooms.size();
    }

    public int getNumberOfUsers() {
        return users.size();
    }

    public int getCapacity() {
        return capacity;
    }
}
