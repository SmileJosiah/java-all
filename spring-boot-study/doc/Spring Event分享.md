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

## Spring内建事件

* ApplicationContextEvent派生事件
  * ContextRefreshedEvent：Spring应用上下文就绪事件
  * ContextStartedEvent：Spring应用上下文启动事件
  * ContextStopedEvent：Spring应用上下文停止事件
  * ContextClosedEvent：Spring应用上下文关闭事件

## Spring Payload事件

* Spring payload事件 - org.springframework.context.PayloadApplicationEvent
  * 使用场景：简化Spring事件发送，关注事件源主体
  * 发送方法
    * ApplicationEventPublisher#publishEvent(java.lang.Object)

## Spring自定义事件

* 扩展 org.springframework.context.ApplicationEvent
* 实现 org.springframework.context.ApplicationListener
* 注册 org.springframework.context.ApplicationListener

## 依赖出入 ApplicationEventPublisher

* 通过ApplicationEventPublisherAware接口
* 通过@Autowried ApplicationEventPublisher

## 同步和异步Spring 事件广播

* 基于实现类 - org.framework.context.event.SimpleApplicationEventMulticaster
  * 模式切换： setTaskExecutor
    * 默认模式：同步
    * 异步模式：如java.util.concurrent.ThreadPoolExecutor
  * 设计缺陷：非基于接口契约编程

## Spring 事件异常处理

* Spring 3.0 错误处理接口 -org.springframework.util.ErrorHanlder

  ```JAVA
  
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
  ```

  

## Spring 事件/监听器实现原理

* 核心类 org.springframework.context.event.SimpleApplicationEventMulticaster
  * 设计模式：观察者模式扩展
    * 被观察者- 
  * 执行模式：同步/异步
  * 异常处理： org.springframework.util.ErrorHandler
  * 泛型处理：org.springframework.core.ResolvableType

## Spring Boot 和Spring Cloud中的事件

* Spring Boot事件

  | 事件类型                 | 发生时机                  |
  | ------------------------ | ------------------------- |
  | ApplicationStartingEvent | 当Spring Boot应用已启动时 |
  | ApplicationStaredEvent   |                           |
  |                          |                           |

* Spring Cloud事件

  | 事件类型              | 发生时机                               |
  | --------------------- | -------------------------------------- |
  | EnviromentChangeEvent | 当Enviroment示例配置属性发生变化的时候 |

  

## Spring 同步和异步事件处理的使用场景

 @EventLisenter的工作原理
