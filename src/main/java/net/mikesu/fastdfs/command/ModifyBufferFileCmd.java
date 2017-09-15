package net.mikesu.fastdfs.command;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import net.mikesu.fastdfs.data.Result;

public class ModifyBufferFileCmd extends AbstractCmd<Boolean> {
	
    private byte[] fileContent;

	@Override
	public Result<Boolean> exec(Socket socket) throws IOException {
	    InputStream is = new ByteArrayInputStream(fileContent);
        request(socket.getOutputStream(), is);
        Response response = response(socket.getInputStream());
        if(response.isSuccess()){
            return new Result<Boolean>(response.getCode(),true);
        }else{
            return new Result<Boolean>(response.getCode(),"Modify Error");
        }
    }

    /**
     * 
     * @param group 组
     * @param remoteFileName 文件ID
     * @param fileContent 文件内容
     */
	public ModifyBufferFileCmd(String group, String remoteFileName, byte[] fileContent){
        super();
        byte[] groupByte = group.getBytes(charset);
        int group_len = groupByte.length;
        if (group_len > FDFS_GROUP_NAME_MAX_LEN) {
            group_len = FDFS_GROUP_NAME_MAX_LEN;
        }
        byte[] fileNameByte = remoteFileName.getBytes(charset);
        body1 = new byte[3 * FDFS_PROTO_PKG_LEN_SIZE + fileNameByte.length];
        Arrays.fill(body1, (byte) 0);
        int offset = 0;
        byte[] fileNameLen = long2buff(fileNameByte.length);
        System.arraycopy(fileNameLen, 0, body1, offset, fileNameLen.length);
        offset += fileNameLen.length;
        byte[] offsetLen = long2buff(0);
        System.arraycopy(offsetLen, 0, body1, offset, offsetLen.length);
        offset += offsetLen.length;
        byte[] modifyLen = long2buff(fileContent.length);
        System.arraycopy(modifyLen, 0, body1, offset, modifyLen.length);
        offset += modifyLen.length;
        System.arraycopy(fileNameByte, 0, body1, offset, fileNameByte.length);
        
        this.fileContent = fileContent;
        this.requestCmd = STORAGE_PROTO_CMD_MODIFY_FILE;
        this.body2Len = fileContent.length;
        this.responseCmd = STORAGE_PROTO_CMD_RESP;        
        this.responseSize = 0;
        
        try {
            System.out.println(IOUtils.toString(body1));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
