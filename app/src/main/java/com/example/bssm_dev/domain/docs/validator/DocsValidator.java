package com.example.bssm_dev.domain.docs.validator;

import com.example.bssm_dev.domain.api.exception.EndpointNotFoundException;
import com.example.bssm_dev.domain.api.repository.ApiRepository;
import com.example.bssm_dev.domain.docs.dto.request.CreateDocsPageRequest;
import com.example.bssm_dev.domain.docs.exception.DocsCustomApiPageMustBeReferenceException;
import com.example.bssm_dev.domain.docs.exception.DocsNotCustomTypeException;
import com.example.bssm_dev.domain.docs.exception.DocsSectionRequiredException;
import com.example.bssm_dev.domain.docs.exception.DocsSectionMismatchException;
import com.example.bssm_dev.domain.docs.exception.UnauthorizedDocsAccessException;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.user.model.User;

import java.util.List;

public class DocsValidator {

    public static void checkIfIsMyDocs(User user, Docs docs) {
        boolean isMyDocs = docs.isMyDocs(user);
        if (!isMyDocs) throw UnauthorizedDocsAccessException.raise();
    }

    public static void checkCustomDocsPages(List<CreateDocsPageRequest> requests) {
        requests.forEach(request -> {
            boolean isApiPage = request.endpoint() != null;
            boolean isReference = request.sourceDocsId() != null;
            if (isApiPage && !isReference) {
                throw DocsCustomApiPageMustBeReferenceException.raise();
            }
        });
    }

    public static void checkParsedEndpointsExist(List<CreateDocsPageRequest> requests, Long repoId, ApiRepository apiRepository) {
        requests.stream()
                .filter(r -> r.endpoint() != null && r.sourceDocsId() == null)
                .forEach(r -> {
                    boolean exists = apiRepository.existsByGithubRepositoryIdAndEndpointAndMethodAndIsCurrentTrue(
                            repoId, r.endpoint(), r.method()
                    );
                    if (!exists) {
                        throw EndpointNotFoundException.raise();
                    }
                });
    }
//
//    public static void checkIfIsMyDocs(User user, DocsSection section) {
//        boolean isMyDocs = section.isMyDocs(user);
//        if (!isMyDocs) throw UnauthorizedDocsAccessException.raise();
//    }
//
//    public static void checkIfIsSectionOfDocs(Long docsId, DocsSection section) {
//        boolean isSectionOfDocs = section.isSectionOfDocs(docsId);
//        if (!isSectionOfDocs) throw DocsSectionMismatchException.raise();
//    }
//
//    public static void checkCustomizeDocs(Docs originalDocs) {
//        boolean isCustomDocs = originalDocs.isCustom();
//        if (!isCustomDocs) throw DocsNotCustomTypeException.raise();
//    }
//
//    public static void checkHasAtLeastOneSection(Docs docs) {
//        boolean hasAtLeastOneSection = docs.getSectionsSize() >= 1;
//        if (!hasAtLeastOneSection) throw DocsSectionRequiredException.raise();
//    }
}
