package com.dt.core.tool.lang;

import com.dt.core.tool.util.support.LangKit;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.util.Iterator;

/**
 * @author: lank
 * @date: 2020年6月3日 下午5:01:20
 * @Description:
 */
public class ImageUtil {

    /**
     * 对一个图像进行旋转
     *
     * @param image  图像
     * @param degree 旋转角度, 90 为顺时针九十度， -90 为逆时针九十度
     * @return 旋转后得图像对象
     */
    public static BufferedImage rotate(BufferedImage image, int degree) {
        int iw = image.getWidth();// 原始图象的宽度
        int ih = image.getHeight();// 原始图象的高度
        int w = 0;
        int h = 0;
        int x = 0;
        int y = 0;
        degree = degree % 360;
        if (degree < 0)
            degree = 360 + degree;// 将角度转换到0-360度之间
        double ang = degree * 0.0174532925;// 将角度转为弧度

        /**
         * 确定旋转后的图象的高度和宽度
         */

        if (degree == 180 || degree == 0 || degree == 360) {
            w = iw;
            h = ih;
        } else if (degree == 90 || degree == 270) {
            w = ih;
            h = iw;
        } else {
            int d = iw + ih;
            w = (int) (d * Math.abs(Math.cos(ang)));
            h = (int) (d * Math.abs(Math.sin(ang)));
        }

        x = (w / 2) - (iw / 2);// 确定原点坐标
        y = (h / 2) - (ih / 2);
        BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
        Graphics2D gs = rotatedImage.createGraphics();
        gs.fillRect(0, 0, w, h);// 以给定颜色绘制旋转后图片的背景
        AffineTransform at = new AffineTransform();
        at.rotate(ang, w / 2, h / 2);// 旋转图象
        at.translate(x, y);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        op.filter(image, rotatedImage);
        image = rotatedImage;
        return image;
    }

    /**
     * 将一个图片文件读入内存
     *
     * @param img 图片文件
     * @return 图片对象
     */
    public static BufferedImage read(Object img) {
        try {
            if (img instanceof BufferedImage) {
                return (BufferedImage) img;
            }
            if (img instanceof CharSequence) {
                return ImageIO.read(new File(img.toString()));
            }
            if (img instanceof File)
                return ImageIO.read((File) img);

            if (img instanceof URL)
                img = ((URL) img).openStream();

            throw LangKit.makeThrow("Unkown img info!! --> " + img);
        } catch (IOException e) {
            try {
                InputStream in = null;
                if (img instanceof File)
                    in = new FileInputStream((File) img);
                else if (img instanceof URL)
                    in = ((URL) img).openStream();
                else if (img instanceof InputStream)
                    in = (InputStream) img;
                if (in != null)
                    return readJpeg(in);
            } catch (IOException e2) {
                e2.fillInStackTrace();
            }
            return null;
            // throw Lang.wrapThrow(e);
        }
    }

    /**
     * 尝试读取JPEG文件的高级方法,可读取32位的jpeg文件
     * <p/>
     * 来自: http://stackoverflow.com/questions/2408613/problem-reading-jpeg-image-
     * using-imageio-readfile-file
     */
    private static BufferedImage readJpeg(InputStream in) throws IOException {
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("JPEG");
        ImageReader reader = null;
        while (readers.hasNext()) {
            reader = readers.next();
            if (reader.canReadRaster()) {
                break;
            }
        }
        if (reader == null)
            return null;
        try {
            ImageInputStream input = ImageIO.createImageInputStream(in);
            reader.setInput(input);
            // Read the image raster
            Raster raster = reader.readRaster(0, null);
            BufferedImage image = createJPEG4(raster);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            writeJpeg(image, out, 1);
            out.flush();
            return read(new ByteArrayInputStream(out.toByteArray()));
        } finally {
            try {
                reader.dispose();
            } catch (Throwable e) {
            }
        }
    }

    /**
     * 写入一个 JPG 图像
     *
     * @param im        图像对象
     * @param targetJpg 目标输出 JPG 图像文件
     * @param quality   质量 0.1f ~ 1.0f
     */
    public static void writeJpeg(RenderedImage im, Object targetJpg, float quality) {
        ImageWriter writer = null;
        try {
            writer = ImageIO.getImageWritersBySuffix("jpg").next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
            ImageOutputStream os = ImageIO.createImageOutputStream(targetJpg);
            writer.setOutput(os);
            writer.write(null, new IIOImage(im, null, null), param);
            os.flush();
            os.close();
        } catch (IOException e) {
            throw LangKit.wrapThrow(e);
        } finally {
            if (writer != null) {
                try {
                    writer.dispose();
                } catch (Throwable e) {
                }
            }
        }
    }

    /**
     * Java's ImageIO can't process 4-component images and Java2D can't apply
     * AffineTransformOp either, so convert raster data to RGB. Technique due to
     * MArk Stephens. Free for any use.
     */
    private static BufferedImage createJPEG4(Raster raster) {
        int w = raster.getWidth();
        int h = raster.getHeight();
        byte[] rgb = new byte[w * h * 3];

        float[] Y = raster.getSamples(0, 0, w, h, 0, (float[]) null);
        float[] Cb = raster.getSamples(0, 0, w, h, 1, (float[]) null);
        float[] Cr = raster.getSamples(0, 0, w, h, 2, (float[]) null);
        float[] K = raster.getSamples(0, 0, w, h, 3, (float[]) null);

        for (int i = 0, imax = Y.length, base = 0; i < imax; i++, base += 3) {
            float k = 220 - K[i], y = 255 - Y[i], cb = 255 - Cb[i], cr = 255 - Cr[i];

            double val = y + 1.402 * (cr - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);

            val = y - 0.34414 * (cb - 128) - 0.71414 * (cr - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base + 1] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);

            val = y + 1.772 * (cb - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base + 2] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);
        }

        raster = Raster.createInterleavedRaster(new DataBufferByte(rgb, rgb.length), w, h, w * 3, 3,
                new int[]{0, 1, 2}, null);

        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        return new BufferedImage(cm, (WritableRaster) raster, true, null);
    }

}
