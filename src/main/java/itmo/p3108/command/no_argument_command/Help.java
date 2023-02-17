package itmo.p3108.command.no_argument_command;

import itmo.p3108.command.type.IndependentCommand;
import itmo.p3108.command.type.NoArgumentCommand;
import lombok.extern.slf4j.Slf4j;

/**
 * put out information about commands
 */
@Slf4j
public class Help implements NoArgumentCommand, IndependentCommand {
    private static  Help help ;

    private Help() {
    }

    public static Help getInstance() {
       if (help ==null){
          help = new Help();
      log.info("Help initialized");
       }
        return help;
    }

    @Override
    public String execute() {
        log.info("Command help print information");

        return "    help : вывести справку по доступным командам\n" +
                "    info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "    show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "    add {element} : добавить новый элемент в коллекцию\n" +
                "    update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "    remove_by_id id : удалить элемент из коллекции по его id\n" +
                "    clear : очистить коллекцию\n" +
                "    save : сохранить коллекцию в файл\n" +
                "    execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "    exit : завершить программу (без сохранения в файл)\n" +
                "    add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                "    remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n" +
                "    reorder : отсортировать коллекцию в порядке, обратном нынешнему\n" +
                "    count_by_height height : вывести количество элементов, значение поля height которых равно заданному\n" +
                "    filter_starts_with_name name : вывести элементы, значение поля name которых начинается с заданной подстроки\n" +
                "    print_descending : вывести элементы коллекции в порядке убывания\n"+
                "    set_path path:устанавливает фаил ,в котором будут храниться элементы "
                ;
    }

    @Override
    public String name() {
        return "help";
    }
}
