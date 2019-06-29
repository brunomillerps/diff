package com.bmps.difftool.domain;

import com.bmps.difftool.DiffProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiffObjectRepositoryTest {

    @Autowired
    private DiffObjectRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test(expected = NullPointerException.class)
    public void returnExceptionForNullDocumentTest() {
        repository.createOrUpdate(null);
    }

    @Test
    public void createNewDocumentWithLeftAndRightSideTest() {
        //GIVEN
        UUID id = UUID.randomUUID();
        DiffObject diffObject =
                new DiffObject()
                        .withId(id.toString())
                        .withLeft(Base64.getEncoder().encodeToString(DiffProvider.JSON_LEFT_STRING.getBytes()).getBytes())
                        .withRight(Base64.getEncoder().encodeToString(DiffProvider.JSON_RIGHT_STRING.getBytes()).getBytes());

        //WHEN
        boolean acknowledged = repository.createOrUpdate(diffObject);

        //THEN
        Assert.assertTrue(acknowledged);

        List<DiffObject> diffObjectList = mongoTemplate.find(Query.query(Criteria.where("id").is(diffObject.getId())), DiffObject.class);
        Assert.assertEquals(1, diffObjectList.size());
        Assert.assertEquals(id.toString(), diffObjectList.get(0).getId());
    }
}
