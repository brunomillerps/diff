package com.bmps.difftool.domain;

import com.bmps.difftool.exception.ClientException;
import com.bmps.difftool.exception.DiffObjectNotFound;
import com.bmps.difftool.infrastructure.JsonByteArrayComparatorFactory;
import com.bmps.difftool.rest.DiffOperationResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.difflib.algorithm.DiffException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

/**
 * An edge service responsible to compare 2 encoded JSON file in Base64 <br>
 *
 * <ul>
 * <li>Will touch the database to get the DiffObject previously provided</li>
 * <li>Will only perform the comparation in case both side are present</li>
 * </ul>
 *
 * @author Bruno Miller
 */
@Component
public class DiffObjectComparatorService {

    private final ObjectMapper mapper;
    private final MongoTemplate mongoTemplate;

    public DiffObjectComparatorService(ObjectMapper mapper, MongoTemplate mongoTemplate) {
        this.mapper = mapper;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Compares 2 previous provided files in Base64 format <br>
     * Uses {@link JsonNode} to make it human readable
     *
     * @param diffId   A UUID format ID, must not be null
     * @param diffType Determines which algorithm should use to diff files
     * @return {@link DiffOperationResponse}
     * @throws ClientException
     * @throws IOException
     */
    public DiffOperationResponse compare(UUID diffId, ComparatorType diffType) throws ClientException, IOException, DiffException {

        DiffObject diffObject = Optional.ofNullable(mongoTemplate.findById(diffId.toString(), DiffObject.class))
                .orElseThrow(DiffObjectNotFound::new);

        if (diffObject.getLeft() == null) {
            throw new ClientException().error("The left side of the comparison has an empty value, please provide one");
        }
        if (diffObject.getRight() == null) {
            throw new ClientException().error("The right side of the comparison has an empty value, please provide one");
        }

        //a full comparation: memory reference, nulls, length and byte/byte
        boolean isEqual = Arrays.equals(diffObject.getLeft(), diffObject.getRight());
        DiffOperationResponse defaultResponse =
                new DiffOperationResponse().data(mapper.readValue(
                        new String(Base64.getMimeDecoder().decode(diffObject.getLeft())), JsonNode.class));

        if (isEqual) {
            return defaultResponse;
        }

        DiffOperationResponse comparedResponseData = JsonByteArrayComparatorFactory
                .createComparatorFactory(diffType)
                .compare(diffObject.getLeft(), diffObject.getRight());

        return comparedResponseData.isMatched() ? defaultResponse : comparedResponseData;
    }
}
