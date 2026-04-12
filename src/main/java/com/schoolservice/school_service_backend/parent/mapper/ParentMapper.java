package com.schoolservice.school_service_backend.parent.mapper;

import com.schoolservice.school_service_backend.parent.dto.request.UpdateParentRequest;
import com.schoolservice.school_service_backend.parent.dto.response.ParentResponse;
import com.schoolservice.school_service_backend.parent.entity.Parent;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ParentMapper {

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "profileStatus", expression = "java(parent.getProfileStatus())")
    ParentResponse toResponse(Parent parent);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateParentFromDto(
            UpdateParentRequest request,
            @MappingTarget Parent parent
    );
}
