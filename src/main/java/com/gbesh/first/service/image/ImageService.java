package com.gbesh.first.service.image;

import com.gbesh.first.dto.ImageDTO;
import com.gbesh.first.exceptions.ResourceNotFoundException;
import com.gbesh.first.model.Image;
import com.gbesh.first.model.Product;
import com.gbesh.first.repository.ImageRepository;
import com.gbesh.first.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{
    private final ImageRepository imageRepository;
    private final IProductService productService;


    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found with id " + id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,
                () -> {
            throw new ResourceNotFoundException("Image not found with id " + id);
        });
    }

    @Override
    public List<ImageDTO> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDTO> savedImageDTOS = new ArrayList<>();
        for (MultipartFile file: files){
            try{
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "images/image/download/";
                String downloadUrl =  buildDownloadUrl + image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedImage = imageRepository.save(image);

                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
                imageRepository.save(savedImage);

                ImageDTO imageDTO = new ImageDTO();
                imageDTO.setImageId(savedImage.getId());
                imageDTO.setImageName(savedImage.getFileName());
                imageDTO.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDTOS.add(imageDTO);

            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return savedImageDTOS;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try{
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);

        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
