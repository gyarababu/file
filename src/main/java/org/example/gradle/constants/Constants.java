package org.example.gradle.constants;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final String INPUT_FILE_PATH1 = "D:\\Programmer\\Projects\\ParseXML\\src\\main\\resources\\input1.xml";
    public static final String INPUT_FILE_PATH2 = "D:\\Programmer\\Projects\\ParseXML\\src\\main\\resources\\input2.xml";
    public static final String OUTPUT_FILE_PATH1 = "D:\\Programmer\\Projects\\ParseXML\\src\\main\\resources\\output1.txt";
    public static final String OUTPUT_FILE_PATH2 = "D:\\Programmer\\Projects\\ParseXML\\src\\main\\resources\\output2.txt";


        private static final Map<String, Integer> FROM_INSTANCE_MAPPING = new HashMap<>();
        private static final Map<String, Integer> TYPE_MAPPING = new HashMap<>();

        static {
            FROM_INSTANCE_MAPPING.put("EMPLOYEES", 1);
            FROM_INSTANCE_MAPPING.put("SQ_EMPLOYEES", 2);
            FROM_INSTANCE_MAPPING.put("EMPLOYEE_ID_FILTER", 3);
            FROM_INSTANCE_MAPPING.put("EXP_EMPLOYEE", 4);
            FROM_INSTANCE_MAPPING.put("trans_ROUTER_EMPLOYEES_BY_JOB_ID", 5);
        }

        static {
            TYPE_MAPPING.put("SOURCE", 1);
            TYPE_MAPPING.put("TRANSFORMATION", 2);
            TYPE_MAPPING.put("TARGET", 3);
        }

        public static int getFromInstanceOrder(String fromInstance) {
            return FROM_INSTANCE_MAPPING.getOrDefault(fromInstance, 6);
        }

        public static int getTypeOrder(String type) {
            return TYPE_MAPPING.getOrDefault(type, 4);
        }
    }

