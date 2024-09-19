package com.seai.manning_agent.sailor.document.mapper;

import com.seai.manning_agent.sailor.document.contract.request.UpdateDocumentRequest;
import com.seai.manning_agent.sailor.document.contract.response.CreateDocumentResponse;
import com.seai.manning_agent.sailor.document.contract.response.GetDocumentResponse;
import com.seai.manning_agent.sailor.document.model.MarineDocument;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ManningAgentDocumentMapper {

    MarineDocument map(UpdateDocumentRequest document);

    GetDocumentResponse map(MarineDocument document);

    CreateDocumentResponse mapCreate(MarineDocument document);
}
