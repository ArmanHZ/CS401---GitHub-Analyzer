package org.openjfx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class ArgumentParser {

    private String filePath;
    private String scriptsV2Path;
    private List<String> activeFilters;
    private boolean isWindows;

    ArgumentParser(String filePath, boolean isWindows) {
        scriptsV2Path = filePath + "\\scripts_v2";
        this.isWindows = isWindows;
        if (isWindows)
            this.filePath =  scriptsV2Path + "\\dummy.bat";
        else
            this.filePath = scriptsV2Path + "\\git_data_collector.sh";
    }

    void setActiveFilters(List<String> list) {
        this.activeFilters = list;
    }

    void parseInput(GitDataCollector argument) {
        switch (argument) {
            case LOG:
                createRunTimeArgument(GitDataCollector.LOG.toString());
                break;
            case LOG_NO_MERGES:
                createRunTimeArgument(GitDataCollector.LOG_NO_MERGES.toString());
                break;
            case LOG_DATE_RESTRICTED:
                createRunTimeArgument(GitDataCollector.LOG_DATE_RESTRICTED.toString());
                break;
            case LOG_DATE_RESTRICTED_NO_MERGES:
                createRunTimeArgument(GitDataCollector.LOG_DATE_RESTRICTED_NO_MERGES.toString());
                break;
            case FILES_CHANGED_TOGETHER:
                createRunTimeArgument(GitDataCollector.FILES_CHANGED_TOGETHER.toString());
                break;
            case UNIQUE_FILE_EXTENSIONS:
                createRunTimeArgument(GitDataCollector.UNIQUE_FILE_EXTENSIONS.toString());
                break;
            case FILTER_FINAL_DUMP:
                String parsedArguments = filterFinalDumpParser(GitDataCollector.FILTER_FINAL_DUMP.toString());
                createRunTimeArgument(parsedArguments);
                break;
        }
    }

    private void invokeGitDataCollectorShellScript(String parsedCommand) {
        try {
            File savePath = new File(scriptsV2Path);
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec(parsedCommand, null, savePath);
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String filterFinalDumpParser(String argument) {
        StringBuilder argumentsAsString = new StringBuilder(argument + " ");
        for (String filter: activeFilters)
            argumentsAsString.append(filter).append(" ");
        return argumentsAsString.toString().substring(0, argumentsAsString.toString().length() - 1);
    }

    private void createRunTimeArgument(String parsedArgument) {
        if (isWindows) {
            String parsedCommand = "cmd.exe /c start \"\"" + " \"" + filePath + "\" " + parsedArgument;
            System.out.println(parsedCommand);
            invokeGitDataCollectorShellScript(parsedCommand);
        } else {
            // TODO Not yet implemented for UNIX systems
            System.out.println("Not yet implemented.");
            System.exit(0);
        }
    }

}
