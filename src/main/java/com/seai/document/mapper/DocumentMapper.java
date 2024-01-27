package com.seai.document.mapper;

import com.seai.document.model.MarineDocument;
import com.seai.document.contract.request.VerifyDocumentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DocumentMapper {

    MarineDocument map(VerifyDocumentRequest verifyDocumentRequest);
}
