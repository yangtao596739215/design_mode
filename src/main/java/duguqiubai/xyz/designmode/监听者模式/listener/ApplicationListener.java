package duguqiubai.xyz.designmode.监听者模式.listener;

import org.springframework.context.ApplicationEvent;

import java.util.EventListener;

/**
 * Interface to be implemented by application event listeners.
 *
 * <p>Based on the standard {@code java.util.EventListener} interface
 * for the Observer design pattern.
 *
 * <p>As of Spring 3.0, an {@code ApplicationListener} can generically declare
 * the event type that it is interested in. When registered with a Spring
 * {@code ApplicationContext}, events will be filtered accordingly, with the
 * listener getting invoked for matching event objects only.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @param <E> the specific {@code ApplicationEvent} subclass to listen to
 * @see org.springframework.context.ApplicationEvent
 * @see org.springframework.context.event.ApplicationEventMulticaster
 * @see org.springframework.context.event.EventListener
 *
 *
 *    真正用来处理事件的listenner
 */
@FunctionalInterface
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    /**
     * Handle an application event.
     * @param event the event to respond to
     *
     *              on
     *
     *              监听到这个事件发生以后，所要做的事情
     */
    void onApplicationEvent(E event);

}
