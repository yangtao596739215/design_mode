package duguqiubai.xyz.designmode.监听者模式.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

public class ApplicationStartedEvent extends SpringApplicationEvent {

    private final ConfigurableApplicationContext context;

    /**
     * Create a new {@link org.springframework.boot.context.event.ApplicationStartedEvent} instance.
     * @param application the current application
     * @param args the arguments the application is running with
     * @param context the context that was being created
     */
    public ApplicationStartedEvent(SpringApplication application, String[] args,
                                   ConfigurableApplicationContext context) {
        super(application, args);
        this.context = context;
    }

    /**
     * Return the application context.
     * @return the context
     */
    public ConfigurableApplicationContext getApplicationContext() {
        return this.context;
    }
}
