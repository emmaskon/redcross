/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.EnvironmentVariables;

/**
 *
 * @author unknown
 */
public class VoucherDownload extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String filename = request.getParameter("filename");
        
        String file_path = EnvironmentVariables.STORAGE_DIRECTORY + File.separator + filename;
        File download_file = new File(file_path);
        FileInputStream fis = new FileInputStream(download_file);

        ServletContext context = getServletContext();

        // gets MIME type of the file
        String mime_type = context.getMimeType(file_path);
        if (mime_type == null) {
            // set to binary type if MIME mapping not found
            mime_type = "application/octet-stream";
        }

        response.setContentType(mime_type);
        response.setContentLength((int) download_file.length());

        // forces download
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename*=UTF-8''" + URLEncoder.encode(download_file.getName(), "UTF-8");
        response.setHeader(headerKey, headerValue);

        // obtains response's output stream
        OutputStream outStream = response.getOutputStream();

        byte[] buffer = new byte[1024 * 10];
        int bytesRead = -1;

        while ((bytesRead = fis.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }

        fis.close();
        outStream.close();
    }
}
