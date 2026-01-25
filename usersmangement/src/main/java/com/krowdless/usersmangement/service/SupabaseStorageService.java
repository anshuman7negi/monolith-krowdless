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

    public void deleteUserProfile(String imageUrl) {

    if (imageUrl == null || imageUrl.isBlank()) {
        return;
    }

    try {
        // imageUrl example:
        // https://xyz.supabase.co/storage/v1/object/public/profile-images/users/user_1/profile_123.jpg

        String publicPrefix = "/storage/v1/object/public/" + bucket + "/";
        int index = imageUrl.indexOf(publicPrefix);

        if (index == -1) {
            return;
        }

        String filePath = imageUrl.substring(index + publicPrefix.length());

        String deleteUrl = supabaseUrl +
                "/storage/v1/object/" + bucket + "/" + filePath;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + serviceRoleKey);
        headers.set("apikey", serviceRoleKey);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        restTemplate.exchange(
                deleteUrl,
                HttpMethod.DELETE,
                request,
                String.class
        );

    } catch (Exception e) {
        // ⚠️ delete fail hone pe app crash nahi karna
        System.err.println("Failed to delete old profile image: " + e.getMessage());
    }
}

// upload destination draft image

public String uploadDestinationDraftImage(
        Long draftId,
        MultipartFile file,
        int sortOrder
) {
    try {
        String ext = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf('.'));

        String filePath =
                "drafts/draft_" + draftId +
                "/img_" + sortOrder + "_" +
                System.currentTimeMillis() + ext;

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
        throw new RuntimeException("Destination image upload failed", e);
    }
}

public void deleteDestinationDraftImage(String imageUrl) {

    if (imageUrl == null || imageUrl.isBlank()) return;

    try {
        String publicPrefix = "/storage/v1/object/public/" + bucket + "/";
        int index = imageUrl.indexOf(publicPrefix);

        if (index == -1) return;

        String filePath =
                imageUrl.substring(index + publicPrefix.length());

        String deleteUrl = supabaseUrl +
                "/storage/v1/object/" + bucket + "/" + filePath;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + serviceRoleKey);
        headers.set("apikey", serviceRoleKey);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        restTemplate.exchange(
                deleteUrl,
                HttpMethod.DELETE,
                request,
                String.class
        );

    } catch (Exception e) {
        System.err.println("Failed to delete destination image: " + e.getMessage());
    }
}




public String uploadStayDraftMedia(
        Long draftId,
        MultipartFile file,
        String mediaType
) {
    try {
        String ext = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf('.'));

        String filePath =
                "stay_drafts/draft_" + draftId + "/" +
                mediaType.toLowerCase() + "_" +
                System.currentTimeMillis() + ext;

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
        throw new RuntimeException("Stay draft media upload failed", e);
    }
}




}
