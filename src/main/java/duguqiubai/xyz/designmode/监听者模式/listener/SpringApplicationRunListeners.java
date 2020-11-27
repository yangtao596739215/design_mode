package duguqiubai.xyz.designmode.监听者模式.listener;

import org.apache.commons.logging.Log;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 集中管理所有发送事件的listenner，遍历调用它们的方法
 */

public class SpringApplicationRunListeners {

    private final Log log;

    private final List<SpringApplicationRunListener> listeners;

    SpringApplicationRunListeners(Log log, Collection<? extends SpringApplicationRunListener> listeners) {
        this.log = log;
        this.listeners = new ArrayList<>(listeners);
    }

    void starting() {
        for (org.springframework.boot.SpringApplicationRunListener listener : this.listeners) {
            listener.starting();
        }
    }

    void environmentPrepared(ConfigurableEnvironment environment) {
        for (org.springframework.boot.SpringApplicationRunListener listener : this.listeners) {
            listener.environmentPrepared(environment);
        }
    }

    void contextPrepared(ConfigurableApplicationContext context) {
        for (org.springframework.boot.SpringApplicationRunListener listener : this.listeners) {
            listener.contextPrepared(context);
        }
    }

    void contextLoaded(ConfigurableApplicationContext context) {
        for (org.springframework.boot.SpringApplicationRunListener listener : this.listeners) {
            listener.contextLoaded(context);
        }
    }

    void started(ConfigurableApplicationContext context) {
        for (org.springframework.boot.SpringApplicationRunListener listener : this.listeners) {
            listener.started(context);
        }
    }

    void running(ConfigurableApplicationContext context) {
        for (org.springframework.boot.SpringApplicationRunListener listener : this.listeners) {
            listener.running(context);
        }
    }

    void failed(ConfigurableApplicationContext context, Throwable exception) {
        for (org.springframework.boot.SpringApplicationRunListener listener : this.listeners) {
            callFailedListener(listener, context, exception);
        }
    }

    private void callFailedListener(SpringApplicationRunListener listener, ConfigurableApplicationContext context,
                                    Throwable exception) {
        try {
            listener.failed(context, exception);
        }
        catch (Throwable ex) {
            if (exception == null) {
                ReflectionUtils.rethrowRuntimeException(ex);
            }
            if (this.log.isDebugEnabled()) {
                this.log.error("Error handling failed", ex);
            }
            else {
                String message = ex.getMessage();
                message = (message != null) ? message : "no error message";
                this.log.warn("Error handling failed (" + message + ")");
            }
        }
    }
}
