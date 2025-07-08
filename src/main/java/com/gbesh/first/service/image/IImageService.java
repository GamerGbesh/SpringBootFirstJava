package com.gbesh.first.service.image;

import com.gbesh.first.dto.ImageDTO;
import com.gbesh.first.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDTO> saveImages(List<MultipartFile> file, Long productId);
    void updateImage(MultipartFile file, Long imageId);
}
