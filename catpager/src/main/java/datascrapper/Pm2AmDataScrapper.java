package datascrapper;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Pm2AmDataScrapper {
    private static final String PATH = "/Users/gaurav/Downloads/pm2am";
    private static final String OUT_PATH = "/Users/gaurav/Downloads/pm2amcsv";

    public static void main(String[] args) throws IOException {
        File dir = new File(PATH);
        File outFile = creatOutFile();
        BufferedWriter writer = null;
        writer = new BufferedWriter(new FileWriter(outFile));

        String[] list = dir.list();
        for (String name : list) {
            String rawXml = readXML(name);
            parseWithString(rawXml, writer);
        }
        writer.flush();
        writer.close();


    }

    private static File creatOutFile() {
        File outFile = new File(OUT_PATH, "pmam.csv");
        outFile.delete();
        try {
            outFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outFile;
    }

    private static void parseWithString(String rawXml, BufferedWriter writer) throws IOException {
        try {
            int index = rawXml.indexOf("Guest Details");
            String userDetails = rawXml.substring(index);
            int tempIndex = userDetails.indexOf("First Name");
            userDetails = userDetails.substring(tempIndex);
            tempIndex = userDetails.indexOf("<br>");
            userDetails = userDetails.substring(tempIndex + 4);

            tempIndex = userDetails.indexOf("</h4>");
            String firstName = userDetails.substring(0, tempIndex).trim();
            System.out.println("First Name" + firstName);

            tempIndex = userDetails.indexOf("Last Name");
            userDetails = userDetails.substring(tempIndex);
            tempIndex = userDetails.indexOf("</u>");
            userDetails = userDetails.substring(tempIndex + 4);
            tempIndex = userDetails.indexOf("</h4>");
            String lastName = userDetails.substring(0, tempIndex).trim();
            System.out.println("Last Name" + lastName);

            tempIndex = userDetails.indexOf("Phone No");
            userDetails = userDetails.substring(tempIndex);
            tempIndex = userDetails.indexOf("<br>");
            userDetails = userDetails.substring(tempIndex + 4);
            tempIndex = userDetails.indexOf("</h4>");
            String phone = userDetails.substring(0, tempIndex).trim();
            System.out.println("Phone No" + phone);


            tempIndex = userDetails.indexOf("Email No");
            userDetails = userDetails.substring(tempIndex);
            tempIndex = userDetails.indexOf("</u>");
            userDetails = userDetails.substring(tempIndex + 4);
            tempIndex = userDetails.indexOf("</h4>");
            String email = userDetails.substring(0, tempIndex).trim();
            System.out.println("Email No" + email);


            if (email == null || email.isEmpty()) {
                System.out.println("Skipping This Contact");
            } else {
                writer.write(firstName + " " + lastName + "," + phone + "," + email);
                writer.newLine();
            }
            System.out.println("--------------");
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.out.println("------Skipping This--------");
        }
    }

    private static void parseXML(String rawXml) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(rawXml);
            document.getDocumentElement().normalize();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String readXML(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(PATH, fileName)));
            String xml = "", line = "";
            while ((line = reader.readLine()) != null) {
                xml += line;
            }
            return xml;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
