package com.srv.sumit;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectDirectories;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectDirectories("src/integration-test/resources/features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.srv.sumit.glue,com.srv.sumit.steps")
@ConfigurationParameter(key = Constants.PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, json:target/cucumber-reports/cucumber.json, html:target/cucumber-reports/cucumber.html")
public class CucumberTestRunner {
}
