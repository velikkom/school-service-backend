package com.schoolservice.school_service_backend.vehicle.mapper;

import com.schoolservice.school_service_backend.vehicle.dto.request.CreateVehicleRequest;
import com.schoolservice.school_service_backend.vehicle.dto.response.VehicleResponse;
import com.schoolservice.school_service_backend.vehicle.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    VehicleResponse toResponse(Vehicle vehicle);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Vehicle toEntity(CreateVehicleRequest request);
}