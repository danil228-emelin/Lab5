package itmo.p3108.command;

import itmo.p3108.command.type.IndependentCommand;
import itmo.p3108.command.type.NoArgumentCommand;
import itmo.p3108.model.Person;
import itmo.p3108.model.PersonReadingBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * Command Remove Greater,
 * remove elements witch greater than created element
 * after reading command,user creates new element.
 * if new element is greater than elements in collections will be deleted
 * provided with default comparator,compare by name and then birthday
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RemoveGreater implements NoArgumentCommand, IndependentCommand {
    @Setter
    @NonNull
    private Comparator<Person> comparator = Comparator.comparing(Person::getName).thenComparing(Person::getBirthday);

    /**
     * @return result of command invocation
     * no elements can be deleted
     */
    @Override
    public String execute() {

        PersonReadingBuilder personReadingBuilder = PersonReadingBuilder.getInstance();
        Person person = Person
                .builder()
                .name(personReadingBuilder.createName())
                .id(personReadingBuilder.createId())
                .height(personReadingBuilder.createHight())
                .eyeColor(personReadingBuilder.createColor())
                .nationality(personReadingBuilder.createNationality())
                .birthday(personReadingBuilder.createBirthDay())
                .coordinates(personReadingBuilder.createCoordinates())
                .creationDate(ZonedDateTime.now())
                .location(personReadingBuilder.createLocation())
                .build();
        ArrayList<Person> arrayList = controller.getPersonList();
        Collection<Person> collection = arrayList.stream().filter(x -> comparator.compare(x, person) > 0).toList();

        if (arrayList.removeAll(collection)) {
            log.info("command RemoveGreater deleted all suitable elements ");
            return "All suitable elements are deleted  ";
        }
        log.info("command RemoveGreater deleted nothing ");

        return "Nothing was deleted";
    }

    @Override
    public String description() {
        return "remove_greater {element} : ?????????????? ???? ?????????????????? ?????? ????????????????, ????????????, ?????? ????????????????";

    }

    @Override
    public String name() {
        return "remove_greater";
    }

}
