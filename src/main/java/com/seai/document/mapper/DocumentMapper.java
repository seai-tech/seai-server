package com.seai.document.mapper;

import com.seai.document.contract.request.UpdateDocumentRequest;
import com.seai.document.contract.response.GetDocumentResponse;
import com.seai.document.model.MarineDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DocumentMapper {

    MarineDocument map(UpdateDocumentRequest document);

    GetDocumentResponse map(MarineDocument document);
}
