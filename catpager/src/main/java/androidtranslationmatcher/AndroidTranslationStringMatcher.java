package androidtranslationmatcher;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AndroidTranslationStringMatcher {
    static String path = "/Users/gaurav/Downloads/translation/";
    static String englishFilePath = "english.xml";
    static String compareFilePath = "dutch.xml";

    public static void main(String[] args) {
        Map<String, String> englishFileMap = new HashMap<>();
        Map<String, String> compareFileMap = new HashMap<>();


        fillXmlMap(path + englishFilePath, englishFileMap);
        fillXmlMap(path + compareFilePath, compareFileMap);
        System.out.println(" --------------------------------------- ");
        System.out.println(" English File Length " + englishFileMap.size());
        System.out.println(" Other File " + compareFilePath + " , Length " + compareFileMap.size());
        System.out.flush();
        for (String key : englishFileMap.keySet()) {
            if (compareFileMap.containsKey(key)) {
                String english = englishFileMap.get(key);
                String compareValue = compareFileMap.get(key);
                checkCDATA(key, english, compareValue);
                checkSpecialSymbols(key, english, compareValue);
                System.err.flush();
            } else {
                System.err.println("Key Does Not Exist In Compare File " + key);
            }
        }
        System.err.flush();

        System.out.println("Finished, Check Red Error Lines");
        System.out.println(" --------------------------------------- ");
    }

    private static void checkSpecialSymbols(String key, String english, String compareValue) {
        checkSymbol(key, english, compareValue, "%s");
        checkSymbol(key, english, compareValue, "%d");
        checkSymbol(key, english, compareValue, "\\n");
    }

    private static void checkSymbol(String key, String english, String compareValue, String s) {
        int engCount = getSymbolCount(english, s);
        int compCount = getSymbolCount(compareValue, s);
        if (engCount != compCount) {
            System.err.println("Special Symbol " + s + " Count Does Not Match " + key + " , count in eng " + engCount + " , other " + compCount);
        }
    }

    private static int getSymbolCount(String line, String s) {
        int total = 0;
//        String[] words = line.split("\\s+");
//        for (String word : words) {
//            // each words can have many instaces of s
//            String[] splitted = word.split(s);
//            for (String splitWord : splitted) {
//                if (splitWord.contains(s)) {
//                    total++;
//                }
//            }
//        }
        int fromIndex = -1;
        while ((fromIndex = line.indexOf(s, fromIndex)) != -1) {
            total++;
            fromIndex++;

        }
        return total;
    }

    private static void checkCDATA(String key, String english, String compareValue) {
        String cdata = "<![CDATA[";
        String suffix = "]]>";
        if (english.contains(cdata)) {
            if (compareValue.contains(cdata)) {
                if (!english.endsWith(suffix) || !compareValue.endsWith(suffix)) {
                    System.err.println("CDATA Ends ERROR in " + key);
                }
            } else {
                System.err.println("CDATA ERROR in " + key);
            }
        } else {
            if (compareValue.contains(cdata)) {
                System.err.println("CDATA ERROR in " + key);
            }
        }
    }

    private static void fillXmlMap(String rawXml, Map<String, String> map) {
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new FileInputStream(new File(rawXml)));
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("string");
            map.clear();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                map.put(nNode.getAttributes().getNamedItem("name").getTextContent(), nNode.getFirstChild().getTextContent());
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String readXML(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
            String xml = "", line = "";
            boolean resourcesStarted = false;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                xml += line;
            }
            reader.close();
            return xml;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
