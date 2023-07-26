package org.example.gradle;

import org.example.gradle.constants.Constants;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        parseAndProcessXMLFiles(Constants.INPUT_FILE_PATH1, Constants.OUTPUT_FILE_PATH1);
        parseAndProcessXMLFiles(Constants.INPUT_FILE_PATH2, Constants.OUTPUT_FILE_PATH2);
    }

    public static void parseAndProcessXMLFiles(String inputFilePath, String outputFilePath) {
        FileWriter writer = null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Parse the input file and store it in a new Document object.
            Document document = builder.parse(new File(inputFilePath));
            // Create a new FileWriter object for the output file.
            writer = new FileWriter(outputFilePath);
            // Declare an array of tag names.
            String[] tagNames = {"CONNECTOR", "INSTANCE", "GROUP"};
            for (String tagName : tagNames) {
                // Call a method called writeAttributes() with the Document, FileWriter, and the current tag name as arguments.
                writeAttributes(document, writer, tagName);
            }
        }  catch (Exception e) {
            System.out.println("Exception: " + e);
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                // Close the FileWriter object.
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Exception while closing the FileWriter: " + e);
                }
            }
        }
    }

    public static void writeAttributes(Document document, FileWriter writer, String tagName) throws Exception {
        NodeList nodeList = document.getElementsByTagName(tagName);
        List<String> combinedList = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                StringBuilder attributes = new StringBuilder();
                NamedNodeMap attributeMap = element.getAttributes();
                for (int j = 0; j < attributeMap.getLength(); j++) {
                    Node attribute = attributeMap.item(j);
                    attributes.append(attribute.getNodeName()).append("=").append(attribute.getNodeValue()).append(", ");
                }
                switch (tagName) {
                    case "CONNECTOR":
                        String fromInstance = element.getAttribute("FROMINSTANCE");
                        String fromField = element.getAttribute("FROMFIELD");
                        String toInstance = element.getAttribute("TOINSTANCE");
                        String toField = element.getAttribute("TOFIELD");
                        int fromInstanceOrder = Constants.getFromInstanceOrder(fromInstance);
                        combinedList.add(fromInstanceOrder + ":connector:fromInstance=" + fromInstance +
                                ", fromField=" + fromField + ", toInstance=" + toInstance + ", toField=" + toField);
                        break;
                    case "INSTANCE": {
                        String name = element.getAttribute("NAME");
                        String type = element.getAttribute("TYPE");
                        int typeOrder = Constants.getTypeOrder(type);
                        combinedList.add(typeOrder + ":instance:Name=" + name + ", type=" + type);
                        break;
                    }
                    case "GROUP": {
                        String name = element.getAttribute("NAME");
                        String expression = element.getAttribute("EXPRESSION");
                        String order = element.getAttribute("ORDER");
                        String type = element.getAttribute("TYPE");
                        combinedList.add(order + ":group:name=" + name + ", expression=" + expression + ", order=" + order + ", type=" + type);
                        break;
                    }
                    default:
                        writer.write(tagName.toLowerCase() + ":" + attributes.substring(0, attributes.length() - 2) + "\n");
                        break;
                }
                }
            }
        combinedList.stream()
                .sorted(Comparator.comparing(s -> Integer.parseInt(s.split(":")[0])))
                .forEach(group -> {
                    try {
                        writer.write(group.substring(group.indexOf(":") + 1) + "\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    }


