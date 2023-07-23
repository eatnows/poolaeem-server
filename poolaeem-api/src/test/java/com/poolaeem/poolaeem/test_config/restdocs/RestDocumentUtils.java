package com.poolaeem.poolaeem.test_config.restdocs;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;

public interface RestDocumentUtils {
    static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                Preprocessors.prettyPrint(),
                Preprocessors.modifyUris()
                        .scheme("https")
                        .host("poolaeem.com")
                        .removePort()
        );
    }

    static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(
                Preprocessors.prettyPrint()
        );
    }
}
