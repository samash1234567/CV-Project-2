import java.util.Random;

/**
 * Admin Class implementing Runnable.
 *
 * @author sam
 * @version 1.0
 */
public class Admin implements Runnable {

    private static int sleepScale = 3000;

    private String name;
    private ChatServer chatServer;
    private int numOfActions = 15;  //Could be either openning or closing a chatroom

    /**
     * Admin constructor to create a new admin
     *
     * @param name name of the admin
     */
    public Admin(String name) {
        // Set the initial value of class variables
        this.name = name;
    }

    public void assignServer(ChatServer chatServer) {
        this.chatServer = chatServer;

    }

    public void run() {

        try {
            while (numOfActions > 0) {

                Random random = new Random();

                //sleeping between each action
                double rand = Math.random();
                Thread.sleep((long) (rand * sleepScale));

                chatServer.open();
                numOfActions--;

                double rand2 = Math.random();
                Thread.sleep((long) (rand2 * sleepScale));

                chatServer.openChatRoom(1);
                numOfActions--;

                double newRand = Math.random();
                Thread.sleep((long) (newRand * sleepScale));

                chatServer.closeChatRoom(1);
                numOfActions--;

                double newRand1 = Math.random();
                Thread.sleep((long) (newRand1 * sleepScale));

                int id = random.nextInt(100);
                chatServer.openChatRoom(id);
                numOfActions--;

                double rand3 = Math.random();
                Thread.sleep((long) (rand3 * sleepScale));

                chatServer.closeChatRoom(id);
                numOfActions--;

                double rand4 = Math.random();
                Thread.sleep((long) (rand4 * sleepScale));

                chatServer.close();
                numOfActions--;
            }
        } catch (InterruptedException ex) {
            System.out.println("Interrupted Admin Thread (" + name + ")");
        }
    }

}
