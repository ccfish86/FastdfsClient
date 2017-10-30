package net.mikesu.fastdfs;

import java.io.File;
import java.util.Map;

import net.mikesu.fastdfs.data.BufferFile;

/**
 * FastDfs操作Client类
 * @author newtech
 * @version 1.1.1
 */
public interface FastdfsClient {

    @Deprecated
	public String upload(File file) throws Exception;
    @Deprecated
	public String upload(File file,String fileName) throws Exception;
    /**
     * 得到文件的URL
     * @param fileId 文件ID
     * @return 文件URL
     * @throws Exception
     */
	public String getUrl(String fileId) throws Exception;
	/**
	 * 更新文件META信息
	 * @param fileId 文件ID
	 * @param meta META信息
	 * @return 更新成功/不成功
	 * @throws Exception
	 */
	public Boolean setMeta(String fileId,Map<String,String> meta) throws Exception;
	/**
	 * 取得文件META信息
	 * @param fileId 文件ID
	 * @return META信息
	 * @throws Exception
	 */
	public Map<String,String> getMeta(String fileId) throws Exception;
	/**
	 * 删除文件
	 * @param fileId 文件ID
	 * @return 删除成功/不成功
	 * @throws Exception
	 */
	public Boolean delete(String fileId) throws Exception;
	/**
	 * 清除池,池不可用
	 */
	public void close();
	/**
	 * 清除池,池可用
	 */
    public void clear();


    /**
     * 上传一个文件
     * @param file 要上传的文件
     * @param ext 文件扩展名
     * @param meta meta key/value的meta data，可为null
     * @return fileid 带group的fileid
     * @throws Exception
     */
	@Deprecated
    public String upload(File file,String ext,Map<String,String> meta) throws Exception;

    /**
     * upload slave
     * @param file
     * @param fileid 带group的fileid,like group1/M00/00/01/abc.jpg
     * @param prefix slave的扩展名，如200x200
     * @param ext 文件扩展名，like jpg，不带.
     * @return 上传后的fileid   group1/M00/00/01/abc_200x200.jpg
     * @throws Exception
     */
    @Deprecated
    public String uploadSlave(File file,String fileid, String prefix, String ext) throws Exception;

    public String upload(BufferFile file) throws Exception;
    public String upload(BufferFile file,String fileName) throws Exception;
    /**
     * 上传一个文件
     * @param file 要上传的文件
     * @param ext 文件扩展名
     * @param meta meta key/value的meta data，可为null
     * @return fileid 带group的fileid
     * @throws Exception
     */
    public String upload(BufferFile file,String ext,Map<String,String> meta) throws Exception;

    /**
     * upload slave
     * @param file
     * @param fileid 带group的fileid,like group1/M00/00/01/abc.jpg
     * @param prefix slave的扩展名，如200x200
     * @param ext 文件扩展名，like jpg，不带.
     * @return 上传后的fileid   group1/M00/00/01/abc_200x200.jpg
     * @throws Exception
     */
    public String uploadSlave(BufferFile file,String fileid, String prefix, String ext) throws Exception;
    
    /**
     * 下载一个文件
     * @param fileId 文件ID
     * @return 文件内容
     * @throws Exception 异常
     */
    public BufferFile download(String fileId) throws Exception;

}
