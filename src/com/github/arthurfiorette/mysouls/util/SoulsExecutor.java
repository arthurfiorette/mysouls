package com.github.arthurfiorette.mysouls.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.github.arthurfiorette.sinklibrary.core.BasePlugin;
import com.github.arthurfiorette.sinklibrary.executor.BukkitExecutor;
import com.github.arthurfiorette.sinklibrary.executor.TaskContext;
import com.github.arthurfiorette.sinklibrary.interfaces.BaseComponent;

public class SoulsExecutor implements BaseComponent, ExecutorService {

  private final ExecutorService service;
  private final BasePlugin plugin;

  public SoulsExecutor(final BasePlugin plugin) {
    this.plugin = plugin;
    this.service = BukkitExecutor.newFixedThreadPool(plugin, TaskContext.ASYNC,
        Runtime.getRuntime().availableProcessors());
  }

  @Override
  public BasePlugin getBasePlugin() {
    return this.plugin;
  }

  @Override
  public void execute(final Runnable command) {
    this.service.execute(command);
  }

  @Override
  public void shutdown() {
    this.service.shutdown();
  }

  @Override
  public List<Runnable> shutdownNow() {
    return this.service.shutdownNow();
  }

  @Override
  public boolean isShutdown() {
    return this.service.isShutdown();
  }

  @Override
  public boolean isTerminated() {
    return this.service.isTerminated();
  }

  @Override
  public boolean awaitTermination(final long timeout, final TimeUnit unit)
      throws InterruptedException {
    return this.service.awaitTermination(timeout, unit);
  }

  @Override
  public <T> Future<T> submit(final Callable<T> task) {
    return this.service.submit(task);
  }

  @Override
  public <T> Future<T> submit(final Runnable task, final T result) {
    return this.service.submit(task, result);
  }

  @Override
  public Future<?> submit(final Runnable task) {
    return this.service.submit(task);
  }

  @Override
  public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks)
      throws InterruptedException {
    return this.service.invokeAll(tasks);
  }

  @Override
  public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks,
      final long timeout, final TimeUnit unit) throws InterruptedException {
    return this.service.invokeAll(tasks, timeout, unit);
  }

  @Override
  public <T> T invokeAny(final Collection<? extends Callable<T>> tasks)
      throws InterruptedException, ExecutionException {
    return this.service.invokeAny(tasks);
  }

  @Override
  public <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout,
      final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    return this.service.invokeAny(tasks, timeout, unit);
  }

}
