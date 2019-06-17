package pro.novatech.solutions.kelasiapi.v1;

/**
 * Created by p.lukengu on 4/1/2017.
 */

public enum MimeType {

    APPLICATION_JSON("application/json"),
    TEXT_XML("text/xml");

    private String type;

    MimeType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }

}
