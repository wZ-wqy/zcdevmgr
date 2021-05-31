package com.dt.core.tool.util;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SmartImageScalr {
    private static String[] CROPS = {"TL", "TC", "TM", "TR", "RC", "RM", "RB", "BC", "BM", "BL", "LM", "LC", "MC"};
    private BufferedImage source;
    private int height;
    private int width;
    private Scalr.Mode fitType;
    private String crop;

    public SmartImageScalr(int width, int height, String fitType, String crop, BufferedImage source) {
        this.source = source;
        init(width, height, fitType, crop, this.source.getWidth(), this.source.getHeight());
    }

    public SmartImageScalr(int width, int height, String fitType, String crop, double origWidth, double origHeight) {
        init(width, height, fitType, crop, origWidth, origHeight);
    }

    private static Rectangle getCropRectangle(String crop, int width, int height, int imageWidth, int imageHeight) {
        Rectangle r = new Rectangle(0, 0, width, height);
        int dw = imageWidth - width;
        int dh = imageHeight - height;
        if (crop.equals("TL")) {
            r.x = 0;
            r.y = 0;
        } else if (crop.equals("TC") || crop.equals("TM")) {
            r.x = dw / 2;
            r.y = 0;
        } else if (crop.equals("TR")) {
            r.x = dw;
            r.y = 0;
        } else if (crop.equals("RC") || crop.equals("RM")) {
            r.x = dw;
            r.y = dh / 2;
        } else if (crop.equals("RB")) {
            r.x = dw;
            r.y = dh;
        } else if (crop.equals("BC") || crop.equals("BM")) {
            r.x = dw / 2;
            r.y = dh;
        } else if (crop.equals("BL")) {
            r.x = 0;
            r.y = dh;
        } else if (crop.equals("LM") || crop.equals("LC")) {
            r.x = 0;
            r.y = dh / 2;
        } else if (crop.equals("MC")) {
            r.x = dw / 2;
            r.y = dh / 2;
        }
        return r;
    }

    private static String checkCropType(String type) {
        if (type == null)
            return null;
        type = type.trim();
        if (type.length() != 2)
            return null;
        String type2 = "" + type.charAt(1) + type.charAt(0) + "";
        for (String c : CROPS) {
            if (c.equals(type) || type2.equals(c))
                return c;
        }
        return null;
    }



    public void init(int width, int height, String fitType, String crop, double origWidth, double origHeight) {
        this.width = width;
        this.height = height;
        this.crop = checkCropType(crop);
        if (fitType == null || (!fitType.equals("width") && !fitType.equals("height")))
            fitType = "width";
        if (fitType.equals("width")) {
            this.fitType = Scalr.Mode.FIT_TO_WIDTH;
            if (width <= 0) {
                (new Exception("当指定适应宽度，但宽度值却未指定")).printStackTrace();
            }
        } else if (fitType.equals("height")) {
            this.fitType = Scalr.Mode.FIT_TO_HEIGHT;
            if (height <= 0) {
                (new Exception("当指定适应宽度，但宽度值却未指定")).printStackTrace();
            }
        }
        int thumbWidth = 0;
        int thumbHeight = 0;
        if (this.fitType == Scalr.Mode.FIT_TO_WIDTH) {
            thumbWidth = width;
            thumbHeight = (int) ((origHeight / origWidth) * thumbWidth);
            if (thumbHeight < height) {
                this.fitType = Scalr.Mode.FIT_TO_HEIGHT;
                if (height <= 0) {
                    (new Exception("需要同时指定高度值才能操作")).printStackTrace();
                }
                return;
            }
        } else if (this.fitType == Scalr.Mode.FIT_TO_HEIGHT) {
            thumbHeight = height;
            thumbWidth = (int) ((origWidth / origHeight) * thumbHeight);
            if (thumbWidth < width) {
                this.fitType = Scalr.Mode.FIT_TO_WIDTH;
                if (width <= 0) {
                    (new Exception("需要同时指定宽度值才能操作")).printStackTrace();
                }
                return;
            }
        }
    }

    public String getFileName(File srcFile, String format) {
        String thumbFileName = srcFile.getName();
        if (fitType == Scalr.Mode.FIT_TO_WIDTH) {
            thumbFileName += "_fw" + width;
        } else if (fitType == Scalr.Mode.FIT_TO_HEIGHT) {
            thumbFileName += "_fh" + height;
        }
        if (crop != null && crop.length() > 0) {
            thumbFileName += "cr" + crop.toLowerCase();
        }
        if (format == null)
            format = "jpg";
        thumbFileName += "." + format;
        return thumbFileName;
    }

    public BufferedImage scaleAndCrop() throws IOException {
        return scaleAndCrop(this.source);
    }

    public BufferedImage scaleAndCrop(BufferedImage source) throws IOException {
        this.source = source;
        BufferedImage thumb = null;
        int origWidth = source.getWidth();
        int origHeight = source.getHeight();
        // 如果尺寸和原始图片尺寸不一样，调整尺寸
        if (width != origWidth || height != origHeight) {
            // 需要预先计算resize后的尺寸，在crop前反复resize存在性能问题
            thumb = Scalr.resize(source, Scalr.Method.QUALITY, fitType, width, height, Scalr.OP_ANTIALIAS);
        } else // 否则不调整尺寸
        {
            thumb = source;
        }
        if (crop != null) {
            Rectangle r = null;
            if (this.fitType == Scalr.Mode.FIT_TO_WIDTH) {
                if (height > 0 && thumb.getHeight() < height) {
                    thumb = Scalr.resize(source, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_HEIGHT, width, height,
                            Scalr.OP_ANTIALIAS);
                    r = getCropRectangle(crop, width, thumb.getHeight(), thumb.getWidth(), thumb.getHeight());
                } else {
                    r = getCropRectangle(crop, thumb.getWidth(), height, thumb.getWidth(), thumb.getHeight());
                }
            }
            if (this.fitType == Scalr.Mode.FIT_TO_HEIGHT) {
                if (width > 0 && thumb.getWidth() < width) {
                    thumb = Scalr.resize(source, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, width, height,
                            Scalr.OP_ANTIALIAS);
                    r = getCropRectangle(crop, thumb.getWidth(), height, thumb.getWidth(), thumb.getHeight());
                } else {
                    r = getCropRectangle(crop, width, thumb.getHeight(), thumb.getWidth(), thumb.getHeight());
                }
            }
            thumb = Scalr.crop(thumb, r.x, r.y, r.width, r.height, Scalr.OP_ANTIALIAS);
        }
        return thumb;
    }
}
