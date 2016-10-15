package fi.gosu.ika.visa.ircbot;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Created by Aikain on 15.10.2016.
 */
public class ReadXml<T> {

    private final Class<T> type;
    private final String path;
    public ReadXml(Class<T> type, String path) {
        this.type = type;
        this.path = path;
    }
    public T getObj() throws JAXBException {
        return (T) JAXBContext.newInstance(this.type).createUnmarshaller().unmarshal(new File(path));
    }
    public void writeToXml(T obj) throws JAXBException {
        Marshaller m = JAXBContext.newInstance(this.type).createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(obj, new File(path));
    }
}
