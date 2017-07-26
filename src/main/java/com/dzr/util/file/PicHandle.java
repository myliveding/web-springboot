package com.dzr.util.file;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

//此包是针对文件的上传时引用的包(springMVC)
//下面这两个包已经被JDK1.7移除，在引用的时候要是报错的话
//在Eclipse中将编译器针对过时的类报错的地方改成警告即可
//（Preferences/Java/compiler/errors/warning/forbidden-->waring）

/**
 * @Description 本类的作用就是将图片文件压缩成指定的宽度和长度
 * @FileName PicHandle
 * @Author dingzr
 * @CreateTime 2017/6/20 21:02 六月
 */
public class PicHandle {

    private File file = null;//文件对象
    private String inputDir;//输入图路径
    private String outputDir;//输出图路径
    private String inputFileName;//输入图文件名
    private String outputFileName;//输出图文件名
    private int outputWidth = 100;//输出图片宽度
    private int outputHeight = 100;//输出图片高度
    private boolean proportion = true;//输出图片是否等比缩放(默认为true)
    private MultipartFile myFile;//文件上传MultipartFile对象

    //初始化变量
    public PicHandle(){
        inputDir = "";
        outputDir = "";
        inputFileName = "";
        outputFileName = "";
        outputWidth = 100;
        outputHeight = 100;
    }

    //属性set方法为对象赋值
    public void setInputDir(String inputDir) {
        this.inputDir = inputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public void setOutputWidth(int outputWidth) {
        this.outputWidth = outputWidth;
    }

    public void setOutputHeight(int outputHeight) {
        this.outputHeight = outputHeight;
    }

    public void setWidthAndHeight(int width, int height) {
        this.outputWidth = width;
        this.outputHeight = height;
    }

    public void setMyFile(MultipartFile myFile) {
        this.myFile = myFile;
    }

    //-----------------下面就是调用的方法

    /**
     *获得图片大小
     *String path:图片路径
     */
    public long getPicSize(String path){
        file = new File(path);
        if(!file.exists()){
            return 0;
        }else{
            return file.length();
        }
    }

    /*
     *图片处理:不涉及文件上传,简单根据传过来的图片路径和图片名称进行压缩处理
     */
    public String compressPic(){
        try{
            //获得文件源
            file = new File(inputDir+inputFileName);
            if(!file.exists()){
                return "";
            }
            if(file.length()/1024 > 500){
                //imageIO.read(arg);图片解析,可传入文件,文件输入流等
                Image img = ImageIO.read(file);
                if(img.getWidth(null) == -1){
                    return "no";
                }else{
                    int newWidth;
                    int newHeight;
                    //判断是否等比缩放
                    if(this.proportion == true ){
                        double rate = (double)img.getWidth(null)/(double)outputWidth;
                        newWidth = outputWidth;
                        newHeight = new Double(img.getHeight(null)/rate).intValue();
                    }else{
                        newWidth = outputWidth;
                        newHeight = outputHeight;
                    }
                    /**
                     *Image.SCALE_SMOOTH的缩略算法生成缩略图片的平滑度的优先级比速度高,生成的图片质量好但是速度慢
                     *根据具体要求取舍时间或是质量
                     */
                    BufferedImage tag = new BufferedImage((int)newWidth,(int)newHeight,BufferedImage.TYPE_INT_RGB);
                /* @param    img    the specified image to be drawn.
                 * @param    x      x坐标.
                 * @param    y      y坐标.
                 * @param    width  the width of the rectangle.
                 * @param    height the height of the rectangle.
                 * @param    observer   像素是否被改变
                 */
                    tag.getGraphics().drawImage(img,0,0,newWidth,newHeight,null);
                    FileOutputStream out = new FileOutputStream(outputDir+outputFileName);
                    //JPEGImageEncoder可适用于其他图片的类型的转换
                    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                    encoder.encode(tag);
                    out.close();
                }
            }

        }catch (Exception e){
        }
        return outputDir+outputFileName;
    }

    /*
    *图片处理:涉及文件上传,上传文件获取的是MultipartFile对象,而不是文件路径或是文件名
    */
    public String compressPicVersion2(){
        try{
            if(myFile.isEmpty()){
                return "";
            }
            Image img = ImageIO.read(myFile.getInputStream());
            if(img.getWidth(null) == -1){
                return "no";
            }else{
                int newWidth;
                int newHeight;
                if(this.proportion == true ){
                    double rate = (double)img.getWidth(null)/(double)outputWidth;
                    newWidth = outputWidth;
                    newHeight = new Double(img.getHeight(null)/rate).intValue();
                }else{
                    newWidth = outputWidth;
                    newHeight = outputHeight;
                }
                BufferedImage tag = new BufferedImage((int)newWidth,(int)newHeight,BufferedImage.TYPE_INT_RGB);
                tag.getGraphics().drawImage(img,0,0,newWidth,newHeight,null);
                FileOutputStream out = new FileOutputStream(outputDir+outputFileName);
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                encoder.encode(tag);
                out.close();
            }
        }catch (IOException eo){

        }
        return outputDir+outputFileName;
    }

    public String compressPic(String inputDir, String outputDir, String inputFileName, String outputFileName,
                              int width, int height, boolean gp){
        // 输入图路径
        this.inputDir = inputDir;
        // 输出图路径
        this.outputDir = outputDir;
        // 输入图文件名
        this.inputFileName = inputFileName;
        // 输出图文件名
        this.outputFileName = outputFileName;
        // 设置图片长宽
        setWidthAndHeight(width, height);
        // 是否是等比缩放 标记
        this.proportion = gp;
        return compressPic();
    }

    public String compressPicVersion2(MultipartFile myFile , String outputDir, String outputFileName,
                                      int width, int height, boolean gp) {
        this.myFile = myFile;
        // 输出图路径
        this.outputDir = outputDir;
        // 输出图文件名
        this.outputFileName = outputFileName;
        // 设置图片长宽
        setWidthAndHeight(width, height);
        // 是否是等比缩放 标记
        this.proportion = gp;
        return compressPicVersion2();
    }

    // main测试
    // compressPic(大图片路径,生成小图片路径,大图片文件名,生成小图片文名,生成小图片宽度,生成小图片高度,是否等比缩放(默认为true))
    public static void main(String[] arg) {
        PicHandle mypic = new PicHandle();
        mypic.compressPic("e:/filein/", "e:/fileout/", "DSC_0042.JPG", "DSC_0042.JPG", 200, 200, true);

//        int count = 0; // 记录全部图片压缩所用时间
//        for (int i = 0; i < 4; i++) {//压缩十张图片
//            System.out.println("输入的图片大小：" + mypic.getPicSize("e:/filein/"+i+".jpg") / 1024 + "KB");
//            int start = (int) System.currentTimeMillis(); // 开始时间
//            mypic.compressPic("e:/filein/", "e:/fileout/", i+".jpg", "r1" + i + ".jpg", 600, 600, true);
//            int end = (int) System.currentTimeMillis(); // 结束时间
//            int re = end - start; // 但图片生成处理时间
//            count += re;
//            System.out.println("第" + (i + 1) + "张图片压缩处理使用了: " + re + "毫秒");
//            System.out.println("输出的图片大小：" + mypic.getPicSize("e:/fileout/r1" + i + ".jpg") / 1024 + "KB");
//        }
//        System.out.println("总共用了：" + count + "毫秒");
    }
}
