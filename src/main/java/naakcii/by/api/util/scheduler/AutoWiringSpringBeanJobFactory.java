package naakcii.by.api.util.scheduler;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

public final class AutoWiringSpringBeanJobFactory extends SpringBeanJobFactory implements
        ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(final ApplicationContext context) {
        this.context = context;
    }

    @Override
    protected Object createJobInstance(final TriggerFiredBundle bundle) {
        return context.getBean(bundle.getJobDetail().getJobClass());
    }
}