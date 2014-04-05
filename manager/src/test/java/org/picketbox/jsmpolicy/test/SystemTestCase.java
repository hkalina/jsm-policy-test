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
    private String server1 = "jk-xubuntu";

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
        Thread.sleep(1000);
        d = new Domain("localhost", 9999, "full");
        d.restartSubsystem();
        d.addOrUpdatePolicy("policyMinimal", policyMinimal);
        d.addOrUpdatePolicy("policyAllowingTestingFile1", policyAllowingTestingFile1);
        d.addOrUpdatePolicy("policyAllowingTestingFile2", policyAllowingTestingFile2);
        s1 = new Server("http://localhost:8080/JsmPolicyTestingAgent/");
    }

    @Test
    public void testAccessiblityOfServer() throws Exception {
        assertTrue(s1.isAccessible());
    }

    @Test
    public void testInitialConditions() throws Exception {
        assertTrue(s1.isReadable(testingFile1.getAbsolutePath()));
        assertTrue(s1.isReadable(testingFile2.getAbsolutePath()));
    }

    @Test
    public void testUndefinedPolicy() throws Exception {
        d.addOrUpdateServer(server1, null);
        Thread.sleep(1000);
        assertTrue(s1.isReadable(testingFile1.getAbsolutePath()));
        assertTrue(s1.isReadable(testingFile2.getAbsolutePath()));
    }

    @Test
    public void testMinimalPolicy() throws Exception {
        d.addOrUpdateServer(server1, "policyMinimal");
        Thread.sleep(1000);
        assertFalse(s1.isReadable(testingFile1.getAbsolutePath()));
        assertFalse(s1.isReadable(testingFile2.getAbsolutePath()));
    }

    @Test
    public void testPolicyAllowingTestingFile1() throws Exception {
        d.addOrUpdateServer(server1, "policyAllowingTestingFile1");
        Thread.sleep(1000);
        assertTrue(s1.isReadable(testingFile1.getAbsolutePath()));
        assertFalse(s1.isReadable(testingFile2.getAbsolutePath()));
    }

    @Test
    public void testPolicyAllowingTestingFile2() throws Exception {
        d.addOrUpdateServer(server1, "policyAllowingTestingFile2");
        Thread.sleep(1000);
        assertFalse(s1.isReadable(testingFile1.getAbsolutePath()));
        assertTrue(s1.isReadable(testingFile2.getAbsolutePath()));
    }

    @Test
    public void testSwitch1to2() throws Exception {

        d.addOrUpdateServer(server1, "policyAllowingTestingFile1");
        Thread.sleep(1000);
        assertTrue(s1.isReadable(testingFile1.getAbsolutePath()));
        assertFalse(s1.isReadable(testingFile2.getAbsolutePath()));

        d.addOrUpdateServer(server1, "policyAllowingTestingFile2");
        Thread.sleep(1000);
        assertFalse(s1.isReadable(testingFile1.getAbsolutePath()));
        assertTrue(s1.isReadable(testingFile2.getAbsolutePath()));

    }

    @After
    public void after() throws Exception {
        d.destroy();
    }

}
