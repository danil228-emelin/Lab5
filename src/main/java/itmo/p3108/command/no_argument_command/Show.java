package itmo.p3108.command.no_argument_command;

import itmo.p3108.command.type.NoArgumentCommand;
import itmo.p3108.model.Person;

import java.util.stream.Collectors;

/**
 * print the elements of collection in order
 */
public class Show implements NoArgumentCommand {
    private static Show show;

    private Show() {
    }

    public static Show getInstance() {
        if (show == null) {
            show = new Show();
        }
        return show;
    }

    @Override
    public String execute() {
        return controller.getPersonList().stream()
                .map(Person::toString)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String name() {
        return "show";
    }
}