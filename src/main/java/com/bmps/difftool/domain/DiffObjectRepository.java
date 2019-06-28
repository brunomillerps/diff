package com.bmps.difftool.domain;

import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class DiffObjectRepository {

    private final MongoTemplate mongoTemplate;

    public DiffObjectRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public boolean createOrUpdate(DiffObject diffObject) {
        Objects.requireNonNull(diffObject, "A diff document is required.");

        Query query = Query.query(Criteria.where("id").is(diffObject.getId()));
        Update update = new Update();

        if (diffObject.getLeft() != null) {
            update.set("left", diffObject.getLeft());
        }

        if (diffObject.getRight() != null) {
            update.set("right", diffObject.getRight());
        }

        UpdateResult upsert = mongoTemplate.upsert(query, update, DiffObject.class);
        return upsert.wasAcknowledged();
    }
}
