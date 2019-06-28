package com.bmps.difftool.domain;

import com.bmps.difftool.DiffProvider;
import com.bmps.difftool.exception.ClientException;
import com.bmps.difftool.exception.DiffObjectNotFound;
import com.bmps.difftool.rest.DiffOperationResponse;
import com.github.difflib.algorithm.DiffException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiffObjectComparatorServiceTest {

    @Autowired
    private DiffObjectComparatorService diffService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void findDiffObjectDiffObjectNotFoundTest() {
        //THEN
        Assertions.assertThrows(DiffObjectNotFound.class, () -> diffService.compare(UUID.randomUUID(), ComparatorType.MYER));
    }

    @Test
    public void findDiffObjectLeftSideIsNullTest() {

        //GIVEN
        UUID id = UUID.randomUUID();
        DiffObject diffObject = new DiffObject().withId(id.toString());
        mongoTemplate.save(diffObject, "diff_objects");

        Assertions.assertThrows(ClientException.class, () -> diffService.compare(id, ComparatorType.MYER));
    }

    @Test
    public void findDiffObjectRightSideIsNullTest() {
        //GIVEN
        UUID id = UUID.randomUUID();
        DiffObject diffObject =
                new DiffObject()
                        .withId(id.toString())
                        .withLeft(Base64.getEncoder()
                                .encodeToString(DiffProvider.JSON_LEFT_STRING.getBytes()).getBytes());

        mongoTemplate.save(diffObject, "diff_objects");

        //WHEN~THEN
        Assertions.assertThrows(ClientException.class, () -> diffService.compare(id, ComparatorType.MYER));
    }

    @Test
    public void bothSideAreEqualThenJustReturnThat() throws DiffException, ClientException, IOException {
        //GIVEN
        UUID id = UUID.randomUUID();
        DiffObject diffObject =
                new DiffObject()
                        .withId(id.toString())
                        .withLeft(Base64.getEncoder().encodeToString(DiffProvider.JSON_LEFT_STRING.getBytes()).getBytes())
                        .withRight(Base64.getEncoder().encodeToString(DiffProvider.JSON_LEFT_STRING.getBytes()).getBytes());

        mongoTemplate.save(diffObject, "diff_objects");

        //WHEN
        DiffOperationResponse response = diffService.compare(id, ComparatorType.MYER);

        //THEN
        Assert.assertNotNull(response);
        Assert.assertTrue(response.isMatched());
        Assert.assertEquals("{\"type\":\"object\",\"description\":\"An runtime exception\",\"properties\":{\"error\":{\"format\":\"string\",\"description\":\"More information may be given here to tell the client what have happened.\"}}}",
                response.getData().toString());

    }
}
