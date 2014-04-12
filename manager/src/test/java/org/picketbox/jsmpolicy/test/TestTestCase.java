package org.picketbox.jsmpolicy.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testsuite verify if testing is correctly prepared:
 *  - agent is deployed on testing server
 *  - name of testing server corresponds with name in configuration
 */
public class TestTestCase {

    Domain d;
    Agent s1, s2;

    @Before
    public void before() throws Exception {
        s1 = new Agent(Constants.url1);
        s2 = new Agent(Constants.url2);
        d = new Domain(Constants.jBossProtocol, Constants.jBossServer, Constants.jBossPort, Constants.jBossProfile);
        d.restartSubsystem();
        d.addOrUpdatePolicy("policyMinimal", Constants.policyMinimal);
        d.addOrUpdatePolicy("policyAllowingTestingFile1", Constants.policyAllowingTestingFile1);
        d.addOrUpdatePolicy("policyAllowingTestingFile2", Constants.policyAllowingTestingFile2);
    }

    @Test
    public void testAccessiblityOfServer1() throws Exception {
        assertTrue(s1.isAccessible());
    }

    @Test
    public void testAccessiblityOfServer2() throws Exception {
        assertTrue(s2.isAccessible());
    }

    @Test
    public void testNameEqualityServer1() throws Exception {
        assertEquals(Constants.server1, s1.getServer());
    }

    @Test
    public void testNameEqualityServer2() throws Exception {
        assertEquals(Constants.server2, s2.getServer());
    }

    @After
    public void after() throws Exception {
        d.destroy();
    }

}
