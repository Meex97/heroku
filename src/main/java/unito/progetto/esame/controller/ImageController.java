package unito.progetto.esame.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unito.progetto.esame.model.ImageModel;
import unito.progetto.esame.model.ProductInfo;
import unito.progetto.esame.repository.ImageRepository;
import unito.progetto.esame.repository.ProductInfoRepository;
import unito.progetto.esame.service.ProductService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "image")
public class ImageController {
    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ProductInfoRepository productInfoRepository;

    @Autowired
    ProductService productService;

    @Transactional
    @PostMapping("/upload")
    public BodyBuilder uplaodImage(@RequestParam("image") MultipartFile file) throws IOException {
        ImageModel img = new ImageModel(file.getOriginalFilename(), file.getContentType(),
                compressBytes(file.getBytes()));
        // imageRepository.save(img);

        ProductInfo prod = productInfoRepository.findByProductId(img.getName());
        // System.out.println(img.getName());
        prod.setProductimage(decompressBytes(compressBytes(file.getBytes())));
        productService.update(prod);

        System.out.println(img.getPicByte());
        System.out.println(prod.getProductimage());

        return ResponseEntity.status(HttpStatus.OK);
    }


    @Transactional
    @GetMapping(path = { "/get/{imageName}" })
    public ImageModel getImage(@PathVariable("imageName") String imageName) throws IOException {
        final Optional<ImageModel> retrievedImage = imageRepository.findByName(imageName);
        ImageModel img = new ImageModel(retrievedImage.get().getName(), retrievedImage.get().getType(),
                decompressBytes(retrievedImage.get().getPicByte()));
        return img;
    }

    // compress the image bytes before storing it in the database
    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        return outputStream.toByteArray();
    }

    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
        } catch (DataFormatException e) {
        }
        return outputStream.toByteArray();
    }
}
