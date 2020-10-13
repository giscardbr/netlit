package br.com.netlit.library.infra.messaging;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
@Configuration
public class AsynchronousEventsConfig {

  private final BeanFactory beanFactory;

  public AsynchronousEventsConfig(final BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  @Bean(name = "applicationEventMulticaster")
  public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
    final ExecutorService executor = Executors.newFixedThreadPool(3, new ThreadFactoryBuilder()
        .setNameFormat("asynchronous-events-%d")
        .setDaemon(true)
        .setUncaughtExceptionHandler((t, e) -> log.error("Thread " + t.getName() + " execution failed", e))
        .build());

    final SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
    eventMulticaster.setTaskExecutor(new LazyTraceExecutor(this.beanFactory, executor));

    return eventMulticaster;
  }
}
