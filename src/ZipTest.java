import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipTest {
    public static void main(String[] args) {
    	final String OUTPUT_ZIP_FILE = "D:\\MyFile.zip";
    	final String SOURCE_FOLDER = "D:\\Rutvik_Files";
    	
    	ZipTest appZip = new ZipTest();
    	List<String> fileList = appZip.generateFileList(new File(SOURCE_FOLDER), SOURCE_FOLDER);
    	boolean rst = appZip.createZipFile(OUTPUT_ZIP_FILE, SOURCE_FOLDER, fileList);
    	System.out.println(rst);
    }
    
    /**
     * Creating zip of recursively file
     * @param OUTPUT_ZIP_FILE output ZIP file location
     * @param SOURCE_FOLDER input source file location
     * @param fileList input file list
     * @return status of zip creation
     */
    public boolean createZipFile(String OUTPUT_ZIP_FILE, String SOURCE_FOLDER, List<String> fileList){
    	try{
    		byte[] buffer = new byte[1024];
    		FileOutputStream fos = new FileOutputStream(OUTPUT_ZIP_FILE);
    		ZipOutputStream zos = new ZipOutputStream(fos);
    		System.out.println("Output to Zip : " + OUTPUT_ZIP_FILE);
    		
    		for(String file : fileList){
    			System.out.println("File Added : " + file);
    			ZipEntry ze= new ZipEntry(file);
    			zos.putNextEntry(ze);
    			FileInputStream in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
    			int len;
    			while ((len = in.read(buffer)) > 0) {
    				zos.write(buffer, 0, len);
    			}
    			in.close();
    		}
    		
    		zos.closeEntry();
    		zos.close();
    	}catch(IOException ex){
    		ex.printStackTrace();
    		return false;
    	}
    	return true;
    }
    
    /**
     * Traverse a directory and get all files and add the file into fileList  
     * @param node file or directory
     * @param SOURCE_FOLDER source file location
     * @return file list from folder
     */
    public List<String> generateFileList(File node, final String SOURCE_FOLDER){
    	List<String> fileList = new ArrayList<String>();
    	
		if(node.isFile()){
			String file = node.getAbsoluteFile().toString();
			fileList.add( file.substring(SOURCE_FOLDER.length()+1, file.length()) );
		}
		
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				List<String> subFolderFileList = generateFileList(new File(node, filename), SOURCE_FOLDER);
				if(subFolderFileList!=null && subFolderFileList.size()>0){
					fileList.addAll(subFolderFileList);
				}
			}
		}
		return fileList;
    }
}