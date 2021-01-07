import java.util.*;
import java.io.*;
import java.util.Map.*;

import soot.G;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.jimple.AssignStmt;
import soot.jimple.JimpleBody;

public class Tool {

    public static void setupSoot(String[] args){
        System.out.println("setting up soot..");
        G.reset();
        Options.v().set_prepend_classpath(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_soot_classpath("./tests");

        for(int i = 0; i < args.length; i++){
            Scene.v().addBasicClass(args[i]);
            SootClass sc = Scene.v().loadClassAndSupport(args[i]);
            sc.setApplicationClass();
        }
        Scene.v().loadNecessaryClasses();

        // for(SootClass x : Scene.v().getApplicationClasses())
        //     System.out.println(x.getName());
    }

    public static void getLocalVariables(JimpleBody body, HashMap<String, LinkedList<String>> adj, ArrayList<String> localVars){
        String local;
        for (Local u : body.getLocals()) {
            local = u.toString();
            localVars.add(local);

            LinkedList<String> l = new LinkedList<String>();
            adj.put(local, l);
        }
        // System.out.println(localVars);
        // System.out.println(adj);
    }

    public static void buildGraph(JimpleBody body, HashMap<String, LinkedList<String>> adj, ArrayList<String> localVars){
        int line = 1;
        for (Unit unit : body.getUnits()){
            System.out.println("(" + line + ")" + unit.toString());
            line++;
            if(unit instanceof AssignStmt){
                Value lhs = ((AssignStmt) unit).getLeftOp();
                // System.out.println(lhs.toString());
                Value rhs = ((AssignStmt) unit).getRightOp();
                for(ValueBox vb : rhs.getUseBoxes()){
                    String local = vb.getValue().toString();
                    // System.out.println(vb.getValue().toString());
                    if(localVars.contains(lhs.toString()) && localVars.contains(local))
                        adj.get(lhs.toString()).add(local);
                }
            }
        }
        System.out.println("\nGRAPH OF LOCAL VARIABLES : ");
        System.out.println(adj);
    }

    public static void depthFirstSearch(HashMap<String, LinkedList<String>> adj, String local, String mainlocal, HashMap<String, Set<String>> depends){
        if(mainlocal == local){
            HashSet<String> l = new HashSet<String>();
            depends.put(local, l);
        }
        else
            depends.get(mainlocal).add(local);

        for(String i : adj.get(local))
            depthFirstSearch(adj, i, mainlocal, depends);
    }

    public static void main(String[] args){
        setupSoot(args);
        for(int i = 0; i < args.length; i++){
            SootClass sc = Scene.v().getSootClass(args[i]);
            System.out.println("\n" + sc.getName());
            SootMethod sm = sc.getMethodByName("main");
            JimpleBody body = (JimpleBody) sm.retrieveActiveBody();

            ArrayList<String> localVars = new ArrayList<String>();
            HashMap<String, LinkedList<String>> adj = new HashMap<String, LinkedList<String>>();
            HashMap<String, Set<String>> depends = new HashMap<String, Set<String>>();

            getLocalVariables(body, adj, localVars);

            buildGraph(body, adj, localVars);

            for(String local : localVars)
                depthFirstSearch(adj, local, local, depends);
            System.out.println("\nDEPENDS : ");
            System.out.println(depends);
        }
    }
}
