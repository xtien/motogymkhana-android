package eu.motogymkhana.competition.context;

import org.robolectric.RuntimeEnvironment;


public class TestContextProvider extends ContextProvider {

    public TestContextProvider() {
        context = RuntimeEnvironment.application;
    }
}
