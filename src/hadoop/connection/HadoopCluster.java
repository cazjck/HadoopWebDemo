package hadoop.connection;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.FutureTask;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.MRConfig;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

import com.google.common.io.Files;

import hadoop.dblp.model.Page;
import hadoop.mapreduce.DBLPDriver;

public class HadoopCluster {
	private static Configuration conf = null;

	private static final String YARN_RESOURCE = "namenode:8032";
	private static final String DEFAULT_FS = "hdfs://namenode:9000";
	private static final String OUTPUT="hdfs://namenode:9000/output";

	public static Configuration getConf() {
		if (conf == null) {
			conf = new YarnConfiguration();
			conf.set("fs.defaultFS", DEFAULT_FS);
			conf.set("mapreduce.framework.name", "yarn");
			conf.set("yarn.resourcemanager.address", YARN_RESOURCE);
			conf.set("os.name", "Ubuntu");
			conf.set(MRConfig.MAPREDUCE_APP_SUBMISSION_CROSS_PLATFORM, "true");

		}
		return conf;
	}

	// Xóa file trong Hadoop Cluster
	public static void deleteFolder(Configuration conf, String folderPath) throws Exception {
		FileSystem fs = FileSystem.get(conf);
		Path path = new Path(folderPath);
		if (fs.exists(path)) {
			fs.delete(path, true);
		}
	}

	// Kiểm tra file trong Hadoop Cluster
	public static ArrayList<Page> checkResultHadoopCluster(String jarPath,String search, String type, int tieuChi) throws Exception {
		FileSystem fs = FileSystem.get(HadoopCluster.getConf());
		//String outputPath=DBLPDriver.outputHadoopCluster+"/"+search.trim()+"_"+type+"_"+tieuChi;
		String outputPath=DBLPDriver.outputHadoopCluster+"/"+search.replaceAll("\\s+", "")+"_"+type+"_"+tieuChi+"/";
		System.out.println("--------outputPath--------------"+outputPath);
		Path path = new Path(outputPath);
		if (fs.exists(path)) {
			System.out.println("Tìm thấy kết quả với từ khóa: "+search);
			return getDataHadoopCluster(outputPath);
		} else {
			return runHadoopCluster(jarPath, search, type, tieuChi,outputPath);
		}
	}
	

	// Lấy dữ liệu từ localhost
	public static ArrayList<Page> getDataHadoopLocal(String pathName) throws Exception {
		FutureTask<ArrayList<Page>> futureTask = new FutureTask<>(new ReadFileHadoopLocalCallable(pathName+"/part-r-00000"));
		futureTask.run();
		return futureTask.get();
	}

	// Lấy dữ liệu từ Hadoop Cluster
	public static ArrayList<Page> getDataHadoopCluster(String pathName) throws Exception {
		System.out.println(pathName+"part-r-00000");
		FutureTask<ArrayList<Page>> futureTask = new FutureTask<>(new ReadFileHadoopClusterCallable(pathName+"/part-r-00000"));
		futureTask.run();
		return futureTask.get();
	}

	// Lấy dữ liệu từ Hadoop Local
	public static ArrayList<Page> runHadoopLocal(String search, String type, int tieuChi,String outputPath) throws Exception {
		FutureTask<Boolean> futureTask = new FutureTask<>(new RunHadoopLocalCallable(search, type, tieuChi));
		futureTask.run();
		if (futureTask.get()) {
			return getDataHadoopLocal(outputPath);
		}
		return null;
	}

	// Lấy dữ liệu từ Hadoop Cluster
	public static ArrayList<Page> runHadoopCluster(String jarPath,String search, String type, int tieuChi,String outputPath) throws Exception {
		FutureTask<Boolean> futureTask = new FutureTask<>(new RunHadoopClusterCallable(jarPath, search, type, tieuChi));
		futureTask.run();
		if (futureTask.get()) {
			return getDataHadoopCluster(outputPath);
		}
		else {
			return null;
		}
	}

}
