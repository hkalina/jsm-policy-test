package org.picketbox.jsmpolicy.test;

public class Constants {

    static String jBossServer = "localhost";
    static int jBossPort = 9999;
    static String jBossProfile = "full";

    static String server1 = "server-one";
    static String url1 = "http://localhost:8080/JsmPolicyTestingAgent/";

    static String testingFile1 = "/etc/passwd";
    static String testingFile2 = "/etc/group";

    // Permission which PolicyManager has to have for switching
    static String diagnosticPermissions =
            "    permission java.security.SecurityPermission \"getPolicy\";\n" +
            "    permission java.util.PropertyPermission \"jboss.server.temp.dir\",\"read\";\n" +
            "    permission java.util.PropertyPermission \"java.security.policy\",\"read\";\n" +
            "    permission java.util.PropertyPermission \"java.home\",\"read\";\n" +
            "    permission java.util.PropertyPermission \"jboss.home.dir\",\"read\";\n" +
            "    permission java.util.PropertyPermission \"jboss.node.name\",\"read\";\n";

    static String policyMinimal =
            "grant {};";

    static String policyAllowingTestingFile1 =
            "grant {\n" + diagnosticPermissions +
            "    permission java.io.FilePermission \""+testingFile1+"\",\"read\";\n" +
            "};";

    static String policyAllowingTestingFile2 =
            "grant {\n" + diagnosticPermissions +
            "    permission java.io.FilePermission \""+testingFile2+"\",\"read\";\n" +
            "};";

    static String safePolicyAllowingTestingFile1 =
            "grant codeBase \"vfs:/content/JsmPolicyTestingAgent.war/-\" {\n" +
            "    permission java.io.FilePermission \""+testingFile1+"\",\"read\";\n" +
            "};";

    static String safePolicyAllowingTestingFile2 =
            "grant codeBase \"vfs:/content/JsmPolicyTestingAgent.war/-\" {\n" +
            "    permission java.io.FilePermission \""+testingFile2+"\",\"read\";\n" +
            "};";

}
