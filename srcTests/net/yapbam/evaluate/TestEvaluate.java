package net.yapbam.evaluate;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class TestEvaluate {
  public static void main(String[] args) throws Exception{
    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine engine = mgr.getEngineByName("JavaScript");
    String foo = "40*(5+2)";
    Object eval = engine.eval(foo);
		System.out.println(eval);
    } 
}
