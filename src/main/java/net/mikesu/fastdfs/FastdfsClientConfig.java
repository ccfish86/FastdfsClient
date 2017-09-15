package net.mikesu.fastdfs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FastdfsClientConfig {
	
	public static final int DEFAULT_CONNECT_TIMEOUT = 5; // second
	public static final int DEFAULT_NETWORK_TIMEOUT = 30; // second
	
	private int connectTimeout = DEFAULT_CONNECT_TIMEOUT * 1000;
	private int networkTimeout = DEFAULT_NETWORK_TIMEOUT * 1000;
	private int maxTotal = 500;
    private int maxTotalPerKey = 5;
    private int minIdlePerKey = 0;
    private int maxIdlePerKey = 5;
    private long maxWaitMillis = -1;
    private boolean testOnBorrow = true;
    private boolean testOnReturn = false;
    private boolean testWhileIdle = false;
	
	private List<String> trackerAddrs = new ArrayList<String>();
	
	private Logger logger = LoggerFactory.getLogger(LoggerFactory.class);
//	private int trackerClientPoolMaxIdlePerKey = 
	
	public FastdfsClientConfig() {
		super();
	}
	
	public FastdfsClientConfig(String configFile) throws ConfigurationException {
		super();
//		String conf = FastdfsClientConfig.class.getClassLoader().getResource(configFile).getPath();
		FileConfiguration config = new PropertiesConfiguration();
		config.setEncoding("UTF-8");
		config.setFileName(configFile);
		config.load();
		
		this.connectTimeout = config.getInt("connect_timeout", DEFAULT_CONNECT_TIMEOUT)*1000;
		this.networkTimeout = config.getInt("network_timeout", DEFAULT_NETWORK_TIMEOUT)*1000;
        this.maxTotal = config.getInt("maxTotal", 500);
        this.maxTotalPerKey = config.getInt("maxTotalPerKey", 5);
        this.minIdlePerKey = config.getInt("minIdlePerKey", 0);
        this.maxIdlePerKey = config.getInt("maxIdlePerKey", 5);
        this.maxWaitMillis = config.getInt("maxWaitMillis", 30)*1000;
        this.testOnBorrow = config.getBoolean("testOnBorrow", true);
        this.testOnReturn = config.getBoolean("testOnReturn", false);
        this.testWhileIdle = config.getBoolean("testWhileIdle", false);
        
		List<Object> trackerServers = config.getList("tracker_server");
		for(Object trackerServer:trackerServers){
			trackerAddrs.add((String)trackerServer);
		}
		
        if (logger.isDebugEnabled()) {
            logger.debug("connect_timeout", connectTimeout);
            logger.debug("network_timeout", networkTimeout);
            logger.debug("maxTotal", maxTotal);
            logger.debug("maxTotalPerKey", maxTotalPerKey);
            logger.debug("minIdlePerKey", minIdlePerKey);
            logger.debug("maxIdlePerKey", maxIdlePerKey);
            logger.debug("maxWaitMillis", maxWaitMillis);
            logger.debug("testOnBorrow", testOnBorrow);
            logger.debug("testOnReturn", testOnReturn);
            logger.debug("testWhileIdle", testWhileIdle);
            
            for (String trackerAddr :trackerAddrs){
                logger.debug("tracker_server:{}", trackerAddr);
            }
        }
	}
	
	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getNetworkTimeout() {
		return networkTimeout;
	}

	public void setNetworkTimeout(int networkTimeout) {
		this.networkTimeout = networkTimeout;
	}

	public List<String> getTrackerAddrs() {
		return trackerAddrs;
	}

	public void setTrackerAddrs(List<String> trackerAddrs) {
		this.trackerAddrs = trackerAddrs;
	}

	public GenericKeyedObjectPoolConfig getTrackerClientPoolConfig(){
		GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();
		poolConfig.setMaxIdlePerKey(maxIdlePerKey);
		poolConfig.setMaxTotal(maxTotal);
		poolConfig.setMaxTotalPerKey(maxTotalPerKey);
        poolConfig.setMinIdlePerKey(minIdlePerKey);
        poolConfig.setMaxIdlePerKey(maxIdlePerKey);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setTestOnBorrow(testOnBorrow);
        poolConfig.setTestOnReturn(testOnReturn);
        poolConfig.setTestWhileIdle(testWhileIdle);

		return poolConfig;
	}
	

	public GenericKeyedObjectPoolConfig getStorageClientPoolConfig(){
		GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();
        poolConfig.setMaxIdlePerKey(maxIdlePerKey);
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxTotalPerKey(maxTotalPerKey);
        poolConfig.setMinIdlePerKey(minIdlePerKey);
        poolConfig.setMaxIdlePerKey(maxIdlePerKey);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setTestOnBorrow(testOnBorrow);
        poolConfig.setTestOnReturn(testOnReturn);
        poolConfig.setTestWhileIdle(testWhileIdle);
		return poolConfig;
	}

}
