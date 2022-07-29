package io.hyperfoil.tools.experimentManager.pipeline.util;

import io.hyperfoil.tools.experimentManager.api.BasePipelineExecutable;
import io.hyperfoil.tools.experimentManager.api.PipelineContext;
import io.hyperfoil.tools.experimentManager.api.Plugin;
import io.hyperfoil.tools.experimentManager.parser.ConfigurationParser;
import org.jboss.logging.Logger;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.hyperfoil.tools.experimentManager.parser.PipelineFactory.containsKeys;
import static io.hyperfoil.tools.experimentManager.parser.PipelineFactory.numElements;

@Singleton
public class ContextSetter implements Plugin<Map<String, Object>> {

    @Override
    public String getName() {
        return "context-setter";
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public ConfigurationParser.SectionParser getParser() {
        return new ConfigurationParser.SectionParser<Map<String, Object>>(
                (section, builder) -> {
                    if (section != null && !section.isEmpty() && numElements(section, 2) && containsKeys(section, "variable", "initial-value")) {
                        return null;
                    }
                    List<String> errors = new ArrayList<>();
                    if (section == null) {
                        errors.add("Usage:");
                        errors.add("context-setter:");
                        errors.add("  variable: my-context-variable");
                        errors.add("  initial-value: 10");
                    } else {
                        if (!numElements(section, 2)) {
                            errors.add("Expecting 2 elements for: context-setter");
                        }
                        if (!containsKeys(section, "variable", "initial-value")) {
                            errors.add("Usage:");
                            errors.add("context-setter:");
                            errors.add("  variable: my-context-variable");
                            errors.add("  initial-value: 10");
                        }
                    }
                    return errors;
                }, //validation

                (config, section, builder) -> {
                    config.addPipelineExecutable(
                            TestContextIncrementerExecutable.instance()
                                    .variable((String) section.get("variable"))
                                    .initialValue((Integer) section.get("initial-value"))
                    );
                });
    }

    public static class TestContextIncrementerExecutable extends BasePipelineExecutable {
        Logger logger = Logger.getLogger(TestContextIncrementerExecutable.class);

        private String contextVariableName;
        private Integer initialValue;

        private TestContextIncrementerExecutable() {

        }

        public TestContextIncrementerExecutable variable(String name) {
            this.contextVariableName = name;
            return this;
        }

        public TestContextIncrementerExecutable initialValue(Integer value) {
            this.initialValue = value;
            return this;
        }

        public static TestContextIncrementerExecutable instance() {
            return new TestContextIncrementerExecutable();
        }

        @Override
        public void run(PipelineContext context) {
            context.<Integer>setObject(this.contextVariableName, initialValue);
        }

    }


}
