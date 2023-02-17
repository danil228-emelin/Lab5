package itmo.p3108.parser;

import itmo.p3108.command.type.Command;
import itmo.p3108.exception.ValidationException;
import itmo.p3108.model.*;
import itmo.p3108.util.CheckData;
import itmo.p3108.util.CollectionController;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * parse elements from and to the xml file
 */
@Slf4j
public final class Parser {
    private static final int TOTAL_NODES = 19;
    private static ArrayList<Optional<String>> optionals = new ArrayList<>();

    private Parser() {
    }

    private static String information(Element element, String tegName) {
        return element.getElementsByTagName(tegName).item(0).getTextContent();
    }

    public static void read(@NonNull String path) {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            Long max_id = 0L;
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder db = dbf.newDocumentBuilder();

            File file = new File(path);
            if (file.length() == 0) {
               log.warn("Fail " + path + " is empty, collection empty as well");
                throw new ValidationException("Fail " + path + " is empty, collection empty as well");
            }
            Document doc = db.parse(file);

            doc.getDocumentElement().normalize();

            NodeList personList = doc.getElementsByTagName("person");

            for (int temp = 0; temp < personList.getLength(); temp++) {
                Node personNode = personList.item(temp);
                if (personNode.getChildNodes().getLength() != TOTAL_NODES) {
                    log.error("Error during parsing:element with index " + temp);
                    System.err.println("Error during parsing:element with index " + temp);
                    System.err.println("Some attributes are absent ");
                    continue;
                }
                if (personNode.getNodeType() == Node.ELEMENT_NODE) {


                    Element personElem = (Element) personNode;

                    Optional<String> eyeColour = Optional.ofNullable(information(personElem, "eyeColor"));

                    Optional<String> id = Optional.ofNullable(information(personElem, "id"));
                    optionals.add(id);
                    Optional<String> name = Optional.ofNullable(information(personElem, "name"));
                    optionals.add(name);
                    Optional<String> height = Optional.ofNullable(information(personElem, "height"));
                    optionals.add(height);
                    Optional<String> createDate = Optional.ofNullable(information(personElem, "creationDate"));
                    optionals.add(createDate);
                    Optional<String> birthday = Optional.ofNullable(information(personElem, "birthday"));
                    optionals.add(birthday);
                    Optional<String> nationality = Optional.ofNullable(information(personElem, "nationality"));
                    optionals.add(nationality);

                    Optional<String> xCoordinates = Optional.empty();
                    Optional<String> yCoordinates = Optional.empty();
                    NodeList coordinateList = doc.getElementsByTagName("coordinates");

                    Optional<String> x = Optional.empty();
                    Optional<String> y = Optional.empty();
                    Optional<String> z = Optional.empty();
                    Optional<String> placeName = Optional.empty();

                    for (int i = 0; i < coordinateList.getLength(); i++) {
                        Element coordinateElem = (Element) coordinateList.item(i);
                        xCoordinates = Optional.ofNullable(information(coordinateElem, "x"));
                        yCoordinates = Optional.ofNullable(information(coordinateElem, "y"));
                    }
                    NodeList location = doc.getElementsByTagName("location");

                    for (int i = 0; i < location.getLength(); i++) {
                        Element coordinateElem = (Element) location.item(i);
                        x = Optional.ofNullable(information(coordinateElem,"x"));
                        y = Optional.ofNullable(information(coordinateElem,"y"));
                        z = Optional.ofNullable(information(coordinateElem,"z"));
                        placeName = Optional.ofNullable(information(coordinateElem,"name"));
                    }
                    if (optionals.stream().parallel().anyMatch(Optional::isEmpty)) {
                        log.error("Error during parsing:element with index " + temp+" value of attribute is null,change or fix xml file");
                        System.err.println("Error during parsing:element with index " + temp);
                        System.err.println("value of attribute is null,change or fix xml file");
                        continue;
                    }


                    if (
                            CheckData.checkName(name.get()) &&
                                    CheckData.checkColourReadingFile(eyeColour.get()) &&
                                    CheckData.checkId(id.get()) &&
                                    CheckData.checkHeight(height.get()) &&
                                    CheckData.checkBirthday(birthday.get()) &&
                                    CheckData.checkNationalityReadingFromFile(nationality.get()) &&
                                    CheckData.checkCoordinateX(xCoordinates.get()) &&
                                    CheckData.checkCoordinateY(yCoordinates.get()) &&
                                    CheckData.checkLocationCoordinateY(y.get()) &&
                                    CheckData.checkLocationCoordinateX(x.get()) &&
                                    CheckData.checkLocationCoordinateZ(z.get()) &&
                                    CheckData.checkName(placeName.get()) &&
                                    CheckData.checkCreationTime(createDate.get())
                    ) {
                    } else {
                        log.error("Error during parsing:some attributes was in incorrect format");
                        System.err.println("Error during parsing:some attributes was in incorrect format");
                    }

                    Person person = Person.builder()
                            .id(Long.parseLong(id.get()))
                            .name(name.get())
                            .height(Double.parseDouble(height.get()))
                            .eyeColor(Color.valueOf(eyeColour.get()))
                            .creationDate(ZonedDateTime.parse(createDate.get()))
                            .nationality(Country.valueOf(nationality.get()))
                            .birthday(LocalDate.parse(birthday.get(), DateTimeFormatter.ofPattern("MM-dd-yyyy")))
                            .coordinates(Coordinates.builder()
                                    .x(Integer.parseInt(xCoordinates.get()))
                                    .y(Float.valueOf(yCoordinates.get()))
                                    .build())
                            .location(Location.builder()
                                    .name(placeName.get())
                                    .x(Double.parseDouble(x.get()))
                                    .y(Float.valueOf(y.get()))
                                    .z(Float.parseFloat(z.get()))
                                    .name(placeName.get())
                                    .build())
                            .build();

                    if (!Command.controller.getPersonList().contains(person)) {
                        Command.controller.add(person);
                        max_id = Long.parseLong(id.get()) > max_id ? Long.parseLong(id.get()) : max_id;
                    }
                }


            }
            PersonReadingBuilder.setId(max_id);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error("Error during parsing:file has incorrect data,collection is empty");
            System.err.println("Error during parsing:file has incorrect data,collection is empty");
        } catch (ValidationException e) {
            log.error(e.getMessage());
            System.err.println(e.getMessage());
        } catch (NullPointerException | NoSuchElementException e) {
            log.error("Error during parsing:One of the element is null,change or fix xml file");
            System.err.println("Error during parsing:One of the element is null,change or fix xml file");
        }

    }

    public static void write(@NonNull String path) throws JAXBException, FileNotFoundException {

        CollectionController controller = CollectionController.getInstance();
        JAXBContext contextObj = JAXBContext.newInstance(CollectionController.class);

        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshallerObj.marshal(controller, new FileOutputStream(path));


    }

}
