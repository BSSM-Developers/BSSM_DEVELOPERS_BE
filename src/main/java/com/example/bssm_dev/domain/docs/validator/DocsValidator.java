//package com.example.bssm_dev.domain.docs.validator;
//
//import com.example.bssm_dev.domain.docs.exception.DocsNotCustomTypeException;
//import com.example.bssm_dev.domain.docs.exception.DocsSectionRequiredException;
//import com.example.bssm_dev.domain.docs.exception.DocsSectionMismatchException;
//import com.example.bssm_dev.domain.docs.exception.UnauthorizedDocsAccessException;
//import com.example.bssm_dev.domain.user.model.User;
//
//public class DocsValidator {
//
//    public static void checkIfIsMyDocs(User user, Docs docs) {
//        boolean isMyDocs = docs.isMyDocs(user);
//        if (!isMyDocs) throw UnauthorizedDocsAccessException.raise();
//    }
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
//}
