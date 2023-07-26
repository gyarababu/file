package org.example.gradle;

import org.example.gradle.constants.Constants;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws Exception {
        FileWriter writer1 = null;
        FileWriter writer2 = null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Parse the first input file and store it in a new Document object.
            Document document1 = builder.parse(new File(Constants.INPUT_FILE_PATH1));
            // Parse the second input file and store it in another new Document object.
            Document document2 = builder.parse(new File(Constants.INPUT_FILE_PATH2));
            // Create a new FileWriter object for the first output file.
            writer1 = new FileWriter(Constants.OUTPUT_FILE_PATH1);
            // Create another new FileWriter object for the second output file.
            writer2 = new FileWriter(Constants.OUTPUT_FILE_PATH2);
            // Declare an array of tag names.
            String[] tagNames = {"CONNECTOR", "INSTANCE", "GROUP"};
            for (String tagName : tagNames) {
                // Call a method called writeAttributes() with the first input file, the first output file, and the current tag name as arguments.
                writeAttributes(document1, writer1, tagName);
                // Call the same method with the second input file, the second output file, and the current tag name as arguments.
                writeAttributes(document2, writer2, tagName);
            }
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        } finally {
            if (writer1 != null) {
                // Close the first FileWriter object.
                writer1.close();
            }
            if (writer2 != null) {
                // Close the second FileWriter object.
                writer2.close();
            }
        }
    }

    public static void writeAttributes(Document document, FileWriter writer, String tagName) throws Exception {
        NodeList nodeList = document.getElementsByTagName(tagName);
        List<String> instances = new ArrayList<>();
        List<String> groups = new ArrayList<>();

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
                        writer.write(":connector:fromInstance=" + fromInstance +
                                ", fromField=" + fromField + ", toInstance=" + toInstance + ", toField=" + toField + "\n");
                        break;
                    case "INSTANCE": {
                        String name = element.getAttribute("NAME");
                        String type = element.getAttribute("TYPE");
                        int typeOrder;
                        switch (type) {
                            case "SOURCE":
                                typeOrder = 1;
                                break;
                            case "TRANSFORMATION":
                                typeOrder = 2;
                                break;
                            case "TARGET":
                                typeOrder = 3;
                                break;
                            default:
                                typeOrder = 4;
                                break;
                        }
                        instances.add(typeOrder + ":instance:Name=" + name + ", type=" + type);
                        break;
                    }
                    case "GROUP": {
                        String name = element.getAttribute("NAME");
                        String expression = element.getAttribute("EXPRESSION");
                        String order = element.getAttribute("ORDER");
                        String type = element.getAttribute("TYPE");
                        groups.add(order + ":group:name=" + name + ", expression=" + expression + ", order=" + order + ", type=" + type);
                        break;
                    }
                    default:
                        writer.write(tagName.toLowerCase() + ":" + attributes.substring(0, attributes.length() - 2) + "\n");
                        break;
                }
                }
            }
        instances.sort(Comparator.comparing(s -> Integer.parseInt(s.split(":")[0])));
        for (String instance : instances) {
            writer.write(instance.substring(instance.indexOf(":") + 1) + "\n");
        }
        groups.sort(Comparator.comparing(s -> Integer.parseInt(s.split(":")[0])));
        for (String group : groups) {
            writer.write(group.substring(group.indexOf(":") + 1) + "\n");
        }
        }

    }

