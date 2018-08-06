package com.icecool.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class LevenshteinDistanceCalculator {

	/**
	 * 阈值
	 */
	private static final int THRESHOLD = 0;

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
				matrix[i][j] = min(matrix[i - 1][j] + 1, matrix[i][j - 1] + 1, matrix[i - 1][j - 1] + cost);
			}
		}

		// Step 7
		return matrix[n][m];
	}

	/**
	 * @param numbers 要比较的数
	 * @return 其中的最小值
	 */
	private static int min(int... numbers) {
		return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
	}

	/**
	 * 传入两个数组，sources代表原单词，targets代表词库
	 *
	 * @param sources 原单词数组
	 * @param targets 词库数组
	 * @return 对比结果map
	 */
	public static Map<String, Map<Integer, List<String>>> getSimilarWords(List<String> sources, List<String> targets) throws FileNotFoundException, UnsupportedEncodingException {
		// 如果原单词数组或者词库数组为空则返回空map
		if (CollectionUtils.isEmpty(sources) || CollectionUtils.isEmpty(targets)) {
			return new HashMap<>();
		}

		Map<String, Map<Integer, List<String>>> result = new TreeMap<>(); // 形近字结果

		// 遍历原单词数组
		for (int i = 0; i < sources.size(); i++) {
			String source = sources.get(i);

			// 原单词非空校验
			if (StringUtils.isBlank(source)) {
				continue;
			}

			int sLen = source.length(); // source的长度
			int thresholdDynamic = getThreshold(sLen); // 动态计算阈值

			System.out.println(i + ":" + source + ":" + sLen);

			Map<Integer, List<String>> similarWordsMap = new TreeMap<>(); // k是L氏距离，v是形近词数组

			// 遍历词库
			for (String target : targets) {
				// 词库单词非空校验
				if (StringUtils.isBlank(target)) {
					continue;
				}

				// 如果是同一个单词直接忽略
				if (source.equals(target)) {
					continue;
				}

				int tLen = target.length(); // target的长度
				// 先对比长度，长度不符合就直接pass了
				if (Math.abs(sLen - tLen) > thresholdDynamic) {
					continue;
				}

				int distance = getLevenshteinDistance(source, target); // 计算利文斯顿距离
				// 判断是否小于阈值
				if (distance <= thresholdDynamic) {
					List<String> list = MapUtils.getObject(similarWordsMap, distance, new ArrayList<>());
					list.add(target);
					similarWordsMap.put(distance, list);
				}
			}

			if (MapUtils.isNotEmpty(similarWordsMap)) {
				result.put(source, similarWordsMap);
			}
		}

		return result;
	}

	private static int getThreshold(int len) {
		if (len >= 1 && len <= 5) {
			return 1;
		} else if (len >= 6 && len <= 8) {
			return 2;
		} else {
			return 3;
		}
	}
}
