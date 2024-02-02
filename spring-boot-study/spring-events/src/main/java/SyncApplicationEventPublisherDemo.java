import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhuxingyi
 * @date 2/2/2024 上午11:19
 */
public class SyncApplicationEventPublisherDemo {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.addApplicationListener(new MyEventListener());
        context.addApplicationListener(new MyErrorEventListener());
        context.refresh();


        ApplicationEventMulticaster multicaster = context.getBean(AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);
        if (multicaster instanceof SimpleApplicationEventMulticaster) {
            SimpleApplicationEventMulticaster eventMulticaster = (SimpleApplicationEventMulticaster) multicaster;
            ExecutorService executorService = Executors.newSingleThreadExecutor(new CustomizableThreadFactory("my-spring-event-thread-pool"));
            eventMulticaster.setTaskExecutor(executorService);
            eventMulticaster.addApplicationListener(new ApplicationListener<ContextClosedEvent>() {
                @Override
                public void onApplicationEvent(ContextClosedEvent event) {
                    if (!executorService.isShutdown()) {
                        executorService.shutdown();
                    }
                }
            });
            eventMulticaster.setErrorHandler(e -> {
                System.out.printf("当Spring事件异常时，原因是:%s\n", e.getMessage());
            });
        }


        multicaster.multicastEvent(new MyEvent("Hello World"));

        context.close();
    }

    static class MyEventListener implements ApplicationListener<MyEvent> {

        @Override
        public void onApplicationEvent(MyEvent event) {
            System.out.printf("[线程：%s]接收到事件:%s\n", Thread.currentThread().getName(), event);
        }
    }

    static class MyErrorEventListener implements ApplicationListener<MyEvent> {

        @Override
        public void onApplicationEvent(MyEvent event) {
            throw new IllegalArgumentException("参数错误");
        }
    }

    static class MyEvent extends ApplicationEvent {

        /**
         * Create a new {@code ApplicationEvent}.
         *
         * @param message 事件消息
         */
        public MyEvent(String message) {
            super(message);
        }

        @Override
        public String getSource() {
            return (String) super.getSource();
        }

        public String getMessage() {
            return getSource();
        }

    }


}
