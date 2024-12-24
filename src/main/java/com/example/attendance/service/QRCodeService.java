// package com.example.attendance.service;

// import org.springframework.stereotype.Service;
// import com.google.zxing.BarcodeFormat;
// import com.google.zxing.WriterException;
// import com.google.zxing.client.j2se.MatrixToImageWriter;
// import com.google.zxing.common.BitMatrix;
// import com.google.zxing.qrcode.QRCodeWriter;

// import java.io.ByteArrayOutputStream;
// import java.io.IOException;

// @Service
// public class QRCodeService {
//     public byte[] generateQRCode(String data) throws WriterException, IOException {
//         QRCodeWriter qrCodeWriter = new QRCodeWriter();
//         BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
//         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//         MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
//         return outputStream.toByteArray();
//     }
// }
