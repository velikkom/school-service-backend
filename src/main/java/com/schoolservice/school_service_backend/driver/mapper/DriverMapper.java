package com.schoolservice.school_service_backend.driver.mapper;

import com.schoolservice.school_service_backend.driver.dto.request.CreateDriverRequest;
import com.schoolservice.school_service_backend.driver.dto.response.DriverResponse;
import com.schoolservice.school_service_backend.driver.entity.Driver;
import com.schoolservice.school_service_backend.vehicle.dto.request.CreateVehicleRequest;
import com.schoolservice.school_service_backend.vehicle.dto.response.VehicleResponse;
import com.schoolservice.school_service_backend.vehicle.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "plateNumber", source = "vehicle.plateNumber")
    DriverResponse toResponse(Driver driver);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Driver toEntity(CreateDriverRequest request);
}