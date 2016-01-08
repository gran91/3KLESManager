package org.kles.model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "responders")
public class ResponderWrapper {

    private List<Responder> responders;

    @XmlElement(name = "responder")
    public List<Responder> getResponders() {
        return this.responders;
    }

    public void setResponders(List<Responder> responders) {
        this.responders = responders;
    }
}
