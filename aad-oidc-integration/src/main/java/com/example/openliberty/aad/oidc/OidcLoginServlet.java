package com.example.openliberty.aad.oidc;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.websphere.security.openidconnect.PropagationHelper;
import com.ibm.websphere.security.openidconnect.token.IdToken;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/home")
@ServletSecurity(value = @HttpConstraint(rolesAllowed= {"authenticated"}))
public class OidcLoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Inject
    @ConfigProperty(name = "admin.group.id")
    private String ADMIN_GROUP_ID;
	
	@Inject
    @ConfigProperty(name = "users.group.id")
    private String USERS_GROUP_ID;
	
	@Inject
    @ConfigProperty(name = "tenant.id")
    private String TENANT_ID;
	
	@Inject
    @ConfigProperty(name = "logout.redirect.url.https")
    private String LOGOUT_REDIRECT_URL_HTTPS;
	
    public OidcLoginServlet() {
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		IdToken idToken = PropagationHelper.getIdToken();
		log.info("Claims of id token are: {}", idToken.getAllClaimsAsJson());
		log.info("The access token is: {}", idToken.getAccessToken());
		
		List<String> groups = (List<String>)idToken.getClaim("groups");
		String role;
		if (groups.contains(ADMIN_GROUP_ID)) {
			role = "administrator";
		} else if (groups.contains(USERS_GROUP_ID)) {
			role = "user";
		} else {
			role = "unknown";
		}
		
		Principal principal = request.getUserPrincipal();
		log.info("The logged-on user is {}, role is {}", principal.getName(), role);
		
		PrintWriter pw = response.getWriter();
		
		pw.append("<!DOCTYPE html><html lang=\"en\"><body>");
        pw.append(String.format("<p>The logged-on user is <b>%s</b>, role is <b>%s</b></p>", principal.getName(), role));
        pw.append("<p><a href=\"logout\">Logout</a></p>");
        pw.append(String.format("<p><a href=\"https://login.microsoftonline.com/%s/oauth2/v2.0/logout?post_logout_redirect_uri=%s\">Logout from AAD</a></p>", TENANT_ID, LOGOUT_REDIRECT_URL_HTTPS));
        pw.append("</body></html>");
        pw.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
