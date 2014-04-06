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
    Agent s1;

    @Before
    public void before() throws Exception {
        d = new Domain("localhost", 9999, "full");
        d.restartSubsystem();
        d.addOrUpdatePolicy("policyMinimal", Constants.policyMinimal);
        d.addOrUpdatePolicy("policyAllowingTestingFile1", Constants.policyAllowingTestingFile1);
        d.addOrUpdatePolicy("policyAllowingTestingFile2", Constants.policyAllowingTestingFile2);
        s1 = new Agent(Constants.url1);
    }

    @Test
    public void testAccessiblityOfServer1() throws Exception {
        assertTrue(s1.isAccessible());
    }

    @Test
    public void testNameEqualityServer1() throws Exception {
        assertEquals(Constants.server1, s1.getServer());
    }

    @After
    public void after() throws Exception {
        d.destroy();
    }

}
