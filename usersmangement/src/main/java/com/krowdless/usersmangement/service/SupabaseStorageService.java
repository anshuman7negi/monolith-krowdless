package com.krowdless.usersmangement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SupabaseStorageService {

    @Value("${SUPABASE_URL}")
    private String supabaseUrl;

    @Value("${SUPABASE_BUCKET}")
    private String bucket;

    @Value("${SUPABASE_SERVICE_KEY}")
    private String serviceRoleKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String uploadUserProfile(Long userId, MultipartFile file) {

        try {
            String filePath = "users/user_" + userId +
                    "/profile_" + System.currentTimeMillis() + ".jpg";

            String uploadUrl = supabaseUrl +
                    "/storage/v1/object/" + bucket + "/" + filePath;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + serviceRoleKey);
            headers.set("apikey", serviceRoleKey);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            HttpEntity<byte[]> request =
                    new HttpEntity<>(file.getBytes(), headers);

            restTemplate.exchange(
                    uploadUrl,
                    HttpMethod.PUT,
                    request,
                    String.class
            );

            return supabaseUrl +
                    "/storage/v1/object/public/" +
                    bucket + "/" + filePath;

        } catch (Exception e) {
            throw new RuntimeException("Supabase image upload failed", e);
        }
    }
}
