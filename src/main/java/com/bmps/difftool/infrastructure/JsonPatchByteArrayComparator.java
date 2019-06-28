package com.bmps.difftool.infrastructure;

import com.bmps.difftool.rest.DiffOperationResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.diff.JsonDiff;

import java.io.IOException;
import java.util.Objects;

public class JsonPatchByteArrayComparator implements JsonByteArrayComparator {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public DiffOperationResponse compare(byte[] left, byte[] right) throws IOException {
        Objects.requireNonNull(left, "Left byte array not provided");
        Objects.requireNonNull(right, "Right byte array not provided");

        Base64Converter base64Converter = new Base64Converter();

        String leftJsonAsString = base64Converter.decodeToString(left);
        String rightJsonAsString = base64Converter.decodeToString(right);

        JsonNode jsonNode1 = mapper.readValue(leftJsonAsString, JsonNode.class);
        JsonNode jsonNode2 = mapper.readValue(rightJsonAsString, JsonNode.class);

        JsonNode jsonNode = JsonDiff.asJson(jsonNode1, jsonNode2);
        if (jsonNode.size() == 0) {
            return new DiffOperationResponse().matched(true);
        }
        return new DiffOperationResponse().data(jsonNode).matched(false);
    }
}
