import com.icecool.util.LevenshteinDistanceCalculator;
import com.icecool.util.FileUtils;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

//			List<String> list = new LinkedList<>();
//
//			Map<Integer, List<String>> map = result.get(source);
//			map.values().forEach(list::addAll);
//
//			writer.println(list);

			Map<Integer, List<String>> map = result.get(source);
			List<String> list = new LinkedList<>();
			map.values().forEach(list::addAll);

			writer.println(list);
		}

		writer.close();

		sources = null;
		targets = null;
	}
}
