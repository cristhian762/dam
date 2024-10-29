package com.mattiuzzi.fontana.cristhian.lista.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class FileHelper {
    // Salvar o arquivo do URI para o armazenamento interno
    public static String saveFileFromUri(Context context, Intent data) {
        Uri fileUri = data.getData(); // Obtém o URI do Intent
        if (fileUri == null) return null;

        String fileName = generateRandomImageName(context, fileUri);
        File outputFile = new File(context.getFilesDir(), fileName); // Cria o arquivo de destino

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
            OutputStream outputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            return outputFile.getAbsolutePath(); // Retorna o caminho do arquivo salvo
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String generateRandomImageName(Context context, Uri fileUri) {
        // Obter o tipo MIME do URI
        String mimeType = context.getContentResolver().getType(fileUri);
        String extension = getFileExtensionFromMimeType(mimeType);

        // Gerar um nome único com a extensão correta
        return UUID.randomUUID().toString() + (extension != null ? "." + extension : "");
    }

    // Método auxiliar para obter a extensão a partir do tipo MIME
    private static String getFileExtensionFromMimeType(String mimeType) {
        if (mimeType == null) return null;
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
    }
}
