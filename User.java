
/**
 * User Class implementing Runnable
 *
 * @author sam
 * @version 1.0
 */
public class User implements Runnable {
    private static int sleepScale = 3000;
    private int userID;
    private ChatServer chatServer;

    private boolean joinedServer;
    private boolean joinedMainRoom;

    private double wantToChat;

    /**
     * User Constructor to make a new user.
     *
     * @param userID     ID of the user
     * @param chatServer Chat server specified for the user.
     */
    public User(int userID, ChatServer chatServer) {
        // Set the initial value of class variables.
        this.userID = userID;
        this.chatServer = chatServer;
        joinedServer = false;
        joinedMainRoom = false;

        // Set wantToChat to random value in range
        // of 10 to 15.
        // Int Range (MAX, MIN) -> (int)Math.random() * (MAX-MIN+1) + MIN
        wantToChat = (int) (Math.random() * (15 - 10 + 1) + 10);
    }

    public double getWantToChat() {
        return wantToChat;
    }

    public int getID() {
        return userID;
    }

    public void run() {
        try {
            // While the user is still interested in chatting ...
            while (wantToChat > 0) {
                if (!joinedServer) { //user must always join the server before room

                    if (chatServer.join(this)) { //user joined server
                        joinedServer = true;
                        wantToChat = wantToChat - 0.5;
                    } else { //attempted to join
                        wantToChat = wantToChat - 0.5;
                    }

                    double randomSleep = Math.random();
                    Thread.sleep((long) (randomSleep * sleepScale));

                } else if (!joinedMainRoom && joinedServer) { //joined the server now join the main room!

                    int mainID = chatServer.getMainRoom();

                    wantToChat = wantToChat - 1;

                    if (chatServer.enterRoom(this, mainID)) { //they have entered main room
                        //pick a random time 2-5 seconds to stay in room
                        joinedMainRoom = true;

                        double rand = Math.random();
                        Thread.sleep((long) (rand * sleepScale));

                        // divide 1000 to turn it back to seconds
                        wantToChat = wantToChat - (rand * sleepScale / 1000);

                        chatServer.leaveRoom(this, mainID);
                    }

                    double randomSleep = Math.random();
                    Thread.sleep((long) (randomSleep * sleepScale));

                } else { //join a random room

                    int randomID = chatServer.getRoom();

                    wantToChat = wantToChat - 2;
                    // Try and join a random Chat Room
                    if (chatServer.enterRoom(this, randomID)) { //they have entered a random room
                        //pick a random time 2-5 seconds to stay in room
                        double rand = Math.random();
                        Thread.sleep((long) (rand * sleepScale));
                        // divide 1000 to turn it back to seconds
                        wantToChat = wantToChat - (rand * sleepScale / 1000);
                        chatServer.leaveRoom(this, randomID);
                    }

                    double rand = Math.random();
                    Thread.sleep((long) (rand * sleepScale));

                }
            }
            //once no longer want to chat leave server
            chatServer.leave(this);

        } catch (InterruptedException ex) {
            System.out.println("Interrupted User Thread (" + userID + ")");
        }
        System.out.println("User Thread (" + userID + ") has ended!");
    }
}
