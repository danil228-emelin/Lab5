package itmo.p3108.command.one_argument_command;

import itmo.p3108.command.type.Command;
import itmo.p3108.model.Person;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

/**
 * print all elements,which name start with argument
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class FilterStartsWithName implements Command {

    private String substring;

    @Override
    public String execute() {
        log.info("command FilterStartsWithName:print elements ");

        return
                controller
                        .getPersonList()
                        .stream()
                        .filter(x -> x.getName().startsWith(substring))
                        .parallel()
                        .map(Person::toString)
                        .collect(Collectors.joining("\n"));
    }

    @Override
    public String description() {
        return "filter_start_with_name name:выводит элементы чье имя начинается с name";
    }

    @Override
    public String name() {
        return "filter_starts_with_name";
    }

    /**
     * set the argument
     */
    public void setSubstring(@NonNull String substring) {
        this.substring = substring.toLowerCase();
    }
}
