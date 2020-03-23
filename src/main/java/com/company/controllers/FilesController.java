package com.company.controllers;

import com.company.models.FileInfo;
import com.company.repositories.FileRepository;
import com.company.services.FileUploader;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Controller
public class FilesController {
    @Autowired
    private FileUploader fileUploader;
    @Autowired
    private FileRepository fileRepository;

    @RequestMapping(value = "/uploadfiles", method = RequestMethod.GET)
    public ModelAndView getUploadPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("uploadfile");
        return modelAndView;
    }

    @RequestMapping(value = "/uploadfiles", method = RequestMethod.POST)
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        fileUploader.uploadAndSaveToDb(multipartFile);
        return null;
    }

    @RequestMapping(value = "/getfile/{file-name:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    byte[] getFile(HttpServletResponse resp, @PathVariable("file-name") String fileName) {
        Optional<FileInfo> fileInfo = fileRepository.getByStorageName(fileName);
        if (fileInfo.isPresent()) {
            try (InputStream inputStream = new FileInputStream(new File(fileInfo.get().getPath()))) {
                resp.setContentType(fileInfo.get().getType());
                resp.setHeader("Content-Disposition", "attachment; filename=\" " + fileInfo.get().getOriginalFileName() + "\"");
                IOUtils.copy(inputStream, resp.getOutputStream());
                resp.getOutputStream().flush();
                resp.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}