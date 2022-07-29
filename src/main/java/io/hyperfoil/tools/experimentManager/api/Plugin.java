package io.hyperfoil.tools.experimentManager.api;

import io.hyperfoil.tools.experimentManager.parser.ConfigurationParser;

public interface Plugin<P>{

    String getName();

    Type getType(); //TODO:: do we really need this?

    ConfigurationParser.SectionParser<P> getParser();

    enum Type {
        RECEIVER,
        PROCESSOR,
        RUNNER
    }

}
