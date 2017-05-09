package hadoop.connection;


import java.util.concurrent.Callable;

import hadoop.mapreduce.DBLPDriver;



public class RunHadoopClusterCallable implements Callable<Boolean> {
	String search,type;
	int tieuChi;
	String jarPath;
	public RunHadoopClusterCallable(String jarPath,String search,String type,int tieuChi ) throws Exception {
		this.jarPath=jarPath;
		this.search=search;
		this.type=type;
		this.tieuChi=tieuChi;
		
	}
	@Override
	public Boolean call() throws Exception {
		return DBLPDriver.runHadoopCluster(jarPath, search, type, tieuChi);
	}

}
