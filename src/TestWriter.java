/**
 * Test class for Writer.java. Contain 5 test cases from the assignments.
 */
public class TestWriter {
    public static void main(String[] args) {

        String test1 = "@startuml\n" +
                "A \"1\"-- \"*\" B\n" +
                "A \"1\"-- \"1\" C\n" +
                "A \"1\"-- \"*\" D \n" +
                "class A {\n" +
                "-x: int\n" +
                "-y: int[]\n" +
                "}\n" +
                "@enduml";
        System.out.println(test1);

        Writer.draw(test1, "/Users/weiyao/Documents/SJSU SE/2017 Spring/CMPE202/JavaToUML/testCases/test1.svg");

        String test2 = "@startuml\n" +
                "interface A1\n" +
                "interface A2\n" +
                "P <|-- B1\n" +
                "A1 <|.. B1\n" +
                "P <|-- B2\n" +
                "A1 <|.. B2\n" +
                "A2 <|.. B2\n" +
                "C1 ..>A1 : uses\n" +
                "C2 ..>A2 : uses\n" +
                "@enduml";
        System.out.println(test2);
        Writer.draw(test2, "/Users/weiyao/Documents/SJSU SE/2017 Spring/CMPE202/JavaToUML/testCases/test2.svg");


        String test3 = "@startuml\n" +
                "class ClassA{\n" +
                "+message : String \n" +
                "-bark: String \n" +
                "testMethod() : void\n" +
                "}\n" +
                "ClassA <|-- ClassB \n" +
                "class ClassB {\n" +
                "-hello: String\n" +
                "}\n" +
                "@enduml";
        System.out.println(test3);
        Writer.draw(test3, "/Users/weiyao/Documents/SJSU SE/2017 Spring/CMPE202/JavaToUML/testCases/test3.svg");

        String test4 = "@startuml\n" +
                "interface Subject {\n " +
                "+attach (obj: Observer): void\n" +
                "+detach(obj: Observer): void\n" +
                "+notifyObserver(): void\n" +
                "}\n" + //interface
                "interface Observer {\n" +
                "+update():void\n" +
                "}\n" + //interface
                "Observer <|..ConcreteObserver\n" +
                "class ConcreteObserver {\n" +
                "+ConcreteObserver(theSubject: ConcreteObserver)\n" +
                "+update(): void\n" +
                "+showState(): void\n" +
                "}\n" + //implement interface
                "Subject <|..ConcreteSubject\n" + //implement interface
                "Observer <..ConcreteSubject\n"+ // use in parameter
                "Observer \"*\"--\"1\"ConcreteSubject\n"+ //use in private variable
                "class ConcreteSubject {\n" +
                "-subjectState:String\n" +
                "+getState():String\n" +
                "+setState(status:String):void\n" +
                "+showState(): void\n"+
                "}\n" + //implement interface, use interface
                "ConcreteSubject <|-- TheEconomy\n" +
                "class TheEconomy {\n" +
                "+TheEconomy()\n" +
                "}\n" +
                "ConcreteObserver <|-- Pessimist\n " +
                "class Pessimist{\n" +
                "+Pessimist(sub:ConcreteSubject)\n"+
                "+update(): void\n" +
                "}\n" +
                "ConcreteObserver <|-- Optimist\n" +
                "class Optimist {\n" +
                "+Optimist(sub:ConcreteSubject) \n" +
                "+update(): void\n" +
                "}\n" +
                "@enduml";

        System.out.println(test4);
        Writer.draw(test4, "/Users/weiyao/Documents/SJSU SE/2017 Spring/CMPE202/JavaToUML/testCases/test4.svg");


        String test5 = "@startuml\n" +
                "interface Component{\n" +
                "+operation():String\n" +
                "}\n" +
                "Component <|.. ConcreteComponent \n" +
                "class ConcreteComponent {\n" +
                "+operation():String\n" +
                "}\n" +
                "Component <|.. Decorator\n" +
                "Component \"1\" -- \"1\"Decorator\n"+ //private attribute of an interface type
                "Component <.. Decorator\n" + //private attribute of an interface type
                "class Decorator {\n" +
                "-component:Component\n" +
                "+Decorator(c: Component)\n" +
                "+operation():String\n" +
                "}\n" +
                "Decorator <|-- ConcreteDecoratorA\n" +
                "Component <..ConcreteDecoratorA\n" +
                "class ConcreteDecoratorA {\n" +
                "-addedState:String\n" +
                "+ConcreteDecoratorA(c:Component)\n" +
                "+operation():String\n" +
                "}\n" +
                "Component <.. Tester\n" +
                "class Tester{\n" +
                "+main(args:String[]): void\n"+
                "}\n" +
                "Decorator <|-- ConcreteDecoratorB\n" +
                "Component <..ConcreteDecoratorB\n" +
                "class ConcreteDecoratorB {\n" +
                "-addedState:String\n" +
                "+ConcreteDecoratorB(c:Component)\n" +
                "+operation():String\n" +
                "}\n" +
                "@enduml";

        System.out.println(test5);
        Writer.draw(test5, "/Users/weiyao/Documents/SJSU SE/2017 Spring/CMPE202/JavaToUML/testCases/test5.svg");
    }
}
