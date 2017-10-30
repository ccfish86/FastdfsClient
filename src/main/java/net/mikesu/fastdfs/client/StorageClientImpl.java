package net.mikesu.fastdfs.client;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.mikesu.fastdfs.FastdfsClientConfig;
import net.mikesu.fastdfs.command.ActiveTestCmd;
import net.mikesu.fastdfs.command.CloseCmd;
import net.mikesu.fastdfs.command.Command;
import net.mikesu.fastdfs.command.DeleteCmd;
import net.mikesu.fastdfs.command.DownloadCmd;
import net.mikesu.fastdfs.command.GetMetaDataCmd;
import net.mikesu.fastdfs.command.SetMetaDataCmd;
import net.mikesu.fastdfs.command.UploadBufferFileCmd;
import net.mikesu.fastdfs.command.UploadBufferFileSlaveCmd;
import net.mikesu.fastdfs.command.UploadCmd;
import net.mikesu.fastdfs.command.UploadSlaveCmd;
import net.mikesu.fastdfs.data.BufferFile;
import net.mikesu.fastdfs.data.Result;

/**
 * 文件存储服务Client
 * 
 * @author 袁贵
 * @version 1.1.1
 * @since  1.0
 */
public class StorageClientImpl extends AbstractClient implements StorageClient {
    private boolean active = true;
    private Socket socket;
    private String host;
    private Integer port;
    private Integer connectTimeout = FastdfsClientConfig.DEFAULT_CONNECT_TIMEOUT * 1000;
    private Integer networkTimeout = FastdfsClientConfig.DEFAULT_NETWORK_TIMEOUT * 1000;
    
    private static Logger logger = LoggerFactory.getLogger(StorageClientImpl.class);

    private Socket getSocket() throws IOException {
        if (socket == null) {
            logger.debug("connecting to storage {}:{}", host, port);
            socket = new Socket();
            socket.setKeepAlive(true);
            socket.setSoTimeout(networkTimeout);
            socket.connect(new InetSocketAddress(host, port), connectTimeout);
            logger.debug("connected to storage {}:{}", host, port);
        }
        return socket;
    }

    public StorageClientImpl(String address) {
        super();
        String[] hostport = address.split(":");
        this.host = hostport[0];
        this.port = Integer.valueOf(hostport[1]);
        this.active = true;
    }

    public StorageClientImpl(String address, Integer connectTimeout, Integer networkTimeout) {
        this(address);
        this.connectTimeout = connectTimeout;
        this.networkTimeout = networkTimeout;
    }

    public void close() throws IOException {
        logger.debug("disconnecting to storage {}:{}", host, port);
        Socket socket = getSocket();
        Command<Boolean> command = new CloseCmd();
        command.exec(socket);
        socket.close();
        socket = null;
        this.active = false;
        logger.debug("disconnected to storage {}:{}", host, port);
    }

    @Override
    public Result<String> uploadSlave(File file, String fileid, String slavePrefix, String ext, Map<String, String> meta) throws IOException {
        Socket socket = getSocket();
        UploadSlaveCmd uploadSlaveCmd = new UploadSlaveCmd(file,fileid,slavePrefix,ext);
        Result<String> result = uploadSlaveCmd.exec(socket);

        if (meta != null) {
            String[] tupple = super.splitFileId(fileid);
            if (tupple != null) {
                String group = tupple[0];
                String fileName = tupple[1];
                this.setMeta(group, fileName, meta);
            }
        }
        return result;
    }

    public Result<String> upload(File file, String fileName, byte storePathIndex) throws IOException {
        Socket socket = getSocket();
        UploadCmd uploadCmd = new UploadCmd(file, fileName, storePathIndex);
        return uploadCmd.exec(socket);
    }

    public Result<Boolean> delete(String group, String fileName) throws IOException {
        Socket socket = getSocket();
        DeleteCmd deleteCmd = new DeleteCmd(group, fileName);
        return deleteCmd.exec(socket);
    }

    @Override
    public Result<Boolean> setMeta(String group, String fileName,
                                   Map<String, String> meta) throws IOException {
        Socket socket = getSocket();
        SetMetaDataCmd setMetaDataCmd = new SetMetaDataCmd(group, fileName, meta);
        return setMetaDataCmd.exec(socket);
    }

    @Override
    public Result<Map<String, String>> getMeta(String group, String fileName)
            throws IOException {
        Socket socket = getSocket();
        GetMetaDataCmd getMetaDataCmd = new GetMetaDataCmd(group, fileName);
        return getMetaDataCmd.exec(socket);
    }


    /**
     * check storage client socket is closed
     *
     * @return boolean
     */
    @Override
    public boolean isClosed() {

        if (this.socket == null) {
            //return true;
            try {
                this.socket = getSocket();
            } catch (IOException e) {
                logger.error("连接失败！", e);
                return true;
            }
        }

        if (this.socket.isClosed()){
            return true;
        }else {
            //根据fastdfs的Active_Test_Cmd测试连通性
            ActiveTestCmd atcmd = new ActiveTestCmd();
            try {
                Result<Boolean> result = atcmd.exec(getSocket());
                //True,表示连接正常
                if(result.getData()){
                    return false;
                }else {
                    return true;
                }
            } catch (IOException e) {
                //e.printStackTrace();
                logger.debug("IO测试异常！", e);
            }
            //有异常，直接丢掉这个连接，让连接池回收
            return true;
        }
    }

    @Override
    public Result<String> uploadSlave(BufferFile file, String fileid, String slavePrefix, String ext, Map<String, String> meta) throws IOException {
        Socket socket = getSocket();
        UploadBufferFileSlaveCmd uploadSlaveCmd = new UploadBufferFileSlaveCmd(file,fileid,slavePrefix,ext);
        Result<String> result = uploadSlaveCmd.exec(socket);

        if (meta != null) {
            String[] tupple = super.splitFileId(fileid);
            if (tupple != null) {
                String group = tupple[0];
                String fileName = tupple[1];
                this.setMeta(group, fileName, meta);
            }
        }
        return result;
    }
    public Result<String> upload(BufferFile file, String fileName, byte storePathIndex) throws IOException {
        Socket socket = getSocket();
        UploadBufferFileCmd uploadCmd = new UploadBufferFileCmd(file, fileName, storePathIndex);
        return uploadCmd.exec(socket);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public Result<BufferFile> download(String group, String fileName) throws IOException {
        Socket socket = getSocket();
        DownloadCmd downloadCmd = new DownloadCmd(group, fileName);
        return downloadCmd.exec(socket);
    }

}
