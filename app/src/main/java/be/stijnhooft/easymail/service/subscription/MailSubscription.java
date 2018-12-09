package be.stijnhooft.easymail.service.subscription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MailSubscription {

    private final MailSubscriptionConfiguration configuration;
    private final Collection<MailSubscriber> subscribers;
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture scheduledTask;

    protected MailSubscription(MailSubscriptionConfiguration configuration) {
        this.configuration = configuration;
        this.subscribers = new ArrayList<>();
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void subscribe(MailSubscriber listener) {
        subscribers.add(listener);
        if (subscribers.size() == 1) {
            start();
        }
    }

    public void unsubscribe(MailSubscriber listener) {
        subscribers.remove(listener);
        if (subscribers.isEmpty()) {
            stop();
        }
    }

    private void stop() {
        scheduledTask.cancel(true);
    }

    private void start() {
        scheduledTask = scheduler.scheduleAtFixedRate(new CheckNewMailTask(configuration, subscribers), 0, configuration.getIntervalInMilliseconds(), TimeUnit.MILLISECONDS);
    }

}
