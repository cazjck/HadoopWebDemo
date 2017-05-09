package hadoop.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import hadoop.connection.HadoopCluster;

public class DBLPDriver  {
	private static final String inputHadoopLocal = "D:/DBLP.json";
	public static final String outputHadoopLocal = "../output";
	private static final String inputHadoopCluster = "hdfs://namenode:9000/input/DBLP.json";
	public static final  String outputHadoopCluster = "hdfs://namenode:9000/output";
	public static void main(String[] args) throws Exception {
	//	runHadoop("Luc","All",0);
	}
	public static boolean runHadoopLocal(String search,String type, int tieuChi) throws Exception {
		//outputHadoopLocal += "/"+search.trim()+"_"+type+"_"+tieuChi+"/";
		String outputPath=outputHadoopLocal+"/"+search.trim()+"_"+type+"_"+tieuChi+"/";

		Configuration conf = new Configuration();
		conf.set("searchWord",search);
		conf.set("type",type.trim());
		conf.setInt("tieuChiTimKiem", tieuChi);
		Job job = Job.getInstance(conf, "Application_"+search);
		job.setJarByClass(DBLPDriver.class); // run in localhost
		// job.setJar("jar/MapReduceDriver.jar");// run in Hadoop cluster
		job.setCombinerClass(DBLPReducer.class);
		job.setMapperClass(DBLPMapper.class);
		job.setReducerClass(DBLPReducer.class);
		// Trong trường hợp Map và Reduce khác dữ liệu
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		//System.out.println(outputLocal);
		deleteFolder(conf,outputPath);
		// HadoopCluster.deleteFolder(conf, output);
		FileInputFormat.addInputPath(job, new Path(inputHadoopLocal));
		FileOutputFormat.setOutputPath(job, new Path(outputPath));

		return job.waitForCompletion(true) ? true : false;
	}
	
	
	public static boolean runHadoopCluster(String jarPath,String search,String type, int tieuChi) throws Exception {
		//outputHadoopCluster =outputHadoopCluster+"/"+search.trim()+"_"+type+"_"+tieuChi+"/";
		String outputPath=outputHadoopCluster+"/"+search.trim()+"_"+type+"_"+tieuChi+"/";
		Configuration conf=HadoopCluster.getConf();
		conf.set("searchWord",search);
		conf.set("type",type.trim());
		conf.setInt("tieuChiTimKiem", tieuChi);
		Job job = Job.getInstance(conf, "Application_"+search.trim()+"_"+type+"_"+tieuChi);
		
		//job.setJarByClass(DBLPDriver.class); // run in localhost
	
		//job.setJar("./HadoopWebDemo/jars/MapReduceDriver.jar");// run in Hadoop cluster
		job.setJar(jarPath);// run in Hadoop cluster
		job.addFileToClassPath(new Path("hdfs://namenode:9000/jar/json-20160810.jar"));
		//job.addCacheArchive(new URI("J:/HK1 year 4/Do an 2 Big Data/Source code/HadoopWebDemo/jar/json-20160810.jar"));
		job.setCombinerClass(DBLPReducer.class);
		job.setMapperClass(DBLPMapper.class);
		job.setReducerClass(DBLPReducer.class);
		// Trong trường hợp Map và Reduce khác dữ liệu
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		//System.out.println(outputHadoopCluster);
		deleteFolder(conf,outputPath);
		// HadoopCluster.deleteFolder(conf, output);
		FileInputFormat.addInputPath(job, new Path(inputHadoopCluster));
		FileOutputFormat.setOutputPath(job, new Path(outputPath));

		return job.waitForCompletion(true) ? true : false;
	}
	public static void deleteFolder(Configuration conf, String folderPath) throws Exception {
		FileSystem fs = FileSystem.get(conf);
		Path path = new Path(folderPath);
		if (fs.exists(path)) {
			fs.delete(path, true);
		}
	}
}
