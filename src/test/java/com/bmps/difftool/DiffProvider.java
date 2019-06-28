package com.bmps.difftool;

public class DiffProvider {

    public static final String JSON_LEFT_STRING =
            "{\n" +
                    "        \"type\": \"object\",\n" +
                    "        \"description\": \"An runtime exception\",\n" +
                    "        \"properties\": {\n" +
                    "          \"error\": {\n" +
                    "            \"format\": \"string\",\n" +
                    "            \"description\": \"More information may be given here to tell the client what have happened.\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      }";

    public static final String JSON_LEFT_STRING_2 =
            "{\n" +
                    "        \"description\": \"An runtime exception\",\n" +
                    "        \"type\": \"object\",\n" +
                    "        \"properties\": {\n" +
                    "          \"error\": {\n" +
                    "            \"format\": \"string\",\n" +
                    "            \"description\": \"More information may be given here to tell the client what have happened.\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      }";
    public static final String JSON_RIGHT_STRING =
            "{\n" +
                    "        \"type\": \"object\",\n" +
                    "        \"description\": \"A runtime exception\",\n" +
                    "        \"properties\": {\n" +
                    "          \"error\": {\n" +
                    "            \"format\": \"string\",\n" +
                    "            \"description\": \"More information may be given here to tell the client what have happened.\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      }";
}
