package com.neighbour_snack.dto;

import com.neighbour_snack.entity.SystemSetting;
import com.neighbour_snack.entity.SystemSetting.SystemSettingCategory;

public record SystemSettingDTO(String key, String value, SystemSettingCategory systemSettingCategory) {

    public static SystemSettingDTO fromSystemSetting(SystemSetting systemSetting) {
        return new SystemSettingDTO(systemSetting.getKey(), systemSetting.getValue(), systemSetting.getSystemSettingCategory());
    }

    public SystemSetting toSystemSetting() {
        return SystemSetting.builder()
                .key(this.key)
                .value(this.value)
                .systemSettingCategory(this.systemSettingCategory)
                .build();
    }

}
