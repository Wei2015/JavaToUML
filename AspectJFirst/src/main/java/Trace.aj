import org.aspectj.lang.Signature;

/**
 * Created by weiyao on 4/21/17.
 */
public aspect Trace {
    int count = 0;


    pointcut tracecall() : call (* *(..)) && within (Main);

    before() : cflow(tracecall()) && within (Main || TheEconomy || Pessimist || Optimist) {
        Signature sig = thisJoinPointStaticPart.getSignature();

       // String typeName = sig.getDeclaringTypeName();

        String methodName = sig.getName();


        String target = thisJoinPoint.getTarget().getClass().toString();


        String sourceName = thisJoinPointStaticPart.getSourceLocation().getWithinType().getCanonicalName();

        System.out.println( sourceName + " :" + methodName + "> " +  target + " :" + count++);

    }


}
