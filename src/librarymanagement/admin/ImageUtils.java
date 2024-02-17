/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
/**
 *
 * @author imman
 */
public class ImageUtils {
    public static byte[] convertImageToBytes(File imageFile) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(imageFile)) {
            byte[] bytes = new byte[(int) imageFile.length()];
            fileInputStream.read(bytes);
            return bytes;
        }
    }
}
