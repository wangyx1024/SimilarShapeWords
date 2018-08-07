import com.wyx.util.LevenshteinDistanceCalculator;
import com.wyx.util.FileUtils;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class TestLevenshteinDistance {

	@Test
	public void test1() throws FileNotFoundException, UnsupportedEncodingException {
		List<String> sources = null; // 原单词数组
		List<String> targets = null; // 词库数组

		String sourcePath = "D:\\test\\word.list"; // 原单词文件路径
		String targetPath = "D:\\test\\word.list"; // 词库文件路径

//		String sourcePath = "D:\\test\\source.list"; // 原单词文件路径
//		String targetPath = "D:\\test\\source.list"; // 词库文件路径

		try {
			sources = FileUtils.readListFromFile(sourcePath); // 从文件中加载
			targets = FileUtils.readListFromFile(targetPath); // 从文件中加载
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String, Map<Integer, List<String>>> result = LevenshteinDistanceCalculator.getSimilarWords(sources, targets);

		PrintWriter writer = new PrintWriter("D:\\test\\the-file-name.txt", "UTF-8");

		for (String source : result.keySet()) {
			writer.print(source + ":");

			Map<Integer, List<String>> map = result.get(source);
			List<String> list = new LinkedList<>();
			map.values().forEach(list::addAll);

			writer.println(list);
		}

		writer.close();
	}

	/**
	 * 使用线程池优化
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@Test
	public void test2() throws FileNotFoundException, UnsupportedEncodingException, ExecutionException, InterruptedException {
		List<String> sources = null; // 原单词数组
		List<String> targets = null; // 词库数组

		String sourcePath = "D:\\test\\word.list"; // 原单词文件路径
		String targetPath = "D:\\test\\word.list"; // 词库文件路径

//		String sourcePath = "D:\\test\\source.list"; // 原单词文件路径
//		String targetPath = "D:\\test\\source.list"; // 词库文件路径

		try {
			sources = FileUtils.readListFromFile(sourcePath); // 从文件中加载
			targets = FileUtils.readListFromFile(targetPath); // 从文件中加载
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (sources == null || targets == null) {
			return;
		}

		int sourceLen = sources.size(); // 原单词数量
		int singleThreadWorkload = 2000; // 单个线程负责单词量
		double threadCount = Math.ceil(sourceLen / singleThreadWorkload); // 所需线程数量

		System.out.println("threadCount:" + threadCount);

		ExecutorService executor = Executors.newCachedThreadPool(); // 线程池
		List<Future<Map<String, Map<Integer, List<String>>>>> futures = new ArrayList<>(); // Callable线程返回结果集合


		for (int i = 0; i <= threadCount; i++) {
			// 根据线程数量分工
			int from = singleThreadWorkload * i; // 起始下标
			int to = Math.min(singleThreadWorkload * (i + 1), sourceLen); // 结束下标

			List<String> subSources = sources.subList(from, to); // 本线程要处理的单词
			System.out.println(i + ":" + subSources);

			final List<String> finalTargets = targets; // 改成final给匿名类用

			// 执行比对方法，将结果放到Future集合里
			Callable<Map<String, Map<Integer, List<String>>>> t = () -> LevenshteinDistanceCalculator.getSimilarWords(subSources, finalTargets);
			Future<Map<String, Map<Integer, List<String>>>> future = executor.submit(t);
			futures.add(future);
		}

		// 开始输出
		PrintWriter writer = new PrintWriter("D:\\test\\the-file-name.txt", "UTF-8");

		for (Future<Map<String, Map<Integer, List<String>>>> future : futures) {
			Map<String, Map<Integer, List<String>>> result = future.get();
			System.out.println(result);

			for (String source : result.keySet()) {
				writer.print(source + ":");
				writer.println(result.get(source));
			}
		}

		writer.close(); // 关闭资源
	}
}
