import LevenshteinDistance.Calculator;
import Utils.FileUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestLevenshteinDistance {

	@Test
	public void test1() {
		List<String> sources = null; // 原单词数组
		List<String> targets = null; // 词库数组

		String sourcePath = "D:\\test\\source.list"; // 原单词文件路径
		String targetPath = "D:\\test\\word.list"; // 词库文件路径

		try {
			sources = FileUtils.readListFromFile(sourcePath); // 从文件中加载
			targets = FileUtils.readListFromFile(targetPath); // 从文件中加载
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String, Set<String>> result = Calculator.getSimilarWords(sources, targets);
		System.out.println(result);

		sources = null;
		targets = null;
	}
}
