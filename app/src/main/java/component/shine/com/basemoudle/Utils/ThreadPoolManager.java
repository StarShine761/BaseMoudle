package component.shine.com.basemoudle.Utils;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 管理线程池
 */
public class ThreadPoolManager {
	private ThreadPoolManager() {
	}

	private static ThreadPoolManager instance = new ThreadPoolManager();
	private ThreadPoolProxy longPool;
	private ThreadPoolProxy shortPool;

	public static ThreadPoolManager getInstance() {
		return instance;
	}

	/**
	 * 联网用线程池
	 * <p>联网比较耗时
	 * <br>国外大牛总结线程的最佳数量 = cpu的核数*2+1
	 * 
	 * 目前我们用的是4核cpu
	 * @return
	 */
	public synchronized ThreadPoolProxy createLongPool() {
		
		if (longPool == null) {
			longPool = new ThreadPoolProxy(9, 9, 5000L);
		}
		
		return longPool;
	}
	/**
	 * 操作本地文件时用该线程池
	 * @return
	 */
	public synchronized ThreadPoolProxy createShortPool() {
		
		if(shortPool==null){
			shortPool = new ThreadPoolProxy(5, 5, 5000L);
		}
		
		return shortPool;
	}

	public class ThreadPoolProxy {
		
		private ThreadPoolExecutor pool;
		private int corePoolSize;
		private int maximumPoolSize;
		private long time;

		public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long time) {
			this.corePoolSize = corePoolSize;
			this.maximumPoolSize = maximumPoolSize;
			this.time = time;
		}
		
		/**
		 * 执行任务
		 * @param runnable
		 */
		public void execute(Runnable runnable) {
			if (pool == null) {
				// 创建线程池
				pool = new ThreadPoolExecutor(corePoolSize, // 线程池里面管理多少个线程
						maximumPoolSize, // 如果排队满了, 额外的开的线程数
						time, // 如果线程池没有要执行的任务 存活多久
						TimeUnit.MILLISECONDS, // 时间的单位
						new LinkedBlockingQueue<Runnable>(10)); // 如果 线程池里管理的线程都已经用了,剩下的任务 临时存到LinkedBlockingQueue对象中 排队
			}
			pool.execute(runnable); // 调用线程池 执行异步任务
		}
		
		/**
		 * 取消任务
		 * @param runnable
		 */
		public void cancel(Runnable runnable) {
			if (pool != null && !pool.isShutdown() && !pool.isTerminated()) {
				pool.remove(runnable); // 取消异步任务
			}
		}
	}
}
