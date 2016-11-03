package com.morenkov.jira.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;


@SuppressWarnings({"UnusedDeclaration"})
@XmlRootElement
public class PropertyMoveBean {

    @XmlElement
    private URI after;

    @XmlElement
    private Position position;


    //Needed so that JAXB works.
    public PropertyMoveBean() {
    }

    /**
     * Absolute positions that a version may be moved to
     */
    public enum Position {
        Earlier,
        Later,
        First,
        Last
    }

    public URI getAfter() {
        return after;
    }

    public void setAfter(URI after) {
        this.after = after;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
