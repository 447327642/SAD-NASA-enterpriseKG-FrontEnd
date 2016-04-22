package models;

/**
 * Created by yuanchen on 4/22/16.
 */
public class PublicationEntity {
    private Publications publications;
    private Author author;
    private PublicationFigure publicationFigure;
    private PublicationTopic publicationTopic;

    public Publications getPublications() {
        return publications;
    }

    public Author getAuthor() {
        return author;
    }

    public PublicationFigure getPublicationFigure() {
        return publicationFigure;
    }

    public PublicationTopic getPublicationTopic() {
        return publicationTopic;
    }

    public void setPublications(Publications publications) {
        this.publications = publications;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setPublicationFigure(PublicationFigure publicationFigure) {
        this.publicationFigure = publicationFigure;
    }

    public void setPublicationTopic(PublicationTopic publicationTopic) {
        this.publicationTopic = publicationTopic;
    }
}
