import java.util.ArrayList;

public class Application {

    public static void main(String[] args) throws InterruptedException {

        // This provides an example of the classes you are required to
        // code for this coursework running.

        // If implemented correctly this code will run without exception
        // nor error (test multiple times). However this does NOT mean
        // your implementation is correct and there may still be concurrency
        // issues.

        Admin admin = new Admin("Liam");
        Thread at = new Thread(admin);
        ChatServer cs = new ChatServer(15, 3, admin);

        //open chatserver and chatrooms available
        cs.open();
        cs.openChatRoom(100);
        cs.openChatRoom(50);
        cs.openChatRoom(45);


        ArrayList<Thread> userThreads = new ArrayList<Thread>();

        at.start();

        // Create 20 users with random UserIDs (1-100) and start their threads
        for (int i = 0; i < 20; i++) {
            int userID = (int) (Math.random() * (100 - 1 + 1) + 1);
            User u1 = new User(userID, cs);
            Thread u1t = new Thread(u1);
            u1t.start();
        }

        // Make sure this waits for all user threads to end
        for (Thread t : userThreads) {
            t.join();
        }

        at.join();
    }
}
