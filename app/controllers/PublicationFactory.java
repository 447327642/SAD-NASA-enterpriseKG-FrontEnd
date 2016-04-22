package controllers;

/**
 * Created by Holysoul on 4/18/16.
 */
public class PublicationFactory extends AbstractFactory{
    @Override
    PublicationControllerInterface getPublicationControllerInterface(String PublicationControllerInterface) {
        return null;
    }

    public PublicationControllerInterface getExecution(String execution){
        if(execution == null){
            return null;
        }
        if(execution.equalsIgnoreCase("publicationRegistration")){
            return new PublicationRegistration();
        }

        return null;
    }
}


