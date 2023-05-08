package com.example.kltn.services.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.kltn.utils.FileUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Configuration
public class CloudinaryUpload {
    @Value("${cloudinary.cloud_name}")
    String cloudName;
    @Value("${cloudinary.api_key}")
    String apiKey;
    @Value("${cloudinary.api_secret}")
    String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true));
        return cloudinary;
    }

    public String getPublicId(String urlImage) {
        int temp1 = urlImage.lastIndexOf(".");
        int temp2 = urlImage.lastIndexOf("/");
        return urlImage.substring(temp2 + 1, temp1);
    }

    public String uploadImage(MultipartFile file, String urlDestroy) throws IOException {
        Map params = ObjectUtils.asMap(
                "resource_type", "auto",
                "folder", "tgdd"
        );
        Map map = cloudinary().uploader().upload(FileUtil.convertMultiPartToFile(file), params);
        if (urlDestroy != null) {
            deleteImage(urlDestroy);
        }

        return map.get("secure_url").toString();
    }

    public void deleteImage(String urlImage) throws IOException {
        cloudinary().uploader().destroy("tgdd/" + getPublicId(urlImage)
                , ObjectUtils.asMap("resource_type", "image"));
    }

    public List<String> uploadImages(List<MultipartFile> files){
        var imgs = new ArrayList<String>();
        files.forEach(file -> {
            try {
                imgs.add(uploadImage(file,null));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return imgs;
    }

}
