package com.example.API.service.image;

import com.example.API.dto.ImageDto;
import com.example.API.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImage(Long id);
    List<ImageDto> saveImage(List<MultipartFile> files , Long productId);
    void updateImage(MultipartFile file, Long imageId);
}
