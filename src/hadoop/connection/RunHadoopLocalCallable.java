package hadoop.connection;


import java.util.concurrent.Callable;

import hadoop.mapreduce.DBLPDriver;



public class RunHadoopLocalCallable implements Callable<Boolean> {
	String search,type;
	int tieuChi;
	public RunHadoopLocalCallable(String search,String type,int tieuChi) throws Exception {
		this.search=search;
		this.type=type;
		this.tieuChi=tieuChi;
		
	}
	@Override
	public Boolean call() throws Exception {
		return DBLPDriver.runHadoopLocal(search, type, tieuChi);
	}

}
