package at.fhtw.swen2.tutorial.presentation;

import at.fhtw.swen2.tutorial.Swen2TemplateApplicationBoot;
import at.fhtw.swen2.tutorial.presentation.events.ApplicationStartupEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Swen2TemplateApplication extends Application {
    private ConfigurableApplicationContext applicationContext;

    private Stage stage;
    
    @Override
    public void start(Stage stage) throws Exception {
        log.debug("Starting TutorialApplication");
        this.stage = stage;
        applicationContext.publishEvent(new ApplicationStartupEvent(this, stage));
    }

    @Override
    public void init(){

        log.debug("Initializing Spring ApplicationContext");

        applicationContext = new SpringApplicationBuilder(Swen2TemplateApplicationBoot.class)
            .sources(Swen2TemplateApplicationBoot.class)
            .initializers(initializers())
            .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void stop() throws Exception {
        log.debug("Stopping TutorialApplication");
        
        applicationContext.close();
        Platform.exit();
    }     

    ApplicationContextInitializer<GenericApplicationContext> initializers() { 
        return ac -> {
            ac.registerBean(Parameters.class, this::getParameters);
            ac.registerBean(HostServices.class, this::getHostServices);
        };
    }

    public Stage getStage() {
        return stage;
    }
}
