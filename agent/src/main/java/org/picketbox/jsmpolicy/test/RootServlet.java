package org.picketbox.jsmpolicy.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.AccessControlException;
import java.security.Policy;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RootServlet extends HttpServlet {
    private static final long serialVersionUID = 3747690702102761650L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body>");
        out.println("<h1>JSM Testing Agent</h1>");
        out.println("<table>");
        out.println(getSecurityManagerRow());
        out.println(getPolicyClassRow());
        out.println(getPropertyRow("java.security.policy"));
        out.println(getPropertyRow("java.home"));
        out.println(getPropertyRow("jboss.home.dir"));
        out.println(getPropertyRow("jboss.node.name"));
        out.println("<tr><td>&nbsp;</td></tr>");
        out.println(getReadableFileRow("/etc/passwd"));
        out.println(getReadableFileRow("/etc/group"));
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
    }

    public String getSecurityManagerRow(){
        try{
            SecurityManager sm = System.getSecurityManager();
            if(sm == null){
                return row("Security manager", "null", "#F88");
            }else{
                return row("Security manager", sm.getClass().getName(), "#8F8");
            }
        }
        catch(Exception e){
            return row("Security manager", e.toString(), "#FF8");
        }
    }

    public String getPolicyClassRow(){
        try{
            Policy p = Policy.getPolicy();
            if(p == null){
                return row("Policy class", "null", "#F88");
            }else{
                return row("Policy class", p.getClass().getName(), "#8F8");
            }
        }
        catch(Exception e){
            return row("Policy class", e.toString(), "#FF8");
        }
    }

    public String getPropertyRow(String property){
        try{
            String pf = System.getProperty(property);
            return row(property, pf, "#8F8");
        }
        catch(AccessControlException e){
            return row(property, e.toString(), "#F88");
        }
        catch(Exception e){
            return row(property, e.toString(), "#FF8");
        }
    }

    public String getReadableFileRow(String file){
        File f = new File(file);
        try{
            InputStreamReader isr = new InputStreamReader(new FileInputStream(f));
            BufferedReader b = new BufferedReader(isr);
            String out = b.readLine();
            b.close();
            return row(file, out, "#8F8");
        }
        catch(AccessControlException e){
            return row(file, e.toString(), "#F88");
        }
        catch(Exception e){
            return row(file, e.toString(), "#AAA");
        }
    }

    public String row(String name, String value, String color){
        return "<tr><td style='background: "+color+"; padding: 5px 10px'>"+name+"</td><td style='background: "+color+"; padding: 5px 10px'>"+value+"</td></tr>";
    }
}
