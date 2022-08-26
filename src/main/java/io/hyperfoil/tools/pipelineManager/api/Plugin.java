package io.hyperfoil.tools.pipelineManager.api;

import io.hyperfoil.tools.pipelineManager.parser.ConfigurationParser;

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
