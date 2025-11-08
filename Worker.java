

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class Worker implements Runnable {
    private final int id;
    private final char startC;
    private final char endC;
    private final Path copyZip, output;

    /**
     * Constructor for Worker class
     * @param id the id of the worker thread
     * @param startC the starting character for the password search
     * @param endC the ending character for the password search
     */
    public Worker(int id, char startC, char endC) {

        this.id = id;
        this.startC = startC;
        this.endC = endC;
        this.copyZip   = Path.of("protected5-" + id + ".zip");
        this.output = Path.of("contents-"    + id);


        try {
            Files.copy(Path.of("protected5.zip"), copyZip);
        } catch(Exception e) {
            throw new RuntimeException("Copy failed", e);
        }

    }

    /**
     * The run method for each thread that calls findPass to look for the password
     */
    @Override
    public void run() {

        try {
            for (char c = startC; c <= endC && !MultiPass.isCorrect(); c++) {
                findPass("" + c);
            }
        } 

        finally {
            delete();
            System.out.println("Thread " + id + "took " + (System.currentTimeMillis() - MultiPass.timeStart) + "ms long");
        }
    }

    /**
     * method to look for the password recursively 
     * @param part the starting point of the password being tested
     */
    public void findPass(String part) {

        if (MultiPass.isCorrect() || part.length() >= 5) {
            return;
        }
        for (char c = 'a'; c <= 'z'; c++) {

            if (MultiPass.isCorrect()) { 
                return;
            }
            String password = part + c;
            try {

                ZipFile zip = new ZipFile(copyZip.toFile());
                zip.setPassword(password);
                zip.extractAll(output.toString());
                MultiPass.setCorrect(password);
                System.out.println("id " + id + " found password: " + password);
                
                return;
            } 
            catch (ZipException ze) { } 

            catch (Exception e) {
                e.printStackTrace();
            }

            findPass(password);
        }
    }


    /**
     * method to delete the files created by each worker thread
     */
    private void delete() {
        File dir = output.toFile();
        if (dir.exists()) {


            File[] files = dir.listFiles();
            if (files != null) {

                for (File file : files) {
                    try {

                        Files.delete(Path.of(file.getPath())); 
                    } 
                    catch(IOException e) {
                        e.printStackTrace();
                    }
                    
                }
            }

            
            try {
                Files.delete(Path.of(dir.getPath()));
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
            
            
        }
        try { 
            Files.delete(copyZip); 
        } 

        catch(IOException e) {
            e.printStackTrace();
        }
        
    }   
    
}
