package org.picketbox.jsmpolicy.test;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SwitchingSystemTestCase {

    Domain d;
    Agent s1;

    @Before
    public void before() throws Exception {
        d = new Domain("localhost", 9999, "full");
        d.restartSubsystem();
        d.addOrUpdatePolicy("policyMinimal", Constants.policyMinimal);
        d.addOrUpdatePolicy("policyAllowingTestingFile1", Constants.policyAllowingTestingFile1);
        d.addOrUpdatePolicy("policyAllowingTestingFile2", Constants.policyAllowingTestingFile2);
        s1 = new Agent("http://localhost:8080/JsmPolicyTestingAgent/");
    }

    @Test
    public void testAccessiblityOfServer() throws Exception {
        assertTrue(s1.isAccessible());
    }

    @Test
    public void testInitialConditions() throws Exception {
        assertTrue(s1.isReadable(Constants.testingFile1));
        assertTrue(s1.isReadable(Constants.testingFile2));
    }

    @Test
    public void testUndefinedPolicy() throws Exception {
        d.addOrUpdateServer(Constants.server1, null);
        Thread.sleep(1000);
        assertTrue(s1.isReadable(Constants.testingFile1));
        assertTrue(s1.isReadable(Constants.testingFile2));
    }

    @Test
    public void testMinimalPolicy() throws Exception {
        d.addOrUpdateServer(Constants.server1, "policyMinimal");
        Thread.sleep(1000);
        assertFalse(s1.isReadable(Constants.testingFile1));
        assertFalse(s1.isReadable(Constants.testingFile2));
    }

    @Test
    public void testPolicyAllowingTestingFile1() throws Exception {
        d.addOrUpdateServer(Constants.server1, "policyAllowingTestingFile1");
        Thread.sleep(1500);
        assertTrue(s1.isReadable(Constants.testingFile1));
        assertFalse(s1.isReadable(Constants.testingFile2));
    }

    @Test
    public void testPolicyAllowingTestingFile2() throws Exception {
        d.addOrUpdateServer(Constants.server1, "policyAllowingTestingFile2");
        Thread.sleep(1500);
        assertFalse(s1.isReadable(Constants.testingFile1));
        assertTrue(s1.isReadable(Constants.testingFile2));
    }

    @Test
    public void testSwitch1to2() throws Exception {

        d.addOrUpdateServer(Constants.server1, "policyAllowingTestingFile1");
        Thread.sleep(1500);
        assertTrue(s1.isReadable(Constants.testingFile1));
        assertFalse(s1.isReadable(Constants.testingFile2));

        d.addOrUpdateServer(Constants.server1, "policyAllowingTestingFile2");
        Thread.sleep(1500);
        assertFalse(s1.isReadable(Constants.testingFile1));
        assertTrue(s1.isReadable(Constants.testingFile2));

    }

    @After
    public void after() throws Exception {
        d.destroy();
    }

}
