package com.schoolservice.school_service_backend.student.mapper;

import com.schoolservice.school_service_backend.student.dto.request.CreateStudentRequest;
import com.schoolservice.school_service_backend.student.dto.response.StudentResponse;
import com.schoolservice.school_service_backend.student.entity.Student;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface StudentMapper {

    /* =========================
       ENTITY → RESPONSE
    ========================= */

    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "parentName",
            expression = "java(student.getParent().getFirstName() + \" \" + student.getParent().getLastName())")
    @Mapping(target = "routeStopId", source = "routeStop.id")
    @Mapping(target = "routeName", source = "routeStop.route.name")
    StudentResponse toResponse(Student student);


    /* =========================
       REQUEST → ENTITY
    ========================= */

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "routeStop", ignore = true)
    @Mapping(target = "active", constant = "true")
    Student toEntity(CreateStudentRequest request);


    /* =========================
       UPDATE MAPPING
    ========================= */

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStudentFromRequest(
            CreateStudentRequest request,
            @MappingTarget Student student
    );
}