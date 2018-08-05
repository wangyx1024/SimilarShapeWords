package LevenshteinDistance;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Calculator {

	/**
	 * 阈值
	 */
	private static final int THRESHOLD = 2;

	/**
	 * 计算Levenshtein Distance主方法
	 *
	 * @param s 原单词
	 * @param t 目标单词
	 * @return Levenshtein Distance
	 */
	private static int getLevenshteinDistance(String s, String t) {
		int matrix[][]; // matrix
		int n; // length of s
		int m; // length of t
		int i; // iterates through s
		int j; // iterates through t
		char s_i; // ith character of s
		char t_j; // jth character of t
		int cost; // cost

		// Step 1
		n = s.length();
		m = t.length();
		if (n == 0) {
			return m;
		}

		if (m == 0) {
			return n;
		}

		matrix = new int[n + 1][m + 1];

		// Step 2
		for (i = 0; i <= n; i++) {
			matrix[i][0] = i;
		}

		for (j = 0; j <= m; j++) {
			matrix[0][j] = j;
		}

		// Step 3
		for (i = 1; i <= n; i++) {
			s_i = s.charAt(i - 1);

			// Step 4
			for (j = 1; j <= m; j++) {
				t_j = t.charAt(j - 1);

				// Step 5
				if (s_i == t_j) {
					cost = 0;
				} else {
					cost = 1;
				}

				// Step 6
				matrix[i][j] = getMinimum(matrix[i - 1][j] + 1, matrix[i][j - 1] + 1, matrix[i - 1][j - 1] + cost);
			}
		}

		// Step 7
		return matrix[n][m];
	}

	/**
	 * 获取三者中的最小值，主计算方法使用
	 *
	 * @param a 数字a
	 * @param b 数字b
	 * @param c 数字c
	 * @return 三者中的最小值
	 */
	private static int getMinimum(int a, int b, int c) {
		int min = a;

		if (b < min) {
			min = b;
		}

		if (c < min) {
			min = c;
		}

		return min;
	}

	/**
	 * 传入两个数组，sources代表原单词，targets代表词库
	 *
	 * @param sources 原单词数组
	 * @param targets 词库数组
	 * @return 对比结果map，key是原单词，value是词库中原单词的形近词set（不包括原单词本身）
	 */
	public static Map<String, Set<String>> getSimilarWords(List<String> sources, List<String> targets) {
		// 如果原单词数组或者词库数组为空则返回空map
		if (CollectionUtils.isEmpty(sources) || CollectionUtils.isEmpty(targets)) {
			return new HashMap<>();
		}

		int sourceWordCount = sources.size(); // 原单词数组数量
		Map<String, Set<String>> result = new HashMap<>(sourceWordCount); // 形近字结果

		// 遍历原单词数组
		for (String source : sources) {
			// 原单词非空校验
			if (StringUtils.isBlank(source)) {
				continue;
			}

			int sLen = source.length(); // source的长度

			Set<String> similarWords = new HashSet<>(); // 词库中和source相似的单词

			// 遍历词库
			for (String target : targets) {
				// 词库单词非空校验
				if (StringUtils.isBlank(target)) {
					continue;
				}

				int tLen = target.length(); // target的长度
				// 先对比长度，长度不符合就直接pass了
				if (Math.abs(sLen - tLen) > THRESHOLD) {
					continue;
				}

				int distance = getLevenshteinDistance(source, target); // 计算利文斯顿距离
				// 小于阈值并且排除source本身
				if (distance <= THRESHOLD && distance != 0) {
					similarWords.add(target);
				}
			}

			result.put(source, similarWords);
		}

		return result;
	}
}
