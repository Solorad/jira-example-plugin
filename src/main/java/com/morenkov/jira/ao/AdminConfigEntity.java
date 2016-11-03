package com.morenkov.jira.ao;

import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.Indexed;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.Table;

import java.util.Date;

/**
 * Property configuration active object entity.
 *
 * @author emorenkov
 */
@Preload
@Table("ADMIN_CONFIG")
public interface AdminConfigEntity extends Entity {
    @NotNull
    @Indexed
    String getPropertyKey();
    void setPropertyKey(String propertyKey);

    @NotNull
    String getDisplayName();
    void setDisplayName(String displayName);

    Date getCreateDate();

    Date getEditDate();
    void setEditDate(Date date);
}
