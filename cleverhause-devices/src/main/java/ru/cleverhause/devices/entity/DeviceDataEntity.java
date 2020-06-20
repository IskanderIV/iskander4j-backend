package ru.cleverhause.devices.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@Document(collection = "device_data")
public class DeviceDataEntity implements Serializable {
    @MongoId
    private Long id;

    private Set<SensorDataEntity> sensors;
    private DeviceErrorEntity errors;

    @LastModifiedDate
    private LocalDateTime changed;

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    public static class SensorDataEntity {
        private int id;
        private Double ack;
        private Boolean adj; //PFB
        private Double ctrlVal; // PFB
        private Boolean radioErr;

        @CreatedDate
        private LocalDateTime created;
        @LastModifiedDate
        private LocalDateTime changed;
    }

    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    public static class DeviceErrorEntity {
        private Boolean gsm;
        private Boolean lcd;
        private Boolean radio;
    }
}
