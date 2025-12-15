package com.example.bssm_dev.domain.docs.service.query;

import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.domain.docs.dto.response.DocsListResponse;
import com.example.bssm_dev.domain.docs.mapper.DocsMapper;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.type.DocumentType;
import com.example.bssm_dev.domain.docs.repository.DocsRepository;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(value = "mongoTransactionManager", readOnly = true)
public class DocsQueryService {
    private final DocsRepository docsRepository;
    private final DocsMapper docsMapper;
    private final UserRepository userRepository;

    public CursorPage<DocsListResponse> getAllDocs(DocumentType docsType, String cursor, Integer size) {
        Pageable pageable = PageRequest.of(0, size + 1);
        
        Slice<Docs> docsSlice = docsRepository.fetchDocs(docsType, cursor, pageable);
        List<Docs> docsList = docsSlice.getContent();

        boolean hasNext = docsList.size() > size;
        List<Docs> content = hasNext ? docsList.subList(0, size) : docsList;

        Map<Long, User> userMap = getUserMapByWriterids(content);

        List<DocsListResponse> responses = docsMapper.toDocsListResponse(userMap, content);
       
        return new CursorPage<>(responses, hasNext);
    }

    public CursorPage<DocsListResponse> getMyDocs(User currentUser, DocumentType docsType, String cursor, Integer size) {
        Pageable pageable = PageRequest.of(0, size + 1);
        
        Slice<Docs> docsSlice = docsRepository.fetchMyDocs(currentUser.getUserId(), docsType, cursor, pageable);
        List<Docs> docsList = docsSlice.getContent();

        boolean hasNext = docsList.size() > size;
        List<Docs> content = hasNext ? docsList.subList(0, size) : docsList;

        String writerName = currentUser.getName();
        List<DocsListResponse> responses = docsMapper.toDocsListResponse(writerName, content);
        return new CursorPage<>(responses, hasNext);
    }

    private Map<Long, User> getUserMapByWriterids(List<Docs> content) {
        Set<Long> writerIds = content.stream()
                .map(Docs::getWriterId)
                .collect(Collectors.toSet());

        Map<Long, User> userMap = userRepository.findAllById(writerIds)
                .stream()
                .collect(
                        Collectors.toMap(User::getUserId, user -> user)
                );
        return userMap;
    }
}
