package hadoop.mapreduce;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.json.JSONArray;
import org.json.JSONObject;

public class DBLPMapper extends Mapper<Object, Text, Text, Text> {
	public static final Log LOG = LogFactory.getLog(DBLPMapper.class);
	private Text valueAuthor = new Text();
	private Text word = new Text();
	JSONObject json;
	Pattern pattern, pattern2;
	Matcher matcher, matcher2;
	String searchWord;
	String type;
	int tieuchiTimKiem;

	@Override
	protected void setup(Mapper<Object, Text, Text, Text>.Context context) throws IOException, InterruptedException {
		Configuration conf = context.getConfiguration();
		searchWord = conf.get("searchWord");
		type = conf.get("type");
		tieuchiTimKiem = conf.getInt("tieuChiTimKiem", 1);
		if (tieuchiTimKiem == 0) {
			/*if (searchWord.startsWith(" ")) {
				searchWord=searchWord.substring(1, searchWord.length());
			}*/
			String[] arr = searchWord.split(" ");
			if (arr.length > 1) {
				pattern = Pattern.compile(arr[0]);
			} else {
				pattern = Pattern.compile(searchWord);
			}
		} else {
			pattern = Pattern.compile(searchWord);
		}
		if (type.equals("All")) {
			type = "";

		} else {
			pattern2 = Pattern.compile(type);
		}
	}

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		// Loại bỏ kết quả dư thừa
		// System.out.println(pattern.toString());
		matcher = pattern.matcher(value.toString());
		// type có thể là : article,...
		if (type != "") {
			matcher2 = pattern2.matcher(value.toString());
			if (matcher.find() && matcher2.find()) {
				// System.out.println(value.toString());
				getLineType(context, value, type);
			}

		} else {
			if (matcher.find()) {
				getLine(context, value);
			}
		}

	}

	private void getLineType(Context context, Text value, String type) {
		word.set(value.toString());
		try {
			json = new JSONObject(value.toString());
			if (json.get("type").equals(type)) {
				// Json có chứa phần tử author
				if (json.has("author")) {
					getAuthor(context, json, "author", searchWord, tieuchiTimKiem);
				}
				// Trường hợp không mà phần tử "author" mà là "editor"
				else if (json.has("editor")) {
					getAuthor(context, json, "editor", searchWord, tieuchiTimKiem);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void getLine(Context context, Text value) {
		word.set(value);
		try {
			json = new JSONObject(value.toString());
			// -----------Tìm tác giả hiện ra tiêu đề
			// Bỏ qua kể www vì kiểu này nói về nội dung của tác giả
			if (json.get("type").equals("www")) {
				return;
			}
			// Json có chứa phần tử author
			else if (json.has("author")) {
				getAuthor(context, json, "author", searchWord, tieuchiTimKiem);
			}
			// Trường hợp không mà phần // tử // "author" mà là // "editor"
			else if (json.has("editor")) {
				getAuthor(context, json, "editor", searchWord, tieuchiTimKiem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void getAuthor(Context context, JSONObject json, String typeAuthor, String searchWord, int tieuchiTimKiem)
			throws Exception {
		Object jsonAuthor = json.get(typeAuthor);
		JSONArray jsonArray;
		// Trường hợp là một JSONArray
		if (jsonAuthor instanceof JSONArray) {
			jsonArray = (JSONArray) jsonAuthor;
			for (Object object : jsonArray) {
				getTextAuthor(context, object, searchWord, json, tieuchiTimKiem);
			}
		} else {
			getTextAuthor(context, jsonAuthor, searchWord, json, tieuchiTimKiem);
		}
	}

	public void getTextAuthor(Context context, Object object, String searchWord, JSONObject json, int tieuchiTimKiem)
			throws Exception {
		String author = object.toString().trim();
		// tìm kiếm tương đối
		if (tieuchiTimKiem == 0) {
			// Trường hợp có 1 author
			if (author.contains(searchWord)) {
				valueAuthor.set(author);
				context.write(word, valueAuthor);
			}
			// tìm kiếm tuyệt đối
		} else if (author.equals(searchWord)) {
			valueAuthor.set(author);
			context.write(word, valueAuthor);
		}
	}
}