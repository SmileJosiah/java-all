import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 层次性Spring事件传播
 * @author zhuxingyi
 * @date 1/2/2024 下午5:46
 */
public class HierarchicalSpringEventPropagateDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext parentContext = new AnnotationConfigApplicationContext();
        parentContext.setId("parent-context");

        AnnotationConfigApplicationContext currentContext = new AnnotationConfigApplicationContext();
        currentContext.setId("current-context");
        parentContext.register(MyListener.class);
        currentContext.setParent(parentContext);
        parentContext.refresh();
        currentContext.refresh();

    }

    static class MyListener implements ApplicationListener<ContextRefreshedEvent>{

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            System.out.printf("接收到Spring应用上下文 [ID:%s]的ContextRefreshedEvent\n",event.getApplicationContext().getId());
        }
    }
}
