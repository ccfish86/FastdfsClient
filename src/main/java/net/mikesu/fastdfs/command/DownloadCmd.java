package net.mikesu.fastdfs.command;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;

import net.mikesu.fastdfs.data.BufferFile;
import net.mikesu.fastdfs.data.Result;

public class DownloadCmd extends AbstractCmd<BufferFile> {
	
	private BufferFile file;

    @Override
    public Result<BufferFile> exec(Socket socket) throws IOException {
        request(socket.getOutputStream());
        Response response = response(socket.getInputStream());
        if (response.isSuccess()) {
            byte[] data = response.getData();
            
            file.setFiledata(data);
            
            Result<BufferFile> result = new Result<BufferFile>(response.getCode());
            result.setData(file);
            return result;
        } else {
            Result<BufferFile> result = new Result<BufferFile>(response.getCode());
            // Error Message
            result.setMessage("下载失败！");
            return result;
        }
    }

    /**
     * 下载
     * @param group Group名
     * @param fileName 文件名
     */
	public DownloadCmd(String group, String fileName){
        super();
        file = new BufferFile();
        file.setName(fileName);
        
        byte[] bsOffset;
        byte[] bsDownBytes;
        
        bsOffset = long2buff(0);
        bsDownBytes = long2buff(0);

        byte[] groupByte = group.getBytes(charset);
        int group_len = groupByte.length;
        if (group_len > FDFS_GROUP_NAME_MAX_LEN) {
            group_len = FDFS_GROUP_NAME_MAX_LEN;
        }
        byte[] fileNameByte = fileName.getBytes(charset);
        body1 = new byte[bsOffset.length + bsDownBytes.length + FDFS_GROUP_NAME_MAX_LEN + fileNameByte.length];
        Arrays.fill(body1, (byte) 0);
        
        int offset = 0;
        System.arraycopy(bsOffset, 0, body1, offset, bsOffset.length);
        offset += bsOffset.length;
        System.arraycopy(bsDownBytes, 0, body1, offset, bsDownBytes.length);
        offset += bsDownBytes.length;
        System.arraycopy(groupByte, 0, body1, offset, group_len);
        offset += FDFS_GROUP_NAME_MAX_LEN;
        System.arraycopy(fileNameByte, 0, body1, offset, fileNameByte.length);
        
        this.requestCmd = STORAGE_PROTO_CMD_DOWNLOAD_FILE;
        this.responseCmd = STORAGE_PROTO_CMD_RESP;
        this.responseSize = -1;
        
        try {
            System.out.println(IOUtils.toString(body1));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
