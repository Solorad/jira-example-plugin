package com.morenkov.jira.ao;

import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.Indexed;
import net.java.ao.schema.NotNull;
import net.java.ao.schema.StringLength;
import net.java.ao.schema.Table;

import java.util.Date;

/**
 * Property configuration entity.
 *
 * @author emorenkov
 */
@Preload({"propertyKey", "displayName", "description"})
@Table("ADMIN_CONFIG")
public interface AdminConfigEntity extends Entity {
    @NotNull
    @Indexed
    String getPropertyKey();
    void setPropertyKey(String propertyKey);

    @NotNull
    String getDisplayName();
    void setDisplayName(String displayName);

    @StringLength(StringLength.UNLIMITED)
    String getDescription();
    void setDescription(String description);

    Date getCreateDate();

    Date getEditDate();
    void setEditDate(Date date);
}
