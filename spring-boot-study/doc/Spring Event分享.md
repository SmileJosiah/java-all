# Spring Event

## Java中的标准事件API

## 面向接口的事件/监听器设计模式

* 事件/监听器场景举例

   Java技术规范 | 事件接口 |监听器接口
  -----| ---- | ---- 
   Java Beans | java.beans.PropertyChangeEvent |java.beans.PropertyChangeListener
   Java AWT | java.awt.event.MouseEvent |java.awt.event.MouseListener
   Java Swing | javax.swing.event.MenuEvent |javax.swing.event.MenuListener
   Java Preference | java.util.prefs.PreferenceChangeEvent |java.util.prefs.PreferenceChangeListener
  

## 面向注解的事件/监听器模式

* 事件/监听器场景举例

  | Java技术规范 | 事件接口                       | 监听器接口                           |
  | ------------ | ------------------------------ | ------------------------------------ |
  | Servlet 3.0+ |                                | javax.servlet.annotation.WebListener |
  | JAP 1.0+     |                                |                                      |
  | Java Common  | javax.annotation.PostConstruct |                                      |
  |              |                                |                                      |

## Spring 标准事件 - ApplicationEvent

* Java标准事件 `java.util.EventObject`扩展
  * 扩展特性：事件发生时间戳
* Spring 应用上下文`ApplicationEvent`扩展 -`ApplicationContextEvent`
  * Spring应用上下问（ApplicationContext）作为事件源
  * 具体事件
    * org.springframework.context.event.ContextClosedEvent
    * org.springframework.context.event.ContextRefreshedEvent
    * org.springframework.context.event.ContextStartedEvent
    * org.springframework.context.event.ContextStoppedEvent

## 基于接口的Spring事件监听器

```JAVA
public class ApplicationListenerDemo {

    public static void main(String[] args) {
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        applicationContext.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                System.out.println("接收到Spring事件:" + event);
            }
        });

        applicationContext.refresh();
        applicationContext.start();
        applicationContext.stop();
        applicationContext.close();
    }
}

```

## 基于注解的Spring事件监听器

Spring注解 `org.springframework.context.event.EventListener`

| 特性                 | 说明                                     |
| -------------------- | ---------------------------------------- |
| 设计特点             | 支持多ApplicationEvent类型，无需接口约束 |
| 注解目标             | 方法                                     |
| 是否支持异步执行     | 支持                                     |
| 是否支持泛型类型事件 | 支持                                     |
| 是否支持顺序控制     | 支持，配合@Order注解控制                 |

```JAVA
@EnableAsync
public class ApplicationListenerDemo {

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
}
```

## 注册Spring ApplicationListener

* ApplicationListener 作为Spring Bean注册
* 通过ConfigurableAplicationContext API注册

## Spring事件发布器

* 方法一 通过ApplicationEventPublisher发布Spring事件
  * 获取ApplicationEventPublisher （事件发布器）
    * 依赖注入
* 方法二：通过ApplicationEventMulticaster 发布Spring事件
  * 获取ApplicationEventMulticaster （事件广播）
    * 依赖注入
    * 依赖查找

## Spring 层次性上下文事件传播

* 发生说明

  当Spring应用出现多层次Spring应用上下文（ApplicationContext）时，如SpringMVC，Spring Boot或SpringCloud场景下，由子ApplicationContext发起Spring事件可能会传递给Parent ApplicationContext（直到Root）的过程

* 如何避免

  * 定位Spring事件源（ApplicationContext）进行过滤处理
