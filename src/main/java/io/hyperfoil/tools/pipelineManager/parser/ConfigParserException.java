package io.hyperfoil.tools.pipelineManager.parser;

import java.util.List;

public class ConfigParserException extends Exception{

    private final List<String> errors;

    public ConfigParserException(String message) {
        super(message);
        this.errors = null;
    }

    public ConfigParserException(String message, List<String> errors){
        super(message);
        this.errors = errors;
    }

    public ConfigParserException(String message, Throwable cause) {
        super(message, cause);
        this.errors = null;
    }

    public String getPrettyMsg() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getMessage());
        stringBuilder.append("\n");
        errors.forEach(error -> stringBuilder.append(error).append("\n"));
        return stringBuilder.toString();
    }

    public List<String> getErrors(){
        return this.errors;
    }
}
