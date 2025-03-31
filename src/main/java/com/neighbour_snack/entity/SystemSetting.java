package com.neighbour_snack.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "system_settings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SystemSetting {

    @Id
    @Column(name = "\"key\"")
    private String key;

    private String value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SystemSettingCategory  systemSettingCategory;

    public enum SystemSettingCategory {
        SMTP,
        APP_VERSION
    }

}
