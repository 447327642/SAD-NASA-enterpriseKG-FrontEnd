package controllers;

/**
 * Created by Holysoul on 4/21/16.
 */
public abstract class AbstractFactory {
    abstract PublicationControllerInterface getPublicationControllerInterface(String PublicationControllerInterface);
}
