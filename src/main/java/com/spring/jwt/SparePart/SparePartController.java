package com.spring.jwt.SparePart;

import com.spring.jwt.SparePartTransaction.CreateSparePartTransactionDto;
import com.spring.jwt.SparePartTransaction.SparePartTransactionDto;
import com.spring.jwt.utils.BaseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;

@RequestMapping("/sparePartManagement")
@RestController
@RequiredArgsConstructor
public class SparePartController {

    private final SparePartService sparePartService;

//    @GetMapping("/photo/{id}")
//    public ResponseEntity<byte[]> getPhoto(@PathVariable Integer id) {
//        byte[] image = sparePartService.getPhotoById(id);
//        if (image == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_JPEG)
//                .body(image);
//    }

    @GetMapping("/getPartById/{id}")
    public ResponseEntity<SparePartDto> getSparePartById(@PathVariable Integer id) {
        SparePartDto sparePart = sparePartService.getSparePartById(id);
        return (sparePart != null) ? ResponseEntity.ok(sparePart) : ResponseEntity.notFound().build();
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllSpareParts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        try {
            long startRequestTime = System.currentTimeMillis();
            PaginatedResponse<SparePartDto> response = sparePartService.getAllSpareParts(page, size);
            long requestDuration = System.currentTimeMillis() - startRequestTime;

            // Generate an ETag based on data content to enable browser caching
            String etag = String.format("W/\"%d-%d-%d\"", page, size, response.hashCode());

            // Set aggressive caching headers for maximum performance
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(Duration.ofSeconds(30))
                            .mustRevalidate()
                            .cachePublic())
                    .eTag(etag)
                    .header("X-Response-Time-Ms", String.valueOf(requestDuration))
                    .header("X-Total-Count", String.valueOf(response.getTotalElements()))
                    .header("Access-Control-Expose-Headers", "X-Total-Count, X-Response-Time-Ms")
                    .body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to retrieve spare parts: " + e.getMessage());
        }
    }


    @PostMapping("/addPart")
    public ResponseEntity<BaseResponseDTO> addPart(
            @RequestParam("partName") String partName,
            @RequestParam("description") String description,
            @RequestParam("manufacturer") String manufacturer,
            @RequestParam("price") Long price,
            @RequestParam("partNumber") String partNumber,
            @RequestParam("photos") List<MultipartFile> photos,
            @RequestParam("sGST") Integer sGST,
            @RequestParam("cGST") Integer cGST,
            @RequestParam("totalGST") Integer totalGST,
            @RequestParam("buyingPrice") Integer buyingPrice)
    {

        BaseResponseDTO response = sparePartService.addPart(
                partName, description, manufacturer, price, partNumber, photos, sGST, cGST, totalGST, buyingPrice);

        return ResponseEntity.ok(response);
    }



    @PatchMapping("/update/{id}")
    public ResponseEntity<SparePartDto> updateSparePart(
            @PathVariable Integer id,
            @RequestParam(required = false) String partName,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String manufacturer,
            @RequestParam(required = false) Long price,
            @RequestParam(required = false) String partNumber,
            @RequestParam(required = false) List<MultipartFile> photos,
            @RequestParam(required = false) Integer sGST,
            @RequestParam(required = false) Integer cGST,
            @RequestParam(required = false) Integer totalGST,
            @RequestParam(required = false) Integer buyingPrice,
            @RequestParam(required = false) String vendor) {

        SparePartDto updatedPart = sparePartService.updatePart(
                id, partName, description, manufacturer, price, partNumber, photos, sGST, cGST, totalGST, buyingPrice,vendor);

        return ResponseEntity.ok(updatedPart);
    }

    @PreAuthorize("permitAll")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponseDTO> deleteSparePart(
            @PathVariable Integer id,
            @RequestParam(required = false) Integer photoIndex) {
        BaseResponseDTO response = sparePartService.deleteSparePartById(id, photoIndex);
        return ResponseEntity.ok(response);
    }

}
