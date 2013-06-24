/**
 * 
 */
package summ.framework.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.Toolkit; //这部分需要图形界面
//以下三个需要图形界面
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment; 
//

import java.awt.Image;
import java.awt.Panel;
import java.awt.RenderingHints;



import java.awt.Transparency;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

//import javax.swing.ImageIcon;


/**
 * 
 * 测试Image的utils功能
 * 似乎本来适合客户端用，必须存在OS的图形界面。
 * 
 * @author wfeng007
 * @date 2011年6月25日
 */
public class ImageUtils  {
	
	//图形环境以及设备初始化 
	//这部分可能在没有图形界面的OS中不可用
	final public static GraphicsEnvironment environment = GraphicsEnvironment
			.getLocalGraphicsEnvironment();
	final public static GraphicsDevice graphicsDevice = environment
			.getDefaultScreenDevice();
	final public static GraphicsConfiguration graphicsConfiguration = graphicsDevice
			.getDefaultConfiguration();
	//
	
	//
	//独立的版本信息获取
	//系统版本信息
	//
	public static String javaVersion=null;
	public static int tmpmajor=-1;
	final public static int JAVA_13=0,JAVA_14=1,JAVA_15=2,JAVA_16=4,JAVA_17=5;
	static{
		
		javaVersion = System.getProperty("java.version");
		if (javaVersion.indexOf("1.4.") != -1) {
			tmpmajor = JAVA_14;
		} else if (javaVersion.indexOf("1.5.") != -1) {
			tmpmajor = JAVA_15;
		} else if (javaVersion.indexOf("1.6.") != -1) {
			tmpmajor = JAVA_16;
		} else if (javaVersion.indexOf("1.7.") != -1) {
			tmpmajor = JAVA_17;
		} else {
			tmpmajor = JAVA_13;
		}
		System.out.println("JVM Version:"+tmpmajor);
	}
	
	

	
	/**
	 * 获取image
	 * @param filename
	 * @return
	 */
	public static Image loadImage(String filename) {
		//lazy load 不能马上获取其图片信息
		Image img = Toolkit.getDefaultToolkit().getImage(filename); 
		//下面方法立刻初始化了image 否则第一次 访问会返回-1 因为Toolkit.getDefaultToolkit()是异步load信息的。
		//FIXME 但是这种方式并不很好
//		img = (new ImageIcon(img)).getImage();
		//或者先试用一次
//		img.getWidth(null);
		//或者使用同步信息等待
		ImageUtils.waitImage(100,img);
		
		//使用createImage直接初始化
		//
		return img;
	}
	
