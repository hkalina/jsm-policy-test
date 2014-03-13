package org.picketbox.jsmpolicy.test;

import static org.junit.Assert.*;

import java.io.File;

import org.jboss.as.controller.client.ModelControllerClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.Client;

public class SystemTestCase {

    Domain d;
    Server s1;

    private File testingFile1 = new File("/etc/passwd");
    private File testingFile2 = new File("/etc/group");

    // Permission which PolicyManager has to have for switching
    private String requiredPermissions =
            "    permission java.lang.RuntimePermission \"setSecurityManager\";\n" +
            "    permission java.security.SecurityPermission \"getPolicy\";\n" +
            "    permission java.lang.RuntimePermission \"createSecurityManager\";\n" +
            "    permission java.util.PropertyPermission \"jboss.server.temp.dir\",\"read\";\n" +
            "    permission java.util.PropertyPermission \"java.security.policy\",\"read,write\";\n" +
            "    permission java.io.FilePermission \"${jboss.server.temp.dir}/-\",\"read,write,delete\";\n";

    private String policyMinimal =
            "grant {\n" + requiredPermissions +
            "};";

    private String policyAllowingTestingFile1 =
            "grant {\n" + requiredPermissions +
            "    permission java.io.FilePermission \""+testingFile1.getAbsolutePath()+"\",\"read\";\n" +
            "};";

    private String policyAllowingTestingFile2 =
            "grant {\n" + requiredPermissions +
            "    permission java.io.FilePermission \""+testingFile2.getAbsolutePath()+"\",\"read\";\n" +
            "};";

    @Before
    public void before() throws Exception {
        d = new Domain("localhost", 9999, "full");
        d.addOrUpdatePolicy("policyMinimal", policyMinimal);
        d.addOrUpdatePolicy("policyAllowingTestingFile1", policyAllowingTestingFile1);
        d.addOrUpdatePolicy("policyAllowingTestingFile2", policyAllowingTestingFile2);
        s1 = new Server("http://localhost:8080/JsmPolicyTestingAgent/");
    }

    @Test
    public void testInitialConditions() throws Exception {
        assertTrue(s1.isAccessible());
        assertFalse(s1.isReadable(testingFile1.getAbsolutePath()));
        assertFalse(s1.isReadable(testingFile2.getAbsolutePath()));
    }

    @After
    public void after() throws Exception {
        d.destroy();
    }

}
