
import java.util.ArrayList;

/**
 * Chatroom Class that is used to open,close, and allow the user to enter/leave.
 *
 * @author sam
 * @version 1.0
 */
public class ChatRoom {

    private int chatRoomID;
    private int capacity;

    private ArrayList<User> users;
    private boolean isOpen;

    /**
     * Chatroom constructor to create a new room.
     *
     * @param chatRoomID ID for the room
     * @param capacity   user capacity
     */
    public ChatRoom(int chatRoomID, int capacity) {
        // Set the initial value of class variables.
        this.chatRoomID = chatRoomID;
        this.capacity = capacity;
        isOpen = false;
        this.users = new ArrayList<>();
    }

    /**
     * Open the chatroom while its open wait.
     *
     * @throws InterruptedException
     */
    public synchronized void open() throws InterruptedException {
       //while chat room open wait!
        while (isOpen) {
            wait();
        }
        // Code to open the Chat Room.
        isOpen = true;

        System.out.println("Chat Room " + chatRoomID + " open!");
        notifyAll();
    }

    /**
     * Close the Chatroom once no users inside of it.
     *
     * @throws InterruptedException
     */
    public synchronized void close() throws InterruptedException {

        //while there are users still in chatroom wait!
        while (!users.isEmpty()) {
            wait();

            for (User username : users) {
                leaveRoom(username);
            }
        }
        isOpen = false;
        System.out.println("Chat Room " + chatRoomID + " is being closed!");
        System.out.println("Chat Room " + chatRoomID + " closed!");
        notifyAll();
    }

    /**
     * User wants to enter the room
     *
     * @param user user wanting to enter
     * @return if successful or not to enter room.
     */

    public boolean enterRoom(User user) {

        users.add(user);
        if (isOpen && users.size() < getCapacity()) { //check capacity and room open
            System.out.println("User " + user.getID() +
                    " joined Chat Room " + chatRoomID + ". (" + user.getWantToChat() + ")");
            return true;
        } else { //failed to enter room
            users.remove(user);
            System.out.println("User " + user.getID()
                    + " not joined Chat Room " + chatRoomID + ". (" + user.getWantToChat() + ")");
            return false;
        }
    }

    /**
     * Allow the user to leave the room.
     *
     * @param user user wanting to leave the room.
     */
    public void leaveRoom(User user) {
        // Code for a User to leave a Chat Room.
        if (getIsOpen()) { //check if open
            users.remove(user);
            System.out.println("User " + user.getID() + " left Chat Room " + chatRoomID + ". (" + user.getWantToChat() + ")");
        } else {
            System.out.println("Chat room is closed.");
        }
    }

    public int getChatRoomID() {
        return chatRoomID;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean getIsOpen() {
        return isOpen;
    }
}

