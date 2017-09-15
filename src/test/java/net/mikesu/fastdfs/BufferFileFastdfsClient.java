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

import net.mikesu.fastdfs.data.BufferFile;

import org.junit.Test;

public class BufferFileFastdfsClient {
	

	@Test
	public void testFastdfsClient() throws Exception {
		FastdfsClient fastdfsClient = FastdfsClientFactory.getFastdfsClient();
		URL fileUrl = this.getClass().getResource("/WP_20150201_11_45_18_Pro.jpg");
		BufferFile file = new BufferFile();
		file.setFiledata(readFile(fileUrl.getPath()));
		file.setName("WP_20150201_11_45_18_Pro.jpg");
		
		String fileId = fastdfsClient.upload(file);
		System.out.println("fileId:"+fileId);
		assertNotNull(fileId);
		String url = fastdfsClient.getUrl(fileId);
		assertNotNull(url);
		System.out.println("url:"+url);
		Map<String,String> meta = new HashMap<String, String>();
		meta.put("fileName", file.getName());
		boolean result = fastdfsClient.setMeta(fileId, meta);
		assertTrue(result);
		Map<String,String> meta2 = fastdfsClient.getMeta(fileId);
		assertNotNull(meta2);
		System.out.println(meta2.get("fileName"));
//		result = fastdfsClient.delete(fileId);
//		assertTrue(result);
		fastdfsClient.close();
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
