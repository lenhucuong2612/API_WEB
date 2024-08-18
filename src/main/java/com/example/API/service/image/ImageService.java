package com.example.API.service.image;

import com.example.API.dto.ImageDto;
import com.example.API.exception.ResourceNotFoundException;
import com.example.API.model.Image;
import com.example.API.model.Product;
import com.example.API.repository.ImageRepository;
import com.example.API.service.product.IProductService;
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
        return imageRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No image found with id: "+id));
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,()->{
            throw new ResourceNotFoundException("No image not found with id: "+id);
        });
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
        Product product=productService.getProductById(productId);
        List<ImageDto> savedImageDtos=new ArrayList<>();
        for(MultipartFile file: files){
            try{
                Image image=new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownload="/api/v1/images/image/download/";
                String downloadUrl=buildDownload+image.getId();
                image.setDownloadUrl(downloadUrl);
                imageRepository.save(image);
                Image savedImage=imageRepository.save(image);

                savedImage.setDownloadUrl(buildDownload+savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto=new ImageDto();
                imageDto.setImageId(savedImage.getId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setDownLoadUrl(savedImage.getDownloadUrl());
                savedImageDtos.add(imageDto);
            }catch(IOException | SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDtos;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image=getImageById(imageId);
        try{
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        }catch(IOException | SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
