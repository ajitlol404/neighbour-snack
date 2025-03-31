package com.neighbour_snack.service;

import com.neighbour_snack.dto.SystemSettingDTO;
import com.neighbour_snack.entity.SystemSetting.SystemSettingCategory;

import java.util.List;

public interface SystemSettingService {

    List<SystemSettingDTO> getAllSystemSettings();

    void saveAllSystemSetting(List<SystemSettingDTO> systemSettingDTOS);

    List<SystemSettingDTO> getSystemSettingsByCategory(SystemSettingCategory systemSettingCategory);

    SystemSettingDTO getSystemSettingByKey(String key);

    SystemSettingDTO saveSystemSetting(SystemSettingDTO systemSettingDTO);

    SystemSettingDTO updateSystemSettingValue(String key, String value);

    String getSystemSettingValueByKey(String key);

}
