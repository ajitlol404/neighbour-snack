package com.neighbour_snack.dao;

import com.neighbour_snack.entity.SystemSetting;
import com.neighbour_snack.entity.SystemSetting.SystemSettingCategory;
import com.neighbour_snack.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemSettingRepository extends JpaRepository<SystemSetting, String> {

    List<SystemSetting> findBySystemSettingCategory(SystemSettingCategory systemSettingCategory);

    default SystemSetting findSystemSettingByKey(String key) {
        return findById(key).orElseThrow(() -> new ResourceNotFoundException("SystemSetting with [KEY= " + key + "] not found"));
    }

}
