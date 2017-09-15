package net.mikesu.fastdfs;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import net.mikesu.fastdfs.data.BufferFile;

public class FastdfsClientModifyTest {
	
    @BeforeClass
    public static void bfClass() {
        System.setProperty("conf.base","D:\\etc\\agent");
    }
    
	@Test
	public void testFastdfsClient() throws Exception {
		FastdfsClient fastdfsClient = FastdfsClientFactory.getFastdfsClient();
//		URL fileUrl = this.getClass().getResource("/1080.jpg");
//		File file = new File(fileUrl.getPath());
//		String fileId = fastdfsClient.upload(file);
//		System.out.println("fileId:"+fileId);
//		assertNotNull(fileId);
//		String url = fastdfsClient.getUrl(fileId);
//		assertNotNull(url);
//		System.out.println("url:"+url);
//		Map<String,String> meta = new HashMap<String, String>();
//		meta.put("fileName", file.getName());
//		boolean result = fastdfsClient.setMeta(fileId, meta);
//		assertTrue(result);
//		Map<String,String> meta2 = fastdfsClient.getMeta(fileId);
//		assertNotNull(meta2);
//		System.out.println(meta2.get("fileName"));
		//result = fastdfsClient.delete(fileId);
		try {
//		    String fileId = "group1/M00/00/04/wKgKK1e2o7WANoqTAAC3jx7efrk793.jpg";
		    String fileId = "group1/M00/00/04/wKgKHVe2tLqAc8kNAAAYMyTEjco95.html";
//		    URL fileUrl2 = this.getClass().getResource("/Koala.jpg");
		    URL fileUrl2 = this.getClass().getResource("/manual2.html");
//		    BufferFile file = fastdfsClient.download(fileId);
//		    System.err.println(file.getFiledata().length);
		    byte[] newFileContent = readFile(fileUrl2.getPath());
		    Boolean result = fastdfsClient.modifyFile(fileId, newFileContent);
		    assertTrue(result);
		} finally{
		    fastdfsClient.close();
		}
	}


    private byte[] readFile(String path) throws IOException {
        
        byte[] s;
        int l;
        File f = new File(path);
        Long fl = f.length();
        
        FileReader fr = new FileReader(f);
        InputStream fi = new FileInputStream(f);
        

        byte[] bff = new byte[fl.intValue()];
        
        if ((l = fi.read(bff)) > 0) {
            
            s = bff;
        } else {
            s = new byte[0];
        }
        fr.close();
        fi.close();
        return s;
    }
}
