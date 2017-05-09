package hadoop.connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.json.JSONObject;

import hadoop.dblp.model.Page;

public class ReadFileHadoopClusterCallable implements Callable<ArrayList<Page>> {
	BufferedReader br;
	FileSystem fs;
	Path path;
	ArrayList<Page> arrayList;
	Page page;
	String line;
	JSONObject jsonObject;

	public ReadFileHadoopClusterCallable(String url) throws Exception {
		arrayList = new ArrayList<>();
		fs = FileSystem.get(HadoopCluster.getConf());
		path = new Path(url);

	}

	@Override
	public ArrayList<Page> call() throws Exception {
		if (fs.exists(path)) {
			br = new BufferedReader(new InputStreamReader(fs.open(path)));
			
			while ((line = br.readLine()) != null) {
				String[] s = line.split("\t");
				page = new Page(jsonObject = new JSONObject(s[0]), jsonObject.getString("title"), s[1]);
				arrayList.add(page);
			}
			br.close();
			fs.close();
		}

		return arrayList;
	}

}
