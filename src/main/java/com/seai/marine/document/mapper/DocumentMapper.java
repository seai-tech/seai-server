package com.seai.marine.document.mapper;

import com.seai.marine.document.contract.request.UpdateDocumentRequest;
import com.seai.marine.document.contract.response.CreateDocumentResponse;
import com.seai.marine.document.contract.response.GetDocumentResponse;
import com.seai.marine.document.model.MarineDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DocumentMapper {

    MarineDocument map(UpdateDocumentRequest document);

    GetDocumentResponse map(MarineDocument document);

    CreateDocumentResponse mapCreate(MarineDocument document);
}
