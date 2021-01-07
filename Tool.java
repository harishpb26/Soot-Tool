import java.io.*;
import java.util.*;

import soot.G;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.ValueBox;
import soot.options.Options;
import soot.jimple.JimpleBody;

public class Tool {
    public static String key;

    public static void setupSoot(String[] args){
        System.out.println("setting up soot..");
        G.reset();
        Options.v().set_prepend_classpath(true);
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_soot_classpath("./tests");
        // System.out.println(Scene.v().getSootClassPath());

        for(int i = 0; i < args.length; i++){
            Scene.v().addBasicClass(args[i]);
            SootClass sc = Scene.v().loadClassAndSupport(args[i]);
            sc.setApplicationClass();
        }
        Scene.v().loadNecessaryClasses();

        // for(SootClass x : Scene.v().getApplicationClasses())
        //     System.out.println(x.getName());
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

    public static Set<String> getLocalVariables(JimpleBody body, HashMap<String, LinkedList<String>> adj){
        Set<String> localVars = new HashSet<String>();
        for (Local u : body.getLocals()) {
            String local = u.toString();
            localVars.add(local);

            LinkedList<String> l = new LinkedList<String>();
            adj.put(local, l);
        }
        // System.out.println(localVars);
        // System.out.println(adj);
        return localVars;
    }

    public static void buildGraph(JimpleBody body, HashMap<String, LinkedList<String>> adj, Set<String> localVars){
        int line = 1;
        boolean isUse = true;
        String localName;
        System.out.println("\nJIMPLEBODY : ");
        for (Unit u : body.getUnits()){
            System.out.println("(" + line + ") " + u.toString());
            for (ValueBox v : u.getUseAndDefBoxes()){
                if(localVars.contains(v.getValue().toString())){
                    localName = v.getValue().toString();
                    if(isUse){
                        key = localName;
                        isUse = false;
                    }
                    else{
                        LinkedList<String> l = adj.get(key);
                        l.add(localName);
                    }
                    // System.out.println(localName);
                }
            }
            line++;
            isUse = true;
        }
        System.out.println("\nGRAPH OF LOCAL VARIABLES : ");
        System.out.println(adj);
    }

    public static void main(String[] args){
        setupSoot(args);
        for(int i = 0; i < args.length; i++){
            SootClass sc = Scene.v().getSootClass(args[i]);
            SootMethod sm = sc.getMethodByName("main");
            JimpleBody body = (JimpleBody) sm.retrieveActiveBody();

            HashMap<String, LinkedList<String>> adj = new HashMap<>();
            Set<String> localVars = getLocalVariables(body, adj);
            buildGraph(body, adj, localVars);

            HashMap<String, Set<String>> depends = new HashMap<>();
            for(String local : localVars)
                depthFirstSearch(adj, local, local, depends);
            System.out.println("\nDEPENDS : ");
            System.out.println(depends);
        }
    }
}
