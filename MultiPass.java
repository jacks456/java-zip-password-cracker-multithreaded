import java.util.*;

/*
 * Threads 4
 * Time: 369863 ms
 * 
 * Threads: 3
 * Time: 2791563 ms
 */


public class MultiPass {
    public static int numThreads = 3;
  
    private static volatile boolean correct = false;
    private static volatile String foundPassword = null;
    public static long timeStart;
    private static long timeEnd;
  
    /**
     * method to set the correct password by the worker thread
     * @param pass the password found by the worker thread
     */
    public static synchronized void setCorrect(String pass) {

      if (!correct) {
        correct = true;
        foundPassword = pass;
        timeEnd = System.currentTimeMillis();
      }
    }
    
    /**
     * method to check if the password is found
     * @return true if the password is found otehrwise false 
     */
    public static boolean isCorrect() { 
        return correct; 

    }
  
    public static void main(String[] args) throws InterruptedException {

      timeStart = System.currentTimeMillis();

      List<Thread> threads = new ArrayList<>();
      int totalLetters = 'z' - 'a' + 1;
      int baseLetters = totalLetters / numThreads;
      int extraLetters = totalLetters % numThreads;

      char startLetter = 'a';
  
      for (int i = 0; i < numThreads; i++) {

        int count;
        if (i < extraLetters) {
          count = baseLetters + 1;
        } 
        else {
          count = baseLetters;
        }

        char endLetter = (char)(startLetter + count - 1);

        threads.add(new Thread(new Worker(i, startLetter, endLetter)));
        startLetter = (char)(endLetter + 1);

      }

  
      for (Thread t : threads) {
        t.start();
      }
      for (Thread t : threads) {
        t.join();
      }
  
      if (correct) {
        System.out.println("Password is " + foundPassword);

        System.out.println("Finished in " + (timeEnd - timeStart) + " ms");

      } 
      else {
        System.out.println("Password not found.");
        
      }
    }
  }