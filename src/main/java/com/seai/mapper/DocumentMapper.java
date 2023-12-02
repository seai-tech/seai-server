package com.seai.mapper;

import com.seai.domain.document.model.MarineDocument;
import com.seai.request.VerifyDocumentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DocumentMapper {

    MarineDocument map(VerifyDocumentRequest verifyDocumentRequest);
}
