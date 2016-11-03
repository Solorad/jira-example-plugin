package com.morenkov.jira.rest;

import com.atlassian.sal.api.message.I18nResolver;
import com.google.common.collect.ImmutableMap;
import com.morenkov.jira.dto.AdminConfig;
import com.morenkov.jira.service.AdminConfigService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static com.atlassian.gzipfilter.org.apache.commons.lang.StringUtils.isEmpty;

/**
 * Rest endpoint for property addition.
 */
@Path("/config")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class AdminPageResource {

    private static final Logger LOG = LoggerFactory.getLogger(AdminPageResource.class);

    private final AdminConfigService adminConfigService;
    private final I18nResolver i18n;

    public AdminPageResource(AdminConfigService adminConfigService
            , I18nResolver i18n) {
        this.adminConfigService = adminConfigService;
        this.i18n = i18n;
    }

    @GET
    public Response getAllConfigs() {
        LOG.debug("getAllConfigs");
        List<AdminConfig> configs = adminConfigService.getAllPropertyConfigs();
        return Response.ok(configs).build();
    }

    @POST
    public Response createPropertyConfig(final AdminConfig config) {
        LOG.debug("createPropertyConfig {}", config);
        String validations = validateInput(config);
        if (validations != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ImmutableMap.of("errors", validations)).build();
        }
        AdminConfig result = adminConfigService.setPropertyConfig(config);
        return Response.ok(result).build();
    }

    @PUT
    @Path("{id}")
    public Response updatePropertyConfig(@PathParam("id") final String id, final AdminConfig config) {
        LOG.debug("updatePropertyConfig id = {}, config = {}", id, config);
        String validations = validateInputForEdit(config);
        if (validations != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ImmutableMap.of("errors", validations)).build();
        }
        AdminConfig result = adminConfigService.setPropertyConfig(config);
        return Response.ok(result).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") final Integer id) {
        LOG.debug("delete entity with id = {}", id);
        if (adminConfigService.removePropertyConfig(id)) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(ImmutableMap.of("errors", "NOT_EXISTS")).build();
    }

    private String validateInput(AdminConfig config) {
        if (config == null) {
            return i18n.getText("solarwinds.comment-property.admin.values-empty");
        }
        if (isEmpty(config.getPropertyKey())) {
            return i18n.getText("solarwinds.comment-property.admin.property-key-empty");
        }
        if (isEmpty(config.getDisplayName())) {
            return i18n.getText("solarwinds.comment-property.admin.display-name-empty");
        }
        if (config.getPropertyKey().indexOf(' ') != -1) {
            return i18n.getText("solarwinds.comment-property.admin.key-with-space");
        }


        AdminConfig propertyConfigByKey = adminConfigService.getPropertyConfigByKey(config.getPropertyKey());
        if (propertyConfigByKey != null) {
            LOG.error("The entry with such key already exists");
            return i18n.getText("solarwinds.comment-property.admin.key-exists");
        }

        return null;
    }

    private String validateInputForEdit(AdminConfig config) {
            if (config == null || config.getId() == null) {
                LOG.error("Invalid config file {}", config);
                return i18n.getText("solarwinds.comment-property.admin.values-empty");
            }

            if (config.getPropertyKey() != null) {
                if (config.getPropertyKey().indexOf(' ') != -1) {
                    return i18n.getText("solarwinds.comment-property.admin.key-with-space");
                }
                if (StringUtils.isEmpty(config.getPropertyKey())) {
                    return i18n.getText("solarwinds.comment-property.admin.property-key-empty");
                }


                AdminConfig propertyConfigByKey =
                        adminConfigService.getPropertyConfigByKey(config.getPropertyKey());
                if (propertyConfigByKey != null) {
                    LOG.error("The entry with such key already exists");
                    return i18n.getText("solarwinds.comment-property.admin.key-exists");
                }
            }
            if ("".equals(config.getDisplayName())) {
                LOG.error("The entry with such key already exists");
                return i18n.getText("solarwinds.comment-property.admin.display-name-empty");
            }
        return null;
    }



}