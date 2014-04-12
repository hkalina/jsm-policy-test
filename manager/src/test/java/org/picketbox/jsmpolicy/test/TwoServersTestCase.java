package org.picketbox.jsmpolicy.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TwoServersTestCase {

    Domain d;
    Agent s1, s2;

    @Before
    public void before() throws Exception {
        d = new Domain(Constants.jBossProtocol, Constants.jBossServer, Constants.jBossPort, Constants.jBossProfile);
        d.restartSubsystem();
        d.addOrUpdatePolicy("policyMinimal", Constants.policyMinimal);
        d.addOrUpdatePolicy("policyAllowingTestingFile1", Constants.policyAllowingTestingFile1);
        d.addOrUpdatePolicy("policyAllowingTestingFile2", Constants.policyAllowingTestingFile2);
        s1 = new Agent(Constants.url1);
        s2 = new Agent(Constants.url2);
    }

    @Test
    public void testInitialConditions() throws Exception {
        assertTrue(s1.isReadable(Constants.testingFile1));
        assertTrue(s1.isReadable(Constants.testingFile2));
        assertTrue(s2.isReadable(Constants.testingFile1));
        assertTrue(s2.isReadable(Constants.testingFile2));
    }

    @Test
    public void testP1P2Policy() throws Exception {
        d.addOrUpdateServer(Constants.server1, "policyAllowingTestingFile1");
        d.addOrUpdateServer(Constants.server2, "policyAllowingTestingFile2");
        Thread.sleep(Constants.switchingTime);
        assertTrue(s1.isReadable(Constants.testingFile1));
        assertFalse(s1.isReadable(Constants.testingFile2));
        assertFalse(s2.isReadable(Constants.testingFile1));
        assertTrue(s2.isReadable(Constants.testingFile2));
    }

    @Test
    public void testSwitchP1P2toP2P1Policy() throws Exception {
        d.addOrUpdateServer(Constants.server1, "policyAllowingTestingFile1");
        d.addOrUpdateServer(Constants.server2, "policyAllowingTestingFile2");
        Thread.sleep(Constants.switchingTime);
        assertTrue(s1.isReadable(Constants.testingFile1));
        assertFalse(s1.isReadable(Constants.testingFile2));
        assertFalse(s2.isReadable(Constants.testingFile1));
        assertTrue(s2.isReadable(Constants.testingFile2));

        d.addOrUpdateServer(Constants.server1, "policyAllowingTestingFile2");
        d.addOrUpdateServer(Constants.server2, "policyAllowingTestingFile1");
        Thread.sleep(Constants.switchingTime);
        assertFalse(s1.isReadable(Constants.testingFile1));
        assertTrue(s1.isReadable(Constants.testingFile2));
        assertTrue(s2.isReadable(Constants.testingFile1));
        assertFalse(s2.isReadable(Constants.testingFile2));
    }

    @After
    public void after() throws Exception {
        d.destroy();
    }

}