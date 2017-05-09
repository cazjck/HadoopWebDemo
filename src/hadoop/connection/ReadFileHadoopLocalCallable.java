package hadoop.connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.json.JSONObject;

import hadoop.dblp.model.Page;

public class ReadFileHadoopLocalCallable implements Callable<ArrayList<Page>> {
	FileInputStream fis;
	InputStreamReader is ;
	BufferedReader br;
	ArrayList<Page> arrayList;
	Page page;
	String line;
	JSONObject jsonObject;
	public ReadFileHadoopLocalCallable(String path) throws Exception {
		fis=new FileInputStream(new File(path+"/part-r-00000"));
		is=new InputStreamReader(fis);
		br=new BufferedReader(is);
		arrayList=new ArrayList<>();
		
	}
	@Override
	public ArrayList<Page> call() throws Exception {
	    while ((line=br.readLine()) != null) {
	      String[] s = line.split("\t");
	     page=new Page(jsonObject=new JSONObject(s[0]),jsonObject.getString("title"), s[1]);
	      arrayList.add(page);
	    }
		return arrayList;
	}

}
