package itmo.p3108.command.no_argument_command;

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
import java.util.Comparator;

/**
 * remove elements witch greater than created element
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RemoveGreater implements NoArgumentCommand, IndependentCommand {
    @Setter
    @NonNull
    private Comparator<Person> comparator = Comparator.comparing(Person::getName).thenComparing(Person::getBirthday);

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
        int size = arrayList.size();
        arrayList.removeIf(x -> {
                    if (comparator.compare(x, person) > 0) {
                        log.info("command RemoveGreater remove " + x.getName());
                        System.out.println(x.getName() + " removed ");
                        return true;
                    }
                    return false;
                }
        );
        if (size != arrayList.size()) {
            log.info("command RemoveGreater deleted all suitable elements ");

            return "All suitable elements are deleted  ";
        }
        log.info("command RemoveGreater deleted nothing ");

        return "Nothing was deleted";
    }

    @Override
    public String description() {
        return "remove_greater {element} : удалить из коллекции все элементы, больше, чем заданный";

    }

    @Override
    public String name() {
        return "remove_greater";
    }

}
