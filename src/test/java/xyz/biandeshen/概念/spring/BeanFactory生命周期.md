```text
The root interface for accessing a Spring bean container.
This is the basic client view of a bean container;
further interfaces such as {@link ListableBeanFactory} and
{@link org.springframework.beans.factory.config.ConfigurableBeanFactory}
are available for specific purposes.

用于访问Spring bean容器的根接口。
这是bean容器的基本客户端视图;
进一步的接口，如{@link ListableBeanFactory}和
{@link org.springframework.beans.factory.config.ConfigurableBeanFactory}
可用于特定目的。

```

```text

This interface is implemented by objects that hold a number of bean definitions,
each uniquely identified by a String name. Depending on the bean definition,
the factory will return either an independent instance of a contained object
(the Prototype design pattern), or a single shared instance (a superior
alternative to the Singleton design pattern, in which the instance is a
singleton in the scope of the factory). Which type of instance will be returned
depends on the bean factory configuration: the API is the same. Since Spring
2.0, further scopes are available depending on the concrete application
context (e.g. "request" and "session" scopes in a web environment).

这个接口是由包含许多bean定义的对象实现的，
每一个都由一个字符串名唯一标识。取决于bean的定义，
工厂将返回一个包含对象的独立实例
(原型设计模式)，或单个共享实例(上级实例)
可选的单例设计模式，其中实例为
单例在工厂的范围内)。将返回哪种类型的实例
取决于bean工厂的配置:API是相同的。因为春天
根据具体的应用程序，可以使用更多的作用域
上下文(如。“请求”和“会话”适用于web环境)。
```

```text
The point of this approach is that the BeanFactory is a central registry
of application components, and centralizes configuration of application
components (no more do individual objects need to read properties files,
for example). See chapters 4 and 11 of "Expert One-on-One J2EE Design and
Development" for a discussion of the benefits of this approach.

这种方法的要点是BeanFactory是一个中央注册表
的应用程序组件，并集中配置的应用程序
组件(单个对象不再需要读取属性文件，
例如)。请参阅“专家一对一J2EE设计和”的第4章和第11章
，以讨论这种方法的好处。

```

```text
Note that it is generally better to rely on Dependency Injection
("push" configuration) to configure application objects through setters
or constructors, rather than use any form of "pull" configuration like a
BeanFactory lookup. Spring's Dependency Injection functionality is
implemented using this BeanFactory interface and its subinterfaces.

注意，依赖依赖注入通常更好
(“推”配置)通过setter配置应用程序对象
或构造函数，而不是使用任何形式的“拉”配置
BeanFactory查找。Spring的依赖注入功能是
使用这个BeanFactory接口及其子接口实现。

```

```text
Normally a BeanFactory will load bean definitions stored in a configuration
source (such as an XML document), and use the {@code org.springframework.beans}
package to configure the beans. However, an implementation could simply return
Java objects it creates as necessary directly in Java code. There are no
constraints on how the definitions could be stored: LDAP, RDBMS, XML,
properties file, etc. Implementations are encouraged to support references
amongst beans (Dependency Injection).

通常，BeanFactory将加载存储在配置中的bean定义
源代码(例如XML文档)，并使用{@code org.springframework.bean}
包来配置bean。然而，实现可以简单地返回
它直接在Java代码中创建必要的Java对象。没有
定义如何存储的约束:LDAP、RDBMS、XML、
属性文件等。鼓励实现支持引用
在bean之间(依赖注入)。
```

```text
In contrast to the methods in {@link ListableBeanFactory}, all of the
operations in this interface will also check parent factories if this is a
{@link HierarchicalBeanFactory}. If a bean is not found in this factory instance,
the immediate parent factory will be asked. Beans in this factory instance
are supposed to override beans of the same name in any parent factory.

与{@link ListableBeanFactory}中的方法相比，所有的
此接口中的操作还将检查父工厂(如果这是一个)
{@link HierarchicalBeanFactory}。如果在这个工厂实例中没有找到bean，
将询问直接的父工厂。工厂实例中的bean应该在任何父工厂中重写同名的bean。
```

```text
Bean factory implementations should support the standard bean lifecycle interfaces
as far as possible. The full set of initialization methods and their standard order is:

BeanNameAware's {@code setBeanName}
BeanClassLoaderAware's {@code setBeanClassLoader}
BeanFactoryAware's {@code setBeanFactory}
EnvironmentAware's {@code setEnvironment}
EmbeddedValueResolverAware's {@code setEmbeddedValueResolver}
ResourceLoaderAware's {@code setResourceLoader}
(only applicable when running in an application context)
ApplicationEventPublisherAware's {@code setApplicationEventPublisher}
(only applicable when running in an application context)
MessageSourceAware's {@code setMessageSource}
(only applicable when running in an application context)
ApplicationContextAware's {@code setApplicationContext}
(only applicable when running in an application context)
ServletContextAware's {@code setServletContext}
(only applicable when running in a web application context)
{@code postProcessBeforeInitialization} methods of BeanPostProcessors
InitializingBean's {@code afterPropertiesSet}
a custom init-method definition
{@code postProcessAfterInitialization} methods of BeanPostProcessors


Bean工厂实现应该尽可能地支持标准的Bean生命周期接口。
完整的初始化方法及其标准顺序是:
BeanNameAware的{@code setBeanName}
BeanClassLoaderAware的{@code setBeanClassLoader}
BeanFactoryAware的{@code setBeanFactory}
EnvironmentAware的{@code setEnvironment}
EmbeddedValueResolverAware的{@code setEmbeddedValueResolver}
ResourceLoaderAware的{@code setResourceLoader}
(只适用于在应用程序上下文中运行时)
ApplicationEventPublisherAware的{@code setApplicationEventPublisher}
(只适用于在应用程序上下文中运行时)
MessageSourceAware的{@code setMessageSource}
(只适用于在应用程序上下文中运行时)
ApplicationContextAware的{@code setApplicationContext}
(只适用于在应用程序上下文中运行时)
ServletContextAware的{@code setServletContext}
(只适用于在web应用程序上下文中运行时)
beanpostprocessor的方法
InitializingBean的{@code afterPropertiesSet}
一个自定义的init-method定义
beanpostprocessor的方法

```

```text
On shutdown of a bean factory, the following lifecycle methods apply:

{@code postProcessBeforeDestruction} methods of DestructionAwareBeanPostProcessors
DisposableBean's {@code destroy}
a custom destroy-method definition

在关闭bean工厂时，应用以下生命周期方法:
destructionawarebeanpostprocessor的{@code postProcessBeforeDestruction}方法
DisposableBean的{@code摧毁}
自定义销毁方法定义

```


