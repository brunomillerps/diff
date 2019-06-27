package com.bmps.difftool;

import com.bmps.difftool.domain.DiffObject;
import com.bmps.difftool.domain.DiffObjectRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@ContextConfiguration(classes = DiffToolApplication.class)
@DataMongoTest
@ExtendWith(SpringExtension.class)
public class MongoRespositoryTest {

    private DiffObjectRepository diffObjectRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void setup() throws Exception {
        diffObjectRepository = new DiffObjectRepository(mongoTemplate);
    }


    @DisplayName("given a json encoded with base64 "
            + " when save object using MongoDB upsert strategy"
            + " then object is saved")
    @Test
    public void upsertTest() throws URISyntaxException, IOException {
        Path jsonBase64Path = Paths.get(getClass().getClassLoader().getResource("json-base64").toURI());
        byte[] encodedJson = Files.readAllBytes(jsonBase64Path);
        boolean wasAcknowledged = diffObjectRepository
                .createOrUpdate(new DiffObject().withId(UUID.randomUUID().toString()).withLeft(encodedJson));

        Assert.assertTrue("The object diff was not inserted/updated", wasAcknowledged);
    }
}
