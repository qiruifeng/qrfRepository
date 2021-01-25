package cn.qrf.unzip;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.*;

public class demo {

    public static void unzip(String zipFilePath, String targetPath)
            throws IOException {
        OutputStream os = null;
        InputStream is = null;
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(zipFilePath,Charset.forName("GBK"));
            String directoryPath = "";
            if (null == targetPath || "".equals(targetPath)) {
                directoryPath = zipFilePath.substring(0, zipFilePath
                        .lastIndexOf("."));
            } else {
                directoryPath = targetPath;
            }
            Enumeration<?> entryEnum = zipFile.entries();
            if (null != entryEnum) {
                ZipEntry zipEntry = null;
                while (entryEnum.hasMoreElements()) {
                    zipEntry = (ZipEntry) entryEnum.nextElement();
                    if (zipEntry.getSize() > 0) {
                        // 文件
                        File targetFile = FileUtil.buildFile(directoryPath
                                + File.separator + zipEntry.getName(), false);
                        os = new BufferedOutputStream(new FileOutputStream(targetFile));
                        is = zipFile.getInputStream(zipEntry);
                        byte[] buffer = new byte[4096];
                        int readLen = 0;
                        while ((readLen = is.read(buffer, 0, 4096)) >= 0) {
                            os.write(buffer, 0, readLen);
                            os.flush();
                        }
                        is.close();
                        os.close();
                    }
                    if (zipEntry.isDirectory()) {
                        String pathTemp =  directoryPath + File.separator
                                + zipEntry.getName();
                        File file = new File(pathTemp);
                        file.mkdirs();
                        System.out.println(pathTemp);
//                        continue;
                    }
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if(null != zipFile){
                zipFile.close();
                zipFile = null;
            }
            if (null != is) {
                is.close();
            }
            if (null != os) {
                os.close();
            }
        }
    }



    public static void main(String[] args) throws IOException {


        unzip("D:\\test\\测试.zip","D:\\test\\target");



//        File file = new File("D:\\test\\test.zip");//当前压缩文件
//        ZipInputStream zin;//创建ZipInputStream对象
//        try {
//            ZipFile zipFile = new ZipFile(file);//创建压缩文件对象
//            zin = new ZipInputStream(new FileInputStream(file), Charset.forName("GBK"));//实例化对象，指明要解压的文件
//            ZipEntry entry ;
//            while (((entry=zin.getNextEntry())!=null)&& !entry.isDirectory()){//如果entry不为空，并不在同一个目录下
//                File tmp = new File(entry.getName());//解压出的文件路径
//                if (!tmp.exists()){//如果文件不存在
//                    tmp.getParentFile().mkdirs();//创建文件父类文件夹路径
//                    OutputStream os = new FileOutputStream(tmp);//将文件目录中的文件放入输出流
//                    //用输入流读取压缩文件中制定目录中的文件
//                    InputStream in = zipFile.getInputStream(entry);
//                    int count = 0;
//                    while ((count = in.read())!=-1){//如有输入流可以读取到数值
//                        os.write(count);//输出流写入
//                    }
//                    os.close();
//                    in.close();
//                }
//                zin.closeEntry();
//                System.out.println(entry.getName()+"解压成功");
//            }
//            zin.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
