package com.example.bssm_dev.domain.docs.repository.impl;

import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.type.DocumentType;
import com.example.bssm_dev.domain.docs.model.type.ServerStatus;
import com.example.bssm_dev.domain.docs.repository.DocsQueryRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DocsQueryRepositoryImpl implements DocsQueryRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Slice<Docs> fetchDocs(DocumentType docsType, ServerStatus serverStatus, String cursor, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deletedAt").isNull());

        if (docsType != null) {
            query.addCriteria(Criteria.where("type").is(docsType));
        }

        if (serverStatus != null) {
            query.addCriteria(Criteria.where("serverStatus").is(serverStatus));
        }

        if (cursor != null) {
            query.addCriteria(Criteria.where("_id").lt(new ObjectId(cursor)));
        }

        query.with(Sort.by(Sort.Direction.DESC, "_id"));
        query.limit(pageable.getPageSize() + 1);

        List<Docs> results = mongoTemplate.find(query, Docs.class);

        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results.removeLast();
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    @Override
    public Slice<Docs> fetchMyDocs(Long writerId, DocumentType docsType, ServerStatus serverStatus, String cursor, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deletedAt").isNull());
        query.addCriteria(Criteria.where("writerId").is(writerId));

        if (docsType != null) {
            query.addCriteria(Criteria.where("type").is(docsType));
        }

        if (serverStatus != null) {
            query.addCriteria(Criteria.where("serverStatus").is(serverStatus));
        }

        if (cursor != null) {
            query.addCriteria(Criteria.where("_id").lt(new ObjectId(cursor)));
        }

        query.with(Sort.by(Sort.Direction.DESC, "_id"));
        query.limit(pageable.getPageSize() + 1);

        List<Docs> results = mongoTemplate.find(query, Docs.class);

        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results.removeLast();
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }


    @Override
    public Slice<Docs> fetchPopularDocs(DocumentType docsType, ServerStatus serverStatus, Long tokenCount, String cursor, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deletedAt").isNull());

        if (docsType != null) {
            query.addCriteria(Criteria.where("type").is(docsType));
        }

        if (serverStatus != null) {
            query.addCriteria(Criteria.where("serverStatus").is(serverStatus));
        }

        if (tokenCount != null && cursor != null) {
            Criteria cursorCriteria = new Criteria().orOperator(
                    Criteria.where("tokenCount").lt(tokenCount),
                    Criteria.where("tokenCount").is(tokenCount).and("_id").lt(new ObjectId(cursor))
            );
            query.addCriteria(cursorCriteria);
        }

        query.with(Sort.by(Sort.Order.desc("tokenCount"), Sort.Order.desc("_id")));
        query.limit(pageable.getPageSize() + 1);

        List<Docs> results = mongoTemplate.find(query, Docs.class);

        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results.removeLast();
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    @Override
    public Slice<Docs> fetchMyPopularDocs(Long writerId, DocumentType docsType, ServerStatus serverStatus, Long tokenCount, String cursor, Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deletedAt").isNull());
        query.addCriteria(Criteria.where("writerId").is(writerId));

        if (docsType != null) {
            query.addCriteria(Criteria.where("type").is(docsType));
        }

        if (serverStatus != null) {
            query.addCriteria(Criteria.where("serverStatus").is(serverStatus));
        }

        if (tokenCount != null && cursor != null) {
            Criteria cursorCriteria = new Criteria().orOperator(
                    Criteria.where("tokenCount").lt(tokenCount),
                    Criteria.where("tokenCount").is(tokenCount).and("_id").lt(new ObjectId(cursor))
            );
            query.addCriteria(cursorCriteria);
        }

        query.with(Sort.by(Sort.Order.desc("tokenCount"), Sort.Order.desc("_id")));
        query.limit(pageable.getPageSize() + 1);

        List<Docs> results = mongoTemplate.find(query, Docs.class);

        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results.removeLast();
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
}
