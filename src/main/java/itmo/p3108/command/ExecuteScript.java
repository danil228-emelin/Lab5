package itmo.p3108.command;

import itmo.p3108.command.type.Command;
import itmo.p3108.exception.FileException;
import itmo.p3108.util.AnalyzerExecuteScript;
import itmo.p3108.util.FileWorker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Command execute script.
 * Execute script. Has one argument (path) for script.
 * If fail doesn't exist or program can't read from -error occur
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ExecuteScript implements Command {
    private String path;

    /**
     * set path ,call before execute method
     */
    public void setPath(String path) {
        Path test = Path.of(path);
        if (!Files.isReadable(test) || !Files.isWritable(test)) {
            log.info("ExecuteScript error during setting path:can't read and write  file");

            throw new FileException("error:can't read and write file");
        }
        this.path = path;
    }

    /**
     * @return the result of command
     */
    @Override
    public String execute() {

        try {
            String[] commands = FileWorker.read(path).split("\n");

            AnalyzerExecuteScript.analyze(commands);
            log.info(String.format("Script %s executed ", path));
            return String.format("Script executed", path);
        } catch (IOException e) {
            log.error("Execute Script:fail error ");
            System.err.println("Execute script:file error");
            return "";
        }
    }

    @Override
    public String description() {
        return "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме";
    }

    @Override
    public String name() {
        return "execute_script";
    }


}
