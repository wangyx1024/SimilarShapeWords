package Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {

	/**
	 * 读取文件，将结果逐行放入数组
	 *
	 * @param path 文件路径
	 * @return 结果数组
	 * @throws IOException
	 */
	public static List<String> readListFromFile(String path) throws IOException {
		FileReader fileReader = new FileReader(path);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		List<String> lines = new LinkedList<>();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}

		bufferedReader.close();

		return lines;
	}
}
