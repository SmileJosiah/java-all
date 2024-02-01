import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author zhuxingyi
 * @date 1/2/2024 下午4:56
 */
@EnableAsync
public class ApplicationListenerDemo  implements ApplicationEventPublisherAware {

    public static void main(String[] args) {
//        GenericApplicationContext applicationContext = new GenericApplicationContext();


        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ApplicationListenerDemo.class);


        applicationContext.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                printf("接收到Spring事件:" + event);
            }
        });
        //基于Spring Bean
        applicationContext.register(MyApplicationListener.class);


        applicationContext.refresh();
        applicationContext.start();
        applicationContext.stop();
        applicationContext.close();


    }

    @EventListener
    @Async
    public void onApplicationEvent(ContextRefreshedEvent event){
        printf("@EventListener接收到Spring ContextRefreshedEvent");
    }
    @EventListener
    @Async
    public void onApplicationEvent(ContextStartedEvent event){
        printf("@EventListener接收到Spring ContextStartedEvent");
    }
    @EventListener
    @Async
    public void onApplicationEvent(ContextStoppedEvent event){
        printf("@EventListener接收到Spring ContextStoppedEvent");
    }

    @EventListener
    @Async
    public void onApplicationEvent(ContextClosedEvent event){
        printf("@EventListener接收到Spring ContextClosedEvent");
    }


    private static void printf(Object ojb){
        System.out.printf("[线程：%s],%s\n",Thread.currentThread().getName(),ojb);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        applicationEventPublisher.publishEvent(new ApplicationEvent("Hello!") {
        });
        applicationEventPublisher.publishEvent("Hello!");
    }


    static class MyApplicationListener implements ApplicationListener<ContextRefreshedEvent>{

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            System.out.println("MyApplicationListener接收到ContextRefreshedEvent事件"+event);
        }
    }

}
