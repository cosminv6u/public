package ro.v6u.cosmin.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Cosmin on 8/1/2017.
 */
public class Main {

    static void printHelp() {
        System.out.println("Usage: "
                + System.lineSeparator() + ">java -jar image-hider.jar reveal fileName separator "
                + System.lineSeparator() + ">java -jar image-hider.jar hide imageName fileName separator "
                + System.lineSeparator() + "'separator' must be complex enough (min 5 chars) and 'file' should be archived");
    }

    static void reveal(String fileName, String separator) throws Exception {
        File file = new File(fileName);
        char[] separatorArray = separator.toCharArray();
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = null;

        int c = 0;
        int i = 0;
        int j = 0;
        StringBuilder sb = new StringBuilder();
        while ((c = fis.read()) != -1) {
            if (i == separatorArray.length) { // found 1
                if (j == separatorArray.length) {
                    fos.write(c);
                } else {
                    sb.append((char) c); // construct filename

                    if (separatorArray[j] == c) {
                        j++;
                        if (j == separatorArray.length) { // found 2
                            String hiddenFileName = sb.toString();
                            hiddenFileName = hiddenFileName.substring(0, hiddenFileName.length() - separatorArray.length);
                            fos = new FileOutputStream(hiddenFileName);
                        }
                    } else {
                        j = 0;
                    }
                }

            } else if (separatorArray[i] == c) {
                i++;
            } else {
                i = 0;
            }
        }
        fis.close();
        fos.close();
    }

    static void hide(String imageFileName, String hiddenFileName, String separator) throws Exception {
        File imageFile = new File(imageFileName);
        File hiddenFile = new File(hiddenFileName);

        FileOutputStream fos = new FileOutputStream(imageFile, true);
        fos.write(separator.getBytes()); // separator
        fos.write(hiddenFileName.getBytes()); // file name
        fos.write(separator.getBytes()); // separator

        FileInputStream fis = new FileInputStream(hiddenFile);
        int c = 0;
        while ((c = fis.read()) != -1) {
            fos.write(c);
        }
        fis.close();
        fos.close();
    }

    static void validate(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("No arguments provided!");
        }
        if (!"reveal".equals(args[0]) && !"hide".equals(args[0])) {
            throw new RuntimeException("Wrong action. Expected 'reveal' or 'hide', was: " + args[0]);
        }
    }

    static void validateReveal(String[] args) {
        if (args.length < 3) {
            throw new RuntimeException("At least 3 arguments were expected! Provided only: " + args.length);
        }
        if (args[2].length() < 5) {
            throw new RuntimeException("'separator' must be at least 5 chars! Was: " + args[2]);
        }
    }

    static void validateHide(String[] args) {
        if (args.length < 4) {
            throw new RuntimeException("At least 4 arguments were expected! Provided only: " + args.length);
        }
        if (args[3].length() < 5) {
            throw new RuntimeException("'separator' must be at least 5 chars! Was: " + args[3]);
        }
    }

    public static void main(String[] args) {
        try {
            validate(args);

            String mode = args[0];

            if ("reveal".equals(mode)) {
                validateReveal(args);
                reveal(args[1], args[2]);
            } else {
                validateHide(args);
                hide(args[1], args[2], args[3]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println();
            printHelp();
        }
    }

}
