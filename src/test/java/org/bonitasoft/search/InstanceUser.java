package org.bonitasoft.search;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.api.ApiAccessType;
import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.identity.ContactData;
import org.bonitasoft.engine.platform.LoginException;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.util.APITypeManager;
import org.bonitasoft.search.exception.TableNotSupportedException;
import org.bonitasoft.search.options.SearchOptionsBuilderRelation;
import org.junit.Test;

public class InstanceUser {
    @Test
    public void testProcessStartedUserName() {
        SearchOptionsBuilderRelation sob = new SearchOptionsBuilderRelation( 0,10);
        sob.filter(UserSearchDescriptorRelation.USER_NAME, "Walter.Bates");
        sob.relation(ProcessInstanceSearchDescriptorRelation.STARTED_BY);
        APISession apiSession=getAPISession();
        
        SearchRelationAPI searchRelation = new SearchRelationAPI( apiSession );
        try {
            searchRelation.search(ProcessInstance.class, sob.done());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public void testProcessStartedUserEmail() {
        
        /**
         * I want all processinstance STARTED BY the user who have a email Walter Bates AND Process Definition.Name=TestProcess 
         */
        SearchOptionsBuilderRelation sob = new SearchOptionsBuilderRelation(0,10);
        sob.filter(ContactDataSearchDescriptorRelation.EMAIL, "Walter.Bates@bonitasoft.com");
        sob.filter(ContactDataSearchDescriptorRelation.PERSONAL, Boolean.FALSE);
        
        // we don't have the filterRelation. So, here we give this kind of relation, saying "Please link the second table by the STARTEDBY
        // First version: all relations must be explicited
        sob.relation(ProcessInstanceSearchDescriptorRelation.STARTED_BY);
        sob.relation(org.bonitasoft.search.ProcessInstanceSearchDescriptorRelation.PROCESS_DEFINITION_ID);
        sob.relation(org.bonitasoft.search.ContactDataSearchDescriptorRelation.USERID);
  
        sob.filter(ProcessDeploymentInfoSearchDescriptorRelation.NAME, "TestProcess");
        
        APISession apiSession=null;
        SearchRelationAPI searchRelation = new SearchRelationAPI( apiSession );
        try {
            searchRelation.search(ProcessInstance.class, sob.done());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    
    @Test
    public void testAdvanceSearch() {
        // link: Activity.assigned Helen Kelly / Process instance started by WalterBates
        
    }
    
    public APISession getAPISession() {
        try {
            // Define the REST parameters
            final Map<String, String> map = new HashMap<String, String>();

            /*if (newConnectMethod) {
                map.put("org.bonitasoft.engine.api-type.server.url",
                        applicationUrl == null ? "http://localhost:7080" : applicationUrl);
                map.put("org.bonitasoft.engine.api-type.application.name",
                        applicationName == null ? "bonita" : applicationName);

            } else {
            */
                map.put("server.url", "http://localhost:8080" );
                map.put("application.name", "bonita");
            
            APITypeManager.setAPITypeAndParams(ApiAccessType.HTTP, map);

            // Set the username and password
            // final String username = "helen.kelly";
            final String username = "walter.bates";
            final String password = "bpm";

            // get the LoginAPI using the TenantAPIAccessor
            LoginAPI loginAPI = TenantAPIAccessor.getLoginAPI();

            // log in to the tenant to create a session
            return loginAPI.login(username, password);
        } catch (final BonitaHomeNotSetException e) {
            e.printStackTrace();
        } catch (final ServerAPIException e) {
            e.printStackTrace();
        } catch (final UnknownAPITypeException e) {
            e.printStackTrace();
        } catch (final LoginException e) {
            e.printStackTrace();
        }
        return null;
    }
}
