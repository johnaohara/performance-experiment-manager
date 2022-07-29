package io.hyperfoil.tools.experimentManager.parser;

import io.hyperfoil.tools.experimentManager.api.Plugin;
import io.hyperfoil.tools.experimentManager.pipeline.ExperimentPipeline;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class PipelineFactory {

    @Inject
    @Any
    Instance<Plugin<?>> pluginBeans;

    Map<String, Plugin> plugins = new HashMap<>();

    private Map<String, ConfigurationParser.SectionParser> mappings = new HashMap<>();


    public void startup(@Observes StartupEvent startupEvent) {

        //special case mapping for root element
        mappings.put("root", new ConfigurationParser.SectionParser<Map<String, List<?>>>(
                        (section, builder) -> {
                            if (section != null && !section.isEmpty()
                                    && section.size() == 1
                                    && section.values().stream().findFirst().get() instanceof List<?>
                            ) {
                                return null;
                            }

                            List<String> errors = new ArrayList<>();
                            if (section == null) {
                                errors.add("No pipline defined");
                            } else {
                                if (section.isEmpty()) {
                                    errors.add("No pipline defined");
                                }
                                if (!(section.values() instanceof List<?>)) {
                                    errors.add("Expecting a List of pipeline steps");
                                }
                            }
                            return errors;
                        }, //validation
                        (config, section, builder) -> {
                            //TODO: callback to set config properties - this needs to be decoupled
                            config.experimentName(section.keySet().stream().findFirst().get()); //get pipeline name
                            List<Map<String, Object>> steps = (List<Map<String, Object>>) section.values().stream().findFirst().get();
                            for (Map<String, Object> step : steps) { //can not use streams here, otherwise ConfigParserException is not propagated back from lambda :(
                                for (Map.Entry<String, Object> entry : step.entrySet()) {
                                    builder.build(config, entry.getKey(), entry.getValue());
                                }
                            }
                        }
                )
        );


        pluginBeans.forEach(plugin -> {
            plugins.put(plugin.getName(), plugin);
            mappings.put(plugin.getName(), plugin.getParser());
        });


//
//        mappings.put("hpo", new Parser.SectionParser<Map<String, Object>>(
//                        (section, builder) -> containsKeys(section, "name")
//                        , (sectionMap, builder) -> {
//                    for (Map.Entry<String, Object> entry : sectionMap.entrySet()) {
//                        builder.add("hpo~".concat(entry.getKey()), entry.getValue());
//                    }
//                }
//                )
//        );
//
//        mappings.put("hpo~name", new Parser.SectionParser<String>(
//                (section, builder) -> true
//                , (expName, builder) -> builder.setName(expName))
//        );
//
//        mappings.put("hpo~experiment-manager", new Parser.SectionParser<Map<String, Object>>(
//                        (experimentManagerMap, builder) -> numElements(experimentManagerMap, 1)
//                        , (experimentManagerMap, builder) -> {
//
//                    Set keys = experimentManagerMap.keySet();
//                    String manager = (String) keys.stream().findFirst().get();
//
//                    runtimeProducer.getRuntime(manager).parseConfig(builder, experimentManagerMap.get(manager));
//
//                })
//        );
//
//
//        mappings.put("qDup~params", new Parser.SectionParser<Map<String, Object>>(
//                (paramsMap, builder) -> numElements(paramsMap, 2) && containsKeys(paramsMap, "name", "tuneable")
//                , (paramsMap, builder) -> builder.addqDupParamMapping(paramsMap.get("name").toString(), paramsMap.get("tuneable").toString()))
//        );
//
//
//        mappings.put("hpo~horreum", new Parser.SectionParser<Map<String, Object>>(
//                        (horreumMap, builder) -> (horreumMap.keySet().size() >= 2) && containsKeys(horreumMap, "jobID", "auth")
//                        , (horreumMap, builder) -> {
//                    String jobID = horreumMap.get("jobID").toString();
//                    builder.addHorreum(
//                            jobID
//                    );
//                    builder.setHorreumJobID(jobID);
//                })
//        );
//
//        mappings.put("hpo~hpo_search_space", new Parser.SectionParser<Map<String, Object>>(
//                        (hpoMap, builder) -> numElements(hpoMap, 8) && containsKeys(hpoMap, "total_trials", "parallel_trials", "value_type", "hpo_algo_impl", "objective_function", "tuneables", "slo_class", "direction")
//                        , (hpoMap, builder) -> {
//                    builder.addHpoSearchSpace(builder.config.getExperimentName(),
//                            builder.config.getHorreumJobID(),
//                            Integer.parseInt(hpoMap.get("total_trials").toString()),
//                            Integer.parseInt(hpoMap.get("parallel_trials").toString()),
//                            hpoMap.get("value_type").toString(),
//                            hpoMap.get("hpo_algo_impl").toString(),
//                            hpoMap.get("objective_function").toString(),
//                            hpoMap.get("slo_class").toString(),
//                            hpoMap.get("direction").toString()
//                    );
//
//                    builder.add("hpo~tunableList", hpoMap.get("tuneables"));
//
//                }
//                )
//        );
//
//        mappings.put("hpo~tunableList", new Parser.SectionParser<List<Map<String, String>>>(
//                        (tunablesList, builder) -> true
//                        , (tunablesList, builder) -> {
//                    for (Map<String, String> tunableMapping : tunablesList) {
//                        builder.add("hpo~tunable", tunableMapping);
//                    }
//                }
//                )
//        );
//
//        mappings.put("hpo~tunable", new Parser.SectionParser<Map<String, Object>>(
//                        (tunable, builder) -> numElements(tunable, 5) && containsKeys(tunable, "value_type", "lower_bound", "name", "upper_bound", "step")
//                        , (tunable, builder) -> {
//                    builder.addTuneable(
//                            tunable.get("value_type").toString(),
//                            tunable.get("name").toString(),
//                            Double.parseDouble(tunable.get("lower_bound").toString()),
//                            Double.parseDouble(tunable.get("upper_bound").toString()),
//                            Double.parseDouble(tunable.get("step").toString())
//                    );
//                })
//        );

    }

    public static boolean numElements(Map map, Integer numElements) {
        return map.keySet().size() == numElements;
    }

    public static boolean containsKeys(Map map, String... elements) {
        for (String element : elements) {
            if (!map.containsKey(element)) {
                return false;
            }
        }
        return true;
    }


    public static PipelineFactory instance() {
        return new PipelineFactory();
    }

    void build(Object value, ExperimentPipeline config) throws ConfigParserException {
        this.build(config, "root", value);
    }

    void build(ExperimentPipeline config, String mappingName, Object value) throws ConfigParserException {
        if (mappings.containsKey(mappingName) && mappings.get(mappingName) != null) {
            ConfigurationParser.SectionParser sectionParser = mappings.get(mappingName);
            var errors = sectionParser.validator.validate(value, this);
            if (errors == null) {
                sectionParser.procesor.process(config, value, this);
            } else {
                throw new ConfigParserException("Could not validate mapping: " + mappingName, errors);
            }
        } else {
            throw new ConfigParserException("Unknown mapping value: " + mappingName);
        }
    }

/*
    PipelineBuilder setName(String name) {
        this.config.setExperimentName(name);
        return this;
    }

    PipelineBuilder setHorreumJobID(String jobID) {
        this.config.setHorreumJobID(jobID);
        return this;
    }

    public PipelineBuilder addqDupJob(String targetHost, String user, String scriptUrl ) {
        this.config.defineQdupJob(targetHost, user, scriptUrl);
        return this;
    }

    public PipelineBuilder addJenkinsJob(String job, String job_url) {
        this.config.defineJenkinsJob(job, job_url);
        return this;
    }

    private void addHorreum( String jobID) {
        this.config.defineHorreum(jobID);
    }

    private void addHpoSearchSpace(String name, String test_id, Integer total_trials, Integer parallel_trials, String value_type,
                                   String hpo_algo_impl, String objective_function, String slo_class, String direction) {
        this.config.defineHpoExperiment(name, test_id, total_trials, parallel_trials, value_type,
                hpo_algo_impl, objective_function, slo_class, direction);
    }

    private void addTuneable(String value_type, String name, Double lower_bound,
                             Double upper_bound, Double step) {
        ExperimentTunable tuneable = new ExperimentTunable(name, value_type, lower_bound, upper_bound, step);

        this.config.getHpoExperiment().addTuneable(tuneable);
    }


    public void addqDupParamMapping(String name, String tuneable) {
        this.config.getqDupJob().addParam(name, tuneable);
    }


    public void addJenkinsParamMapping(String name, String tuneable) {
        this.config.getJenkinsJob().addParam(name, tuneable);
    }
*/

}
