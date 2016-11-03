package com.morenkov.jira.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Data transfer object for property config entity.
 *
 * @author emorenkov
 */
@XmlRootElement
public class AdminConfig {
    @XmlElement(name = "id")
    private Integer id;
    @XmlElement (name = "propertyKey")
    private String propertyKey;
    @XmlElement (name = "displayName")
    private String displayName;

    public AdminConfig() {
    }

    public AdminConfig(Integer id, String propertyKey, String displayName) {
        this.id = id;
        this.propertyKey = propertyKey;
        this.displayName = displayName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
