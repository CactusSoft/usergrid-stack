package org.usergrid.rest.management.organizations;

import junit.framework.Assert;
import org.codehaus.jackson.JsonNode;
import org.junit.Ignore;
import org.junit.Test;
import org.usergrid.cassandra.Concurrent;
import org.usergrid.management.OrganizationInfo;
import org.usergrid.rest.AbstractRestIT;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.usergrid.utils.MapUtils.hashMap;


@Concurrent()
public class OrganizationResourceIT extends AbstractRestIT {

  @Test
  @Ignore( "Running alone is fine, with other tests it bombs: " +
          "OrganizationResourceIT.testOrganizationUpdate:32 » UniformInterface POST htt..." )
  public void testOrganizationUpdate() throws Exception {
    Map<String, Object> properties = new HashMap<String,Object>();
    properties.put("securityLevel", 5);

    Map payload = hashMap("email",
        "test-user-1@mockserver.com").map("username", "test-user-1")
        .map("name", "Test User").map("password", "password")
        .map("organization", "test-org-1")
        .map("company", "Apigee");
    payload.put(OrganizationsResource.ORGANIZATION_PROPERTIES, properties);

    JsonNode node = resource().path("/management/organizations")
        .accept(MediaType.APPLICATION_JSON)
        .type(MediaType.APPLICATION_JSON_TYPE)
        .post(JsonNode.class, payload);
    assertNotNull(node);

    OrganizationInfo orgInfo = setup.getMgmtSvc().getOrganizationByName("test-org-1");
    assertEquals(5L, orgInfo.getProperties().get("securityLevel"));

    payload = new HashMap();
    properties.put("securityLevel", 6);
    payload.put(OrganizationsResource.ORGANIZATION_PROPERTIES, properties);

    node = resource().path("/management/organizations/test-org-1")
        .queryParam("access_token", superAdminToken())
        .accept(MediaType.APPLICATION_JSON)
        .type(MediaType.APPLICATION_JSON_TYPE)
        .put(JsonNode.class, payload);
    logNode(node);

    node = resource().path("/management/organizations/test-org-1")
        .queryParam("access_token", superAdminToken())
        .accept(MediaType.APPLICATION_JSON)
        .type(MediaType.APPLICATION_JSON_TYPE)
        .get(JsonNode.class);
    logNode(node);
    Assert.assertEquals(6, node.get("organization").get(OrganizationsResource.ORGANIZATION_PROPERTIES).get("securityLevel").asInt());
  }

}
