package com.bmps.difftool.domain;

import com.bmps.difftool.AbstractSpringIntegrationTest;
import com.bmps.difftool.DiffProvider;
import com.bmps.difftool.exception.ClientException;
import com.bmps.difftool.exception.DiffObjectNotFound;
import com.bmps.difftool.rest.DiffOperationResponse;
import com.github.difflib.algorithm.DiffException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

public class DiffObjectComparatorServiceIntegrationTest extends AbstractSpringIntegrationTest {

    @Autowired
    private DiffObjectComparatorService diffService;

    @Test(expected = DiffObjectNotFound.class)
    public void findDiffObjectDiffObjectNotFoundTest() throws DiffException, ClientException, IOException {
        //THEN
        diffService.compare(UUID.randomUUID(), ComparatorType.MYER);
    }

    @Test(expected = ClientException.class)
    public void findDiffObjectLeftSideIsNullTest() throws DiffException, ClientException, IOException {

        //GIVEN
        UUID id = UUID.randomUUID();
        DiffObject diffObject = new DiffObject().withId(id.toString());
        mongoTemplate.save(diffObject, "diff_objects");
        diffService.compare(id, ComparatorType.MYER);
    }

    @Test(expected = ClientException.class)
    public void findDiffObjectRightSideIsNullTest() throws DiffException, ClientException, IOException {
        //GIVEN
        UUID id = UUID.randomUUID();
        DiffObject diffObject =
                new DiffObject()
                        .withId(id.toString())
                        .withLeft(Base64.getEncoder()
                                .encodeToString(DiffProvider.JSON_LEFT_STRING.getBytes()).getBytes());

        mongoTemplate.save(diffObject, "diff_objects");

        //WHEN/THEN
        diffService.compare(id, ComparatorType.MYER);
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
