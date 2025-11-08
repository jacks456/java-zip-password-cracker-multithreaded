import net.lingala.zip4j.core.*;
import net.lingala.zip4j.exception.*;

public class Single {

    public static void main(String[] args) {
        findPass("");
        System.out.println(System.currentTimeMillis());
    }

    private static boolean correct = false;

    /**
     * method to check if the password is found adapted from Example.java
     * @return true if the password is found otehrwise false 
     * @param part the starting point of the password being tested
     */
    public static void findPass(String part) {
        if (correct || part.length() >= 3) {
            return;
        }
        for (char character = 'a'; character <= 'z'; character++) {
            String password = part + character;
            try {
                ZipFile zipFile = new ZipFile("protected3.zip");
                zipFile.setPassword(password);
                zipFile.extractAll("contents");
                System.out.println("Password Correct! Password: " + password);
                correct = true;
                return;
            } catch (ZipException ze) { } 
            catch (Exception e) {
                e.printStackTrace();
            }
            findPass(password);
        }
    }
}
