package com.spring.jwt.utils;

import lombok.SneakyThrows;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

public class ImageCompressionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ImageCompressionUtil.class);

    private static final int TARGET_WIDTH = 800;
    private static final int TARGET_HEIGHT = 800;
    private static final float INITIAL_QUALITY = 0.9f;
    private static final float QUALITY_STEP = 0.1f;
    private static final long TARGET_SIZE_KB = 100;

    public static byte[] compressImage(byte[] originalImageBytes) throws IOException {
        if (originalImageBytes == null || originalImageBytes.length == 0) {
            logger.warn("Empty image data received for compression");
            return null;
        }

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(originalImageBytes);
            BufferedImage originalImage = ImageIO.read(inputStream);

            if (originalImage == null) {
                logger.error("Could not read image data, returned null BufferedImage");
                throw new IllegalArgumentException("Invalid image data");
            }

            logger.debug("Original image dimensions: {}x{}", originalImage.getWidth(), originalImage.getHeight());
            BufferedImage resizedImage = resizeImage(originalImage, TARGET_WIDTH, TARGET_HEIGHT);

            if (resizedImage == null) {
                logger.error("Image resizing failed");
                throw new IOException("Failed to resize image");
            }

            byte[] result = compressToTargetSize(resizedImage, TARGET_SIZE_KB);

            if (result == null || result.length == 0) {
                logger.error("Image compression returned empty result");
                throw new IOException("Failed to compress image");
            }

            logger.debug("Successfully compressed image from {} bytes to {} bytes",
                    originalImageBytes.length, result.length);

            return result;
        } catch (Exception e) {
            logger.error("Error during image compression: {}", e.getMessage(), e);
            throw new IOException("Error compressing image: " + e.getMessage(), e);
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        try {
            if (originalImage == null) {
                logger.error("Null image passed to resize function");
                return null;
            }

            if (originalImage.getWidth() <= 0 || originalImage.getHeight() <= 0) {
                logger.error("Invalid image dimensions: {}x{}", originalImage.getWidth(), originalImage.getHeight());
                return null;
            }

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            double ratio = (double) width / height;

            int newWidth = targetWidth;
            int newHeight = targetHeight;

            if (width > height) {
                newHeight = (int) (newWidth / ratio);
            } else {
                newWidth = (int) (newHeight * ratio);
            }

            logger.debug("Resizing image to {}x{}", newWidth, newHeight);

            return Thumbnails.of(originalImage)
                    .size(newWidth, newHeight)
                    .asBufferedImage();
        } catch (IOException e) {
            logger.error("Failed to resize image: {}", e.getMessage(), e);
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error resizing image: {}", e.getMessage(), e);
            return null;
        }
    }

    private static byte[] compressToTargetSize(BufferedImage image, long targetSizeKB) throws IOException {
        if (image == null) {
            logger.error("Null image passed to compress function");
            return null;
        }

        float quality = INITIAL_QUALITY;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            while (quality >= 0.1f) {
                outputStream.reset();

                // Check if we can get a JPG writer
                Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
                if (!writers.hasNext()) {
                    logger.error("No JPEG image writer available");
                    // Fallback to PNG if JPEG not available
                    ImageIO.write(image, "png", outputStream);
                    return outputStream.toByteArray();
                }

                ImageWriter writer = writers.next();
                ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
                writer.setOutput(ios);

                ImageWriteParam param = writer.getDefaultWriteParam();
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality);

                writer.write(null, new IIOImage(image, null, null), param);
                writer.dispose();
                ios.close();

                long sizeInKB = outputStream.size() / 1024;
                logger.debug("Compressed image to {} KB with quality {}", sizeInKB, quality);

                if (sizeInKB <= targetSizeKB) {
                    break;
                }

                quality -= QUALITY_STEP;
            }

            return outputStream.toByteArray();
        } catch (Exception e) {
            logger.error("Error during compression: {}", e.getMessage(), e);

            // Fallback: try to save as PNG if JPEG compression fails
            try {
                outputStream.reset();
                ImageIO.write(image, "png", outputStream);
                return outputStream.toByteArray();
            } catch (Exception fallbackException) {
                logger.error("Fallback to PNG also failed: {}", fallbackException.getMessage());
                throw new IOException("Image compression failed", e);
            }
        }
    }

    public static CompletableFuture<byte[]> compressImageAsync(byte[] originalImageBytes) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return compressImage(originalImageBytes);
            } catch (IOException e) {
                logger.error("Async image compression failed: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to compress image", e);
            }
        });
    }
}