package duguqiubai.xyz.designmode.监听者模式.multicaster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.AbstractApplicationEventMulticaster;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;
import org.springframework.util.ErrorHandler;

import java.util.concurrent.Executor;

/**
 * public void multicastEvent(final ApplicationEvent event, @Nullable ResolvableType eventType)
 *
 *  用来发布事件，传入事件和类型（通过类型解析哪些监听者需要处理）
 *
 *  通过DefaultListenerRetriever，来持有所有的listener
 *
 */

public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

    @Nullable
    private Executor taskExecutor;

    @Nullable
    private ErrorHandler errorHandler;


    /**
     * Create a new SimpleApplicationEventMulticaster.
     */
    public SimpleApplicationEventMulticaster() {
    }

    /**
     * Create a new SimpleApplicationEventMulticaster for the given BeanFactory.
     */
    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        setBeanFactory(beanFactory);
    }


    /**
     * Set a custom executor (typically a {@link org.springframework.core.task.TaskExecutor})
     * to invoke each listener with.
     * <p>Default is equivalent to {@link org.springframework.core.task.SyncTaskExecutor},
     * executing all listeners synchronously in the calling thread.
     * <p>Consider specifying an asynchronous task executor here to not block the
     * caller until all listeners have been executed. However, note that asynchronous
     * execution will not participate in the caller's thread context (class loader,
     * transaction association) unless the TaskExecutor explicitly supports this.
     * @see org.springframework.core.task.SyncTaskExecutor
     * @see org.springframework.core.task.SimpleAsyncTaskExecutor
     */
    public void setTaskExecutor(@Nullable Executor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    /**
     * Return the current task executor for this multicaster.
     */
    @Nullable
    protected Executor getTaskExecutor() {
        return this.taskExecutor;
    }

    /**
     * Set the {@link ErrorHandler} to invoke in case an exception is thrown
     * from a listener.
     * <p>Default is none, with a listener exception stopping the current
     * multicast and getting propagated to the publisher of the current event.
     * If a {@linkplain #setTaskExecutor task executor} is specified, each
     * individual listener exception will get propagated to the executor but
     * won't necessarily stop execution of other listeners.
     * <p>Consider setting an {@link ErrorHandler} implementation that catches
     * and logs exceptions (a la
     * {@link org.springframework.scheduling.support.TaskUtils#LOG_AND_SUPPRESS_ERROR_HANDLER})
     * or an implementation that logs exceptions while nevertheless propagating them
     * (e.g. {@link org.springframework.scheduling.support.TaskUtils#LOG_AND_PROPAGATE_ERROR_HANDLER}).
     * @since 4.1
     */
    public void setErrorHandler(@Nullable ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    /**
     * Return the current error handler for this multicaster.
     * @since 4.1
     */
    @Nullable
    protected ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }


    @Override
    public void multicastEvent(ApplicationEvent event) {
        multicastEvent(event, resolveDefaultEventType(event));
    }

    @Override
    public void multicastEvent(final ApplicationEvent event, @Nullable ResolvableType eventType) {
        ResolvableType type = (eventType != null ? eventType : resolveDefaultEventType(event));
        Executor executor = getTaskExecutor();
        for (ApplicationListener<?> listener : getApplicationListeners(event, type)) {
            if (executor != null) {
                executor.execute(() -> invokeListener(listener, event));
            }
            else {
                invokeListener(listener, event);
            }
        }
    }

    private ResolvableType resolveDefaultEventType(ApplicationEvent event) {
        return ResolvableType.forInstance(event);
    }

    /**
     * Invoke the given listener with the given event.
     * @param listener the ApplicationListener to invoke
     * @param event the current event to propagate
     * @since 4.1
     */
    protected void invokeListener(ApplicationListener<?> listener, ApplicationEvent event) {
        ErrorHandler errorHandler = getErrorHandler();
        if (errorHandler != null) {
            try {
                doInvokeListener(listener, event);
            }
            catch (Throwable err) {
                errorHandler.handleError(err);
            }
        }
        else {
            doInvokeListener(listener, event);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void doInvokeListener(ApplicationListener listener, ApplicationEvent event) {
        try {
            listener.onApplicationEvent(event);
        }
        catch (ClassCastException ex) {
            String msg = ex.getMessage();
            if (msg == null || matchesClassCastMessage(msg, event.getClass())) {
                // Possibly a lambda-defined listener which we could not resolve the generic event type for
                // -> let's suppress the exception and just log a debug message.
                Log logger = LogFactory.getLog(getClass());
                if (logger.isTraceEnabled()) {
                    logger.trace("Non-matching event type for listener: " + listener, ex);
                }
            }
            else {
                throw ex;
            }
        }
    }

    private boolean matchesClassCastMessage(String classCastMessage, Class<?> eventClass) {
        // On Java 8, the message starts with the class name: "java.lang.String cannot be cast..."
        if (classCastMessage.startsWith(eventClass.getName())) {
            return true;
        }
        // On Java 11, the message starts with "class ..." a.k.a. Class.toString()
        if (classCastMessage.startsWith(eventClass.toString())) {
            return true;
        }
        // On Java 9, the message used to contain the module name: "java.base/java.lang.String cannot be cast..."
        int moduleSeparatorIndex = classCastMessage.indexOf('/');
        if (moduleSeparatorIndex != -1 && classCastMessage.startsWith(eventClass.getName(), moduleSeparatorIndex + 1)) {
            return true;
        }
        // Assuming an unrelated class cast failure...
        return false;
    }
}
