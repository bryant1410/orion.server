package org.eclipse.orion.internal.server.hosting;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.orion.internal.server.servlets.hosting.IHostedSite;
import org.eclipse.orion.internal.server.servlets.site.SiteConfiguration;
import org.eclipse.orion.internal.server.servlets.site.SiteConfigurationConstants;
import org.eclipse.orion.internal.server.servlets.workspace.WebUser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class HostedSite implements IHostedSite {
	
	private Map<String, String> mappings;
	private String userName;
	private String workspaceId;
	private String host;
	private String editServer;
	
	public HostedSite(SiteConfiguration siteConfig, WebUser user, String host, String devServer) {
		this.mappings = Collections.unmodifiableMap(createMap(siteConfig));
		this.userName = user.getName();
		this.workspaceId = siteConfig.getWorkspace();
		this.host = host;
		this.editServer = devServer;
	}

	private static Map<String,String> createMap(SiteConfiguration siteConfig) {
		Map<String,String> map = new HashMap<String,String>();
		JSONArray mappingsJson = siteConfig.getMappingsJSON();
		for (int i=0; i < mappingsJson.length(); i++) {
			try {
				JSONObject mapping = (JSONObject) mappingsJson.get(i);
				String source = mapping.optString(SiteConfigurationConstants.KEY_SOURCE, null);
				String target = mapping.optString(SiteConfigurationConstants.KEY_TARGET, null);
				if (source != null && target != null) {
					map.put(source, target);
				}
			} catch (JSONException e) {
				// Shouldn't happen
			}
		}
		return map; 
	}

	/**
	 * @return Mappings defined by the site configuration
	 */
	public Map<String, String> getMappings() {
		return mappings;
	}

	/**
	 * @return The user who launched this site
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return Workspace id that this site will use
	 */
	public String getWorkspaceId() {
		return workspaceId;
	}

	/**
	 * @return The host where this site is accessible
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * @return The URL of the Orion server where the hosted files can be edited
	 */
	public String getEditServerUrl() {
		return editServer;
	}

}