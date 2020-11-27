package duguqiubai.xyz.designmode.监听者模式.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.core.env.ConfigurableEnvironment;

public class ApplicationEnvironmentPreparedEvent extends SpringApplicationEvent {

    private final ConfigurableEnvironment environment;
    /**
     * Create a new {@link org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent} instance.
     * @param application the current application
     * @param args the arguments the application is running with
     * @param environment the environment that was just created
     */
    public ApplicationEnvironmentPreparedEvent(SpringApplication application, String[] args,
                                               ConfigurableEnvironment environment) {
        super(application, args);
        this.environment = environment;
    }
    /**
     * Return the environment.
     * @return the environment
     */
    public ConfigurableEnvironment getEnvironment() {
        return this.environment;
    }

}
