package ru.cleverhause.devices.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.With;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "device_params")
public class DeviceParamsEntity implements Serializable {
    @MongoId
    private String id;
    private String deviceName;
    // should be unique with  deviceName
//    @Indexed(unique = true)
    private String username;
    @CreatedDate
    private LocalDateTime created;
    @LastModifiedDate
    private LocalDateTime changed;
    @NonNull
    private Set<SensorParamsEntity> sensors;

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SensorParamsEntity {
        private Integer id;
        private Double min;
        private Double max;
        private Double discrete;
        private Boolean adj;
        private Boolean rotate;
        private Boolean signaling;

        @CreatedDate
        private LocalDateTime created;
        @LastModifiedDate
        private LocalDateTime changed;
    }
}
