package com.morenkov.jira.servlet;

import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.util.velocity.VelocityRequestContextFactory;
import com.atlassian.templaterenderer.TemplateRenderer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet for generating admin page.
 *
 * @author emorenkov
 */
public class AdministrationExampleServlet extends HttpServlet {
    private static final String TEMPLATE_PATH = "templates/servlet/admin-page.vm";
    private final GlobalPermissionManager permissionManager;
    private final TemplateRenderer templateRenderer;
    private final JiraAuthenticationContext authContext;
    private final VelocityRequestContextFactory contextFactory;



    public AdministrationExampleServlet(GlobalPermissionManager permissionManager
            , TemplateRenderer templateRenderer
            , JiraAuthenticationContext authContext
            , VelocityRequestContextFactory contextFactory) {
        this.permissionManager = permissionManager;
        this.templateRenderer = templateRenderer;
        this.authContext = authContext;
        this.contextFactory = contextFactory;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (isAuthenticated(request, response)) {
            Map<String, Object> params = new HashMap<>();
            contextFactory.getJiraVelocityRequestContext().getBaseUrl();
            templateRenderer.render(TEMPLATE_PATH, contextFactory.getDefaultVelocityParams(params, authContext),
                    response.getWriter());
        }
    }

    /**
     * if user is not an admin -> redirects to login page
     */
    private boolean isAuthenticated(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ApplicationUser user = authContext.getLoggedInUser();
        if (user == null || !permissionManager.hasPermission(GlobalPermissionKey.ADMINISTER, user)) {
            response.sendRedirect(getUri(request).toASCIIString());
            return false;
        }
        return true;
    }


    private URI getUri(HttpServletRequest request) {
        return URI.create(contextFactory.getJiraVelocityRequestContext().getBaseUrl() + "/login.jsp?os_destination=" + request.getServletPath()
                          + "&permissionViolation=true");
    }
}