	/**
	 * 延迟加载的image,
	 * 等待以使其同步信息。
	 * 
	 * 用于image初始化
	 * 
	 * @param delay 等待时间
	 * @param image
	 */
	private static void waitImage(int delay, Image image) {
		try {
			for (int i = 0; i < delay; i++) {
				if (Toolkit.getDefaultToolkit().prepareImage(image, -1, -1, null)) {
					System.out.println("image syn-ok. delay count:"+i);
					return;
				}
				Thread.sleep(delay);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//
	
	/**
	 * 生成一个空白的 BufferImage
	 * 
	 * @param w 宽
	 * @param h 高
	 * @param flag 是否使用ARGB 而不是普通的RGB 即是否为有Alpha通道
	 * @return
	 */
	final static public BufferedImage createBufferedImage(int w, int h, boolean flag) {
		if (flag) {
			if (tmpmajor>JAVA_15) { //检测是否为1.6及以上
				System.out.println("w:"+w+" h:"+h);
				return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE); //底层使用int形表示argb
			} else {
				//1.5以前需要使用图形环境获取
				return ImageUtils.graphicsConfiguration
						.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
			}
		} else { //不考虑用new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)
			return ImageUtils.graphicsConfiguration.createCompatibleImage(w,
					h, Transparency.BITMASK);
		}
	}
	
	
	
	
	/**
	 * FIXME 不应该单独写方法
	 * 将指定Image中的内容重新写到一个已有的BufferedImage中
	 * target 以及 source大小应该一致 target长宽均大于source也可
	 * source内容直接从target的0,0位置画
	 * 
	 * @param target
	 * @param source
	 * @return
	 */
	private static void drawToBufferedImage(BufferedImage target, Image source) {
		Graphics2D g = target.createGraphics();
		g.drawImage(source, 0, 0, null); //目标 坐标0 0 
		g.dispose();
//		return target;
	}
	
	/**
	 * 根据一个Image生成一个新的 BufferedImage (ARGB)
	 * @param source
	 * @return
	 */
	public static BufferedImage createBufferedImage(Image source) {
//		System.out.println("w:"+source.getWidth(null)+" h:"+source.getHeight(null));
		BufferedImage bi=ImageUtils.createBufferedImage(source.getWidth(null),source.getHeight(null),true);
		ImageUtils.drawToBufferedImage(bi,source);
		return bi;
	}
	
	/**
	 * 默认使用int 形式的ARGB
	 * Image需要用PixelGrabber抓取像素
	 * 如果是BufferedImage则其实可以用raster
	 * @param sourceImage
	 * @return
	 */
	public static int[] grabPixels(Image sourceImage){
		
		//切块后
		//后续处理图形像素点
		//处理首先需要获取像素矩阵
		PixelGrabber pgr = new PixelGrabber(sourceImage, 0,
				0, -1, -1, true); // -1 -1 表示整个  强制转为rgb 如果是灰度图则返回的像素是byte[]
		try {
			pgr.grabPixels();//从image中抓取pixel到PixelGrabber //需要较长时间
		} catch (InterruptedException ex) {//这个.....
			ex.printStackTrace();
			return null;
		}
		//FIXME 可能是byte[]
		int pixels[] = (int[]) pgr.getPixels(); //将PixelGrabber中的pixel返回出来 也可能是byte[]取决于image //TYPE_INT_ARGB_PRE
		
		return pixels;

	}
	
	
	//过滤像素点 将白色或rgb=247,0,255的颜色定义为空像素
	private static void filtePixels(int[] source){ //设置为透明
		for (int i = 0; i < source.length; i++) {
//			LColor color = LColor.getLColor(pixels[i]);
			int[] rgbs=getARGBs(source[i]);
			if ((rgbs[0] == 247 && rgbs[1] == 0 && rgbs[2] == 255)
					|| (rgbs[0] == 255 && rgbs[1] == 255 && rgbs[2] == 255)) {
				source[i] = 0;
			}
		}
	}
	//切分pixel为颜色及Alpha通道
	private static int[] getARGBs(final int pixel) {
		int[] rgbs = new int[4];
		rgbs[0] = (pixel >> 16) & 0xff; // Red
		rgbs[1] = (pixel >> 8) & 0xff; // Green
		rgbs[2] = (pixel) & 0xff; // Blue
		rgbs[3] = (pixel >> 24) & 0xff; // Alpha
		return rgbs;
	}
	
	/**
	 * 根据像素阵列 创建图像
	 * @param width
	 * @param height
	 * @param pixels
	 * @return
	 */
	public static Image createImage(int width,int height,int[] pixels){
		//形成根据rgb以及大小形成画图器
		ImageProducer ip = new MemoryImageSource(width, height, pixels, 0, width);
		
		//重新生成image //最后重新形成Image对象
		Image img = Toolkit.getDefaultToolkit().createImage(ip);
		
		return img;
	}
	
	/**
	 * 剪切指定图像 
	 * 生成新的bufferedImage
	 * 
	 * @param sourceImage 源image
	 * @param objectWidth 剪切目标宽
	 * @param objectHeight 剪切目标高
	 * @param x1 源左上角坐标的 x
	 * @param y1  源左上角坐标的 y
	 * @param x2 源右下角坐标的 x
	 * @param y2 源右下角坐标的 y
	 * @return
	 */
	public static BufferedImage clipImage(final Image sourceImage,
			int objectWidth, int objectHeight, int x1, int y1, int x2, int y2) {
		BufferedImage buffer = createBufferedImage(objectWidth,
				objectHeight, true);
		Graphics g = buffer.getGraphics();
		Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.drawImage(sourceImage, 0, 0, objectWidth, objectHeight, x1, y1,
				x2, y2, null);
		graphics2D.dispose();
		graphics2D = null;
		return buffer;
	}
	
	/**
	 * 剪切指定图像
	 * 
	 * @param image
	 * @param objectWidth 区域宽
	 * @param objectHeight 区域高
	 * @param x
	 * @param y
	 * @return
	 */
	public static BufferedImage clipImage(final Image image,
			int objectWidth, int objectHeight, int x, int y) {
		BufferedImage buffer = createBufferedImage(objectWidth,
				objectHeight, true);
		Graphics2D graphics2D = buffer.createGraphics();
		graphics2D.drawImage(image, 0, 0, objectWidth, objectHeight, x, y, x
				+ objectWidth, objectHeight + y, null);
		graphics2D.dispose();
		graphics2D = null;
		return buffer;
	}
	
	/**
	 * 切分并过滤像素
	 * @param image
	 * @param splitedWidth 切分后每块宽度
	 * @param splitedHeight 切分后每块高度
	 * @param isFiltrate 是否过滤像素
	 * @return
	 */
	public static Image[] splitImages(Image image, int splitedWidth, int splitedHeight,
			boolean isFiltrate) {
//		//
//		int colLength = image.getWidth(null) / splitedWidth;
//		int rowLength = image.getHeight(null) / splitedHeight;
//		int l = colLength * rowLength;
//		//
//		Image[] imgs = new Image[l]; int index = 0;
//		for (int y = 0; y < rowLength; y++) {
//			for (int x = 0; x < colLength; x++) {
//				imgs[index]=clipImage(image,splitedWidth,splitedHeight,(x * splitedWidth),(y * splitedHeight),splitedWidth
//						+ (x * splitedWidth),splitedHeight + (y * splitedHeight));
//				//切块后
//				//后续处理图形像素点
//				//处理首先需要获取像素矩阵
//				int[] pixels=grabPixels(imgs[index]);
//				
//				if(isFiltrate){
//					//这里过滤只是一个样式
//					filtePixels(pixels);
//				}
//				
//				//处理pixels后重新构建image
//				imgs[index]=createImage(imgs[index].getWidth(null),imgs[index].getHeight(null),pixels);
//				
//				//下一个索引
//				index++;
//			}
//		}
//		return imgs;
		
		//
		Image[][] image2d=splitImages2D(image, splitedWidth, splitedHeight, isFiltrate);
		int l = image2d.length * image2d[0].length; 
		Image[] imgs = new Image[l]; int index = 0;
		for (int i = 0; i < image2d.length; i++) {
			for (int j = 0; j < image2d[i].length; j++) {
				imgs[index]=image2d[i][j];
				index++;
			}
		}
		return imgs;
		
	}
	
	/**
	 * 
	 * 切分并过滤像素
	 * 形成矩阵形状的图片集
	 * 
	 * @param image
	 * @param splitedWidth
	 * @param splitedHeight
	 * @param isFiltrate
	 * @return
	 */
	public static Image[][] splitImages2D(Image image, int splitedWidth, int splitedHeight,
			boolean isFiltrate) {
		//
		int colLength = image.getWidth(null) / splitedWidth;
		int rowLength = image.getHeight(null) / splitedHeight;
		//
		Image[][] imgs = new Image[rowLength][colLength]; 
		for (int y = 0; y < rowLength; y++) {
			for (int x = 0; x < colLength; x++) {
				imgs[y][x]=clipImage(image,splitedWidth,splitedHeight,(x * splitedWidth),(y * splitedHeight),splitedWidth
						+ (x * splitedWidth),splitedHeight + (y * splitedHeight));
				//切块后
				//后续处理图形像素点
				//处理首先需要获取像素矩阵
				int[] pixels=grabPixels(imgs[y][x]);
				
				if(isFiltrate){
					//这里过滤只是一个样式
					filtePixels(pixels);
				}
				
				//处理pixels后重新构建image
				imgs[y][x]=createImage(imgs[y][x].getWidth(null),imgs[y][x].getHeight(null),pixels);
				
			}
		}
		return imgs;
	}
	
	
	
	
	/**
	 * 
	 * 变更指定Image大小
	 * 
	 * @param image 源图片
	 * @param w 新宽度
	 * @param h 新高度
	 * @return 变更后目标图片
	 */
	public static Image getResize(Image image, int w, int h) {
		
		if (image.getWidth(null) == w && image.getHeight(null) == h) {
			return image;
		}
		BufferedImage result = null;
		Graphics2D graphics2d;
		//
		//创建一个新的BufferedImage
		//创建其画笔
		//并设置画笔  缩放原则为双线性插值法
		//
		(graphics2d = (result = createBufferedImage(w, h, true))
				.createGraphics()).setRenderingHint(
				RenderingHints.KEY_INTERPOLATION, //缩放原则
				RenderingHints.VALUE_INTERPOLATION_BILINEAR); //双线性插值法
		
		//在目标图上(result) 勾画出原图图形image
		//参数：原图图形image, 目标位置左上角坐标x,目标位置左上角坐标y,目标位置右下角坐标x,目标位置左右下角坐标y
		// （切出原图形位置）原图图形image中左上角x，（切出原图形位置）原图图形image中左上角y  
		//（切出原图形位置）原图图形image中右下角x，（切出原图形位置）原图图形image中右下角y,
		// 触发处理器
		graphics2d.drawImage(image, 0, 0, w, h, 0, 0, image.getWidth(null),
				image.getHeight(null), null);
		//
		
		// 销毁画笔
		graphics2d.dispose();
		//
		graphics2d = null;
		
		return result;
	}

	
	/**
	 * 透明度设定
	 * 
	 * @param g 需要被设定的画笔
	 * @param d 透明度0.0 - 1.0 之间
	 */
	final static public void setAlpha(Graphics g, double d) {
		AlphaComposite alphacomposite = AlphaComposite
				.getInstance(3, (float) d);
		((Graphics2D) g).setComposite(alphacomposite);
	}




}
