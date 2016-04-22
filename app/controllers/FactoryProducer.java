package controllers;

/**
 * Created by Holysoul on 4/21/16.
 */
public class FactoryProducer {
    public static AbstractFactory getFactory(String choice){
        if(choice.equalsIgnoreCase("PublicationControllerInterface")) {
            return new PublicationFactory();
        }
        return null;
    }
}
