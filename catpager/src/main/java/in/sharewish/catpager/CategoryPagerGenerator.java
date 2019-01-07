package in.sharewish.catpager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CategoryPagerGenerator {
    private final static String ROOT_DIR = "/Users/gaurav/Documents/Projects/sharewish-in";
    private final static String TEMPLATE = "_drafts/cat_template.txt";
    private final static String[] CAT_NAMES = {"good_morning", "good_night"};
    private final static String[] CAT_READABLE_NAMES = {"Good Morning", "Good Night"};

    public static void main(String[] args) {
        File rootDir = new File(ROOT_DIR);
        File templateFile = new File(rootDir, TEMPLATE);
        if (rootDir.exists()) {
            if (templateFile.exists()) {
                traverseCategories(rootDir);
            } else {
                System.out.println("Template Not Exists");
            }
        } else {
            System.out.println("Root Directory Not Exists");
        }
    }

    private static void traverseCategories(File rootDir) {
        int i = 0;
        for (String cat : CAT_NAMES) {
            File catDir = new File(rootDir, cat);
            if (catDir.exists()) {
                File postDir = new File(catDir, "_posts");
                if (postDir.exists()) {
                    traversePosts(catDir, postDir, cat, CAT_READABLE_NAMES[i]);
                } else {
                    System.out.println("Posts Directory Not Exists " + cat);
                }
            } else {
                System.out.println("Category Directory Not Exists " + cat);
            }
            i++;
        }
    }

    private static void traversePosts(File catDir,
                                      File postDir,
                                      String catName,
                                      String catReadableName) {

        int totalPosts = postDir.list().length;
        if (totalPosts > 0) {
            for (int i = 0; i < totalPosts; i += 5) {
                createPageFile(i, i / 5, totalPosts, catDir, catName, catReadableName);
            }
        } else {
            System.out.println("No Post Exists in this category " + catReadableName);
        }
    }

    private static void createPageFile(int counter, int pageNum, int totalPosts, File catDir, String catName, String catReadableName) {
        File template = new File(ROOT_DIR, TEMPLATE);
        try {
            FileReader templaterReader = new FileReader(template);
            BufferedReader bufferedReader = new BufferedReader(templaterReader);
            String line = "";

            String fileName = counter == 0 ? "index.md" : "page" + (pageNum + 1) + ".md";
            File pageFile = new File(catDir, fileName);
            if (pageFile.exists()) {
                pageFile.delete();
            }
            pageFile.createNewFile();
            FileWriter fileWriter = new FileWriter(pageFile);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            boolean startFound = false;
            while ((line = bufferedReader.readLine()) != null) {
                if ("---".equals(line)) {
                    if (startFound) {
                        // append cat related lines
                        writer.write("category:    " + catName);
                        writer.newLine();
                        writer.write("categorytitle:    " + catReadableName);
                        writer.newLine();
                        int startPostIndex = counter + 1;
                        writer.write("start:    " + startPostIndex); // jekyll is one based
                        writer.newLine();
                        // both index are inclusive and we show 6 items per page
                        int endPostIndex = startPostIndex + 5;
                        writer.write("end:    " + endPostIndex);
                        writer.newLine();
                        // next and previous buttons
                        if (endPostIndex - 5 > 1) {
                            writer.write("previous:    " + true);
                            writer.newLine();
                            String previousPageAddress = pageNum == 1 ? "" : "page" + pageNum;
                            writer.write("previousPageAddr:    " + "/" + catName + "/" + previousPageAddress);
                            writer.newLine();
                        }
                        if (endPostIndex <= totalPosts) {
                            writer.write("next:    " + true);
                            writer.newLine();
                            String nextPageAddress = "page" + (pageNum + 2);
                            writer.write("nextPageAddr:    " + "/" + catName + "/" + nextPageAddress);
                            writer.newLine();
                        }
                    }
                    startFound = true;
                }
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (
                FileNotFoundException e)

        {
            e.printStackTrace();
        } catch (
                IOException e)

        {
            e.printStackTrace();
        }
    }

}
