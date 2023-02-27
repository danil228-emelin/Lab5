package itmo.p3108.command.no_argument_command;

import itmo.p3108.command.type.IndependentCommand;
import itmo.p3108.command.type.NoArgumentCommand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * exit without saving collection
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Exit implements IndependentCommand, NoArgumentCommand {
    @Override
    public String execute() {
        log.info("Exit from the system");
        log.warn(" command didn't save collection before exit");

        System.exit(0);

        return "";
    }

    @Override
    public String description() {
        return "exit : завершить программу (без сохранения в файл)";
    }

    @Override
    public String name() {
        return "exit";
    }
}
