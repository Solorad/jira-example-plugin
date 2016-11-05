package com.morenkov.jira.service;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.morenkov.jira.ao.AdminConfigEntity;
import com.morenkov.jira.dto.AdminConfig;
import net.java.ao.DBParam;
import net.java.ao.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author emorenkov
 */
@Component
public class AdminConfigService {

    private static final Logger log = LoggerFactory.getLogger(AdminConfigService.class);

    private final ActiveObjects ao;

    @Autowired
    public AdminConfigService(@ComponentImport ActiveObjects ao) {
        this.ao = ao;
    }

    public AdminConfig getPropertyConfigByKey(String key) {
        if (key == null) {
            log.error("Null propertyKey was sent to search");
            return null;
        }
        AdminConfigEntity[] propertyConfigs = ao.find(AdminConfigEntity.class,
                                                      Query.select().where("PROPERTY_KEY = ?", key));
        if (propertyConfigs == null || propertyConfigs.length == 0) {
            return null;
        }
        return transformToDto(propertyConfigs[0]);
    }

    public List<AdminConfig> getAllPropertyConfigs() {
        List<AdminConfig> propertyConfigs = new ArrayList<>();
        AdminConfigEntity[] adminConfigEntities = ao.find(AdminConfigEntity.class);
        for (AdminConfigEntity entity : adminConfigEntities) {
            AdminConfig config = transformToDto(entity);
            propertyConfigs.add(config);
        }
        return propertyConfigs;
    }

    public AdminConfig setPropertyConfig(AdminConfig config) {
        if (config == null) {
            log.error("Null property config was sent");
            return new AdminConfig();
        }

        AdminConfigEntity entity;
        if (config.getId() == null
            || (entity = ao.get(AdminConfigEntity.class, config.getId())) == null) {
            entity = ao.create(AdminConfigEntity.class,
                               new DBParam("PROPERTY_KEY", config.getPropertyKey()),
                               new DBParam("DISPLAY_NAME", config.getDisplayName()),
                               new DBParam("DESCRIPTION", config.getDescription()),
                               new DBParam("CREATE_DATE", Calendar.getInstance().getTime()));
        } else if (!entity.getPropertyKey().equals(config.getPropertyKey())
                   || !entity.getDisplayName().equals(config.getDisplayName())) {

            if (!StringUtils.isEmpty(config.getPropertyKey())) {
                entity.setPropertyKey(config.getPropertyKey());
            }

            if (!StringUtils.isEmpty(config.getDisplayName())) {
                entity.setDisplayName(config.getDisplayName());
            }

            entity.setEditDate(Calendar.getInstance().getTime());
            entity.save();
        }
        return transformToDto(entity);
    }

    public boolean removePropertyConfig(Integer configKey) {
        if (configKey == null) {
            log.error("Null configKey was send to remove");
            return false;
        }
        AdminConfigEntity config = getPropertyConfig(configKey);
        if (config != null) {
            ao.delete(config);
            return true;
        }
        return false;
    }

    private AdminConfigEntity getPropertyConfig(Integer configKey) {
        return ao.get(AdminConfigEntity.class, configKey);
    }

    private AdminConfig transformToDto(AdminConfigEntity entity) {
        if (entity == null) {
            return null;
        }
        return new AdminConfig(
                entity.getID()
                , entity.getPropertyKey()
                , entity.getDisplayName()
                , entity.getDescription());
    }
}
