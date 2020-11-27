package duguqiubai.xyz.designmode.监听者模式.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.SpringApplicationEvent;

public class ApplicationStartingEvent extends SpringApplicationEvent {

    /**
     * Create a new {@link org.springframework.boot.context.event.ApplicationStartingEvent} instance.
     * @param application the current application
     * @param args the arguments the application is running with
     */
    public ApplicationStartingEvent(SpringApplication application, String[] args) {
        super(application, args);
    }

}
