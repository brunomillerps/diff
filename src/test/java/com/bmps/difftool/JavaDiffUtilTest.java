package com.bmps.difftool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.diff.JsonDiff;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class JavaDiffUtilTest {

    @Test
    public void computeDifferenceUsingJacksonTest() throws IOException {
        //build simple lists of the lines of the two testfiles
        InputStream originalIS = getClass().getClassLoader().getResource("menu-left.json").openStream();
        InputStream revisedIS = getClass().getClassLoader().getResource("menu-right.json").openStream();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode1 = mapper.readValue(originalIS, JsonNode.class);
        JsonNode jsonNode2 = mapper.readValue(revisedIS, JsonNode.class);

        JsonPatch jsonPatch = JsonDiff.asJsonPatch(jsonNode1, jsonNode2);

        System.out.println(jsonPatch.toString());
    }
}
