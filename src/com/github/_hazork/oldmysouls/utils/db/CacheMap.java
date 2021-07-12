package com.github._hazork.oldmysouls.utils.db;

import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class CacheMap<K, V> extends ConcurrentHashMap<K, V> implements Runnable {

  private static final long serialVersionUID = -8795424293264578508L;

  private final long seconds;
  private final long delay;
  private final BiConsumer<K, V> callback;

  private final ConcurrentHashMap<K, LocalTime> time_map = new ConcurrentHashMap<>();
  private Thread worker = new Thread(this, this.getClass().getSimpleName());

  public CacheMap(final long delay, final long seconds) {
    this(delay, seconds, null);
  }

  public CacheMap(final long delay, final long seconds, final BiConsumer<K, V> callback) {
    this.delay = delay;
    this.seconds = seconds;
    this.callback = (callback == null) ? ((K, V) -> {}) : callback;
    this.tryRun();
  }

  @Override
  public V put(final K key, final V value) {
    this.time_map.put(key, LocalTime.now());
    final V obj = super.put(key, value);
    this.tryRun();
    return obj;
  }

  @Override
  public V remove(final Object key) {
    this.time_map.remove(key);
    return super.remove(key);
  }

  @Override
  public void run() {
    while (!this.isEmpty()) {
      for (final Entry<K, LocalTime> entry : this.time_map.entrySet()) {
        if (LocalTime.now().minusSeconds(this.seconds).isAfter(entry.getValue())) {
          this.removeSafety(entry.getKey());
        }
        try {
          Thread.sleep(this.delay);
        } catch (final InterruptedException e) {
          e.printStackTrace();
          this.close();
        }
      }
    }
  }

  public void removeSafety(final K key) {
    if (this.callback != null) {
      this.callback.accept(key, this.get(key));
    }
    this.remove(key);
  }

  public void close() {
    this.keySet().stream().forEach(this::removeSafety);
  }

  @Override
  public void clear() {
    super.clear();
    this.time_map.clear();
  }

  private void tryRun() {
    if (!this.worker.isAlive()) {
      this.worker = new Thread(this);
      this.worker.start();
    }
  }
}
