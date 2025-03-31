package com.neighbour_snack.service.impl;

import com.neighbour_snack.dao.SystemSettingRepository;
import com.neighbour_snack.dto.SystemSettingDTO;
import com.neighbour_snack.entity.SystemSetting;
import com.neighbour_snack.entity.SystemSetting.SystemSettingCategory;
import com.neighbour_snack.service.SystemSettingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.neighbour_snack.dto.SystemSettingDTO.fromSystemSetting;

@Service
@RequiredArgsConstructor
public class SystemSettingServiceImpl implements SystemSettingService {

    private final SystemSettingRepository systemSettingRepository;

    @Override
    public List<SystemSettingDTO> getAllSystemSettings() {
        return systemSettingRepository.findAll()
                .stream()
                .map(SystemSettingDTO::fromSystemSetting)
                .toList();
    }

    @Override
    @Transactional
    public void saveAllSystemSetting(List<SystemSettingDTO> systemSettingDTOS) {
        systemSettingRepository.saveAll(systemSettingDTOS.stream().map(SystemSettingDTO::toSystemSetting).toList());
    }

    @Override
    public List<SystemSettingDTO> getSystemSettingsByCategory(SystemSettingCategory systemSettingCategory) {
        return systemSettingRepository.findBySystemSettingCategory(systemSettingCategory)
                .stream()
                .map(SystemSettingDTO::fromSystemSetting)
                .toList();
    }

    @Override
    public SystemSettingDTO getSystemSettingByKey(String key) {
        return fromSystemSetting(systemSettingRepository.findSystemSettingByKey(key));
    }

    @Override
    @Transactional
    public SystemSettingDTO saveSystemSetting(SystemSettingDTO systemSettingDTO) {
        return fromSystemSetting(systemSettingRepository.save(systemSettingDTO.toSystemSetting()));
    }

    @Override
    @Transactional
    public SystemSettingDTO updateSystemSettingValue(String key, String value) {
        SystemSetting systemSetting = systemSettingRepository.findSystemSettingByKey(key);
        systemSetting.setValue(value);
        SystemSetting updatedSystemSetting = systemSettingRepository.save(systemSetting);
        return fromSystemSetting(updatedSystemSetting);
    }

    @Override
    public String getSystemSettingValueByKey(String key) {
        return getSystemSettingByKey(key).value();
    }
}

