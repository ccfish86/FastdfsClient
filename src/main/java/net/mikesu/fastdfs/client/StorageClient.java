package net.mikesu.fastdfs.client;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import net.mikesu.fastdfs.data.BufferFile;
import net.mikesu.fastdfs.data.Result;

/**
 * 文件存储服务Client
 * 
 * @author 袁贵
 * @version 1.1.1
 * @since  1.0
 */
public interface StorageClient {
	
	public Result<String> upload(File file,String fileName,byte storePathIndex) throws IOException;
	public Result<Boolean> delete(String group,String fileName) throws IOException;
	public Result<Boolean> setMeta(String group,String fileName,Map<String,String> meta) throws IOException;
	public Result<Map<String,String>> getMeta(String group,String fileName) throws IOException;
	public void close() throws IOException;

    /**
     * 指定主文件id,存为slave
     * @param file 文件
     * @param fileid 主文件id,带group,如 g1/M00/00/00/aaaabbbbccc.jpg
     * @param slavePrefix slave的后缀名 如200x200,最终的文件名将为g1/M00/00/00/aaaabbbbccc_200x200.jpg
     * @param ext 扩展文件名，可以为null,如果为null，则从fileid里取
     * @param meta 文件元数据，可以为null
     * @return fileid 带group的文件fileid
     * @throws IOException
     */
    public Result<String> uploadSlave(File file, String fileid,String slavePrefix,String ext,Map<String,String> meta) throws IOException;

    /**
     * check storage client socket is closed
     * @return boolean
     */
    public boolean isClosed();
    
    /**
     * 指定主文件id,存为slave
     * @param file 文件
     * @param fileid 主文件id,带group,如 g1/M00/00/00/aaaabbbbccc.jpg
     * @param slavePrefix slave的后缀名 如200x200,最终的文件名将为g1/M00/00/00/aaaabbbbccc_200x200.jpg
     * @param ext 扩展文件名，可以为null,如果为null，则从fileid里取
     * @param meta 文件元数据，可以为null
     * @return fileid 带group的文件fileid
     * @throws IOException
     */
    public Result<String> uploadSlave(BufferFile file, String fileid,String slavePrefix,String ext,Map<String,String> meta) throws IOException;
    public Result<String> upload(BufferFile file,String fileName,byte storePathIndex) throws IOException;

    public boolean isActive();

    public void setActive(boolean active);
    
    /**
     * 下载文件
     * @param group 组
     * @param fileName 文件名
     * @return 文件内容
     * @throws IOException
     * @since  3.0
     */
    public Result<BufferFile> download(String group,String fileName) throws IOException;
    

}
