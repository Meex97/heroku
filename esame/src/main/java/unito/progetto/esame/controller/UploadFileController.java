package unito.progetto.esame.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import unito.progetto.esame.repository.FileRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UploadFileController {

    @Autowired
    FileRepository fileRepository;

    /*
     * MultipartFile Upload
     */
    @PostMapping("/file/upload")
    public String uploadMultipartFile(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("File uploaded successfully! -> filename = " + file.getOriginalFilename());
            // save file to PostgreSQL
           /* FileModel filemode = new FileModel(file.getOriginalFilename(), file.getContentType(), file.getBytes());
            fileRepository.save(filemode);*/
            return "File uploaded successfully! -> filename = " + file.getOriginalFilename();
        } catch (  Exception e) {
            return "FAIL! Maybe You had uploaded the file before or the file's size > 500KB";
        }
    }
}
