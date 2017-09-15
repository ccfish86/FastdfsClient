package net.mikesu.fastdfs.data;

/**
 * 待上传的文件
 * 
 * @author 袁贵
 * @since  2015年5月18日
 */
public class BufferFile {

    /**
     * 文件内容
     */
    private byte[] filedata;
    /**
     * 文件名
     */
    private String name;
    /**
     * 取得文件内容
     * @return the filedata
     */
    public byte[] getFiledata() {
        return filedata;
    }
    /**
     * 设置文件内容
     * @param filedata the filedata to set
     */
    public void setFiledata(byte[] filedata) {
        this.filedata = filedata;
    }
    
    /**
     * 取得文件名
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * 设置文件名
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
