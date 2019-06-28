package com.bmps.difftool;

import com.bmps.difftool.infrastructure.Base64Converter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Patch;
import com.github.difflib.patch.PatchFailedException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.diff.JsonDiff;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

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

    @Test
    public void jsonAreEqualInContentButDifferentInLocaltion() throws URISyntaxException, IOException, PatchFailedException, DiffException {
        //GIVEN
        Path jsonBase64PathSource = Paths.get(getClass().getClassLoader()
                .getResource("equal-in-content-differ-to-position/json-base64").toURI());

        Path jsonBase64PathTarget = Paths.get(getClass().getClassLoader()
                .getResource("equal-in-content-differ-to-position/json-base64-target").toURI());

        byte[] encodedSource = Files.readAllBytes(jsonBase64PathSource);
        Assert.assertNotNull(encodedSource);
        byte[] encodedTarget = Files.readAllBytes(jsonBase64PathTarget);
        Assert.assertNotNull(encodedTarget);

        Base64Converter base64Converter = new Base64Converter();

        //WHEN
        List<String> jsonSourceLines = base64Converter.decodeToArrayList(encodedSource);
        List<String> jsonTargetLines = base64Converter.decodeToArrayList(encodedTarget);

        //generating diff information.
        Patch<String> diff = DiffUtils.diff(jsonSourceLines, jsonTargetLines);

        //generating unified diff format
        List<String> unifiedDiff = UnifiedDiffUtils.generateUnifiedDiff("json-base64-left.json", "json-base64-right.json", jsonSourceLines, diff, 0);

        unifiedDiff.forEach(System.out::println);

        //importing unified diff format from file or here from memory to a Patch
        Patch<String> importedPatch = UnifiedDiffUtils.parseUnifiedDiff(unifiedDiff);

        //apply patch to original list
        List<String> patchedText = DiffUtils.patch(jsonSourceLines, importedPatch);

        //THEN
        System.out.println(patchedText);

    }

    @Test
    public void unified() throws DiffException, PatchFailedException {
        List<String> text1 = Arrays.asList("this is a test", "a test");
        List<String> text2 = Arrays.asList("this is a testfile", "a test");

//generating diff information.
        Patch<String> diff = DiffUtils.diff(text1, text2);

//generating unified diff format
        List<String> unifiedDiff = UnifiedDiffUtils.generateUnifiedDiff("original-file.txt", "new-file.txt", text1, diff, 0);

        unifiedDiff.forEach(System.out::println);

//importing unified diff format from file or here from memory to a Patch
        Patch<String> importedPatch = UnifiedDiffUtils.parseUnifiedDiff(unifiedDiff);

//apply patch to original list
        List<String> patchedText = DiffUtils.patch(text1, importedPatch);

        System.out.println(patchedText);
    }
}
