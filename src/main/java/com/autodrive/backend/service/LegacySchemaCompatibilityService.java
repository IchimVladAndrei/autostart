package com.autodrive.backend.service;

import com.autodrive.backend.entity.vehicle.ExtraOption;
import com.autodrive.backend.entity.vehicle.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LegacySchemaCompatibilityService {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    private Boolean carsTableExists;
    private Boolean carExtraOptionsTableExists;

    public UUID ensureLegacyCar(Vehicle vehicle) {
        if (vehicle == null || !tableExists("cars")) {
            return null;
        }

        UUID carId = findLegacyCarId(vehicle.getVin());
        if (carId == null) {
            carId = UUID.randomUUID();
            try {
                jdbcTemplate.update(
                        "insert into cars (id, model, price, vin, brand_id) values (?, ?, ?, ?, ?)",
                        carId,
                        vehicle.getModel(),
                        price(vehicle),
                        vehicle.getVin(),
                        brandId(vehicle)
                );
            } catch (DuplicateKeyException ignored) {
                carId = findLegacyCarId(vehicle.getVin());
            }
        }

        if (carId != null) {
            updateLegacyCar(vehicle, carId);
            syncLegacyCarOptions(vehicle, carId);
        }

        return carId;
    }

    private UUID findLegacyCarId(String vin) {
        try {
            return jdbcTemplate.queryForObject("select id from cars where vin = ?", UUID.class, vin);
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }
    }

    private void updateLegacyCar(Vehicle vehicle, UUID carId) {
        jdbcTemplate.update(
                "update cars set model = ?, price = ?, brand_id = ? where id = ?",
                vehicle.getModel(),
                price(vehicle),
                brandId(vehicle),
                carId
        );
    }

    private void syncLegacyCarOptions(Vehicle vehicle, UUID carId) {
        if (!tableExists("car_extra_options")) {
            return;
        }

        jdbcTemplate.update("delete from car_extra_options where car_id = ?", carId);
        Set<ExtraOption> options = vehicle.getExtraOptions();
        if (options == null || options.isEmpty()) {
            return;
        }

        for (ExtraOption option : options) {
            jdbcTemplate.update(
                    "insert into car_extra_options (car_id, extra_option_id) values (?, ?)",
                    carId,
                    option.getId()
            );
        }
    }

    private BigDecimal price(Vehicle vehicle) {
        return vehicle.getBasePrice();
    }

    private UUID brandId(Vehicle vehicle) {
        return vehicle.getBrand() != null ? vehicle.getBrand().getId() : null;
    }

    private boolean tableExists(String tableName) {
        if ("cars".equals(tableName) && Boolean.TRUE.equals(carsTableExists)) {
            return true;
        }
        if ("car_extra_options".equals(tableName) && Boolean.TRUE.equals(carExtraOptionsTableExists)) {
            return true;
        }

        boolean exists = checkTableExists(tableName);
        if ("cars".equals(tableName)) {
            carsTableExists = exists ? Boolean.TRUE : null;
        } else if ("car_extra_options".equals(tableName)) {
            carExtraOptionsTableExists = exists ? Boolean.TRUE : null;
        }
        return exists;
    }

    private boolean checkTableExists(String tableName) {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (var tables = metaData.getTables(null, null, tableName, new String[]{"TABLE"})) {
                if (tables.next()) {
                    return true;
                }
            }
            try (var tables = metaData.getTables(null, null, tableName.toUpperCase(Locale.ROOT), new String[]{"TABLE"})) {
                return tables.next();
            }
        } catch (SQLException | DataAccessException ignored) {
            return false;
        }
    }
}
