package org.picketbox.jsmpolicy.test;

public class Constants {

    static String server1 = "server-one";
    static String url1 = "http://localhost:8080/JsmPolicyTestingAgent/";

    static String testingFile1 = "/etc/passwd";
    static String testingFile2 = "/etc/group";

    // Permission which PolicyManager has to have for switching
    static String requiredPermissions =
            "    permission java.lang.RuntimePermission \"setSecurityManager\";\n" +
            "    permission java.security.SecurityPermission \"getPolicy\";\n" +
            "    permission java.lang.RuntimePermission \"createSecurityManager\";\n" +
            "    permission java.util.PropertyPermission \"jboss.server.temp.dir\",\"read\";\n" +
            "    permission java.util.PropertyPermission \"java.security.policy\",\"read,write\";\n" +
            "    permission java.util.PropertyPermission \"java.home\",\"read\";\n" +
            "    permission java.util.PropertyPermission \"jboss.home.dir\",\"read\";\n" +
            "    permission java.util.PropertyPermission \"jboss.node.name\",\"read\";\n" +
            "    permission java.io.FilePermission \"${jboss.server.temp.dir}/-\",\"read,write,delete\";\n";

    static String policyMinimal =
            "grant {\n" + requiredPermissions +
            "};";

    static String policyAllowingTestingFile1 =
            "grant {\n" + requiredPermissions +
            "    permission java.io.FilePermission \""+testingFile1+"\",\"read\";\n" +
            "};";

    static String policyAllowingTestingFile2 =
            "grant {\n" + requiredPermissions +
            "    permission java.io.FilePermission \""+testingFile2+"\",\"read\";\n" +
            "};";
}
