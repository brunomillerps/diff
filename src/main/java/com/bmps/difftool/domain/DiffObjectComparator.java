package com.bmps.difftool.domain;

import com.bmps.difftool.exception.ClientException;
import com.bmps.difftool.exception.DiffObjectNotFound;
import com.bmps.difftool.rest.DiffOperationResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.diff.JsonDiff;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Component
public class DiffObjectComparator {

    private final ObjectMapper mapper;
    private final MongoTemplate mongoTemplate;

    public DiffObjectComparator(ObjectMapper mapper, MongoTemplate mongoTemplate) {
        this.mapper = mapper;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Compares 2 previous provided files in Base64 format <br>
     * Uses {@link JsonNode} to make it human readable
     *
     * @param diffId A UUID format ID, must not be null
     * @return {@link DiffOperationResponse}
     * @throws ClientException
     * @throws IOException
     */
    public DiffOperationResponse compare(UUID diffId) throws ClientException, IOException, JsonPatchException {

        DiffObject diffObject = Optional.ofNullable(mongoTemplate.findById(diffId.toString(), DiffObject.class))
                .orElseThrow(DiffObjectNotFound::new);

        if (diffObject.getLeft() == null) {
            throw new ClientException().error("The left side of the comparison has an empty value, please provide one");
        }
        if (diffObject.getRight() == null) {
            throw new ClientException().error("The right side of the comparison has an empty value, please provide one");
        }

        boolean isEqual = Arrays.equals(diffObject.getLeft(), diffObject.getRight());
        String jsonLeft = new String(Base64.getMimeDecoder().decode(diffObject.getLeft()));
        DiffOperationResponse response = new DiffOperationResponse().data(mapper.readValue(jsonLeft, JsonNode.class));

        if (!isEqual) {
            JsonNode jsonDiff = compare(diffObject);
            if (jsonDiff.size()==0) {
                return response;
            }
            return new DiffOperationResponse().data(jsonDiff).matched(false);
        }

        //just return that
        return response;
    }

    private JsonNode compare(DiffObject diffObject) throws IOException {
        String left = new String(Base64.getMimeDecoder().decode(diffObject.getLeft()));
        String right = new String(Base64.getMimeDecoder().decode(diffObject.getRight()));

        JsonNode jsonNode1 = mapper.readValue(left, JsonNode.class);
        JsonNode jsonNode2 = mapper.readValue(right, JsonNode.class);

        //inverted source and target so that the left is the Original and we can see what have changed with incoming values.
        return JsonDiff.asJson(jsonNode2, jsonNode1);
    }
}
